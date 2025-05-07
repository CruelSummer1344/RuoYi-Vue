<template>
  <div class="specialties">
    <!-- 搜索框 -->
    <div class="search-container">
      <el-input
          v-model="searchQuery"
          placeholder="搜索周边特色"
          class="search-input"
          clearable
          @keyup.enter.native="handleSearch"
      >
        <el-button slot="append" icon="el-icon-search" @click="handleSearch" />
      </el-input>
    </div>

    <!-- 特色列表 -->
    <el-row :gutter="24" class="specialty-list">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in paginatedSpecialties" :key="item.id">
        <el-card class="specialty-card" shadow="never">
          <div class="image-container">
            <img :src="item.image" alt="specialty image" class="specialty-image" />
          </div>
          <div class="specialty-content">
            <h4>{{ item.title }}</h4>
            <p class="specialty-desc">{{ item.description }}</p>
            <div class="specialty-footer">
              <span class="specialty-type">{{ item.type }}</span>
<!--              <el-button type="text" @click="viewDetails(item)">查看详情</el-button>-->
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 空状态提示 -->
    <el-empty
        v-if="filteredSpecialties.length === 0"
        description="没有找到匹配的特色内容"
        :image-size="200">
    </el-empty>

    <!-- 分页 -->
    <div class="pagination-container" v-if="filteredSpecialties.length > 0">
      <el-pagination
          background
          layout="prev, pager, next"
          :total="filteredSpecialties.length"
          :page-size="pageSize"
          :current-page.sync="currentPage"
          @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'Specialties',
  data() {
    return {
      searchQuery: '',
      currentPage: 1,
      pageSize: 8, // 每页显示8项，四列布局每行4个
      specialties: [
        {
          id: 1,
          title: '黄河口蟹黄汤包',
          description: '选用黄河口新鲜螃蟹，汤汁鲜美，皮薄馅足。',
          type: '美食',
          image: require('@/assets/images/1.jpg')
        },
        {
          id: 2,
          title: '三角洲渔家民宿',
          description: '依水而建，体验渔家生活，环境清幽舒适。',
          type: '民宿',
          image: require('@/assets/images/2.webp')
        },
        {
          id: 3,
          title: '东营盐焗鸡',
          description: '传统工艺制作，肉质鲜嫩，咸香入味。',
          type: '美食',
          image: require('@/assets/images/3.webp')
        },
        {
          id: 4,
          title: '湿地芦苇小屋',
          description: '芦苇搭建的生态民宿，融入自然，适合度假。',
          type: '民宿',
          image: require('@/assets/images/4.webp')
        },
        {
          id: 5,
          title: '黄河鲤鱼宴',
          description: '新鲜黄河鲤鱼烹制，口感鲜美，营养丰富。',
          type: '美食',
          image: require('@/assets/images/5.webp')
        },
        {
          id: 6,
          title: '黄河人家客栈',
          description: '传统建筑风格，提供地道乡村体验。',
          type: '民宿',
          image: require('@/assets/images/6.webp')
        },
        {
          id: 7,
          title: '东营河蟹',
          description: '黄河口特产，肉质鲜美，蟹黄丰富，营养价值高。',
          type: '美食',
          image: require('@/assets/images/7.webp')
        },
        {
          id: 8,
          title: '湿地观景木屋',
          description: '位于湿地保护区边缘，可以近距离观赏湿地风光和鸟类。',
          type: '民宿',
          image: require('@/assets/images/8.webp')
        }
      ]
    }
  },
  computed: {
    filteredSpecialties() {
      if (!this.searchQuery) return this.specialties
      return this.specialties.filter(item =>
          item.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          item.description.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          item.type.toLowerCase().includes(this.searchQuery.toLowerCase())
      )
    },
    paginatedSpecialties() {
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return this.filteredSpecialties.slice(start, end)
    }
  },
  methods: {
    handleSearch() {
      if (this.searchQuery === '') {
        return
      }
      this.currentPage = 1
      this.$message.success(`搜索关键词：${this.searchQuery}`)
    },
    handlePageChange(page) {
      this.currentPage = page
    },
    viewDetails(item) {
      this.$message.info(`查看详情：${item.title}`)
    }
  }
}
</script>

<style scoped>
.specialties {
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
}
.specialty-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}
.image-container {
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
  flex-grow: 1;
}
.specialty-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}
.specialty-type {
  font-size: 13px;
  color: #2563eb;
  background: #eff6ff;
  padding: 4px 10px;
  border-radius: 12px;
}
.specialty-footer .el-button {
  font-size: 14px;
  color: #2563eb;
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
  .specialties {
    padding: 30px 15px;
  }
  .specialty-image {
    height: 180px;
  }
}

@media (max-width: 768px) {
  .search-input {
    max-width: 90%;
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
  .specialties {
    padding: 20px 10px;
  }
  .search-container {
    margin-bottom: 24px;
  }
  .specialty-image {
    height: 200px;
  }
  .specialty-desc {
    font-size: 13px;
  }
}
</style>