<script>
import axios from "axios";

export default {
  data() {
    return {
      // 在线上传文件相关
      userInfoForm: {
        name: undefined,
        nation: undefined,
        address: undefined,
        cardNumber: '',
        sex: undefined,
        birthday: '',
        allInfo: ''
      },
      licenseInfoForm: {  // 营业执照信息表单
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
      invoiceInfoForm: {  // 发票信息表单
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
      submitOption: "",
      totalSteps: 4,
      tableData: [],
      total: 0,
      page: 1,
      pageSize: 10,
      // 点集
      nodes: [
        {
          id: '已完成', // String，该节点存在则必须，节点的唯一标识
          x: 100, // Number，可选，节点位置的 x 值
          y: 20, // Number，可选，节点位置的 y 值
          label: '已完成', // 节点文本
          class: 'c0',
        },
        {
          id: '进行中', // String，该节点存在则必须，节点的唯一标识
          x: 220, // Number，可选，节点位置的 x 值
          y: 20, // Number，可选，节点位置的 y 值
          label: '进行中', // 节点文本
          class: 'c1',
        },
        {
          id: '未开始', // String，该节点存在则必须，节点的唯一标识
          x: 340, // Number，可选，节点位置的 x 值
          y: 20, // Number，可选，节点位置的 y 值
          label: '未开始', // 节点文本
          class: 'c2',
        },
        {
          id: 'node1', // String，该节点存在则必须，节点的唯一标识
          x: 300, // Number，可选，节点位置的 x 值
          y: 250, // Number，可选，节点位置的 y 值
          label: '起诉状提交', // 节点文本
          class: 'c0',
          status: 'done',
        },
        {
          id: 'node2', // String，该节点存在则必须，节点的唯一标识
          x: 500, // Number，可选，节点位置的 x 值
          y: 150, // Number，可选，节点位置的 y 值
          label: '原告提交证据',
        },
        {
          id: 'node3', // String，该节点存在则必须，节点的唯一标识
          x: 500, // Number，可选，节点位置的 x 值
          y: 250, // Number，可选，节点位置的 y 值
          label: '被告提交证据', // 节点文本
        },
        {
          id: 'node4', // String，该节点存在则必须，节点的唯一标识
          x: 500, // Number，可选，节点位置的 x 值
          y: 350, // Number，可选，节点位置的 y 值
          label: '其他相关证据提交', // 节点文本
        },
        {
          id: 'node5', // String，该节点存在则必须，节点的唯一标识
          x: 700, // Number，可选，节点位置的 x 值
          y: 150, // Number，可选，节点位置的 y 值
          label: '证据送达', // 节点文本
          class: 'c2',
          status: 'undo',
        },
        {
          id: 'node6', // String，该节点存在则必须，节点的唯一标识
          x: 700, // Number，可选，节点位置的 x 值
          y: 250, // Number，可选，节点位置的 y 值
          label: '证据送达', // 节点文本
          class: 'c2',
          status: 'undo',
        },
        {
          id: 'node7', // String，该节点存在则必须，节点的唯一标识
          x: 700, // Number，可选，节点位置的 x 值
          y: 350, // Number，可选，节点位置的 y 值
          label: '证据送达', // 节点文本
          class: 'c2',
          status: 'undo',
        },
      ],
      // 边集
      edges: [
        {
          source: 'node1', // String，必须，起始点 id
          target: 'node2', // String，必须，目标点 id
          // label: '连线1', // 边的文本
        },
        {
          source: 'node1', // String，必须，起始点 id
          target: 'node3', // String，必须，目标点 id
        },
        {
          source: 'node1', // String，必须，起始点 id
          target: 'node4', // String，必须，目标点 id
        },
        {
          source: 'node2',
          target: 'node5',
        },
        {
          source: 'node3',
          target: 'node6',
        },
        {
          source: 'node4',
          target: 'node7',
        },
      ],
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
      this.$emit('exit-dialog');
    },
    handleMaterialTypeSelected() {
      console.log("已选择材料类型：" + this.materialType)
      this.materialTypeDialogVisible = false;
      this.submitProcess += 1;
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
        this.submitProcess += 1;
        this.submitDialogVisible = false;
        this.previewDialogVisible = true;
        // if (this.handleNode.getModel().label === "原告提交证据") {
        //   this.caseInfoForm.plaintiff_id_card_id = row.id;
        // } else if (this.handleNode.getModel().label === "被告提交证据") {
        //   this.caseInfoForm.defendant_id_card_id = row.id;
        // }
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
      // 获取文件的url
      this.pdfUrl = response.data.url;
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
    submitIdCard() {
      axios.post('http://localhost:8080/api/idcards', {
        id: "",
        document_Id: "",
        id_card_url: this.pdfUrl,
        file_name: this.fileName,
        name: this.userInfoForm.name,
        nation: this.userInfoForm.nation,
        sex: this.userInfoForm.sex,
        birthday: this.userInfoForm.birthday,
        address: this.userInfoForm.address,
        card_number: this.userInfoForm.cardNumber,
        all_info: this.userInfoForm.allInfo
      }).then(res => {
            console.log(res);
            if (res.data.code === 200) {
              this.$message({
                showClose: true,
                message: '提交数据库成功',
                type: 'success'
              });
            } else {
              this.$message({
                showClose: true,
                message: '提交数据库失败',
                type: 'error'
              });
            }
          }
      )
    },
    submitLicense() {
      axios.post('http://localhost:8080/api/licenses', {
        license_id: "",
        document_Id: "",
        license_url: this.pdfUrl,
        file_name: this.fileName,
        license_code: this.licenseInfoForm.licenseCode,
        license_number: this.licenseInfoForm.licenseNumber,
        license_enterprise_name: this.licenseInfoForm.licenseEnterpriseName,
        license_enterprise_type: this.licenseInfoForm.licenseEnterpriseType,
        license_legal_representative: this.licenseInfoForm.licenseLegalRepresentative,
        license_business_scope: this.licenseInfoForm.licenseBusinessScope,
        license_registered_capital: this.licenseInfoForm.licenseRegisteredCapital,
        license_establish_date: this.licenseInfoForm.licenseEstablishDate,
        license_operation_period: this.licenseInfoForm.licenseOperationPeriodStart.toString() +
            "至" + this.licenseInfoForm.licenseOperationPeriodEnd.toString(),
        license_domicile: this.licenseInfoForm.licenseDomicile,
        all_info: this.licenseInfoForm.allInfo
      }).then(res => {
            console.log(res);
            if (res.data.code === 200) {
              this.$message({
                showClose: true,
                message: '提交数据库成功',
                type: 'success'
              });
            } else {
              this.$message({
                showClose: true,
                message: '提交数据库失败',
                type: 'error'
              });
            }
          }
      )
    },
    submitInvoice() {
      axios.post('http://localhost:8080/api/invoices', {
        invoice_id: "",
        document_Id: "",
        invoice_url: this.pdfUrl,
        file_name: this.fileName,
        invoice_code: this.invoiceInfoForm.invoiceCode,
        invoice_number: this.invoiceInfoForm.invoiceNumber,
        invoice_amount: this.invoiceInfoForm.invoiceAmount,
        invoice_date: this.invoiceInfoForm.invoiceDate,
        purchaser_name: this.invoiceInfoForm.purchaserName,
        seller_name: this.invoiceInfoForm.sellerName,
        project_name: this.invoiceInfoForm.projectName,
        all_info: this.invoiceInfoForm.allInfo
      }).then(res => {
            console.log(res);
            if (res.data.code === 200) {
              this.$message({
                showClose: true,
                message: '提交数据库成功',
                type: 'success'
              });
            } else {
              this.$message({
                show: true,
                message: '提交数据库失败',
                type: 'error'
              });
            }
          }
      )
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
  <!-- 证据类型选择对话框-->
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

    <!-- 证据提交对话框-->
    <el-dialog
        :visible.sync="submitDialogVisible"
        title="证据提交"
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

    <!-- 材料预览对话框-->
    <el-dialog :visible.sync="previewDialogVisible"
               title="材料预览"
               width="50%"
               :close-on-click-modal="false">
      <el-progress :percentage="(100 * submitProcess / totalSteps)  "></el-progress>
      <br>
    </el-dialog>

    <!-- 确认提交对话框-->
    <el-dialog :visible.sync="confirmDialogVisible"
               title="确认提交"
               width="50%"
               :close-on-click-modal="false">
      <el-progress :percentage="(100 * submitProcess / totalSteps)  "></el-progress>
      <br>
    </el-dialog>
  </div>
</template>

<style scoped>
.el-table .highlight-row {
  background-color: #FFFF00 !important;
}
</style>