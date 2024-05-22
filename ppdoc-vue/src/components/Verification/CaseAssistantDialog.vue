<script>
import axios from "axios";
import PdfCompareViewer from "@/components/Utils/PdfCompareViewer.vue";

export default {
  components: {PdfCompareViewer},
  data() {
    return {
      // 在线上传文件相关
      userInfoForm: {
        id: "",
        name: undefined,
        nation: undefined,
        address: undefined,
        cardNumber: '',
        sex: undefined,
        birthday: '',
        allInfo: ''
      },
      licenseInfoForm: {  // 营业执照信息表单
        license_id: "",
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
        licenseDomicile: '',
        allInfo: ''
      },
      licenseOperationPeriod: [],
      invoiceInfoForm: {  // 发票信息表单
        invoiceInfoForm: '',
        invoiceCode: '',
        invoiceNumber: '',
        invoiceAmount: '',
        invoiceDate: '',
        allInfo: '',
        purchaserName: '',
        sellerName: '',
        projectName: ''
      },
      pdfUrl: "",
      ocrPdfUrl: "",
      isConverted: "",
      fileName: "",
      fullscreenLoading: false,
      limit: 1,

      //  弹窗相关
      materialTypeDialogVisible: "",
      submitProcess: "",
      submitDialogVisible: false,
      previewDialogVisible: false,
      confirmDialogVisible: false,
      tempMaterialData: [],
      fileList: [],
      materialType: "",
      materialTypeName: "",
      submitOption: "",
      totalSteps: 4,
      tableData: [],
      total: 0,
      page: 1,
      pageSize: 10,
    }
  },
  props: {
    handleNode: "",
    caseInfoForm: {
      case_id: "",
      indictment_id: "",
      case_type: "",
      plaintiff_name: "",
      plaintiff_id: "",
      plaintiff_type: "",
      defendant_name: "",
      defendant_id: "",
      defendant_type: "",
      plaintiff_id_card_id: "",
      defendant_id_card_id: "",
      plaintiff_license_id: "",
      is_plaintiff_submit: "",
      is_defendant_submit: "",
      is_related_submit: "",
    },
  },
  methods: {
    handleExit() {
      this.materialTypeDialogVisible = false;
      this.submitDialogVisible = false;
      this.previewDialogVisible = false;
      this.confirmDialogVisible = false;
      this.$emit('exit-dialog');
    },
    handleMaterialTypeSelected() {
      console.log("已选择材料类型：" + this.materialType)
      switch (this.materialType) {
        case 1:
          this.materialTypeName = "身份证";
          break;
        case 2:
          this.materialTypeName = "营业执照";
          break;
        case 3:
          this.materialTypeName = "发票";
          break;
      }
      this.materialTypeDialogVisible = false;
      this.submitProcess += 1;
      this.submitDialogVisible = true;
    },
    handlePreviewEnd() {
      this.submitProcess += 1;
      this.previewDialogVisible = false;
      this.submitDialogVisible = true;
    },
    handleIdCardSelect(index, row) {
      this.$confirm('确认选择该记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success',
      }).then(() => {
        this.$message({
          type: 'success',
          message: '选择成功!'
        });
        //确定选择，处理逻辑
        this.tempMaterialData = row;
        if (this.handleNode.getModel().label === "原告提交证据") {
          this.caseInfoForm.plaintiff_id_card_id = row.id;
        } else if (this.handleNode.getModel().label === "被告提交证据") {
          this.caseInfoForm.defendant_id_card_id = row.id;
        }
        this.submitProcess += 1;
        this.submitDialogVisible = false;
        this.previewDialogVisible = true;
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消选择'
        });
      });
    },
    handleLicenseSelect(index, row) {
      this.$confirm('确认选择该记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success',
      }).then(() => {
        this.$message({
          type: 'success',
          message: '选择成功!'
        });
        //确定选择，处理逻辑
        this.tempMaterialData = row;
        if (this.handleNode.getModel().label === "原告提交证据") {
          this.caseInfoForm.plaintiff_license_id = row.id;
        } else if (this.handleNode.getModel().label === "被告提交证据") {
          this.caseInfoForm.defendant_license_id = row.id;
        }
        this.submitProcess += 1;
        this.submitDialogVisible = false;
        this.previewDialogVisible = true;
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消选择'
        });
      });
    },
    handleInvoiceSelect(index, row) {
      this.$confirm('确认选择该记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success',
      }).then(() => {
        this.$message({
          type: 'success',
          message: '选择成功!'
        });
        //确定选择，处理逻辑
        console.log(index, row);
        this.tempMaterialData = row;
        this.caseInfoForm.invoice_id = row.id;
        this.submitProcess += 1;
        this.submitDialogVisible = false;
        this.previewDialogVisible = true;
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消选择'
        });
      });
    },
    handleSubmitOptionSelected() {
      if (this.submitOption === 1) {
        if (this.materialType === 1) {
          // 个人身份证
          axios.get('http://localhost:8080/api/idcards', {
            params: {
              page: this.page,
              pageSize: this.pageSize,
            }
          }).then(res => {
            this.tableData = res.data.data.rows;
            this.total = res.data.data.total;
          });
        } else if (this.materialType === 2) {
          // 企业营业执照
          axios.get('http://localhost:8080/api/licenses', {
            params: {
              page: this.page,
              pageSize: this.pageSize,
            }
          }).then(res => {
            this.tableData = res.data.data.rows;
            this.total = res.data.data.total;
          });
        } else if (this.materialType === 3) {
          // 发票
          axios.get("http://localhost:8080/api/invoices", {
            params: {
              page: this.page,
              pageSize: this.pageSize,
            }
          }).then(res => {
            this.tableData = res.data.data.rows;
            this.total = res.data.data.total;
          })
        }
      }
    },
    highLightIdCardRow({row}) {
      if (row.card_number === this.caseInfoForm.plaintiff_id) {
        // 行数据中的身份证号码与组件状态中的原告或被告身份证号码之一相匹配
        console.log("行数据与组件状态中的数据相匹配")
        return 'highlight-row';
      }
    },
    // 文件上传成功回调函数
    handleSuccess(response, file) {
      switch (this.materialType) {
        case 1:
          this.userInfoForm = response.data.data;
          break;
        case 2:
          this.licenseInfoForm = response.data.data;
          break;
        case 3:
          this.invoiceInfoForm = response.data.data;
          break;
      }
      if (response.msg === "converted") {
        this.$message("文档已转换为双层pdf文档");
        this.isConverted = true;
      } else {
        this.$message("文档有可复制文本，未进行识别和转换");
        this.isConverted = false;
      }
      // 获取文件的url
      this.pdfUrl = response.data.url;
      this.ocrPdfUrl = response.data.ocrPdfUrl;
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
    // 上传文件之前钩子函数
    beforeUpload() {
      this.fullscreenLoading = true;
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    // 文件超出个数限制时的钩子函数
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    submitUpload() {
      this.submitProcess += 1;
      this.submitDialogVisible = false;
      this.previewDialogVisible = true;
    },
    relateIdCard() {
      // 绑定材料和案件
      if (this.handleNode.getModel().label === "原告提交证据") {
        axios.put('http://localhost:8080/api/cases', {
          case_id: this.caseInfoForm.case_id,
          plaintiff_id: this.userInfoForm.cardNumber,
          plaintiff_name: this.userInfoForm.name,
          plaintiff_id_card_id: this.userInfoForm.id,
          is_plaintiff_submit: true,
        }).then(res => {
          if (res.data.code === 200) {
            this.$message({
              showClose: true,
              message: '证据材料提交成功',
              type: 'success'
            });
          } else {
            this.$message({
              showClose: true,
              message: '证据材料提交失败',
              type: 'error'
            });
          }
          this.submitProcess = 1;
          this.confirmDialogVisible = false;
          this.$emit('exit-dialog');
        })
      } else if (this.handleNode.getModel().label === "被告提交证据") {
        axios.put('http://localhost:8080/api/cases', {
          case_id: this.caseInfoForm.case_id,
          defendant_id: this.userInfoForm.cardNumber,
          defendant_name: this.userInfoForm.name,
          defendant_id_card_id: this.userInfoForm.id,
          is_defendant_submit: true,
        }).then(res => {
          if (res.data.code === 200) {
            this.$message({
              showClose: true,
              message: '证据材料提交成功',
              type: 'success'
            });
          } else {
            this.$message({
              showClose: true,
              message: '证据材料提交失败',
              type: 'error'
            });
          }
          this.submitProcess = 1;
          this.confirmDialogVisible = false;
          this.$emit('exit-dialog');
        })
      }
    },
    relateLicense() {
      // 绑定材料和案件
      if (this.handleNode.getModel().label === "原告提交证据") {
        axios.put('http://localhost:8080/api/cases', {
          case_id: this.caseInfoForm.case_id,
          plaintiff_id: this.licenseInfoForm.licenseCode,
          plaintiff_name: this.licenseInfoForm.licenseEnterpriseName,
          plaintiff_license_id: this.licenseInfoForm.license_id,
          is_plaintiff_submit: true,
        }).then(res => {
          if (res.data.code === 200) {
            this.$message({
              showClose: true,
              message: '证据材料提交成功',
              type: 'success'
            });
          } else {
            this.$message({
              showClose: true,
              message: '证据材料提交失败',
              type: 'error'
            });
          }
          this.submitProcess = 1;
          this.confirmDialogVisible = false;
          this.$emit('exit-dialog');
        })
      } else if (this.handleNode.getModel().label === "被告提交证据") {
        axios.put('http://localhost:8080/api/cases', {
          case_id: this.caseInfoForm.case_id,
          defendant_id: this.licenseInfoForm.licenseCode,
          defendant_name: this.licenseInfoForm.licenseEnterpriseName,
          defendant_license_id: this.licenseInfoForm.license_id,
          is_defendant_submit: true,
        }).then(res => {
          if (res.data.code === 200) {
            this.$message({
              showClose: true,
              message: '证据材料提交成功',
              type: 'success'
            });
          } else {
            this.$message({
              showClose: true,
              message: '证据材料提交失败',
              type: 'error'
            });
          }
          this.submitProcess = 1;
          this.confirmDialogVisible = false;
          this.$emit('exit-dialog');
        })
      }
    },
    relateInvoice() {
      // 绑定材料和案件
      axios.put('http://localhost:8080/api/cases', {
        case_id: this.caseInfoForm.case_id,
        related_invoice_id: this.invoiceInfoForm.invoice_id,
        is_related_submit: true,
      }).then(res => {
        if (res.data.code === 200) {
          this.$message({
            showClose: true,
            message: '证据材料提交成功',
            type: 'success'
          });
        } else {
          this.$message({
            showClose: true,
            message: '证据材料提交失败',
            type: 'error'
          });
        }
        this.submitProcess = 1;
        this.confirmDialogVisible = false;
        this.$emit('exit-dialog');
      })
    },
    highLightLicenseRow({row}) {
      if (row.license_code === this.caseInfoForm.plaintiff_id) {
        console.log("行数据与组件状态中的数据相匹配")
        return 'highlight-row';
      }
    },
    handleCurrentChange(val) {
      this.page = val;
      if (this.caseInfoForm.plaintiff_type == "个人") {
        axios.get('http://localhost:8080/api/idcards', {
          params: {
            page: val,
            pageSize: this.pageSize,
          }
        }).then(res => {
          this.tableData = res.data.data.rows;
          this.total = res.data.data.total;
        });
      } else {
        axios.get('http://localhost:8080/api/licenses', {
          params: {
            page: val,
            pageSize: this.pageSize,
          }
        }).then(res => {
          this.tableData = res.data.data.rows;
          this.total = res.data.data.total;
        });
      }
      console.log(`当前页: ${val}`);
    },
    handleSizeChange(val) {
      this.pageSize = val;
      if (this.caseInfoForm.plaintiff_type == "个人") {
        axios.get('http://localhost:8080/api/idcards', {
          params: {
            page: this.page,
            pageSize: val,
          }
        }).then(res => {
          this.tableData = res.data.data.rows;
          this.total = res.data.data.total;
        });
      } else {
        axios.get('http://localhost:8080/api/licenses', {
          params: {
            page: this.page,
            pageSize: val,
          }
        }).then(res => {
          this.tableData = res.data.data.rows;
          this.total = res.data.data.total;
        });
      }
    },
  }
  ,
  mounted() {
    this.materialTypeDialogVisible = true;
    this.submitProcess = 1;
    console.log("组件刷新");
  }
  ,
}
</script>

