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
          <iframe :src="`static/pdf/web/viewer.html?file=`+pdfUrl" width="100%" height="1230"></iframe>
          <!-------------->
          <br>
          <GenerateSummary :allinfo="indictmentInfoForm.allInfo"/>
        </el-col>
        <el-col :span="12">
          <el-row>
            <!-- 起诉状信息表单 -->
            <el-form ref="form" :model="indictmentInfoForm" label-width="80px">
              <el-form-item label="案件类型">
                <el-input v-model="indictmentInfoForm.caseType"></el-input>
              </el-form-item>
              <el-form-item label="原告名称">
                <el-input v-model="indictmentInfoForm.plaintiffName"></el-input>
              </el-form-item>
              <el-form-item label="原告ID">
                <el-input v-model="indictmentInfoForm.plaintiffId"></el-input>
              </el-form-item>
              <el-form-item label="原告类型">
                <el-input v-model="indictmentInfoForm.plaintiffType"></el-input>
              </el-form-item>
              <el-form-item label="原告地址">
                <el-input v-model="indictmentInfoForm.plaintiffAddress"></el-input>
              </el-form-item>
              <el-form-item label="原告联系方式">
                <el-input v-model="indictmentInfoForm.plaintiffContact"></el-input>
              </el-form-item>
              <el-form-item label="被告名称">
                <el-input v-model="indictmentInfoForm.defendantName"></el-input>
              </el-form-item>
              <el-form-item label="被告ID">
                <el-input v-model="indictmentInfoForm.defendantId"></el-input>
              </el-form-item>
              <el-form-item label="被告类型">
                <el-input v-model="indictmentInfoForm.defendantType"></el-input>
              </el-form-item>
              <el-form-item label="被告地址">
                <el-input v-model="indictmentInfoForm.defendantAddress"></el-input>
              </el-form-item>
              <el-form-item label="被告联系方式">
                <el-input v-model="indictmentInfoForm.defendantContact"></el-input>
              </el-form-item>
              <el-form-item label="诉讼请求">
                <el-input v-model="indictmentInfoForm.litigationRequest"></el-input>
              </el-form-item>
              <el-form-item label="事实背景">
                <el-input v-model="indictmentInfoForm.factsBackground"></el-input>
              </el-form-item>
              <el-form-item label="法律依据">
                <el-input v-model="indictmentInfoForm.legalBasis"></el-input>
              </el-form-item>
              <el-form-item label="证据清单">
                <el-input v-model="indictmentInfoForm.evidenceList"></el-input>
              </el-form-item>
              <el-form-item label="法院名称">
                <el-input v-model="indictmentInfoForm.courtName"></el-input>
              </el-form-item>
              <el-form-item label="起诉状日期">
                <el-date-picker type="date" placeholder="选择日期" v-model="indictmentInfoForm.indictmentDate"
                                style="margin-right: 500px;"></el-date-picker>
              </el-form-item>
              <el-form-item label="所有文本">
                <el-input v-model="indictmentInfoForm.allInfo"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="onSubmit">提交</el-button>
              </el-form-item>
            </el-form>
          </el-row>
          <el-row>
            <extract-info :allinfo="indictmentInfoForm.allInfo"/>
          </el-row>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import GenerateSummary from "@/components/Utils/GenerateSummary.vue";
import ExtractInfo from "@/components/Utils/ExtractInfo.vue";
import axios from "axios";

export default {
  components: {ExtractInfo, GenerateSummary},
  data() {
    return {
      indictmentInfoForm: {
        caseType: "",
        plaintiffName: "",
        plaintiffId: "",
        plaintiffType: "",
        plaintiffAddress: "",
        plaintiffContact: "",
        defendantName: "",
        defendantId: "",
        defendantType: "",
        defendantAddress: "",
        defendantContact: "",
        litigationRequest: "",
        factsBackground: "",
        legalBasis: "",
        evidenceList: "",
        courtName: "",
        indictmentDate: "",
        allInfo: "",
      },
      pdfUrl: "",
      fileName: "",
      fullscreenLoading: false,
      fileList: [],
      imageUrl: '',
    }
  },
  methods: {
    handleSuccessPdf(response, file) {
      // 假设服务器返回的响应数据中包含了起诉状文档的URL
      this.pdfUrl = response.data.url;
      console.log("起诉状的url是：" + response.data.url);
      this.indictmentInfoForm = response.data.data;
      //表格收到数据后关闭加载动效
      this.fullscreenLoading = false;
      // 更新数据的操作
      this.$emit('dataUpdated');
      //获取文档名
      this.fileName = file.name;

      const newFile = {
        name: file.name, // 文件名
        url: response.data.url // 服务器返回的文件URL
      };
      // 将新文件添加到fileList数组中
      this.fileList.push(newFile);
      // 如果有文件数量限制，需要进行相应的处理
      if (this.fileList.length > this.limit) {
        // 可以选择移除最早的文件
        this.fileList.shift();
      }
    },
    beforeUpload() {
      this.fullscreenLoading = true;
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    // 发送axios请求，将表单数据保存的数据库中
    onSubmit() {
      axios.post('http://localhost:8080/indictments', {
        indictment_id: "",
        document_id: "",
        indictment_url: this.pdfUrl,
        file_name: this.fileName,
        case_type: this.indictmentInfoForm.caseType,
        plaintiff_name: this.indictmentInfoForm.plaintiffName,
        plaintiff_id: this.indictmentInfoForm.plaintiffId,
        plaintiff_type: this.indictmentInfoForm.plaintiffType,
        plaintiff_address: this.indictmentInfoForm.plaintiffAddress,
        plaintiff_contact: this.indictmentInfoForm.plaintiffContact,
        defendant_name: this.indictmentInfoForm.defendantName,
        defendant_id: this.indictmentInfoForm.defendantId,
        defendant_type: this.indictmentInfoForm.defendantType,
        defendant_address: this.indictmentInfoForm.defendantAddress,
        defendant_contact: this.indictmentInfoForm.defendantContact,
        litigation_request: this.indictmentInfoForm.litigationRequest,
        facts_background: this.indictmentInfoForm.factsBackground,
        legal_basis: this.indictmentInfoForm.legalBasis,
        evidence_list: this.indictmentInfoForm.evidenceList,
        court_name: this.indictmentInfoForm.courtName,
        indictment_date: this.indictmentInfoForm.indictmentDate,
        all_info: this.indictmentInfoForm.allInfo,
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
  }
}
</script>

<style scoped>
.preview-image {
  max-width: 100%;
  height: auto;
}

.el-col {
  border: 1px solid rgba(0, 0, 0, 0.1);
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 5px;
  background-color: #f5f5f5;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}
</style>
