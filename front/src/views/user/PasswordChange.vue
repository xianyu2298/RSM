<template>
  <el-card style="max-width:520px;margin:0 auto">
    <h3 style="margin:0 0 12px 0">修改密码</h3>

    <el-form :model="form" label-width="90px">
      <el-form-item label="用户ID">
        <el-input-number
          v-if="isAdmin"
          v-model="form.userId"
          :min="1"
        />
        <el-input
          v-else
          v-model="form.userId"
          disabled
        />
      </el-form-item>

      <el-form-item v-if="!isAdmin" label="旧密码">
        <el-input v-model="form.oldPwd" show-password />
      </el-form-item>

      <el-form-item label="新密码">
        <el-input v-model="form.newPwd" show-password />
      </el-form-item>

      <el-form-item label="确认新密码">
        <el-input v-model="form.newPwd2" show-password />
      </el-form-item>
    </el-form>

    <div style="display:flex;justify-content:flex-end;gap:8px">
      <el-button @click="reset">重置</el-button>
      <el-button type="primary" @click="submit">提交</el-button>
    </div>

    <div style="margin-top:10px;color:#888;font-size:12px">
      提示：这里的旧密码/新密码都填“明文”，后端会做加密对比与保存。
    </div>
  </el-card>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { changePassword, resetPassword } from '../../api/user'
import { getCurrentUser, isAdminUser } from '../../utils/http'

const user = getCurrentUser()
const isAdmin = isAdminUser(user)

const form = reactive({
  userId: user && user.id ? user.id : null,
  oldPwd: '',
  newPwd: '',
  newPwd2: ''
})

function reset() {
  form.oldPwd = ''
  form.newPwd = ''
  form.newPwd2 = ''
}

async function submit() {
  if (!form.userId) return ElMessage.warning('userId 必填')
  if (!form.newPwd) return ElMessage.warning('新密码必填')
  if (form.newPwd !== form.newPwd2) return ElMessage.warning('两次新密码不一致')

  if (isAdmin) {
    await resetPassword({ userId: form.userId, newPwd: form.newPwd })
  } else {
    if (!form.oldPwd) return ElMessage.warning('旧密码必填')
    await changePassword({ userId: form.userId, oldPwd: form.oldPwd, newPwd: form.newPwd })
  }
  ElMessage.success('修改成功')
  reset()
}
</script>
