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
    // 点击表格中的某一条案件记录的“选择”按钮触发的方法
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
        console.log(index, row);
        // 触发自定义事件'select-case'，并将起诉状ID作为参数传递给父组件
        this.$emit('select-case', row.indictment_id);
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消选择'
        });
      });
    },
    handleCurrentChange(val) {
      this.page = val;
      axios.get('http://localhost:8080/api/indictments', {
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
      axios.get('http://localhost:8080/api/indictments', {
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
    axios.get('http://localhost:8080/api/indictments').then(res => {
      // 返回的数据是res.data
      this.tableData = res.data.data.rows;
      this.total = res.data.data.total;
    });
  }
}
</script>

<template>
  <div>
    <el-table :data="tableData"
              border
              stripe
              width="100%">
      <el-table-column prop="case_type" label="案件类型" width="80"></el-table-column>
      <el-table-column prop="plaintiff_name" label="原告姓名" width="100"></el-table-column>
      <el-table-column prop="plaintiff_id" label="原告ID" width="100"></el-table-column>
      <el-table-column prop="defendant_name" label="被告姓名" width="100"></el-table-column>
      <el-table-column prop="defendant_id" label="被告ID" width="100"></el-table-column>
      <el-table-column prop="litigation_request" label="诉讼请求" width="200"
                       show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="facts_background" label="事实背景" width="150"
                       show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="evidence_list" label="证据列表" width="100" show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="court_name" label="法院名称" width="100"></el-table-column>
      <el-table-column prop="indictment_date" label="起诉日期" width="100"></el-table-column>
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
