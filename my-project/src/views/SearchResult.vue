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
      attractionsList: [
        {
          id: 1,
          name: '黄河口生态旅游区',
          image: '/assets/attraction-1.jpg',
          level: '5A景区',
          rating: 4.9,
          price: 120
        },
        {
          id: 2,
          name: '天鹅湖景区',
          image: '/assets/attraction-2.jpg',
          level: '4A景区',
          rating: 4.7,
          price: 80
        },
        {
          id: 3,
          name: '孙子文化园',
          image: '/assets/attraction-3.jpg',
          level: '4A景区',
          rating: 4.6,
          price: 60
        }
      ],
      travelData: [
        {
          id: 1,
          title: '生态观鸟之旅',
          image: '/assets/route-1.jpg',
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
          image: '/assets/route-2.jpg',
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
          image: '/assets/route-3.jpg',
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
          image: '/assets/route-4.jpg',
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
          image: '/assets/route-5.jpg',
          tag: '探险',
          duration: '2天1晚',
          startPoint: '东营市区',
          price: 498,
          rating: 4.5,
          description: '探险者的乐园，充满刺激，探索黄河入海口的壮观景象。这条线路深入黄河三角洲国家级自然保护区，徒步穿越芦苇荡和滩涂，乘船体验黄河与渤海的交汇，感受大自然的磅礴力量，适合喜欢冒险和探索的游客前来挑战。'
        }
      ],
      guideList: [
        {
          id: 1,
          title: '东营三日游完美攻略，带你领略黄河入海的壮美',
          author: '旅行家小王',
          authorAvatar: '/assets/avatar-1.jpg',
          image: '/assets/guide-1.jpg',
          views: 12567,
          likes: 3456
        },
        {
          id: 2,
          title: '东营美食地图：不可错过的十大特色美食',
          author: '美食达人',
          authorAvatar: '/assets/avatar-2.jpg',
          image: '/assets/guide-2.jpg',
          views: 9876,
          likes: 2345
        },
        {
          id: 3,
          title: '带着孩子游东营，亲子游玩全攻略',
          author: '亲子游专家',
          authorAvatar: '/assets/avatar-3.jpg',
          image: '/assets/guide-3.jpg',
          views: 8765,
          likes: 1987
        }
      ],
      hotelList: [
        {
          id: 1,
          name: '东营国际大酒店',
          image: '/assets/hotel-1.jpg',
          price: 399,
          description: '市中心高端酒店，交通便利，配套设施齐全。'
        },
        {
          id: 2,
          name: '黄河口生态酒店',
          image: '/assets/hotel-2.jpg',
          price: 299,
          description: '靠近黄河口景区，环境优美，适合度假休闲。'
        },
        {
          id: 3,
          name: '胜利宾馆',
          image: '/assets/hotel-3.jpg',
          price: 199,
          description: '经济实惠，适合商务及短途旅行。'
        }
      ],
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