<script>
import axios from 'axios'
import Schema from "async-validator";

export default {
  name: 'Channel',
  data() {
    return {
      tableData: [],
      total: 0,
      page: 1,
      pageSize: 10,
      showAddChannelDialog: false,
      showEditChannelDialog: false,
      options: [
        {
          value: 'Moonshot AI',
          label: 'Moonshot AI',
          children: [
            {
              value: 'moonshot-v1-8k',
              label: 'moonshot-v1-8k',
            },
            {
              value: 'moonshot-v1-32k',
              label: 'moonshot-v1-32k',
            },
            {
              value: 'moonshot-v1-128k',
              label: 'moonshot-v1-128k',
            }
          ]
        },
        {
          value: '智谱ChatGLM',
          label: '智谱ChatGLM',
          children: [
            {
              value: 'ChatGLM-3',
              label: 'ChatGLM-3',
            },
            {
              value: 'ChatGLM-4',
              label: 'ChatGLM-4',
            },
          ]
        }
      ],
      rules: {
        channelName: [
          {required: true, message: '请输入渠道名称', trigger: 'blur'},
          {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        channelModelName: [
          {required: true, message: '请选择模型', trigger: 'change'}
        ],
        channelApiKey: <REDACTED_SECRET>
          {required: true, message: '请输入API Key', trigger: 'blur'},
        ],
        channelSecretKey: <REDACTED_SECRET>
          {required: true, message: '请输入Secret Key', trigger: 'blur'},
        ]
      },
      ChannelInfoForm: {
        channelId: null,
        channelName: null,
        channelType: null,
        channelStatus: null,
        channelResponseTime: null,
        channelModelName: null,
        channelCreateTime: null,
        channelApiKey: <REDACTED_SECRET>
        channelSecretKey: <REDACTED_SECRET>
      }
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!');
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    handleChange(value) {
      this.ChannelInfoForm.channelType = value[0];
      console.log("渠道类型为" + this.ChannelInfoForm.channelType);
      this.ChannelInfoForm.channelModelName = value[1];
      console.log("模型为" + this.ChannelInfoForm.channelModelName);
    },
    handleEdit(index, row) {
      console.log(index, row);
      // this.dialogVisible = true;
      axios.get('http://localhost:8080/api/channels/' + row.channelId).then(res => {
        this.showEditChannelDialog = true;
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
        axios.delete('http://localhost:8080/api/channels/' + row.channelId).then(res => {
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
      axios.put('http://localhost:8080/api/channels/', this.InfoForm).then(res => {
        console.log(res);
        //刷新表格
        axios.get('http://localhost:8080/api/channels', {
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
      axios.get('http://localhost:8080/api/channels', {
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
      axios.get('http://localhost:8080/api/channels', {
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
    axios.get('http://localhost:8080/api/channels').then(res => {
      // 返回的数据是res.data
      this.tableData = res.data.data.rows;
      this.total = res.data.data.total;
    });
  },
}
</script>

<template>
  <div>
    <el-container>
      <el-header>
        <div class="button-bar">
          <el-button type="primary" plain @click="showAddChannelDialog=true">添加新的渠道</el-button>
          <el-button type="success" plain>测试所有渠道</el-button>
          <el-button type="info" plain>测试禁用渠道</el-button>
          <el-button type="danger" plain>删除禁用渠道</el-button>
        </div>
      </el-header>
      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="channelId" label="ID" width="180"></el-table-column>
        <el-table-column prop="channelName" label="名称"></el-table-column>
        <el-table-column prop="channelType" label="类型"></el-table-column>
        <el-table-column prop="channelState" label="状态" width="80"></el-table-column>
        <el-table-column prop="channelResponseTime" label="响应时间" width="80"></el-table-column>
        <el-table-column prop="channelCreateTime" label="创建时间">
          <template slot-scope="scope" v-if="scope.row.channelCreateTime">
            {{ scope.row.channelCreateTime.toLocaleString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '') }}
          </template>
        </el-table-column>
        <el-table-column fixed="right" width="280" label="操作">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="success"
                @click="handleTest(scope.$index, scope.row)">测试
            </el-button>
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
            <el-button
                size="mini"
                type="warning"
                @click="handleDisable(scope.$index, scope.row)">禁用
            </el-button>
            <el-button
                size="mini"
                type="info"
                @click="handleEdit(scope.$index, scope.row)">编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <br>
      <el-footer>
        <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="10"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
        ></el-pagination>
        <br>
      </el-footer>
    </el-container>

    <el-dialog title="创建新的渠道"
               :rules="rules"
               :visible.sync="showAddChannelDialog"
               :close-on-click-modal="false">
      <el-form :model="ChannelInfoForm" label-width="auto" label-position="left" ref="NewChannelInfoForm">
        <el-form-item label="模型" prop="channelModelName">
          <el-cascader
              :options="options"
              :props="{ expandTrigger: 'hover' }"
              @change="handleChange"
              width="auto"
              size="medium"
              filterable
              style="margin-left: 0;"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="名称" prop="channelName">
          <el-input v-model="ChannelInfoForm.channelName"></el-input>
        </el-form-item>
        <el-form-item label="API密钥" prop="channelApiKey">
          <el-input v-model="ChannelInfoForm.channelApiKey"></el-input>
        </el-form-item>
        <el-form-item label="密钥" prop="channelSecretKey" v-if="ChannelInfoForm.channelType ==='Moonshot AI'">
          <el-input v-model="ChannelInfoForm.channelSecretKey"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog title="编辑渠道" :visible.sync="showEditChannelDialog">
    </el-dialog>
  </div>
</template>

<style scoped>
.el-container {
  margin-top: 1rem; /* 增加顶部外边距，提高呼吸感 */
  margin-bottom: 3rem; /* 同上，底部外边距 */
  border: 1px solid rgba(238, 238, 238, 0.3); /* 柔和的边框颜色，增加透明度以适应更多背景 */
  border-radius: 8px; /* 添加圆角，使元素显得不那么生硬 */
  background-color: #f9f9f9; /* 轻微的背景色，提升层次感 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 添加阴影，增强立体效果 */
  transition: all 0.3s ease; /* 平滑的过渡效果，提升用户体验 */
  /* 增大内边距*/
  padding: 20px 30px;
}

.button-bar {
  text-align: left;
}

.el-form-item .el-cascader {
  margin-left: 0;
}
</style>
