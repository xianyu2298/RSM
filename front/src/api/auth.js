import http from '../utils/http'

export const login = async ({ username, password }) => {


    return http.post('/api/auth/login', { username, password })
}

export const logout = async () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
}
