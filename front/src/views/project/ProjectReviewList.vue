<template>
  <el-card>
    <template #header>
      <div style="display:flex; justify-content: space-between; align-items: center">
        <span>项目审核（待处理）</span>
        <el-button type="primary" @click="load">刷新</el-button>
      </div>
    </template>

    <el-table :data="rows" border>
      <el-table-column prop="projectCode" label="项目编号" width="140" />
      <el-table-column prop="name" label="项目名称" min-width="200" />
      <el-table-column prop="leaderPersonId" label="申请人" width="150">
        <template #default="{ row }">
          {{ personMap[row.leaderPersonId] || row.leaderPersonId }}
        </template>
      </el-table-column>
      <el-table-column prop="startDate" label="开始" width="110" />
      <el-table-column prop="endDate" label="结束" width="110" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-popconfirm title="确定通过该项目申请吗？" @confirm="directAudit(row, 'PLANNING')">
            <template #reference>
              <el-button size="small" type="success">通过</el-button>
            </template>
          </el-popconfirm>
          <el-button size="small" type="danger" @click="openAudit(row, 'AUDIT_REJECTED')">驳回</el-button>
          <el-button size="small" @click="goDetail(row.id)">详情</el-button>
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

    <!-- 审核对话框 -->
    <el-dialog v-model="auditDlg.visible" :title="auditDlg.title" width="500px">
      <el-form :model="auditForm" label-width="80px">
        <el-form-item label="审核备注">
          <el-input v-model="auditForm.remark" type="textarea" placeholder="请输入审核意见（必填，特别是驳回时）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDlg.visible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { projectPage, projectAudit } from '../../api/project'
import { personGet } from '../../api/person'
import { useRouter } from 'vue-router'

const router = useRouter()
const rows = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)

const personMap = ref({})

const auditDlg = reactive({ visible: false, title: '', row: null, status: '' })
const auditForm = reactive({ remark: '' })

async function load() {
  const data = await projectPage({
    page: page.value,
    size: size.value,
    statusCode: 'AUDIT_PENDING'
  })
  rows.value = data.records
  total.value = data.total
  
  // 加载申请人姓名
  const ids = [...new Set(data.records.map(r => r.leaderPersonId).filter(id => id && !personMap.value[id]))]
  for (const id of ids) {
    try {
      const res = await personGet(id)
      if (res) {
        personMap.value[id] = res.name
      }
    } catch (e) {
      console.error('Failed to load person', id, e)
    }
  }
}

function openAudit(row, status) {
  auditDlg.row = row
  auditDlg.status = status
  auditDlg.title = status === 'PLANNING' ? '审核通过' : '审核驳回'
  auditForm.remark = row.remark || ''
  auditDlg.visible = true
}

async function submitAudit() {
  if (auditDlg.status === 'AUDIT_REJECTED' && !auditForm.remark) {
    return ElMessage.warning('驳回请填写理由')
  }
  
  await projectAudit(auditDlg.row.id, auditDlg.status, auditForm.remark)
  ElMessage.success('操作成功')
  auditDlg.visible = false
  load()
}

async function directAudit(row, status) {
  // 通过时不需要填备注，直接传空字符串或原有的备注
  await projectAudit(row.id, status, row.remark || '管理员审核通过')
  ElMessage.success('已通过该项目申请')
  load()
}

function goDetail(id) {
  router.push(`/project/${id}`)
}

onMounted(load)
</script>
