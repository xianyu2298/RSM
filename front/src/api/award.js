import http from '../utils/http'

export const awardPage = (params) => http.get('/api/award/page', { params })
export const awardAdd = (data) => http.post('/api/award', data)
export const awardUpdate = (data) => http.put('/api/award', data)
export const awardDelete = (id) => http.delete(`/api/award/${id}`)
export const awardGet = (id) => http.get(`/api/award/${id}`)
