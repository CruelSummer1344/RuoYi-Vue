<template>
  <div class="home">
    <!-- 搜索框 -->
    <div class="search-container">
      <div class="search-wrapper">
        <div class="logo">
          <img alt="Logo" class="logo-image" src="../assets/logo/dy1.jpg"/>
          <h1>黄河口三角洲生态旅游</h1>
        </div>
        <el-input
            v-model="searchQuery"
            placeholder="搜索景点、路线、攻略..."
            class="search-input"
            clearable
            @keyup.enter.native="handleSearch"
        >
          <el-select
              slot="prepend"
              v-model="searchType"
              placeholder="请选择"
              style="width: 110px;"
          >
            <el-option label="景点" value="attraction"></el-option>
            <el-option label="路线" value="route"></el-option>
            <el-option label="攻略" value="guide"></el-option>
            <el-option label="酒店" value="hotel"></el-option>
          </el-select>
          <el-button slot="append" :loading="searchLoading" icon="el-icon-search" @click="handleSearch"/>
        </el-input>
        <div class="hot-keywords">
          <span>热门搜索：</span>
          <el-button
              v-for="keyword in hotKeywords"
              :key="keyword"
              size="mini"
              type="text"
              @click="quickSearch(keyword)"
          >{{ keyword }}</el-button>
        </div>
      </div>
    </div>

    <!-- 轮播图 -->
    <div class="carousel-container">
      <el-carousel arrow="always" class="carousel" height="450px" indicator-position="outside">
        <el-carousel-item v-for="item in carouselItems" :key="item.id">
          <el-card class="carousel-card" shadow="never">
            <div class="image-container">
              <img :src="item.image" alt="carousel image" class="carousel-image"/>
            </div>
            <div class="carousel-content">
              <h4>{{ item.title }}</h4>
              <p class="carousel-desc">{{ item.description }}</p>
            </div>
          </el-card>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 三列布局 -->
    <el-row :gutter="24" class="content-row">
      <!-- 热度排行榜 -->
      <el-col :xs="24" :sm="12" :md="8">
        <div class="rank-list">
          <h3>热度排行榜</h3>
          <el-row :gutter="12">
            <el-col :xs="24" v-for="item in rankData" :key="item.id">
              <el-card class="rank-card" shadow="never">
                <div class="rank-content">
                  <span :class="{'top-three': item.rank <= 3}" class="rank-number">{{ item.rank }}</span>
                  <div class="rank-info">
                    <h4>{{ item.name }}</h4>
                    <p class="rank-desc"><i class="el-icon-view"></i> {{ item.views }}</p>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-col>

      <!-- 旅游推荐及景点 -->
      <el-col :xs="24" :sm="12" :md="8">
        <!-- 旅游推荐 -->
        <div class="travel-recommend">
          <h3>景点推荐</h3>
          <el-row :gutter="12">
            <el-col :xs="24" v-for="item in paginatedTravelData" :key="item.id">
              <el-card class="travel-card" shadow="never">
                <div class="image-container">
                  <img :src="item.image" alt="旅游路线图片" class="travel-image"  @click="gotoSpot"/>
                  <span v-if="item.tag" class="travel-tag" >{{ item.tag }}</span>
                </div>
                <div class="travel-content">
                  <h4>{{ item.title }}</h4>
                  <p class="travel-desc">{{ item.description }}</p>
                  <div class="travel-footer">
                    <span class="travel-meta"><i class="el-icon-time"></i> {{ item.duration }}</span>
                    <span class="travel-meta"><i class="el-icon-location"></i> {{ item.startPoint }}</span>
                    <div class="travel-price">
                      <span class="price-value">¥{{ item.price }}</span>
                      <span class="price-unit">起/人</span>
                    </div>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <div class="pagination-container" v-if="travelData.length > 0">
            <el-pagination
                background
                layout="prev, pager, next"
                :total="totalTravelItems"
                :page-size="pageSize"
                :current-page.sync="currentPage"
                @current-change="handlePageChange"
            />
          </div>
        </div>
      </el-col>

      <!-- 新闻资讯及攻略 -->
      <el-col :xs="24" :sm="12" :md="8">
        <!-- 新闻资讯 -->
        <div class="news-list">
          <h3>旅游资讯</h3>
          <el-tabs v-model="activeNewsTab" @tab-click="handleNewsTabClick">
            <el-tab-pane label="最新动态" name="latest"></el-tab-pane>
            <el-tab-pane label="活动公告" name="events"></el-tab-pane>
            <el-tab-pane label="政策法规" name="policy"></el-tab-pane>
          </el-tabs>
          <el-row :gutter="12">
            <el-col :xs="24" v-for="item in filteredNewsData" :key="item.id">
              <el-card class="news-card" shadow="never">
                <div class="news-content">
                  <h4>{{ item.title }}</h4>
                  <p class="news-desc">{{ item.summary || '暂无摘要' }}</p>
                  <div class="news-footer">
                    <span class="news-meta">{{ item.date }}</span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!-- 旅游攻略 -->
        <div class="travel-guide">
          <h3>旅游攻略</h3>
          <el-row :gutter="12">
            <el-col :xs="24" v-for="item in guideList" :key="item.id">
              <el-card class="guide-card" shadow="never">
                <div class="image-container">
                  <img :src="item.image" alt="攻略图片" class="guide-image"/>
                </div>
                <div class="guide-content">
                  <h4>{{ item.title }}</h4>
                  <p class="guide-desc">作者：{{ item.author }}</p>
                  <div class="guide-footer">
                    <span class="guide-meta"><i class="el-icon-view"></i> {{ item.views }}</span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>

    <!-- 天气预报 -->
    <div class="weather-widget">
      <h3>东营天气</h3>
      <el-card class="weather-card" shadow="never">
        <div class="weather-content">
          <div class="weather-today">
            <i :class="weatherInfo.icon" class="weather-icon"></i>
            <div class="weather-temp">
              <span class="temp-value">{{ weatherInfo.temperature }}</span>
              <span class="temp-unit">°C</span>
            </div>
            <p class="weather-desc">{{ weatherInfo.description }}</p>
            <p class="weather-update">{{ weatherUpdateTime }}</p>
          </div>
          <div class="weather-details">
            <p class="weather-detail">湿度：{{ weatherInfo.humidity }}%</p>
            <p class="weather-detail">风力：{{ weatherInfo.wind }}</p>
            <p class="weather-detail">空气质量：{{ weatherInfo.airQuality }}</p>
          </div>
          <div class="weather-forecast">
            <div v-for="item in weatherForecast" :key="item.day" class="forecast-item">
              <p class="forecast-day">{{ item.day }}</p>
              <i :class="item.icon" class="forecast-icon"></i>
              <p class="forecast-temp">{{ item.tempRange }}</p>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 返回顶部 -->
    <el-backtop :bottom="50" :right="50"></el-backtop>

    <!-- 错误提示 -->
    <el-dialog
        :visible.sync="errorDialogVisible"
        center
        title="提示"
        width="30%"
    >
      <span>{{ errorMessage }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="errorDialogVisible = false">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getUserProfile } from "@/api/system/user";
