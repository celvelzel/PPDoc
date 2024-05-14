<script>
import G6 from '@antv/g6';

export default {
  data() {
    return {
      nodes: [
        {
          id: 'node1', // String，该节点存在则必须，节点的唯一标识
          x: 100, // Number，可选，节点位置的 x 值
          y: 200, // Number，可选，节点位置的 y 值
          label: '起诉状提交', // 节点文本
          class:'c0',
        },
        {
          id: 'node2', // String，该节点存在则必须，节点的唯一标识
          x: 300, // Number，可选，节点位置的 x 值
          y: 200, // Number，可选，节点位置的 y 值
          label: '自然人身份证提交',
          class:'c1',
        },
        {
          id: 'node3', // String，该节点存在则必须，节点的唯一标识
          x: 500, // Number，可选，节点位置的 x 值
          y: 200, // Number，可选，节点位置的 y 值
          label: '企业营业执照提交', // 节点文本
          class:'c1',
        },
        {
          id: 'node4', // String，该节点存在则必须，节点的唯一标识
          x: 700, // Number，可选，节点位置的 x 值
          y: 200, // Number，可选，节点位置的 y 值
          label: '案件相关材料提交', // 节点文本
          class:'c1',
        },
      ],
      // 边集
      edges: [
        {
          source: 'node1', // String，必须，起始点 id
          target: 'node2', // String，必须，目标点 id
          label: '连线1', // 边的文本
        },
          {
            source: 'node3', // String，必须，起始点 id
            target: 'node4', // String，必须，目标点 id
            label: '连线3', // 边的文本
          },
          {
            source: 'node2', // String，必须，起始点 id
            target: 'node3', // String，必须，目标点 id
            label: '连线2', // 边的文本
          },
      ],
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
            fill: '#fff', // 节点标签文字颜色
          },
        },
      },
      // 边在默认状态下的样式配置（style）和其他配置
      defaultEdge: {
        type:'polyline',
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
      //fitViewPadding: [5, 5, 5, 5], // 画布上四周的留白宽度。
      animate: true, // 是否开启动画
    });

    this.nodes.forEach((node) => {
      if (!node.style) {
        node.style = {};
      }
      switch (
          node.class // 根据节点数据中的 class 属性配置图形
          ) {
        case 'c0': {
          node.type = 'rect';
          node.size = [100, 30];
          node.style.fill = '#ace2b3';
          node.style.radius = 10;
          break;
        }
        case 'c1': {
          node.type = 'rect';
          node.size = [100,30]; // class = 'c1' 时节点大小
          node.style.fill = '#acc2e2';
          node.style.radius = 10;
          break;
        }
      }
    });

    graph.data({
      nodes: this.nodes,
      edges: this.edges,
    }); // 加载数据
    graph.render(); // 渲染
  }
}
</script>

<template>
  <div id="mountNode"></div>
</template>

<style scoped>

</style>
