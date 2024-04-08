<script>

</script>

<template>
  <el-menu :default-openeds="['1', '3']"
           style="text-align: left;font-size: 14px;color: #333;display: flex;padding: 0px">
    <el-submenu index="1">
      <template slot="title">
        <i class="el-icon-menu"></i>
        文档录入
      </template>
      <el-menu-item-group>
        <el-menu-item index="1-1"><router-link to="/doc">通用文档录入</router-link></el-menu-item>
        <el-menu-item index="1-2"><router-link to="/invoice">发票录入</router-link></el-menu-item>
        <el-menu-item index="1-3"><router-link to="/license">营业执照录入</router-link></el-menu-item>
        <el-menu-item index="1-4">起诉状录入</el-menu-item>
        <el-menu-item index="1-5"><router-link to="/idcard">身份证录入</router-link></el-menu-item>
      </el-menu-item-group>
    </el-submenu>
  </el-menu>
</template>

<style scoped>

</style>
