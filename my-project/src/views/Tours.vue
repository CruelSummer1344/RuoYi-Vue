<template>
    <div class="tours">
        <!-- 搜索框 -->
        <div class="search-container">
            <el-input
                v-model="searchQuery"
                placeholder="搜索旅游项目"
                class="search-input"
                clearable
                @keyup.enter.native="handleSearch"
            >
                <el-button slot="append" icon="el-icon-search" @click="handleSearch" />
            </el-input>
        </div>

        <!-- 最近活动 -->
        <el-card class="recent-activities" shadow="hover">
            <h3 slot="header">最近活动</h3>
            <el-carousel height="280px" :interval="5000">
                <el-carousel-item v-for="activity in recentActivities" :key="activity.id">
                    <div class="activity-item" @click="goToActivity(activity.id)">
                        <img :src="activity.image" alt="activity image" class="activity-image" />
                        <p>{{ activity.title }} - {{ activity.date }}</p>
                    </div>
                </el-carousel-item>
            </el-carousel>
        </el-card>

        <!-- 旅游项目列表 -->
        <el-row :gutter="20" class="tour-list">
            <el-col :xs="24" :sm="24" :md="12" :lg="12" v-for="tour in paginatedTours" :key="tour.id">
                <el-card class="tour-card" shadow="hover">
                    <el-row :gutter="15">
                        <el-col :xs="24" :sm="8" :md="8" :lg="8">
                            <img :src="tour.image" alt="tour image" class="tour-image" />
                        </el-col>
                        <el-col :xs="24" :sm="16" :md="16" :lg="16">
                            <div class="tour-content">
                                <h4>{{ tour.title }}</h4>
                                <p class="tour-desc">{{ tour.description }}</p>
                                <p class="tour-price"><strong>价格：</strong>¥{{ tour.price }}</p>
                                <el-select
                                    v-model="tour.selectedPackage"
                                    placeholder="选择套餐"
                                    size="small"
                                    class="package-select"
                                >
                                    <el-option
                                        v-for="pkg in tour.packages"
                                        :key="pkg.id"
                                        :label="pkg.name"
                                        :value="pkg.id"
                                    />
                                </el-select>

                                <div class="action-buttons">
                                    <el-button type="primary" size="small" @click="bookTour(tour)">立即预约</el-button>
                                    <el-button type="success" size="small" plain @click="contactCustomerService">联系客服</el-button>
                                </div>

                                <el-collapse accordion class="tour-comments">
                                    <el-collapse-item title="查看评论" name="comments">
                                        <div v-for="comment in tour.comments" :key="comment.id" class="comment">
                                            <span>{{ comment.user }}:</span> {{ comment.text }}
                                        </div>
                                    </el-collapse-item>
                                </el-collapse>
                            </div>
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>
        </el-row>

        <!-- 分页 -->
        <div class="pagination-container">
            <el-pagination
                background
                layout="prev, pager, next"
                :total="filteredTours.length"
                :page-size="pageSize"
                :current-page.sync="currentPage"
                @current-change="handlePageChange"
            />
        </div>
    </div>
</template>

