<template>
  <div class="page-container">
    <!-- 查询栏 -->
    <el-form :inline="true" :model="query">
      <el-form-item label="姓名">
        <el-input v-model="query.name" placeholder="请输入姓名" />
      </el-form-item>
      <el-form-item label="工号">
        <el-input v-model="query.empNo" placeholder="请输入工号" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="reset">重置</el-button>
        <el-button v-if="isAdmin" type="success" @click="openAdd">新增人员</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="list" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="empNo" label="工号" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="gender" label="性别" width="80" />
      <el-table-column prop="title" label="职称" />
      <el-table-column prop="department" label="部门" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="email" label="邮箱" />



      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button v-if="isAdmin" size="small" @click="openEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="primary" @click="goDetail(scope.row.id)">详情</el-button>
          <el-button v-if="isAdmin" size="small" type="danger" @click="remove(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>

    </el-table>

    <!-- 分页 -->
    <el-pagination
        background
        layout="total, prev, pager, next"
        :total="total"
        :page-size="query.size"
        v-model:current-page="query.page"
        @current-change="load"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="form.id ? '编辑人员' : '新增人员'" v-model="visible">
      <el-form :model="form" label-width="80px">
        <el-form-item label="工号">
          <el-input v-model="form.empNo" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="职称">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="部门">
          <el-input v-model="form.department" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { pagePerson, addPerson, updatePerson, deletePerson } from '../../api/person'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { getCurrentUser, isAdminUser } from '../../utils/http'

const router = useRouter()
const goDetail = (id) => router.push(`/person/${id}`)
const currentUser = getCurrentUser()
const isAdmin = isAdminUser(currentUser)
const query = reactive({
  page: 1,
  size: 10,
  name: '',
  empNo: ''
})

const list = ref([])
const total = ref(0)
const visible = ref(false)
const form = reactive({})

function load() {
  pagePerson(query).then(data => {
    list.value = data.records
    total.value = data.total
    query.empNo = ''
  })

}

function reset() {
  query.name = ''
  query.empNo = ''
  query.page = 1
  load()
}

function openAdd() {
  Object.assign(form, {})
  visible.value = true
}

function openEdit(row) {
  Object.assign(form, row)
  visible.value = true
}

function save() {
  const api = form.id ? updatePerson : addPerson
  api(form).then(() => {
    ElMessage.success('操作成功')
    visible.value = false
    load()
  })
}

function remove(id) {
  ElMessageBox.confirm('确定删除该人员吗？', '提示', { type: 'warning' })
      .then(() => {
        deletePerson(id).then(() => {
          ElMessage.success('删除成功')
          load()
        })
      })
}

onMounted(load)
</script>

<style scoped>
.page-container {
  padding: 20px;
}
</style>
