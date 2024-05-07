<script>
import axios from 'axios'

export default {
  name: 'Channel',
  data() {
    return {
      page: 1,
      total: 0,
      tableData: [
        {
          id: '1',
          name: '渠道1',
          type: 'MoonShot AI',
          state: '禁用',
          response_time: '0.1s',
          create_time: '2023-03-05 12:00:00'
        },
        {
          id: '3',
          name: '渠道3',
          type: 'ChatGPT',
          state: '禁用',
          response_time: '0.1s',
          create_time: '2023-03-01 12:00:00'
        },
        {
          id: '2',
          name: '渠道2',
          type: 'ChatGLM',
          state: '禁用',
          response_time: '0.1s',
          create_time: '2023-04-01 12:00:00'
        },
      ]
    }
  },
  methods: {},
}
</script>

<template>
  <div>
    <el-container>
      <el-header>
        <div class="button-bar">
        <el-button type="primary" plain>添加新的渠道</el-button>
          <el-button type="success" plain>测试所有渠道</el-button>
          <el-button type="info" plain>测试禁用渠道</el-button>
          <el-button type="danger" plain>删除禁用渠道</el-button>
        </div>
      </el-header>
      <el-table :data="tableData" style="width: 100%" border  >
        <el-table-column prop="id" label="ID" width="180"></el-table-column>
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="type" label="类型"></el-table-column>
        <el-table-column prop="state" label="状态" width="80"></el-table-column>
        <el-table-column prop="response_time" label="响应时间" width="80"></el-table-column>
        <el-table-column prop="create_time" label="创建时间"></el-table-column>
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
                size="small"
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
    <el-row>

    </el-row>
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
</style>
