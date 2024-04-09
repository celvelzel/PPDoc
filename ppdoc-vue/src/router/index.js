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
        component: () => import('../views/InvoiceView.vue')
    },
    {
        path:'/idcard',
        name:'idcard',
        component:()=>import('../views/IdCardView.vue')
    },
    {
        path:'/license',
        name:'license',
        component:()=>import('../views/LicenseView.vue')
    },
    {
        path:'/doc',
        name:'doc',
        component:()=>import('../views/DocView.vue')
    },
    {
        path:'/pdf',
        name:'pdf',
        component:()=>import('../views/PdfTest.vue')
    },
    {
        path:'/pdfvuer',
        name:'pdfvuer',
        component:()=>import('../views/PdfVuer.vue')
    }
]

const router = new VueRouter({
    routes
})

export default router
