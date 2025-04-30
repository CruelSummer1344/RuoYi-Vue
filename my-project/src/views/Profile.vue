<template>
  <div class="profile">
    <h2>个人信息</h2>
    <el-card class="profile-form-card" shadow="always">
      <el-form :model="form" :rules="rules" ref="profileForm" label-width="100px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio label="0">男</el-radio>
            <el-radio label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('profileForm')">保存</el-button>
          <el-button @click="$router.go(-1)">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import {getUserProfile, updateUser} from "@/api/system/user";

export default {
  name: 'Profile',
  data() {
    return {
      form: {
        userName: '',
        email: '',
        phonenumber: '',
        sex: '',
      },
      rules: {
        userName: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        email: [
          {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
        ],
        phonenumber: [
          {pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号', trigger: 'blur'}
        ]
      }
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        console.log('sads', response)
        this.form = response.data;
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          // 模拟保存接口
          updateUser(this.form).then(response => {
            if (response.code === 200) {
              this.$message.success('个人信息更新成功');
            } else {
              this.$message.error('更新失败：' + response.data.msg);
            }
          }).catch(() => {
            // 模拟成功
            this.$message.success('个人信息更新成功（模拟）');
          });
        }
      });
    }
  }
};
</script>

<style scoped>
.profile {
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}

h2 {
  text-align: center;
  color: #303133;
  margin-bottom: 20px;
}

.profile-form-card {
  padding: 20px;
}
</style>