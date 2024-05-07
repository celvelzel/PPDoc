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
        case_id: null,
        indictment_id: null,
        case_type: null,
        plaintiff_name: null,
        plaintiff_id: null,
        plaintiff_type: null,
        defendant_name: null,
        defendant_id: null,
        defendant_type: null,
        plaintiff_id_card_id: null,
        defendant_id_card_id: null,
        plaintiff_license_id: null,
        defendant_license_id: null,
        related_invoice_id: null,
      },
    }
  },
  methods: {
    handleEdit(index, row) {
      console.log(index, row);
      // this.dialogVisible = true;
      axios.get('http://localhost:8080/api/cases/' + row.case_id).then(res => {
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
        axios.delete('http://localhost:8080/api/cases/' + row.case_id).then(res => {
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
      axios.put('http://localhost:8080/api/cases/', this.InfoForm).then(res => {
        console.log(res);
        //刷新表格
        axios.get('http://localhost:8080/api/cases', {
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
    handleSelect(index, row) {
      this.$confirm('确认选择该案件?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '选择成功!'
        });
        //确定选择，处理逻辑
        console.log("选择了案件" + index, row);
        this.$emit('select-case', row.case_id);
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消选择'
        });
      });
    },
    handleCurrentChange(val) {
      this.page = val;
      axios.get('http://localhost:8080/api/cases', {
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
      axios.get('http://localhost:8080/api/cases', {
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
    axios.get('http://localhost:8080/api/cases').then(res => {
      // 返回的数据是res.data
      this.tableData = res.data.data.rows;
      this.total = res.data.data.total;
    });
  },
}
</script>

<template>
  <div>
    <el-table :data="tableData" border>
      <el-table-column prop="case_id" label="案件ID" width="100"></el-table-column>
      <el-table-column prop="case_type" label="案件类型" width="180"></el-table-column>
      <el-table-column prop="plaintiff_name" label="原告姓名" width="200"></el-table-column>
      <el-table-column prop="plaintiff_id" label="原告ID" width="200"></el-table-column>
      <el-table-column prop="defendant_name" label="被告姓名" width="200"></el-table-column>
      <el-table-column prop="defendant_id" label="被告ID" width="200"></el-table-column>
      <el-table-column fixed="right" width="210" label="操作">
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
          <!--          案件辅助界面用选择按钮-->
          <el-button
              size="small"
              @click="handleSelect(scope.$index, scope.row)">选择
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
        title="修改案件信息"
        :visible.sync="dialogVisible"
        :close-on-click-modal="false"
        width="50%">
      <el-form ref="form" :model="InfoForm" label-width="auto">
        <el-form-item label="案件类型">
          <el-input v-model="InfoForm.case_type"></el-input>
        </el-form-item>
        <el-form-item label="原告姓名">
          <el-input v-model="InfoForm.plaintiff_name"></el-input>
        </el-form-item>
        <el-form-item label="原告ID">
          <el-input v-model="InfoForm.plaintiff_id"></el-input>
        </el-form-item>
        <el-form-item label="被告姓名">
          <el-input v-model="InfoForm.defendant_name"></el-input>
        </el-form-item>
        <el-form-item label="被告ID">
          <el-input v-model="InfoForm.defendant_id"></el-input>
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
