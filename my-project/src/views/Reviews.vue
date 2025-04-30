<template>
  <div class="reviews">
    <h2>用户评论</h2>

    <!-- 发表评论 -->
    <el-card class="review-form-card" shadow="always">
      <h3>发表您的评论</h3>
      <el-form :model="reviewForm" :rules="rules" ref="reviewForm" label-width="100px">
        <el-form-item label="选择项目" prop="projectId">
          <el-select v-model="reviewForm.projectId" placeholder="请选择体验过的项目" @change="onProjectChange">
            <el-option v-for="project in projects" :key="project.id" :label="project.name" :value="project.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="评分" prop="rating">
          <el-rate v-model="reviewForm.rating" show-score text-color="#ff9900" score-template="{value} 分"></el-rate>
        </el-form-item>
        <el-form-item label="评论内容" prop="content">
          <el-input type="textarea" v-model="reviewForm.content" placeholder="请分享您的真实体验" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="上传图片">
          <el-upload
              action="/api/upload"
              :on-success="handleUploadSuccess"
              :on-error="handleUploadError"
              :before-upload="beforeUpload"
              :file-list="reviewForm.images"
              list-type="picture-card"
              multiple
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitReview('reviewForm')">提交评论</el-button>
          <el-button @click="resetForm('reviewForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 评论列表 -->
    <div class="review-list">
      <h3>最新评论</h3>
      <el-form inline class="sort-form">
        <el-form-item label="排序">
          <el-select v-model="sortOrder" @change="sortReviews">
            <el-option label="最新评论" value="desc"></el-option>
            <el-option label="最早评论" value="asc"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-card v-for="review in sortedReviews" :key="review.id" class="review-card" shadow="hover">
        <div class="review-header">
          <span class="username">{{ review.username }}</span>
          <el-rate v-model="review.rating" disabled show-score text-color="#ff9900" score-template="{value} 分"></el-rate>
          <span class="date">{{ review.date }}</span>
        </div>
        <p>{{ review.content }}</p>
        <div class="review-images" v-if="review.images.length > 0">
          <el-image v-for="(img, index) in review.images" :key="index" :src="img.url" fit="cover" style="width: 100px; height: 100px; margin-right: 10px;" :preview-src-list="[img.url]"></el-image>
        </div>
        <p class="project-name"><strong>项目：</strong>{{ review.projectName }}</p>
      </el-card>
      <el-empty v-if="sortedReviews.length === 0" description="暂无评论"></el-empty>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Reviews',
  data() {
    return {
      projects: [
        { id: 1, name: '生态观鸟之旅' },
        { id: 2, name: '黄河文化深度游' }
      ],
      reviewForm: {
        projectId: '',
        projectName: '',
        rating: null,
        content: '',
        images: []
      },
      rules: {
        projectId: [{ required: true, message: '请选择项目', trigger: 'change' }],
        rating: [{ required: true, message: '请评分', trigger: 'change' }],
        content: [
          { required: true, message: '请输入评论内容', trigger: 'blur' },
          { min: 10, max: 500, message: '评论内容需在10-500字之间', trigger: 'blur' }
        ]
      },
      reviews: [],
      sortOrder: 'desc' // 默认按最新排序
    };
  },
  computed: {
    sortedReviews() {
      return [...this.reviews].sort((a, b) => {
        return this.sortOrder === 'desc'
            ? new Date(b.date) - new Date(a.date)
            : new Date(a.date) - new Date(b.date);
      });
    }
  },
  created() {
    this.loadReviews(); // 初始化加载评论
    // 如果从订单跳转过来，预填项目
    if (this.$route.query.projectId) {
      this.reviewForm.projectId = parseInt(this.$route.query.projectId);
      this.onProjectChange(this.reviewForm.projectId);
    }
  },
  methods: {
    // 加载评论
    loadReviews() {
      this.$axios.get('/api/reviews').then(response => {
        if (response.data.code === 200) {
          this.reviews = response.data.data;
        }
      }).catch(() => {
        this.$message.error('加载评论失败');
      });
    },
    // 项目选择
    onProjectChange(projectId) {
      const project = this.projects.find(p => p.id === projectId);
      this.reviewForm.projectName = project ? project.name : '';
    },
    // 上传图片成功
    handleUploadSuccess(response) {
      if (response.code === 200) {
        this.reviewForm.images.push({ url: response.data.url });
        this.$message.success('图片上传成功');
      }
    },
    // 上传图片失败
    handleUploadError() {
      this.$message.error('图片上传失败');
    },
    // 上传前校验
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/');
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isImage) {
        this.$message.error('只能上传图片文件！');
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过2MB！');
      }
      return isImage && isLt2M;
    },
    // 提交评论
    submitReview(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$axios.post('/api/reviews/submit', {
            projectId: this.reviewForm.projectId,
            projectName: this.reviewForm.projectName,
            rating: this.reviewForm.rating,
            content: this.reviewForm.content,
            images: this.reviewForm.images
          }).then(response => {
            if (response.data.code === 200) {
              this.$message.success('评论提交成功，待审核');
              this.reviews.push({
                id: response.data.reviewId,
                username: localStorage.getItem('username') || '匿名用户',
                rating: this.reviewForm.rating,
                content: this.reviewForm.content,
                images: this.reviewForm.images,
                projectName: this.reviewForm.projectName,
                date: new Date().toISOString().split('T')[0],
                status: '待审核'
              });
              this.resetForm(formName);
            } else {
              this.$message.error('评论提交失败：' + response.data.msg);
            }
          }).catch(() => {
            this.$message.error('提交请求失败');
          });
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.reviewForm.images = [];
    },
    // 排序评论
    sortReviews() {
      this.loadReviews(); // 可选：重新加载后排序
    }
  }
};
</script>

<style scoped>
.reviews {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h2, h3 {
  text-align: center;
  color: #303133;
  margin-bottom: 20px;
}

.review-form-card {
  margin-bottom: 40px;
}

.review-list {
  margin-top: 40px;
}

.sort-form {
  margin-bottom: 20px;
}

.review-card {
  margin-bottom: 20px;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.username {
  font-weight: bold;
  color: #409eff;
}

.date {
  color: #909399;
  font-size: 12px;
}

.review-images {
  margin-top: 10px;
}

.project-name {
  color: #606266;
  margin-top: 10px;
}
</style>