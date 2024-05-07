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
        license_id: "",
        document_id: "",
        file_name: "",
        license_url: "",
        license_code: '',
        license_number: '',
        license_enterprise_name: '',
        license_enterprise_type: '',
        license_legal_representative: '',
        license_business_scope: '',
        license_registered_capital: '',
        license_establish_date: '',
        license_operation_period: [],
        license_domicile: '',
        all_info: ''
      },
      operation_period:[],
    }
  },
  methods: {
    handleEdit(index, row) {
      console.log(index, row);
      // this.dialogVisible = true;
      axios.get('http://localhost:8080/api/licenses/' + row.license_id).then(res => {
        this.dialogVisible = true;
        this.InfoForm = res.data.data;
        // 数据回显处理日期范围
        this.operation_period = [new Date(this.InfoForm.license_operation_period.split(',')[0]), new Date(this.InfoForm.license_operation_period.split(',')[1])]
      });
    },
    handleDelete(index, row) {
      console.log(index, row);
      this.$confirm('此操作将永久删除该文档, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete('http://localhost:8080/api/licenses/' + row.document_id).then(res => {
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
      // 更新数据处理日期范围
      this.InfoForm.license_operation_period = this.operation_period[0].toLocaleDateString() + ',' + this.operation_period[1].toLocaleDateString();
      axios.put('http://localhost:8080/api/licenses/', this.InfoForm).then(res => {
        console.log(res);
        //刷新表格
        axios.get('http://localhost:8080/api/licenses', {
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
      axios.get('http://localhost:8080/api/licenses', {
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
      axios.get('http://localhost:8080/api/licenses', {
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
    axios.get('http://localhost:8080/api/licenses').then(res => {
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
      <el-table-column prop="file_name" label="文件名" width="130"></el-table-column>
      <el-table-column prop="license_url" label="文件链接" width="150" show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="license_code" label="统一社会信用代码" width="175"></el-table-column>
      <el-table-column prop="license_number" label="证照编号" width="170"></el-table-column>
      <el-table-column prop="license_enterprise_name" label="企业名称" width="180"></el-table-column>
      <el-table-column prop="license_enterprise_type" label="证照类型" width="180"></el-table-column>
      <el-table-column prop="license_legal_representative" label="法定代表人" width="90"></el-table-column>
      <el-table-column prop="license_business_scope" label="经营范围" width="180"
                       show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="license_registered_capital" label="注册资本" width="180"></el-table-column>
      <el-table-column prop="license_establish_date" label="成立日期" width="95"></el-table-column>
      <el-table-column prop="license_operation_period" label="营业期限" width="180"></el-table-column>
      <el-table-column prop="license_domicile" label="住所" width="180"></el-table-column>
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
        title="修改营业执照信息"
        :visible.sync="dialogVisible"
        :close-on-click-modal="false"
        width="50%">
      <el-form ref="form" :model="InfoForm" label-width="auto">
        <el-form-item label="统一社会信用代码">
          <el-input v-model="InfoForm.license_code"></el-input>
        </el-form-item>
        <el-form-item label="证照编号">
          <el-input v-model="InfoForm.license_number"></el-input>
        </el-form-item>
        <el-form-item label="企业名称">
          <el-input v-model="InfoForm.license_enterprise_name"></el-input>
        </el-form-item>
        <el-form-item label="证照类型">
          <el-input v-model="InfoForm.license_enterprise_type"></el-input>
        </el-form-item>
        <el-form-item label="法定代表人">
          <el-input v-model="InfoForm.license_legal_representative"></el-input>
        </el-form-item>
        <el-form-item label="经营范围">
          <el-input v-model="InfoForm.license_business_scope"></el-input>
        </el-form-item>
        <el-form-item label="注册资本">
          <el-input v-model="InfoForm.license_registered_capital"></el-input>
        </el-form-item>
        <el-form-item label="成立日期">
          <el-date-picker
              v-model="InfoForm.license_establish_date"
              type="date"
              placeholder="选择日期"
              style="margin-right: 500px;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="营业期限">
          <el-date-picker
              v-model="operation_period"
              type="daterange"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="margin-right: 500px;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="住所">
          <el-input v-model="InfoForm.license_domicile"></el-input>
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

