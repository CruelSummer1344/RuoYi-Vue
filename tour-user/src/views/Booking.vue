<template>
  <div class="booking">
    <!-- 分类选择 -->
    <div class="category-tabs">
      <el-tabs v-model="activeCategory" @tab-click="handleCategoryChange">
        <el-tab-pane label="景点预订" name="tourism">
          <!-- 项目搜索区域 -->
          <h2>景点预订</h2>
          <div class="search-container">
            <el-form :inline="true" :model="searchForm" class="demo-form-inline">
              <el-form-item label="景点名称">
                <el-input v-model="searchForm.name" class="search-input" placeholder="输入景点名称"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="searchSpot">搜索</el-button>
                <el-button @click="resetSearch">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 项目列表 -->
          <el-row :gutter="24" class="specialty-list">
            <el-col v-for="project in filteredProjects" :key="project.id" :md="8" :sm="12" :xs="24">
              <el-card class="specialty-card" shadow="never">
                <div class="image-container">
                  <el-image :src="realUrl(project.image)" alt="项目图片" class="specialty-image"/>
                  <div v-if="project.tag" class="specialty-type-tag">{{ project.tag }}</div>
                </div>
                <div class="specialty-content">
                  <h4>{{ project.name }}</h4>
                  <p class="specialty-desc">{{ project.description }}</p>
                  <div class="specialty-info">
                    <span><i class="el-icon-location"></i> {{ project.location }}</span>
                    <span><i class="el-icon-view"></i> {{ project.views }}</span>
                  </div>
                  <div class="package-list">
                    <h4>可选套餐</h4>
                    <el-table :data="project.packages" class="package-table" style="width: 100%">
                      <el-table-column label="套餐名称" prop="name"></el-table-column>
                      <el-table-column label="价格" prop="price">
                        <template slot-scope="scope">
                          <span class="price">¥{{ scope.row.price }}</span>
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" width="100">
                        <template slot-scope="scope">
                          <el-button
                              class="action-button"
                              size="small"
                              type="text"
                              @click="openBookingDialog(project, scope.row)"
                          >
                            预订
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <!-- 分页 -->
          <div v-if="filteredProjects.length > 0" class="pagination-container">
            <el-pagination
                :page-size="6"
                :total="filteredProjects.length"
                background
                layout="prev, pager, next"
                @current-change="handlePageChange"
            ></el-pagination>
          </div>
        </el-tab-pane>

        <el-tab-pane label="旅游项目" name="project">
          <!-- 旅游项目搜索区域 -->
          <h2>旅游项目预订</h2>
          <div class="search-container">
            <el-form :inline="true" :model="projectSearchForm" class="demo-form-inline">
              <el-form-item label="项目名称">
                <el-input v-model="projectSearchForm.name" class="search-input" placeholder="输入项目名称"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="searchTourProjects">搜索</el-button>
                <el-button @click="resetTourProjectSearch">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 旅游项目列表 -->
          <el-row :gutter="24" class="specialty-list">
            <el-col v-for="project in filteredTourProjects" :key="project.id" :md="8" :sm="12" :xs="24">
              <el-card class="specialty-card" shadow="never">
                <div class="image-container">
                  <el-image :src="realUrl(project.image)" alt="项目图片" class="specialty-image"/>
                  <div v-if="project.tag" class="specialty-type-tag">{{ project.tag }}</div>
                </div>
                <div class="specialty-content">
                  <h4>{{ project.name }}</h4>
                  <p class="specialty-desc">{{ project.description }}</p>
                  <div class="specialty-info">
                    <span><i class="el-icon-location"></i> {{ project.location }}</span>
                    <span><i class="el-icon-view"></i> {{ project.views }}</span>
                  </div>
                  <div class="package-list">
                    <h4>可选套餐</h4>
                    <el-table :data="project.packages" class="package-table" style="width: 100%">
                      <el-table-column label="套餐名称" prop="name"></el-table-column>
                      <el-table-column label="价格" prop="price">
                        <template slot-scope="scope">
                          <span class="price">¥{{ scope.row.price }}</span>
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" width="100">
                        <template slot-scope="scope">
                          <el-button
                              class="action-button"
                              size="small"
                              type="text"
                              @click="openTourProjectBookingDialog(project, scope.row)"
                          >
                            预订
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane label="酒店预订" name="hotel">
          <!-- 酒店搜索区域 -->
          <h2>酒店预订</h2>
          <div class="search-container">
            <el-form :inline="true" :model="hotelSearchForm" class="demo-form-inline">
              <el-form-item label="酒店名称">
                <el-input v-model="hotelSearchForm.name" class="search-input" placeholder="输入酒店名称"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="searchHotels">搜索</el-button>
                <el-button @click="resetHotelSearch">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 酒店列表 -->
          <div class="hotel-list">
            <el-card v-for="hotel in filteredHotels" :key="hotel.id" class="hotel-card specialty-card" shadow="never">
              <div class="hotel-content">
                <div class="hotel-image-container">
                  <el-image :src="realUrl(hotel.image)" alt="酒店图片" class="specialty-image"/>
                  <div v-if="hotel.tag" class="specialty-type-tag">{{ hotel.tag }}</div>
                </div>
                <div class="hotel-info">
                  <h4>{{ hotel.name }}</h4>
                  <p class="specialty-desc">{{ hotel.description }}</p>
                  <div class="specialty-info">
                    <span><i class="el-icon-location"></i> {{ hotel.location }}</span>
                    <span><i class="el-icon-star-on"></i> {{ hotel.rating }}分</span>
                    <span><i class="el-icon-view"></i> {{ hotel.views }}浏览</span>
                  </div>
                  <div class="room-list">
                    <h4>可选房型</h4>
                    <el-table :data="hotel.rooms" class="package-table" style="width: 100%">
                      <el-table-column label="房型" prop="name"></el-table-column>
                      <el-table-column label="价格" prop="price">
                        <template slot-scope="scope">
                          <span class="price">¥{{ scope.row.price }}</span>
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" width="100">
                        <template slot-scope="scope">
                          <el-button
                              size="small"
                              type="text"
                              @click="openHotelBookingDialog(hotel, scope.row)"
                              class="action-button"
                          >
                            预订
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>
              </div>
            </el-card>
          </div>

          <!-- 分页 -->
          <div v-if="filteredHotels.length > 0" class="pagination-container">
            <el-pagination
                :page-size="5"
                :total="filteredHotels.length"
                background
                layout="prev, pager, next"
                @current-change="handleHotelPageChange"
            ></el-pagination>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 订单管理 -->
    <div class="order-management">
      <h2>我的订单</h2>
      <div class="search-container">
        <el-form :inline="true" :model="orderSearchForm" class="demo-form-inline">
          <el-form-item label="订单状态">
            <el-select v-model="orderSearchForm.status" class="search-input" clearable placeholder="选择订单状态">
              <el-option label="全部" value=""></el-option>
              <el-option label="已支付" value="已支付"></el-option>
              <el-option label="待支付" value="待支付"></el-option>
              <el-option label="已取消" value="已取消"></el-option>
              <el-option label="已完成" value="已完成"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="日期范围">
            <el-date-picker
                v-model="orderSearchForm.dateRange"
                end-placeholder="结束日期"
                range-separator="至"
                start-placeholder="开始日期"
                type="daterange"
                value-format="yyyy-MM-dd"
                class="search-date"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchOrders">搜索</el-button>
            <el-button @click="resetOrderSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 订单列表 -->
      <el-card class="order-table-card" shadow="never">
        <el-table v-if="filteredOrders.length > 0" :data="filteredOrders" class="order-table" style="width: 100%">
          <el-table-column label="订单号" prop="orderId" width="180"></el-table-column>
          <el-table-column label="项目名称" prop="projectName"></el-table-column>
          <el-table-column label="订单类型" prop="type">
            <template slot-scope="scope">
              <span v-if="scope.row.spotId">景点</span>
              <span v-else-if="scope.row.hotelId">酒店</span>
              <span v-else>旅游项目</span>
            </template>
          </el-table-column>
          <el-table-column label="创建日期" prop="createTime" width="120"></el-table-column>
          <el-table-column label="人数" prop="peopleCount" width="80"></el-table-column>
          <el-table-column label="总价" prop="totalPrice" width="120">
            <template slot-scope="scope">
              <span class="price">¥{{ scope.row.totalPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" prop="status" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)" class="status-tag">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template slot-scope="scope">
              <el-button class="action-button" size="small" type="text" @click="viewOrderDetails(scope.row)">查看详情
              </el-button>
              <el-button
                  v-if="scope.row.status === '待支付'"
                  class="action-button"
                  size="small"
                  type="text"
                  @click="cancelOrder(scope.row)"
              >
                取消订单
              </el-button>
              <el-button
                  v-if="scope.row.status === '已支付' || scope.row.status === '已完成'"
                  class="action-button"
                  size="small"
                  type="text"
                  @click="goToReview(scope.row)"
              >
                评价
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="暂无订单"></el-empty>
      </el-card>
    </div>

    <!-- 各种对话框 (保持原代码不变) -->
  </div>
</template>

<script>
import {getHotel, listHotel} from "@/api/hotel/hotel";
import {getSpot, listSpot} from "@/api/spot/spot";
import {addOrder, listOrder, updateOrder} from "@/api/order/order";
import {getProject, listProject} from "@/api/projects/projects";

export default {
  name: 'Booking',
  data() {
    return {
      // 当前选中的分类
      activeCategory: 'tourism',

      // 对话框显示控制
      dialogVisible: false,
      orderDetailVisible: false,
      hotelDialogVisible: false,
      tourProjectDialogVisible: false,

      // 预订表单
      bookingForm: {
        projectId: '',
        projectName: '',
        packageId: '',
        packageName: '',
        hotel: '',
        restaurant: '',
        activity: '',
        date: '',
        peopleCount: 1,
        paymentMethod: 'wechat',
      },

      // 旅游项目预订表单
      tourProjectBookingForm: {
        productId: '',
        projectName: '',
        packageId: '',
        packageName: '',
        date: '',
        peopleCount: 1,
        paymentMethod: 'wechat',
      },

      // 酒店预订表单
      hotelBookingForm: {
        hotelId: '',
        hotelName: '',
        roomId: '',
        roomType: '',
        checkInDate: '',
        checkOutDate: '',
        checkInTime: '14:00',
        checkOutTime: '12:00',
        roomCount: 1,
        paymentMethod: 'wechat'
      },

      // 表单验证规则
      rules: {
        date: [{required: true, message: '请选择日期', trigger: 'change'}],
        peopleCount: [{required: true, message: '请选择人数', trigger: 'change'}],
        paymentMethod: [{required: true, message: '请选择支付方式', trigger: 'change'}]
      },

      // 酒店表单验证规则
      hotelRules: {
        checkInDate: [{required: true, message: '请选择入住日期', trigger: 'change'}],
        checkOutDate: [{required: true, message: '请选择离店日期', trigger: 'change'}],
        roomCount: [{required: true, message: '请选择房间数量', trigger: 'change'}],
        paymentMethod: [{required: true, message: '请选择支付方式', trigger: 'change'}]
      },

      // 项目搜索表单
      searchForm: {
        name: '',
        type: ''
      },

      // 酒店搜索表单
      hotelSearchForm: {
        name: '',
        priceRange: ''
      },

      // 订单搜索表单
      orderSearchForm: {
        status: '',
        dateRange: []
      },

      // 选中的订单（用于详情展示）
      selectedOrder: null,

      // 项目数据
      projects: [
        {
          id: 1,
          name: '黄河口生态旅游区',
          description: '探索中国唯一能够同时观赏黄河入海奇观和湿地风光的生态旅游胜地',
          location: '东营市河口区',
          type: 'attraction',
          tag: '5A景区',
          views: 12580,
          image: 'http://www.xinhuanet.com/2021-06/09/1127547819_16232418196431n.jpeg',
          packages: [
            {
              id: 101,
              name: '标准门票',
              price: 120,
              description: '含景区内所有景点参观'
            },
            {
              id: 102,
              name: '黄河入海观光套餐',
              price: 220,
              description: '含门票、游船、导游讲解',
              activity: '黄河入海观光船游览'
            },
            {
              id: 103,
              name: '生态探秘两日游',
              price: 580,
              description: '含门票、住宿、餐饮、导游',
              hotel: '黄河口生态度假酒店',
              restaurant: '渔家风味餐厅',
              activity: '湿地徒步+观鸟+黄河入海观光'
            }
          ]
        },
        {
          id: 2,
          name: '天鹅湖景区',
          description: '中国最大的新生湿地，国际重要湿地，候鸟迁徙的重要驿站',
          location: '东营市东营区',
          type: 'eco',
          tag: '4A景区',
          views: 8654,
          image: 'http://www.xinhuanet.com/2021-06/09/1127547819_16232418196441n.jpeg',
          packages: [
            {
              id: 201,
              name: '标准门票',
              price: 80,
              description: '含景区内所有景点参观'
            },
            {
              id: 202,
              name: '观鸟摄影套餐',
              price: 150,
              description: '含门票、专业观鸟向导、摄影指导',
              activity: '专业观鸟向导服务'
            },
            {
              id: 203,
              name: '湿地生态两日游',
              price: 480,
              description: '含门票、住宿、餐饮、导游',
              hotel: '天鹅湖度假酒店',
              restaurant: '生态农家餐厅',
              activity: '湿地徒步+观鸟+摄影指导'
            }
          ]
        },
        {
          id: 3,
          name: '孙子文化园',
          description: '体验孙子兵法文化，了解中国古代军事智慧的主题公园',
          location: '东营市广饶县',
          type: 'culture',
          tag: '4A景区',
          views: 5890,
          image: 'http://www.xinhuanet.com/2021-06/09/1127547819_16232418196451n.jpeg',
          packages: [
            {
              id: 301,
              name: '标准门票',
              price: 60,
              description: '含景区内所有景点参观'
            },
            {
              id: 302,
              name: '兵法文化体验套餐',
              price: 120,
              description: '含门票、专业讲解、兵法体验活动',
              activity: '兵法体验互动活动'
            },
            {
              id: 303,
              name: '文化深度两日游',
              price: 420,
              description: '含门票、住宿、餐饮、导游',
              hotel: '孙子文化主题酒店',
              restaurant: '兵圣宴餐厅',
              activity: '兵法讲座+沙盘推演+古代兵器体验'
            }
          ]
        },
        {
          id: 4,
          name: '黄河三角洲国家级自然保护区',
          description: '中国最年轻的湿地生态系统，珍稀鸟类栖息地',
          location: '东营市河口区',
          type: 'eco',
          tag: '国家级保护区',
          views: 9876,
          image: '/assets/attraction-4.jpg',
          packages: [
            {
              id: 401,
              name: '生态参观票',
              price: 100,
              description: '含保护区参观路线'
            },
            {
              id: 402,
              name: '科研考察套餐',
              price: 280,
              description: '含门票、专业生态讲解、深入考察',
              activity: '生态科普讲解'
            },
            {
              id: 403,
              name: '生态研学两日游',
              price: 680,
              description: '含门票、住宿、餐饮、专业向导',
              hotel: '三角洲生态酒店',
              restaurant: '湿地风味餐厅',
              activity: '生态讲座+野外考察+鸟类观测'
            }
          ]
        },
        {
          id: 5,
          name: '红海滩生态旅游区',
          description: '每年秋季，碱蓬草变红，形成壮观的"红地毯"景观',
          location: '东营市河口区',
          type: 'attraction',
          tag: '网红打卡地',
          views: 15678,
          image: '/assets/attraction-5.jpg',
          packages: [
            {
              id: 501,
              name: '标准门票',
              price: 80,
              description: '含景区内所有景点参观'
            },
            {
              id: 502,
              name: '摄影专线套餐',
              price: 180,
              description: '含门票、摄影向导、最佳拍摄点',
              activity: '摄影向导服务'
            },
            {
              id: 503,
              name: '红海滩两日游',
              price: 520,
              description: '含门票、住宿、餐饮、导游',
              hotel: '红海滩度假酒店',
              restaurant: '海鲜风味餐厅',
              activity: '日出日落摄影+滩涂徒步+渔家体验'
            }
          ]
        },
        {
          id: 6,
          name: '黄河文化旅游线路',
          description: '探索黄河文化，体验黄河沿岸的历史与自然风光',
          location: '东营市多区域',
          type: 'route',
          tag: '精品线路',
          views: 7890,
          image: '/assets/route-1.jpg',
          packages: [
            {
              id: 601,
              name: '一日游',
              price: 298,
              description: '黄河入海口-湿地公园-渔家体验',
              restaurant: '渔家风味餐',
              activity: '黄河文化讲解+渔家体验'
            },
            {
              id: 602,
              name: '两日游',
              price: 598,
              description: '黄河入海口-湿地公园-孙子文化园-渔家体验',
              hotel: '黄河文化主题酒店',
              restaurant: '黄河风味餐厅',
              activity: '黄河文化讲解+渔家体验+兵法体验'
            },
            {
              id: 603,
              name: '三日游',
              price: 898,
              description: '全面体验黄河文化和东营自然风光',
              hotel: '五星级东营大酒店',
              restaurant: '特色美食餐厅',
              activity: '黄河文化+湿地生态+红海滩+渔家体验'
            }
          ]
        },
        {
          id: 7,
          name: '黄河口生态度假酒店',
          description: '位于黄河口生态旅游区内的五星级度假酒店，提供豪华舒适的住宿体验',
          location: '东营市河口区',
          type: 'hotel',
          tag: '五星级',
          rating: 4.8,
          views: 12580,
          image: '/assets/hotel-1.jpg',
          checkInTime: '14:00',
          checkOutTime: '12:00',
          rooms: [
            {
              id: 701,
              name: '豪华大床房',
              price: 680,
              description: '45平米，1.8米大床，河景房'
            },
            {
              id: 702,
              name: '豪华双床房',
              price: 680,
              description: '45平米，2张1.2米床，河景房'
            },
            {
              id: 703,
              name: '行政套房',
              price: 1280,
              description: '80平米，1.8米大床，客厅，河景房'
            }
          ]
        },
        {
          id: 8,
          name: '天鹅湖度假酒店',
          description: '毗邻天鹅湖景区的四星级度假酒店，提供优雅舒适的住宿环境',
          location: '东营市东营区',
          type: 'hotel',
          tag: '四星级',
          rating: 4.5,
          views: 8654,
          image: '/assets/hotel-2.jpg',
          checkInTime: '14:00',
          checkOutTime: '12:00',
          rooms: [
            {
              id: 801,
              name: '标准大床房',
              price: 480,
              description: '35平米，1.8米大床，湖景房'
            },
            {
              id: 802,
              name: '标准双床房',
              price: 480,
              description: '35平米，2张1.2米床，湖景房'
            },
            {
              id: 803,
              name: '豪华套房',
              price: 880,
              description: '60平米，1.8米大床，客厅，湖景房'
            }
          ]
        },
        {
          id: 9,
          name: '孙子文化主题酒店',
          description: '融合孙子兵法文化元素的特色酒店，提供独特的文化体验',
          location: '东营市广饶县',
          type: 'hotel',
          tag: '四星级',
          rating: 4.6,
          views: 5890,
          image: '/assets/hotel-3.jpg',
          checkInTime: '14:00',
          checkOutTime: '12:00',
          rooms: [
            {
              id: 901,
              name: '文化主题大床房',
              price: 520,
              description: '40平米，1.8米大床，文化主题装饰'
            },
            {
              id: 902,
              name: '文化主题双床房',
              price: 520,
              description: '40平米，2张1.2米床，文化主题装饰'
            },
            {
              id: 903,
              name: '兵法主题套房',
              price: 980,
              description: '70平米，1.8米大床，客厅，兵法主题装饰'
            }
          ]
        }
      ],

      // 订单数据
      orders: [],

      // 筛选后的项目和订单
      filteredProjects: [],
      filteredOrders: [],
      filteredHotels: [],

      // 旅游项目搜索表单
      projectSearchForm: {
        name: '',
      },

      // 旅游项目数据
      tourProjects: [],
      filteredTourProjects: [],

      // 当前页码
      currentPage: 1,
      hotelCurrentPage: 1
    };
  },
  computed: {
    // 计算总价
    totalPrice() {
      const project = this.projects.find(p => p.id === this.bookingForm.projectId);
      if (!project) return '0.00';

      const pkg = project.packages.find(p => p.id === this.bookingForm.packageId);
      return pkg ? (pkg.price * this.bookingForm.peopleCount).toFixed(2) : '0.00';
    },
    hotelTotalPrice() {
      const hotel = this.filteredHotels.find(h => h.id === this.hotelBookingForm.hotelId);
      if (!hotel) return '0.00';

      const room = hotel.rooms.find(r => r.id === this.hotelBookingForm.roomId);
      return room ? (room.price * this.hotelBookingForm.roomCount).toFixed(2) : '0.00';
    }
  },
  created() {
    // 初始化筛选后的项目和订单
    this.getSpotList();
    this.getOrderList();
    this.getHotelList();
    this.getTourProjectList();

    // 从路由获取项目和套餐
    if (this.$route.query.projectId && this.$route.query.packageId) {
      const projectId = parseInt(this.$route.query.projectId);
      const packageId = parseInt(this.$route.query.packageId);

      const project = this.projects.find(p => p.id === projectId);
      if (project) {
        const pkg = project.packages.find(p => p.id === packageId);
        if (pkg) {
          this.openBookingDialog(project, pkg);
        }
      }
    }
  },
  methods: {
    realUrl(path) {
      return process.env.VUE_APP_BASE_API + path;
    },
    // 获取景点列表
    getSpotList() {
      listSpot().then(response => {
        this.filteredProjects = response.rows.map(spot => ({
          id: spot.spotId,
          name: spot.name,
          description: spot.description,
          location: spot.location,
          type: spot.type || 'attraction',
          tag: spot.tag || '景点',
          views: spot.views || 0,
          image: spot.imageUrl || '/assets/default-spot.jpg',
          packages: [
            {
              id: spot.spotId * 100 + 1,
              name: '标准门票',
              price: spot.price,
              description: spot.description
            }
          ]
        }));
      }).catch(err=> {});
    },

    // 搜索景点
    searchSpot() {
      const query = {
        name: this.searchForm.name
      };
      listSpot(query).then(response => {
        this.filteredProjects = response.rows.map(spot => ({
          id: spot.spotId,
          name: spot.name,
          description: spot.description,
          location: spot.location,
          type: spot.type || 'attraction',
          tag: spot.tag || '景点',
          views: spot.views || 0,
          image: spot.imageUrl || '/assets/default-spot.jpg',
          packages: [
            {
              id: spot.spotId * 100 + 1,
              name: '标准门票',
              price: spot.price,
              description: spot.description
            }
          ]
        }));
        this.currentPage = 1;
      });
    },

    // 重置景点搜索
    resetSearch() {
      this.searchForm = {
        name: '',
        type: ''
      };
      this.getSpotList();
      this.currentPage = 1;
    },

    // 打开预订对话框
    openBookingDialog(project, pkg) {
      getSpot(project.id).then(response => {
        const spotData = response.data;
        this.bookingForm = {
          projectId: spotData.spotId,
          projectName: spotData.name,
          packageId: pkg.id,
          packageName: pkg.name,
          hotel: pkg.hotel || '',
          restaurant: pkg.restaurant || '',
          activity: pkg.activity || '',
          date: '',
          peopleCount: 1,
          paymentMethod: 'wechat',
          totalPrice: pkg.price
        };
        this.dialogVisible = true;
      });
    },

    // 确认预订
    confirmBooking(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          // 创建订单对象
          const orderData = {
            userId: this.$store.state.user.userId,
            spotId: this.bookingForm.projectId,
            status: '已支付',
            peopleCount: this.bookingForm.peopleCount,
            totalPrice: this.bookingForm.totalPrice,
            remarks: JSON.stringify({
              projectName: this.bookingForm.projectName,
              packageName: this.bookingForm.packageName,
              hotel: this.bookingForm.hotel,
              restaurant: this.bookingForm.restaurant,
              activity: this.bookingForm.activity
            })
          };

          // 调用后端 API 保存订单
          addOrder(orderData).then(response => {
            if (response.code === 200) {
              this.$message.success('订单创建并支付成功');
              this.dialogVisible = false;
              this.$refs[formName].resetFields();

              // 询问是否评价
              this.$confirm('支付成功！是否现在评价？', '提示', {
                confirmButtonText: '去评价',
                cancelButtonText: '稍后',
                type: 'success'
              }).then(() => {
                this.goToReview({
                  orderId: response.data.orderId,
                  spotId: this.bookingForm.projectId
                });
              }).catch(() => {});
            }
          }).catch(error => {
            this.$message.error('订单创建失败：' + error.message);
          });
        }
      });
    },
    // 获取订单列表
    getOrderList() {
      listOrder().then(response => {
        this.orders = response.rows.map(order => ({
          orderId: order.orderId,
          spotId: order.spotId,
          hotelId: order.hotelId,
          projectId: order.projectId,
          projectName: order.remarks ? JSON.parse(order.remarks).projectName : '',
          createTime: this.formatDate(order.createdAt),
          peopleCount: order.peopleCount,
          totalPrice: order.totalPrice,
          status: order.status,
          details: order.remarks ? JSON.parse(order.remarks) : {}
        }));
        this.filteredOrders = [...this.orders];
      }).catch(err=> {});
    },

    // 搜索订单
    searchOrders() {
      const query = {
        status: this.orderSearchForm.status,
        createTime: this.orderSearchForm.dateRange ? this.orderSearchForm.dateRange[0] : null,
        endTime: this.orderSearchForm.dateRange ? this.orderSearchForm.dateRange[1] : null
      };
      listOrder(query).then(response => {
        this.orders = response.rows.map(order => ({
          orderId: order.orderId,
          spotId: order.spotId,
          hotelId: order.hotelId,
          projectId: order.projectId,
          projectName: order.remarks ? JSON.parse(order.remarks).projectName : '',
          createTime: this.formatDate(order.createdAt),
          peopleCount: order.peopleCount,
          totalPrice: order.totalPrice,
          status: order.status,
          details: order.remarks ? JSON.parse(order.remarks) : {}
        }));
        this.filteredOrders = [...this.orders];
      });
    },

    // 重置订单搜索
    resetOrderSearch() {
      this.orderSearchForm = {
        status: '',
        dateRange: []
      };
      this.filteredOrders = [...this.orders];
    },

    // 查看订单详情
    viewOrderDetails(order) {
      this.selectedOrder = order;
      this.orderDetailVisible = true;
    },

    // 取消订单
    cancelOrder(order) {
      this.$confirm('确定要取消该订单吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const orderData = {
          orderId: order.orderId,
          status: '已取消'
        };
        updateOrder(orderData).then(response => {
          if (response.code === 200) {
            this.$message.success('订单已取消');
            this.getOrderList();
          }
        }).catch(error => {
          this.$message.error('取消订单失败：' + error.message);
        });
      }).catch(() => {
      });
    },

    // 前往评价页面
    goToReview(order) {
      let projectId = order.spotId || order.hotelId || order.projectId;
      this.$router.push({
        path: '/reviews',
        query: {
          projectId: projectId,
          orderId: order.orderId
        }
      });
    },

    // 处理分页变化
    handlePageChange(page) {
      this.currentPage = page;
    },

    // 获取订单状态对应的类型
    getStatusType(status) {
      switch (status) {
        case '已支付':
          return 'success';
        case '待支付':
          return 'warning';
        case '已取消':
          return 'info';
        case '已完成':
          return 'primary';
        default:
          return '';
      }
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return '';

      if (typeof date === 'string') return date;

      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');

      return `${year}-${month}-${day}`;
    },

    // 获取酒店列表
    getHotelList() {
      listHotel().then(response => {
        this.filteredHotels = response.rows.map(hotel => ({
          id: hotel.hotelId,
          name: hotel.name,
          description: hotel.description,
          location: hotel.location,
          type: 'hotel',
          tag: hotel.tag || '酒店',
          rating: hotel.rating || 4.5,
          views: hotel.views || 0,
          image: hotel.imageUrl || '/assets/default-hotel.jpg',
          checkInTime: hotel.checkInTime || '14:00',
          checkOutTime: hotel.checkOutTime || '12:00',
          rooms: [
            {
              id: hotel.hotelId * 100 + 1,
              name: '标准房',
              price: hotel.price,
              description: hotel.description
            }
          ]
        }));
      }).catch(err=> {});
    },

    // 搜索酒店
    searchHotels() {
      const query = {
        name: this.hotelSearchForm.name,
        priceRange: this.hotelSearchForm.priceRange
      };
      listHotel(query).then(response => {
        this.filteredHotels = response.rows.map(hotel => ({
          id: hotel.hotelId,
          name: hotel.name,
          description: hotel.description,
          location: hotel.location,
          type: 'hotel',
          tag: hotel.tag || '酒店',
          rating: hotel.rating || 4.5,
          views: hotel.views || 0,
          image: hotel.imageUrl || '/assets/default-hotel.jpg',
          checkInTime: hotel.checkInTime || '14:00',
          checkOutTime: hotel.checkOutTime || '12:00',
          rooms: [
            {
              id: hotel.hotelId * 100 + 1,
              name: '标准房',
              price: hotel.price,
              description: hotel.description
            }
          ]
        }));
        this.hotelCurrentPage = 1;
      });
    },

    // 重置酒店搜索
    resetHotelSearch() {
      this.hotelSearchForm = {
        name: '',
        priceRange: ''
      };
      this.getHotelList();
      this.hotelCurrentPage = 1;
    },

    // 打开酒店预订对话框
    openHotelBookingDialog(hotel, room) {
      getHotel(hotel.id).then(response => {
        const hotelData = response.data;
        this.hotelBookingForm = {
          hotelId: hotelData.hotelId,
          hotelName: hotelData.name,
          roomId: room.id,
          roomType: room.name,
          checkInDate: '',
          checkOutDate: '',
          checkInTime: hotelData.checkInTime || '14:00',
          checkOutTime: hotelData.checkOutTime || '12:00',
          roomCount: 1,
          paymentMethod: 'wechat'
        };
        this.hotelDialogVisible = true;
      });
    },

    // 确认酒店预订
    confirmHotelBooking(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          // 创建订单对象
          const orderData = {
            userId: this.$store.state.user.userId,
            hotelId: this.hotelBookingForm.hotelId,
            status: '已支付',
            peopleCount: this.hotelBookingForm.roomCount,
            totalPrice: this.hotelTotalPrice,
            remarks: JSON.stringify({
              projectName: this.hotelBookingForm.hotelName,
              packageName: this.hotelBookingForm.roomType,
              checkInDate: this.formatDate(this.hotelBookingForm.checkInDate),
              checkOutDate: this.formatDate(this.hotelBookingForm.checkOutDate),
              checkInTime: this.hotelBookingForm.checkInTime,
              checkOutTime: this.hotelBookingForm.checkOutTime
            })
          };

          // 调用后端 API 保存订单
          addOrder(orderData).then(response => {
            if (response.code === 200) {
              this.$message.success('订单创建并支付成功');
              this.hotelDialogVisible = false;
              this.$refs[formName].resetFields();

              // 询问是否评价
              this.$confirm('支付成功！是否现在评价？', '提示', {
                confirmButtonText: '去评价',
                cancelButtonText: '稍后',
                type: 'success'
              }).then(() => {
                this.goToReview({
                  orderId: response.data.orderId,
                  hotelId: this.hotelBookingForm.hotelId
                });
              }).catch(() => {});
            }
          }).catch(error => {
            this.$message.error('订单创建失败：' + error.message);
          });
        }
      });
    },

    // 处理酒店分页变化
    handleHotelPageChange(page) {
      this.hotelCurrentPage = page;
    },

    // 处理分类变化
    handleCategoryChange(tab) {
      this.activeCategory = tab.name;
    },

    // 获取旅游项目列表
    getTourProjectList() {
      listProject().then(response => {
        this.tourProjects = response.rows.map(project => ({
          id: project.projectId,
          name: project.name,
          description: project.description,
          location: project.location,
          tag: project.tag || '旅游项目',
          views: project.views || 0,
          image: project.imgUrl || '/assets/default-project.jpg',
          packages: [
            {
              id: project.projectId * 100 + 1,
              name: '标准套餐',
              price: project.price,
              description: project.description
            }
          ]
        }));
        this.filteredTourProjects = [...this.tourProjects];
      }).catch(err=> {});
    },

    // 搜索旅游项目
    searchTourProjects() {
      const query = {
        name: this.projectSearchForm.name
      };
      listProject(query).then(response => {
        this.tourProjects = response.rows.map(project => ({
          id: project.projectId,
          name: project.name,
          description: project.description,
          location: project.location,
          tag: project.tag || '旅游项目',
          views: project.views || 0,
          image: project.imgUrl || '/assets/default-project.jpg',
          packages: [
            {
              id: project.projectId * 100 + 1,
              name: '标准套餐',
              price: project.price,
              description: project.description
            }
          ]
        }));
        this.filteredTourProjects = [...this.tourProjects];
      });
    },

    // 重置旅游项目搜索
    resetTourProjectSearch() {
      this.projectSearchForm = {
        name: ''
      };
      this.getTourProjectList();
    },

    // 打开旅游项目预订对话框
    openTourProjectBookingDialog(project, pkg) {
      getProject(project.id).then(response => {
        const projectData = response.data;
        this.tourProjectBookingForm = {
          projectId: projectData.projectId,
          projectName: projectData.name,
          packageId: pkg.id,
          packageName: pkg.name,
          date: '',
          peopleCount: 1,
          paymentMethod: 'wechat',
          totalPrice: pkg.price
        };
        this.tourProjectDialogVisible = true;
      });
    },

    // 确认旅游项目预订
    confirmTourProjectBooking(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          // 创建订单对象
          const orderData = {
            userId: this.$store.state.user.userId,
            productId: this.tourProjectBookingForm.projectId,
            status: '已支付',
            peopleCount: this.tourProjectBookingForm.peopleCount,
            totalPrice: this.tourProjectBookingForm.totalPrice,
            remarks: JSON.stringify({
              projectName: this.tourProjectBookingForm.projectName,
              packageName: this.tourProjectBookingForm.packageName
            })
          };

          // 调用后端 API 保存订单
          addOrder(orderData).then(response => {
            if (response.code === 200) {
              this.$message.success('订单创建并支付成功');
              this.tourProjectDialogVisible = false;
              this.$refs[formName].resetFields();

              // 询问是否评价
              this.$confirm('支付成功！是否现在评价？', '提示', {
                confirmButtonText: '去评价',
                cancelButtonText: '稍后',
                type: 'success'
              }).then(() => {
                this.goToReview({
                  orderId: response.data.orderId,
                  projectId: this.tourProjectBookingForm.projectId
                });
              }).catch(() => {});
            }
          }).catch(error => {
            this.$message.error('订单创建失败：' + error.message);
          });
        }
      });
    },

    // 格式化日期时间
    formatDateTime(date, time) {
      if (!date) return '';
      // 如果是ISO字符串，直接格式化
      let d = new Date(date);
      if (!isNaN(d.getTime())) {
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        const hour = String(d.getHours()).padStart(2, '0');
        const min = String(d.getMinutes()).padStart(2, '0');
        const sec = String(d.getSeconds()).padStart(2, '0');
        // 如果时间为00:00:00且有time参数，拼接time
        if (hour === '00' && min === '00' && sec === '00' && time) {
          return `${year}-${month}-${day} ${time.length === 5 ? time+':00' : time}`;
        }
        return `${year}-${month}-${day} ${hour}:${min}:${sec}`;
      }
      // 否则直接返回字符串
      return date;
    },
  }
};
</script>

