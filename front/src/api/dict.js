import http from '../utils/http'

// 默认：/api/dict/items?typeCode=PROJECT_NATURE
export const dictItems = (typeCode) =>
    http.get('/api/dict/items', { params: { typeCode } })
