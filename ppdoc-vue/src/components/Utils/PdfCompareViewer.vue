<script>
export default {
  props: {
    pdfUrl:"",
    ocrPdfUrl:"",
  },
}
</script>

<template>
  <el-row gutter="20">
    <el-col :span="12">
      <h1>原文档</h1>
      <!-- PDF预览组件-->
      <iframe :src="`static/pdf/web/viewer.html?file=`+pdfUrl" width="100%" height="800"></iframe>
      <!-------------->
    </el-col>
    <el-col :span="12">
      <h1>识别文档</h1>
      <!-- PDF预览组件-->
      <iframe :src="`static/pdf/web/viewer.html?file=`+ocrPdfUrl" width="100%" height="800"></iframe>
      <!-------------->
    </el-col>
  </el-row>
</template>

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

el-row {
  border: 1px solid rgba(0, 0, 0, 0.1);
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 5px;
  background-color: #f5f5f5;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}
</style>