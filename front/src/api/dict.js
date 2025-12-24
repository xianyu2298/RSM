import http from '../utils/http'

// 业务页面：按类型取启用项
export const dictItems = (typeCode) =>
    http.get('/api/dict/items', { params: { typeCode } })

// 字典类型
export const dictTypePage = (params) =>
    http.get('/api/dict/type/page', { params })

export const dictTypeList = () =>
    http.get('/api/dict/type/list')

export const dictTypeAdd = (data) =>
    http.post('/api/dict/type/add', data)

export const dictTypeUpdate = (data) =>
    http.post('/api/dict/type/update', data)

export const dictTypeDelete = (id) =>
    http.delete(`/api/dict/type/${id}`)

// 字典项
export const dictItemPage = (params) =>
    http.get('/api/dict/item/page', { params })

export const dictItemAdd = (data) =>
    http.post('/api/dict/item/add', data)

export const dictItemUpdate = (data) =>
    http.post('/api/dict/item/update', data)

export const dictItemDelete = (id) =>
    http.delete(`/api/dict/item/${id}`)
