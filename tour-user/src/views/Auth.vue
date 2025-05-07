<template>
  <div class="auth-container">
    <h2>{{ isLogin ? '登录' : '注册' }}</h2>
    <el-form :model="form" :rules="rules" ref="authForm" label-width="100px" class="auth-form">
      <!-- 用户名 -->
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <!-- 邮箱 -->
      <el-form-item label="邮箱" prop="email" v-if="!isLogin || loginMethod === 'email'">
        <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <!-- 手机号 -->
      <el-form-item label="手机号" prop="phone" v-if="!isLogin || loginMethod === 'phone'">
        <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
      </el-form-item>
      <!-- 密码 -->
      <el-form-item label="密码" prop="password" v-if="loginMethod !== 'code'">
        <el-input v-model="form.password" type="password" placeholder="请输入密码" @input="checkPasswordStrength"></el-input>
        <div v-if="!isLogin" class="password-strength">{{ passwordStrength }}</div>
      </el-form-item>
      <!-- 确认密码（注册时） -->
      <el-form-item label="确认密码" prop="confirmPassword" v-if="!isLogin">
        <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码"></el-input>
      </el-form-item>
      <!-- 验证码 -->
      <el-form-item label="验证码" prop="code" v-if="loginMethod === 'code'">
        <el-input v-model="form.code" placeholder="请输入验证码" style="width: 60%;">
          <el-button slot="append" @click="sendCode" :disabled="codeSent">{{ codeButtonText }}</el-button>
        </el-input>
      </el-form-item>
      <!-- 登录方式切换 -->
<!--      <el-form-item label="登录方式" v-if="isLogin">-->
<!--        <el-radio-group v-model="loginMethod">-->
<!--          <el-radio label="password">密码登录</el-radio>-->
<!--          <el-radio label="code">验证码登录</el-radio>-->
<!--        </el-radio-group>-->
<!--      </el-form-item>-->
      <!-- 操作按钮 -->
      <el-form-item>
        <el-button type="primary" @click="submitForm('authForm')">
          {{ isLogin ? '登录' : '注册' }}
        </el-button>
        <el-button @click="toggleForm">
          {{ isLogin ? '去注册' : '去登录' }}
        </el-button>
      </el-form-item>
      <!-- 社交账户登录 -->
      <el-form-item v-if="isLogin">
        <div class="social-login">
          <span>社交账户登录：</span>
          <el-button type="success" @click="socialLogin('wechat')">微信登录</el-button>
          <el-button type="info" @click="socialLogin('qq')">QQ 登录</el-button>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {login, register} from "@/api/login";
import {mapGetters} from "vuex";
import {getUserProfile} from "@/api/system/user";

