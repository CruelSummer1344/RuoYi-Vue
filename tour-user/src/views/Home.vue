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
            class="search-input"
            placeholder="搜索景点、路线、攻略..."
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
          <el-button slot="append" :loading="searchLoading" icon="el-icon-search" @click="handleSearch"></el-button>
        </el-input>
        <div class="hot-keywords">
          <span>热门搜索：</span>
          <el-button
              v-for="keyword in hotKeywords"
              :key="keyword"
              size="mini"
              type="text"
              @click="quickSearch(keyword)"
          >{{ keyword }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- 轮播图 -->
    <div class="carousel-container">
      <el-carousel arrow="always" class="carousel" height="450px" indicator-position="outside">
        <el-carousel-item v-for="item in carouselItems" :key="item.id">
          <div class="carousel-content">
            <img :src="item.image" alt="carousel image" class="carousel-image"/>
            <div class="carousel-caption">
              <h2>{{ item.title }}</h2>
              <p>{{ item.description }}</p>
              <el-button round type="primary" @click="viewDetail(item)">查看详情</el-button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 三列布局 -->
    <el-row :gutter="20" class="content-row">
      <!-- 热度排行榜 -->
      <el-col :span="6">
        <div class="rank-list">
          <div class="section-header">
            <h3>热度排行榜</h3>
            <el-button type="text" @click="viewAllRankings">查看全部 <i class="el-icon-arrow-right"></i></el-button>
          </div>
          <el-table
              :data="rankData"
              :show-header="false"
              style="width: 100%"
              @row-click="handleRankItemClick"
          >
            <el-table-column width="60">
              <template slot-scope="scope">
                <div :class="{'top-three': scope.row.rank <= 3}" class="rank-number">
                  {{ scope.row.rank }}
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="name">
              <template slot-scope="scope">
                <div class="rank-item-name">{{ scope.row.name }}</div>
                <div class="rank-item-views">
                  <i class="el-icon-view"></i> {{ scope.row.views || Math.floor(Math.random() * 10000) }}
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 天气预报 -->
        <div class="weather-widget">
          <div class="section-header">
            <h3>东营天气</h3>
            <span class="update-time">{{ weatherUpdateTime }}</span>
          </div>
          <div class="weather-content">
            <div class="weather-today">
              <div class="weather-icon">
                <i :class="weatherInfo.icon"></i>
              </div>
              <div class="weather-temp">
                <span class="temp-value">{{ weatherInfo.temperature }}</span>
                <span class="temp-unit">°C</span>
              </div>
              <div class="weather-desc">{{ weatherInfo.description }}</div>
            </div>
            <div class="weather-details">
              <div class="weather-detail-item">
                <span class="label">湿度：</span>
                <span class="value">{{ weatherInfo.humidity }}%</span>
              </div>
              <div class="weather-detail-item">
                <span class="label">风力：</span>
                <span class="value">{{ weatherInfo.wind }}</span>
              </div>
              <div class="weather-detail-item">
                <span class="label">空气质量：</span>
                <span :class="'air-quality-' + weatherInfo.airQualityLevel" class="value">
                  {{ weatherInfo.airQuality }}
                </span>
              </div>
            </div>
          </div>
          <div class="weather-forecast">
            <div v-for="(item, index) in weatherForecast" :key="index" class="forecast-item">
              <div class="forecast-day">{{ item.day }}</div>
              <div class="forecast-icon">
                <i :class="item.icon"></i>
              </div>
              <div class="forecast-temp">{{ item.tempRange }}</div>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 旅游推荐及简介 -->
      <el-col :span="12">
        <div class="travel-recommend">
          <div class="section-header">
            <h3>精品旅游路线</h3>
            <el-button type="text" @click="viewAllRoutes">查看全部 <i class="el-icon-arrow-right"></i></el-button>
          </div>

          <el-tabs v-model="activeRouteTab" @tab-click="handleRouteTabClick">
            <el-tab-pane label="热门路线" name="popular"></el-tab-pane>
            <el-tab-pane label="亲子游" name="family"></el-tab-pane>
            <el-tab-pane label="文化游" name="culture"></el-tab-pane>
            <el-tab-pane label="生态游" name="eco"></el-tab-pane>
          </el-tabs>

          <div class="travel-cards">
            <el-card
                v-for="item in paginatedTravelData"
                :key="item.id"
                class="travel-card"
                shadow="hover"
                @click.native="viewRouteDetail(item)"
            >
              <div class="travel-card-content">
                <div class="travel-card-image">
                  <img :src="item.image || `/assets/route-${item.id}.jpg`" alt="旅游路线图片">
                  <div v-if="item.tag" class="travel-card-tag">{{ item.tag }}</div>
                </div>
                <div class="travel-card-info">
                  <h4>{{ item.title }}</h4>
                  <div class="travel-card-meta">
                    <span><i class="el-icon-time"></i> {{ item.duration || '2天1晚' }}</span>
                    <span><i class="el-icon-location"></i> {{ item.startPoint || '东营市区' }}</span>
                  </div>
                  <p class="travel-card-desc">{{ item.description }}</p>
                  <div class="travel-card-footer">
                    <div class="travel-card-price">
                      <span class="price-label">¥</span>
                      <span class="price-value">{{ item.price || Math.floor(Math.random() * 500 + 300) }}</span>
                      <span class="price-unit">起/人</span>
                    </div>
                    <div class="travel-card-rating">
                      <el-rate
                          v-model="item.rating || 4.5"
                          disabled
                          score-template="{value}"
                          text-color="#ff9900"
                      >
                      </el-rate>
                    </div>
                  </div>
                </div>
              </div>
            </el-card>
          </div>

          <div class="pagination-container">
            <el-pagination
                :current-page.sync="currentPage"
                :page-size="pageSize"
                :total="totalTravelItems"
                background
                layout="prev, pager, next"
                @current-change="handlePageChange"
            ></el-pagination>
          </div>
        </div>

        <!-- 景点推荐 -->
        <div class="attractions-recommend">
          <div class="section-header">
            <h3>热门景点</h3>
            <el-button type="text" @click="viewAllAttractions">查看全部 <i class="el-icon-arrow-right"></i></el-button>
          </div>

          <el-row :gutter="20">
            <el-col v-for="(item, index) in attractionsList" :key="index" :span="8">
              <div class="attraction-card" @click="viewAttractionDetail(item)">
                <div class="attraction-image">
                  <img :src="item.image || `/assets/attraction-${index+1}.jpg`" alt="景点图片">
                  <div class="attraction-level">{{ item.level }}</div>
                </div>
                <div class="attraction-info">
                  <h4>{{ item.name }}</h4>
                  <div class="attraction-rating">
                    <el-rate
                        v-model="item.rating"
                        disabled
                        score-template="{value}"
                        text-color="#ff9900"
                    >
                    </el-rate>
                    <span class="rating-score">{{ item.rating }}分</span>
                  </div>
                  <div class="attraction-price">
                    <span class="price-value">¥{{ item.price }}</span>
                    <span class="price-label">起</span>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-col>

      <!-- 新闻资讯快讯 -->
      <el-col :span="6">
        <div class="news-list">
          <div class="section-header">
            <h3>旅游资讯</h3>
            <el-button type="text" @click="viewAllNews">查看全部 <i class="el-icon-arrow-right"></i></el-button>
          </div>

          <el-tabs v-model="activeNewsTab" @tab-click="handleNewsTabClick">
            <el-tab-pane label="最新动态" name="latest"></el-tab-pane>
            <el-tab-pane label="活动公告" name="events"></el-tab-pane>
            <el-tab-pane label="政策法规" name="policy"></el-tab-pane>
          </el-tabs>

          <el-timeline>
            <el-timeline-item
                v-for="item in filteredNewsData"
                :key="item.id"
                :color="item.important ? '#ff4949' : ''"
                :timestamp="item.date"
                placement="top"
            >
              <div class="news-item" @click="viewNewsDetail(item)">
                <div class="news-title">{{ item.title }}</div>
                <div v-if="item.summary" class="news-summary">{{ item.summary }}</div>
                <div v-if="item.views || item.comments" class="news-meta">
                  <span v-if="item.views"><i class="el-icon-view"></i> {{ item.views }}</span>
                  <span v-if="item.comments"><i class="el-icon-chat-line-square"></i> {{ item.comments }}</span>
                </div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>

        <!-- 旅游攻略 -->
        <div class="travel-guide">
          <div class="section-header">
            <h3>旅游攻略</h3>
            <el-button type="text" @click="viewAllGuides">查看全部 <i class="el-icon-arrow-right"></i></el-button>
          </div>

          <div class="guide-list">
            <div
                v-for="(item, index) in guideList"
                :key="index"
                class="guide-item"
                @click="viewGuideDetail(item)"
            >
              <div class="guide-image">
                <img :src="item.image || `/assets/guide-${index+1}.jpg`" alt="攻略图片">
              </div>
              <div class="guide-info">
                <h4>{{ item.title }}</h4>
                <div class="guide-author">
                  <img :src="item.authorAvatar || '/assets/default-avatar.png'" alt="作者头像" class="author-avatar">
                  <span>{{ item.author }}</span>
                </div>
                <div class="guide-meta">
                  <span><i class="el-icon-view"></i> {{ item.views }}</span>
                  <span><i class="el-icon-star-off"></i> {{ item.likes }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 返回顶部 -->
    <el-backtop :bottom="50" :right="50"></el-backtop>

    <!-- 全局加载状态 -->
    <el-dialog
        :visible.sync="errorDialogVisible"
        center
        title="提示"
        width="30%"
    >
      <span>{{ errorMessage }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="errorDialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {getUserProfile} from "@/api/system/user";

export default {
  name: 'Home',
  data() {
    return {
      // 用户信息
      userInfo: null,

      // 搜索相关
      searchQuery: '', // 搜索框输入内容
      searchType: 'attraction', // 搜索类型
      searchLoading: false, // 搜索加载状态
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
        {title: '景点门票', icon: 'el-icon-ticket', route: '/attractions'},
        {title: '酒店住宿', icon: 'el-icon-house', route: '/hotels'},
        {title: '特色美食', icon: 'el-icon-dish', route: '/food'},
        {title: '旅游路线', icon: 'el-icon-map-location', route: '/routes'},
        {title: '文化体验', icon: 'el-icon-reading', route: '/culture'},
        {title: '交通指南', icon: 'el-icon-truck', route: '/transportation'}
      ],

      // 热度排行榜
      rankData: [
        {rank: 1, name: '黄河口生态旅游区', views: 12580},
        {rank: 2, name: '黄河三角洲国家级自然保护区', views: 9876},
        {rank: 3, name: '天鹅湖景区', views: 8654},
        {rank: 4, name: '黄河口文化旅游岛', views: 7532},
        {rank: 5, name: '东营胜利广场', views: 6421},
        {rank: 6, name: '孙子文化旅游区', views: 5890},
        {rank: 7, name: '利津黄河公园', views: 4567},
        {rank: 8, name: '广饶孙子兵法城', views: 3456}
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
        {day: '今天', icon: 'el-icon-sunny', tempRange: '22°~28°'},
        {day: '明天', icon: 'el-icon-cloudy', tempRange: '20°~26°'},
        {day: '后天', icon: 'el-icon-partly-cloudy', tempRange: '19°~25°'}
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
      currentPage: 1, // 当前页码
      pageSize: 2,    // 每页显示条数
      totalTravelItems: 0, // 旅游推荐总数

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
          image: 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=400&q=80',
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
    // 分页后的旅游推荐数据
    paginatedTravelData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.travelData.slice(start, end);
    },

    // 根据标签筛选的新闻数据
    filteredNewsData() {
      if (this.activeNewsTab === 'latest') {
        return this.newsData.slice(0, 5);
      } else {
        return this.newsData.filter(item => item.category === this.activeNewsTab).slice(0, 5);
      }
    }
  },
  created() {
    // 初始化时设置总数
    this.totalTravelItems = this.travelData.length;
    this.getUserInfo();

    // 模拟获取天气数据
    this.getWeatherData();
  },
  methods: {
    // 获取用户信息
    getUserInfo() {
      // 防止重复请求
      if (this.isRequesting) {
        return;
      }

      this.isRequesting = true;

      // 设置请求超时
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

        // 处理错误，但不显示给用户，保持未登录状态
        console.error('获取用户信息失败:', error);
      });
    },

    // 显示错误信息
    showError(message) {
      this.errorMessage = message;
      this.errorDialogVisible = true;
    },

    // 格式化日期
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

    // 模拟获取天气数据
    getWeatherData() {
      // 实际项目中应该调用天气API
      this.weatherUpdateTime = this.formatDate(new Date(), 'MM-DD HH:mm');
    },

    // 防抖函数
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

    // 搜索处理
    handleSearch: function () {
      // 使用防抖处理搜索
      const debouncedSearch = this.debounce(function () {
        if (this.searchQuery.trim()) {
          this.searchLoading = true;

          // 模拟搜索请求
          setTimeout(() => {
            this.searchLoading = false;
            this.$message.success(`正在搜索${this.searchType === 'attraction' ? '景点' :
                this.searchType === 'route' ? '路线' :
                    this.searchType === 'guide' ? '攻略' : '酒店'}：${this.searchQuery}`);
            // 实际项目中应该跳转到搜索结果页面
            // this.$router.push({
            //   path: '/search',
            //   query: {
            //     type: this.searchType,
            //     keyword: this.searchQuery
            //   }
            // });
          }, 500);
        } else {
          this.$message.warning('请输入搜索内容');
        }
      }, 300);

      debouncedSearch.call(this);
    },

    // 快速搜索
    quickSearch(keyword) {
      this.searchQuery = keyword;
      this.handleSearch();
    },

    // 导航菜单选择
    handleSelect(key) {
      // 实际项目中应该跳转到对应页面
      this.$message.success(`导航到：${key}`);
    },

    // 查看轮播图详情
    viewDetail(item) {
      this.$message.success(`查看详情：${item.title}`);
    },

    // 导航到指定路由
    navigateTo(route) {
      this.$message.success(`导航到：${route}`);
    },

    // 查看所有排行榜
    viewAllRankings() {
      this.$message.success('查看全部排行榜');
    },

    // 点击排行榜项目
    handleRankItemClick(row) {
      this.$message.success(`查看景点：${row.name}`);
    },

    // 路线标签切换
    handleRouteTabClick(tab) {
      this.$message.success(`切换到：${tab.label}`);
      // 实际项目中应该根据标签筛选数据
    },

    // 分页切换
    handlePageChange(page) {
      this.currentPage = page;
    },

    // 查看所有路线
    viewAllRoutes() {
      this.$message.success('查看全部旅游路线');
    },

    // 查看路线详情
    viewRouteDetail(item) {
      this.$message.success(`查看路线详情：${item.title}`);
    },

    // 查看所有景点
    viewAllAttractions() {
      this.$message.success('查看全部景点');
    },

    // 查看景点详情
    viewAttractionDetail(item) {
      this.$message.success(`查看景点详情：${item.name}`);
    },

    // 新闻标签切换
    handleNewsTabClick(tab) {
      this.$message.success(`切换到：${tab.label}`);
    },

    // 查看所有新闻
    viewAllNews() {
      this.$message.success('查看全部新闻');
    },

    // 查看新闻详情
    viewNewsDetail(item) {
      this.$message.success(`查看新闻详情：${item.title}`);
    },

    // 查看所有攻略
    viewAllGuides() {
      this.$message.success('查看全部攻略');
    },

    // 查看攻略详情
    viewGuideDetail(item) {
      this.$message.success(`查看攻略详情：${item.title}`);
    },

    // 登录处理
    handleLogin() {
      this.$message.success('跳转到登录页面');
    },

    // 注册处理
    handleRegister() {
      this.$message.success('跳转到注册页面');
    },

    // 下拉菜单命令处理
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
  min-height: 100vh;
  background-color: #f5f7fa;
  display: flex;
  flex-direction: column;
  align-items: center; /* 确保子元素水平居中 */
  padding: 0; /* 移除可能存在的内边距 */
}

/* 搜索框样式 */
.search-container {
  display: flex;
  width: 100%;
  background-color: #fff;
  padding: 15px 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.search-wrapper {
  width: 80%;
  margin: 0 auto;
}

.logo {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.logo-image {
  width: 50px;
  height: 50px;
  margin-right: 10px;
}

.logo h1 {
  font-size: 24px;
  color: #1890ff;
  margin: 0;
}

.search-input {
  width: 100%;
}

.hot-keywords {
  margin-top: 10px;
  font-size: 13px;
  color: #999;
}

.hot-keywords span {
  margin-right: 10px;
}

.hot-keywords .el-button {
  padding: 0 5px;
  color: #666;
}

.hot-keywords .el-button:hover {
  color: #1890ff;
}

/* 轮播图样式 */
.carousel-container {
  width: 80%;
  margin: 20px auto;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.carousel {
  border-radius: 8px;
}

.carousel-content {
  position: relative;
  height: 100%;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.carousel-caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
  color: #fff;
  text-align: left;
}

.carousel-caption h2 {
  font-size: 28px;
  margin-bottom: 10px;
}

.carousel-caption p {
  font-size: 16px;
  margin-bottom: 15px;
  max-width: 60%;
}

/* 三列布局样式 */
.content-row {
  width: 80%;
  max-width: 1200px; /* 添加最大宽度，防止内容过宽 */
  margin: 20px auto;
  display: flex;
  justify-content: center; /* 确保内容水平居中 */
  align-items: flex-start; /* 子元素顶部对齐 */
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  border-bottom: 1px solid #eaeaea;
  padding-bottom: 10px;
}

.section-header h3 {
  font-size: 18px;
  color: #333;
  margin: 0;
  position: relative;
  padding-left: 10px;
}

.section-header h3::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 18px;
  background-color: #1890ff;
  border-radius: 2px;
}

/* 排行榜样式 */
.rank-list {
  background: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

.rank-number {
  width: 24px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  border-radius: 50%;
  background-color: #f0f0f0;
  color: #666;
  font-weight: bold;
}

.rank-number.top-three {
  background-color: #1890ff;
  color: #fff;
}

.rank-item-name {
  font-size: 14px;
  color: #333;
  margin-bottom: 5px;
}

.rank-item-views {
  font-size: 12px;
  color: #999;
}

/* 天气组件 */
.weather-widget {
  background: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.update-time {
  font-size: 12px;
  color: #999;
}

.weather-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px 0;
}

.weather-today {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 15px;
}

.weather-icon {
  font-size: 40px;
  color: #1890ff;
  margin-bottom: 10px;
}

.weather-temp {
  display: flex;
  align-items: flex-start;
}

.temp-value {
  font-size: 36px;
  font-weight: bold;
  color: #333;
}

.temp-unit {
  font-size: 16px;
  margin-top: 5px;
  color: #666;
}

.weather-desc {
  font-size: 16px;
  color: #666;
  margin-top: 5px;
}

.weather-details {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  margin-bottom: 15px;
}

.weather-detail-item {
  width: 48%;
  margin-bottom: 10px;
  font-size: 13px;
  color: #666;
}

.weather-detail-item .label {
  color: #999;
}

.weather-forecast {
  width: 100%;
  display: flex;
  justify-content: space-between;
  border-top: 1px solid #f0f0f0;
  padding-top: 15px;
}

.forecast-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.forecast-day {
  font-size: 13px;
  color: #666;
  margin-bottom: 5px;
}

.forecast-icon {
  font-size: 20px;
  color: #1890ff;
  margin-bottom: 5px;
}

.forecast-temp {
  font-size: 13px;
  color: #333;
}

/* 旅游推荐样式 */
.travel-recommend {
  background: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

.travel-cards {
  margin-top: 15px;
}

.travel-card {
  margin-bottom: 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.travel-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.travel-card-content {
  display: flex;
}

.travel-card-image {
  width: 200px;
  height: 150px;
  position: relative;
  overflow: hidden;
  border-radius: 4px;
  margin-right: 15px;
}

.travel-card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.travel-card:hover .travel-card-image img {
  transform: scale(1.05);
}

.travel-card-tag {
  position: absolute;
  top: 10px;
  left: 10px;
  padding: 2px 8px;
  background-color: #1890ff;
  color: #fff;
  font-size: 12px;
  border-radius: 4px;
}

.travel-card-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.travel-card-info h4 {
  font-size: 16px;
  color: #333;
  margin-top: 0;
  margin-bottom: 10px;
}

.travel-card-meta {
  display: flex;
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
}

.travel-card-meta span {
  margin-right: 15px;
  display: flex;
  align-items: center;
}

.travel-card-meta i {
  margin-right: 5px;
}

.travel-card-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 10px;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.travel-card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.travel-card-price {
  display: flex;
  align-items: baseline;
}

.price-label {
  color: #ff4d4f;
  font-size: 14px;
  margin-right: 2px;
}

.price-value {
  color: #ff4d4f;
  font-size: 20px;
  font-weight: bold;
}

.price-unit {
  color: #999;
  font-size: 12px;
  margin-left: 2px;
}

.pagination-container {
  text-align: center;
  margin-top: 20px;
}

/* 景点推荐样式 */
.attractions-recommend {
  background: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.attraction-card {
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 15px;
}

.attraction-card:hover {
  transform: translateY(-5px);
}

.attraction-image {
  height: 150px;
  position: relative;
  overflow: hidden;
  border-radius: 8px;
}

.attraction-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.attraction-card:hover .attraction-image img {
  transform: scale(1.05);
}

.attraction-level {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 2px 8px;
  background-color: rgba(0, 0, 0, 0.6);
  color: #fff;
  font-size: 12px;
  border-radius: 4px;
}

.attraction-info {
  padding: 10px 0;
}

.attraction-info h4 {
  font-size: 15px;
  color: #333;
  margin: 0 0 5px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.attraction-rating {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.rating-score {
  font-size: 13px;
  color: #ff9900;
  margin-left: 5px;
}

.attraction-price {
  font-size: 13px;
}

.attraction-price .price-value {
  font-size: 16px;
}

/* 新闻列表样式 */
.news-list {
  background: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

.news-item {
  cursor: pointer;
  padding: 5px 0;
}

.news-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 5px;
  line-height: 1.4;
}

.news-summary {
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
  line-height: 1.4;
}

.news-meta {
  display: flex;
  font-size: 12px;
  color: #999;
}

.news-meta span {
  margin-right: 10px;
  display: flex;
  align-items: center;
}

.news-meta i {
  margin-right: 3px;
}

/* 旅游攻略样式 */
.travel-guide {
  background: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.guide-list {
  display: flex;
  flex-direction: column;
}

.guide-item {
  display: flex;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
}

.guide-item:last-child {
  border-bottom: none;
}

.guide-image {
  width: 80px;
  height: 60px;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 10px;
}

.guide-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.guide-info {
  flex: 1;
}

.guide-info h4 {
  font-size: 14px;
  color: #333;
  margin: 0 0 5px 0;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}

.guide-author {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
}

.author-avatar {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  margin-right: 5px;
}

.guide-meta {
  display: flex;
  font-size: 12px;
  color: #999;
}

.guide-meta span {
  margin-right: 10px;
  display: flex;
  align-items: center;
}

.guide-meta i {
  margin-right: 3px;
}

/* 底部区域 */
.footer {
  background-color: #333;
  color: #fff;
  padding: 40px 0 20px;
  margin-top: 40px;
}

.footer-content {
  width: 80%;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
}

.footer-section {
  width: 22%;
  margin-bottom: 20px;
}

.footer-section h4 {
  font-size: 16px;
  margin-bottom: 15px;
  position: relative;
  padding-bottom: 10px;
}

.footer-section h4::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 30px;
  height: 2px;
  background-color: #1890ff;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section ul li {
  margin-bottom: 8px;
}

.footer-section ul li a {
  color: #bbb;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
}

.footer-section ul li a:hover {
  color: #1890ff;
}

.social-media {
  display: flex;
  flex-direction: column;
}

.social-media a {
  color: #bbb;
  text-decoration: none;
  font-size: 14px;
  margin-bottom: 8px;
  transition: color 0.3s;
  display: flex;
  align-items: center;
}

.social-media a:hover {
  color: #1890ff;
}

.social-media a i {
  margin-right: 5px;
}

.footer-bottom {
  width: 80%;
  margin: 0 auto;
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #444;
}

.footer-bottom p {
  font-size: 13px;
  color: #999;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .search-wrapper,
  .carousel-container,
  .content-row,
  .footer-content,
  .footer-bottom {
    width: 90%;
  }
}

@media (max-width: 992px) {
  .travel-card-content {
    flex-direction: column;
  }

  .travel-card-image {
    width: 100%;
    margin-right: 0;
    margin-bottom: 10px;
  }

  .footer-section {
    width: 48%;
  }
}

@media (max-width: 768px) {
  .search-input {
    width: 100%;
  }

  .carousel-caption h2 {
    font-size: 22px;
  }

  .carousel-caption p {
    font-size: 14px;
    max-width: 100%;
  }

  .quick-access-item i {
    font-size: 24px;
  }

  .quick-access-item span {
    font-size: 12px;
  }

  .footer-section {
    width: 100%;
  }
}
</style>