<!-- InvoiceUpload.vue -->
<template>
  <el-container>
    <el-main>
      <el-row>
      </el-row>
      <el-row type="flex" justify="space-between">
        <el-col :span="12">
          <!-- 发票pdf上传组件 -->
          <el-upload action="http://localhost:8080/invoices"
                     :on-preview="handlePictureCardPreview"
                     :on-remove="handleRemove"
                     :on-success="handleSuccessPdf"
                     :before-remove="beforeRemove"
                     multiple
                     :limit="2"
                     :on-exceed="handleExceed"
                     :file-list="fileList">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传pdf文件</div>
            <!-- 发票图片预览组件 -->
            <img v-if="imageUrl" :src="imageUrl" alt="发票预览" class="preview-image">
          </el-upload>
          <br>
          <!-- PDF预览组件-->
          <iframe :src="`static/pdf/web/viewer.html?file=`+pdfUrl" width="600" height="570"></iframe>
          <!-------------->
          <br>
          <GenerateSummary :allinfo="invoiceInfoForm.allInfo"/>
        </el-col>
        <el-col :span="12">
          <el-row>
            <!-- 发票信息表单 -->
            <el-form ref="form" :model="invoiceInfoForm" label-width="80px">
              <el-form-item label="发票代码">
                <el-input v-model="invoiceInfoForm.invoiceCode"></el-input>
              </el-form-item>
              <el-form-item label="发票号码">
                <el-input v-model="invoiceInfoForm.invoiceNumber"></el-input>
              </el-form-item>
              <el-form-item label="发票金额">
                <el-input v-model="invoiceInfoForm.invoiceAmount"></el-input>
              </el-form-item>
              <el-form-item label="发票日期">
                <el-date-picker type="date" placeholder="选择日期" v-model="invoiceInfoForm.invoiceDate"
                                style="margin-right: 500px;"></el-date-picker>
              </el-form-item>
              <el-form-item label="购买方名称">
                <el-input v-model="invoiceInfoForm.purchaserName"></el-input>
              </el-form-item>
              <el-form-item label="销售方名称">
                <el-input v-model="invoiceInfoForm.sellerName"></el-input>
              </el-form-item>
              <el-form-item label="项目名称">
                <el-input v-model="invoiceInfoForm.projectName"></el-input>
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
// import axios from 'axios'

import ExtractInfo from "@/components/ExtractInfo.vue";
import GenerateSummary from "@/components/GenerateSummary.vue";

export default {
  components: {GenerateSummary, ExtractInfo},
  data() {
    return {
      imageUrl: '',  // 发票图片预览地址
      fileList: [],  // 上传的文件列表
      invoiceInfoForm: {  // 发票信息表单
        invoiceCode: '',
        invoiceNumber: '',
        invoiceAmount: '',
        invoiceDate: '',
        allInfo: '',
        purchaserName: '',
        sellerName: '',
        projectName: ''
      },
      pdfUrl:"",
    };
  },
  methods: {
    handlePreview(file) {
      this.imageUrl = file.url;
    },
    handleSuccessPdf(response, file) {
      // 假设服务器返回的响应数据中包含了发票的URL
      this.pdfUrl = response.data.url;
      console.log("发票的url是："+response.data.url);
      this.invoiceInfoForm = response.data.data;
      // 更新数据的操作
      this.$emit('dataUpdated');

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
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    submitUpload() {
      this.$refs.upload.submit();
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
    onSubmit() {
      console.log('提交发票信息');
      // 在这里处理表单提交逻辑
    }
  }
};
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