<script>
export default {
    name: 'Tours',
    data() {
        return {
            searchQuery: '',
            currentPage: 1,
            pageSize: 4, // 增加每页显示数量，以便在两列布局下显示更多项目
            tours: [
                {
                    id: 1,
                    title: '黄河口湿地观鸟之旅',
                    description: '探索黄河口三角洲独特的湿地生态，观赏珍稀鸟类。',
                    price: 200,
                    image: 'https://ts1.tc.mm.bing.net/th/id/R-C.0dcae2d2ced5b5757cc9d8d76783c438?rik=QQvMRj2YBrIToA&riu=http%3a%2f%2fwenhui.whb.cn%2fu%2fcms%2fwww%2f202104%2f24223449s69j.jpg&ehk=IcFbW%2fUU3tfgVkFdWhQd8Flt6QNi7XWBwM9R0Xnoia8%3d&risl=&pid=ImgRaw&r=0?text=湿地观鸟',
                    packages: [
                        { id: 1, name: '单人票' },
                        { id: 2, name: '含酒店套餐 (+500元)' },
                        { id: 3, name: '含餐饮套餐 (+300元)' }
                    ],
                    selectedPackage: null,
                    comments: [
                        { id: 1, user: '张三', text: '鸟类种类丰富，导游讲解很专业！' },
                        { id: 2, user: '李四', text: '景色很美，值得一游。' }
                    ]
                },
                {
                    id: 2,
                    title: '黄河入海口游船体验',
                    description: '乘坐游船，近距离感受黄河入海的壮观景象。',
                    price: 150,
                    image: 'https://ts1.tc.mm.bing.net/th/id/R-C.4dba070a5945e019dd1b15f57e27c3ca?rik=GwdpcDAodG5W%2fA&riu=http%3a%2f%2fimg.redocn.com%2fsheying%2f20141016%2fhuangheruhaikoudeyuchuan_3250825.jpg&ehk=Ai8NXpFua%2bmBO27qmUFcJU%2bLuH6k%2bub2DcWiuYjONng%3d&risl=&pid=ImgRaw&r=0?text=游船体验',
                    packages: [
                        { id: 1, name: '单人票' },
                        { id: 2, name: '含午餐套餐 (+100元)' }
                    ],
                    selectedPackage: null,
                    comments: [
                        { id: 1, user: '王五', text: '游船很平稳，风景震撼！' }
                    ]
                },
                {
                    id: 3,
                    title: '生态摄影之旅',
                    description: '适合摄影爱好者，捕捉三角洲自然美景。',
                    price: 300,
                    image: 'https://ts1.tc.mm.bing.net/th/id/R-C.8d2e190b5a9c72c77dd6832bfc7c8323?rik=ud1eCkygYn2xlg&riu=http%3a%2f%2fnews.southcn.com%2fzl%2fyc%2fcs%2fcontent%2fimages%2fattachement%2fjpg%2fsite4%2f20200120%2f6c4b908e2c6a1f8fb25837.jpg&ehk=dmMrT2BE5NzR34WTtEgpusoqyhfbuEfICc3zzdC4JP8%3d&risl=&pid=ImgRaw&r=0?text=摄影之旅',
                    packages: [
                        { id: 1, name: '单人票' },
                        { id: 2, name: '含专业指导套餐 (+200元)' }
                    ],
                    selectedPackage: null,
                    comments: [
                        { id: 1, user: '赵六', text: '拍到了很多好照片，推荐！' }
                    ]
                },
                {
                    id: 4,
                    title: '黄河文化探索之旅',
                    description: '深入了解黄河文化，参观历史遗迹和博物馆。',
                    price: 280,
                    image: 'https://ts1.tc.mm.bing.net/th/id/R-C.4dba070a5945e019dd1b15f57e27c3ca?rik=GwdpcDAodG5W%2fA&riu=http%3a%2f%2fimg.redocn.com%2fsheying%2f20141016%2fhuangheruhaikoudeyuchuan_3250825.jpg&ehk=Ai8NXpFua%2bmBO27qmUFcJU%2bLuH6k%2bub2DcWiuYjONng%3d&risl=&pid=ImgRaw&r=0',
                    packages: [
                        { id: 1, name: '单人票' },
                        { id: 2, name: '含讲解套餐 (+120元)' }
                    ],
                    selectedPackage: null,
                    comments: [
                        { id: 1, user: '小明', text: '讲解员很专业，学到了很多知识！' }
                    ]
                }
            ],
            recentActivities: [
                { id: 1, title: '湿地保护日', date: '2025-03-15', image: 'https://ts1.tc.mm.bing.net/th/id/R-C.0dcae2d2ced5b5757cc9d8d76783c438?rik=QQvMRj2YBrIToA&riu=http%3a%2f%2fwenhui.whb.cn%2fu%2fcms%2fwww%2f202104%2f24223449s69j.jpg&ehk=IcFbW%2fUU3tfgVkFdWhQd8Flt6QNi7XWBwM9R0Xnoia8%3d&risl=&pid=ImgRaw&r=0?text=活动1' },
                { id: 2, title: '春季观鸟节', date: '2025-04-01', image: 'https://ts1.tc.mm.bing.net/th/id/R-C.8d2e190b5a9c72c77dd6832bfc7c8323?rik=ud1eCkygYn2xlg&riu=http%3a%2f%2fnews.southcn.com%2fzl%2fyc%2fcs%2fcontent%2fimages%2fattachement%2fjpg%2fsite4%2f20200120%2f6c4b908e2c6a1f8fb25837.jpg&ehk=dmMrT2BE5NzR34WTtEgpusoqyhfbuEfICc3zzdC4JP8%3d&risl=&pid=ImgRaw&r=0?text=活动2' }
            ]
        }
    },
    computed: {
        filteredTours() {
            if (!this.searchQuery) return this.tours
            return this.tours.filter(tour =>
                tour.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                tour.description.toLowerCase().includes(this.searchQuery.toLowerCase())
            )
        },
        paginatedTours() {
            const start = (this.currentPage - 1) * this.pageSize
            const end = start + this.pageSize
            return this.filteredTours.slice(start, end)
        }
    },
    methods: {
        handleSearch() {
            this.currentPage = 1
            this.$message.success(`搜索关键词：${this.searchQuery}`)
        },
        handlePageChange(page) {
            this.currentPage = page
        },
        bookTour(tour) {
            const pkg = tour.packages.find(p => p.id === tour.selectedPackage) || tour.packages[0]
            this.$message.success(`已预约：${tour.title} - ${pkg.name}`)
        },
        contactCustomerService() {
            this.$message.info('正在连接客服...')
        },
        goToActivity(activityId) {
            this.$message.info(`跳转到活动详情：${activityId}`)
        }
    }
}
</script>

