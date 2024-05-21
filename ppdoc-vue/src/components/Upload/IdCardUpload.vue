<template>
  <el-container>
    <el-main>
      <el-row class="centered-row bg">
        <el-col class="centered-row">
          <el-upload action="http://localhost:8080/api/idcards/pdf/upload"
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
          </el-upload>
        </el-col>
        <el-col class="centered-row">
          <el-upload action="http://localhost:8080/api/idcards/image/upload"
                     list-type="picture-card"
                     :on-preview="handlePictureCardPreview"
                     :on-remove="handleRemove"
                     :before-upload="beforeUpload"
                     :on-success="handleSuccessImage">
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-col>
      </el-row>
      <!-- PDF预览组件-->
      <iframe :src="`static/pdf/web/viewer.html?file=`+pdfUrl" width="100%" height="350"></iframe>
      <!-------------->
      <el-row :gutter="30" style="margin-top: 10px;">
        <el-col :span="12">
          <!-- 表单-->
          <el-form ref="form" :model="userInfoForm" label-width="80px">
            <el-form-item label="姓名">
              <el-input v-model="userInfoForm.name"></el-input>
            </el-form-item>
            <el-form-item label="民族">
              <el-input v-model="userInfoForm.nation"></el-input>
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="userInfoForm.sex">
                <el-radio label="男"></el-radio>
                <el-radio label="女"></el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="住址">
              <el-input type="textarea" v-model="userInfoForm.address"></el-input>
            </el-form-item>
            <el-form-item label="身份证号">
              <el-input v-model="userInfoForm.cardNumber"></el-input>
            </el-form-item>
            <el-form-item label="所有文本">
              <el-input v-model="userInfoForm.allInfo"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit">提交</el-button>
              <el-button>取消</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="12">
          <extract-info :allinfo="userInfoForm.allInfo"/>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import ExtractInfo from "@/components/Utils/ExtractInfo.vue";
import axios from "axios";

export default {
  components: {ExtractInfo},
  data() {
    return {
      userInfoForm: {
        name: undefined,
        nation: undefined,
        address: undefined,
        cardNumber: '',
        sex: undefined,
        birthday: '',
        allInfo: ''
      },
      dialogImageUrl: '',
      limit: 1,
      dialogVisible: false,
      fileList: [],
      pdfUrl: "",
      fileName: "",
      fullscreenLoading: false
    };
  },
  methods: {
    // element UI中el-upload图片上传成功时的回调，详情看官网
    // https://element.eleme.cn/#/zh-CN/component/upload
    handleSuccessImage(response) {
      console.log(response)
      this.pdfUrl = response.data.url;
      this.userInfoForm = response.data.data;
      //表格收到数据后关闭加载动效
      this.fullscreenLoading = false;
    },
    handleSuccessPdf(response, file) {
      this.pdfUrl = response.data.url;
      console.log("文档的url是：" + response.data.url);
      this.userInfoForm = response.data.data;
      //表格收到数据后关闭加载动效
      this.fullscreenLoading = false;
      //获取文档名
      this.fileName = file.name;

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
    },
    beforeUpload() {
      this.fullscreenLoading = true;
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
      if (fileList.length == 0) {
        this.userInfoForm = {
          name: undefined,
          nation: undefined,
          address: undefined,
          cardNumber: undefined,
          sex: undefined,
          birthday: undefined
        }
      }
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },

    // 发送axios请求，将表单数据保存的数据库中
    onSubmit() {
      axios.post('http://localhost:8080/api/idcards', {
        id: "",
        document_Id: "",
        id_card_url: this.pdfUrl,
        file_name: this.fileName,
        name: this.userInfoForm.name,
        nation: this.userInfoForm.nation,
        sex: this.userInfoForm.sex,
        birthday: this.userInfoForm.birthday,
        address: this.userInfoForm.address,
        card_number: this.userInfoForm.cardNumber,
        all_info: this.userInfoForm.allInfo
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
};
</script>

<style scoped>
/* 自定义CSS类，用于居中对齐 */
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
