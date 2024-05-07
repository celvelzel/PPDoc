<script>
import axios from "axios";

export default {
  data() {
    return {
      tableData: [],
      total: 0,
      page: 1,
      pageSize: 10,
      dialogVisible: false,
      InfoForm: {
        invoice_id: "",
        document_Id: "",
        invoice_url: "",
        file_name: "",
        invoice_code: '',
        invoice_number: '',
        invoice_amount: '',
        invoice_date: '',
        purchaser_name: '',
        seller_name: '',
        project_name: '',
        all_info: "",
      },
    }
  },
  methods: {
    handleEdit(index, row) {
      console.log(index, row);
      // this.dialogVisible = true;
      axios.get('http://localhost:8080/api/invoices/' + row.invoice_id).then(res => {
        this.dialogVisible = true;
        this.InfoForm = res.data.data;
      });
    },
    handleDelete(index, row) {
      console.log(index, row);
      this.$confirm('此操作将永久删除该文档, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete('http://localhost:8080/api/invoices/' + row.document_id).then(res => {
          console.log(res);
          this.tableData.splice(index, 1);
        });
        this.$message({
          type: 'success',
          message: '删除成功!'
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleUpdate() {
      this.dialogVisible = false;
      axios.put('http://localhost:8080/api/invoices/', this.docInfoForm).then(res => {
        console.log(res);
        //刷新表格
        axios.get('http://localhost:8080/api/invoices', {
          params: {
            page: this.page,
            pageSize: this.pageSize,
          }
        }).then(res => {
          this.tableData = res.data.data.rows;
          this.total = res.data.data.total;
        });
      });
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

    <el-dialog
        title="修改发票信息"
        :visible.sync="dialogVisible"
        :close-on-click-modal="false"
        width="50%">
      <el-form ref="form" :model="InfoForm" label-width="auto">
        <el-form-item label="发票代码">
          <el-input v-model="InfoForm.invoice_code"></el-input>
        </el-form-item>
        <el-form-item label="发票号码">
          <el-input v-model="InfoForm.invoice_number"></el-input>
        </el-form-item>
        <el-form-item label="发票金额">
          <el-input v-model="InfoForm.invoice_amount"></el-input>
        </el-form-item>
        <el-form-item label="开票日期">
          <el-date-picker v-model="InfoForm.invoice_date"
                          type="date"
                          placeholder="选择日期"
                          style="margin-right: 500px;"></el-date-picker>
        </el-form-item>
        <el-form-item label="购买方名称">
          <el-input v-model="InfoForm.purchaser_name"></el-input>
        </el-form-item>
        <el-form-item label="销售方名称">
          <el-input v-model="InfoForm.seller_name"></el-input>
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input v-model="InfoForm.project_name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleUpdate">保 存</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>

</style>
