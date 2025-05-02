<template>
  <div class="attraction-detail-flex">
    <div class="main-info">
      <div class="image-section">
        <img :src="routeImage" alt="路线图片" class="main-image" />
      </div>
      <div class="info-section">
        <h2>{{ routeTitle }}</h2>
        <p>{{ routeDesc }}</p>
        <el-button type="primary" size="large" @click="reserveDialogVisible = true">立即预约</el-button>
      </div>
    </div>
    <div class="tabs-section">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="游览路线" name="route">
          <div class="route-map-container">
            <div id="routeMap" class="route-map"></div>
            <div class="map-controls">
              <el-button-group>
                <el-button size="small" @click="zoomIn">放大</el-button>
                <el-button size="small" @click="zoomOut">缩小</el-button>
              </el-button-group>
              <el-button-group class="ml-2">
                <el-button size="small" @click="resetView">重置视图</el-button>
                <el-button size="small" @click="toggleSatellite">卫星图</el-button>
              </el-button-group>
            </div>
          </div>
          <ul class="route-list">
            <li v-for="(item, idx) in routes" :key="idx" @click="focusOnPoint(idx)">
              <span class="route-title">{{ item.title }}</span>
              <span class="route-desc">{{ item.desc }}</span>
            </li>
          </ul>
        </el-tab-pane>
        <el-tab-pane label="游客须知" name="notice">
          <div class="notice-content">
            <ul>
              <li>请根据导游指引有序参观，注意安全。</li>
              <li>部分路线需提前预约，建议合理安排行程。</li>
              <li>请勿乱扔垃圾，保护生态环境。</li>
              <li>如遇恶劣天气，部分项目可能暂停开放。</li>
            </ul>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog
      title="预约提示"
      :visible.sync="reserveDialogVisible"
      width="30%"
      center
    >
      <span>预约成功！我们期待您的到来。</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="reserveDialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'AttractionRouteDetail',
  data() {
    return {
      routeImage: 'https://img2.baidu.com/it/u=3333333333,4444444444&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=533',
      routeTitle: '生态观鸟之旅',
      routeDesc: '这是一个风景优美的地方，湿地生态丰富，适合观鸟和摄影。游客可以乘船深入黄河三角洲湿地，近距离观赏东方白鹳、黑嘴鸥等珍稀鸟类，秋季还能看到碱蓬草形成的壮丽，是自然爱好者和摄影师的理想选择。',
      reserveDialogVisible: false,
      activeTab: 'route',
      routes: [
        { title: '第一天', desc: '东营市区出发-黄河三角洲湿地-观鸟台-生态讲解', lng: 118.6747, lat: 37.4342 },
        { title: '第二天', desc: '乘船游览-摄影采风-返回市区', lng: 118.6747, lat: 37.4342 }
      ],
      map: null,
      markers: [],
      polyline: null,
      isSatellite: false
    }
  },
  mounted() {
    this.initMap();
  },
  methods: {
    initMap() {
      // 初始化地图
      this.map = new AMap.Map('routeMap', {
        zoom: 13,
        center: [118.6747, 37.4342],
        viewMode: '3D'
      });

      // 添加路线点标记
      this.routes.forEach((point, index) => {
        const marker = new AMap.Marker({
          position: [point.lng, point.lat],
          title: point.title,
          content: `<div class="marker-content">${index + 1}</div>`,
          offset: new AMap.Pixel(-10, -10)
        });
        marker.on('click', () => {
          this.showInfoWindow(marker, point);
        });
        this.markers.push(marker);
        this.map.add(marker);
      });

      // 绘制路线
      const path = this.routes.map(point => [point.lng, point.lat]);
      this.polyline = new AMap.Polyline({
        path: path,
        strokeColor: '#1890ff',
        strokeWeight: 6,
        strokeOpacity: 0.8
      });
      this.map.add(this.polyline);

      // 自动调整地图视野
      this.map.setFitView();
    },
    showInfoWindow(marker, point) {
      const infoWindow = new AMap.InfoWindow({
        content: `<div class="info-window">
          <h3>${point.title}</h3>
          <p>${point.desc}</p>
        </div>`,
        offset: new AMap.Pixel(0, -30)
      });
      infoWindow.open(this.map, marker.getPosition());
    },
    zoomIn() {
      this.map.zoomIn();
    },
    zoomOut() {
      this.map.zoomOut();
    },
    resetView() {
      this.map.setFitView();
    },
    toggleSatellite() {
      this.isSatellite = !this.isSatellite;
      this.map.setLayers([this.isSatellite ? 'satellite' : 'normal']);
    },
    focusOnPoint(index) {
      const point = this.routes[index];
      this.map.setCenter([point.lng, point.lat]);
      this.map.setZoom(15);
      this.showInfoWindow(this.markers[index], point);
    }
  }
}
</script>

<style scoped>
.attraction-detail-flex {
  width: 80%;
  margin: 40px auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  padding: 30px;
}
.main-info {
  display: flex;
  align-items: flex-start;
  margin-bottom: 30px;
}
.image-section {
  flex: 0 0 320px;
  margin-right: 30px;
}
.main-image {
  width: 320px;
  height: 220px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.info-section {
  flex: 1;
}
.info-section h2 {
  font-size: 24px;
  color: #1890ff;
  margin-bottom: 15px;
}
.info-section p {
  font-size: 15px;
  color: #333;
  line-height: 1.7;
  margin-bottom: 20px;
}
.tabs-section {
  margin-top: 10px;
}
.route-map-container {
  position: relative;
  height: 500px;
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.route-map {
  width: 100%;
  height: 100%;
}
.map-controls {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 100;
}
.ml-2 {
  margin-left: 10px;
}
.route-list {
  padding-left: 0;
  list-style: none;
}
.route-list li {
  margin-bottom: 12px;
  font-size: 15px;
  color: #444;
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  transition: all 0.3s;
}
.route-list li:hover {
  background-color: #f0f9ff;
}
.route-title {
  font-weight: bold;
  color: #1890ff;
  margin-right: 10px;
}
.notice-content ul {
  padding-left: 18px;
}
.notice-content li {
  font-size: 15px;
  color: #666;
  margin-bottom: 8px;
}
.marker-content {
  width: 20px;
  height: 20px;
  background-color: #1890ff;
  color: white;
  border-radius: 50%;
  text-align: center;
  line-height: 20px;
  font-size: 12px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
}
.info-window {
  padding: 10px;
}
.info-window h3 {
  margin: 0 0 5px 0;
  color: #1890ff;
}
.info-window p {
  margin: 0;
  color: #666;
  font-size: 14px;
}
</style> 