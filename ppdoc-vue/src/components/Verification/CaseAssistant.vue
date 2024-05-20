<script>
import G6 from '@antv/g6';
import caseAssistantDialog from "@/components/Verification/CaseAssistantDialog.vue";

export default {
  components: {
    caseAssistantDialog
  },
  data() {
    return {
      handleNode: "",
      dialogVisible: "",
      submitProcess: "",
      // 点集
      nodes: [
        {
          id: '已完成', // String，该节点存在则必须，节点的唯一标识
          x: 100, // Number，可选，节点位置的 x 值
          y: 20, // Number，可选，节点位置的 y 值
          label: '已完成', // 节点文本
          class: 'c0',
        },
        {
          id: '进行中', // String，该节点存在则必须，节点的唯一标识
          x: 220, // Number，可选，节点位置的 x 值
          y: 20, // Number，可选，节点位置的 y 值
          label: '进行中', // 节点文本
          class: 'c1',
        },
        {
          id: '未开始', // String，该节点存在则必须，节点的唯一标识
          x: 340, // Number，可选，节点位置的 x 值
          y: 20, // Number，可选，节点位置的 y 值
          label: '未开始', // 节点文本
          class: 'c2',
        },
        {
          id: 'node1', // String，该节点存在则必须，节点的唯一标识
          x: 300, // Number，可选，节点位置的 x 值
          y: 250, // Number，可选，节点位置的 y 值
          label: '起诉状提交', // 节点文本
          class: 'c0',
          status: 'done',
        },
        {
          id: 'node2', // String，该节点存在则必须，节点的唯一标识
          x: 500, // Number，可选，节点位置的 x 值
          y: 150, // Number，可选，节点位置的 y 值
          label: '原告提交证据',
        },
        {
          id: 'node3', // String，该节点存在则必须，节点的唯一标识
          x: 500, // Number，可选，节点位置的 x 值
          y: 250, // Number，可选，节点位置的 y 值
          label: '被告提交证据', // 节点文本
        },
        {
          id: 'node4', // String，该节点存在则必须，节点的唯一标识
          x: 500, // Number，可选，节点位置的 x 值
          y: 350, // Number，可选，节点位置的 y 值
          label: '其他相关证据提交', // 节点文本
        },
        {
          id: 'node5', // String，该节点存在则必须，节点的唯一标识
          x: 700, // Number，可选，节点位置的 x 值
          y: 150, // Number，可选，节点位置的 y 值
          label: '证据送达', // 节点文本
          class: 'c2',
          status: 'undo',
        },
        {
          id: 'node6', // String，该节点存在则必须，节点的唯一标识
          x: 700, // Number，可选，节点位置的 x 值
          y: 250, // Number，可选，节点位置的 y 值
          label: '证据送达', // 节点文本
          class: 'c2',
          status: 'undo',
        },
        {
          id: 'node7', // String，该节点存在则必须，节点的唯一标识
          x: 700, // Number，可选，节点位置的 x 值
          y: 350, // Number，可选，节点位置的 y 值
          label: '证据送达', // 节点文本
          class: 'c2',
          status: 'undo',
        },
      ],
      // 边集
      edges: [
        {
          source: 'node1', // String，必须，起始点 id
          target: 'node2', // String，必须，目标点 id
          // label: '连线1', // 边的文本
        },
        {
          source: 'node1', // String，必须，起始点 id
          target: 'node3', // String，必须，目标点 id
        },
        {
          source: 'node1', // String，必须，起始点 id
          target: 'node4', // String，必须，目标点 id
        },
        {
          source: 'node2',
          target: 'node5',
        },
        {
          source: 'node3',
          target: 'node6',
        },
        {
          source: 'node4',
          target: 'node7',
        },
      ],
    }
  },
  props: {
    caseInfoForm: {
      case_id: "",
      indictment_id: "",
      case_type: "",
      plaintiff_name: "",
      plaintiff_id: "",
      plaintiff_type: "",
      defendant_name: "",
      defendant_id: "",
      defendant_type: "",
      plaintiff_id_card_id: "",
      defendant_id_card_id: "",
      plaintiff_license_id: "",
      is_plaintiff_submit: "",
      is_defendant_submit: "",
      is_related_submit: "",
    },
  },
  methods:{
    hideDialog() {
      this.dialogVisible = false;
    }
  },
  mounted() {
    //图实例化，至少需要为图设置容器、宽、高：
    const graph = new G6.Graph({
      container: 'mountNode', // 指定挂载容器, String | HTMLElement，必须，图画布的容器 id 或容器元素
      width: 1200, // Number，必须，图的宽度
      height: 500, // Number，必须，图的高度
      modes: {
        // 定义图形状态时的行为
        default: ['drag-canvas', 'zoom-canvas', 'drag-node'],
      },
      defaultNode: {
        size: 50, // Number | Array，可选，节点大小
        style: {
          fill: 'steelblue', // 节点填充色
          stroke: '#666', // 节点描边色
          lineWidth: 1, // 节点描边粗细
        },
        // 节点上的标签文本配置
        labelCfg: {
          // 节点上的标签文本样式配置
          style: {
            fill: '#000000', // 节点标签文字颜色
          },
        },
      },
      // 边在默认状态下的样式配置（style）和其他配置
      defaultEdge: {
        type: 'polyline',
        // 边样式配置
        style: {
          opacity: 0.6, // 边透明度
          stroke: 'grey', // 边描边颜色
          endArrow: true, // 边是否显示尾部箭头
        },
        // 边上的标签文本配置
        labelCfg: {
          autoRotate: true, // 边上的标签文本根据边的方向旋转
        },
      },
      //fitView: true, //设置是否将图适配到画布中
      fitViewPadding: [5, 5, 5, 5], // 画布上四周的留白宽度。
      animate: true, // 是否开启动画
    });

    this.nodes.forEach((node) => {
      if (!node.style) {
        node.style = {};
      }

      // 根据案件信息配置节点状态
      if (this.caseInfoForm.is_plaintiff_submit === 1) {
        if (node.label === '原告提交证据') {
          console.log('原告已提交证据');
          node.status = 'done';
        } else if (node.id === 'node5') {
          node.status = 'doing';
        }
      } else {
        if (node.label === '原告提交证据') {
          console.log('原告未提交证据');
          node.status = 'doing';
        } else if (node.id === 'node5') {
          node.status = 'undo';
        }
      }

      if (this.caseInfoForm.is_defendant_submit === 'true') {
        if (node.label === '被告提交证据') {
          console.log('被告已提交证据');
          node.status = 'done';
        } else if (node.id === 'node6') {
          node.status = 'doing';
        }
      } else {
        if (node.label === '被告提交证据') {
          console.log('被告未提交证据');
          node.status = 'doing';
        } else if (node.id === 'node6') {
          node.status = 'undo';
        }
      }

      if (this.caseInfoForm.is_related_submit === 'true') {
        if (node.label === '相关材料提交') {
          console.log('相关材料已提交');
          node.status = 'done';
        } else if (node.id === 'node7') {
          node.status = 'doing';
        }
      } else {
        if (node.label === '其他相关证据提交') {
          console.log('相关材料未提交');
          node.status = 'doing';
        } else if (node.id === 'node7') {
          node.status = 'undo';
        }
      }

      // 根据节点状态配置节点样式
      switch (node.status) {
        case 'done': {
          node.class = 'c0';
          break;
        }
        case 'doing': {
          node.class = 'c1';
          break;
        }
        case 'undo': {
          node.class = 'c2';
          break;
        }
      }


      switch (
          node.class // 根据节点数据中的 class 属性配置图形
          ) {
        case 'c0': {
          node.type = 'rect';
          node.size = [100, 30];
          node.style.fill = '#67C23A';
          node.style.stroke = '#333'; // 添加深色边框
          node.style.strokeWidth = 2; // 边框宽度
          node.style.radius = 10; // 圆角
          node.style.shadowColor = 'rgba(0, 0, 0, 0.3)'; // 添加阴影
          node.style.shadowBlur = 4;
          node.style.shadowOffsetX = 2;
          node.style.shadowOffsetY = 2;
          break;
        }
        case 'c1': {
          node.type = 'rect';
          node.size = [100, 30]; // class = 'c1' 时节点大小
          node.style.fill = '#E6A23C';
          node.style.stroke = '#333'; // 添加深色边框
          node.style.strokeWidth = 2; // 边框宽度
          node.style.radius = 10;
          node.style.shadowColor = 'rgba(0, 0, 0, 0.3)'; // 添加阴影
          node.style.shadowBlur = 4;
          node.style.shadowOffsetX = 2;
          node.style.shadowOffsetY = 2;
          break;
        }
        case 'c2': {
          node.type = 'rect';
          node.size = [100, 30]; // class = 'c1' 时节点大小
          node.style.fill = '#ffffff';
          node.style.stroke = '#333'; // 添加深色边框
          node.style.strokeWidth = 2; // 边框宽度
          node.style.radius = 10;
          node.style.shadowColor = 'rgba(0, 0, 0, 0.3)'; // 添加阴影
          node.style.shadowBlur = 4;
          node.style.shadowOffsetX = 2;
          node.style.shadowOffsetY = 2;
          break;
        }
      }

      graph.data({
        nodes: this.nodes,
        edges: this.edges,
      }); // 加载数据

      graph.on('node:click', (ev) => {
        const node = ev.item; // 被点击的节点元素
        const shape = ev.target; // 被点击的图形，可根据该信息作出不同响应，以达到局部响应效果
        console.log("点击了节点：" + node._cfg.id)
        if ((node.getModel().status === 'doing' && node.getModel().label === '原告提交证据')
            || (node.getModel().label === '被告提交证据' && node.getModel().status === 'doing')
            || (node.getModel().label === '其他相关证据提交' && node.getModel().status === 'doing')) {
          console.log("打开弹窗");
          this.dialogVisible = true;
          this.submitProcess = 1;
          this.handleNode = node;
        }
      });

      graph.on('edge:click', (ev) => {
        const edge = ev.item; // 被点击的边元素
        const shape = ev.target; // 被点击的图形，可根据该信息作出不同响应，以达到局部响应效果
        console.log("点击了边：" + edge._cfg.id);
      });

      graph.render(); // 渲染
    });
  }
}
</script>

<template>
  <div>
    <!-- 渲染流程图-->
    <div id="mountNode"></div>
    <!-- 提交材料对话框 -->
    <div v-if="this.dialogVisible === true">
      <case-assistant-dialog :case-info-form="this.caseInfoForm"
                             :handle-node="this.handleNode"
                             @exit-dialog="hideDialog"
                             @close="hideDialog"></case-assistant-dialog>
    </div>
  </div>
</template>

<style scoped>

</style>
