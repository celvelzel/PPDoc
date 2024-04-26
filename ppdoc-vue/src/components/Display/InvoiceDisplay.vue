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
  methods: {
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
    handleCurrentChange(val) {
      this.page = val;
      axios.get('http://localhost:8080/api/invoices',{
        params: {
          page: val,
          pageSize : this.pageSize,
        }
      }).then(res => {
        this.tableData = res.data.data.rows;
        this.total = res.data.data.total;
      });
      console.log(`当前页: ${val}`);
    },
    handleSizeChange(val) {
      this.pageSize = val;
      axios.get('http://localhost:8080/api/invoices',{
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
    <el-table :data="tableData" border>
      <el-table-column prop="file_name" label="文件名" width="100"></el-table-column>
      <el-table-column prop="invoice_url" label="发票链接" width="150" show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="invoice_code" label="发票代码" width="120"></el-table-column>
      <el-table-column prop="invoice_number" label="发票号码" width="100"></el-table-column>
      <el-table-column prop="invoice_amount" label="发票金额" width="70"></el-table-column>
      <el-table-column prop="invoice_data" label="开票日期" width="100"></el-table-column>
      <el-table-column prop="purchaser_name" label="购买方名称" width="150"></el-table-column>
      <el-table-column prop="seller_name" label="销售方名称" width="150"></el-table-column>
      <el-table-column prop="project_name" label="项目名称" width="100"></el-table-column>
      <el-table-column prop="all_info" label="所有信息" width="100" show-overflow-tooltip="true"></el-table-column>
      <el-table-column fixed="right" width="150" label="操作">
        <template slot-scope="scope">
          <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)">编辑
          </el-button>
          <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除
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
  </div>
</template>

<style scoped>

</style>
