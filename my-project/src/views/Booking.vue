<template>
    <div class="booking">
        <!-- 项目搜索区域 -->
        <h2>旅游项目预订</h2>
        <div class="search-form">
            <el-form :inline="true" :model="searchForm" class="demo-form-inline">
                <el-form-item label="项目名称">
                    <el-input v-model="searchForm.name" placeholder="输入项目名称"></el-input>
                </el-form-item>
                <el-form-item label="项目类型">
                    <el-select v-model="searchForm.type" placeholder="选择项目类型" clearable>
                        <el-option label="景点门票" value="attraction"></el-option>
                        <el-option label="精品路线" value="route"></el-option>
                        <el-option label="文化体验" value="culture"></el-option>
                        <el-option label="生态游" value="eco"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="searchProjects">搜索</el-button>
                    <el-button @click="resetSearch">重置</el-button>
                </el-form-item>
            </el-form>
        </div>

        <!-- 项目列表 -->
        <el-row :gutter="20" class="project-row">
            <el-col :xs="24" :sm="12" :md="8" v-for="project in filteredProjects" :key="project.id">
                <el-card class="project-card" shadow="hover">
                    <div class="project-image">
                        <img :src="project.image" alt="项目图片" class="image">
                        <div class="project-tag" v-if="project.tag">{{ project.tag }}</div>
                    </div>
                    <h3>{{ project.name }}</h3>
                    <p class="project-desc">{{ project.description }}</p>
                    <div class="project-info">
                        <span><i class="el-icon-location"></i> {{ project.location }}</span>
                        <span><i class="el-icon-view"></i> {{ project.views }}</span>
                    </div>
                    <div class="package-list">
                        <h4>可选套餐</h4>
                        <el-table :data="project.packages" style="width: 100%">
                            <el-table-column prop="name" label="套餐名称"></el-table-column>
                            <el-table-column prop="price" label="价格">
                                <template slot-scope="scope">
                                    <span class="price">¥{{ scope.row.price }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" width="100">
                                <template slot-scope="scope">
                                    <el-button
                                        type="text"
                                        size="small"
                                        @click="openBookingDialog(project, scope.row)"
                                    >
                                        预订
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <!-- 分页 -->
        <div class="pagination-container" v-if="filteredProjects.length > 0">
            <el-pagination
                background
                layout="prev, pager, next"
                :total="filteredProjects.length"
                :page-size="6"
                @current-change="handlePageChange"
            ></el-pagination>
        </div>

        <!-- 订单管理 -->
        <div class="order-management">
            <h2>我的订单</h2>
            <div class="order-search-form">
                <el-form :inline="true" :model="orderSearchForm" class="demo-form-inline">
                    <el-form-item label="订单状态">
                        <el-select v-model="orderSearchForm.status" placeholder="选择订单状态" clearable>
                            <el-option label="全部" value=""></el-option>
                            <el-option label="已支付" value="已支付"></el-option>
                            <el-option label="待支付" value="待支付"></el-option>
                            <el-option label="已取消" value="已取消"></el-option>
                            <el-option label="已完成" value="已完成"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="日期范围">
                        <el-date-picker
                            v-model="orderSearchForm.dateRange"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            value-format="yyyy-MM-dd"
                        ></el-date-picker>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="searchOrders">搜索</el-button>
                        <el-button @click="resetOrderSearch">重置</el-button>
                    </el-form-item>
                </el-form>
            </div>

            <!-- 订单列表 -->
            <el-table :data="filteredOrders" style="width: 100%" v-if="filteredOrders.length > 0">
                <el-table-column prop="orderId" label="订单号" width="180"></el-table-column>
                <el-table-column prop="projectName" label="项目名称"></el-table-column>
                <el-table-column prop="date" label="预订日期" width="120"></el-table-column>
                <el-table-column prop="peopleCount" label="人数" width="80"></el-table-column>
                <el-table-column prop="totalPrice" label="总价" width="120">
                    <template slot-scope="scope">
                        <span class="price">¥{{ scope.row.totalPrice }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                    <template slot-scope="scope">
                        <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="viewOrderDetails(scope.row)">查看详情</el-button>
                        <el-button
                            type="text"
                            size="small"
                            @click="cancelOrder(scope.row)"
                            v-if="scope.row.status === '待支付'"
                        >
                            取消订单
                        </el-button>
                        <el-button
                            type="text"
                            size="small"
                            @click="goToReview(scope.row)"
                            v-if="scope.row.status === '已支付' || scope.row.status === '已完成'"
                        >
                            评价
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-empty description="暂无订单" v-else></el-empty>
        </div>

        <!-- 预订对话框 -->
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
                    <span class="total-price">￥{{ totalPrice }}</span>
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

        <!-- 订单详情对话框 -->
        <el-dialog title="订单详情" :visible.sync="orderDetailVisible" width="40%">
            <div v-if="selectedOrder">
                <el-descriptions :column="1" border>
                    <el-descriptions-item label="订单号">{{ selectedOrder.orderId }}</el-descriptions-item>
                    <el-descriptions-item label="项目名称">{{ selectedOrder.projectName }}</el-descriptions-item>
                    <el-descriptions-item label="预订日期">{{ selectedOrder.date }}</el-descriptions-item>
                    <el-descriptions-item label="人数">{{ selectedOrder.peopleCount }}</el-descriptions-item>
                    <el-descriptions-item label="总价">¥{{ selectedOrder.totalPrice }}</el-descriptions-item>
                    <el-descriptions-item label="状态">
                        <el-tag :type="getStatusType(selectedOrder.status)">{{ selectedOrder.status }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="酒店" v-if="selectedOrder.details && selectedOrder.details.hotel">
                        {{ selectedOrder.details.hotel }}
                    </el-descriptions-item>
                    <el-descriptions-item label="餐饮" v-if="selectedOrder.details && selectedOrder.details.restaurant">
                        {{ selectedOrder.details.restaurant }}
                    </el-descriptions-item>
                    <el-descriptions-item label="活动" v-if="selectedOrder.details && selectedOrder.details.activity">
                        {{ selectedOrder.details.activity }}
                    </el-descriptions-item>
                </el-descriptions>
            </div>
            <div slot="footer">
                <el-button @click="orderDetailVisible = false">关闭</el-button>
                <el-button
                    type="primary"
                    @click="goToReview(selectedOrder)"
                    v-if="selectedOrder && (selectedOrder.status === '已支付' || selectedOrder.status === '已完成')"
                >
                    去评价
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: 'Booking',
    data() {
        return {
            // 对话框显示控制
            dialogVisible: false,
            orderDetailVisible: false,

            // 预订表单
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

            // 表单验证规则
            rules: {
                date: [{ required: true, message: '请选择日期', trigger: 'change' }],
                peopleCount: [{ required: true, message: '请选择人数', trigger: 'change' }],
                paymentMethod: [{ required: true, message: '请选择支付方式', trigger: 'change' }]
            },

            // 项目搜索表单
            searchForm: {
                name: '',
                type: ''
            },

            // 订单搜索表单
            orderSearchForm: {
                status: '',
                dateRange: []
            },

            // 选中的订单（用于详情展示）
            selectedOrder: null,

            // 项目数据
            projects: [
                {
                    id: 1,
                    name: '黄河口生态旅游区',
                    description: '探索中国唯一能够同时观赏黄河入海奇观和湿地风光的生态旅游胜地',
                    location: '东营市河口区',
                    type: 'attraction',
                    tag: '5A景区',
                    views: 12580,
                    image: 'http://www.xinhuanet.com/2021-06/09/1127547819_16232418196431n.jpeg',
                    packages: [
                        {
                            id: 101,
                            name: '标准门票',
                            price: 120,
                            description: '含景区内所有景点参观'
                        },
                        {
                            id: 102,
                            name: '黄河入海观光套餐',
                            price: 220,
                            description: '含门票、游船、导游讲解',
                            activity: '黄河入海观光船游览'
                        },
                        {
                            id: 103,
                            name: '生态探秘两日游',
                            price: 580,
                            description: '含门票、住宿、餐饮、导游',
                            hotel: '黄河口生态度假酒店',
                            restaurant: '渔家风味餐厅',
                            activity: '湿地徒步+观鸟+黄河入海观光'
                        }
                    ]
                },
                {
                    id: 2,
                    name: '天鹅湖景区',
                    description: '中国最大的新生湿地，国际重要湿地，候鸟迁徙的重要驿站',
                    location: '东营市东营区',
                    type: 'eco',
                    tag: '4A景区',
                    views: 8654,
                    image: 'http://www.xinhuanet.com/2021-06/09/1127547819_16232418196441n.jpeg',
                    packages: [
                        {
                            id: 201,
                            name: '标准门票',
                            price: 80,
                            description: '含景区内所有景点参观'
                        },
                        {
                            id: 202,
                            name: '观鸟摄影套餐',
                            price: 150,
                            description: '含门票、专业观鸟向导、摄影指导',
                            activity: '专业观鸟向导服务'
                        },
                        {
                            id: 203,
                            name: '湿地生态两日游',
                            price: 480,
                            description: '含门票、住宿、餐饮、导游',
                            hotel: '天鹅湖度假酒店',
                            restaurant: '生态农家餐厅',
                            activity: '湿地徒步+观鸟+摄影指导'
                        }
                    ]
                },
                {
                    id: 3,
                    name: '孙子文化园',
                    description: '体验孙子兵法文化，了解中国古代军事智慧的主题公园',
                    location: '东营市广饶县',
                    type: 'culture',
                    tag: '4A景区',
                    views: 5890,
                    image: 'http://www.xinhuanet.com/2021-06/09/1127547819_16232418196451n.jpeg',
                    packages: [
                        {
                            id: 301,
                            name: '标准门票',
                            price: 60,
                            description: '含景区内所有景点参观'
                        },
                        {
                            id: 302,
                            name: '兵法文化体验套餐',
                            price: 120,
                            description: '含门票、专业讲解、兵法体验活动',
                            activity: '兵法体验互动活动'
                        },
                        {
                            id: 303,
                            name: '文化深度两日游',
                            price: 420,
                            description: '含门票、住宿、餐饮、导游',
                            hotel: '孙子文化主题酒店',
                            restaurant: '兵圣宴餐厅',
                            activity: '兵法讲座+沙盘推演+古代兵器体验'
                        }
                    ]
                },
                {
                    id: 4,
                    name: '黄河三角洲国家级自然保护区',
                    description: '中国最年轻的湿地生态系统，珍稀鸟类栖息地',
                    location: '东营市河口区',
                    type: 'eco',
                    tag: '国家级保护区',
                    views: 9876,
                    image: '/assets/attraction-4.jpg',
                    packages: [
                        {
                            id: 401,
                            name: '生态参观票',
                            price: 100,
                            description: '含保护区参观路线'
                        },
                        {
                            id: 402,
                            name: '科研考察套餐',
                            price: 280,
                            description: '含门票、专业生态讲解、深入考察',
                            activity: '生态科普讲解'
                        },
                        {
                            id: 403,
                            name: '生态研学两日游',
                            price: 680,
                            description: '含门票、住宿、餐饮、专业向导',
                            hotel: '三角洲生态酒店',
                            restaurant: '湿地风味餐厅',
                            activity: '生态讲座+野外考察+鸟类观测'
                        }
                    ]
                },
                {
                    id: 5,
                    name: '红海滩生态旅游区',
                    description: '每年秋季，碱蓬草变红，形成壮观的"红地毯"景观',
                    location: '东营市河口区',
                    type: 'attraction',
                    tag: '网红打卡地',
                    views: 15678,
                    image: '/assets/attraction-5.jpg',
                    packages: [
                        {
                            id: 501,
                            name: '标准门票',
                            price: 80,
                            description: '含景区内所有景点参观'
                        },
                        {
                            id: 502,
                            name: '摄影专线套餐',
                            price: 180,
                            description: '含门票、摄影向导、最佳拍摄点',
                            activity: '摄影向导服务'
                        },
                        {
                            id: 503,
                            name: '红海滩两日游',
                            price: 520,
                            description: '含门票、住宿、餐饮、导游',
                            hotel: '红海滩度假酒店',
                            restaurant: '海鲜风味餐厅',
                            activity: '日出日落摄影+滩涂徒步+渔家体验'
                        }
                    ]
                },
                {
                    id: 6,
                    name: '黄河文化旅游线路',
                    description: '探索黄河文化，体验黄河沿岸的历史与自然风光',
                    location: '东营市多区域',
                    type: 'route',
                    tag: '精品线路',
                    views: 7890,
                    image: '/assets/route-1.jpg',
                    packages: [
                        {
                            id: 601,
                            name: '一日游',
                            price: 298,
                            description: '黄河入海口-湿地公园-渔家体验',
                            restaurant: '渔家风味餐',
                            activity: '黄河文化讲解+渔家体验'
                        },
                        {
                            id: 602,
                            name: '两日游',
                            price: 598,
                            description: '黄河入海口-湿地公园-孙子文化园-渔家体验',
                            hotel: '黄河文化主题酒店',
                            restaurant: '黄河风味餐厅',
                            activity: '黄河文化讲解+渔家体验+兵法体验'
                        },
                        {
                            id: 603,
                            name: '三日游',
                            price: 898,
                            description: '全面体验黄河文化和东营自然风光',
                            hotel: '五星级东营大酒店',
                            restaurant: '特色美食餐厅',
                            activity: '黄河文化+湿地生态+红海滩+渔家体验'
                        }
                    ]
                }
            ],

            // 订单数据
            orders: [
                {
                    orderId: 'ORD20250301001',
                    projectId: 1,
                    projectName: '黄河口生态旅游区',
                    date: '2025-03-15',
                    peopleCount: 2,
                    totalPrice: '440.00',
                    status: '已支付',
                    details: {
                        packageName: '黄河入海观光套餐',
                        activity: '黄河入海观光船游览'
                    }
                },
                {
                    orderId: 'ORD20250228002',
                    projectId: 3,
                    projectName: '孙子文化园',
                    date: '2025-03-10',
                    peopleCount: 1,
                    totalPrice: '120.00',
                    status: '已完成',
                    details: {
                        packageName: '兵法文化体验套餐',
                        activity: '兵法体验互动活动'
                    }
                },
                {
                    orderId: 'ORD20250227003',
                    projectId: 2,
                    projectName: '天鹅湖景区',
                    date: '2025-04-05',
                    peopleCount: 3,
                    totalPrice: '1440.00',
                    status: '待支付',
                    details: {
                        packageName: '湿地生态两日游',
                        hotel: '天鹅湖度假酒店',
                        restaurant: '生态农家餐厅',
                        activity: '湿地徒步+观鸟+摄影指导'
                    }
                }
            ],

            // 筛选后的项目和订单
            filteredProjects: [],
            filteredOrders: [],

            // 当前页码
            currentPage: 1
        };
    },
    computed: {
        // 计算总价
        totalPrice() {
            const project = this.projects.find(p => p.id === this.bookingForm.projectId);
            if (!project) return '0.00';

            const pkg = project.packages.find(p => p.id === this.bookingForm.packageId);
            return pkg ? (pkg.price * this.bookingForm.peopleCount).toFixed(2) : '0.00';
        }
    },
    created() {
        // 初始化筛选后的项目和订单
        this.filteredProjects = [...this.projects];
        this.filteredOrders = [...this.orders];

        // 从路由获取项目和套餐
        if (this.$route.query.projectId && this.$route.query.packageId) {
            const projectId = parseInt(this.$route.query.projectId);
            const packageId = parseInt(this.$route.query.packageId);

            const project = this.projects.find(p => p.id === projectId);
            if (project) {
                const pkg = project.packages.find(p => p.id === packageId);
                if (pkg) {
                    this.openBookingDialog(project, pkg);
                }
            }
        }
    },
    methods: {
        // 打开预订对话框
        openBookingDialog(project, pkg) {
            this.bookingForm = {
                projectId: project.id,
                projectName: project.name,
                packageId: pkg.id,
                packageName: pkg.name,
                hotel: pkg.hotel || '',
                restaurant: pkg.restaurant || '',
                activity: pkg.activity || '',
                date: '',
                peopleCount: 1,
                paymentMethod: 'wechat'
            };
            this.dialogVisible = true;
        },

        // 确认预订
        confirmBooking(formName) {
            this.$refs[formName].validate(valid => {
                if (valid) {
                    // 创建订单对象
                    const order = {
                        orderId: `ORD${Date.now()}`,
                        projectId: this.bookingForm.projectId,
                        projectName: this.bookingForm.projectName,
                        date: this.formatDate(this.bookingForm.date),
                        peopleCount: this.bookingForm.peopleCount,
                        totalPrice: this.totalPrice,
                        status: '已支付',
                        details: {
                            packageName: this.bookingForm.packageName,
                            hotel: this.bookingForm.hotel,
                            restaurant: this.bookingForm.restaurant,
                            activity: this.bookingForm.activity
                        }
                    };

                    // 添加到订单列表
                    this.orders.unshift(order);
                    this.filteredOrders = [...this.orders];

                    // 提示成功并关闭对话框
                    this.$message.success('订单创建并支付成功');
                    this.dialogVisible = false;

                    // 重置表单
                    this.$refs[formName].resetFields();

                    // 询问是否评价
                    this.$confirm('支付成功！是否现在评价？', '提示', {
                        confirmButtonText: '去评价',
                        cancelButtonText: '稍后',
                        type: 'success'
                    }).then(() => {
                        this.goToReview(order);
                    }).catch(() => {});
                }
            });
        },

        // 搜索项目
        searchProjects() {
            this.filteredProjects = this.projects.filter(project => {
                const nameMatch = !this.searchForm.name ||
                    project.name.toLowerCase().includes(this.searchForm.name.toLowerCase());
                const typeMatch = !this.searchForm.type || project.type === this.searchForm.type;
                return nameMatch && typeMatch;
            });
            this.currentPage = 1;
        },

        // 重置项目搜索
        resetSearch() {
            this.searchForm = {
                name: '',
                type: ''
            };
            this.filteredProjects = [...this.projects];
            this.currentPage = 1;
        },

        // 搜索订单
        searchOrders() {
            this.filteredOrders = this.orders.filter(order => {
                // 状态筛选
                const statusMatch = !this.orderSearchForm.status ||
                    order.status === this.orderSearchForm.status;

                // 日期范围筛选
                let dateMatch = true;
                if (this.orderSearchForm.dateRange && this.orderSearchForm.dateRange.length === 2) {
                    const orderDate = new Date(order.date);
                    const startDate = new Date(this.orderSearchForm.dateRange[0]);
                    const endDate = new Date(this.orderSearchForm.dateRange[1]);
                    dateMatch = orderDate >= startDate && orderDate <= endDate;
                }

                return statusMatch && dateMatch;
            });
        },

        // 重置订单搜索
        resetOrderSearch() {
            this.orderSearchForm = {
                status: '',
                dateRange: []
            };
            this.filteredOrders = [...this.orders];
        },

        // 查看订单详情
        viewOrderDetails(order) {
            this.selectedOrder = order;
            this.orderDetailVisible = true;
        },

        // 取消订单
        cancelOrder(order) {
            this.$confirm('确定要取消该订单吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                // 更新订单状态
                const index = this.orders.findIndex(o => o.orderId === order.orderId);
                if (index !== -1) {
                    this.orders[index].status = '已取消';
                    this.filteredOrders = [...this.orders];
                    this.$message.success('订单已取消');
                }
            }).catch(() => {});
        },

        // 前往评价页面
        goToReview(order) {
            this.$router.push({
                path: '/reviews',
                query: {
                    projectId: order.projectId,
                    orderId: order.orderId
                }
            });
        },

        // 处理分页变化
        handlePageChange(page) {
            this.currentPage = page;
        },

        // 获取订单状态对应的类型
        getStatusType(status) {
            switch(status) {
                case '已支付':
                    return 'success';
                case '待支付':
                    return 'warning';
                case '已取消':
                    return 'info';
                case '已完成':
                    return 'primary';
                default:
                    return '';
            }
        },

        // 格式化日期
        formatDate(date) {
            if (!date) return '';

            if (typeof date === 'string') return date;

            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');

            return `${year}-${month}-${day}`;
        }
    }
};
</script>

<style scoped>
.booking {
    padding: 20px;
    width: 100%;
    margin: 0 auto;
    box-sizing: border-box;
}

h2, h3, h4 {
    color: #303133;
    margin-bottom: 20px;
}

h2 {
    text-align: center;
    margin-top: 30px;
}

h4 {
    font-size: 16px;
    margin: 15px 0 10px;
    border-left: 3px solid #1890ff;
    padding-left: 10px;
}

.search-form, .order-search-form {
    margin-bottom: 20px;
    background: #f5f7fa;
    padding: 20px;
    border-radius: 8px;
}

.project-row {
    margin: 0 -10px;
    width: 100%;
}

.project-card {
    margin-bottom: 20px;
    height: 100%;
    display: flex;
    flex-direction: column;
}

.project-image {
    position: relative;
    height: 180px;
    overflow: hidden;
    border-radius: 4px;
    margin-bottom: 15px;
}

.project-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s;
}

