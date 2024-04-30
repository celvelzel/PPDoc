<script>
import axios from "axios";

import {defineComponent} from "vue";
import MyMenu from "@/components/Utils/MyMenu.vue";
import CaseDisplay from "@/components/Display/CaseDisplay.vue";
import AntGraph from "@/components/Utils/AntGraph.vue";

export default defineComponent({
  components: {CaseDisplay, MyMenu,AntGraph},
  data() {
    return {
      activeStep: 2,
      isSelected: false,
      caseInfoForm: {
        case_id: "",
        indictment_id: "",
        case_type: "",
        plaintiff_name: "",
        plaintiff_id: "",
        plaintiff_type: "",
        defendant_name: "",
        defendant_id: "",
        defendant_type: "",
        plaintiff_id_card_id: "",
        defendant_id_card_id: "",
        plaintiff_license_id: "",
      }
    }
  },
  methods: {
    handleCaseSelection(case_id) {
      axios.get(`http://localhost:8080/api/cases/${case_id}`).then(res => {
        console.log("被选中的案件：" + res.data.data);
        // 根据选择的案件，填入案件基本信息
        this.caseInfoForm.case_id = res.data.data.case_id;
        this.caseInfoForm.indictment_id = res.data.data.indictment_id;
        this.caseInfoForm.case_type = res.data.data.case_type;
        this.caseInfoForm.plaintiff_name = res.data.data.plaintiff_name;
        this.caseInfoForm.plaintiff_id = res.data.data.plaintiff_id;
        this.caseInfoForm.defendant_name = res.data.data.defendant_name;
        this.caseInfoForm.defendant_id = res.data.data.defendant_id;
        this.caseInfoForm.defendant_type = res.data.data.defendant_type;
        this.caseInfoForm.plaintiff_id_card_id = res.data.data.plaintiff_id_card_id;
        this.caseInfoForm.defendant_id_card_id = res.data.data.defendant_id_card_id;
        this.caseInfoForm.plaintiff_license_id = res.data.data.plaintiff_license_id;
        this.isSelected = true;

        this.showGraphDialog = true;
      })
    },
}
})
</script>

<template>
  <div>
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header style="font-size: 35px;background-color: #B3C0D1; color: #333;text-align: left;line-height: 60px">
        案件辅助
      </el-header>
      <el-container>
        <el-aside class="custom-side" width="201px" style="background-color: rgb(238, 241, 246)">
          <MyMenu></MyMenu>
        </el-aside>
        <el-main>
          <el-row v-show="!isSelected">
            <case-display operation="select" @select-case="handleCaseSelection"></case-display>
          </el-row>
          <el-row v-show="isSelected">
            <el-descriptions title="案件信息" column="4">
              <el-descriptions-item label="案号">{{this.caseInfoForm.case_id}}</el-descriptions-item>
              <el-descriptions-item label="原告">{{this.caseInfoForm.plaintiff_name}}</el-descriptions-item>
              <el-descriptions-item label="被告">{{ this.caseInfoForm.defendant_name }}</el-descriptions-item>
              <el-descriptions-item label="案件类型">{{this.caseInfoForm.case_type}}</el-descriptions-item>
            </el-descriptions>
          </el-row>
          <el-row v-show="isSelected">
            <el-steps :active="activeStep" align-center>
              <el-step title="立案前"></el-step>
              <el-step title="立案"></el-step>
              <el-step title="排期送达"></el-step>
              <el-step title="庭前申请"></el-step>
              <el-step title="证据准备"></el-step>
              <el-step title="开庭"></el-step>
              <el-step title="结案"></el-step>
              <el-step title="结案后"></el-step>
            </el-steps>
            </el-row>
          <el-row v-show="isSelected">
          </el-row>
          </el-main>
        </el-container>
    </el-container>
  </div>

</template>

<style scoped>
.el-row {
  margin-top: 1rem; /* 增加顶部外边距，提高呼吸感 */
  margin-bottom: 3rem; /* 同上，底部外边距 */
  border: 1px solid rgba(238, 238, 238, 0.3); /* 柔和的边框颜色，增加透明度以适应更多背景 */
  border-radius: 8px; /* 添加圆角，使元素显得不那么生硬 */
  background-color: #f9f9f9; /* 轻微的背景色，提升层次感 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 添加阴影，增强立体效果 */
  transition: all 0.3s ease; /* 平滑的过渡效果，提升用户体验 */
  /* 增大内边距*/
  padding: 20px 30px;
}

/* 可选：鼠标悬停时改变效果，提升交互体验 */
.el-row:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15);
}
</style>
