<template>
  <el-card>
    <div style="display:flex;gap:8px;flex-wrap:wrap;align-items:center">
      <el-input v-model="q.username" placeholder="用户名" style="width:200px" />
      <el-input v-model="q.realName" placeholder="姓名" style="width:200px" />
      <el-select v-model="q.role" placeholder="角色" clearable style="width:150px">
        <el-option label="管理员" value="ADMIN" />
        <el-option label="普通用户" value="USER" />
      </el-select>
      <el-select v-model="q.status" placeholder="状态" clearable style="width:150px">
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>

      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
      <el-button v-if="isAdmin" type="success" @click="openAdd">新增用户</el-button>
    </div>

    <el-table :data="rows" style="margin-top:12px" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="160" />
      <el-table-column prop="realName" label="姓名" width="160" />
      <el-table-column prop="role" label="角色" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.status === 1" type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="260">
        <template #default="{ row }">
          <el-button v-if="isAdmin" size="small" @click="openEdit(row)">编辑</el-button>
          <el-button v-if="isAdmin" size="small" type="warning" @click="openResetPwd(row)">
            重置密码
          </el-button>
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
  <el-dialog v-model="dlg.visible" :title="dlg.title" width="560px">
    <el-form :model="form" label-width="90px">
      <el-form-item label="用户名">
        <el-input v-model="form.username" :disabled="!!form.id" placeholder="登录用户名" />
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="form.realName" />
      </el-form-item>
      <el-form-item label="角色">
        <el-select v-model="form.role" style="width:100%">
          <el-option label="管理员" value="ADMIN" />
          <el-option label="普通用户" value="USER" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.status" style="width:100%">
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="!form.id" label="初始密码">
        <el-input v-model="form.initPwd" placeholder="不填则默认 123456" show-password />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dlg.visible=false">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-dialog>

  <!-- 重置密码 -->
  <el-dialog v-model="pwdDlg.visible" title="重置密码" width="480px">
    <div style="margin-bottom:8px">用户：{{ pwdDlg.username }}</div>
    <el-input v-model="pwdDlg.newPwd" placeholder="新密码" show-password />
    <template #footer>
      <el-button @click="pwdDlg.visible=false">取消</el-button>
      <el-button type="primary" @click="doResetPwd">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { userPage, userAdd, userUpdate, userDelete, resetPassword } from '../../api/user'
import { getCurrentUser, isAdminUser } from '../../utils/http'

const currentUser = getCurrentUser()
const isAdmin = isAdminUser(currentUser)

const q = reactive({ username: '', realName: '', role: '', status: '' })
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref([])

const dlg = reactive({ visible: false, title: '' })
const form = reactive({
  id: null,
  username: '',
  realName: '',
  role: 'USER',
  status: 1,
  initPwd: ''
})

const pwdDlg = reactive({ visible: false, userId: null, username: '', newPwd: '' })

async function load() {
  const data = await userPage({
    page: page.value,
    size: size.value,
    username: q.username || undefined,
    realName: q.realName || undefined,
    role: q.role || undefined,
    status: q.status === '' ? undefined : q.status
  })
  total.value = data.total
  rows.value = data.records
}

function reset() {
  q.username = ''
  q.realName = ''
  q.role = ''
  q.status = ''
  page.value = 1
  load()
}

function openAdd() {
  dlg.title = '新增用户'
  dlg.visible = true
  Object.assign(form, {
    id: null,
    username: '',
    realName: '',
    role: 'USER',
    status: 1,
    initPwd: ''
  })
}

function openEdit(row) {
  dlg.title = '编辑用户'
  dlg.visible = true
  Object.assign(form, {
    id: row.id,
    username: row.username,
    realName: row.realName,
    role: row.role,
    status: row.status,
    initPwd: ''
  })
}

async function save() {
  if (!form.username) return ElMessage.warning('用户名必填')
  if (!form.realName) return ElMessage.warning('姓名必填')

  if (form.id) {
    await userUpdate(form)
  } else {
    // 这里把 initPwd 一起传给后端（你后端如果不接收也没关系，会忽略）
    await userAdd({
      username: form.username,
      realName: form.realName,
      role: form.role,
      status: form.status,
      initPwd: form.initPwd
    })
  }

  ElMessage.success('保存成功')
  dlg.visible = false
  load()
}

async function remove(id) {
  await userDelete(id)
  ElMessage.success('删除成功')
  load()
}


function openResetPwd(row) {
  pwdDlg.visible = true
  pwdDlg.userId = row.id
  pwdDlg.username = row.username
  pwdDlg.newPwd = ''
}

async function doResetPwd() {
  if (!pwdDlg.newPwd) return ElMessage.warning('请输入新密码')
  await resetPassword({ userId: pwdDlg.userId, newPwd: pwdDlg.newPwd })
  ElMessage.success('重置成功')
  pwdDlg.visible = false
}

onMounted(load)
</script>
