<script>
import axios from "axios";
// 引入 echarts 核心模块，核心模块提供了 echarts 使用必须要的接口。
import * as echarts from 'echarts/core';
import {LegendComponent} from 'echarts/components';
import {GraphChart} from 'echarts/charts';
// 标签自动布局、全局过渡动画等特性
import {LabelLayout, UniversalTransition} from 'echarts/features';
// 引入 Canvas 渲染器
import {CanvasRenderer} from 'echarts/renderers';


export default {
  created() {
    // 注册必须的组件
    echarts.use([
      LegendComponent,
      GraphChart,
      CanvasRenderer,
      LabelLayout,
      UniversalTransition,
      CanvasRenderer,
    ]);
  },
  data() {
    return {
      tableData: [],
      total: 0,
      page: 1,
      pageSize: 10,
      showGraphDialog: false,
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
    handleCheck(index, row) {
      console.log(index, row);
      this.showGraphDialog = true;
      this.$nextTick(() => {
        this.initChart(row.case_id);
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
    },
    /**
     * 节点点击事件
     */
    async nodeClick(params) {
      console.log('点了节点:' + params.name, "clicked");
      if(params.dataType === "node" &&params.data.category === 0)
      {
        if (echarts.getInstanceByDom(document.getElementById('graph'))) {
          echarts.dispose(document.getElementById('graph'));
          //判断 dom 是否为空或未定义,已存在则调用 dispose() 方法销毁
        }
        this.initChart(params.data.myId);
      }
    },
    /**
     * 设置echarts配置项,重绘画布
     */
    initChart(case_id) {
      let chartDom = document.getElementById('graph');
      let myChart = echarts.init(chartDom);
      myChart.showLoading();
      myChart.on("click", (params) => {
        if (params.dataType === "node") {
          //判断点击的是图表的节点部分
          this.nodeClick(params);
        }
      });
      // 使用 axios 获取 webkit-dep 数据
      axios.get(`http://localhost:8080/api/cases/graph/${case_id}`)
          .then(response => {
            myChart.hideLoading();
            const webkitDep = response.data.data;
            console.log("收到的数据：" + webkitDep);
            // 配置图表的选项
            const option = {
              // // 鼠标hover的提示语
              // tooltip: {
              //   show: true, //默认值为true
              //   showContent: true, //是否显示提示框浮层
              //   trigger: "item", //触发类型，默认数据项触发
              //   triggerOn: "mousemove", //提示触发条件，mousemove鼠标移至触发，还有click点击触发
              //   alwaysShowContent: false, //默认离开提示框区域隐藏，true为一直显示
              //   showDelay: 0, //浮层显示的延迟，单位为 ms，默认没有延迟，也不建议设置。在 triggerOn 为 'mousemove' 时有效。
              //   hideDelay: 200, //浮层隐藏的延迟，单位为 ms，在 alwaysShowContent 为 true 的时候无效。
              //   enterable: false, //鼠标是否可进入提示框浮层中，默认为false，如需详情内交互，如添加链接，按钮，可设置为 true。
              //   position: "right", //提示框浮层的位置，默认不设置时位置会跟随鼠标的位置。只在 trigger 为'item'的时候有效。
              //   confine: false, //是否将 tooltip 框限制在图表的区域内。外层的 dom 被设置为 'overflow: hidden'，或者移动端窄屏，导致 tooltip 超出外界被截断时，此配置比较有用。
              //   transitionDuration: 0.4, //提示框浮层的移动动画过渡时间，单位是 s，设置为 0 的时候会紧跟着鼠标移动。
              // },
              legend: {
                data: ['案件', '起诉状', '自然人身份证', '企业营业执照', '发票', '属性'],
                top: 0,
                itemGap: 26,
                textStyle: {
                  padding: [0, 12]
                },
                backgroundColor: '#f5f5f5'
              },
              // 图表控件对应颜色（索引 01234）
              //color: ["#FF6F61","#7EC0EE","#6667AB", "#FFC773", "#FFD700","#939597"],
              color: ["#880a0a", "#8C531B", "#A67B5B", "#7F7053", "#4A312C", "#D3D3D3"],
              series: [
                {
                  type: 'graph', // 类型:关系图
                  layout: 'force', // 图的布局，类型为力导图
                  // animation: true,
                  legendHoverLink: true, //是否启用图例 hover(悬停) 时的联动高亮。
                  hoverAnimation: true, //是否开启鼠标悬停节点的显示动画
                  roam: true, // 是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移,可以设置成 'scale' 或者 'move'。设置成 true 为都开启
                  //edgeSymbol: ['circle', 'arrow'], // 箭头
                  //edgeSymbolSize: [4, 8], // 箭头大小 边两端的标记大小，可以是一个数组分别指定两端，也可以是单个统一指定。
                  draggable: true, // 节点是否可拖拽，只在使用力引导布局(layout: 'force',)的时候有用
                  focusNodeAdjacency: true, // 是否在鼠标移到节点上的时候突出显示节点以及节点的边和邻接节点。
                  force: {
                    edgeLength: 120, // 边的两个节点之间的距离
                    repulsion: 100, // 节点斥力
                    gravity: 0.01, // 所有节点受到的向中心的引力因子。该值越大节点越往中心点靠拢。
                    layoutAnimation: false, // 节点动画
                  },
                  // 线条样式
                  lineStyle: {
                    normal: {
                      width: 2,
                    },
                    curveness: 0.1, // 线条的曲线程度，从0到1 ---  不加弧度，两节点互相指向时，线上的字会重叠
                    emphasis: {
                      //高亮状态
                      width: 8,
                    },
                  },
                  // 线上的字体
                  edgeLabel: {
                    normal: {
                      show: true,
                      position: "middle",//边的文字样式
                      formatter: function (x) {
                        return x.data.name;
                      },
                    },
                  },
                  // 图形上的文本标签
                  label: {
                    position: 'bottom',
                    show: true,
                    //overflow: "truncate", //超出的部分截断
                    formatter: '{b}',
                  },
                  itemStyle: {
                    //鼠标放上去有阴影效果
                    emphasis: {
                      shadowColor: "#495653",
                      shadowOffsetX: 0,
                      shadowOffsetY: 0,
                      shadowBlur: 40,
                    },
                  },
                  symbolSize: 40, //节点大小
                  data: webkitDep.nodes.map(function (node, idx) {
                    node.id = idx;
                    return node;
                  }),
                  categories: webkitDep.categories,
                  edges: webkitDep.links
                }
              ],
            };
            myChart.setOption(option);
          })
          .catch(error => {
            console.error('获取到错误:', error);
          });
    },
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
          <!--          知识图谱界面用查看按钮-->
          <el-button
              size="mini"
              @click="handleCheck(scope.$index, scope.row)">查看
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


    <el-dialog :visible.sync="showGraphDialog" title="案件图谱" fullscreen>
<!--      <span>可通过点击案件节点切换到相关案件</span>-->
      <div id="graph" style="height: 600px; width: 1500px"></div>
    </el-dialog>
    <el-dialog
        title="修改案件信息"
        :visible.sync="dialogVisible"
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

