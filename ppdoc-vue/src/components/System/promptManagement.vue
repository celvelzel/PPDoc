<script>
export default{
  data(){
    return{
      textarea:''
    }
  }
}
</script>

<template>
  <div>
    <h1>提示词管理</h1>
    <el-input
        type="textarea"
        placeholder="请输入内容"
        v-model="textarea"
        maxlength="400"
        show-word-limit
    >
    </el-input>
  </div>
</template>

<style scoped>

</style>