<style scoped>
.booking {
  padding: 40px 20px;
  max-width: 1400px;
  margin: 0 auto;
  background: #f8fafc;
}

/* 标题样式 */
h2 {
  font-size: 24px;
  font-weight: 600;
  color: #1f2a44;
  margin-bottom: 30px;
  text-align: center;
}

h4 {
  margin: 0 0 12px;
  font-size: 18px;
  font-weight: 600;
  color: #1f2a44;
}

/* 搜索框 */
.search-container {
  margin-bottom: 40px;
  display: flex;
  justify-content: center;
}

.search-input {
  width: 300px;
  border-radius: 8px;
  overflow: hidden;
}

.search-date {
  width: 360px;
}

.search-input >>> .el-input__inner {
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  height: 40px;
}

.el-button {
  border-radius: 8px;
  height: 40px;
  padding: 0 20px;
}

.el-button--primary {
  background: #2563eb;
  border-color: #2563eb;
}

/* 特色列表 */
.specialty-list {
  margin-bottom: 40px;
}

.specialty-card {
  background: white;
  border: none;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
  margin-bottom: 24px;
}

.specialty-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.image-container, .hotel-image-container {
  position: relative;
  overflow: hidden;
}

.specialty-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.specialty-card:hover .specialty-image {
  transform: scale(1.05);
}

