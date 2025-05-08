<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>订单分析</span>
          </div>
          <div class="chart-wrapper">
            <div ref="orderChart" style="width: 100%; height: 400px"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>游客数量分析</span>
          </div>
          <div class="chart-wrapper">
            <div ref="visitorChart" style="width: 100%; height: 400px"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {listOrderData} from "@/api/order/order";

export default {
  name: "Statistics",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 订单数据
      orderList: [],
      // 图表实例
      orderChart: null,
      visitorChart: null
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.initCharts();
  },
  methods: {
    /** 查询订单列表 */
    getList() {
      this.loading = true;
      listOrderData().then(response => {
        this.orderList = response.data;
        this.loading = false;
        this.updateCharts();
      });
    },
    /** 初始化图表 */
    initCharts() {
      this.orderChart = echarts.init(this.$refs.orderChart);
      this.visitorChart = echarts.init(this.$refs.visitorChart);
      window.addEventListener('resize', this.resizeCharts);
    },
    /** 更新图表数据 */
    updateCharts() {
      // 处理订单数据
      const orderData = this.processOrderData();
      const visitorData = this.processVisitorData();

      // 设置订单图表配置
      const orderOption = {
        title: {
          text: '订单统计'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: orderData.dates
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: '订单数量',
          type: 'line',
          data: orderData.counts,
          smooth: true
        }]
      };

      // 设置游客图表配置
      const visitorOption = {
        title: {
          text: '游客数量统计'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: visitorData.dates
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: '游客数量',
          type: 'bar',
          data: visitorData.counts
        }]
      };

      this.orderChart.setOption(orderOption);
      this.visitorChart.setOption(visitorOption);
    },
    /** 处理订单数据 */
    processOrderData() {
      // 按日期分组统计订单数量
      const orderMap = new Map();
      this.orderList.forEach(order => {
        const date = this.parseTime(order.createdAt, '{y}-{m}-{d}');
        orderMap.set(date, (orderMap.get(date) || 0) + 1);
      });

      return {
        dates: Array.from(orderMap.keys()),
        counts: Array.from(orderMap.values())
      };
    },
    /** 处理游客数据 */
    processVisitorData() {
      // 按日期分组统计游客数量
      const visitorMap = new Map();
      this.orderList.forEach(order => {
        const date = this.parseTime(order.createdAt, '{y}-{m}-{d}');
        visitorMap.set(date, (visitorMap.get(date) || 0) + (order.peopleCount || 0));
      });

      return {
        dates: Array.from(visitorMap.keys()),
        counts: Array.from(visitorMap.values())
      };
    },
    /** 调整图表大小 */
    resizeCharts() {
      this.orderChart && this.orderChart.resize();
      this.visitorChart && this.visitorChart.resize();
    }
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts);
    this.orderChart && this.orderChart.dispose();
    this.visitorChart && this.visitorChart.dispose();
  }
};
</script>

<style scoped>
.chart-wrapper {
  background: #fff;
  padding: 16px 16px;
  margin-bottom: 32px;
}
</style>
