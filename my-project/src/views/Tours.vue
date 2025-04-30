<template>
  <div class="tours">
    <!-- 搜索框 -->
    <div class="search-container">
      <el-input
          v-model="searchQuery"
          placeholder="搜索旅游项目"
          class="search-input"
          clearable
          @keyup.enter.native="handleSearch"
      >
        <el-button slot="append" icon="el-icon-search" @click="handleSearch" />
      </el-input>
    </div>

    <!-- 最近活动 -->
    <el-card class="recent-activities" shadow="hover">
      <h3 slot="header">最近活动</h3>
      <el-carousel height="180px" :interval="5000">
        <el-carousel-item v-for="activity in recentActivities" :key="activity.id">
          <div class="activity-item" @click="goToActivity(activity.id)">
            <img :src="activity.image" alt="activity image" class="activity-image" />
            <p>{{ activity.title }} - {{ activity.date }}</p>
          </div>
        </el-carousel-item>
      </el-carousel>
    </el-card>

    <!-- 旅游项目列表 -->
    <el-row :gutter="20" class="tour-list">
      <el-col :span="24" v-for="tour in paginatedTours" :key="tour.id">
        <el-card class="tour-card" shadow="hover">
          <el-row :gutter="15">
            <el-col :span="6">
              <img :src="tour.image" alt="tour image" class="tour-image" />
            </el-col>
            <el-col :span="12">
              <h4>{{ tour.title }}</h4>
              <p class="tour-desc">{{ tour.description }}</p>
              <p class="tour-price"><strong>价格：</strong>¥{{ tour.price }}</p>
              <el-select
                  v-model="tour.selectedPackage"
                  placeholder="选择套餐"
                  size="small"
                  class="package-select"
              >
                <el-option
                    v-for="pkg in tour.packages"
                    :key="pkg.id"
                    :label="pkg.name"
                    :value="pkg.id"
                />
              </el-select>
              <el-collapse accordion>
                <el-collapse-item title="查看评论" name="comments">
                  <div v-for="comment in tour.comments" :key="comment.id" class="comment">
                    <span>{{ comment.user }}:</span> {{ comment.text }}
                  </div>
                </el-collapse-item>
              </el-collapse>
            </el-col>
            <el-col :span="6" class="action-col">
              <el-button type="primary" size="medium" @click="bookTour(tour)">立即预约</el-button>
              <el-button type="success" size="medium" plain @click="contactCustomerService">联系客服</el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
          layout="prev, pager, next"
          :total="filteredTours.length"
          :page-size="pageSize"
          :current-page.sync="currentPage"
          @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'Tours',
  data() {
    return {
      searchQuery: '',
      currentPage: 1,
      pageSize: 3,
      tours: [
        {
          id: 1,
          title: '黄河口湿地观鸟之旅',
          description: '探索黄河口三角洲独特的湿地生态，观赏珍稀鸟类。',
          price: 200,
          image: 'https://via.placeholder.com/300x200?text=湿地观鸟',
          packages: [
            { id: 1, name: '单人票' },
            { id: 2, name: '含酒店套餐 (+500元)' },
            { id: 3, name: '含餐饮套餐 (+300元)' }
          ],
          selectedPackage: null,
          comments: [
            { id: 1, user: '张三', text: '鸟类种类丰富，导游讲解很专业！' },
            { id: 2, user: '李四', text: '景色很美，值得一游。' }
          ]
        },
        {
          id: 2,
          title: '黄河入海口游船体验',
          description: '乘坐游船，近距离感受黄河入海的壮观景象。',
          price: 150,
          image: 'https://via.placeholder.com/300x200?text=游船体验',
          packages: [
            { id: 1, name: '单人票' },
            { id: 2, name: '含午餐套餐 (+100元)' }
          ],
          selectedPackage: null,
          comments: [
            { id: 1, user: '王五', text: '游船很平稳，风景震撼！' }
          ]
        },
        {
          id: 3,
          title: '生态摄影之旅',
          description: '适合摄影爱好者，捕捉三角洲自然美景。',
          price: 300,
          image: 'https://via.placeholder.com/300x200?text=摄影之旅',
          packages: [
            { id: 1, name: '单人票' },
            { id: 2, name: '含专业指导套餐 (+200元)' }
          ],
          selectedPackage: null,
          comments: [
            { id: 1, user: '赵六', text: '拍到了很多好照片，推荐！' }
          ]
        }
      ],
      recentActivities: [
        { id: 1, title: '湿地保护日', date: '2025-03-15', image: 'https://via.placeholder.com/600x200?text=活动1' },
        { id: 2, title: '春季观鸟节', date: '2025-04-01', image: 'https://via.placeholder.com/600x200?text=活动2' }
      ]
    }
  },
  computed: {
    filteredTours() {
      if (!this.searchQuery) return this.tours
      return this.tours.filter(tour =>
          tour.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          tour.description.toLowerCase().includes(this.searchQuery.toLowerCase())
      )
    },
    paginatedTours() {
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return this.filteredTours.slice(start, end)
    }
  },
  methods: {
    handleSearch() {
      this.currentPage = 1
      this.$message.success(`搜索关键词：${this.searchQuery}`)
    },
    handlePageChange(page) {
      this.currentPage = page
    },
    bookTour(tour) {
      const pkg = tour.packages.find(p => p.id === tour.selectedPackage) || tour.packages[0]
      this.$message.success(`已预约：${tour.title} - ${pkg.name}`)
    },
    contactCustomerService() {
      this.$message.info('正在连接客服...')
    },
    goToActivity(activityId) {
      this.$message.info(`跳转到活动详情：${activityId}`)
    }
  }
}
</script>

<style scoped>
.tours {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

/* 搜索框 */
.search-container {
  text-align: center;
  margin-bottom: 25px;
}
.search-input {
  width: 60%;
  max-width: 500px;
}

/* 最近活动 */
.recent-activities {
  margin-bottom: 30px;
  border-radius: 8px;
}
.recent-activities h3 {
  margin: 0;
}
.activity-item {
  text-align: center;
  cursor: pointer;
}
.activity-image {
  width: 100%;
  height: 130px;
  object-fit: cover;
  border-radius: 4px;
}
.activity-item p {
  margin: 10px 0 0;
  font-size: 14px;
  color: #606266;
}

/* 旅游项目列表 */
.tour-list {
  margin-bottom: 30px;
}
.tour-card {
  margin-bottom: 20px;
  border-radius: 8px;
  transition: all 0.3s;
}
.tour-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
.tour-image {
  width: 100%;
  height: 140px;
  object-fit: cover;
  border-radius: 4px;
}
.tour-desc {
  font-size: 14px;
  color: #606266;
  margin: 10px 0;
  line-height: 1.5;
}
.tour-price {
  font-size: 16px;
  color: #303133;
  margin: 10px 0;
}
.package-select {
  width: 200px;
  margin: 10px 0;
}
.el-collapse {
  margin-top: 10px;
}
.comment {
  font-size: 13px;
  color: #909399;
  margin: 5px 0;
}
.comment span {
  font-weight: bold;
  color: #606266;
}
.action-col {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

/* 分页 */
.pagination-container {
  text-align: center;
  padding: 20px 0;
}
</style>