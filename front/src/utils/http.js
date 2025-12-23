import axios from 'axios'
import { ElMessage } from 'element-plus'


const http = axios.create({
    baseURL: '',
    timeout: 15000
})

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
        ElMessage.error(err?.response?.data?.message || err.message || '网络错误')
        return Promise.reject(err)
    }
)



export default http