import { v4 as uuidv4 } from 'uuid';

export default {
  name: 'Home',
  data() {
    return {
      // 用户信息
      userInfo: null,

      // 搜索相关
      searchQuery: '',
      searchType: 'attraction',
      searchLoading: false,
      hotKeywords: ['黄河口', '湿地公园', '天鹅湖', '孙子文化', '红海滩'],

      // 导航菜单
      activeIndex: 'home',

      // 轮播图
      carouselItems: [
        {
          id: 1,
          image: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80',
          title: '黄河入海 壮美东营',
          description: '探索中国唯一能够同时观赏黄河入海奇观和湿地风光的生态旅游胜地'
        },
        {
          id: 2,
          image: 'https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=800&q=80',
          title: '湿地天堂 生态东营',
          description: '中国最大的新生湿地，国际重要湿地，候鸟迁徙的重要驿站'
        },
        {
          id: 3,
          image: 'https://images.unsplash.com/photo-1500534314209-a25ddb2bd429?auto=format&fit=crop&w=800&q=80',
          title: '红色海滩 梦幻东营',
          description: '每年秋季，碱蓬草变红，形成壮观的"红地毯"景观，摄影爱好者的天堂'
        }
      ],

      // 快捷入口
      quickAccessItems: [
        { title: '景点门票', icon: 'el-icon-ticket', route: '/attractions' },
        { title: '酒店住宿', icon: 'el-icon-house', route: '/hotels' },
        { title: '特色美食', icon: 'el-icon-dish', route: '/food' },
        { title: '旅游路线', icon: 'el-icon-map-location', route: '/routes' },
        { title: '文化体验', icon: 'el-icon-reading', route: '/culture' },
        { title: '交通指南', icon: 'el-icon-truck', route: '/transportation' }
      ],

      // 热度排行榜
      rankData: [
        { id: uuidv4(), rank: 1, name: '黄河口生态旅游区', views: 12580 },
        { id: uuidv4(), rank: 2, name: '黄河三角洲国家级自然保护区', views: 9876 },
        { id: uuidv4(), rank: 3, name: '天鹅湖景区', views: 8654 },
        { id: uuidv4(), rank: 4, name: '黄河口文化旅游岛', views: 7532 },
        { id: uuidv4(), rank: 5, name: '东营胜利广场', views: 6421 },
        { id: uuidv4(), rank: 6, name: '孙子文化旅游区', views: 5890 },
        { id: uuidv4(), rank: 7, name: '利津黄河公园', views: 4567 },
        { id: uuidv4(), rank: 8, name: '广饶孙子兵法城', views: 3456 }
      ],

      // 天气信息
      weatherUpdateTime: this.formatDate(new Date(), 'MM-DD HH:mm'),
      weatherInfo: {
        temperature: 26,
        description: '晴',
        icon: 'el-icon-sunny',
        humidity: 65,
        wind: '东北风 3级',
        airQuality: '良',
        airQualityLevel: 2
      },
      weatherForecast: [
        { day: '今天', icon: 'el-icon-sunny', tempRange: '22°~28°' },
        { day: '明天', icon: 'el-icon-cloudy', tempRange: '20°~26°' },
        { day: '后天', icon: 'el-icon-partly-cloudy', tempRange: '19°~25°' }
      ],

      // 旅游路线
      activeRouteTab: 'popular',
      travelData: [
        {
          id: 1,
          title: '生态观鸟之旅',
          image: 'https://images.pexels.com/photos/459225/pexels-photo-459225.jpeg?auto=compress&w=400&q=80',
          tag: '热门',
          duration: '2天1晚',
          startPoint: '东营市区',
          price: 398,
          rating: 4.8,
          description: '这是一个风景优美的地方，湿地生态丰富，适合观鸟和摄影。游客可以乘船深入黄河三角洲湿地，近距离观赏东方白鹳、黑嘴鸥等珍稀鸟类，秋季还能看到碱蓬草形成的壮丽，是自然爱好者和摄影师的理想选择。'
        },
        {
          id: 2,
          title: '黄河文化深度游',
          image: 'https://images.unsplash.com/photo-1502082553048-f009c37129b9?auto=format&fit=crop&w=400&q=80',
          tag: '文化',
          duration: '3天2晚',
          startPoint: '东营市区',
          price: 568,
          rating: 4.6,
          description: '历史悠久，文化底蕴深厚，体验孙子故里和渔家风情。这条线路带你探访广饶孙子文化旅游区，参观孙子祠和博物馆，了解《孙子兵法》的智慧，随后前往黄河口文化旅游岛，住渔家民宿，品尝黄河大闸蟹，感受黄河入海的文化与生活气息。'
        },
        {
          id: 3,
          title: '家庭亲子休闲游',
          image: 'https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=400&q=80',
          tag: '亲子',
          duration: '2天1晚',
          startPoint: '东营市区',
          price: 458,
          rating: 4.9,
          description: '自然风光迷人，适合家庭度假，含亲子科普和野餐体验。带孩子游览黄河口生态旅游区，乘船欣赏黄河入海奇观，参加湿地生态讲解，天鹅湖景区还能喂天鹅、钓鱼，最后入住渔家民宿，全家共享轻松愉快的周末时光。'
        },
        {
          id: 4,
          title: '秋季红地毯摄影游',
          image: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=400&q=80',
          tag: '摄影',
          duration: '3天2晚',
          startPoint: '东营市区',
          price: 628,
          rating: 4.7,
          description: '美食天堂，体验当地特色，秋季拍摄红地毯湿地景观。每逢9-10月，黄河口生态旅游区的碱蓬草变成红色，形成震撼的自然画卷，游客可漫步湿地栈道，拍摄绝美照片，搭配当地黄河鲤鱼宴和渔家美食，享受视觉与味觉的双重盛宴。'
        },
        {
          id: 5,
          title: '黄河口生态探秘游',
          image: 'https://images.unsplash.com/photo-1500534314209-a25ddb2bd429?auto=format&fit=crop&w=400&q=80',
          tag: '探险',
          duration: '2天1晚',
          startPoint: '东营市区',
          price: 498,
          rating: 4.5,
          description: '探险者的乐园，充满刺激，探索黄河入海口的壮观景象。这条线路深入黄河三角洲国家级自然保护区，徒步穿越芦苇荡和滩涂，乘船体验黄河与渤海的交汇，感受大自然的磅礴力量，适合喜欢冒险和探索的游客前来挑战。'
        }
      ],
      currentPage: 1,
      pageSize: 4,
      totalTravelItems: 0,

      // 景点推荐
      attractionsList: [
        {
          id: 1,
          name: '黄河口生态旅游区',
          image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=400&q=80',
          level: '5A景区',
          rating: 4.9,
          price: 120
        },
        {
          id: 2,
          name: '天鹅湖景区',
          image: 'https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=400&q=80',
          level: '4A景区',
          rating: 4.7,
          price: 80
        },
        {
          id: 3,
          name: '孙子文化园',
          image: 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?фффauto=format&fit=crop&w=400&q=80',
          level: '4A景区',
          rating: 4.6,
          price: 60
        }
      ],

      // 新闻资讯
      activeNewsTab: 'latest',
      newsData: [
        {
          id: 1,
          title: '东营市推出夏季旅游优惠政策，景区门票全面打折',
          date: '2025-03-07',
          summary: '为促进夏季旅游市场繁荣，东营市文化和旅游局联合各大景区推出系列优惠政策...',
          views: 3562,
          comments: 125,
          category: 'latest',
          important: true
        },
        {
          id: 2,
          title: '第五届黄河口生态旅游节将于6月15日盛大开幕',
          date: '2025-03-06',
          summary: '本届旅游节以"生态东营·魅力黄河口"为主题，将举办系列文化旅游活动...',
          views: 2891,
          comments: 98,
          category: 'events'
        },
        {
          id: 3,
          title: '东营湿地保护取得显著成效，生物多样性明显提升',
          date: '2025-03-05',
          views: 1756,
          comments: 63,
          category: 'latest'
        },
        {
          id: 4,
          title: '东营市发布《旅游景区安全管理规范》，提升游客安全保障',
          date: '2025-03-04',
          views: 1432,
          comments: 47,
          category: 'policy'
        },
        {
          id: 5,
          title: '2025年东营市乡村旅游发展计划公布，助力乡村振兴',
          date: '2025-03-03',
          views: 1876,
          comments: 82,
          category: 'policy'
        },
        {
          id: 6,
          title: '东营美食文化节将于下月举行，展示黄河口特色美食',
          date: '2025-03-02',
          views: 2145,
          comments: 93,
          category: 'events',
          important: true
        }
      ],

      // 旅游攻略
      guideList: [
        {
          id: 1,
          title: '东营三日游完美攻略，带你领略黄河入海的壮美',
          author: '旅行家小王',
          authorAvatar: 'https://randomuser.me/api/portraits/men/32.jpg',
          image: 'https://images.pexels.com/photos/210243/pexels-photo-210243.jpeg?auto=compress&w=400&q=80',
          views: 12567,
          likes: 3456
        },
        {
          id: 2,
          title: '东营美食地图：不可错过的十大特色美食',
          author: '美食达人',
          authorAvatar: 'https://randomuser.me/api/portraits/women/44.jpg',
          image: 'https://images.unsplash.com/photo-1502082553048-f009c37129b9?auto=format&fit=crop&w=400&q=80',
          views: 9876,
          likes: 2345
        },
        {
          id: 3,
          title: '带着孩子游东营，亲子游玩全攻略',
          author: '亲子游专家',
          authorAvatar: 'https://randomuser.me/api/portraits/men/65.jpg',
          image: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=400&q=80',
          views: 8765,
          likes: 1987
        }
      ],

      // 错误处理
      errorDialogVisible: false,
      errorMessage: '',

      // 请求状态
      isRequesting: false,
      requestTimer: null
    };
  },
  computed: {
    paginatedTravelData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.travelData.slice(start, end);
    },
    filteredNewsData() {
      if (this.activeNewsTab === 'latest') {
        return this.newsData.slice(0, 5);
      } else {
        return this.newsData.filter(item => item.category === this.activeNewsTab).slice(0, 5);
      }
    }
  },
  created() {
    this.totalTravelItems = this.travelData.length;
    this.getUserInfo();
    this.getWeatherData();
  },
  methods: {
    gotoSpot() {
      this.$router.push("/booking")
    },
    getUserInfo() {
      if (this.isRequesting) return;
      this.isRequesting = true;
      this.requestTimer = setTimeout(() => {
        this.isRequesting = false;
        this.showError('请求超时，请稍后再试');
      }, 10000);
      getUserProfile().then(response => {
        clearTimeout(this.requestTimer);
        this.isRequesting = false;
        if (response.code === 200) {
          this.userInfo = response.data;
        }
      }).catch(error => {
        clearTimeout(this.requestTimer);
        this.isRequesting = false;
        console.error('获取用户信息失败:', error);
      });
    },
    showError(message) {
      this.errorMessage = message;
      this.errorDialogVisible = true;
    },
    formatDate(date, format) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      format = format.replace('YYYY', year);
      format = format.replace('MM', month);
      format = format.replace('DD', day);
      format = format.replace('HH', hours);
      format = format.replace('mm', minutes);
      return format;
    },
    getWeatherData() {
      this.weatherUpdateTime = this.formatDate(new Date(), 'MM-DD HH:mm');
    },
    debounce(fn, delay) {
      let timer = null;
      return function () {
        const context = this;
        const args = arguments;
        clearTimeout(timer);
        timer = setTimeout(function () {
          fn.apply(context, args);
        }, delay);
      };
    },
    handleSearch() {
      const debouncedSearch = this.debounce(() => {
        if (this.searchQuery.trim()) {
          this.searchLoading = true;
          setTimeout(() => {
            this.searchLoading = false;
            this.$message.success(`搜索${this.searchType === 'attraction' ? '景点' :
                this.searchType === 'route' ? '路线' :
                    this.searchType === 'guide' ? '攻略' : '酒店'}：${this.searchQuery}`);
          }, 500);
        } else {
          this.$message.warning('请输入搜索内容');
        }
      }, 300);
      debouncedSearch.call(this);
    },
    quickSearch(keyword) {
      this.searchQuery = keyword;
      this.handleSearch();
    },
    viewDetail(item) {
      this.$message.success(`查看详情：${item.title}`);
    },
    handleRankItemClick(row) {
      this.$message.success(`查看景点：${row.name}`);
    },
    handleRouteTabClick(tab) {
      this.$message.success(`切换到：${tab.label}`);
    },
    handlePageChange(page) {
      this.currentPage = page;
    },
    viewRouteDetail(item) {
      this.$message.success(`查看路线详情：${item.title}`);
    },
    viewAttractionDetail(item) {
      this.$message.success(`查看景点详情：${item.name}`);
    },
    handleNewsTabClick(tab) {
      this.$message.success(`切换到：${tab.label}`);
    },
    viewNewsDetail(item) {
      this.$message.success(`查看新闻详情：${item.title}`);
    },
    viewGuideDetail(item) {
      this.$message.success(`查看攻略详情：${item.title}`);
    },
    handleLogin() {
      this.$message.success('跳转到登录页面');
    },
    handleRegister() {
      this.$message.success('跳转到注册页面');
    },
    handleCommand(command) {
      if (command === 'logout') {
        this.$message.success('退出登录');
        this.userInfo = null;
      } else {
        this.$message.success(`导航到：${command}`);
      }
    }
  }
}
</script>

