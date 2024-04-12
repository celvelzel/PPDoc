<template>
  <el-container>
    <el-main>
      <el-row class="centered-row bg">
        <el-col class="centered-row">
          <el-upload action="http://localhost:8080/docs"
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
          </el-upload>
        </el-col>
        <el-col class="centered-row">
          <el-upload action="http://localhost:8080/images"
                     list-type="picture-card"
                     :on-preview="handlePictureCardPreview"
                     :on-remove="handleRemove"
                     :on-success="handleSuccessImage">
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-col>
      </el-row>

      <el-row :gutter="30" style="margin-top: 10px;">
        <el-col :span="12">

          <!-- PDF预览组件-->
          <iframe :src="`static/pdf/web/viewer.html?file=`+pdfUrl" width="600" height="1050"></iframe>
          <!-------------->

        </el-col>
        <el-col :span="12">
          <!-- OCR结果表单-->
          <el-form ref="form" label-width="80px">
            <el-form-item label="所有文本">
              <el-input v-model="docInfoForm.allInfo"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit">提交表单</el-button>
              <el-button>取消</el-button>
            </el-form-item>
          </el-form>
<!--          摘要生成组件-->
          <GenerateSummary :allinfo="docInfoForm.allInfo"/>
<!--          ------------>
<!--          提取信息组件-->
          <extract-info :allinfo="docInfoForm.allInfo"/>
<!--          ------------>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import {defineComponent} from "vue";
import ExtractInfo from "@/components/ExtractInfo.vue";
import GenerateSummary from "@/components/GenerateSummary.vue";

export default defineComponent({
  components: {GenerateSummary, ExtractInfo},
  data() {
    return {
      docInfoForm: {
        allInfo: ''
      },
      limit: 3,
      dialogImageUrl: '',
      dialogVisible: false,
      fileList: [],
      pdfUrl:"",
    }
  },
  methods: {
    // element UI中el-upload图片上传成功时的回调，详情看官网
    // https://element.eleme.cn/#/zh-CN/component/upload
    handleSuccessImage(response) {
      console.log(response)
      this.userInfoForm = response
    },
    handleSuccessPdf(response, file) {
      this.pdfUrl = response.data.url;
      console.log("文档的url是："+response.data.url);
      this.docInfoForm = response.data.data;
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
      if (fileList.length == 0) {
        this.allInfo =  ""
      }
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
      // 这里可以发送axios请求，将表单数据保存到数据库中
    },
  }
})
</script>

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

.el-upload {
  margin-top: 10px;
}

.el-button {
  margin-right: 10px;
}

.center-text {
  text-align: center;
}
</style>