.project-card:hover .project-image img {
    transform: scale(1.05);
}

.project-tag {
    position: absolute;
    top: 10px;
    right: 10px;
    padding: 2px 8px;
    background-color: rgba(0, 0, 0, 0.6);
    color: #fff;
    font-size: 12px;
    border-radius: 4px;
}

.project-desc {
    color: #606266;
    margin: 10px 0;
    font-size: 14px;
    line-height: 1.5;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
}

.project-info {
    display: flex;
    justify-content: space-between;
    font-size: 13px;
    color: #909399;
    margin-bottom: 15px;
}

.package-list {
    margin-top: auto;
}

.price {
    color: #ff4d4f;
    font-weight: bold;
}

.total-price {
    font-size: 18px;
    color: #ff4d4f;
    font-weight: bold;
}

.pagination-container {
    text-align: center;
    margin: 30px 0;
}

.order-management {
    margin-top: 40px;
}

.order-search-form {
    background: #f5f7fa;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
}

/* 响应式调整 */
@media (max-width: 1200px) {
    .booking {
        padding: 15px;
    }
}

@media (max-width: 768px) {
    .project-image {
        height: 150px;
    }

    .search-form .el-form-item,
    .order-search-form .el-form-item {
        margin-bottom: 10px;
    }

    .el-form-item {
        margin-right: 0;
        display: block;
        width: 100%;
    }

    .el-form--inline .el-form-item {
        display: block;
        margin-right: 0;
        width: 100%;
    }

    .el-date-editor.el-input,
    .el-date-editor.el-input__inner,
    .el-select {
        width: 100%;
    }
}

@media (max-width: 576px) {
    .booking {
        padding: 10px;
    }

    h2 {
        font-size: 20px;
    }

    h3 {
        font-size: 16px;
    }

    .el-dialog {
        width: 90% !important;
        margin-top: 10vh !important;
    }
}
</style>