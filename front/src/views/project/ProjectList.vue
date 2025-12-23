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
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="projectCode" label="项目编号" width="140" />
      <el-table-column prop="name" label="项目名称" min-width="240" />
      <el-table-column prop="natureCode" label="性质" width="120" />
      <el-table-column prop="scopeCode" label="范围" width="120" />
      <el-table-column prop="startDate" label="开始" width="120" />
      <el-table-column prop="endDate" label="结束" width="120" />
      <el-table-column label="操作" width="260">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-button size="small" type="primary" @click="openMembers(row)">成员</el-button>
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
      <el-form-item label="负责人ID">
        <el-input-number v-model="form.leaderPersonId" :min="1" />
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

  <!-- 成员维护 -->
  <el-dialog v-model="memDlg.visible" :title="memDlg.title" width="820px">
    <div style="display:flex;gap:8px;align-items:center;margin-bottom:10px;flex-wrap:wrap">
      <el-input-number v-model="memForm.personId" :min="1" />
      <el-input v-model="memForm.duty" placeholder="职责" style="width:200px" />
      <el-date-picker v-model="memForm.joinDate" type="date" value-format="YYYY-MM-DD" placeholder="加入日期" />
      <el-input v-model="memForm.remark" placeholder="备注" style="width:200px" />
      <el-button type="primary" @click="addMember">添加成员</el-button>
    </div>

    <el-table :data="members" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="personId" label="人员ID" width="100" />
      <el-table-column prop="duty" label="职责" width="160" />
      <el-table-column prop="joinDate" label="加入日期" width="140" />
      <el-table-column prop="remark" label="备注" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-popconfirm title="移除该成员？" @confirm="delMember(row.id)">
            <template #reference>
              <el-button size="small" type="danger">移除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <template #footer>
      <el-button @click="memDlg.visible=false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { dictItems } from '../../api/dict'
import { projectPage, projectAdd, projectUpdate, projectDelete } from '../../api/project'
import { memberList, memberAdd, memberDelete } from '../../api/projectMember'

const q = reactive({ name: '', natureCode: '', scopeCode: '' })
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref([])

const natureItems = ref([])
const scopeItems = ref([])

const dlg = reactive({ visible: false, title: '' })
const form = reactive({
  id: null, projectCode: '', name: '',
  natureCode: '', scopeCode: '',
  startDate: '', endDate: '',
  leaderPersonId: null, budget: null, remark: ''
})
const dateRange = ref([])

const memDlg = reactive({ visible: false, title: '', projectId: null })
const members = ref([])
const memForm = reactive({ personId: 1, duty: '成员', joinDate: '', remark: '' })

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
  Object.assign(form, {
    id: null, projectCode: '', name: '',
    natureCode: '', scopeCode: '',
    startDate: '', endDate: '',
    leaderPersonId: null, budget: null, remark: ''
  })
  dateRange.value = []
}

function openEdit(row) {
  dlg.title = '编辑项目'
  dlg.visible = true
  Object.assign(form, row)
  dateRange.value = [row.startDate || '', row.endDate || '']
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

async function openMembers(row) {
  memDlg.visible = true
  memDlg.title = `成员维护：${row.name}（项目ID=${row.id}）`
  memDlg.projectId = row.id
  memForm.personId = 1
  memForm.duty = '成员'
  memForm.joinDate = ''
  memForm.remark = ''
  await reloadMembers()
}

async function reloadMembers() {
  members.value = await memberList(memDlg.projectId)
}

async function addMember() {
  if (!memForm.personId) return ElMessage.warning('人员ID必填')
  await memberAdd({
    projectId: memDlg.projectId,
    personId: memForm.personId,
    duty: memForm.duty,
    joinDate: memForm.joinDate,
    remark: memForm.remark
  })
  ElMessage.success('添加成功')
  await reloadMembers()
}

async function delMember(id) {
  await memberDelete(id)
  ElMessage.success('移除成功')
  await reloadMembers()
}

onMounted(async () => {
  await loadDict()
  await load()
})
</script>
