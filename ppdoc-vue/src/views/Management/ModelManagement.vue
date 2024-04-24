<script>
import axios from "axios";

export default {
  data() {
    return {
      tableData: [],
    }
  },
  methods: {
    handleSelect(index, row) {
      console.log(index, row);
      axios.post('http://localhost:8080/models', {
        modelName: row.modelName
      }).then(res => {
        console.log(res);
        if (res.data.code === 200) {
          this.$message({
            showClose: true,
            message: '切换模型成功',
            type: 'success'
          });
        } else {
          this.$message({
            showClose: true,
            message: '切换模型失败',
            type: 'error'
          });
        }
      })
    },
  }
}
</script>

<template>

</template>

<style scoped>

</style>
