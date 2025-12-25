import axios from 'axios'
import { ElMessage } from 'element-plus'

const http = axios.create({
    baseURL: '',
    timeout: 15000
})

http.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token')
        if (token) {
            if (!config.headers) {
                config.headers = {}
            }
            config.headers.Authorization = token
        }
        return config
    },
    (error) => Promise.reject(error)
)

http.interceptors.response.use(
    (resp) => {
        const r = resp.data
        if (r && typeof r.code !== 'undefined') {
            if (r.code === 0) return r.data
            ElMessage.error(r.msg || '请求失败')
            return Promise.reject(new Error(r.msg || '请求失败'))
        }
        return r
    },
    (err) => {
        const data = err && err.response && err.response.data
        if (data && typeof data.code !== 'undefined') {
            ElMessage.error(data.msg || '请求失败')
        } else {
            ElMessage.error(err.message || '网络错误')
        }
        return Promise.reject(err)
    }
)

export default http