<style scoped>
.tours {
    padding: 20px;
    width: 100%;
    box-sizing: border-box;
    margin: 0 auto;
}

/* 搜索框 */
.search-container {
    text-align: center;
    margin-bottom: 25px;
    width: 100%;
}
.search-input {
    width: 60%;
    max-width: 500px;
}

/* 最近活动 */
.recent-activities {
    margin-bottom: 30px;
    border-radius: 8px;
    width: 100%;
}
.recent-activities h3 {
    margin: 0;
    font-size: 18px;
    padding: 10px 0;
}
.activity-item {
    text-align: center;
    cursor: pointer;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
}
.activity-image {
    width: 100%;
    height: 230px; /* 增加图片高度 */
    object-fit: cover;
    border-radius: 4px;
}
.activity-item p {
    margin: 15px 0 0;
    font-size: 16px;
    color: #606266;
    font-weight: 500;
}

/* 旅游项目列表 */
.tour-list {
    margin-bottom: 30px;
    width: 100%;
}
.tour-card {
    margin-bottom: 20px;
    border-radius: 8px;
    transition: all 0.3s;
    height: 100%;
}
.tour-card:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-5px);
}
.tour-image {
    width: 100%;
    height: 140px;
    object-fit: cover;
    border-radius: 4px;
}
.tour-content {
    display: flex;
    flex-direction: column;
    height: 100%;
}
.tour-desc {
    font-size: 14px;
    color: #606266;
    margin: 10px 0;
    line-height: 1.5;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
.tour-price {
    font-size: 16px;
    color: #303133;
    margin: 10px 0;
}
.package-select {
    width: 100%;
    max-width: 200px;
    margin: 10px 0;
}
.action-buttons {
    display: flex;
    gap: 10px;
    margin: 15px 0;
}
.tour-comments {
    margin-top: 10px;
}
.comment {
    font-size: 13px;
    color: #909399;
    margin: 5px 0;
}
.comment span {
    font-weight: bold;
    color: #606266;
}

/* 分页 */
.pagination-container {
    text-align: center;
    padding: 20px 0;
    width: 100%;
}

/* 响应式调整 */
@media (max-width: 992px) {
    .action-buttons {
        flex-direction: column;
        gap: 5px;
    }

    .action-buttons .el-button {
        margin-left: 0 !important;
    }
}

@media (max-width: 768px) {
    .search-input {
        width: 90%;
    }

    .activity-image {
        height: 200px;
    }

    .tour-image {
        height: 180px;
        margin-bottom: 10px;
    }
}

@media (max-width: 576px) {
    .tours {
        padding: 10px;
    }

    .activity-image {
        height: 180px;
    }

    .el-button {
        width: 100%;
        margin-bottom: 10px;
    }
}
</style>