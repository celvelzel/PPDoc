<script>
import axios from "axios";

export default {
  props: {
    allinfo: String
  },
  data() {
    return {
      outputResult: '',
      options: [{
        value: '1',
        label: '非常简短的摘要（一句话）'
      }, {
        value: '2',
        label: '简短的摘要（几句话）'
      }, {
        value: '3',
        label: '中等长度的摘要（段落）'
      }, {
        value: '4',
        label: '详细摘要（多个段落）'
      }, {
        value: '5',
        label: '执行摘要（针对专业或商务文档）'
      }],
      value: ''
    };
  },
  methods: {
    exportResult() {
      // 这里可以添加生成摘要的处理逻辑
      console.log('生成摘要');
      //清空输出框
      this.outputResult = ``;
      // 获取摘要选项和文本
      var summary_option = this.value;
      var ocr_text = this.allinfo;
      const postData = {
        summary_option: summary_option,
        ocr_text: ocr_text
      };

      // 发送请求到后端 API
      axios.post('http://localhost:8080/generatesummary', postData)
          .then(response => {
            console.log('生成摘要请求成功');
            this.outputResult = JSON.stringify(response.data);
          })
          .catch(error => {
            console.error('生成摘要请求失败', error);
          });
    },
    clearHistory() {
      // 这里可以添加清空历史会话的处理逻辑
      this.outputResult = '';
      this.summary_option = '';
      console.log('清空历史会话');
    }
  }
}
</script>

<template>
  <el-col>
    <el-form label-width="80px" style="margin-top: 10px;margin-bottom: 10px">
      <el-form-item label="摘要选项" label-width="200px" rows="1" align="left">
        <el-select v-model="value" clearable placeholder="请选择摘要长度">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item align="center">
        <el-button type="primary" @click="exportResult">生成摘要</el-button>
        <el-button @click="clearHistory">清空</el-button>
      </el-form-item>
    </el-form>
    <el-input
        type="textarea" readonly="true" :rows="10" placeholder="输出结果" v-model="outputResult"  style="white-space: pre-wrap;"
    ></el-input>
  </el-col>
</template>

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
