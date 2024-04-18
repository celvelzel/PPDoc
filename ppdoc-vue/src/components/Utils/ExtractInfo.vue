<!--向大模型传递关键字提取信息的组件-->
<template>
  <!-- 输出结果和按钮 -->
  <el-col>
    <el-form label-width="80px" style="margin-top: 10px;margin-bottom: 10px">
      <el-form-item label="待抽取字段" label-width="85px" rows="4">
        <el-input type="textarea"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  v-model="fields"
                  placeholder="多个字段需要用逗号分隔，如姓名，性别，年龄"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="exportResult">提取结果</el-button>
        <el-button @click="clearHistory">清空历史会话</el-button>
      </el-form-item>
    </el-form>
    <el-input
        type="textarea" readonly="true" :rows="10" placeholder="输出结果" v-model="outputResult"
    ></el-input>
  </el-col>
</template>

<script>
import axios from "axios";

export default {
  props: {
    allinfo: String
  },
  data() {
    return {
      fields: "",
      outputResult: ''
    };
  },
  methods: {
    exportResult() {
      // 这里可以添加导出结果的处理逻辑
      console.log('提取结果');
      // 获取待抽取字段的值
      var fieldsToExtract = this.fields;
      var ocr_text = this.allinfo;
      const postData = {
        fields: fieldsToExtract,
        ocr_text: ocr_text
      };

      // 发送请求到后端 API
      axios.post('http://localhost:8080/extractinfo', postData)
          .then(response => {
            // 处理后端返回的响应
            console.log('导出结果请求成功');
            this.outputResult = JSON.stringify(response.data);
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
}
</script>

<style scoped>
body {
  background-color: #f5f5f5;
  font-family: 'Arial', sans-serif;
  margin: 0;
  padding: 0;
}

.el-col {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-form {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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
