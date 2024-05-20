<script>
import axios from "axios";

import {defineComponent} from "vue";
import MyMenu from "@/components/Utils/MyMenu.vue";
import CaseList from "@/components/Display/CaseList.vue";
import CaseAssistant from "@/components/Verification/CaseAssistant.vue";

export default defineComponent({
  components: {CaseAssistant, CaseList, MyMenu},
  data() {
    return {
      activeStep: 1,
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
        is_plaintiff_submit: "",
        is_defendant_submit: "",
        is_related_submit: "",
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
        this.caseInfoForm.is_plaintiff_submit = res.data.data.is_plaintiff_submit;
        this.caseInfoForm.is_defendant_submit = res.data.data.is_defendant_submit;
        this.caseInfoForm.is_related_submit = res.data.data.is_related_submit;
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
          <!--选择案件界面 -->
          <h1 v-show="!isSelected">选择案件</h1>
          <el-row v-show="!isSelected">
            <CaseList @select-case="handleCaseSelection"></CaseList>
          </el-row>
          <!--案件信息界面 -->
          <el-row v-show="isSelected">
            <el-descriptions title="案件基本信息" column="4" border direction="horizontal"
                             content-style="background: #f9f9f9;">
              <el-descriptions-item label="案号">{{ this.caseInfoForm.case_id }}</el-descriptions-item>
              <el-descriptions-item label="原告">{{ this.caseInfoForm.plaintiff_name }}</el-descriptions-item>
              <el-descriptions-item label="被告">{{ this.caseInfoForm.defendant_name }}</el-descriptions-item>
              <el-descriptions-item label="案件类型">{{ this.caseInfoForm.case_type }}</el-descriptions-item>
            </el-descriptions>
            <el-steps :active="activeStep" align-center finish-status="success">
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
            <!--            流程图组件-->
            <case-assistant :case-info-form="caseInfoForm"></case-assistant>
          </el-row>
        </el-main>
      </el-container>
    </el-container>
  </div>

</template>

<style scoped>
/* 全局样式 */
body {
  margin: 0;
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #f4f4f4;
}

/* 容器样式 */
.el-container {
  margin: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

/* 头部样式 */
.el-header {
  background-color: #409EFF;
  color: #fff;
  font-size: 1.8rem;
  line-height: 60px;
  text-align: center;
  border-radius: 8px 8px 0 0;
}

/* 侧边栏样式 */
.custom-side {
  background-color: #fff;
  border-right: 1px solid #eee;
}

/* 主要内容区域样式 */
.el-main {
  padding: 20px;
}

/* 标题样式 */
h1 {
  font-size: 1.6rem;
  color: #333;
  margin-bottom: 20px;
}

/* 描述列表样式 */
.el-descriptions {
  margin-top: 20px;
  color: #666;
}

/* 步骤条样式 */
.el-steps {
  margin-top: 40px;
}

/* 流程图组件样式 */
.ant-graph {
  margin-top: 40px;
}

/* 行样式 */
.el-row {
  margin-top: 20px;
  margin-bottom: 20px;
  border: 1px solid rgba(238, 238, 238, 0.3);
  border-radius: 8px;
  background-color: #f9f9f9;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  padding: 20px 30px;
}

</style>
