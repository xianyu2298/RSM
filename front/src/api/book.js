import http from '../utils/http'

export const bookPage = (params) => http.get('/api/book/page', { params })
export const bookAdd = (data) => http.post('/api/book', data)
export const bookUpdate = (data) => http.put('/api/book', data)
export const bookDelete = (id) => http.delete(`/api/book/${id}`)
export const bookGet = (id) => http.get(`/api/book/${id}`)
