<template>
  <div style="padding:16px">
    <el-card>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <div>
          <h2 style="margin:0">字典管理</h2>
          <div style="color:#888;margin-top:6px">维护字典类型与字典项</div>
        </div>
      </div>
    </el-card>

    <el-card v-if="isAdmin" style="margin-top:12px">
      <el-tabs v-model="active">
        <!-- 字典类型 -->
        <el-tab-pane label="字典类型" name="type">
          <el-form :inline="true" :model="typeQuery" style="margin-bottom:10px">
            <el-form-item label="类型编码">
              <el-input v-model="typeQuery.typeCode" placeholder="typeCode" />
            </el-form-item>
            <el-form-item label="类型名称">
              <el-input v-model="typeQuery.typeName" placeholder="typeName" />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="typeQuery.status" placeholder="全部" style="width:120px">
                <el-option label="全部" :value="null" />
                <el-option label="启用" :value="1" />
                <el-option label="停用" :value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadTypes">查询</el-button>
              <el-button @click="resetType">重置</el-button>
              <el-button type="success" @click="openAddType">新增类型</el-button>
            </el-form-item>
          </el-form>

          <el-table :data="typeList" border>
            <el-table-column prop="id" label="ID" width="80"/>
            <el-table-column prop="typeCode" label="类型编码" width="180"/>
            <el-table-column prop="typeName" label="类型名称" />
            <el-table-column prop="sortNo" label="排序" width="90"/>
            <el-table-column prop="status" label="状态" width="90">
              <template #default="{ row }">
                <span v-if="row.status === 1">启用</span>
                <span v-else>停用</span>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" @click="openEditType(scope.row)">编辑</el-button>
                <el-button size="small" type="primary" @click="quickToItems(scope.row.typeCode)">管理字典项</el-button>
                <el-popconfirm title="删除该类型将同时删除其字典项，确认？" @confirm="delType(scope.row.id)">
                  <template #reference>
                    <el-button size="small" type="danger">删除</el-button>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>

          <div style="margin-top:10px;display:flex;justify-content:flex-end">
            <el-pagination
                background
                layout="total, prev, pager, next"
                :total="typeTotal"
                :page-size="typeQuery.size"
                v-model:current-page="typeQuery.page"
                @current-change="loadTypes"
            />
          </div>
        </el-tab-pane>

        <!-- 字典项 -->
        <el-tab-pane label="字典项" name="item">
          <el-form :inline="true" :model="itemQuery" style="margin-bottom:10px">
            <el-form-item label="类型">
              <el-select v-model="itemQuery.typeCode" placeholder="请选择类型" style="width:260px" @change="loadItems">
                <el-option v-for="t in typeOptions" :key="t.typeCode" :label="`${t.typeCode} - ${t.typeName}`" :value="t.typeCode"/>
              </el-select>
            </el-form-item>
            <el-form-item label="项编码">
              <el-input v-model="itemQuery.itemCode" placeholder="itemCode" />
            </el-form-item>
            <el-form-item label="项名称">
              <el-input v-model="itemQuery.itemName" placeholder="itemName" />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="itemQuery.status" placeholder="全部" style="width:120px">
                <el-option label="全部" :value="null" />
                <el-option label="启用" :value="1" />
                <el-option label="停用" :value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadItems">查询</el-button>
              <el-button @click="resetItem">重置</el-button>
              <el-button type="success" :disabled="!itemQuery.typeCode" @click="openAddItem">新增字典项</el-button>
            </el-form-item>
          </el-form>

          <el-table :data="itemList" border>
            <el-table-column prop="id" label="ID" width="80"/>
            <el-table-column prop="typeCode" label="类型编码" width="180"/>
            <el-table-column prop="itemCode" label="项编码" width="160"/>
            <el-table-column prop="itemName" label="项名称"/>
            <el-table-column prop="sortNo" label="排序" width="90"/>
            <el-table-column prop="status" label="状态" width="90">
              <template #default="{ row }">
                <span v-if="row.status === 1">启用</span>
                <span v-else>停用</span>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注"/>
            <el-table-column label="操作" width="160">
              <template #default="scope">
                <el-button size="small" @click="openEditItem(scope.row)">编辑</el-button>
                <el-popconfirm title="删除该字典项？" @confirm="delItem(scope.row.id)">
                  <template #reference>
                    <el-button size="small" type="danger">删除</el-button>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>

          <div style="margin-top:10px;display:flex;justify-content:flex-end">
            <el-pagination
                background
                layout="total, prev, pager, next"
                :total="itemTotal"
                :page-size="itemQuery.size"
                v-model:current-page="itemQuery.page"
                @current-change="loadItems"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 类型弹窗 -->
    <el-dialog v-model="typeDlg.visible" :title="typeDlg.form.id ? '编辑类型' : '新增类型'" width="520px">
      <el-form :model="typeDlg.form" label-width="90px">
        <el-form-item label="类型编码">
          <el-input v-model="typeDlg.form.typeCode" />
        </el-form-item>
        <el-form-item label="类型名称">
          <el-input v-model="typeDlg.form.typeName" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="typeDlg.form.sortNo" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="typeDlg.form.status" style="width:100%">
            <el-option label="启用" :value="1"/>
            <el-option label="停用" :value="0"/>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="typeDlg.form.remark" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="typeDlg.visible=false">取消</el-button>
        <el-button type="primary" @click="saveType">保存</el-button>
      </template>
    </el-dialog>

    <!-- 字典项弹窗 -->
    <el-dialog v-model="itemDlg.visible" :title="itemDlg.form.id ? '编辑字典项' : '新增字典项'" width="520px">
      <el-form :model="itemDlg.form" label-width="90px">
        <el-form-item label="类型编码">
          <el-input v-model="itemDlg.form.typeCode" :disabled="true" />
        </el-form-item>
        <el-form-item label="项编码">
          <el-input v-model="itemDlg.form.itemCode" />
        </el-form-item>
        <el-form-item label="项名称">
          <el-input v-model="itemDlg.form.itemName" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="itemDlg.form.sortNo" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="itemDlg.form.status" style="width:100%">
            <el-option label="启用" :value="1"/>
            <el-option label="停用" :value="0"/>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="itemDlg.form.remark" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="itemDlg.visible=false">取消</el-button>
        <el-button type="primary" @click="saveItem">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import {ElMessage} from 'element-plus'
