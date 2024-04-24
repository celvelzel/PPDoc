<script>
import MyMenu from "@/components/Utils/MyMenu.vue";
import CaseDisplay from "@/components/Display/CaseDisplay.vue";
import axios from "axios";
export default {
  name: "ProcessView",
  components: {
    MyMenu,
    CaseDisplay
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
      },
      activeStep: 1,
    };
  },
  methods: {
    handleCaseSelection(indictment_id) {
      // 当子组件触发'select-case'事件时，会调用这个方法
      // 处理业务逻辑
      axios.get("http://localhost:8080/indictments",{
        params: {
          indictment_id: indictment_id
        }
      }).then(res => {
        console.log(res.data.data);
        this.form = res.data.data;
      })
      // 如果数据库查询成功或者操作完成，跳转到下一步
      this.activeStep += 1;
    },
  }
};
</script>

<template>
  <div>
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header style="font-size: 35px;background-color: #B3C0D1; color: #333;text-align: left;line-height: 60px">
        立案审查流程
      </el-header>
      <el-container>
        <el-aside class="custom-side" width="201px" style="background-color: rgb(238, 241, 246)">
          <MyMenu></MyMenu>
        </el-aside>
        <el-main>
          <el-steps :active="activeStep" align-center>
            <el-step title="选择案件"></el-step>
            <el-step title="自然人身份证审核"></el-step>
            <el-step title="企业营业执照审核"></el-step>
            <el-step title="其他材料审核"></el-step>
          </el-steps>
          <br>
          <br>
          <!--          案件选择组件-->
          <CaseDisplay v-if="1==this.activeStep" @select-case="handleCaseSelection"></CaseDisplay>
          <!--          案件选择组件-->
          <br>
          <br>

        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>

</style>
