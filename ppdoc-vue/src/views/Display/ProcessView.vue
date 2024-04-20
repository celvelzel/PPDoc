<script>
import MyMenu from "@/components/Utils/MyMenu.vue";
export default {
  name: "ProcessView",
  components: {
    MyMenu
  },
  data() {
    return {
      form: {
        name: "",
        region: "",
        date1: "",
        date2: "",
        delivery: false,
        type: [],
        resource: "",
        desc: ""
      }
    };
  }
};
</script>

<template>
  <div>
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header style="font-size: 35px;background-color: #B3C0D1; color: #333;text-align: left;line-height: 60px">
        政务审核流程
      </el-header>
      <el-container>
        <el-aside class="custom-side" width="201px" style="background-color: rgb(238, 241, 246)">
          <MyMenu></MyMenu>
        </el-aside>
        <el-main>

          <el-steps :active="1" align-center>
            <el-step title="步骤1" description="这是一段很长的描述性文字"></el-step>
            <el-step title="步骤2" description="这是一段很长很长的描述性文字"></el-step>
            <el-step title="步骤3" description="这是一段很长很长很长的描述性文字"></el-step>
            <el-step title="步骤4" description="这是一段很长很长很长很长的描述性文字"></el-step>
          </el-steps>

          <el-form ref="form" :model="form" label-width="80px">

            <el-form-item label="案件名称">
              <el-input v-model="form.name"></el-input>
            </el-form-item>

            <el-form-item label="案件区域">
              <el-select v-model="form.region" placeholder="请选择活动区域">
                <el-option label="区域一" value="shanghai"></el-option>
                <el-option label="区域二" value="beijing"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="案件时间">
              <el-col :span="11">
                <el-date-picker type="date" placeholder="选择日期" v-model="form.date1"
                                style="width: 100%;"></el-date-picker>
              </el-col>
              <el-col class="line" :span="2">-</el-col>
              <el-col :span="11">
                <el-time-picker type="fixed-time" placeholder="选择时间" v-model="form.date2"
                                style="width: 100%;"></el-time-picker>
              </el-col>
            </el-form-item>
          </el-form>

        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>

</style>
