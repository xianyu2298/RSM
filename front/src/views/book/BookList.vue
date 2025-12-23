<template>
  <el-card>
    <div style="display:flex;gap:8px;flex-wrap:wrap;align-items:center">
      <el-input v-model="q.name" placeholder="著作名称" style="width:240px" />
      <el-input v-model="q.personId" placeholder="作者人员ID" style="width:140px" />
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
      <el-button type="success" @click="openAdd">新增著作</el-button>
    </div>

    <el-table :data="rows" style="margin-top:12px" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="personId" label="人员ID" width="90" />
      <el-table-column prop="name" label="著作名称" min-width="240" />
      <el-table-column prop="publisher" label="出版社" width="180" />
      <el-table-column prop="publishDate" label="出版日期" width="120" />
      <el-table-column prop="isbn" label="ISBN" width="170" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="remove(row.id)">
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
      <el-form-item label="人员ID">
        <el-input-number v-model="form.personId" :min="1" />
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

const q = reactive({ name: '', personId: '' })
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref([])

const dlg = reactive({ visible: false, title: '' })
const form = reactive({
  id: null,
  personId: 1,
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
    name: q.name || undefined,
    personId: q.personId || undefined
  })
  total.value = data.total
  rows.value = data.records
}

function reset() {
  q.name = ''
  q.personId = ''
  page.value = 1
  load()
}

function openAdd() {
  dlg.title = '新增著作'
  dlg.visible = true
  Object.assign(form, {
    id: null,
    personId: 1,
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
}

async function save() {
  if (!form.personId) return ElMessage.warning('人员ID必填')
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

onMounted(load)
</script>
