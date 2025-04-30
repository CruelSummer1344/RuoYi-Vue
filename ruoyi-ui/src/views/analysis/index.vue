<template>
  <div>
    <!-- 订单统计折线图 -->
    <div id="orderChart" style="width: 100%; height: 400px;"></div>
    <!-- 旅游项目销量 & 销售额折线图 -->
    <div id="projectSalesChart" style="width: 100%; height: 400px; margin-top: 20px;"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  data() {
    return {
      orderChart: null,
      projectSalesChart: null,
      orderChartData: this.generateMockOrderData(),
      projectSalesData: this.generateProjectSalesData()
    };
  },
  mounted() {
    this.drawOrderChart();
    this.drawProjectSalesChart();
  },
  methods: {
    // 订单统计折线图
    drawOrderChart() {
      this.orderChart = echarts.init(document.getElementById("orderChart"));
      const option = {
        title: { text: "订单统计折线图" },
        tooltip: { trigger: "axis" },
        legend: { data: ["待支付", "已支付", "已取消"] },
        xAxis: {
          type: "category",
          data: this.orderChartData.map(item => item.order_date)
        },
        yAxis: { type: "value" },
        series: [
          {
            name: "待支付",
            type: "line",
            data: this.orderChartData.map(item => item.pending_count)
          },
          {
            name: "已支付",
            type: "line",
            data: this.orderChartData.map(item => item.paid_count)
          },
          {
            name: "已取消",
            type: "line",
            data: this.orderChartData.map(item => item.cancelled_count)
          }
        ]
      };
      this.orderChart.setOption(option);
    },

    // 旅游项目销量 & 销售额折线图
    drawProjectSalesChart() {
      this.projectSalesChart = echarts.init(document.getElementById("projectSalesChart"));
      const option = {
        title: { text: "各旅游项目销量 & 销售额" },
        tooltip: { trigger: "axis" },
        legend: { data: ["销量", "销售额"] },
        xAxis: {
          type: "category",
          data: this.projectSalesData.map(item => item.project)
        },
        yAxis: [
          { type: "value", name: "销量" },
          { type: "value", name: "销售额" }
        ],
        series: [
          {
            name: "销量",
            type: "line",
            yAxisIndex: 0,
            data: this.projectSalesData.map(item => item.sales)
          },
          {
            name: "销售额",
            type: "line",
            yAxisIndex: 1,
            data: this.projectSalesData.map(item => item.revenue)
          }
        ]
      };
      this.projectSalesChart.setOption(option);
    },

    // 生成订单数据
    generateMockOrderData() {
      const data = [];
      const today = new Date();
      for (let i = 6; i >= 0; i--) {
        const date = new Date(today);
        date.setDate(today.getDate() - i);
        data.push({
          order_date: date.toISOString().split("T")[0],
          pending_count: Math.floor(Math.random() * 10 + 1),
          paid_count: Math.floor(Math.random() * 10 + 1),
          cancelled_count: Math.floor(Math.random() * 5)
        });
      }
      return data;
    },

    // 生成项目销量数据
    generateProjectSalesData() {
      const projects = [
        "丽江古城",
        "黄山日出",
        "长城探险",
        "张家界玻璃桥",
        "西湖游船",
        "桂林山水",
        "故宫文化之旅"
      ];
      return projects.map(project => {
        const sales = Math.floor(Math.random() * 100 + 50);
        const price = Math.floor(Math.random() * 200 + 100);
        return {
          project,
          sales,
          revenue: sales * price
        };
      });
    }
  }
};
</script>
