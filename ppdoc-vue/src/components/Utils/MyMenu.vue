<script>
export default {
}
</script>

<template>
  <el-menu :default-openeds="['1','2','3']" style="text-align: left;font-size: 14px;color: #333">
    <el-submenu index="1">
      <template slot="title">
        <i class="el-icon-document"></i>
        文档录入
      </template>
      <el-menu-item-group>
        <el-menu-item index="1-1"><router-link to="/doc/upload">通用文档录入</router-link></el-menu-item>
        <el-menu-item index="1-2"><router-link to="/idcard/upload">身份证录入</router-link></el-menu-item>
        <el-menu-item index="1-3"><router-link to="/invoice/upload">发票录入</router-link></el-menu-item>
        <el-menu-item index="1-4"><router-link to="/license/upload">营业执照录入</router-link></el-menu-item>
<!--        <el-menu-item index="1-5">起诉状录入</el-menu-item>-->
      </el-menu-item-group>
    </el-submenu>
    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-menu"></i>
        文档管理
      </template>
      <el-menu-item-group>
        <el-menu-item index="2-1"><router-link to="/doc">通用文档管理</router-link></el-menu-item>
        <el-menu-item index="2-2"><router-link to="/idcard">身份证管理</router-link></el-menu-item>
        <el-menu-item index="2-3"><router-link to="/invoice">发票管理</router-link></el-menu-item>
        <el-menu-item index="2-4"><router-link to="/license">营业执照管理</router-link></el-menu-item>
        <!--        <el-menu-item index="1-5">起诉状管理</el-menu-item>-->
      </el-menu-item-group>
    </el-submenu>
    <el-submenu index="3">
      <template slot="title">
        <i class="el-icon-document"></i>
        信息审核
      </template>
      <el-menu-item-group>
        <el-menu-item index="1-1"><router-link to="/process">政务信息审核流程</router-link></el-menu-item>、
      </el-menu-item-group>
    </el-submenu>
  </el-menu>
</template>

<style scoped>

</style>
