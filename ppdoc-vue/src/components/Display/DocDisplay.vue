<script>
import axios from "axios";

export default {
  data() {
    return {
      tableData: [],
      docInfoForm: {
        document_id: "",
        document_url: "",
        document_name: "",
        document_type: "",
        all_info: "",
      },
      total: 0,
      page: 1,
      pageSize: 10,
      dialogVisible: false,
    }
  },
  methods: {
    handleEdit(index, row) {
      console.log(index, row);
      // this.dialogVisible = true;
      axios.get('http://localhost:8080/api/docs/' + row.document_id).then(res => {
        this.dialogVisible = true;
        this.docInfoForm = res.data.data;
      });
    },
    handleDelete(index, row) {
      console.log(index, row);
      this.$confirm('此操作将永久删除该文档, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete('http://localhost:8080/api/docs/' + row.document_id).then(res => {
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
      axios.put('http://localhost:8080/api/docs/', this.docInfoForm).then(res => {
        console.log(res);
        //刷新表格
        axios.get('http://localhost:8080/api/docs', {
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
      axios.get('http://localhost:8080/api/docs', {
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
      axios.get('http://localhost:8080/api/docs', {
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
    axios.get('http://localhost:8080/api/docs').then(res => {
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
      <el-table-column prop="document_name" label="文档名" width="180"></el-table-column>
      <el-table-column prop="document_url" label="文档链接" width="400" show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="document_type" label="文档类型" width="100"></el-table-column>
      <el-table-column prop="all_info" label="所有信息" width="400" show-overflow-tooltip="true"></el-table-column>
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
        title="修改文档信息"
        :visible.sync="dialogVisible"
        :close-on-click-modal="false"
        width="30%">
      <el-form ref="form" :model="docInfoForm" label-width="80px">
        <el-form-item label="文档名">
          <el-input v-model="docInfoForm.document_name"></el-input>
        </el-form-item>
        <el-form-item label="文档类型">
          <el-input v-model="docInfoForm.document_type"></el-input>
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
