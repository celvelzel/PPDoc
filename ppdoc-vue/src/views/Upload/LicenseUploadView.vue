<template>
  <div>
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header style="font-size: 35px;background-color: #B3C0D1; color: #333;text-align: left;line-height: 60px">
        营业执照录入
      </el-header>
      <el-container>
        <el-aside class="custom-side" width="201px" style="background-color: rgb(238, 241, 246)">
          <MyMenu></MyMenu>
        </el-aside>
        <el-main>
          <LicenseUpload></LicenseUpload><!-- 营业执照组件 -->
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import LicenseUpload from "@/components/Upload/LicenseUpload.vue"
import MyMenu from "@/components/Utils/MyMenu.vue"
export default{
  components: {LicenseUpload, MyMenu},
  data(){
    return {
      imageUrl: '',
      fileList: [],
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
      },
      licenseOperationPeriod: [this.licenseForm.licenseOperationPeriodStart,
        this.licenseForm.licenseOperationPeriodEnd]
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

</style>
