<template>
  <el-container style="height:100vh">
    <!-- 左侧菜单 -->
    <el-aside width="200px">
      <el-menu router :default-active="$route.path">
        <el-menu-item index="/project">项目管理</el-menu-item>
        <el-menu-item index="/person">人员管理</el-menu-item>
        <el-menu-item index="/paper">论文管理</el-menu-item>
        <el-menu-item index="/book">著作管理</el-menu-item>
        <el-menu-item index="/award">获奖管理</el-menu-item>
        <el-menu-item v-if="isAdmin" index="/user">用户管理</el-menu-item>
        <el-menu-item v-if="isAdmin" index="/dict">字典管理</el-menu-item>
        <el-menu-item v-if="!isAdmin" index="/password">修改密码</el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧 -->
    <el-container>
      <!-- 顶部 -->
      <el-header class="header">
        <div>高校科研管理系统</div>

        <div class="right">
          <span class="user">
            {{ user.realName || user.username }}
          </span>
          <el-button size="small" type="danger" @click="logout">
            退出登录
          </el-button>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { getCurrentUser, isAdminUser } from '../utils/http'

const router = useRouter()
const user = getCurrentUser()
const isAdmin = isAdminUser(user)

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.replace('/login')
}
</script>

<style scoped>
.header{
  display:flex;
  justify-content:space-between;
  align-items:center;
  background:#fff;
  border-bottom:1px solid #eee;
}
.right{
  display:flex;
  align-items:center;
  gap:10px;
}
.user{
  color:#666;
}
</style>
