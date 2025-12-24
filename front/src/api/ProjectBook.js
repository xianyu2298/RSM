import http from '../utils/http'

// 列表：某项目已绑定著作
export const projectBookList = (projectId) =>
    http.get('/api/project-book/list', { params: { projectId } })

// 绑定：把著作绑定到项目
export const projectBookBind = (projectId, bookId) =>
    http.post('/api/project-book/bind', { projectId, bookId })

// 解绑：删除中间表记录（注意这里传的是 project_book 的 id）
export const projectBookUnbind = (id) =>
    http.delete(`/api/project-book/${id}`)
