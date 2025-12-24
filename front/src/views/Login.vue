<template>
  <div class="login-wrap">
    <el-card class="login-card">
      <h2 style="margin:0 0 16px 0">高校科研管理系统</h2>

      <el-form :model="form" label-width="70px" @keyup.enter="submit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>

        <div style="display:flex;justify-content:flex-end;gap:8px">
          <el-button @click="reset">重置</el-button>
          <el-button type="primary" @click="submit">登录</el-button>
        </div>

        <div style="margin-top:10px;color:#888;font-size:12px">
          暂未接入 Security，先用普通接口登录。
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { login } from '../api/auth'

const router = useRouter()

const form = reactive({
  username: '',
  password: ''
})

function reset() {
  form.username = ''
  form.password = ''
}

async function submit() {
  if (!form.username) return ElMessage.warning('请输入用户名')
  if (!form.password) return ElMessage.warning('请输入密码')

  // ✅ 调用后端登录
  const user = await login({ username: form.username, password: form.password })

  // 保存登录态
  localStorage.setItem('token', user.token || 'demo-token')
  localStorage.setItem('user', JSON.stringify(user))

  ElMessage.success('登录成功')
  router.replace('/project')
}
</script>

<style scoped>
.login-wrap{
  height: 100vh;
  display:flex;
  justify-content:center;
  align-items:center;
  background:#f5f6f7;
}
.login-card{
  width: 420px;
}
</style>
