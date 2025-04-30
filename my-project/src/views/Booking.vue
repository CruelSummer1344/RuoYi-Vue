<template>
  <el-dialog title="预订详情" :visible.sync="dialogVisible" width="40%">
    <el-form :model="bookingForm" ref="bookingForm" :rules="rules" label-width="100px">
      <el-form-item label="项目名称" prop="projectName">
        <el-input v-model="bookingForm.projectName" disabled></el-input>
      </el-form-item>
      <el-form-item label="套餐" prop="packageName">
        <el-input v-model="bookingForm.packageName" disabled></el-input>
      </el-form-item>
      <el-form-item label="酒店" v-if="bookingForm.hotel">
        <el-input v-model="bookingForm.hotel" disabled></el-input>
      </el-form-item>
      <el-form-item label="餐饮" v-if="bookingForm.restaurant">
        <el-input v-model="bookingForm.restaurant" disabled></el-input>
      </el-form-item>
      <el-form-item label="活动" v-if="bookingForm.activity">
        <el-input v-model="bookingForm.activity" disabled></el-input>
      </el-form-item>
      <el-form-item label="预订日期" prop="date">
        <el-date-picker v-model="bookingForm.date" type="date" placeholder="选择日期"></el-date-picker>
      </el-form-item>
      <el-form-item label="人数" prop="peopleCount">
        <el-input-number v-model="bookingForm.peopleCount" :min="1" :max="10"></el-input-number>
      </el-form-item>
      <el-form-item label="总价">
        <span>￥{{ totalPrice }}</span>
      </el-form-item>
      <el-form-item label="支付方式" prop="paymentMethod">
        <el-radio-group v-model="bookingForm.paymentMethod">
          <el-radio label="wechat">微信支付</el-radio>
          <el-radio label="alipay">支付宝</el-radio>
          <el-radio label="bank">银行卡</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="confirmBooking('bookingForm')">确认支付</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      bookingForm: {
        projectId: '',
        projectName: '',
        packageId: '',
        packageName: '',
        hotel: '',
        restaurant: '',
        activity: '',
        date: '',
        peopleCount: 1,
        paymentMethod: 'wechat'
      },
      rules: {
        date: [{ required: true, message: '请选择日期', trigger: 'change' }],
        peopleCount: [{ required: true, message: '请选择人数', trigger: 'change' }],
        paymentMethod: [{ required: true, message: '请选择支付方式', trigger: 'change' }]
      }
    };
  },
  computed: {
    totalPrice() {
      const project = this.projects.find(p => p.id === this.bookingForm.projectId);
      if (!project) return 0;
      const pkg = project.packages.find(p => p.id === this.bookingForm.packageId);
      return pkg ? (pkg.price * this.bookingForm.peopleCount).toFixed(2) : 0;
    }
  },
  created() {
    // 从路由获取项目和套餐
    if (this.$route.query.projectId && this.$route.query.packageId) {
      const project = this.projects.find(p => p.id === parseInt(this.$route.query.projectId));
      if (project) {
        const pkg = project.packages.find(p => p.id === parseInt(this.$route.query.packageId));
        if (pkg) {
          this.bookingForm.projectId = project.id;
          this.bookingForm.projectName = project.name;
          this.bookingForm.packageId = pkg.id;
          this.bookingForm.packageName = pkg.name;
          this.bookingForm.hotel = pkg.hotel || '';
          this.bookingForm.restaurant = pkg.restaurant || '';
          this.bookingForm.activity = pkg.activity || '';
          this.dialogVisible = true;
        }
      }
    }
  },
  methods: {
    confirmBooking(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          const order = {
            projectId: this.bookingForm.projectId,
            projectName: this.bookingForm.projectName,
            packageId: this.bookingForm.packageId,
            packageName: this.bookingForm.packageName,
            hotel: this.bookingForm.hotel,
            restaurant: this.bookingForm.restaurant,
            activity: this.bookingForm.activity,
            date: this.bookingForm.date,
            peopleCount: this.bookingForm.peopleCount,
            totalPrice: this.totalPrice,
            paymentMethod: this.bookingForm.paymentMethod
          };
          // 模拟订单创建
          this.orders.push({
            orderId: `ORD${Date.now()}`,
            projectId: order.projectId,
            projectName: order.projectName,
            date: order.date.toISOString().split('T')[0],
            peopleCount: order.peopleCount,
            totalPrice: order.totalPrice,
            status: '已支付',
            details: {
              hotel: order.hotel,
              restaurant: order.restaurant,
              activity: order.activity
            }
          });
          this.filteredOrders = [...this.orders];
          this.$message.success('订单创建并支付成功（模拟）');
          this.dialogVisible = false;
          this.$refs['bookingForm'].resetFields();
          this.$confirm('支付成功！是否现在评价？', '提示', {
            confirmButtonText: '去评价',
            cancelButtonText: '稍后',
            type: 'success'
          }).then(() => {
            this.$router.push({ path: '/reviews', query: { projectId: this.bookingForm.projectId } });
          });
        }
      });
    }
  }
};
</script>
<style scoped>
.booking {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h2, h3 {
  text-align: center;
  color: #303133;
  margin-bottom: 20px;
}

.search-form, .order-search-form {
  margin-bottom: 20px;
}

.project-card {
  margin-bottom: 20px;
}

.project-card h3 {
  color: #409eff;
  margin: 0 0 10px 0;
}

.project-card p {
  color: #606266;
  margin: 5px 0;
}

.order-management {
  margin-top: 40px;
}

.order-search-form {
  background: #f5f7fa;
  padding: 10px;
  border-radius: 8px;
}
</style>