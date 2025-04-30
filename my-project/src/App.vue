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
      <p>© 2025 黄河三角洲文旅平台. All Rights Reserved.</p>
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
</style>