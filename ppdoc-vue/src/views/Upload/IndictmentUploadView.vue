<script>

import {defineComponent} from "vue";
import MyMenu from "@/components/Utils/MyMenu.vue";
import IndictmentUpload from "@/components/Upload/IndictmentUpload.vue";

export default defineComponent({
  components: {IndictmentUpload, MyMenu}
})
</script>

<template>
  <div>
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header style="font-size: 35px;background-color: #B3C0D1; color: #333;text-align: left;line-height: 60px">
        起诉状录入
      </el-header>
      <el-container>
        <el-aside class="custom-side" width="201px" style="background-color: rgb(238, 241, 246)">
          <MyMenu></MyMenu>
        </el-aside>
        <el-main>
          <indictment-upload></indictment-upload>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.el-header {
  background-color: #B3C0D1;
  color: #333;
  text-align: center;
  line-height: 60px;
}
.custom-aside {
  /* 边框和圆角 */
  border-radius: 4px;
  border: 1px solid #dcdfe6;

  /* 盒阴影 */
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  /* 内容与边框之间的距离 */
  padding: 10px;

  /* 保持内部组件居中对齐 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>
