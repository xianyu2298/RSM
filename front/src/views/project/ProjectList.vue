<template>
  <el-card>
    <div style="display:flex;gap:8px;flex-wrap:wrap;align-items:center">
      <el-input v-model="q.name" placeholder="项目名称" style="width:200px" />
      <el-select v-model="q.natureCode" placeholder="项目性质" style="width:200px" clearable>
        <el-option v-for="i in natureItems" :key="i.itemCode" :label="i.itemName" :value="i.itemCode" />
      </el-select>
      <el-select v-model="q.scopeCode" placeholder="项目范围" style="width:200px" clearable>
        <el-option v-for="i in scopeItems" :key="i.itemCode" :label="i.itemName" :value="i.itemCode" />
      </el-select>

      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
      <el-button type="success" @click="openAdd">新增项目</el-button>
    </div>

    <el-table :data="rows" style="margin-top:12px" border>
      <el-table-column prop="projectCode" label="项目编号" width="140" />
      <el-table-column prop="name" label="项目名称" min-width="240" />
      <el-table-column prop="natureCode" label="性质" width="120">
        <template #default="{ row }">
          {{ dictName('PROJECT_NATURE', row.natureCode) }}
        </template>
      </el-table-column>
      <el-table-column prop="scopeCode" label="范围" width="120">
        <template #default="{ row }">
          {{ dictName('PROJECT_SCOPE', row.scopeCode) }}
        </template>
      </el-table-column>
      <el-table-column prop="startDate" label="开始" width="120" />
      <el-table-column prop="endDate" label="结束" width="120" />
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <div v-if="isAdmin || row.leaderPersonId === currentUser.id">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="primary" @click="goDetail(row.id)">详情</el-button>
            <el-popconfirm title="确定删除？" @confirm="remove(row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </div>
          <div v-else>
            <el-button size="small" type="primary" @click="goDetail(row.id)">详情</el-button>
            <span style="color: #909399; font-size: 12px; margin-left: 8px;">无权限</span>
          </div>
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

  <!-- 新增/编辑项目 -->
  <el-dialog v-model="dlg.visible" :title="dlg.title" width="700px">
    <el-form :model="form" label-width="100px">
      <el-form-item label="项目编号">
        <el-input v-model="form.projectCode" />
      </el-form-item>
      <el-form-item label="项目名称">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="项目性质">
        <el-select v-model="form.natureCode" style="width:100%">
          <el-option v-for="i in natureItems" :key="i.itemCode" :label="i.itemName" :value="i.itemCode" />
        </el-select>
      </el-form-item>
      <el-form-item label="项目范围">
        <el-select v-model="form.scopeCode" style="width:100%">
          <el-option v-for="i in scopeItems" :key="i.itemCode" :label="i.itemName" :value="i.itemCode" />
        </el-select>
      </el-form-item>
      <el-form-item label="起止日期">
        <el-date-picker
            v-model="dateRange"
            type="daterange"
            value-format="YYYY-MM-DD"
            range-separator="至"
            start-placeholder="开始"
            end-placeholder="结束"
            style="width:100%"
        />
      </el-form-item>
      <el-form-item label="负责人">
        <template v-if="isAdmin">
          <el-select
              v-model="form.leaderPersonId"
              filterable
              remote
              reserve-keyword
              placeholder="输入姓名或工号搜索负责人"
              :remote-method="remoteSearchLeader"
              :loading="leaderLoading"
              style="width:100%"
          >
            <el-option
                v-for="p in leaderOptions"
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
      <el-form-item label="预算">
        <el-input-number v-model="form.budget" :min="0" :step="1000" />
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
import { dictItems } from '../../api/dict'
import { projectPage, projectAdd, projectUpdate, projectDelete } from '../../api/project'
import { pagePerson, personGet } from '../../api/person'
import { useRouter } from 'vue-router'
import { getCurrentUser, isAdminUser } from '../../utils/http'
const router = useRouter()
function goDetail(id) {
  router.push(`/project/${id}`)
}
const currentUser = getCurrentUser()
const isAdmin = isAdminUser(currentUser)
const q = reactive({ name: '', natureCode: '', scopeCode: '' })
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref([])

const natureItems = ref([])
const scopeItems = ref([])

const leaderOptions = ref([])
const leaderLoading = ref(false)

function dictName(typeCode, itemCode) {
  if (!itemCode) return '-'
  let items = []
  if (typeCode === 'PROJECT_NATURE') {
    items = natureItems.value || []
  } else if (typeCode === 'PROJECT_SCOPE') {
    items = scopeItems.value || []
  }
  const hit = items.find(i => i.itemCode === itemCode)
  return hit ? hit.itemName : itemCode
}

const dlg = reactive({ visible: false, title: '' })
const form = reactive({
  id: null, projectCode: '', name: '',
  natureCode: '', scopeCode: '',
  startDate: '', endDate: '',
  leaderPersonId: null, budget: null, remark: ''
})
const dateRange = ref([])

async function loadDict() {
  natureItems.value = await dictItems('PROJECT_NATURE')
  scopeItems.value = await dictItems('PROJECT_SCOPE')
}

async function load() {
  const data = await projectPage({
    page: page.value,
    size: size.value,
    name: q.name || undefined,
    natureCode: q.natureCode || undefined,
    scopeCode: q.scopeCode || undefined
  })
  total.value = data.total
  rows.value = data.records
}

function reset() {
  q.name = ''; q.natureCode = ''; q.scopeCode = ''
  page.value = 1
  load()
}

function openAdd() {
  dlg.title = '新增项目'
  dlg.visible = true
  leaderOptions.value = []
  Object.assign(form, {
    id: null, projectCode: '', name: '',
    natureCode: '', scopeCode: '',
    startDate: '', endDate: '',
    leaderPersonId: isAdmin ? null : currentUser.id, budget: null, remark: ''
  })
  dateRange.value = []
}

async function openEdit(row) {
  dlg.title = '编辑项目'
  dlg.visible = true
  Object.assign(form, row)
  dateRange.value = [row.startDate || '', row.endDate || '']
  leaderOptions.value = []
  if (row.leaderPersonId) {
    try {
      const p = await personGet(row.leaderPersonId)
      if (p) {
        leaderOptions.value = [{
          id: p.id,
          empNo: p.empNo,
          name: p.name
        }]
      }
    } catch (e) {
    }
  }
}

async function save() {
  if (!form.name) return ElMessage.warning('项目名称必填')
  if (!form.natureCode) return ElMessage.warning('请选择项目性质')
  if (!form.scopeCode) return ElMessage.warning('请选择项目范围')
  if (dateRange.value?.length === 2) {
    form.startDate = dateRange.value[0]
    form.endDate = dateRange.value[1]
  }

  if (form.id) await projectUpdate(form)
  else await projectAdd(form)

  ElMessage.success('保存成功')
  dlg.visible = false
  load()
}

async function remove(id) {
  await projectDelete(id)
  ElMessage.success('删除成功')
  load()
}

async function remoteSearchLeader(keyword) {
  if (!keyword || keyword.trim() === '') {
    leaderOptions.value = []
    return
  }
  leaderLoading.value = true
  try {
    const r1 = await pagePerson({ page: 1, size: 10, name: keyword })
    const r2 = await pagePerson({ page: 1, size: 10, empNo: keyword })
    const map = new Map()
    ;(r1.records || []).forEach(p => map.set(p.id, p))
    ;(r2.records || []).forEach(p => map.set(p.id, p))
    leaderOptions.value = Array.from(map.values())
  } finally {
    leaderLoading.value = false
  }
}

onMounted(async () => {
  await loadDict()
  await load()
})
</script>
