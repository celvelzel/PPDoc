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
    <el-table :data="tableData" border>
      <el-table-column prop="file_name" label="文件名" width="130"></el-table-column>
      <el-table-column prop="indictment_url" label="起诉状链接" width="100" show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="case_type" label="案件类型" width="80"></el-table-column>
      <el-table-column prop="plaintiff_name" label="原告姓名" width="100"></el-table-column>
      <el-table-column prop="plaintiff_id" label="原告ID" width="100"></el-table-column>
      <el-table-column prop="plaintiff_type" label="原告类型" width="80"></el-table-column>
      <el-table-column prop="plaintiff_address" label="原告地址" width="100"></el-table-column>
      <el-table-column prop="plaintiff_contact" label="原告联系方式" width="110"></el-table-column>
      <el-table-column prop="defendant_name" label="被告姓名" width="100"></el-table-column>
      <el-table-column prop="defendant_id" label="被告ID" width="100"></el-table-column>
      <el-table-column prop="defendant_type" label="被告类型" width="80"></el-table-column>
      <el-table-column prop="defendant_address" label="被告地址" width="100"></el-table-column>
      <el-table-column prop="defendant_contact" label="被告联系方式" width="110"></el-table-column>
      <el-table-column prop="litigation_request" label="诉讼请求" width="100" show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="facts_background" label="事实背景" width="100" show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="legal_basis" label="法律依据" width="100" show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="evidence_list" label="证据列表" width="100" show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="court_name" label="法院名称" width="100"></el-table-column>
      <el-table-column prop="indictment_date" label="起诉日期" width="100"></el-table-column>
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