<style scoped>
.home {
  padding: 40px 20px;
  max-width: 1400px;
  margin: 0 auto;
  background: #f8fafc;
}

/* 搜索框 */
.search-container {
  margin-bottom: 40px;
  display: flex;
  justify-content: center;
}
.search-wrapper {
  width: 100%;
  max-width: 600px;
}
.logo {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  justify-content: center;
}
.logo-image {
  width: 50px;
  height: 50px;
  margin-right: 10px;
}
.logo h1 {
  font-size: 24px;
  color: #1f2a44;
  margin: 0;
}
.search-input {
  width: 100%;
  border-radius: 25px;
  overflow: hidden;
}
.search-input >>> .el-input__inner {
  border-radius: 0 25px 25px 0;
  border: 1px solid #e2e8f0;
  height: 46px;
}
.search-input >>> .el-input-group__prepend {
  border-radius: 25px 0 0 25px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-right: none;
}
.search-input >>> .el-input-group__append {
  border-radius: 0 25px 25px 0;
  background: #2563eb;
  border: none;
}
.search-input >>> .el-input-group__append .el-button {
  color: white;
}
.hot-keywords {
  margin-top: 12px;
  text-align: center;
  font-size: 13px;
  color: #64748b;
}
.hot-keywords span {
  margin-right: 10px;
}
.hot-keywords .el-button {
  color: #2563eb;
  font-size: 13px;
}

