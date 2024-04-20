import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/Display/HomeView.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/doc',
        name: 'doc',
        component: () => import('../views/Display/DocView.vue'),
        meta: {title: "文档管理",},
    },
    {
        path: '/invoice',
        name: 'invoice',
        component: () => import('../views/Display/InvoiceView.vue'),
        meta: {title: "发票管理",},
    },
    {
        path: '/idcard',
        name: 'idcard',
        component: () => import('../views/Display/IdCardView.vue'),
        meta: {title: "身份证管理",},
    },
    {
        path: '/license',
        name: 'license',
        component: () => import('../views/Display/LicenseView.vue'),
        meta: {title: "营业执照管理",},
    },
    {
        path: '/invoice/upload',
        name: 'invoiceUpload',
        component: () => import('../views/Upload/InvoiceUploadView.vue'),
        meta: {title: "发票上传",},
    },
    {
        path: '/idcard/upload',
        name: 'idcardUpload',
        component: () => import('../views/Upload/IdCardUploadView.vue'),
        meta: {title: "身份证上传",},
    },
    {
        path: '/license/upload',
        name: 'licenseUpload',
        component: () => import('../views/Upload/LicenseUploadView.vue'),
        meta: {title: "发票上传",},
    },
    {
        path: '/doc/upload',
        name: 'docUpload',
        component: () => import('../views/Upload/DocUploadView.vue'),
        meta: {title: "通用文档上传",},
    },
    {
        path: '/process',
        name: 'processs',
        component: () => import('../views/Display/ProcessView.vue'),
        meta: {title: "政务审核流程",},
    },
    {
        path: '/pdf',
        name: 'pdf',
        component: () => import('../views/Display/PDFPreview.vue'),
        meta: {title: "PDF预览",},
    },
    {
        path: '/dialog',
        name: 'dialog',
        component: () => import('../views/DialogTest.vue'),
        meta: {title: "Dialog预览",},
    },
]

const router = new VueRouter({
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
