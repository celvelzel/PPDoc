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
      axios.get('http://localhost:8080/docs',{
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
      axios.get('http://localhost:8080/docs',{
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
    axios.get('http://localhost:8080/docs').then(res => {
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
      <el-table-column prop="document_id" label="文档编号" width="180"></el-table-column>
      <el-table-column prop="document_url" label="文档链接" width="180"></el-table-column>
      <el-table-column prop="document_name" label="文档名" width="180"></el-table-column>
      <el-table-column prop="document_type" label="文档类型" width="180"></el-table-column>
      <el-table-column prop="all_info" label="所有信息" width="180"></el-table-column>
      <el-table-column label="操作">
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
