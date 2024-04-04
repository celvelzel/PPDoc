<!-- InvoiceUpload.vue -->
<template>
  <el-container>
    <el-main>
      <el-row>
        <el-col :span="12">
          <!-- 发票图片上传组件 -->
          <el-upload
              class="upload-demo"
              action="http://localhost:8080/pdfs"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :file-list="fileList"
              list-type="picture">
            <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
            <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器
            </el-button>
            <div slot="tip" class="el-upload__tip">只能上传pdf文件</div>
          </el-upload>
        </el-col>
        <el-col :span="12">
          <!-- 发票图片预览组件 -->
          <img v-if="imageUrl" :src="imageUrl" alt="发票预览" class="preview-image">
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <!-- 发票信息表单 -->
          <el-form ref="form" :model="invoiceForm" label-width="80px">
            <el-form-item label="发票号码">
              <el-input v-model="invoiceForm.invoiceNumber"></el-input>
            </el-form-item>
            <el-form-item label="发票金额">
              <el-input v-model="invoiceForm.invoiceAmount"></el-input>
            </el-form-item>
            <el-form-item label="发票日期">
              <el-date-picker type="date" placeholder="选择日期" v-model="invoiceForm.invoiceDate"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit">提交</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      imageUrl: '',  // 发票图片预览地址
      fileList: [],  // 上传的文件列表
      invoiceForm: {  // 发票信息表单
        invoiceNumber: '',
        invoiceAmount: '',
        invoiceDate: ''
      }
    };
  },
  methods: {
    handlePreview(file) {
      this.imageUrl = file.url;
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    submitUpload() {
      this.$refs.upload.submit();
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
</style>

