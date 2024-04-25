<script>
import axios from "axios";

export default {
  data() {
    return {
      localCaseInfoForm:this.caseInfoForm,
      related_file_name:this.displayForm.related_file_name,
    }
  },
  props: {
    displayForm: [],
    caseInfoForm: [],
  },
  methods: {
    onSubmit() {
      axios.post('http://localhost:8080/cases', {
        case_id: null,
        indictment_id : this.localCaseInfoForm.indictment_id,
        case_type: this.localCaseInfoForm.case_type,
        plaintiff_name: this.localCaseInfoForm.plaintiff_name,
        plaintiff_id: this.localCaseInfoForm.plaintiff_id,
        plaintiff_type: this.localCaseInfoForm.plaintiff_type,
        defendant_name: this.localCaseInfoForm.defendant_name,
        defendant_id: this.localCaseInfoForm.defendant_id,
        defendant_type: this.localCaseInfoForm.defendant_type,
        plaintiff_id_card_id: this.localCaseInfoForm.plaintiff_id_card_id,
        defendant_id_card_id: this.localCaseInfoForm.defendant_id_card_id,
        plaintiff_license_id: this.localCaseInfoForm.plaintiff_license_id,
        defendant_license_id: this.localCaseInfoForm.defendant_license_id,
        related_invoice_id: this.localCaseInfoForm.related_invoice_id,
      }).then(res => {
            console.log(res);
            if (res.data.code === 200) {
              this.$message({
                showClose: true,
                message: '提交数据库成功',
                type: 'success'
              });
            } else {
              this.$message({
                showClose: true,
                message: '提交数据库失败',
                type: 'error'
              });
            }
          }
      )
    },
  },
}
</script>

<template>
  <el-container>
    <el-main>
      <el-row :gutter="30" style="margin-top: 10px;">
        <el-col :span="12">

          <!-- PDF预览组件-->
          <iframe :src="`static/pdf/web/viewer.html?file=`+this.displayForm.indictmentPdfUrl" width="100%" height="1050"></iframe>
          <!-------------->

        </el-col>
        <el-col :span="12">
          <el-form label-position="top" ref="form">
            <el-form-item label="起诉状编号">
              <el-input v-model="this.localCaseInfoForm.indictment_id"></el-input>
            </el-form-item>
            <el-form-item label="案件类型">
              <el-input v-model="this.localCaseInfoForm.case_type"></el-input>
            </el-form-item>
            <el-form-item label="原告名称">
              <el-input v-model="this.localCaseInfoForm.plaintiff_name"></el-input>
            </el-form-item>
            <el-form-item :v-if="'个人'==this.localCaseInfoForm.plaintiff_type" label="原告自然人身份证">
              <el-input v-model="this.localCaseInfoForm.plaintiff_id"></el-input>
            </el-form-item>
            <el-form-item :v-if="'企业'==this.localCaseInfoForm.plaintiff_type" label="原告企业统一信用代码">
              <el-input v-model="this.localCaseInfoForm.plaintiff_id"></el-input>
            </el-form-item>
            <el-form-item label="被告名称">
              <el-input v-model="this.localCaseInfoForm.defendant_name"></el-input>
            </el-form-item>
            <el-form-item :v-if="'个人'==this.localCaseInfoForm.defendant_type" label="被告自然人身份证">
              <el-input v-model="this.localCaseInfoForm.defendant_id"></el-input>
            </el-form-item>
            <el-form-item :v-if="'企业'==this.localCaseInfoForm.defendant_type" label="被告企业统一信用代码">
              <el-input v-model="this.localCaseInfoForm.defendant_id"></el-input>
            </el-form-item>
            <el-form-item label="相关发票">
              <el-input v-model="this.related_file_name"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit">提交</el-button>
              <el-button>取消</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<style scoped>
.centered-row {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
  margin-bottom: 10px;
}

.bg {
  background: #B3C0D1;
}

body {
  background-color: #f5f5f5;
  font-family: 'Arial', sans-serif;
  margin: 0;
  padding: 0;
}

.container {
  width: 100%;
  max-width: 960px; /* Adjust the max-width as needed */
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}

.el-row {
  margin-bottom: 20px;
}

.el-form {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-form-item {
  margin-bottom: 15px;
}
</style>