.specialty-content {
  padding: 20px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.specialty-desc {
  font-size: 14px;
  color: #64748b;
  margin: 0 0 16px;
  line-height: 1.6;
  flex-grow: 1;
}

.specialty-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  font-size: 13px;
  color: #64748b;
}

.specialty-type-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 13px;
  color: #2563eb;
  background: #eff6ff;
  padding: 4px 10px;
  border-radius: 12px;
}

/* 酒店卡片 */
.hotel-card {
  width: 100%;
}

.hotel-content {
  display: flex;
  gap: 20px;
}

.hotel-image-container {
  width: 300px;
  height: 200px;
  flex-shrink: 0;
}

.hotel-info {
  flex: 1;
  display: flex;
  flex-direction: column;
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

/* 订单管理 */
.order-management {
  margin-top: 60px;
}

.order-table-card {
  border-radius: 12px;
  margin-bottom: 40px;
}

.order-table {
  margin: 20px 0;
}

.price {
  color: #ef4444;
  font-weight: 600;
}

.status-tag {
  font-size: 13px;
  border-radius: 12px;
  padding: 2px 8px;
  font-weight: 500;
}

.action-button {
  color: #2563eb;
}

.package-table {
  margin-top: 10px;
  border-radius: 8px;
  overflow: hidden;
}

/* 标签页样式 */
.category-tabs >>> .el-tabs__item {
  font-size: 16px;
  font-weight: 500;
  color: #64748b;
  height: 50px;
  line-height: 50px;
}

.category-tabs >>> .el-tabs__item.is-active {
  color: #2563eb;
  font-weight: 600;
}

.category-tabs >>> .el-tabs__active-bar {
  background-color: #2563eb;
  height: 3px;
}

/* 响应式调整 */
@media (max-width: 992px) {
  .booking {
    padding: 30px 15px;
  }

  .hotel-content {
    flex-direction: column;
  }

  .hotel-image-container {
    width: 100%;
  }

  .specialty-image {
    height: 180px;
  }
}

@media (max-width: 768px) {
  .search-input,
  .search-date,
  .el-select {
    width: 100%;
  }

  .specialty-image {
    height: 160px;
  }

  .specialty-content {
    padding: 16px;
  }

  .specialty-content h4 {
    font-size: 16px;
  }

  .el-form--inline .el-form-item {
    display: block;
    margin-right: 0;
    width: 100%;
  }
}

@media (max-width: 576px) {
  .booking {
    padding: 20px 10px;
  }

  .search-container {
    margin-bottom: 24px;
  }

  .specialty-image {
    height: 200px;
  }

  h2 {
    font-size: 20px;
    margin-bottom: 20px;
  }

  .specialty-desc {
    font-size: 13px;
  }
}
</style>