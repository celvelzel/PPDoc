<script>
import MyMenu from "@/components/Utils/MyMenu.vue";
import CaseDisplay from "@/components/Verification/CaseDisplay.vue";
import PlaintiffInfoVerification from "@/components/Verification/PlaintiffInfoVerification.vue";
import DefendantInfoVerification from "@/components/Verification/DefendantInfoVerification.vue";
import invoiceVerification from "@/components/Verification/InvoiceVerification.vue";
import finishVerification from "@/components/Verification/FinishVerification.vue";
import axios from "axios";

export default {
  name: "ProcessView",
  components: {
    MyMenu,
    CaseDisplay,
    PlaintiffInfoVerification,
    DefendantInfoVerification,
    invoiceVerification,
    finishVerification,
  },
  data() {
    return {
      displayForm: {
        indictmentPdfUrl: "",
        related_file_name: "",
      },
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
        defendant_license_id: "",
        related_invoice_id: "",
        all_info: "",
      },
      activeStep: 1,
    };
  },
  methods: {
    handleCaseSelection(indictment_id) {
      // 当子组件触发'select-case'事件时，会调用这个方法
      // 处理业务逻辑
      axios.get(`http://localhost:8080/api/indictments/${indictment_id}`).then(res => {
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
        this.caseInfoForm.all_info = res.data.data.all_info;
        // 将起诉状pdf的url填入displayForm
        this.displayForm.indictmentPdfUrl = res.data.data.indictment_url;
      })
      // 如果数据库查询成功或者操作完成，跳转到下一步
      this.activeStep += 1;
    },
    handleSkip() {
      this.activeStep += 1;
    },
    handleIdCardSelection(id_card_id, card_number, name, type) {
      // 当子组件触发'select-id-card'事件时，会调用这个方法
      // 处理业务逻辑
      if (type == "plaintiff") {
        this.caseInfoForm.plaintiff_id = card_number;
        this.caseInfoForm.plaintiff_name = name;
        this.caseInfoForm.plaintiff_id_card_id = id_card_id;
      } else if (type == "defendant") {
        this.caseInfoForm.defendant_id = card_number;
        this.caseInfoForm.defendant_name = name;
        this.caseInfoForm.defendant_id_card_id = id_card_id;
      }
      console.log("被选中的身份证id：" + id_card_id);
      this.activeStep += 1;
    },
    handleLicenseSelection(license_id, license_code, license_enterprise_name, type) {
      // 当子组件触发'select-license'事件时，会调用这个方法
      // 处理业务逻辑
      if (type == "plaintiff") {
        this.caseInfoForm.plaintiff_id = license_code;
        this.caseInfoForm.plaintiff_name = license_enterprise_name;
        this.caseInfoForm.plaintiff_license_id = license_id;
      } else if (type == "defendant") {
        this.caseInfoForm.defendant_id = license_code;
        this.caseInfoForm.defendant_name = license_enterprise_name;
        this.caseInfoForm.defendant_license_id = license_id;
      }
      console.log("被选中的营业执照id：" + license_id);
      this.activeStep += 1;
    },
    handleInvoiceSelection(invoice_id, invoice_file_name) {
      // 当子组件触发'select-invoice'事件时，会调用这个方法
      // 处理业务逻辑
      this.caseInfoForm.related_invoice_id = invoice_id;
      this.displayForm.related_file_name = invoice_file_name;
      console.log("被选中的发票id：" + invoice_id);
      this.activeStep += 1;
    },
  }
}
</script>

<template>
  <div>
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header style="font-size: 35px;background-color: #B3C0D1; color: #333;text-align: left;line-height: 60px">
        立案审查
      </el-header>
      <el-container>
        <el-aside class="custom-side" width="201px" style="background-color: rgb(238, 241, 246)">
          <MyMenu></MyMenu>
        </el-aside>
        <el-main>
          <el-steps :active="activeStep" align-center>
            <el-step title="选择案件"></el-step>
            <el-step title="原告信息审查"></el-step>
            <el-step title="被告信息审查"></el-step>
            <el-step title="其他材料审查"></el-step>
            <el-step title="完成立案审查"></el-step>
          </el-steps>
          <br>
          <br>
          <!--          案件选择组件-->
          <CaseDisplay v-if="1==this.activeStep" @select-case="handleCaseSelection"></CaseDisplay>
          <!--          案件选择组件-->
          <!--          原告信息审查组件-->
          <PlaintiffInfoVerification v-if="2==this.activeStep"
                                     :caseInfoForm="caseInfoForm"
                                     :display-form="displayForm"
                                     @select-id-card="handleIdCardSelection"
                                     @select-license="handleLicenseSelection"></PlaintiffInfoVerification>
          <!--          被告信息审查组件-->
          <defendant-info-verification v-if="3==this.activeStep"
                                       :caseInfoForm="caseInfoForm"
                                       :display-form="displayForm"
                                       @select-id-card="handleIdCardSelection"
                                       @select-license="handleLicenseSelection"></defendant-info-verification>
          <!--          其他信息审查组件-->
          <invoice-verification v-if="4==this.activeStep"
                                :caseInfoForm="caseInfoForm"
                                :display-form="displayForm"
                                @select-invoice="handleInvoiceSelection"
                                @skip="handleSkip"></invoice-verification>
          <!--          完成立案审查组件-->
          <finish-verification v-if="5==this.activeStep"
                               :caseInfoForm="caseInfoForm"
                               :display-form="displayForm"></finish-verification>
          <br>
          <br>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>

</style>
