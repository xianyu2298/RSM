import http from '../utils/http'

// 列表：某项目已绑定论文
export const projectPaperList = (projectId) =>
    http.get('/api/project-paper/list', { params: { projectId } })

// 绑定：把论文绑定到项目
export const projectPaperBind = (projectId, paperId) =>
    http.post('/api/project-paper/bind', { projectId, paperId })

// 解绑：删除中间表记录（注意这里传的是 project_paper 的 id）
export const projectPaperUnbind = (id) =>
    http.delete(`/api/project-paper/${id}`)
