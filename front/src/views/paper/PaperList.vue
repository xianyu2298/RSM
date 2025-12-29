<template>
  <el-card>
    <div style="display:flex;gap:8px;flex-wrap:wrap;align-items:center">
      <el-input v-model="q.title" placeholder="论文标题" style="width:220px" />
      <el-select v-model="q.indexCode" placeholder="检索源" style="width:180px" clearable>
        <el-option v-for="i in indexItems" :key="i.itemCode" :label="i.itemName" :value="i.itemCode" />
      </el-select>

      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
      <el-button type="success" @click="openAdd">新增论文</el-button>
    </div>

    <el-table :data="rows" style="margin-top:12px" border>
      <el-table-column label="作者" width="160">
        <template #default="{ row }">
          {{ row.empNo }} - {{ row.personName }}
        </template>
      </el-table-column>

      <el-table-column prop="title" label="标题" min-width="260" />
      <el-table-column prop="journal" label="期刊/会议" width="160">
        <template #default="{ row }">
          {{ dictName('PAPER_JOURNAL', row.journal) }}
        </template>
      </el-table-column>
      <el-table-column prop="indexCode" label="检索源" width="120">
        <template #default="{ row }">
          {{ dictName('PAPER_INDEX_SOURCE', row.indexCode) }}
        </template>
      </el-table-column>
      <el-table-column prop="publishDate" label="发表日期" width="120" />
      <el-table-column prop="doi" label="DOI" width="160" />
      <el-table-column label="附件" width="100">
        <template #default="{ row }">
          <el-link v-if="row.filePath" type="primary" :href="`/api/file/download/${row.filePath}`" target="_blank">下载</el-link>
          <span v-else style="color:#999">无</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <div v-if="isAdmin || row.personId === currentUser.id">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-popconfirm title="确定删除？" @confirm="remove(row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </div>
          <span v-else style="color: #909399; font-size: 12px;">无权限</span>
        </template>
      </el-table-column>
    </el-table>

    <div style="display:flex;justify-content:flex-end;margin-top:12px">
      <el-pagination
          v-model:current-page="page"
          v-model:page-size="size"
          layout="total, prev, pager, next, sizes"
          :total="total"
          @current-change="load"
          @size-change="load"
      />
    </div>
  </el-card>

  <!-- 新增/编辑 -->
  <el-dialog v-model="dlg.visible" :title="dlg.title" width="720px">
    <el-form :model="form" label-width="90px">
      <el-form-item label="作者">
        <template v-if="isAdmin">
          <el-select
              v-model="form.personId"
              filterable
              remote
              reserve-keyword
              placeholder="输入姓名或工号搜索作者"
              :remote-method="remoteSearchPerson"
              :loading="personLoading"
              style="width:100%"
          >
            <el-option
                v-for="p in personOptions"
                :key="p.id"
                :label="`${p.empNo} - ${p.name}`"
                :value="p.id"
            />
          </el-select>
        </template>
        <template v-else>
          <div>{{ currentUser.empNo }} - {{ currentUser.realName }}</div>
        </template>
      </el-form-item>

      <el-form-item label="论文标题">
        <el-input v-model="form.title" />
      </el-form-item>

      <el-form-item label="期刊/会议">
        <el-select v-model="form.journal" style="width:100%" clearable>
          <el-option v-for="i in journalItems" :key="i.itemCode" :label="i.itemName" :value="i.itemCode" />
        </el-select>
      </el-form-item>

      <el-form-item label="检索源">
        <el-select v-model="form.indexCode" style="width:100%">
          <el-option v-for="i in indexItems" :key="i.itemCode" :label="i.itemName" :value="i.itemCode" />
        </el-select>
      </el-form-item>

      <el-form-item label="发表日期">
        <el-date-picker v-model="form.publishDate" type="date" value-format="YYYY-MM-DD" />
      </el-form-item>

      <el-form-item label="DOI">
        <el-input v-model="form.doi" placeholder="例如：10.1000/demo" />
      </el-form-item>

      <el-form-item label="附件">
        <el-upload
          action="/api/file/upload"
          :on-success="handleUploadSuccess"
          :before-upload="beforeUpload"
          :limit="1"
          :file-list="fileList"
        >
          <el-button type="primary">点击上传</el-button>
          <template #tip>
            <div class="el-upload__tip" v-if="form.filePath">
              已上传: {{ form.filePath }}
            </div>
          </template>
        </el-upload>
      </el-form-item>

      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dlg.visible=false">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { dictItems } from '../../api/dict'
