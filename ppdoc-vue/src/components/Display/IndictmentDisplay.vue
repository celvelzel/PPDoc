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
        indictment_id: "",
        document_id: "",
        indictment_url: "",
        file_name: "",
        case_type: "",
        plaintiff_name: "",
        plaintiff_id: "",
        plaintiff_type: "",
        plaintiff_address: "",
        plaintiff_contact: "",
        defendant_name: "",
        defendant_id: "",
        defendant_type: "",
        defendant_address: "",
        defendant_contact: "",
        litigation_request: "",
        facts_background: "",
        legal_basis: "",
        evidence_list: "",
        court_name: "",
        indictment_date: "",
        all_info: "",
      },
    }
  },
  methods: {
    handleEdit(index, row) {
      console.log(index, row);
      // this.dialogVisible = true;
      axios.get('http://localhost:8080/api/indictments/' + row.indictment_id).then(res => {
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
        axios.delete('http://localhost:8080/api/indictments/' + row.document_id).then(res => {
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
      axios.put('http://localhost:8080/api/indictments/', this.docInfoForm).then(res => {
        console.log(res);
        //刷新表格
        axios.get('http://localhost:8080/api/indictments', {
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
      <el-table-column prop="indictment_url" label="起诉状链接" width="100"
                       show-overflow-tooltip="true"></el-table-column>
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
      <el-table-column prop="litigation_request" label="诉讼请求" width="100"
                       show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="facts_background" label="事实背景" width="100"
                       show-overflow-tooltip="true"></el-table-column>
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

    <el-dialog
        title="修改起诉状文档信息"
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
        <el-form-item label="原告类型">
          <el-input v-model="InfoForm.plaintiff_type"></el-input>
        </el-form-item>
        <el-form-item label="原告地址">
          <el-input v-model="InfoForm.plaintiff_address"></el-input>
        </el-form-item>
        <el-form-item label="原告联系方式">
          <el-input v-model="InfoForm.plaintiff_contact"></el-input>
        </el-form-item>
        <el-form-item label="被告姓名">
          <el-input v-model="InfoForm.defendant_name"></el-input>
        </el-form-item>
        <el-form-item label="被告ID">
          <el-input v-model="InfoForm.defendant_id"></el-input>
        </el-form-item>
        <el-form-item label="被告类型">
          <el-input v-model="InfoForm.defendant_type"></el-input>
        </el-form-item>
        <el-form-item label="被告地址">
          <el-input v-model="InfoForm.defendant_address"></el-input>
        </el-form-item>
        <el-form-item label="被告联系方式">
          <el-input v-model="InfoForm.defendant_contact"></el-input>
        </el-form-item>
        <el-form-item label="诉讼请求">
          <el-input v-model="InfoForm.litigation_request"></el-input>
        </el-form-item>
        <el-form-item label="事实背景">
          <el-input v-model="InfoForm.facts_background"></el-input>
        </el-form-item>
        <el-form-item label="法律依据">
          <el-input v-model="InfoForm.legal_basis"></el-input>
        </el-form-item>
        <el-form-item label="证据列表">
          <el-input v-model="InfoForm.evidence_list"></el-input>
        </el-form-item>
        <el-form-item label="法院名称">
          <el-input v-model="InfoForm.court_name"></el-input>
        </el-form-item>
        <el-form-item label="起诉状日期">
          <el-date-picker type="date" v-model="InfoForm.indictment_date"
                          style="margin-right: 500px;"></el-date-picker>
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
