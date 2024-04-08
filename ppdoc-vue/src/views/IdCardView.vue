<!-- IdCardView.vue -->
<template>
  <div>
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header style="font-size: 35px;background-color: #B3C0D1; color: #333;text-align: left;line-height: 60px">
        身份证录入
      </el-header>
      <el-container>
        <el-aside class="custom-side" width="200px" style="background-color: rgb(238, 241, 246)">
          <MyMenu></MyMenu>
        </el-aside>
        <el-main>
          <IdCardUpload></IdCardUpload><!-- 身份证组件 -->
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import IdCardUpload from "@/components/IdCardUpload.vue";
import axios from 'axios'
import MyMenu from "@/components/MyMenu.vue"

export default {
  components: {
    IdCardUpload,
    MyMenu
  },
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
      fields: "",
      outputResult: '',
      dialogImageUrl: '',
      dialogVisible: false,
      fileList: []
      //{name: 'test.pdf', url: ''}
    };
  },
  methods: {
    // element UI中el-upload图片上传成功时的回调，详情看官网
    // https://element.eleme.cn/#/zh-CN/component/upload
    handleSuccessImage(response) {
      console.log(response)
      this.userInfoForm = response
    },
    /**
     * PDF文件上传成功后的回调函数
     * @param {Object} response 上传后的响应数据
     * @param {Object} file 上传的文件对象
     * @param {Array} fileList 文件列表
     */
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
      this.userInfoForm = response
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
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },

    // 发送axios请求，将表单数据保存的数据库中
    onSubmit() {
      // 这里可以发送axios请求，将表单数据保存到数据库中
    },
    exportResult() {
      // 这里可以添加导出结果的处理逻辑
      console.log('导出结果');
      // 获取待抽取字段的值
      var fieldsToExtract = this.fields;
      var ocr_text = this.userInfoForm.allInfo;
      const postData = {
        fields: fieldsToExtract,
        ocr_text: ocr_text
      };

      // 发送请求到后端 API
      axios.post('/extractinfo', postData)
          .then(response => {
            // 处理后端返回的响应

            console.log('导出结果请求成功');
            this.outputResult = JSON.stringify(response.data);
            // 这里可以进行文件下载或其他操作
          })
          .catch(error => {
            // 处理错误情况
            console.error('导出结果请求失败', error);
          });
    },
    clearHistory() {
      // 这里可以添加清空历史会话的处理逻辑
      this.outputResult = '';
      this.fields.field = '';
      console.log('清空历史会话');
    }
  }
};
</script>

<style scoped>
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
