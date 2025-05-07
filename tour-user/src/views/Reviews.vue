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
          <span class="date">{{ review.createTime }}</span>
        </div>
        <p>{{ review.content }}</p>
        <p class="project-name"><strong>项目：</strong>{{ review.projectName }}</p>
      </el-card>
      <el-empty v-if="sortedReviews.length === 0" description="暂无评论"></el-empty>
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

.project-name {
  color: #606266;
  margin-top: 10px;
}
</style>