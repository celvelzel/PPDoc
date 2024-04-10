<!--营业执照组件-->
<template>
  <el-container>
    <el-main>
      <el-row>
      </el-row>
      <el-row type="flex" justify="space-between">
        <el-col :span="12">
          <!-- 营业执照pdf上传组件 -->
          <el-upload action="http://localhost:8080/licenses"
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
          <PDFViewer></PDFViewer>
        </el-col>
        <el-col :span="12">
          <el-row>
            <!-- 发票信息表单 -->
            <el-form ref="form" label-width="80px">
              <el-form-item label="统一社会信用代码">
                <el-input v-model="licenseForm.licenseCode"></el-input>
              </el-form-item>
              <el-form-item label="证照编号">
                <el-input v-model="licenseForm.licenseNumber"></el-input>
              </el-form-item>
              <el-form-item label="名称">
                <el-input v-model="licenseForm.licenseEnterpriseName"></el-input>
              </el-form-item>
              <el-form-item label="类型">
                <el-input v-model="licenseForm.licenseEnterpriseType"></el-input>
              </el-form-item>
              <el-form-item label="法定代表人">
                <el-input v-model="licenseForm.licenseLegalRepresentative"></el-input>
              </el-form-item>
              <el-form-item label="经营范围">
                <el-input v-model="licenseForm.licenseBusinessScope"></el-input>
              </el-form-item>
              <el-form-item label="注册资本">
                <el-input v-model="licenseForm.licenseRegisteredCapital"></el-input>
              </el-form-item>
              <el-form-item label="成立日期">
                <el-date-picker type="date" placeholder="选择日期" v-model="licenseForm.licenseEstablishDate"
                                style="margin-right: 500px;"></el-date-picker>
              </el-form-item>
              <el-form-item label="营业期限">
                <el-date-picker type="daterange" range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                v-model="licenseOperationPeriod"
                                style="margin-right: 500px;"></el-date-picker>
              </el-form-item>
              <el-form-item label="住所">
                <el-input v-model="licenseForm.licenseDomicile"></el-input>
              </el-form-item>
              <el-form-item label="所有文本">
                <el-input v-model="licenseForm.allInfo"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="onSubmit">提交</el-button>
              </el-form-item>
            </el-form>
          </el-row>
          <el-row>
            <extract-info :allinfo="licenseForm.allInfo"></extract-info>
          </el-row>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
//import axios from 'axios'
import ExtractInfo from "@/components/ExtractInfo.vue";
import PDFViewer from "@/components/PDFViewer.vue";

export default {
  components: {PDFViewer, ExtractInfo},
  data() {
    return {
      imageUrl: '',
      fileList: [],
      dialogImageUrl: '',
      dialogVisible: false,
      licenseForm: {  // 营业执照信息表单
        licenseCode: '',
        licenseNumber: '',
        licenseEnterpriseName: '',
        licenseEnterpriseType: '',
        licenseLegalRepresentative: '',
        licenseBusinessScope: '',
        licenseRegisteredCapital: '',
        licenseEstablishDate: '',
        licenseOperationPeriodStart: '',
        licenseOperationPeriodEnd: '',
        licenseDomicile: '',
        allInfo: ''
      },
      licenseOperationPeriod:[]
    }
  },
  methods: {
    handlePreview(file) {
      this.imageUrl = file.url;
    },
    handleSuccessPdf(response, file) {
      // 假设服务器返回的响应数据中包含了文件的URL
      const newFile = {
        name: file.name, // 文件名
        url: response.url // 服务器返回的文件URL
      };
      // 将新文件添加到fileList数组中
      this.fileList.push(newFile);
      // 如果有文件数量限制，需要进行相应的处理
      if (this.fileList.length > this.limit) {
        // 可以选择移除最早的文件
        this.fileList.shift();
      }
      console.log(response)
      this.licenseForm = response
      this.licenseOperationPeriod = [this.licenseForm.licenseOperationPeriodStart,
        this.licenseForm.licenseOperationPeriodEnd];
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
}
</script>

<style scoped>
.el-col {
  border: 1px solid rgba(0, 0, 0, 0.1);
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 5px;
  background-color: #f5f5f5;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}
.el-form-item__label {
  width: 150px;
  margin-bottom: 8px; /* 调整行间距 */
}

.el-header {
  background-color: #B3C0D1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.custom-aside {
  /* 边框和圆角 */
  border-radius: 4px;
  border: 1px solid #dcdfe6;

  /* 盒阴影 */
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  /* 内容与边框之间的距离 */
  padding: 10px;

  /* 保持内部组件居中对齐 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>
