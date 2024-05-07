<script>
import Channel from "@/components/System/Channel.vue";
import MyMenu from "@/components/Utils/MyMenu.vue";

export default {
  components: {MyMenu, Channel},
  data() {
    return {
      activeName: 'channel'
    };
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  },
}
</script>

<template>
  <div>
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header style="font-size: 35px;background-color: #B3C0D1; color: #333;text-align: left;line-height: 60px">
        模型管理
      </el-header>
      <el-container>
        <el-aside class="custom-side" width="201px" style="background-color: rgb(238, 241, 246)">
          <MyMenu></MyMenu>
        </el-aside>
        <el-main>
          <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="渠道管理" name="channel">
              <Channel></Channel>
            </el-tab-pane>
            <el-tab-pane label="提示词管理" name="prompt">
              提示词管理
            </el-tab-pane>
          </el-tabs>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>

</style>
