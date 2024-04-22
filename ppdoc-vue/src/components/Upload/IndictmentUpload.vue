<template>
  <el-container>
    <el-main>
      <el-row>
      </el-row>
      <el-row type="flex" justify="space-between">
        <el-col :span="12">
          <!-- 发票pdf上传组件 -->
          <el-upload action="http://localhost:8080/indictments/upload"
                     :on-preview="handlePictureCardPreview"
                     :on-remove="handleRemove"
                     :on-success="handleSuccessPdf"
                     :before-upload="beforeUpload"
                     :before-remove="beforeRemove"
                     multiple
                     :limit="2"
                     :on-exceed="handleExceed"
                     :file-list="fileList"

                     v-loading.fullscreen.lock="fullscreenLoading"
                     element-loading-text="加载中"
                     element-loading-spinner="el-icon-loading"
                     element-loading-background="rgba(0, 0, 0, 0.8)">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传pdf文件</div>
            <!-- 发票图片预览组件 -->
            <img v-if="imageUrl" :src="imageUrl" alt="起诉状预览" class="preview-image">
          </el-upload>
          <br>
          <!-- PDF预览组件-->
          <iframe :src="`static/pdf/web/viewer.html?file=`+pdfUrl" width="600" height="570"></iframe>
          <!-------------->
          <br>
          <GenerateSummary :allinfo="indictmentInfoForm.allInfo"/>
        </el-col>
        <el-col :span="12">
          <el-row>
            <!-- 发票信息表单 -->
            <el-form ref="form" :model="indictmentInfoForm" label-width="80px">
              <el-form-item label="案件类型">
                <el-input v-model="indictmentInfoForm.caseType"></el-input>
              </el-form-item>
              <el-form-item label="原告名称">
                <el-input v-model="indictmentInfoForm.plaintiff_name"></el-input>
              </el-form-item>
              <el-form-item label="原告地址">
                <el-input v-model="indictmentInfoForm.plaintiff_address"></el-input>
              </el-form-item>
              <el-form-item label="被告名称">
                <el-input v-model="indictmentInfoForm.defendant_name"></el-input>
              </el-form-item>
              <el-form-item label="被告地址">
                <el-input v-model="indictmentInfoForm.defendant_address"></el-input>
              </el-form-item>

              <el-form-item label="起诉状日期">
                <el-date-picker type="date" placeholder="选择日期" v-model="indictmentInfoForm.indictment_date"
                                style="margin-right: 500px;"></el-date-picker>
              </el-form-item>

              <el-form-item label="所有文本">
                <el-input v-model="invoiceInfoForm.allInfo"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="onSubmit">提交</el-button>
              </el-form-item>
            </el-form>
          </el-row>
          <el-row>
            <extract-info :allinfo="invoiceInfoForm.allInfo"/>
          </el-row>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>

import {defineComponent} from "vue";
import GenerateSummary from "@/components/Utils/GenerateSummary.vue";
import ExtractInfo from "@/components/Utils/ExtractInfo.vue";

export default {
  components: {ExtractInfo, GenerateSummary},
  data() {
    return {
      indictmentInfoForm: {
        caseType: "",
        plaintiff_name: "",
        plaintiff_number: "",
        plaintiff_type: "",
        plaintiff_address: "",
        plaintiff_contact: "",
        defendant_name: "",
        defendant_number: "",
        defendant_type: "",
        defendant_address: "",
        defendant_contact: "",
        litigation_request: "",
        facts_background: "",
        legal_basis: "",
        evidence_list: "",
        court_name: "",
        indictment_date: "",
        all_info: "",
      },
    }
  }
}
</script>

<style scoped>

</style>
