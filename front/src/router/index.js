import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../views/Layout.vue'
import ProjectList from '../views/project/ProjectList.vue'
import PersonList from '../views/person/PersonList.vue'
import PaperList from '../views/paper/PaperList.vue'
import BookList from '../views/book/BookList.vue'
import AwardList from '../views/award/AwardList.vue'

import UserList from '../views/user/UserList.vue'
import PasswordChange from '../views/user/PasswordChange.vue'




const routes = [
    {
        path: '/',
        component: Layout,
        redirect: '/project',
        children: [
            { path: '/project', component: ProjectList }
            ,
            { path: '/person', component: PersonList }
            ,
            { path: '/paper', component: PaperList }
            ,
            { path: '/book', component: BookList }
            ,
            { path: '/award', component: AwardList }
            ,
            { path: '/user', component: UserList }
            ,
            { path: '/password', component: PasswordChange }


        ]
    }

]

export default createRouter({
    history: createWebHistory(),
    routes
})
