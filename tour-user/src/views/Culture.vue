<template>
  <div class="culture">
    <h2>特色文化</h2>
    <!-- 搜索框 -->
    <div class="search-container">
      <el-input
          v-model="searchQuery"
          placeholder="搜索文化信息"
          class="search-input"
          clearable
          @keyup.enter.native="handleSearch"
      >
        <el-button slot="append" icon="el-icon-search" @click="handleSearch" />
      </el-input>
    </div>
    <!-- 文化简介 -->
    <el-card class="culture-intro" shadow="hover">
      <h3>黄河口文化</h3>
      <p>黄河三角洲是中华文明的重要发源地，融合了孙子文化、渔家风情和生态保护理念...</p>
    </el-card>
    <!-- 最近活动 -->
    <el-card class="recent-activities" shadow="hover">
      <h3>最近活动</h3>
      <el-row :gutter="20">
        <el-col :span="12" v-for="activity in filteredActivities" :key="activity.id">
          <el-card class="activity-card" shadow="hover">
            <img :src="activity.image" alt="activity image" class="activity-image" />
            <h4>{{ activity.title }}</h4>
            <p>{{ activity.description }}</p>
            <p><strong>日期：</strong>{{ activity.date }}</p>
            <el-button type="primary" size="small" @click="goToTours(activity)">查看相关项目</el-button>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
    <!-- 美食文化 -->
    <el-card class="food-culture" shadow="hover">
      <h3>美食文化</h3>
      <el-row :gutter="20">
        <el-col :span="8" v-for="food in filteredFoods" :key="food.id">
          <el-card class="food-card" shadow="hover">
            <img :src="food.image" alt="food image" class="food-image" />
            <h4>{{ food.title }}</h4>
            <p>{{ food.description }}</p>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Culture',
  data() {
    return {
      searchQuery: '',
      activities: [
        {
          id: 1,
          title: '湿地保护日',
          description: '参与湿地生态保护，了解黄河三角洲的环保工作。',
          date: '2025-03-15',
          image: 'https://via.placeholder.com/300x200?text=湿地保护'
        },
        {
          id: 2,
          title: '春季观鸟节',
          description: '与专家一起观赏珍稀鸟类，学习鸟类知识。',
          date: '2025-04-01',
          image: 'https://via.placeholder.com/300x200?text=观鸟节'
        }
      ],
      foods: [
        {
          id: 1,
          title: '黄河口蟹黄汤包',
          description: '选用黄河口新鲜螃蟹，汤汁鲜美，皮薄馅足。',
          image: 'https://via.placeholder.com/300x200?text=蟹黄汤包'
        },
        {
          id: 2,
          title: '黄河鲤鱼宴',
          description: '新鲜黄河鲤鱼烹制，口感鲜美，营养丰富。',
          image: 'https://via.placeholder.com/300x200?text=鲤鱼宴'
        }
      ]
    };
  },
  computed: {
    filteredActivities() {
      if (!this.searchQuery) return this.activities;
      return this.activities.filter(item =>
          item.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          item.description.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
    filteredFoods() {
      if (!this.searchQuery) return this.foods;
      return this.foods.filter(item =>
          item.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          item.description.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    }
  },
  methods: {
    handleSearch() {
      this.$message.success(`搜索关键词：${this.searchQuery}`);
    },
    goToTours(activity) {
      this.$router.push('/tours');
    }
  }
};
</script>

<style scoped>
.culture {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}
h2, h3 {
  text-align: center;
  color: #303133;
  margin-bottom: 20px;
}
.search-container {
  text-align: center;
  margin-bottom: 25px;
}
.search-input {
  width: 60%;
  max-width: 500px;
}
.culture-intro, .recent-activities, .food-culture {
  margin-bottom: 30px;
}
.activity-card, .food-card {
  margin-bottom: 20px;
}
.activity-image, .food-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
}
</style>