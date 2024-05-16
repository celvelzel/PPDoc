<script>
export default {}
</script>

<template>
  <el-menu style="text-align: left;">
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

/* 菜单整体样式 */
.el-menu {
  border: none; /* 移除边框 */
  background-color: #f9f9f9; /* 轻微的背景色，提升层次感 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加轻微阴影 */
}

/* 菜单标题样式 */
.el-menu .el-submenu__title {
  font-size: 16px; /* 增大字体大小 */
  font-weight: bold; /* 字体加粗 */
  color: #333; /* 更深色的字体颜色 */
  padding: 0 20px; /* 增加水平 padding，提供更多空间 */
}

/* 菜单项样式 */
.el-menu-item {
  font-size: 14px; /* 设置菜单项字体大小 */
  padding: 0 20px; /* 增加水平 padding */
}

/* 激活状态下的菜单项样式 */
.el-menu-item.is-active {
  color: #007bff; /* 蓝色字体，表示激活状态 */
  background-color: #e7f1ff; /* 轻微的背景色，表示激活状态 */
}

/* 鼠标悬停时的菜单项样式 */
.el-menu-item:hover {
  background-color: #f1f1f1; /* 悬停时改变背景色 */
}

/* 为图标添加样式 */
.iconfont {
  margin-right: 10px; /* 留出适当的空间 */
  font-size: 18px; /* 增大图标大小 */
  vertical-align: middle; /* 垂直居中对齐 */
}

/* 菜单链接样式 */
.router-link-exact-active,
.router-link-active {
  text-decoration: none; /* 去除下划线 */
  color: inherit; /* 继承父元素的字体颜色 */
}

/* 菜单分组标题样式 */
.el-menu-item-group__title {
  padding: 10px 20px; /* 增加上下 padding，提供更好的视觉分隔 */
  font-size: 14px; /* 设置分组标题字体大小 */
  color: #666; /* 分组标题使用较淡的颜色 */
}


</style>
