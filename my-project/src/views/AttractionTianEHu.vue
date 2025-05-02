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
      <h2>天鹅湖景区</h2>
      <p>
        天鹅湖景区位于山东省东营市，是中国著名的湿地生态旅游区。每年冬春季节，成千上万只天鹅在此栖息越冬，湖面宽广，水草丰美，是观鸟、摄影和亲子游的理想之地。景区内设有观鸟台、生态步道和亲水平台，游客可近距离观赏天鹅和多种水鸟，感受人与自然的和谐共处。
      </p>
      <div class="intro-meta">
        <span><i class="el-icon-location"></i> 山东省东营市东营区</span>
        <span><i class="el-icon-star-on"></i> 4A景区</span>
        <span><i class="el-icon-view"></i> 8654人浏览</span>
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
  name: 'AttractionTianEHu',
  data() {
    return {
      carouselItems: [
        {
          id: 1,
          image: 'https://img2.baidu.com/it/u=1234567890,1234567890&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=533',
          title: '天鹅湖冬日美景',
          description: '每年冬季，成千上万只天鹅在此栖息。'
        },
        {
          id: 2,
          image: 'https://img2.baidu.com/it/u=2234567890,2234567890&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=533',
          title: '生态湿地风光',
          description: '湖面宽广，水草丰美，生态环境优美。'
        },
        {
          id: 3,
          image: 'https://img2.baidu.com/it/u=3234567890,3234567890&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=533',
          title: '亲子观鸟乐园',
          description: '设有观鸟台和亲水平台，适合家庭游玩。'
        }
      ],
      reserveDialogVisible: false,
      comments: [
        {
          avatar: '/assets/avatar-1.jpg',
          name: '观鸟达人',
          time: '2025-03-08 11:20',
          content: '天鹅数量很多，拍到了很多美照，孩子也很喜欢！'
        },
        {
          avatar: '/assets/avatar-2.jpg',
          name: '摄影爱好者',
          time: '2025-03-07 17:05',
          content: '湿地风光很美，适合拍摄天鹅和自然景色。'
        },
        {
          avatar: '/assets/avatar-3.jpg',
          name: '亲子家庭',
          time: '2025-03-06 13:40',
          content: '带孩子来观鸟，体验很棒，推荐家庭出游！'
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