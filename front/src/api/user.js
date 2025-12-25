import http from '../utils/http'

// 用户分页
export const userPage = (params) => http.get('/api/user/page', { params })

export const userAdd = (data) => http.post('/api/user', data)

export const userUpdate = (data) => http.put('/api/user', data)

export const userDelete = (id) => http.delete(`/api/user/${id}`)

// 修改密码：
export const changePassword = (params) =>
    http.put('/api/user/password', null, { params })

//
export const resetPassword = (params) =>
    http.put('/api/user/reset-password', null, { params })
