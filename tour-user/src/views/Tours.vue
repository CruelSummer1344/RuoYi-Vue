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
    <el-card class="recent-activities" shadow="never">
      <h3 slot="header">最近活动</h3>
      <el-carousel :interval="5000" height="280px">
        <el-carousel-item v-for="activity in recentActivities" :key="activity.id">
          <div class="activity-item" @click="goToActivity(activity.id)">
            <img :src="activity.image" alt="activity image" class="activity-image"/>
            <p>{{ activity.title }} - {{ activity.date }}</p>
          </div>
        </el-carousel-item>
      </el-carousel>
    </el-card>

    <!-- 旅游项目列表 -->
    <el-row :gutter="24" class="tour-list">
      <el-col v-for="tour in paginatedTours" :key="tour.id" :lg="12" :md="12" :sm="24" :xs="24">
        <el-card class="tour-card" shadow="never">
          <el-row :gutter="20">
            <el-col :lg="8" :md="8" :sm="8" :xs="24">
              <div class="image-container">
                <el-image :src="realUrl(tour)" alt="tour image" class="specialty-image"/>
              </div>
            </el-col>
            <el-col :lg="16" :md="16" :sm="16" :xs="24">
              <div class="specialty-content">
                <h4>{{ tour.title }}</h4>
                <p class="specialty-desc">{{ tour.description }}</p>
                <p class="tour-price"><strong>价格：</strong>¥{{ tour.price }}</p>
                <el-select
                    v-model="tour.selectedPackage"
                    class="package-select"
                    placeholder="选择套餐"
                    size="small"
                >
                  <el-option
                      v-for="pkg in tour.packages"
                      :key="pkg.id"
                      :label="pkg.name"
                      :value="pkg.id"
                  />
                </el-select>

                <div class="action-buttons">
                  <el-button size="small" type="primary" @click="bookTour(tour)">立即预约</el-button>
                  <el-button plain size="small" @click="contactCustomerService">联系客服</el-button>
                </div>

                <el-collapse accordion class="tour-comments">
                  <el-collapse-item name="comments" title="查看评论">
                    <div v-for="comment in tour.comments" :key="comment.id" class="comment">
                      <span>{{ comment.user }}:</span> {{ comment.text }}
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
          :current-page.sync="currentPage"
          :page-size="pageSize"
          :total="filteredTours.length"
          background
          layout="prev, pager, next"
          @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script>
import {listSpot, getSpot} from "@/api/spot/spot";

