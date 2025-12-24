<template>
  <div style="padding:16px">
    <el-card>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <div>
          <h2 style="margin:0">人员详情</h2>
          <div style="color:#888;margin-top:6px">人员ID：{{ personId }}</div>
        </div>
        <el-button @click="back">返回</el-button>
      </div>

      <el-descriptions v-if="person" :column="3" border style="margin-top:12px">
        <el-descriptions-item label="工号">{{ person.empNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ person.name || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ person.gender || '-' }}</el-descriptions-item>

        <el-descriptions-item label="职称">{{ person.title || '-' }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ person.department || '-' }}</el-descriptions-item>
        <el-descriptions-item label="入职日期">{{ person.hireDate || '-' }}</el-descriptions-item>

        <el-descriptions-item label="电话">{{ person.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ person.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ person.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="margin-top:12px">
      <el-tabs v-model="active">
        <!-- 参与项目 -->
        <el-tab-pane label="参与项目" name="project">
          <el-table :data="projects" border style="margin-top:10px">
            <el-table-column prop="id" label="ID" width="80"/>
            <el-table-column prop="projectCode" label="项目编号" width="140"/>
            <el-table-column prop="name" label="项目名称"/>
            <el-table-column prop="startDate" label="开始日期" width="140"/>
            <el-table-column prop="endDate" label="结束日期" width="140"/>
            <el-table-column prop="statusCode" label="状态" width="120">
              <template #default="{ row }">
                {{ dictName('PROJECT_STATUS', row.statusCode) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button size="small" type="primary" @click="goProject(row.id)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 论文 -->
        <el-tab-pane label="论文" name="paper">
          <el-table :data="papers" border style="margin-top:10px">
            <el-table-column prop="id" label="ID" width="80"/>
            <el-table-column prop="title" label="题目"/>
            <el-table-column prop="journal" label="期刊" width="220"/>
            <el-table-column prop="indexCode" label="检索源" width="140">
              <template #default="{ row }">
                {{ dictName('PAPER_INDEX_SOURCE', row.indexCode) }}
              </template>
            </el-table-column>
            <el-table-column prop="publishDate" label="发表日期" width="140"/>
            <el-table-column prop="doi" label="DOI" width="180"/>
          </el-table>
        </el-tab-pane>

        <!-- 著作 -->
        <el-tab-pane label="著作" name="book">
          <el-table :data="books" border style="margin-top:10px">
            <el-table-column prop="id" label="ID" width="80"/>
            <el-table-column prop="name" label="书名"/>
            <el-table-column prop="publisher" label="出版社" width="220"/>
            <el-table-column prop="publishDate" label="出版日期" width="140"/>
            <el-table-column prop="isbn" label="ISBN" width="180"/>
          </el-table>
        </el-tab-pane>

        <!-- 参与项目获奖 -->
        <el-tab-pane label="获奖（参与项目）" name="award">
          <el-table :data="awards" border style="margin-top:10px">
            <el-table-column prop="id" label="ID" width="80"/>
            <el-table-column prop="awardName" label="奖项名称"/>
            <el-table-column prop="awardPersons" label="获奖人（项目成员）" width="260" />
            <el-table-column prop="awardLevel" label="级别" width="140"/>
            <el-table-column prop="awardOrg" label="授奖单位" width="220"/>
            <el-table-column prop="awardDate" label="获奖日期" width="140"/>
            <el-table-column prop="projectCode" label="项目编号" width="140"/>
            <el-table-column prop="projectName" label="项目名称" width="220"/>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'

import {dictItems} from '../../api/dict'
import {personGet} from '../../api/person'
import {paperPage} from '../../api/paper'
import {bookPage} from '../../api/book'
import {projectListByPersonId} from '../../api/project'
import {awardListByPersonId} from '../../api/award'

const route = useRoute()
const router = useRouter()
const personId = Number(route.params.id)

if (Number.isNaN(personId)) {
  ElMessage.error('人员ID不合法')
  router.push('/person')
}


const active = ref('project')

const person = ref(null)
const projects = ref([])
const papers = ref([])
const books = ref([])
const awards = ref([])

// 字典缓存（复用你项目详情页同样写法）
const dictMap = reactive({})

function dictName(typeCode, itemCode) {
  if (!itemCode) return '-'
  const items = dictMap[typeCode] || []
  const hit = items.find(i => i.itemCode === itemCode)
  return hit ? hit.itemName : itemCode
}

async function loadDict() {
  const types = ['PROJECT_STATUS', 'PAPER_INDEX_SOURCE']
  for (const t of types) {
    try {
      dictMap[t] = await dictItems(t)
    } catch {
      dictMap[t] = []
    }
  }
}

function back() {
  router.push('/person')
}

function goProject(id) {
  router.push(`/project/${id}`)
}

async function loadAll() {
  try {
    person.value = await personGet(personId)

    // 项目（参与项目）
    projects.value = await projectListByPersonId(personId)

    // 论文/著作（本人发表/出版）
    const p = await paperPage({page: 1, size: 100, personId})
    papers.value = p.records || []

    const b = await bookPage({page: 1, size: 100, personId})
    books.value = b.records || []

    // 获奖（参与项目的获奖）
    awards.value = await awardListByPersonId(personId)
  } catch (e) {
    console.error(e)
    ElMessage.error('加载人员详情失败（请检查后端接口/控制台）')
  }
}

onMounted(async () => {
  await loadDict()
  await loadAll()
})
</script>
