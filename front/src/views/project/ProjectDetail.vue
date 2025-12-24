<template>
  <div style="padding:16px">
    <el-card>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <div>
          <h2 style="margin:0">项目详情</h2>
          <div style="color:#888;margin-top:6px">项目ID：{{ projectId }}</div>
        </div>
        <el-button @click="back">返回</el-button>
      </div>

      <el-descriptions v-if="project" :column="3" border style="margin-top:12px">
        <el-descriptions-item label="项目编号">{{ project.projectCode }}</el-descriptions-item>
        <el-descriptions-item label="项目名称">{{ project.name }}</el-descriptions-item>
        <el-descriptions-item label="项目状态">{{ dictName('PROJECT_STATUS', project.statusCode) }}</el-descriptions-item>

        <el-descriptions-item label="项目性质">{{ dictName('PROJECT_NATURE', project.natureCode) }}</el-descriptions-item>
        <el-descriptions-item label="项目范围">{{ dictName('PROJECT_SCOPE', project.scopeCode) }}</el-descriptions-item>
        <el-descriptions-item label="负责人ID">{{ project.leaderPersonId || '-' }}</el-descriptions-item>

        <el-descriptions-item label="开始日期">{{ project.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ project.endDate }}</el-descriptions-item>
        <el-descriptions-item label="预算">{{ project.budget }}</el-descriptions-item>

        <el-descriptions-item label="备注" :span="3">{{ project.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="margin-top:12px">
      <el-tabs v-model="active">
        <!-- 成员 -->
        <el-tab-pane label="成员" name="member">
          <el-button type="success" @click="openAddMember">新增成员</el-button>
          <el-table :data="members" border style="margin-top:10px">
            <el-table-column prop="id" label="ID" width="80"/>
            <el-table-column prop="personId" label="人员ID" width="100"/>
            <el-table-column prop="empNo" label="工号" width="120" />
            <el-table-column prop="personName" label="姓名" width="120" />
            <el-table-column prop="duty" label="职责" width="140"/>
            <el-table-column prop="joinDate" label="加入日期" width="140"/>
            <el-table-column prop="remark" label="备注"/>
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
        </el-tab-pane>

        <!-- 获奖 -->
        <el-tab-pane label="获奖" name="award">
          <el-button type="success" @click="openAddAward">新增获奖</el-button>
          <el-table :data="awards" border style="margin-top:10px">
            <el-table-column prop="id" label="ID" width="80"/>
            <el-table-column prop="awardName" label="奖项名称"/>
            <el-table-column prop="awardLevel" label="级别" width="140"/>
            <el-table-column prop="awardOrg" label="授奖单位"/>
            <el-table-column prop="awardDate" label="获奖日期" width="140"/>
            <el-table-column prop="remark" label="备注"/>
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-popconfirm title="删除该记录？" @confirm="delAward(row.id)">
                  <template #reference>
                    <el-button size="small" type="danger">删除</el-button>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 论文 -->
        <el-tab-pane label="论文（按负责人展示）" name="paper">
          <div style="color:#666;margin-bottom:10px">
            说明：你目前论文表没有 projectId，这里按“负责人 personId”展示，答辩展示够用。
          </div>
          <el-table :data="papers" border>
            <el-table-column prop="id" label="ID" width="80"/>
            <el-table-column prop="title" label="题目"/>
            <el-table-column prop="journal" label="期刊"/>
            <el-table-column prop="indexCode" label="检索源" width="140">
              <template #default="{ row }">
                {{ dictName('PAPER_INDEX_SOURCE', row.indexCode) }}
              </template>
            </el-table-column>
            <el-table-column prop="publishDate" label="发表日期" width="140"/>
          </el-table>
        </el-tab-pane>

        <!-- 著作 -->
        <el-tab-pane label="著作（按负责人展示）" name="book">
          <div style="color:#666;margin-bottom:10px">
            说明：你目前著作表没有 projectId，这里按“负责人 personId”展示。
          </div>
          <el-table :data="books" border>
            <el-table-column prop="id" label="ID" width="80"/>
            <el-table-column prop="name" label="书名"/>
            <el-table-column prop="publisher" label="出版社"/>
            <el-table-column prop="publishDate" label="出版日期" width="140"/>
            <el-table-column prop="isbn" label="ISBN" width="180"/>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 新增成员弹窗（远程搜索人员：输入姓名/工号） -->
    <el-dialog v-model="memberDlg.visible" title="新增成员" width="520px">
      <el-form :model="memberDlg.form" label-width="90px">
        <el-form-item label="选择人员">
          <el-select
              v-model="memberDlg.form.personId"
              filterable remote reserve-keyword
              placeholder="输入姓名/工号搜索"
              :remote-method="remoteSearchPerson"
              :loading="personLoading"
              style="width:100%"
          >
            <el-option
                v-for="p in personOptions"
                :key="p.id"
                :label="`${p.empNo} - ${p.name}`"
                :value="p.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="职责">
          <el-input v-model="memberDlg.form.duty" placeholder="负责人/成员"/>
        </el-form-item>

        <el-form-item label="加入日期">
          <el-date-picker
              v-model="memberDlg.form.joinDate"
              type="date"
              value-format="YYYY-MM-DD"
              style="width:100%"
          />
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="memberDlg.form.remark"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="memberDlg.visible=false">取消</el-button>
        <el-button type="primary" @click="submitAddMember">保存</el-button>
      </template>
    </el-dialog>

    <!-- 新增获奖弹窗 -->
    <el-dialog v-model="awardDlg.visible" title="新增获奖" width="520px">
      <el-form :model="awardDlg.form" label-width="90px">
        <el-form-item label="奖项名称">
          <el-input v-model="awardDlg.form.awardName"/>
        </el-form-item>

        <el-form-item label="奖项级别">
          <el-input v-model="awardDlg.form.awardLevel"/>
        </el-form-item>

        <el-form-item label="授奖单位">
          <el-input v-model="awardDlg.form.awardOrg"/>
        </el-form-item>

        <el-form-item label="获奖日期">
          <el-date-picker
              v-model="awardDlg.form.awardDate"
              type="date"
              value-format="YYYY-MM-DD"
              style="width:100%"
          />
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="awardDlg.form.remark"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="awardDlg.visible=false">取消</el-button>
        <el-button type="primary" @click="submitAddAward">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

import { dictItems } from '../../api/dict'
import { projectGet } from '../../api/project'
import { memberList, memberAdd, memberDelete } from '../../api/projectMember'
import { awardPage, awardAdd, awardDelete } from '../../api/award'
import { paperPage } from '../../api/paper'
import { bookPage } from '../../api/book'
import { pagePerson } from '../../api/person'

const route = useRoute()
const router = useRouter()
const projectId = Number(route.params.id)

const project = ref(null)
const members = ref([])
const awards = ref([])
const papers = ref([])
const books = ref([])
const active = ref('member')

// 字典缓存
const dictMap = reactive({})

function dictName(typeCode, itemCode) {
  if (!itemCode) return '-'
  const items = dictMap[typeCode] || []
  const hit = items.find(i => i.itemCode === itemCode)
  return hit ? hit.itemName : itemCode
}

async function loadDict() {
  const types = ['PROJECT_NATURE', 'PROJECT_SCOPE', 'PROJECT_STATUS', 'PAPER_INDEX_SOURCE']
  for (const t of types) {
    try { dictMap[t] = await dictItems(t) } catch { dictMap[t] = [] }
  }
}

async function loadAll() {
  project.value = await projectGet(projectId)
  members.value = await memberList(projectId)

  // ✅ 用你现有的 /api/award/page 按 projectId 过滤即可（无需改后端）
  const a = await awardPage({ page: 1, size: 100, projectId })
  awards.value = a.records || []

  // 论文/著作按负责人展示
  if (project.value?.leaderPersonId) {
    const pid = project.value.leaderPersonId
    const p = await paperPage({ page: 1, size: 100, personId: pid })
    papers.value = p.records || []
    const b = await bookPage({ page: 1, size: 100, personId: pid })
    books.value = b.records || []
  } else {
    papers.value = []
    books.value = []
  }
}

function back() {
  router.push('/project')
}

/** 成员新增 */
const memberDlg = reactive({
  visible: false,
  form: { projectId, personId: null, duty: '成员', joinDate: '', remark: '' }
})

function openAddMember() {
  memberDlg.form = { projectId, personId: null, duty: '成员', joinDate: '', remark: '' }
  personOptions.value = []
  memberDlg.visible = true
}

async function submitAddMember() {
  if (!memberDlg.form.personId) return ElMessage.warning('请选择人员')
  if (!memberDlg.form.joinDate) return ElMessage.warning('请选择加入日期')
  await memberAdd(memberDlg.form)
  ElMessage.success('新增成员成功')
  memberDlg.visible = false
  members.value = await memberList(projectId)
}

async function delMember(id) {
  await memberDelete(id)
  ElMessage.success('移除成功')
  members.value = await memberList(projectId)
}

/** 人员远程搜索（用你现有的 /api/person/page） */
const personOptions = ref([])
const personLoading = ref(false)

async function remoteSearchPerson(keyword) {
  if (!keyword || keyword.trim() === '') {
    personOptions.value = []
    return
  }
  personLoading.value = true
  try {
    // 你后端 person 支持 name + empNo，所以这里合并两次查询结果，避免后端再加新接口
    const r1 = await pagePerson({ page: 1, size: 10, name: keyword })
    const r2 = await pagePerson({ page: 1, size: 10, empNo: keyword })
    const map = new Map()
    ;(r1.records || []).forEach(p => map.set(p.id, p))
    ;(r2.records || []).forEach(p => map.set(p.id, p))
    personOptions.value = Array.from(map.values())
  } finally {
    personLoading.value = false
  }
}

/** 获奖新增 */
const awardDlg = reactive({
  visible: false,
  form: { projectId, awardName: '', awardLevel: '', awardOrg: '', awardDate: '', remark: '' }
})

function openAddAward() {
  awardDlg.form = { projectId, awardName: '', awardLevel: '', awardOrg: '', awardDate: '', remark: '' }
  awardDlg.visible = true
}

async function submitAddAward() {
  if (!awardDlg.form.awardName) return ElMessage.warning('请输入奖项名称')
  if (!awardDlg.form.awardDate) return ElMessage.warning('请选择获奖日期')
  await awardAdd(awardDlg.form)
  ElMessage.success('新增获奖成功')
  awardDlg.visible = false
  const a = await awardPage({ page: 1, size: 100, projectId })
  awards.value = a.records || []
}

async function delAward(id) {
  await awardDelete(id)
  ElMessage.success('删除成功')
  const a = await awardPage({ page: 1, size: 100, projectId })
  awards.value = a.records || []
}

onMounted(async () => {
  await loadDict()
  await loadAll()
})
</script>
