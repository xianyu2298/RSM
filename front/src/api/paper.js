import http from '../utils/http'

export const paperPage = (params) => http.get('/api/paper/page', { params })
export const paperAdd = (data) => http.post('/api/paper', data)
export const paperUpdate = (data) => http.put('/api/paper', data)
export const paperDelete = (id) => http.delete(`/api/paper/${id}`)
export const paperGet = (id) => http.get(`/api/paper/${id}`)
