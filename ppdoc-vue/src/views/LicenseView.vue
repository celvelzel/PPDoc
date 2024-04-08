<template>
  <div>
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-header style="font-size: 35px;background-color: #B3C0D1; color: #333;text-align: left;line-height: 60px">
        营业执照录入
      </el-header>
      <el-container>
        <el-aside class="custom-side" width="201px" style="background-color: rgb(238, 241, 246)">
          <el-menu :default-openeds="['1', '3']"
                   style="text-align: left;font-size: 14px;color: #333;display: flex;padding: 0px">
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
          <LicenseUpload></LicenseUpload><!-- 营业执照组件 -->
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import LicenseUpload from "@/components/LicenseUpload.vue"
export default{
  components: {LicenseUpload},
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
      }
    }
  }
}

</script>

<style scoped>

</style>
