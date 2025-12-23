import http from '../utils/http'

// 用户分页（如果你后端不是 /api/user/page，把路径告诉我我改）
export const userPage = (params) => http.get('/api/user/page', { params })

export const userAdd = (data) => http.post('/api/user', data)

export const userUpdate = (data) => http.put('/api/user', data)

export const userDelete = (id) => http.delete(`/api/user/${id}`)

// 修改密码：按你现在后端的 query 参数方式对接
export const changePassword = (params) =>
    http.put('/api/user/password', null, { params })

// （可选）初始化密码/重置密码：如果你后端有接口就用，没有先不写
export const resetPassword = (params) =>
    http.put('/api/user/reset-password', null, { params })
