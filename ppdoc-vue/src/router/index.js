import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/about',
        name: 'about',
        component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    },
    {
        path: '/invoice',
        name: 'invoice',
        component: () => import(/* webpackChunkName: "about" */ '../views/InvoiceView.vue')
    },
    {
        path:'/idcard',
        name:'idcard',
        component:()=>import(/* webpackChunkName: "about" */ '../views/IdCardView.vue')
    }
]

const router = new VueRouter({
    routes
})

export default router
