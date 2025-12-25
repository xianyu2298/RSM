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
import PersonDetail from '../views/person/PersonDetail.vue'
import ProjectDetail from '../views/project/ProjectDetail.vue'
import { getCurrentUser, isAdminUser } from '../utils/http'

const routes = [
    { path: '/login', component: Login },
    {
        path: '/',
        component: Layout,
        redirect: '/project',
        children: [
            { path: '/project', component: ProjectList },

            { path: '/project/:id', component: ProjectDetail },

            { path: '/person', component: PersonList },
            { path: '/paper', component: PaperList },
            { path: '/book', component: BookList },
            { path: '/award', component: AwardList },
            { path: '/user', component: UserList },
            { path: '/password', component: PasswordChange },
            { path: '/person/:id', component: () => import('../views/person/PersonDetail.vue') },
            { path: '/dict', component: () => import('../views/dict/DictManage.vue') }


        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.path === '/login') {
        return next()
    }
    if (!token) {
        return next('/login')
    }
    const user = getCurrentUser()
    const isAdmin = isAdminUser(user)
    if (to.path.startsWith('/user') || to.path.startsWith('/dict')) {
        if (!isAdmin) {
            return next('/project')
        }
    }
    next()
})

export default router
