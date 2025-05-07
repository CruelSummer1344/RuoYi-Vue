<template>
  <div class="reviews">
    <h2>用户评论</h2>

    <!-- 发表评论 -->
    <el-card class="review-form-card" shadow="never">
      <h3>发表您的评论</h3>
      <el-form :model="reviewForm" :rules="rules" ref="reviewForm" label-width="100px">
        <el-form-item label="选择项目" prop="projectId">
          <el-select v-model="reviewForm.projectId" placeholder="请选择体验过的项目" @change="onProjectChange" class="form-select">
            <el-option v-for="project in projects" :key="project.id" :label="project.name" :value="project.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="评分" prop="rating">
          <el-rate v-model="reviewForm.rating" show-score text-color="#ff9900" score-template="{value} 分"></el-rate>
        </el-form-item>
        <el-form-item label="评论内容" prop="content">
          <el-input type="textarea" v-model="reviewForm.content" placeholder="请分享您的真实体验" :rows="4" class="form-textarea"></el-input>
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
      <div class="sort-container">
        <el-form inline class="sort-form">
          <el-form-item label="排序">
            <el-select v-model="sortOrder" @change="sortReviews" class="sort-select">
              <el-option label="最新评论" value="desc"></el-option>
              <el-option label="最早评论" value="asc"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>

      <el-row :gutter="24" v-if="sortedReviews.length > 0">
        <el-col :xs="24" :sm="24" :md="12" :lg="12" v-for="review in sortedReviews" :key="review.id">
          <el-card class="review-card" shadow="never">
            <div class="review-header">
              <span class="username">{{ review.username }}</span>
              <span class="date">{{ review.createTime }}</span>
            </div>
            <div class="review-rating">
              <el-rate v-model="review.rating" disabled show-score text-color="#ff9900" score-template="{value} 分"></el-rate>
            </div>
            <p class="review-content">{{ review.content }}</p>
            <div class="review-footer">
              <span class="project-tag">{{ review.projectName }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-empty v-if="sortedReviews.length === 0" description="暂无评论" :image-size="200"></el-empty>
    </div>
  </div>
</template>

<script>
import {addComments, listComments} from "@/api/comments/comments";
import {listSpot} from "@/api/spot/spot";
import {listHotel} from "@/api/hotel/hotel";

export default {
  name: 'Reviews',
  data() {
    return {
      projects: [],
      reviewForm: {
        projectId: '',
        projectName: '',
        rating: null,
        content: ''
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
      sortOrder: 'desc'
    };
  },
  computed: {
    sortedReviews() {
      return [...this.reviews].sort((a, b) => {
        return this.sortOrder === 'desc'
            ? new Date(b.createTime) - new Date(a.createTime)
            : new Date(a.createTime) - new Date(b.createTime);
      });
    }
  },
  created() {
    this.loadProjects();
    this.loadReviews();
  },
  methods: {
    loadProjects() {
      listSpot().then(response => {
        if (response.code === 200) {
          this.projects = this.projects.concat(response.rows.map(spot => ({
            id: spot.spotId,
            name: spot.name
          })));
        }
      });
      listHotel().then(resp => {
        if (resp.code === 200) {
          this.projects = this.projects.concat(resp.rows.map(hotel => ({
            id: hotel.hotelId,
            name: hotel.name
          })))
        }
      });
    },
    loadReviews() {
      listComments().then(response => {
        if (response.code === 200) {
          this.reviews = response.rows.map(comment => ({
            id: comment.commentId,
            username: comment.username || '匿名用户',
            rating: comment.rating,
            content: comment.content,
            projectName: comment.projectName,
            createTime: comment.createTime
          }));
        }
      });
    },
    onProjectChange(projectId) {
      const project = this.projects.find(p => p.id === projectId);
      this.reviewForm.projectName = project ? project.name : '';
    },
    submitReview(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          const commentData = {
            projectId: this.reviewForm.projectId,
            projectName: this.reviewForm.projectName,
            rating: this.reviewForm.rating,
            content: this.reviewForm.content,
            status: '待审核',
            userId: this.$store.state.user.userId,
            username: this.$store.state.user.name
          };
          
          addComments(commentData).then(response => {
            if (response.code === 200) {
              this.$message.success('评论提交成功，待审核');
              this.loadReviews();
              this.resetForm(formName);
            }
          });
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    sortReviews() {
      this.loadReviews();
    }
  }
};
</script>

<style scoped>
.reviews {
  padding: 40px 20px;
  max-width: 1400px;
  margin: 0 auto;
  background: #f8fafc;
}

h2 {
  font-size: 28px;
  font-weight: 600;
  color: #1f2a44;
  margin-bottom: 30px;
  text-align: center;
}

h3 {
  font-size: 22px;
  font-weight: 600;
  color: #1f2a44;
  margin-bottom: 24px;
  text-align: center;
}

/* 评论表单卡片 */
.review-form-card {
  margin-bottom: 40px;
  border-radius: 12px;
  overflow: hidden;
  transition: box-shadow 0.3s ease;
  border: none;
}

.review-form-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.05);
}

.review-form-card >>> .el-card__body {
  padding: 30px;
}

.form-select, .form-textarea {
  width: 100%;
  border-radius: 8px;
}

.form-select >>> .el-input__inner,
.form-textarea >>> .el-textarea__inner {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
}

.form-textarea >>> .el-textarea__inner {
  padding: 12px;
  line-height: 1.6;
}

.el-button {
  border-radius: 8px;
  padding: 10px 20px;
  font-weight: 500;
}

.el-button--primary {
  background-color: #2563eb;
  border-color: #2563eb;
}

.el-button--primary:hover,
.el-button--primary:focus {
  background-color: #1d4ed8;
  border-color: #1d4ed8;
}

/* 评论列表 */
.review-list {
  margin-top: 60px;
}

.sort-container {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 24px;
}

.sort-select {
  width: 150px;
  border-radius: 8px;
}

.sort-select >>> .el-input__inner {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
}

.review-card {
  margin-bottom: 24px;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  border: none;
  height: 100%;
}

.review-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.review-card >>> .el-card__body {
  padding: 24px;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.username {
  font-weight: 600;
  color: #1f2a44;
  font-size: 16px;
}

.date {
  color: #64748b;
  font-size: 14px;
}

.review-rating {
  margin-bottom: 16px;
}

.review-content {
  color: #1f2a44;
  line-height: 1.6;
  margin-bottom: 16px;
  font-size: 14px;
}

.review-footer {
  display: flex;
  align-items: center;
}

.project-tag {
  font-size: 13px;
  color: #2563eb;
  background: #eff6ff;
  padding: 4px 10px;
  border-radius: 12px;
  display: inline-block;
}

/* 响应式调整 */
@media (max-width: 992px) {
  .reviews {
    padding: 30px 15px;
  }

  .review-form-card >>> .el-card__body {
    padding: 24px;
  }
}

@media (max-width: 768px) {
  h2 {
    font-size: 24px;
  }

  h3 {
    font-size: 20px;
  }

  .sort-container {
    justify-content: center;
  }
}

@media (max-width: 576px) {
  .reviews {
    padding: 20px 10px;
  }

  .review-form-card >>> .el-card__body {
    padding: 20px;
  }

  .el-form-item {
    margin-bottom: 18px;
  }

  .el-button {
    width: 100%;
    margin-bottom: 10px;
    margin-left: 0 !important;
  }
}
</style>