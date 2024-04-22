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
      axios.get('http://localhost:8080/licenses',{
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
      axios.get('http://localhost:8080/licenses',{
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
    axios.get('http://localhost:8080/licenses').then(res => {
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
      <el-table-column prop="license_enterprise_name" label="证照名称" width="180"></el-table-column>
      <el-table-column prop="license_enterprise_type" label="证照类型" width="180"></el-table-column>
      <el-table-column prop="license_legal_representative" label="法定代表人" width="90"></el-table-column>
      <el-table-column prop="license_business_scope" label="经营范围" width="180" show-overflow-tooltip="true"></el-table-column>
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
  </div>
</template>

<style scoped>

</style>