import {
  dictTypePage, dictTypeList, dictTypeAdd, dictTypeUpdate, dictTypeDelete,
  dictItemPage, dictItemAdd, dictItemUpdate, dictItemDelete
} from '../../api/dict'

const currentUser = JSON.parse(localStorage.getItem('user') || '{}')
const isAdmin = currentUser && currentUser.role === 'ADMIN'

const active = ref('type')

// ====== 类型 ======
const typeQuery = reactive({page: 1, size: 10, typeCode: '', typeName: '', status: null})
const typeList = ref([])
const typeTotal = ref(0)
const typeOptions = ref([])

const typeDlg = reactive({
  visible: false,
  form: {id: null, typeCode: '', typeName: '', remark: '', status: 1, sortNo: 0}
})

async function loadTypes() {
  const res = await dictTypePage(typeQuery)
  typeList.value = res.records || []
  typeTotal.value = res.total || 0
}

async function loadTypeOptions() {
  typeOptions.value = await dictTypeList()
}

function resetType() {
  typeQuery.page = 1
  typeQuery.typeCode = ''
  typeQuery.typeName = ''
  typeQuery.status = null
  loadTypes()
}

function openAddType() {
  typeDlg.form = {id: null, typeCode: '', typeName: '', remark: '', status: 1, sortNo: 0}
  typeDlg.visible = true
}

function openEditType(row) {
  typeDlg.form = {...row}
  typeDlg.visible = true
}

async function saveType() {
  if (!typeDlg.form.typeCode) return ElMessage.warning('请输入类型编码')
  if (!typeDlg.form.typeName) return ElMessage.warning('请输入类型名称')
  const api = typeDlg.form.id ? dictTypeUpdate : dictTypeAdd
  await api(typeDlg.form)
  ElMessage.success('保存成功')
  typeDlg.visible = false
  await loadTypes()
  await loadTypeOptions()
}

async function delType(id) {
  await dictTypeDelete(id)
  ElMessage.success('删除成功')
  await loadTypes()
  await loadTypeOptions()
}

function quickToItems(typeCode) {
  active.value = 'item'
  itemQuery.typeCode = typeCode
  itemQuery.page = 1
  loadItems()
}

// ====== 字典项 ======
const itemQuery = reactive({page: 1, size: 10, typeCode: '', itemCode: '', itemName: '', status: null})
const itemList = ref([])
const itemTotal = ref(0)

const itemDlg = reactive({
  visible: false,
  form: {id: null, typeCode: '', itemCode: '', itemName: '', status: 1, sortNo: 0, remark: ''}
})

async function loadItems() {
  const res = await dictItemPage(itemQuery)
  itemList.value = res.records || []
  itemTotal.value = res.total || 0
}

function resetItem() {
  itemQuery.page = 1
  itemQuery.itemCode = ''
  itemQuery.itemName = ''
  itemQuery.status = null
  loadItems()
}

function openAddItem() {
  itemDlg.form = {id: null, typeCode: itemQuery.typeCode, itemCode: '', itemName: '', status: 1, sortNo: 0, remark: ''}
  itemDlg.visible = true
}

function openEditItem(row) {
  itemDlg.form = {...row}
  itemDlg.visible = true
}

async function saveItem() {
  if (!itemDlg.form.typeCode) return ElMessage.warning('请选择类型')
  if (!itemDlg.form.itemCode) return ElMessage.warning('请输入项编码')
  if (!itemDlg.form.itemName) return ElMessage.warning('请输入项名称')
  const api = itemDlg.form.id ? dictItemUpdate : dictItemAdd
  await api(itemDlg.form)
  ElMessage.success('保存成功')
  itemDlg.visible = false
  await loadItems()
}

async function delItem(id) {
  await dictItemDelete(id)
  ElMessage.success('删除成功')
  await loadItems()
}

onMounted(async () => {
  await loadTypes()
  await loadTypeOptions()
})
</script>