/* 轮播图 */
.carousel-container {
  margin-bottom: 40px;
}
.carousel-card {
  background: white;
  border: none;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.carousel-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}
.image-container {
  position: relative;
  overflow: hidden;
}
.carousel-image {
  width: 100%;
  height: 300px;
  object-fit: cover;
  transition: transform 0.3s ease;
}
.carousel-card:hover .carousel-image {
  transform: scale(1.05);
}
.carousel-content {
  padding: 20px;
  flex-grow: 1;
}
.carousel-content h4 {
  margin: 0 0 12px;
  font-size: 18px;
  font-weight: 600;
  color: #1f2a44;
}
.carousel-desc {
  font-size: 14px;
  color: #64748b;
  margin: 0 0 16px;
  line-height: 1.6;
}
.carousel-content .el-button {
  font-size: 14px;
  color: #2563eb;
}

/* 三列布局 */
.content-row {
  margin-bottom: 40px;
}
.rank-list, .travel-recommend, .attractions-recommend, .news-list, .travel-guide {
  margin-bottom: 40px;
}
.rank-list h3, .travel-recommend h3, .attractions-recommend h3, .news-list h3, .travel-guide h3 {
  font-size: 20px;
  color: #1f2a44;
  margin-bottom: 20px;
}

/* 排行榜 */
.rank-card {
  background: white;
  border: none;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 100%;
}
.rank-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}
.rank-content {
  display: flex;
  align-items: center;
  padding: 16px;
}
.rank-number {
  width: 24px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  border-radius: 50%;
  background: #eff6ff;
  color: #2563eb;
  font-size: 13px;
  font-weight: 600;
  margin-right: 12px;
}
.rank-number.top-three {
  background: #2563eb;
  color: white;
}
.rank-info {
  flex-grow: 1;
}
.rank-info h4 {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
  color: #1f2a44;
}
.rank-desc {
  font-size: 13px;
  color: #64748b;
  margin: 0;
}
.rank-card .el-button {
  font-size: 14px;
  color: #2563eb;
}

