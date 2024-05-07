<script>

import {defineComponent} from "vue";
import MyMenu from "@/components/Utils/MyMenu.vue";
import CaseGraph from "@/components/Utils/CaseGraph.vue";

export default defineComponent({
  components: {MyMenu, CaseGraph}
})
</script>

<template>
  <div>
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header style="font-size: 35px;background-color: #B3C0D1; color: #333;text-align: left;line-height: 60px">
        案件知识图谱
      </el-header>
      <el-container>
        <el-aside class="custom-side" width="201px" style="background-color: rgb(238, 241, 246)">
          <MyMenu></MyMenu>
        </el-aside>
        <el-main>
          <CaseGraph></CaseGraph>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>

</style>
