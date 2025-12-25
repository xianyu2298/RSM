<template>
  <el-card>
    <div style="display:flex;gap:8px;flex-wrap:wrap;align-items:center">
      <el-input v-model="q.title" placeholder="论文标题" style="width:220px" />
      <el-input v-model="q.personId" placeholder="作者人员ID" style="width:140px" />
      <el-select v-model="q.indexCode" placeholder="检索源" style="width:180px" clearable>
        <el-option v-for="i in indexItems" :key="i.itemCode" :label="i.itemName" :value="i.itemCode" />
      </el-select>

      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
      <el-button v-if="isAdmin" type="success" @click="openAdd">新增论文</el-button>
    </div>

    <el-table :data="rows" style="margin-top:12px" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="personId" label="人员ID" width="90" />


      <el-table-column label="作者" width="160">
        <template #default="{ row }">
          {{ row.empNo }} - {{ row.personName }}
        </template>
      </el-table-column>

      <el-table-column prop="title" label="标题" min-width="260" />
      <el-table-column prop="journal" label="期刊/会议" width="160" />
      <el-table-column prop="indexCode" label="检索源" width="120" />
      <el-table-column prop="publishDate" label="发表日期" width="120" />
      <el-table-column prop="doi" label="DOI" width="160" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button v-if="isAdmin" size="small" @click="openEdit(row)">编辑</el-button>
          <el-popconfirm v-if="isAdmin" title="确定删除？" @confirm="remove(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
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
      <el-form-item label="人员ID">
        <el-input-number v-model="form.personId" :min="1" />
      </el-form-item>

      <el-form-item label="论文标题">
        <el-input v-model="form.title" />
      </el-form-item>

      <el-form-item label="期刊/会议">
        <el-input v-model="form.journal" />
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
import { getCurrentUser, isAdminUser } from '../../utils/http'

const currentUser = getCurrentUser()
const isAdmin = isAdminUser(currentUser)

const q = reactive({ title: '', personId: '', indexCode: '' })
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref([])

const indexItems = ref([])

const dlg = reactive({ visible: false, title: '' })
const form = reactive({
  id: null,
  personId: 1,
  title: '',
  journal: '',
  indexCode: '',
  publishDate: '',
  doi: '',
  remark: ''
})

async function loadDict() {
  // 论文检索源字典：EI/SCI/核心/一般
  indexItems.value = await dictItems('PAPER_INDEX_SOURCE')
}

async function load() {
  const data = await paperPage({
    page: page.value,
    size: size.value,
    title: q.title || undefined,
    personId: q.personId || undefined,
    indexCode: q.indexCode || undefined
  })
  total.value = data.total
  rows.value = data.records
}

function reset() {
  q.title = ''
  q.personId = ''
  q.indexCode = ''
  page.value = 1
  load()
}

function openAdd() {
  dlg.title = '新增论文'
  dlg.visible = true
  Object.assign(form, {
    id: null,
    personId: 1,
    title: '',
    journal: '',
    indexCode: '',
    publishDate: '',
    doi: '',
    remark: ''
  })
}

function openEdit(row) {
  dlg.title = '编辑论文'
  dlg.visible = true
  Object.assign(form, row)
}

async function save() {
  if (!form.personId) return ElMessage.warning('人员ID必填')
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

onMounted(async () => {
  await loadDict()
  await load()
})
</script>
