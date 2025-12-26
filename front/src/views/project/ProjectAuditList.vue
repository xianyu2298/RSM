<template>
  <el-card>
    <template #header>
      <div style="display:flex; justify-content: space-between; align-items: center">
        <span>审核中的项目</span>
        <el-button type="primary" @click="load">刷新</el-button>
      </div>
    </template>

    <el-table :data="rows" border>
      <el-table-column prop="projectCode" label="项目编号" width="140" />
      <el-table-column prop="name" label="项目名称" min-width="200" />
      <el-table-column prop="statusCode" label="审核状态" width="120">
        <template #default="{ row }">
          <el-tag v-if="row.statusCode === 'AUDIT_PENDING'" type="warning">待审核</el-tag>
          <el-tag v-else-if="row.statusCode === 'AUDIT_REJECTED'" type="danger">未通过</el-tag>
          <el-tag v-else type="info">{{ row.statusCode }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="审核备注/项目备注" min-width="200" />
      <el-table-column prop="startDate" label="开始" width="110" />
      <el-table-column prop="endDate" label="结束" width="110" />
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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { projectPage } from '../../api/project'
import { useRouter } from 'vue-router'
import { getCurrentUser } from '../../utils/http'

const router = useRouter()
const currentUser = getCurrentUser()

const rows = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)

async function load() {
  // 查询当前用户的所有项目
  const data = await projectPage({
    page: 1,
    size: 1000, // 获取所有以进行前端过滤，或者分两次查
    personId: currentUser.id
  })
  
  // 过滤出待审核和未通过的
  rows.value = (data.records || []).filter(p => 
    p.statusCode === 'AUDIT_PENDING' || p.statusCode === 'AUDIT_REJECTED'
  )
  total.value = rows.value.length
}



onMounted(load)
</script>
