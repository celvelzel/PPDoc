<script>
export default {}
</script>

<template>
  <el-menu :default-openeds="['1']" style="text-align: left;font-size: 14px;color: #333">
    <el-submenu index="1">
      <template slot="title">
        <span class="iconfont icon-jingqingluru"></span>
        文档录入
      </template>
      <el-menu-item-group>
        <el-menu-item index="1-1">
          <span class="iconfont icon-wendang"></span>
          <router-link to="/doc/upload">通用文档录入</router-link>
        </el-menu-item>
        <el-menu-item index="1-2">
          <span class="iconfont icon-shenfenrenzheng"></span>
          <router-link to="/idcard/upload">身份证录入</router-link>
        </el-menu-item>
        <el-menu-item index="1-3">
          <span class="iconfont icon-fapiao"></span>
          <router-link to="/invoice/upload">发票录入</router-link>
        </el-menu-item>
        <el-menu-item index="1-4">
          <span class="iconfont icon-zhengzhaozhizhao"></span>
          <router-link to="/license/upload">营业执照录入</router-link>
        </el-menu-item>
        <el-menu-item index="1-5">
          <span class="iconfont icon-hetong"></span>
          <router-link to="/indictment/upload">起诉状录入</router-link>
        </el-menu-item>
      </el-menu-item-group>
    </el-submenu>
    <el-submenu index="2">
      <template slot="title">
        <span class="iconfont icon-guanli"></span>
        文档管理
      </template>
      <el-menu-item-group>
        <el-menu-item index="2-1">
          <span class="iconfont icon-wendang"></span>
          <router-link to="/doc">通用文档管理</router-link>
        </el-menu-item>
        <el-menu-item index="2-2">
          <span class="iconfont icon-shenfenrenzheng"></span>
          <router-link to="/idcard">身份证管理</router-link>
        </el-menu-item>
        <el-menu-item index="2-3">
          <span class="iconfont icon-fapiao"></span>
          <router-link to="/invoice">发票管理</router-link>
        </el-menu-item>
        <el-menu-item index="2-4">
          <span class="iconfont icon-zhengzhaozhizhao"></span>
          <router-link to="/license">营业执照管理</router-link>
        </el-menu-item>
        <el-menu-item index="2-5">
          <span class="iconfont icon-hetong"></span>
          <router-link to="/indictment">起诉状管理</router-link>
        </el-menu-item>
      </el-menu-item-group>
    </el-submenu>
    <el-submenu index="3">
      <template slot="title">
        <span class="iconfont icon-yinzhangrenzheng"></span>
        智能审阅
      </template>
      <el-menu-item-group>
        <el-menu-item index="3-1">
          <span class="iconfont icon-zhinengheyue"></span>
          <router-link to="/assistant">案件辅助</router-link>
        </el-menu-item>
        <el-menu-item index="3-2">
          <span class="iconfont icon-zhengfu"></span>
          <router-link to="/process">立案审查</router-link>
        </el-menu-item>
        <el-menu-item index="3-3">
          <span class="iconfont icon-zhishitupu-moren"></span>
          <router-link to="/case">案件知识图谱</router-link>
        </el-menu-item>
      </el-menu-item-group>
    </el-submenu>
    <el-submenu index="4">
      <template slot="title">
        <span class="iconfont icon-icon-test"></span>
        系统管理
      </template>
      <el-menu-item-group>
        <el-menu-item index="4-1">
          <span class="iconfont icon-guanli"></span>
          <router-link to="/model">模型管理</router-link>
        </el-menu-item>
      </el-menu-item-group>
    </el-submenu>
  </el-menu>
</template>

<style scoped>
/* 去除链接下划线 */
.router-link-exact-active,
.router-link-active {
  text-decoration: none;
}

* {
  text-decoration: none;
  color: #333;
}

/* 菜单项激活颜色 */
.el-menu--horizontal > .el-menu-item.is-active,
.el-menu--horizontal > .el-submenu__title.is-active,
.el-submenu__title:hover,
.el-menu--horizontal > .el-menu-item:hover {
  color: #007bff !important;
}

/* 非激活状态下，鼠标悬停时的菜单项颜色 */
.el-menu--horizontal > .el-menu-item:hover:not(.is-active),
.el-submenu__title:hover:not(.is-active) {
  color: #6c757d !important;
}

/* 为图标添加样式 */
.iconfont {
  margin-right: 9px;
  font-size: 16px;
  vertical-align: middle;
}

/* 菜单标题样式 */
.menu-title {
  display: inline-block;
  vertical-align: middle;
}
</style>
