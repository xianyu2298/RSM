<template>
  <el-card>
    <div style="display:flex;gap:8px;flex-wrap:wrap;align-items:center">
      <el-input v-model="q.name" placeholder="著作名称" style="width:240px" />
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
      <el-button type="success" @click="openAdd">新增著作</el-button>
    </div>

    <el-table :data="rows" style="margin-top:12px" border>
      <el-table-column label="作者" width="160">
        <template #default="{ row }">
          {{ row.empNo }} - {{ row.personName }}
        </template>
      </el-table-column>

      <el-table-column prop="name" label="著作名称" min-width="240" />
      <el-table-column prop="publisher" label="出版社" width="180" />
      <el-table-column prop="publishDate" label="出版日期" width="120" />
      <el-table-column prop="isbn" label="ISBN" width="170" />
      <el-table-column prop="remark" label="备注" min-width="200" />
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

      <el-form-item label="著作名称">
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item label="出版社">
        <el-input v-model="form.publisher" />
      </el-form-item>

      <el-form-item label="出版日期">
        <el-date-picker v-model="form.publishDate" type="date" value-format="YYYY-MM-DD" />
      </el-form-item>

      <el-form-item label="ISBN">
        <el-input v-model="form.isbn" placeholder="例如：978-7-xxxx-xxxx-x" />
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
import { bookPage, bookAdd, bookUpdate, bookDelete } from '../../api/book'
import { pagePerson } from '../../api/person'
import { getCurrentUser, isAdminUser } from '../../utils/http'

const currentUser = getCurrentUser()
const isAdmin = isAdminUser(currentUser)

const q = reactive({ name: '' })
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref([])

const personOptions = ref([])
const personLoading = ref(false)

const dlg = reactive({ visible: false, title: '' })
const form = reactive({
  id: null,
  personId: null,
  name: '',
  publisher: '',
  publishDate: '',
  isbn: '',
  remark: ''
})

async function load() {
  const data = await bookPage({
    page: page.value,
    size: size.value,
    name: q.name || undefined
  })
  total.value = data.total
  rows.value = data.records
}

function reset() {
  q.name = ''
  page.value = 1
  load()
}

function openAdd() {
  dlg.title = '新增著作'
  dlg.visible = true
  personOptions.value = []
  Object.assign(form, {
    id: null,
    personId: isAdmin ? null : currentUser.id,
    name: '',
    publisher: '',
    publishDate: '',
    isbn: '',
    remark: ''
  })
}

function openEdit(row) {
  dlg.title = '编辑著作'
  dlg.visible = true
  Object.assign(form, row)
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
  if (!form.name) return ElMessage.warning('著作名称必填')

  if (form.id) await bookUpdate(form)
  else await bookAdd(form)

  ElMessage.success('保存成功')
  dlg.visible = false
  load()
}

async function remove(id) {
  await bookDelete(id)
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

onMounted(load)
</script>
