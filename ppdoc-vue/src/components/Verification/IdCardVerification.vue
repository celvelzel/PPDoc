<script>
import axios from "axios";

export default {
  data() {
    return {
      tableData: [],
      total: 0,
      page: 1,
      pageSize: 10,
      isPlaintiffSelected: false,
    }
  },
  props: {
    displayForm: [],
    caseInfoForm: [],
  },
  methods: {
    handlePlaintiffSelect(index, row) {
      this.$confirm('确认选择该记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success',
      }).then(() => {
        this.$message({
          type: 'success',
          message: '选择成功!'
        });
        //确定选择，处理逻辑
        console.log(index, row);
        // 触发自定义事件，并将身份证ID作为参数传递给父组件
        this.$emit('select-id-card', row.id, row.card_number, row.name, "plaintiff");
        this.isPlaintiffSelected = true;
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消选择'
        });
      });
    },
    handleDefendantSelect(index, row) {
      this.$confirm('确认选择该记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success',
      }).then(() => {
        this.$message({
          type: 'success',
          message: '选择成功!'
        });
        //确定选择，处理逻辑
        console.log(index, row);
        // 触发自定义事件，并将身份证ID作为参数传递给父组件
        this.$emit('select-id-card', row.id, row.card_number, row.name, "defendant");
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消选择'
        });
      });
    },
    handleSkip() {
      this.$emit('skip')
    },
    highLightRow({row}) {
      if (row.card_number == this.caseInfoForm.plaintiff_id || row.card_number == this.caseInfoForm.defendant_id) {
        // 行数据中的身份证号码与组件状态中的原告或被告身份证号码之一相匹配
        console.log("行数据中的身份证号码与组件状态中的原告或被告身份证号码之一相匹配")
        // if判断成功，但没有成功渲染
        return 'rowHighLight';
      }
    },
    handleCurrentChange(val) {
      this.page = val;
      axios.get('http://localhost:8080/api/idcards', {
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
      axios.get('http://localhost:8080/api/idcards', {
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
    axios.get('http://localhost:8080/api/idcards').then(res => {
      // 返回的数据是res.data
      this.tableData = res.data.data.rows;
      this.total = res.data.data.total;
    });
  }
}
</script>

<template>
  <div v-if="this.caseInfoForm.plaintiff_type == '个人' && this.caseInfoForm.defendant_type != '个人'">
    <el-row :gutter="20">
      <el-col span="11">
        <h1>起诉状预览</h1>
        <!-- PDF预览组件-->
        <iframe :src="`static/pdf/web/viewer.html?file=`+this.displayForm.indictmentPdfUrl" width="100%"
                height="750"></iframe>
        <!-------------->
      </el-col>
      <el-col span="13">
        <h1>原告身份证审核</h1>
        <el-table :data="tableData"
                  border
                  :row-class-name="highLightRow">
          <el-table-column prop="name" label="姓名" width="80"></el-table-column>
          <el-table-column prop="sex" label="性别" width="50"></el-table-column>
          <el-table-column prop="nation" label="民族" width="50"></el-table-column>
          <el-table-column prop="address" label="住址" width="200"></el-table-column>
          <el-table-column prop="card_number" label="身份证号" width="180"></el-table-column>
          <el-table-column fixed="right" label="操作">
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  align="center"
                  type="success"
                  @click="handlePlaintiffSelect(scope.$index, scope.row)">选择
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
      </el-col>
    </el-row>
  </div>

  <div v-else-if="this.caseInfoForm.plaintiff_type != '个人' && this.caseInfoForm.defendant_type =='个人'">
    <el-row :gutter="20">
      <el-col span="11">
        <h1>起诉状预览</h1>
        <!-- PDF预览组件-->
        <iframe :src="`static/pdf/web/viewer.html?file=`+this.displayForm.indictmentPdfUrl" width="100%"
                height="750"></iframe>
        <!-------------->
      </el-col>
      <el-col span="13">
        <h1>被告身份证审核</h1>
        <el-table :data="tableData"
                  border
                  :row-class-name="highLightRow">
          <el-table-column prop="name" label="姓名" width="80"></el-table-column>
          <el-table-column prop="sex" label="性别" width="50"></el-table-column>
          <el-table-column prop="nation" label="民族" width="50"></el-table-column>
          <el-table-column prop="address" label="住址" width="200"></el-table-column>
          <el-table-column prop="card_number" label="身份证号" width="180"></el-table-column>
          <el-table-column fixed="right" label="操作">
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  align="center"
                  type="success"
                  @click="handleDefendantSelect(scope.$index, scope.row)">选择
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
      </el-col>
    </el-row>
  </div>
  <!-- 没有案件相关身份证的提示 -->
  <div v-else-if="this.caseInfoForm.plaintiff_type != '个人' && this.caseInfoForm.defendant_type !='个人'">
    <el-col :sm="12" :lg="6" align="center">
      <el-result icon="warning" title="暂无案件相关身份证" subTitle="请进行下一步">
        <template slot="extra">
          <el-button type="primary" size="medium" @click="handleSkip()">继续</el-button>
        </template>
      </el-result>
    </el-col>
  </div>
  <div v-else-if="this.caseInfoForm.plaintiff_type == '个人' && this.caseInfoForm.defendant_type == '个人'">
    <el-row :gutter="20">
      <el-col span="11">
        <h1>起诉状预览</h1>
        <!-- PDF预览组件-->
        <iframe :src="`static/pdf/web/viewer.html?file=`+this.displayForm.indictmentPdfUrl" width="100%"
                height="750"></iframe>
        <!-------------->
      </el-col>
      <el-col v-if="!this.isPlaintiffSelected" span="13">
        <h1>原告身份证审核</h1>
        <el-table :data="tableData"
                  border
                  :row-class-name="highLightRow">
          <el-table-column prop="name" label="姓名" width="80"></el-table-column>
          <el-table-column prop="sex" label="性别" width="50"></el-table-column>
          <el-table-column prop="nation" label="民族" width="50"></el-table-column>
          <el-table-column prop="address" label="住址" width="200"></el-table-column>
          <el-table-column prop="card_number" label="身份证号" width="180"></el-table-column>
          <el-table-column fixed="right" label="操作">
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  align="center"
                  type="success"
                  @click="handlePlaintiffSelect(scope.$index, scope.row)">选择
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
      </el-col>
      <el-col v-else-if="this.isPlaintiffSelected" span="13">
        <h1>被告身份证审核</h1>
        <el-table :data="tableData"
                  border
                  :row-class-name="highLightRow">
          <el-table-column prop="name" label="姓名" width="80"></el-table-column>
          <el-table-column prop="sex" label="性别" width="50"></el-table-column>
          <el-table-column prop="nation" label="民族" width="50"></el-table-column>
          <el-table-column prop="address" label="住址" width="200"></el-table-column>
          <el-table-column prop="card_number" label="身份证号" width="180"></el-table-column>
          <el-table-column fixed="right" label="操作">
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  align="center"
                  type="success"
                  @click="handleDefendantSelect(scope.$index, scope.row)">选择
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
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.rowHighLight {
  background-color: #efdb09;
  color: #efdb09;
}
</style>