<template>
  <!-- 1证据类型选择对话框-->
  <div>
    <el-dialog
        :visible.sync="materialTypeDialogVisible"
        title="证据提交"
        width="50%"
        :close-on-click-modal="false">
      <el-progress :percentage="(100 * submitProcess / totalSteps)  "></el-progress>
      <br>
      <h1>请选择证据材料类型：</h1>
      <br>
      <el-radio-group v-model="materialType">
        <el-radio :label="1">身份证</el-radio>
        <el-radio :label="2">营业执照</el-radio>
        <el-radio :label="3">发票</el-radio>
      </el-radio-group>
      <br>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleExit">取 消</el-button>
        <el-button type="primary"
                   @click="handleMaterialTypeSelected">下一步</el-button>
      </span>
    </el-dialog>

    <!-- 2证据提交对话框-->
    <el-dialog
        :visible.sync="submitDialogVisible"
        :title="materialTypeName+'证据提交'"
        width="50%"
        :close-on-click-modal="false">
      <el-progress :percentage="(100 * submitProcess / totalSteps)  "></el-progress>
      <br>
      <el-radio-group v-model="submitOption" @input="handleSubmitOptionSelected">
        <el-radio :label="1">从现有证据材料中选择</el-radio>
        <el-radio :label="2">上传新的证据材料</el-radio>
      </el-radio-group>
      <br>
      <!--从现有材料中上传-->
      <div v-if="submitOption === 1">
        <!--材料类型为身份证-->
        <div v-if="materialType === 1">
          <el-table :data="tableData"
                    border
                    stripe
                    :row-class-name="highLightIdCardRow">
            <el-table-column prop="name" label="姓名" width="80"></el-table-column>
            <el-table-column prop="sex" label="性别" width="50"></el-table-column>
            <el-table-column prop="nation" label="民族" width="50"></el-table-column>
            <el-table-column prop="address" label="住址" width="200"></el-table-column>
            <el-table-column prop="card_number" label="身份证号" width="180"></el-table-column>
            <el-table-column fixed="right" label="操作">
              <template slot-scope="scope">
                <el-button
                    size="mini"
                    align="center"
                    type="success"
                    @click="handleIdCardSelect(scope.$index, scope.row)">选择
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!--材料类型为营业执照-->
        <div v-if="materialType === 2">
          <el-table :data="tableData"
                    border
                    stripe
                    :row-class-name="highLightLicenseRow">
            <el-table-column prop="license_code" label="统一社会信用代码" width="175"></el-table-column>
            <el-table-column prop="license_number" label="证照编号" width="170"></el-table-column>
            <el-table-column prop="license_enterprise_name" label="企业名称" width="180"></el-table-column>
            <el-table-column prop="license_legal_representative" label="法定代表人" width="100"></el-table-column>
            <el-table-column fixed="right" label="操作">
              <template slot-scope="scope">
                <el-button
                    size="mini"
                    align="center"
                    type="success"
                    @click="handleLicenseSelect(scope.$index, scope.row)">选择
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!--材料类型为发票-->
        <div v-if="materialType === 3">
          <el-table :data="tableData"
                    border
                    stripe>
            <el-table-column prop="invoice_code" label="发票代码" width="120"></el-table-column>
            <el-table-column prop="invoice_number" label="发票号码" width="100"></el-table-column>
            <el-table-column prop="invoice_amount" label="发票金额" width="70"></el-table-column>
            <el-table-column prop="invoice_data" label="开票日期" width="100"></el-table-column>
            <el-table-column prop="purchaser_name" label="购买方名称" width="150"></el-table-column>
            <el-table-column prop="seller_name" label="销售方名称" width="150"></el-table-column>
            <el-table-column prop="project_name" label="项目名称" width="100"></el-table-column>
            <el-table-column fixed="right" label="操作">
              <template slot-scope="scope">
                <el-button
                    size="mini"
                    align="center"
                    type="success"
                    @click="handleInvoiceSelect(scope.$index, scope.row)">选择
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <br>
        <el-pagination
            background
            layout="total, sizes, prev, pager, next,jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :total="total">
        </el-pagination>
      </div>

      <!--上传新的材料-->
      <div v-if="submitOption === 2">
        <br>
        <el-upload
            v-if="materialType ===1"
            action="http://localhost:8080/api/idcards/upload"
            :on-success="handleSuccess"
            :on-remove="handleRemove"
            :before-upload="beforeUpload"
            :before-remove="beforeRemove"
            :limit="1"
            :on-exceed="handleExceed"
            :file-list="fileList"

            v-loading.fullscreen.lock="fullscreenLoading"
            element-loading-text="加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)">
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传
          </el-button>
        </el-upload>
        <el-upload
            v-if="materialType ===2"
            action="http://localhost:8080/api/licenses/upload"
            :on-success="handleSuccess"
            :on-error="handleError"
            :file-list="fileList">
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传
          </el-button>
        </el-upload>
        <el-upload
            v-if="materialType ===3"
            action="http://localhost:8080/api/invoices/upload"
            :on-success="handleSuccess"
            :on-error="handleError"
            :file-list="fileList">
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传
          </el-button>
        </el-upload>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleExit">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 材料预览对话框 previewDialog-->
    <el-dialog :visible.sync="previewDialogVisible"
               title="材料预览"
               width="50%"
               :close-on-click-modal="false">
      <el-progress :percentage="(100 * submitProcess / totalSteps)  "></el-progress>
      <br>
      <!--选择已存在的材料-->
      <span v-if="this.submitOption === 1">
        <!--身份证-->
        <span v-if="this.materialType === 1">
          <pdf-compare-viewer :pdfUrl="this.tempMaterialData.id_card_url"
                              :ocrPdfUrl="this.tempMaterialData.id_card_ocr_url"></pdf-compare-viewer>
        </span>
        <!--营业执照-->
        <span v-if="this.materialType === 2">
          <pdf-compare-viewer :pdfUrl="this.tempMaterialData.license_url"
                              :ocrPdfUrl="this.tempMaterialData.license_ocr_url"></pdf-compare-viewer>
        </span>
        <!--发票-->
        <span v-if="this.materialType === 3">
          <pdf-compare-viewer :pdfUrl="this.tempMaterialData.invoice_url"
                              :ocrPdfUrl="this.tempMaterialData.invoice_ocr_url"></pdf-compare-viewer>
        </span>
        <!--上传新材料-->
      </span>
      <span v-if="this.submitOption === 2 && this.isConverted === true">
        <pdf-compare-viewer :pdfUrl="pdfUrl" :ocrPdfUrl="ocrPdfUrl"></pdf-compare-viewer>
      </span>
      <span v-if="this.submitOption === 2 && this.isConverted === false">
         <h1>原文档</h1>
        <!-- PDF预览组件-->
      <iframe :src="`static/pdf/web/viewer.html?file=`+pdfUrl" width="100%" height="800"></iframe>
        <!-------------->
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleExit">取 消</el-button>
        <el-button type="primary"
                   @click="handlePreviewEnd">下一步</el-button>
      </span>
    </el-dialog>

    <!-- 确认提交对话框-->
    <el-dialog :visible.sync="confirmDialogVisible"
               title="确认提交"
               width="50%"
               :close-on-click-modal="false">
      <el-progress :percentage="(100 * submitProcess / totalSteps)  "></el-progress>
      <br>
      <!--材料类型为身份证-->
      <span v-if="this.materialType === 1">
        <!--身份证信息表单-->
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
                <el-button type="primary" @click="relateIdCard">提交</el-button>
                <el-button @click="handleExit">取消</el-button>
              </el-form-item>
            </el-form>
      </span>

      <!--材料类型为营业执照-->
      <span v-if="this.materialType === 2">
        <!-- 营业执照信息表单 -->
            <el-form ref="form" label-width="auto" label-position="left">
              <el-form-item label="统一社会信用代码">
                <el-input v-model="licenseInfoForm.licenseCode"></el-input>
              </el-form-item>
              <el-form-item label="证照编号">
                <el-input v-model="licenseInfoForm.licenseNumber"></el-input>
              </el-form-item>
              <el-form-item label="名称">
                <el-input v-model="licenseInfoForm.licenseEnterpriseName"></el-input>
              </el-form-item>
              <el-form-item label="类型">
                <el-input v-model="licenseInfoForm.licenseEnterpriseType"></el-input>
              </el-form-item>
              <el-form-item label="法定代表人">
                <el-input v-model="licenseInfoForm.licenseLegalRepresentative"></el-input>
              </el-form-item>
              <el-form-item label="经营范围">
                <el-input v-model="licenseInfoForm.licenseBusinessScope"></el-input>
              </el-form-item>
              <el-form-item label="注册资本">
                <el-input v-model="licenseInfoForm.licenseRegisteredCapital"></el-input>
              </el-form-item>
              <el-form-item label="成立日期">
                <el-date-picker type="date" placeholder="选择日期" v-model="licenseInfoForm.licenseEstablishDate"
                                style="margin-right: 500px;"></el-date-picker>
              </el-form-item>
              <el-form-item label="营业期限">
                <el-date-picker type="daterange" range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                v-model="licenseOperationPeriod"
                                style="margin-right: 500px;"></el-date-picker>
              </el-form-item>
              <el-form-item label="住所">
                <el-input v-model="licenseInfoForm.licenseDomicile"></el-input>
              </el-form-item>
              <el-form-item label="所有文本">
                <el-input v-model="licenseInfoForm.allInfo"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="relateLicense">提交</el-button>
                <el-button @click="handleExit">取消</el-button>
              </el-form-item>
            </el-form>
      </span>

      <!--材料类型为发票-->
      <span v-if="this.materialType === 3">
        <!-- 发票信息表单 -->
            <el-form ref="form" :model="invoiceInfoForm" label-width="auto">
              <el-form-item label="发票代码">
                <el-input v-model="invoiceInfoForm.invoiceCode"></el-input>
              </el-form-item>
              <el-form-item label="发票号码">
                <el-input v-model="invoiceInfoForm.invoiceNumber"></el-input>
              </el-form-item>
              <el-form-item label="发票金额">
                <el-input v-model="invoiceInfoForm.invoiceAmount"></el-input>
              </el-form-item>
              <el-form-item label="开票日期">
                <el-date-picker type="date" placeholder="选择日期" v-model="invoiceInfoForm.invoiceDate"
                                style="margin-right: 500px;"></el-date-picker>
              </el-form-item>
              <el-form-item label="购买方名称">
                <el-input v-model="invoiceInfoForm.purchaserName"></el-input>
              </el-form-item>
              <el-form-item label="销售方名称">
                <el-input v-model="invoiceInfoForm.sellerName"></el-input>
              </el-form-item>
              <el-form-item label="项目名称">
                <el-input v-model="invoiceInfoForm.projectName"></el-input>
              </el-form-item>
              <el-form-item label="所有文本">
                <el-input v-model="invoiceInfoForm.allInfo"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="relateInvoice">提交</el-button>
                <el-button @click="handleExit">取消</el-button>
              </el-form-item>
            </el-form>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>
.el-table .highlight-row {
  background-color: #FFFF00 !important;
}
</style>