/* 旅游推荐 */
.travel-card {
  background: white;
  border: none;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.travel-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}
.travel-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  transition: transform 0.3s ease;
}
.travel-card:hover .travel-image {
  transform: scale(1.05);
}
.travel-tag {
  position: absolute;
  top: 12px;
  left: 12px;
  font-size: 13px;
  color: #2563eb;
  background: #eff6ff;
  padding: 4px 10px;
  border-radius: 12px;
}
.travel-content {
  padding: 20px;
  flex-grow: 1;
}
.travel-content h4 {
  margin: 0 0 12px;
  font-size: 18px;
  font-weight: 600;
  color: #1f2a44;
}
.travel-desc {
  font-size: 14px;
  color: #64748b;
  margin: 0 0 16px;
  line-height: 1.6;
}
.travel-footer {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
}
.travel-meta {
  font-size: 13px;
  color: #64748b;
  margin-right: 16px;
  margin-bottom: 8px;
}
.travel-price {
  display: flex;
  align-items: baseline;
  margin-bottom: 8px;
}
.price-value {
  font-size: 16px;
  color: #2563eb;
  font-weight: 600;
}
.price-unit {
  font-size: 13px;
  color: #64748b;
  margin-left: 4px;
}
.travel-card .el-button {
  font-size: 14px;
  color: #2563eb;
}

