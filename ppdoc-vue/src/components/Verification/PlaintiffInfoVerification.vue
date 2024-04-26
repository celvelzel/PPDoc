<script>
import axios from "axios";

export default {
  data() {
    return {
      tableData: [],
      total: 0,
      page: 1,
      pageSize: 10,
    }
  },
  props: {
    displayForm: [],
    caseInfoForm: [],
  },
  methods: {
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
        console.log(index, row);
        // 触发自定义事件，并将身份证ID作为参数传递给父组件
        this.$emit('select-id-card', row.id, row.card_number, row.name, "plaintiff");
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
        console.log(index, row);
        // 触发自定义事件，并将身份证ID作为参数传递给父组件
        this.$emit('select-license', row.license_id, row.license_code, row.license_enterprise_name, "plaintiff");
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消选择'
        });
      });
    },
    highLightIdCardRow({row}) {
      if (row.card_number == this.caseInfoForm.plaintiff_id) {
        // 行数据中的身份证号码与组件状态中的原告或被告身份证号码之一相匹配
        console.log("行数据与组件状态中的数据相匹配")
        return 'highlight-row';
      }
    },
    highLightLicenseRow({row}) {
      if (row.license_code == this.caseInfoForm.plaintiff_id) {
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
      console.log(`每页 ${val} 条`);
    }
  },
  mounted() {
    if (this.caseInfoForm.plaintiff_type == "个人") {
      axios.get('http://localhost:8080/api/idcards').then(res => {
        // 返回的数据是res.data
        this.tableData = res.data.data.rows;
        this.total = res.data.data.total;
      });
    } else {
      axios.get('http://localhost:8080/api/licenses').then(res => {
        // 返回的数据是res.data
        this.tableData = res.data.data.rows;
        this.total = res.data.data.total;
      });
    }
  }
}
</script>

<template>
  <div v-if="this.caseInfoForm.plaintiff_type == '个人'">
    <el-row :gutter="20">
      <el-col span="11">
        <h1>起诉状预览</h1>
        <!-- PDF预览组件-->
        <iframe :src="`static/pdf/web/viewer.html?file=`+this.displayForm.indictmentPdfUrl" width="100%"
                height="750"></iframe>
        <!-------------->
      </el-col>
      <el-col span="13">
        <h1>原告自然人身份证审核</h1>
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
        <br>
        <el-pagination
            background
            layout="total, sizes, prev, pager, next,jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :total="total">
        </el-pagination>
      </el-col>
    </el-row>
  </div>

  <div v-else-if="this.caseInfoForm.plaintiff_type != '个人'">
    <el-row :gutter="20">
      <el-col span="10">
        <h1>起诉状预览</h1>
        <!-- PDF预览组件-->
        <iframe :src="`static/pdf/web/viewer.html?file=`+this.displayForm.indictmentPdfUrl" width="100%"
                height="650"></iframe>
        <!-------------->
      </el-col>
      <el-col span="14">
        <h1>原告企业营业执照审核</h1>
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
        <br>
        <el-pagination
            background
            layout="total, sizes, prev, pager, next,jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :total="total">
        </el-pagination>
      </el-col>
    </el-row>
  </div>
</template>

<style>
.el-table .highlight-row {
  background-color: #FFFF00 !important;
}
</style>
