<script>

import {defineComponent} from "vue";
import MyMenu from "@/components/Utils/MyMenu.vue";

export default({
  components: {MyMenu},
  data() {
    return {
      activeIndex: '1', // 激活的头部菜单项
      quickAccess: [ // 快速入口配置
        { icon: 'iconfont icon-jingqingluru', label: '文档录入', path: '/doc/upload' },
        { icon: 'iconfont icon-guanli', label: '文档管理', path: '/doc' },
        { icon: 'iconfont icon-yinzhangrenzheng', label: '智能审阅', path: '/assistant' },
        { icon: 'iconfont icon-icon-test', label: '系统管理', path: '/model' }
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
          <h1>欢迎使用文档管理系统</h1>
          <p>本系统提供文档的录入、管理、智能审阅以及系统管理等功能。</p>
          <!-- 快速入口 -->
          <el-row :gutter="20">
            <el-col :span="6" v-for="(item, index) in quickAccess" :key="index">
              <el-card>
                <div @click="navigate(item.path)" style="cursor: pointer;">
                  <i :class="item.icon"></i>
                  <div style="margin-top: 10px;">{{ item.label }}</div>
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

</style>