/* 景点推荐 */
.attraction-card {
  background: white;
  border: none;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.attraction-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}
.attraction-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  transition: transform 0.3s ease;
}
.attraction-card:hover .attraction-image {
  transform: scale(1.05);
}
.attraction-level {
  position: absolute;
  top: 12px;
  right: 12px;
  font-size: 13px;
  color: #2563eb;
  background: #eff6ff;
  padding: 4px 10px;
  border-radius: 12px;
}
.attraction-content {
  padding: 20px;
  flex-grow: 1;
}
.attraction-content h4 {
  margin: 0 0 12px;
  font-size: 18px;
  font-weight: 600;
  color: #1f2a44;
}
.attraction-desc {
  font-size: 14px;
  color: #64748b;
  margin: 0 0 16px;
}
.attraction-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.attraction-price {
  font-size: 14px;
  color: #2563eb;
}
.attraction-card .el-button {
  font-size: 14px;
  color: #2563eb;
}

/* 新闻资讯 */
.news-card {
  background: white;
  border: none;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 100%;
}
.news-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}
.news-content {
  padding: 20px;
}
.news-content h4 {
  margin: 0 0 12px;
  font-size: 16px;
  font-weight: 600;
  color: #1f2a44;
}
.news-desc {
  font-size: 14px;
  color: #64748b;
  margin: 0 0 16px;
  line-height: 1.6;
}
.news-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.news-meta {
  font-size: 13px;
  color: #64748b;
}
.news-card .el-button {
  font-size: 14px;
  color: #2563eb;
}

