<template>
  <el-container style="height:100vh">
    <el-aside width="200px">
      <el-menu router :default-active="$route.path">
        <el-menu-item index="/project">项目管理</el-menu-item>
      </el-menu>

      <el-menu router :default-active="$route.path">
        <el-menu-item index="/person">人员管理</el-menu-item>
      </el-menu>

      <el-menu router :default-active="$route.path">
        <el-menu-item index="/paper">论文管理</el-menu-item>
      </el-menu>

      <el-menu router :default-active="$route.path">
        <el-menu-item index="/book">著作管理</el-menu-item>
      </el-menu>

      <el-menu router :default-active="$route.path">
        <el-menu-item index="/award">获奖管理</el-menu-item>
      </el-menu>

      <el-menu router :default-active="$route.path">
        <el-menu-item index="/user">用户管理</el-menu-item>
      </el-menu>

      <el-menu router :default-active="$route.path">
        <el-menu-item index="/password">密码修改</el-menu-item>
      </el-menu>

    </el-aside>

    <el-container>
      <el-header style="display:flex;align-items:center;justify-content:space-between">
        <div style="font-weight:600">高校科研管理系统</div>
        <div style="font-size:12px;color:#666">Vue3 + SpringBoot</div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>
<script setup>
</script>