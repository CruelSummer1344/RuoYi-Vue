<template>
  <div class="attraction-detail">
    <!-- 轮播图 -->
    <div class="carousel-container">
      <el-carousel height="400px" class="carousel" indicator-position="outside" arrow="always">
        <el-carousel-item v-for="item in carouselItems" :key="item.id">
          <div class="carousel-content">
            <img :src="item.image" alt="carousel image" class="carousel-image" />
            <div class="carousel-caption">
              <h2>{{ item.title }}</h2>
              <p>{{ item.description }}</p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 景点介绍 -->
    <div class="intro-section">
      <h2>黄河口生态旅游区</h2>
      <p>
        黄河口生态旅游区位于山东省东营市，是中国唯一能够同时观赏黄河入海奇观和湿地风光的生态旅游胜地。这里拥有丰富的湿地资源，是候鸟迁徙的重要驿站。每年秋季，碱蓬草变红，形成壮观的"红地毯"景观，是摄影爱好者的天堂。游客可以乘船深入黄河三角洲湿地，近距离观赏东方白鹳、黑嘴鸥等珍稀鸟类，体验人与自然和谐共生的美好画卷。
      </p>
      <div class="intro-meta">
        <span><i class="el-icon-location"></i> 山东省东营市黄河入海口</span>
        <span><i class="el-icon-star-on"></i> 5A景区</span>
        <span><i class="el-icon-view"></i> 12580人浏览</span>
      </div>
      <el-button type="primary" size="large" @click="reserveDialogVisible = true">立即预约</el-button>
    </div>

    <!-- 网友评论 -->
    <div class="comments-section">
      <div class="section-header">
        <h3>网友评论</h3>
      </div>
      <div class="comment-list">
        <div class="comment-item" v-for="(item, index) in comments" :key="index">
          <img :src="item.avatar" class="comment-avatar" alt="头像" />
          <div class="comment-content">
            <div class="comment-user">
              <span class="user-name">{{ item.name }}</span>
              <span class="comment-time">{{ item.time }}</span>
            </div>
            <div class="comment-text">{{ item.content }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 预约弹窗 -->
    <el-dialog
      title="预约提示"
      :visible.sync="reserveDialogVisible"
      width="30%"
      center
    >
      <span>预约成功！我们期待您的到来。</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="reserveDialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'AttractionHuanghekou',
  data() {
    return {
      carouselItems: [
        {
          id: 1,
          image: 'http://www.xinhuanet.com/2021-06/09/1127547819_16232418196431n.jpeg',
          title: '黄河入海 壮美东营',
          description: '探索中国唯一能够同时观赏黄河入海奇观和湿地风光的生态旅游胜地'
        },
        {
          id: 2,
          image: 'http://www.xinhuanet.com/2021-06/09/1127547819_16232418196441n.jpeg',
          title: '湿地天堂 生态东营',
          description: '中国最大的新生湿地，国际重要湿地，候鸟迁徙的重要驿站'
        },
        {
          id: 3,
          image: 'http://www.xinhuanet.com/2021-06/09/1127547819_16232418196451n.jpeg',
          title: '红色海滩 梦幻东营',
          description: '每年秋季，碱蓬草变红，形成壮观的"红地毯"景观，摄影爱好者的天堂'
        }
      ],
      reserveDialogVisible: false,
      comments: [
        {
          avatar: '/assets/avatar-1.jpg',
          name: '旅行家小王',
          time: '2025-03-08 10:23',
          content: '景区风光非常美，湿地生态很棒，拍了很多好看的照片！推荐大家秋天来。'
        },
        {
          avatar: '/assets/avatar-2.jpg',
          name: '美食达人',
          time: '2025-03-07 18:45',
          content: '黄河入海口的景色很壮观，附近的渔家美食也很有特色。'
        },
        {
          avatar: '/assets/avatar-3.jpg',
          name: '亲子游专家',
          time: '2025-03-06 14:12',
          content: '带孩子来湿地科普，体验很好，孩子很喜欢看鸟和坐船。'
        }
      ]
    }
  }
}
</script>

<style scoped>
.attraction-detail {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding-bottom: 40px;
}

.carousel-container {
  width: 80%;
  margin: 30px auto 0 auto;
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
  height: 400px;
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
  margin-bottom: 0;
  max-width: 60%;
}

.intro-section {
  width: 80%;
  margin: 30px auto 0 auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 30px 30px 20px 30px;
}

.intro-section h2 {
  font-size: 24px;
  color: #1890ff;
  margin-bottom: 15px;
}

.intro-section p {
  font-size: 15px;
  color: #333;
  line-height: 1.7;
  margin-bottom: 15px;
}

.intro-meta {
  display: flex;
  gap: 30px;
  font-size: 14px;
  color: #666;
  margin-bottom: 20px;
}

.intro-meta i {
  margin-right: 5px;
  color: #1890ff;
}

.comments-section {
  width: 80%;
  margin: 30px auto 0 auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 30px;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
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

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  align-items: flex-start;
}

.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 15px;
}

.comment-content {
  flex: 1;
}

.comment-user {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 5px;
}

.user-name {
  font-weight: bold;
  color: #1890ff;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-text {
  font-size: 14px;
  color: #333;
  line-height: 1.6;
}
</style> 