/* 旅游攻略 */
.guide-card {
  background: white;
  border: none;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 100%;
  display: flex;
}
.guide-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}
.guide-image {
  width: 120px;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}
.guide-card:hover .guide-image {
  transform: scale(1.05);
}
.guide-content {
  padding: 20px;
  flex-grow: 1;
}
.guide-content h4 {
  margin: 0 0 12px;
  font-size: 16px;
  font-weight: 600;
  color: #1f2a44;
}
.guide-desc {
  font-size: 14px;
  color: #64748b;
  margin: 0 0 16px;
}
.guide-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.guide-meta {
  font-size: 13px;
  color: #64748b;
}
.guide-card .el-button {
  font-size: 14px;
  color: #2563eb;
}

/* 天气预报 */
.weather-widget {
  margin-bottom: 40px;
}
.weather-widget h3 {
  font-size: 20px;
  color: #1f2a44;
  margin-bottom: 20px;
}
.weather-card {
  background: white;
  border: none;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.weather-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}
.weather-content {
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.weather-today {
  text-align: center;
  margin-bottom: 20px;
}
.weather-icon {
  font-size: 40px;
  color: #2563eb;
  margin-bottom: 12px;
}
.weather-temp {
  display: flex;
  align-items: baseline;
  margin-bottom: 12px;
}
.temp-value {
  font-size: 36px;
  font-weight: 600;
  color: #1f2a44;
}
.temp-unit {
  font-size: 18px;
  color: #64748b;
  margin-left: 4px;
}
.weather-desc {
  font-size: 16px;
  color: #64748b;
  margin: 0 0 12px;
}
.weather-update {
  font-size: 13px;
  color: #64748b;
  margin: 0;
}
.weather-details {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin-bottom: 20px;
}
.weather-detail {
  font-size: 14px;
  color: #64748b;
  margin: 0 12px 8px;
}
.weather-forecast {
  display: flex;
  justify-content: space-around;
  width: 100%;
}
.forecast-item {
  text-align: center;
}
.forecast-day {
  font-size: 14px;
  color: #1f2a44;
  margin: 0 0 8px;
}
.forecast-icon {
  font-size: 24px;
  color: #2563eb;
  margin-bottom: 8px;
}
.forecast-temp {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

/* 分页 */
.pagination-container {
  text-align: center;
  padding: 30px 0;
}
.pagination-container >>> .el-pagination {
  padding: 0;
}
.pagination-container >>> .el-pager li {
  font-size: 14px;
  margin: 0 4px;
  border-radius: 8px;
}
.pagination-container >>> .el-pagination button {
  border-radius: 8px;
}

/* 响应式调整 */
@media (max-width: 992px) {
  .home {
    padding: 30px 15px;
  }
  .carousel-image, .travel-image, .attraction-image {
    height: 180px;
  }
  .guide-image {
    width: 100px;
  }
}

@media (max-width: 768px) {
  .search-wrapper {
    max-width: 90%;
  }
  .carousel-image, .travel-image, .attraction-image {
    height: 160px;
  }
  .guide-image {
    width: 80px;
  }
  .carousel-content, .travel-content, .attraction-content, .news-content, .guide-content, .weather-content {
    padding: 16px;
  }
  .carousel-content h4, .travel-content h4, .attraction-content h4 {
    font-size: 16px;
  }
  .news-content h4, .guide-content h4 {
    font-size: 15px;
  }
}

@media (max-width: 576px) {
  .home {
    padding: 20px 10px;
  }
  .search-container {
    margin-bottom: 24px;
  }
  .carousel-image, .travel-image, .attraction-image {
    height: 140px;
  }
  .guide-image {
    width: 60px;
  }
  .carousel-desc, .travel-desc, .attraction-desc, .news-desc, .guide-desc {
    font-size: 13px;
  }
}
</style>