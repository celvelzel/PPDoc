<script>
import MyMenu from "@/components/Utils/MyMenu.vue";
import CaseDisplay from "@/components/Verification/CaseDisplay.vue";
import IdCardVerification from "@/components/Verification/IdCardVerification.vue";
import axios from "axios";
export default {
  name: "ProcessView",
  components: {
    MyMenu,
    CaseDisplay,
    IdCardVerification
  },
  data() {
    return {
      displayForm: {
        indictmentPdfUrl:"",
      },
      caseInfoForm:{
        case_id:"",
        indictment_id:"",
        case_type:"",
        plaintiff_name:"",
        plaintiff_id:"",
        plaintiff_type:"",
        defendant_name:"",
        defendant_id:"",
        defendant_type:"",
        plaintiff_id_card_id:"",
        defendant_id_card_id:"",
        plaintiff_license_id:"",
        defendant_license_id:"",
        related_invoice_id:"",
      },
      activeStep: 1,
    };
  },
  methods: {
    handleCaseSelection(indictment_id) {
      // 当子组件触发'select-case'事件时，会调用这个方法
      // 处理业务逻辑
      axios.get(`http://localhost:8080/indictments/${indictment_id}`).then(res => {
        console.log("被选中的案件的起诉状：" + res.data.data);
        // 根据选择的起诉状，填入案件基本信息
        this.caseInfoForm.indictment_id = res.data.data.indictment_id;
        this.caseInfoForm.case_type = res.data.data.case_type;
        this.caseInfoForm.plaintiff_name = res.data.data.plaintiff_name;
        this.caseInfoForm.plaintiff_id = res.data.data.plaintiff_id;
        this.caseInfoForm.plaintiff_type = res.data.data.plaintiff_type;
        this.caseInfoForm.defendant_name = res.data.data.defendant_name;
        this.caseInfoForm.defendant_id = res.data.data.defendant_id;
        this.caseInfoForm.defendant_type = res.data.data.defendant_type;
        // 将起诉状pdf的url填入displayForm
        this.displayForm.indictmentPdfUrl = res.data.data.indictment_url;
      })
      // 如果数据库查询成功或者操作完成，跳转到下一步
      this.activeStep += 1;
    },
    handleSkip(){
      this.activeStep += 1;
    },
    handleIdCardSelection(id_card_id){
      // 当子组件触发'select-id-card'事件时，会调用这个方法
      // 处理业务逻辑
      console.log("被选中的身份证id：" + id_card_id);
    }
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
          <!--          身份证审核组件-->
          <IdCardVerification v-if="2==this.activeStep" :caseInfoForm="caseInfoForm" :display-form="displayForm" @skip="handleSkip" @select-id-card="handleIdCardSelection"></IdCardVerification>
          <!--          身份证审核组件-->
          <br>
          <br>

        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>

</style>
