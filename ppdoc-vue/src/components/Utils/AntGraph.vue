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
          label: '起始点', // 节点文本
        },
        {
          id: 'node2', // String，该节点存在则必须，节点的唯一标识
          x: 300, // Number，可选，节点位置的 x 值
          y: 200, // Number，可选，节点位置的 y 值
          label: '目标点',
        },
      ],
      // 边集
      edges: [
        {
          source: 'node1', // String，必须，起始点 id
          target: 'node2', // String，必须，目标点 id
          label: '连线', // 边的文本
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
        size: 30, // Number | Array，可选，节点大小
      },
      fitView: true, //设置是否将图适配到画布中
      //fitViewPadding: [5, 5, 5, 5], // 画布上四周的留白宽度。
      animate: true, // 是否开启动画
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
