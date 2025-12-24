import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../views/Layout.vue'
import ProjectList from '../views/project/ProjectList.vue'
import PersonList from '../views/person/PersonList.vue'
import PaperList from '../views/paper/PaperList.vue'
import BookList from '../views/book/BookList.vue'
import AwardList from '../views/award/AwardList.vue'
import UserList from '../views/user/UserList.vue'
import PasswordChange from '../views/user/PasswordChange.vue'
import Login from '../views/Login.vue'

// ✅ 新增：项目详情页
import ProjectDetail from '../views/project/ProjectDetail.vue'

const routes = [
    { path: '/login', component: Login },
    {
        path: '/',
        component: Layout,
        redirect: '/project',
        children: [
            { path: '/project', component: ProjectList },
            // ✅ 新增：/project/10 这种
            { path: '/project/:id', component: ProjectDetail },

            { path: '/person', component: PersonList },
            { path: '/paper', component: PaperList },
            { path: '/book', component: BookList },
            { path: '/award', component: AwardList },
            { path: '/user', component: UserList },
            { path: '/password', component: PasswordChange },
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.path === '/login') return next()
    if (!token) return next('/login')
    next()
})

export default router
