<!-- InvoiceView.vue -->
<template>
  <div>
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header style="font-size: 35px;background-color: #B3C0D1; color: #333;text-align: left;line-height: 60px">
        PPDoc
      </el-header>
      <el-container>
        <el-aside class="custom-side" width="201px" style="background-color: rgb(238, 241, 246)">
          <el-menu :default-openeds="['1', '3']" style="text-align: left;font-size: 14px;color: #333;display: flex;padding: 0px">
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-menu"></i>
                文档录入
              </template>
              <el-menu-item-group>
                <el-menu-item index="1-1">通用文档录入</el-menu-item>
                <el-menu-item index="1-2">发票录入</el-menu-item>
                <el-menu-item index="1-3">营业执照录入</el-menu-item>
                <el-menu-item index="1-4">起诉状录入</el-menu-item>
                <el-menu-item index="1-5">身份证录入</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main>
          <InvoiceUpload></InvoiceUpload><!-- 发票组件 -->
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import InvoiceUpload from "@/components/InvoiceUpload.vue";
export default {
  components: {
    InvoiceUpload
  },
  data() {
    return {
      imageUrl: '',  // 发票图片预览地址
      fileList: [],  // 上传的文件列表
      invoiceForm: {  // 发票信息表单
        invoiceNumber: '',
        invoiceAmount: '',
        invoiceDate: ''
      }
    };
  },
  methods: {
    handlePreview(file) {
      this.imageUrl = file.url;
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    onSubmit() {
      console.log('提交发票信息');
      // 在这里处理表单提交逻辑
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