export default {
  name: 'Auth',
  data() {
    // 密码强度校验
    const validatePassword = (rule, value, callback) => {
      if (!this.isLogin || this.loginMethod !== 'code') {
        const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{6,}$/;
        if (!regex.test(value)) {
          callback(new Error('密码需包含字母、数字和特殊字符，长度至少6位'));
        } else {
          callback();
        }
      } else {
        callback();
      }
    };
    // 确认密码校验
    const validateConfirmPassword = (rule, value, callback) => {
      if (!this.isLogin && value !== this.form.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };
    // 邮箱校验
    const validateEmail = (rule, value, callback) => {
      if (value && !/\S+@\S+\.\S+/.test(value)) {
        callback(new Error('请输入正确的邮箱地址'));
      } else {
        callback();
      }
    };
    // 手机号校验
    const validatePhone = (rule, value, callback) => {
      if ((!this.isLogin || this.loginMethod === 'phone') && !value) {
        callback(new Error('请输入手机号'));
      } else if (value && !/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error('请输入正确的11位手机号'));
      } else {
        callback();
      }
    };
    return {
      isLogin: true, // 默认显示登录
      loginMethod: 'password', // 默认密码登录
      form: {
        username: '',
        email: '',
        phone: '',
        password: '',
        confirmPassword: '',
        code: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        email: [{ validator: validateEmail, trigger: 'blur' }],
        phone: [{ validator: validatePhone, trigger: 'blur' }],
        password: [{ validator: validatePassword, trigger: 'blur' }],
        confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }],
        code: [
          { required: this.loginMethod === 'code', message: '请输入验证码', trigger: 'blur' }
        ]
      },
      passwordStrength: '',
      codeSent: false,
      countdown: 60,
      codeButtonText: '发送验证码'
    };
  },
  components: {
    ...mapGetters([
      'avatar',
    ]),
  },
  methods: {
    // 切换登录/注册
    toggleForm() {
      this.isLogin = !this.isLogin;
      this.loginMethod = 'password'; // 重置登录方式
      this.$refs['authForm'].resetFields();
      this.passwordStrength = '';
    },
    // 检查密码强度
    checkPasswordStrength() {
      if (this.isLogin) return;
      const value = this.form.password;
      if (!value) {
        this.passwordStrength = '';
      } else if (/^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/.test(value)) {
        this.passwordStrength = '强';
      } else if (/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/.test(value)) {
        this.passwordStrength = '中';
      } else {
        this.passwordStrength = '弱';
      }
    },
    // 发送验证码
    sendCode() {
      const { email, phone } = this.form;
      if (!email && !phone) {
        this.$message.error('请先输入邮箱或手机号');
        return;
      }
      this.$axios.post('/sendCode', { email, phone }).then(response => {
        if (response.data.code === 200) {
          this.$message.success('验证码已发送');
          this.codeSent = true;
          this.startCountdown();
        } else {
          this.$message.error(response.data.msg);
        }
      }).catch(() => {
        this.$message.error('验证码发送失败');
      });
    },
    // 验证码倒计时
    startCountdown() {
      this.codeButtonText = `${this.countdown}s 后重试`;
      const timer = setInterval(() => {
        this.countdown--;
        this.codeButtonText = `${this.countdown}s 后重试`;
        if (this.countdown <= 0) {
          clearInterval(timer);
          this.codeSent = false;
          this.countdown = 60;
          this.codeButtonText = '发送验证码';
        }
      }, 1000);
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.isLogin) {
            this.login();
          } else {
            this.register();
          }
        }
      });
    },
    // 登录
    async login() {
      await this.$store.dispatch("Login", this.form).then(() => {
      }).catch(() => {
      });
      this.getUser().then((res) => {
        this.$store.state.user.name = res.data.userName
        this.$router.push({ path: "/" }).catch(()=>{});
      })
    },
    getUser() {
      return new Promise((resolve, reject) => {
        getUserProfile().then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 注册
    register() {
      register({
        username: this.form.username,
        email: this.form.email,
        phonenumber: this.form.phone,
        password: this.form.password
      }).then(response => {
        if (response.data.code === 200) {
          this.$message.success('注册成功，请登录');
          this.isLogin = true;
          this.$refs['authForm'].resetFields();
        } else {
          this.$message.error('注册失败：' + response.data.msg);
        }
      }).catch(() => {
        this.$message.error('注册请求失败');
      });
    },
    // 社交账户登录（模拟）
    socialLogin(type) {
      this.$message.info(`即将通过 ${type === 'wechat' ? '微信' : 'QQ'} 登录`);
      // 实际需跳转到第三方授权页面，此处仅模拟
      setTimeout(() => {
        localStorage.setItem('token', 'social-token');
        this.$router.push('/tours');
      }, 1000);
    },
  }
};
</script>

<style scoped>
.auth-container {
  width: 450px;
  margin: 100px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #303133;
}

.auth-form {
  padding: 0 20px;
}

.password-strength {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.social-login {
  //text-align: center;
}

.social-login span {
  //margin-right: 10px;
  color: #606266;
}
</style>