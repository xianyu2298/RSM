<template>
  <el-card>
    <div style="display:flex;gap:8px;flex-wrap:wrap;align-items:center">
      <el-input v-model="q.awardName" placeholder="奖项名称" style="width:240px" />
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
      <el-button v-if="isAdmin" type="success" @click="openAdd">新增获奖</el-button>
    </div>

    <el-table :data="rows" style="margin-top:12px" border>
      <el-table-column prop="projectCode" label="项目编号" width="140" />
      <el-table-column prop="projectName" label="项目名称" />
      <el-table-column prop="awardName" label="奖项名称" min-width="220" />
      <el-table-column prop="awardPersons" label="获奖人（项目成员）" width="260" />
      <el-table-column prop="awardLevel" label="级别" width="120" />
      <el-table-column prop="awardOrg" label="授奖单位" width="180" />
      <el-table-column prop="awardDate" label="获奖日期" width="120" />
      <el-table-column prop="remark" label="备注" min-width="200" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button v-if="isAdmin" size="small" @click="openEdit(row)">编辑</el-button>
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

  <el-dialog v-model="dlg.visible" :title="dlg.title" width="720px">
    <el-form :model="form" label-width="90px">
      <el-form-item label="所属项目">
        <el-select v-model="form.projectId" filterable placeholder="请选择项目" style="width:100%">
          <el-option
              v-for="p in projectOptions"
              :key="p.id"
              :label="`${p.id} - ${p.name}`"
              :value="p.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="奖项名称">
        <el-input v-model="form.awardName" placeholder="例如：校级科技进步奖" />
      </el-form-item>

      <el-form-item label="奖项级别">
        <el-input v-model="form.awardLevel" placeholder="例如：一等奖/二等奖/省部级等" />
      </el-form-item>

      <el-form-item label="授奖单位">
        <el-input v-model="form.awardOrg" placeholder="例如：某某大学/教育厅" />
      </el-form-item>

      <el-form-item label="获奖日期">
        <el-date-picker v-model="form.awardDate" type="date" value-format="YYYY-MM-DD" />
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
import { awardPage, awardAdd, awardUpdate, awardDelete } from '../../api/award'
import { projectPage } from '../../api/project'
import { getCurrentUser, isAdminUser } from '../../utils/http'

const currentUser = getCurrentUser()
const isAdmin = isAdminUser(currentUser)

const q = reactive({ awardName: '' })
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref([])

const dlg = reactive({ visible: false, title: '' })
const projectOptions = ref([])
const form = reactive({
  id: null,
  projectId: 1,
  awardName: '',
  awardLevel: '',
  awardOrg: '',
  awardDate: '',
  remark: ''
})

async function load() {
  const data = await awardPage({
    page: page.value,
    size: size.value,
    awardName: q.awardName || undefined
  })
  total.value = data.total
  rows.value = data.records
}

function reset() {
  q.awardName = ''
  page.value = 1
  load()
}

function openAdd() {
  dlg.title = '新增获奖'
  dlg.visible = true
  Object.assign(form, {
    id: null,
    projectId: 1,
    awardName: '',
    awardLevel: '',
    awardOrg: '',
    awardDate: '',
    remark: ''
  })
}

function openEdit(row) {
  dlg.title = '编辑获奖'
  dlg.visible = true
  Object.assign(form, row)
}

async function save() {
  if (!form.projectId) return ElMessage.warning('请选择所属项目')
  if (!form.awardName) return ElMessage.warning('奖项名称必填')

  if (form.id) await awardUpdate(form)
  else await awardAdd(form)

  ElMessage.success('保存成功')
  dlg.visible = false
  load()
}

async function remove(id) {
  await awardDelete(id)
  ElMessage.success('删除成功')
  load()
}

async function loadProjectOptions() {
  const res = await projectPage({ page: 1, size: 1000 })
  projectOptions.value = res.records || []
}

onMounted(async () => {
  await load()
  await loadProjectOptions()
})
</script>