import { paperPage, paperAdd, paperUpdate, paperDelete } from '../../api/paper'
import { pagePerson } from '../../api/person'
import { getCurrentUser, isAdminUser } from '../../utils/http'

const currentUser = getCurrentUser()
const isAdmin = isAdminUser(currentUser)

const q = reactive({ title: '', indexCode: '' })
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref([])

const indexItems = ref([])
const journalItems = ref([])
const fileList = ref([])

const dlg = reactive({ visible: false, title: '' })
const form = reactive({
  id: null,
  personId: null,
  title: '',
  journal: '',
  indexCode: '',
  publishDate: '',
  doi: '',
  filePath: '',
  remark: ''
})

const personOptions = ref([])
const personLoading = ref(false)

function dictName(typeCode, itemCode) {
  if (!itemCode) return '-'
  let items = []
  if (typeCode === 'PAPER_INDEX_SOURCE') {
    items = indexItems.value || []
  } else if (typeCode === 'PAPER_JOURNAL') {
    items = journalItems.value || []
  }
  const it = items.find(i => i.itemCode === itemCode)
  return it ? it.itemName : itemCode
}

async function loadDict() {
  // 论文检索源字典：EI/SCI/核心/一般
  indexItems.value = await dictItems('PAPER_INDEX_SOURCE')
  // 期刊会议字典
  journalItems.value = await dictItems('PAPER_JOURNAL')
}

async function load() {
  const data = await paperPage({
    page: page.value,
    size: size.value,
    title: q.title || undefined,
    indexCode: q.indexCode || undefined
  })
  total.value = data.total
  rows.value = data.records
}

function reset() {
  q.title = ''
  q.indexCode = ''
  page.value = 1
  load()
}

function handleUploadSuccess(res) {
  if (res.code === 0) {
    form.filePath = res.data
    ElMessage.success('文件上传成功')
  } else {
    ElMessage.error('文件上传失败: ' + res.msg)
  }
}

function beforeUpload(file) {
  const isLt20M = file.size / 1024 / 1024 < 20
  if (!isLt20M) {
    ElMessage.error('上传文件大小不能超过 20MB!')
  }
  return isLt20M
}

function openAdd() {
  dlg.title = '新增论文'
  dlg.visible = true
  personOptions.value = []
  fileList.value = []
  Object.assign(form, {
    id: null,
    personId: isAdmin ? null : currentUser.id,
    title: '',
    journal: '',
    indexCode: '',
    publishDate: '',
    doi: '',
    filePath: '',
    remark: ''
  })
}

function openEdit(row) {
  dlg.title = '编辑论文'
  dlg.visible = true
  Object.assign(form, row)
  fileList.value = row.filePath ? [{ name: row.filePath, url: row.filePath }] : []
  personOptions.value = row.personId
    ? [{
      id: row.personId,
      empNo: row.empNo,
      name: row.personName
    }]
    : []
}

async function save() {
  if (!form.personId) return ElMessage.warning('请选择作者')
  if (!form.title) return ElMessage.warning('论文标题必填')
  if (!form.indexCode) return ElMessage.warning('请选择检索源')

  if (form.id) await paperUpdate(form)
  else await paperAdd(form)

  ElMessage.success('保存成功')
  dlg.visible = false
  load()
}

async function remove(id) {
  await paperDelete(id)
  ElMessage.success('删除成功')
  load()
}

async function remoteSearchPerson(keyword) {
  if (!keyword || keyword.trim() === '') {
    personOptions.value = []
    return
  }
  personLoading.value = true
  try {
    const r1 = await pagePerson({ page: 1, size: 10, name: keyword })
    const r2 = await pagePerson({ page: 1, size: 10, empNo: keyword })
    const map = new Map()
    ;(r1.records || []).forEach(p => map.set(p.id, p))
    ;(r2.records || []).forEach(p => map.set(p.id, p))
    personOptions.value = Array.from(map.values())
  } finally {
    personLoading.value = false
  }
}

onMounted(async () => {
  await loadDict()
  await load()
})
</script>