export default {
  name: 'Tours',
  data() {
    return {
      searchQuery: '',
      currentPage: 1,
      pageSize: 4,
      tours: [],
      recentActivities: [
        {
          id: 1,
          title: '湿地保护日',
          date: '2025-03-15',
          image: 'https://ts1.tc.mm.bing.net/th/id/R-C.0dcae2d2ced5b5757cc9d8d76783c438?rik=QQvMRj2YBrIToA&riu=http%3a%2f%2fwenhui.whb.cn%2fu%2fcms%2fwww%2f202104%2f24223449s69j.jpg&ehk=IcFbW%2fUU3tfgVkFdWhQd8Flt6QNi7XWBwM9R0Xnoia8%3d&risl=&pid=ImgRaw&r=0?text=活动1'
        },
        {
          id: 2,
          title: '春季观鸟节',
          date: '2025-04-01',
          image: 'https://ts1.tc.mm.bing.net/th/id/R-C.8d2e190b5a9c72c77dd6832bfc7c8323?rik=ud1eCkygYn2xlg&riu=http%3a%2f%2fnews.southcn.com%2fzl%2fyc%2fcs%2fcontent%2fimages%2fattachement%2fjpg%2fsite4%2f20200120%2f6c4b908e2c6a1f8fb25837.jpg&ehk=dmMrT2BE5NzR34WTtEgpusoqyhfbuEfICc3zzdC4JP8%3d&risl=&pid=ImgRaw&r=0?text=活动2'
        }
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
  created() {
    this.getTourList();
  },
  methods: {
    realUrl(tour) {
      return process.env.VUE_APP_BASE_API + tour.image
    },
    // 获取旅游项目列表
    getTourList() {
      listSpot().then(response => {
        this.tours = response.rows.map(spot => ({
          id: spot.spotId,
          title: spot.name,
          description: spot.description,
          price: spot.price,
          image: spot.imageUrl || '/assets/default-spot.jpg',
          packages: [
            {id: 1, name: '标准门票'},
            {id: 2, name: '含讲解套餐 (+120元)'},
            {id: 3, name: '含餐饮套餐 (+300元)'}
          ],
          selectedPackage: null,
          comments: [
            {id: 1, user: '游客', text: '景点很美，值得一游！'}
          ]
        }));
      });
    },

    handleSearch() {
      const query = {
        name: this.searchQuery
      };
      listSpot(query).then(response => {
        this.tours = response.rows.map(spot => ({
          id: spot.spotId,
          title: spot.name,
          description: spot.description,
          price: spot.price,
          image: spot.image || '/assets/default-spot.jpg',
          packages: [
            {id: 1, name: '标准门票'},
            {id: 2, name: '含讲解套餐 (+120元)'},
            {id: 3, name: '含餐饮套餐 (+300元)'}
          ],
          selectedPackage: null,
          comments: [
            {id: 1, user: '游客', text: '景点很美，值得一游！'}
          ]
        }));
        this.currentPage = 1;
        this.$message.success(`搜索关键词：${this.searchQuery}`);
      });
    },

    handlePageChange(page) {
      this.currentPage = page;
    },

    bookTour(tour) {
      getSpot(tour.id).then(response => {
        const spotData = response.data;
        const pkg = tour.packages.find(p => p.id === tour.selectedPackage) || tour.packages[0];
        this.$message.success(`已预约：${spotData.name} - ${pkg.name}`);
        // 跳转到预订页面
        this.$router.push({
          path: '/booking',
          query: {
            projectId: spotData.spotId,
            packageId: pkg.id
          }
        });
      });
    },

    contactCustomerService() {
      this.$message.info('正在连接客服...');
    },

    goToActivity(activityId) {
      this.$message.info(`跳转到活动详情：${activityId}`);
    }
  }
}
</script>

<style scoped>
.tours {
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

.search-input {
  width: 100%;
  max-width: 600px;
  border-radius: 25px;
  overflow: hidden;
}

.search-input >>> .el-input__inner {
  border-radius: 25px 0 0 25px;
  border: 1px solid #e2e8f0;
  height: 46px;
}

.search-input >>> .el-input-group__append {
  border-radius: 0 25px 25px 0;
  background: #2563eb;
  border: none;
}

.search-input >>> .el-input-group__append .el-button {
  color: white;
}

/* 最近活动 */
.recent-activities {
  margin-bottom: 40px;
  border-radius: 12px;
  overflow: hidden;
  border: none;
  transition: box-shadow 0.3s ease;
}

.recent-activities:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.recent-activities >>> .el-card__header {
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.recent-activities h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2a44;
}

.activity-item {
  text-align: center;
  cursor: pointer;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.activity-image {
  width: 100%;
  height: 230px;
  object-fit: cover;
  border-radius: 8px;
  transition: transform 0.3s ease;
}

.el-carousel-item:hover .activity-image {
  transform: scale(1.02);
}

.activity-item p {
  margin: 15px 0 0;
  font-size: 16px;
  font-weight: 500;
  color: #1f2a44;
}

/* 旅游项目列表 */
.tour-list {
  margin-bottom: 40px;
}

.tour-card {
  margin-bottom: 24px;
  border-radius: 12px;
  background: white;
  border: none;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 100%;
}

.tour-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.image-container {
  position: relative;
  overflow: hidden;
  height: 100%;
  min-height: 160px;
  border-radius: 8px;
}

.specialty-image {
  width: 100%;
  height: 100%;
  min-height: 160px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.tour-card:hover .specialty-image {
  transform: scale(1.05);
}

.specialty-content {
  padding: 16px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.specialty-content h4 {
  margin: 0 0 12px;
  font-size: 18px;
  font-weight: 600;
  color: #1f2a44;
}

.specialty-desc {
  font-size: 14px;
  color: #64748b;
  margin: 0 0 16px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.tour-price {
  font-size: 16px;
  color: #1f2a44;
  margin: 0 0 16px;
}

.package-select {
  width: 100%;
  margin-bottom: 16px;
}

.package-select >>> .el-input__inner {
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.action-buttons {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.action-buttons .el-button {
  border-radius: 8px;
}

.action-buttons .el-button--primary {
  background: #2563eb;
  border-color: #2563eb;
}

.tour-comments {
  margin-top: auto;
  border-radius: 8px;
  overflow: hidden;
}

.tour-comments >>> .el-collapse-item__header {
  font-size: 14px;
  color: #2563eb;
  border-bottom: none;
  padding: 8px 0;
}

.tour-comments >>> .el-collapse-item__content {
  padding: 10px 0;
}

.comment {
  font-size: 13px;
  color: #64748b;
  margin: 5px 0;
  padding: 8px 12px;
  background: #f8fafc;
  border-radius: 8px;
}

.comment span {
  font-weight: 600;
  color: #1f2a44;
  margin-right: 4px;
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
  .tours {
    padding: 30px 15px;
  }

  .specialty-image {
    height: 180px;
  }

  .action-buttons {
    flex-direction: column;
    gap: 8px;
  }

  .action-buttons .el-button {
    margin-left: 0 !important;
  }
}

@media (max-width: 768px) {
  .search-input {
    max-width: 90%;
  }

  .activity-image {
    height: 200px;
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
}

@media (max-width: 576px) {
  .tours {
    padding: 20px 10px;
  }

  .search-container {
    margin-bottom: 24px;
  }

  .activity-image {
    height: 180px;
  }

  .specialty-desc {
    font-size: 13px;
  }

  .el-button {
    width: 100%;
  }
}
</style>