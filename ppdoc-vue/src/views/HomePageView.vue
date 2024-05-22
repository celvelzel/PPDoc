<script>

import {defineComponent} from "vue";
import MyMenu from "@/components/Utils/MyMenu.vue";

export default ({
  components: {MyMenu},
  data() {
    return {
      activeIndex: '1', // 激活的头部菜单项
      quickAccess: [ // 快速入口配置
        {
          icon: 'iconfont icon-jingqingluru', label: '文档录入',
          children: [
            {label: '通用文档录入', path: '/doc/upload', icon: 'iconfont icon-wendang'},
            {label: '身份证录入', path: '/idcard/upload', icon: 'iconfont icon-shenfenrenzheng'},
            {label: '发票录入', path: '/invoice/upload', icon: 'iconfont icon-fapiao'},
            {label: '营业执照录入', path: '/license/upload', icon: 'iconfont icon-zhengzhaozhizhao'},
            {label: '起诉状录入', path: '/indictment/upload', icon: 'iconfont icon-hetong'},
          ]
        },
        {
          icon: 'iconfont icon-guanli', label: '文档管理',
          children: [
            {label: '通用文档管理', path: '/doc', icon: 'iconfont icon-wendang'},
            {label: '身份证管理', path: '/idcard', icon: 'iconfont icon-shenfenrenzheng'},
            {label: '发票管理', path: '/invoice', icon: 'iconfont icon-fapiao'},
            {label: '营业执照管理', path: '/license', icon: 'iconfont icon-zhengzhaozhizhao'},
            {label: '起诉状管理', path: '/indictment', icon: 'iconfont icon-hetong'},
          ]
        },
        {
          icon: 'iconfont icon-yinzhangrenzheng', label: '智能审阅',
          children: [
              {label: '案件辅助', path: '/assistant', icon: 'iconfont icon-zhinengheyue'},
            {label: '立案审查', path: '/process', icon: 'iconfont icon-zhengfu'},
            {label: '案件知识图谱', path: '/case', icon: 'iconfont icon-zhishitupu-moren'},
          ]
        },
        {
          icon: 'iconfont icon-icon-test', label: '系统管理',
          children: [
              {label: '模型管理', path: '/model', icon: 'iconfont icon-guanli'},
          ]
        }
        // 根据需要可以继续添加
      ]
    };
  },
  methods: {
    navigate(path) {
      this.$router.push(path);
    }
  }
})

</script>

<template>
  <div>
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header style="font-size: 35px;background-color: #B3C0D1; color: #333;text-align: left;line-height: 60px">
        首页
      </el-header>
      <el-container>
        <el-aside class="custom-side" width="201px" style="background-color: rgb(238, 241, 246)">
          <MyMenu></MyMenu>
        </el-aside>
        <el-main>
          <img alt="logo" src="../assets/QQLogo.png" style="width: 200px; height: 210px;">
          <h1>欢迎使用智能文档分析系统</h1>
          <p>本系统提供文档的录入、管理、智能审阅以及系统管理等功能。</p>
          <!-- 快速入口 -->
          <el-row :gutter="20">
            <el-col :span="6" v-for="(item, index) in quickAccess" :key="index">
              <el-card class="box-card"
                       shadow="hover">
                <div slot="header" class="clearfix">
                  <i :class="item.icon"></i>
                  <div style="margin-top: 10px;">{{ item.label }}</div>
                </div>
                <div v-for="(child, index) in item.children" :key="index" @click="navigate(child.path)"
                     class ="left-align"
                     style="cursor: pointer; margin-top: 20px; margin-bottom: 20px;">
                  <i :class="child.icon"></i>
                  <span>{{ child.label }}</span>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.left-align {
  text-align: left;
}
.box-card{
  height: 350px;
}
</style>