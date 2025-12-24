import http from '../utils/http'

export const projectPage = (params) => http.get('/api/project/page', { params })
export const projectAdd = (data) => http.post('/api/project', data)
export const projectUpdate = (data) => http.put('/api/project', data)
export const projectDelete = (id) => http.delete(`/api/project/${id}`)
export const projectGet = (id) => http.get(`/api/project/${id}`)
