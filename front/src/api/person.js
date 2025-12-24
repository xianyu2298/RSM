import http from '../utils/http'

// 分页查询
export const pagePerson = (params) => http.get('/api/person/page', { params })

// 新增
export const addPerson = (data) => http.post('/api/person', data)

// 修改
export const updatePerson = (data) => http.put('/api/person', data)

// 删除
export const deletePerson = (id) => http.delete(`/api/person/${id}`)

// 根据ID获取（可选）
export const getPerson = (id) => http.get(`/api/person/${id}`)
export const personGet = (id) => http.get(`/api/person/${id}`)

