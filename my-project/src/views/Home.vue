<template>
  <div class="home">
    <!-- 搜索框 -->
    <div class="search-container">
      <el-input
          v-model="searchQuery"
          placeholder="请输入搜索内容"
          class="search-input"
          @keyup.enter.native="handleSearch"
      >
        <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
      </el-input>
    </div>

    <!-- 轮播图 -->
    <el-carousel height="400px" class="carousel">
      <el-carousel-item v-for="item in carouselItems" :key="item.id">
        <img :src="item.image" alt="carousel image" class="carousel-image" />
      </el-carousel-item>
    </el-carousel>

    <!-- 三列布局 -->
    <el-row :gutter="20" class="content-row">
      <!-- 热度排行榜 -->
      <el-col :span="6">
        <div class="rank-list">
          <h3>热度排行榜</h3>
          <el-table :data="rankData" style="width: 100%">
            <el-table-column prop="rank" label="排名" width="60"></el-table-column>
            <el-table-column prop="name" label="名称"></el-table-column>
          </el-table>
        </div>
      </el-col>

      <!-- 旅游推荐及简介 -->
      <el-col :span="12">
        <div class="travel-recommend">
          <div class="travel-header">
            <h3>旅游推荐</h3>
            <el-pagination
                small
                layout="prev, pager, next"
                :total="totalTravelItems"
                :page-size="pageSize"
                :current-page.sync="currentPage"
                @current-change="handlePageChange"
            ></el-pagination>
          </div>
          <el-card v-for="item in paginatedTravelData" :key="item.id" class="travel-card">
            <h4>{{ item.title }}</h4>
            <p>{{ item.description }}</p>
          </el-card>
        </div>
      </el-col>

      <!-- 新闻资讯快讯 -->
      <el-col :span="6">
        <div class="news-list">
          <h3>新闻资讯</h3>
          <el-timeline>
            <el-timeline-item
                v-for="item in newsData"
                :key="item.id"
                :timestamp="item.date"
                placement="top"
            >
              {{ item.title }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getUserProfile}  from "@/api/system/user";

export default {
  name: 'Home',
  data() {
    return {
      searchQuery: '', // 搜索框输入内容
      carouselItems: [
        {id: 1, image: 'http://www.xinhuanet.com/2021-06/09/1127547819_16232418196431n.jpeg'},
        {id: 2, image: 'http://www.xinhuanet.com/2021-06/09/1127547819_16232418196441n.jpeg'},
        {id: 3, image: 'http://www.xinhuanet.com/2021-06/09/1127547819_16232418196451n.jpeg'}
      ],
      rankData: [
        {rank: 1, name: '黄河口生态旅游区'},
        {rank: 2, name: '黄河三角洲国家级自然保护区'},
        {rank: 3, name: '天鹅湖景区'},
        {rank: 4, name: '黄河口文化旅游岛'},
        {rank: 5, name: '东营胜利广场'},
        {rank: 6, name: '孙子文化旅游区'},
      ],
      travelData: [
        {
          id: 1,
          title: '生态观鸟之旅',
          description: '这是一个风景优美的地方，湿地生态丰富，适合观鸟和摄影。游客可以乘船深入黄河三角洲湿地，近距离观赏东方白鹳、黑嘴鸥等珍稀鸟类，秋季还能看到碱蓬草形成的壮丽‘红地毯’，是自然爱好者和摄影师的理想选择。'
        },
        {
          id: 2,
          title: '黄河文化深度游',
          description: '历史悠久，文化底蕴深厚，体验孙子故里和渔家风情。这条线路带你探访广饶孙子文化旅游区，参观孙子祠和博物馆，了解《孙子兵法》的智慧，随后前往黄河口文化旅游岛，住渔家民宿，品尝黄河大闸蟹，感受黄河入海的文化与生活气息。'
        },
        {
          id: 3,
          title: '家庭亲子休闲游',
          description: '自然风光迷人，适合家庭度假，含亲子科普和野餐体验。带孩子游览黄河口生态旅游区，乘船欣赏黄河入海奇观，参加湿地生态讲解，天鹅湖景区还能喂天鹅、钓鱼，最后入住渔家民宿，全家共享轻松愉快的周末时光。'
        },
        {
          id: 4,
          title: '秋季红地毯摄影游',
          description: '美食天堂，体验当地特色，秋季拍摄红地毯湿地景观。每逢9-10月，黄河口生态旅游区的碱蓬草变成红色，形成震撼的自然画卷，游客可漫步湿地栈道，拍摄绝美照片，搭配当地黄河鲤鱼宴和渔家美食，享受视觉与味觉的双重盛宴。'
        },
        {
          id: 5,
          title: '黄河口生态探秘游',
          description: '探险者的乐园，充满刺激，探索黄河入海口的壮观景象。这条线路深入黄河三角洲国家级自然保护区，徒步穿越芦苇荡和滩涂，乘船体验黄河与渤海的交汇，感受大自然的磅礴力量，适合喜欢冒险和探索的游客前来挑战。'
        }
      ],
      newsData: [
        {id: 1, title: '新闻标题 1', date: '2025-03-07'},
        {id: 2, title: '新闻标题 2', date: '2025-03-06'},
        {id: 3, title: '新闻标题 3', date: '2025-03-05'}
      ],
      currentPage: 1, // 当前页码
      pageSize: 2,    // 每页显示条数
      totalTravelItems: 0 // 旅游推荐总数
    };
  },
  computed: {
    // 分页后的旅游推荐数据
    paginatedTravelData() {
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return this.travelData.slice(start, end)
    }
  },
  created() {
    // 初始化时设置总数
    this.totalTravelItems = this.travelData.length
    this.getUser();
  },
  methods: {
    handleSearch() {
      if (this.searchQuery.trim()) {
        this.$message.success(`搜索内容：${this.searchQuery}`)
      } else {
        this.$message.warning('请输入搜索内容')
      }
    },
    handlePageChange(page) {
      this.currentPage = page
    },
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
}

/* 搜索框样式 */
.search-container {
  text-align: center;
  margin-bottom: 20px;
}
.search-input {
  width: 50%;
}

/* 轮播图样式 */
.carousel {
  margin-bottom: 20px;
}
.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 三列布局样式 */
.content-row {
  margin-top: 20px;
}
.rank-list,
.travel-recommend,
.news-list {
  background: #fff;
  padding: 15px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}
.travel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}
.travel-card {
  margin-bottom: 15px;
}
</style>