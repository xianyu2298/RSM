import http from '../utils/http'

export const memberList = (projectId) =>
    http.get('/api/project-member/list', { params: { projectId } })

export const memberAdd = (data) => http.post('/api/project-member', data)
export const memberDelete = (id) => http.delete(`/api/project-member/${id}`)
