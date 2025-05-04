<template>
  <div class="search-result-page">
    <h2>搜索结果</h2>
    <div class="search-info">
      <span>类型：{{ typeLabel }}</span>
      <span>关键词：{{ keyword }}</span>
    </div>
    <div class="result-list">
      <div v-if="!keyword">请输入关键词进行搜索。</div>
      <div v-else>
        <div v-if="results.length === 0">暂无相关结果。</div>
        <el-row :gutter="20" v-else>
          <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in results" :key="item.id">
            <el-card class="result-card" shadow="hover">
              <img v-if="item.image" :src="item.image" class="result-image" :alt="item.title || item.name">
              <div class="result-content">
                <h4>{{ item.title || item.name }}</h4>
                <p v-if="item.description">{{ item.description }}</p>
                <p v-if="item.level">等级：{{ item.level }}</p>
                <p v-if="item.price">价格：¥{{ item.price }}</p>
                <p v-if="item.author">作者：{{ item.author }}</p>
                <p v-if="item.duration">时长：{{ item.duration }}</p>
                <p v-if="item.startPoint">出发地：{{ item.startPoint }}</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SearchResult',
  data() {
    return {
      // 首页 mock 数据
      attractionsList: [],
      travelData: [],
      guideList: [],
      hotelList: [],
      results: []
    }
  },
  computed: {
    type() {
      return this.$route.query.type || 'attraction';
    },
    keyword() {
      return this.$route.query.keyword || '';
    },
    typeLabel() {
      switch(this.type) {
        case 'attraction': return '景点';
        case 'route': return '路线';
        case 'guide': return '攻略';
        case 'hotel': return '酒店';
        default: return this.type;
      }
    }
  },
  watch: {
    '$route.query': {
      handler: 'doSearch',
      immediate: true
    }
  },
  methods: {
    doSearch() {
      if (!this.keyword) {
        this.results = [];
        return;
      }
      const kw = this.keyword.trim().toLowerCase();
      if (this.type === 'attraction') {
        this.results = this.attractionsList.filter(item =>
          item.name.toLowerCase().includes(kw)
        );
      } else if (this.type === 'route') {
        this.results = this.travelData.filter(item =>
          item.title.toLowerCase().includes(kw) ||
          (item.description && item.description.toLowerCase().includes(kw))
        );
      } else if (this.type === 'guide') {
        this.results = this.guideList.filter(item =>
          item.title.toLowerCase().includes(kw) ||
          (item.author && item.author.toLowerCase().includes(kw))
        );
      } else if (this.type === 'hotel') {
        this.results = this.hotelList.filter(item =>
          item.name.toLowerCase().includes(kw) ||
          (item.description && item.description.toLowerCase().includes(kw))
        );
      } else {
        this.results = [];
      }
    }
  }
}
</script>

<style scoped>
.search-result-page {
  width: 80%;
  margin: 40px auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  padding: 30px;
}
.search-info {
  margin-bottom: 20px;
  color: #1890ff;
}
.result-list {
  font-size: 16px;
  color: #333;
}
.result-card {
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.result-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
  margin-bottom: 10px;
}
.result-content {
  padding: 10px 0;
}
.result-content h4 {
  font-size: 16px;
  color: #1890ff;
  margin: 0 0 8px 0;
}
.result-content p {
  font-size: 14px;
  color: #555;
  margin-bottom: 4px;
}
</style> 