<template>
  <div id="app">
    <!-- 顶部菜单 -->
    <el-header>
      <el-menu :default-active="activeIndex" mode="horizontal" router @select="handleSelect">
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/tours">旅游项目</el-menu-item>
        <el-menu-item index="/booking">预约订购</el-menu-item>
        <el-menu-item index="/specialties">周边特色</el-menu-item>
        <el-menu-item index="/reviews">用户评论</el-menu-item>
        <el-menu-item index="/contact">联系客服</el-menu-item>
      </el-menu>
      <div class="auth-area">
        <!-- 未登录时显示登录按钮 -->
        <el-button v-if="!isLoggedIn" type="primary" class="login-btn" @click="handleLogin">
          登录
        </el-button>
        <!-- 已登录时显示用户名和下拉菜单 -->
        <el-dropdown v-else @command="handleCommand">
          <el-button type="primary" class="login-btn">
            欢迎，{{ name }} <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile">个人中心</el-dropdown-item>
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>

    <!-- 内容主体 -->
    <el-main>
      <router-view></router-view>
    </el-main>

    <!-- 底部 -->
    <el-footer>
      <!-- 底部区域 -->
      <div class="footer">
        <div class="footer-content">
          <div class="footer-section">
            <h4>关于我们</h4>
            <ul>
              <li><a href="#">网站介绍</a></li>
              <li><a href="#">联系方式</a></li>
              <li><a href="#">招聘信息</a></li>
              <li><a href="#">版权声明</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>帮助中心</h4>
            <ul>
              <li><a href="#">常见问题</a></li>
              <li><a href="#">订单查询</a></li>
              <li><a href="#">取消政策</a></li>
              <li><a href="#">用户协议</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>商家合作</h4>
            <ul>
              <li><a href="#">景区合作</a></li>
              <li><a href="#">酒店合作</a></li>
              <li><a href="#">广告合作</a></li>
              <li><a href="#">营销合作</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>关注我们</h4>
            <div class="social-media">
              <a href="#">微信公众号</a>
              <a href="#">微博</a>
              <a href="#">小程序</a>
              <a href="#">客服热线</a>
            </div>
          </div>
        </div>
        <div class="footer-bottom">
          <p>© 2025 东营旅游门户网站 版权所有 | 鲁ICP备XXXXXXXX号</p>
        </div>
      </div>
    </el-footer>
  </div>
</template>

<script>
import {getUserProfile} from "@/api/system/user";
import {mapState} from "vuex";
import {logout} from "@/api/login";

export default {
  data() {
    return {
      user: {},
      name: null
    }
  },
  computed: {
    ...mapState([
        'name', 'token'
    ]),
    activeIndex() {
      return this.$route.path; // 动态高亮当前路由
    },
    isLoggedIn() {
      this.name = this.$store.state.user.name
      return this.$store.state.user.token; // 检查是否登录
    },
    username() {
      return this.$store.state.user.name || '用户'; // 显示用户名
    }
  },
  created() {
    this.name = this.$store.state.user.name || ''
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
      });
    },
    // 菜单选择（添加防抖）
    handleSelect(key) {
      if (this.$route.path !== key) {
        this.debouncePush(key); // 使用防抖跳转
      }
    },
    // 防抖跳转函数
    debouncePush: debounce(function (path) {
      this.$router.push(path).catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    }, 300), // 300ms 延迟
    // 登录跳转
    handleLogin() {
      if (this.$route.path !== '/auth') {
        this.$router.push('/auth');
      }
    },
    // 退出登录
    handleCommand(command) {
      switch (command) {
        case 'profile':
          this.$router.push('/profile');
          break;
        case 'logout':
          this.$store.dispatch('LogOut').then(() => {
            location.href = '/'
          })
      }
    },
  }
};

// 防抖函数
function debounce(func, wait) {
  let timeout;
  return function (...args) {
    clearTimeout(timeout);
    timeout = setTimeout(() => func.apply(this, args), wait);
  };
}
</script>

<style scoped>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.el-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #303133; /* 深灰色背景 */
  color: #fff;
}

.el-menu {
  background-color: transparent;
  border-bottom: none;
}

.el-menu-item {
  color: #fff !important;
}

.el-menu-item.is-active {
  color: #409eff !important;
  border-bottom-color: #409eff !important;
}

.el-menu-item:hover {
  background-color: #404245 !important;
}

.auth-area {
  display: flex;
  align-items: center;
}

.login-btn {
  margin-right: 20px;
}

.el-main {
  flex: 1;
  padding: 20px;
}

.el-footer {
  text-align: center;
  background-color: #f5f7fa;
  padding: 10px;
  color: #606266;
}

/* 底部区域 */
.footer {
  background-color: #333;
  color: #fff;
  padding: 40px 0 20px;
  margin-top: 40px;
}

.footer-content {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
  gap: 30px;
  padding: 0 15px;
}

.footer-section {
  flex: 1;
  min-width: 200px;
  max-width: 250px;
  margin-bottom: 20px;
  text-align: center;
}

.footer-section h4 {
  font-size: 16px;
  margin-bottom: 15px;
  position: relative;
  padding-bottom: 10px;
  display: inline-block;
}

.footer-section h4::after {
  content: '';
  position: absolute;
  left: 50%;
  bottom: 0;
  width: 30px;
  height: 2px;
  background-color: #1890ff;
  transform: translateX(-50%);
  transition: width 0.3s ease;
}

.footer-section h4:hover::after {
  width: 50px;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section ul li {
  text-align: center;
  margin-bottom: 8px;
}

.footer-section ul li a {
  color: #bbb;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
  display: inline-block;
}

.footer-section ul li a:hover {
  color: #1890ff;
}

.social-media {
  display: flex;
  flex-direction: column;
}

.social-media a {
  color: #bbb;
  text-decoration: none;
  font-size: 14px;
  margin-bottom: 8px;
  transition: color 0.3s;
  display: flex;
  justify-content: center;
  align-items: center;
}

.social-media a:hover {
  color: #1890ff;
}

.social-media a i {
  margin-right: 5px;
}

.footer-bottom {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
  padding: 20px 0;
  border-top: 1px solid #444;
}

.footer-bottom p {
  font-size: 13px;
  color: #999;
  margin: 0;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .footer-content {
    width: 95%;
    gap: 25px;
  }
  .footer-bottom {
    width: 95%;
  }
}

@media (max-width: 992px) {
  .footer-section {
    flex: 0 0 45%;
    max-width: none;
    min-width: 180px;
  }
}

@media (max-width: 768px) {
  .footer-content {
    gap: 20px;
    padding: 0 10px;
  }
  .footer-section {
    flex: 0 0 100%;
    min-width: 0;
    max-width: none;
  }
}
</style>