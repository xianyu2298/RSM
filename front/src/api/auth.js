import http from '../utils/http'

// 方式A：你后端已经有 /api/auth/login（推荐）
// 方式B：你还没有，就先用临时登录（下面会讲怎么做）

export const login = async ({ username, password }) => {
    // ✅ 如果你后端还没写登录接口，先用临时逻辑：允许直接进入
    //   等你后端接口完成，把这里注释掉改成 http.post 即可
    // return { id: 1, username, realName: username, role: 'USER', token: 'demo-token' }

    // ✅ 推荐：真正调用后端
    return http.post('/api/auth/login', { username, password })
}

export const logout = async () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
}
