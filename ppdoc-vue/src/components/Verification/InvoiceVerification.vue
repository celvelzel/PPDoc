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
    handleSelect(index, row) {
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
        this.$emit('select-invoice', row.invoice_id, row.file_name);
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消选择'
        });
      });
    },
    handleSkip() {
      this.$emit('skip')
    },
    handleCurrentChange(val) {
      this.page = val;
      axios.get('http://localhost:8080/api/invoices', {
        params: {
          page: val,
          pageSize: this.pageSize,
        }
      }).then(res => {
        this.tableData = res.data.data.rows;
        this.total = res.data.data.total;
      });
      console.log(`当前页: ${val}`);
    },
    handleSizeChange(val) {
      this.pageSize = val;
      axios.get('http://localhost:8080/api/invoices', {
        params: {
          page: this.page,
          pageSize: val,
        }
      }).then(res => {
        this.tableData = res.data.data.rows;
        this.total = res.data.data.total;
      });
      console.log(`每页 ${val} 条`);
    }
  },
  mounted() {
    axios.get('http://localhost:8080/api/invoices').then(res => {
      // 返回的数据是res.data
      this.tableData = res.data.data.rows;
      this.total = res.data.data.total;
    });
  }
}
</script>

<template>
  <div>
    <el-row :gutter="20">
      <el-col span="11">
        <h1>起诉状预览</h1>
        <!-- PDF预览组件-->
        <iframe :src="`static/pdf/web/viewer.html?file=`+this.displayForm.indictmentPdfUrl" width="100%"
                height="750"></iframe>
        <!-------------->
      </el-col>
      <el-col span="13">
        <h1>发票材料审查</h1>
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
                  @click="handleSelect(scope.$index, scope.row)">选择
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <br>
        <el-button
            size="mini"
            align="center"
            type="warning"
            style="margin-top: 20px; margin-bottom: 30px"
            @click="handleSkip()">跳过
        </el-button>
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

<style scoped>

</style>
