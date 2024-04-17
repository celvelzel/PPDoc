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
        path: '/doc',
        name: 'doc',
        component: () => import('../views/DocView.vue'),
        meta: {title: "文档管理",},
    },
    {
        path: '/invoice',
        name: 'invoice',
        component: () => import('../views/InvoiceView.vue'),
        meta: {title: "发票管理",},
    },
    {
        path: '/idcard',
        name: 'idcard',
        component: () => import('../views/IdCardView.vue'),
        meta: {title: "身份证管理",},
    },
    {
        path: '/license',
        name: 'license',
        component: () => import('../views/LicenseView.vue'),
        meta: {title: "营业执照管理",},
    },
    {
        path: '/invoice/upload',
        name: 'invoiceUpload',
        component: () => import('../views/InvoiceUploadView.vue'),
        meta: {title: "发票上传",},
    },
    {
        path: '/idcard/upload',
        name: 'idcardUpload',
        component: () => import('../views/IdCardUploadView.vue'),
        meta: {title: "身份证上传",},
    },
    {
        path: '/license/upload',
        name: 'licenseUpload',
        component: () => import('../views/LicenseUploadView.vue'),
        meta: {title: "发票上传",},
    },
    {
        path: '/doc/upload',
        name: 'docUpload',
        component: () => import('../views/DocUploadView.vue'),
        meta: {title: "通用文档上传",},
    },
    {
        path: '/pdf',
        name: 'pdf',
        component: () => import('../views/PDFPreview.vue'),
        meta: {title: "PDF预览",},
    },
]

const router = new VueRouter({
    mode: 'history',
    routes
})

export default router

router.beforeEach((to, from, next) => {
    // 设置页面标题
    if (to.meta.title) {
        document.title = to.meta.title;
    }
    next();
});
