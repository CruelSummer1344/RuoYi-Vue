<template>
    <div class="specialties">
        <!-- 搜索框 -->
        <div class="search-container">
            <el-input
                    v-model="searchQuery"
                    placeholder="搜索周边特色"
                    class="search-input"
                    clearable
                    @keyup.enter.native="handleSearch"
            >
                <el-button slot="append" icon="el-icon-search" @click="handleSearch" />
            </el-input>
        </div>

        <!-- 特色列表 -->
        <el-row :gutter="20" class="specialty-list">
            <el-col :xs="24" :sm="12" :md="8" :lg="8" v-for="item in paginatedSpecialties" :key="item.id">
                <el-card class="specialty-card" shadow="hover">
                    <img :src="item.image" alt="specialty image" class="specialty-image" />
                    <div class="specialty-content">
                        <h4>{{ item.title }}</h4>
                        <p class="specialty-desc">{{ item.description }}</p>
                        <p class="specialty-type"><strong>类型：</strong>{{ item.type }}</p>
                        <el-button type="primary" size="small" @click="viewDetails(item)">查看详情</el-button>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <!-- 空状态提示 -->
        <el-empty
                v-if="filteredSpecialties.length === 0"
                description="没有找到匹配的特色内容"
                :image-size="200">
        </el-empty>

        <!-- 分页 -->
        <div class="pagination-container" v-if="filteredSpecialties.length > 0">
            <el-pagination
                    background
                    layout="prev, pager, next"
                    :total="filteredSpecialties.length"
                    :page-size="pageSize"
                    :current-page.sync="currentPage"
                    @current-change="handlePageChange"
            />
        </div>
    </div>
</template>

<script>
export default {
    name: 'Specialties',
    data() {
        return {
            searchQuery: '',
            currentPage: 1,
            pageSize: 6, // 每页显示6项，三列布局每行3个
            specialties: [
                {
                    id: 1,
                    title: '黄河口蟹黄汤包',
                    description: '选用黄河口新鲜螃蟹，汤汁鲜美，皮薄馅足。',
                    type: '美食',
                    image: 'https://via.placeholder.com/300x200?text=蟹黄汤包'
                },
                {
                    id: 2,
                    title: '三角洲渔家民宿',
                    description: '依水而建，体验渔家生活，环境清幽舒适。',
                    type: '民宿',
                    image: 'https://via.placeholder.com/300x200?text=渔家民宿'
                },
                {
                    id: 3,
                    title: '东营盐焗鸡',
                    description: '传统工艺制作，肉质鲜嫩，咸香入味。',
                    type: '美食',
                    image: 'https://via.placeholder.com/300x200?text=盐焗鸡'
                },
                {
                    id: 4,
                    title: '湿地芦苇小屋',
                    description: '芦苇搭建的生态民宿，融入自然，适合度假。',
                    type: '民宿',
                    image: 'https://via.placeholder.com/300x200?text=芦苇小屋'
                },
                {
                    id: 5,
                    title: '黄河鲤鱼宴',
                    description: '新鲜黄河鲤鱼烹制，口感鲜美，营养丰富。',
                    type: '美食',
                    image: 'https://via.placeholder.com/300x200?text=鲤鱼宴'
                },
                {
                    id: 6,
                    title: '黄河人家客栈',
                    description: '传统建筑风格，提供地道乡村体验。',
                    type: '民宿',
                    image: 'https://via.placeholder.com/300x200?text=黄河人家'
                },
                {
                    id: 7,
                    title: '东营河蟹',
                    description: '黄河口特产，肉质鲜美，蟹黄丰富，营养价值高。',
                    type: '美食',
                    image: 'https://via.placeholder.com/300x200?text=东营河蟹'
                },
                {
                    id: 8,
                    title: '湿地观景木屋',
                    description: '位于湿地保护区边缘，可以近距离观赏湿地风光和鸟类。',
                    type: '民宿',
                    image: 'https://via.placeholder.com/300x200?text=观景木屋'
                }
            ]
        }
    },
    computed: {
        // 过滤后的特色内容
        filteredSpecialties() {
            if (!this.searchQuery) return this.specialties
            return this.specialties.filter(item =>
                item.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                item.description.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                item.type.toLowerCase().includes(this.searchQuery.toLowerCase())
            )
        },
        // 分页后的特色内容
        paginatedSpecialties() {
            const start = (this.currentPage - 1) * this.pageSize
            const end = start + this.pageSize
            return this.filteredSpecialties.slice(start, end)
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
        viewDetails(item) {
            this.$message.info(`查看详情：${item.title}`)
            // 可以跳转到详情页面或弹出对话框
        }
    }
}
</script>

<style scoped>
.specialties {
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

/* 特色列表 */
.specialty-list {
    margin-bottom: 30px;
    width: 100%;
}
.specialty-card {
    margin-bottom: 20px;
    border-radius: 8px;
    overflow: hidden;
    transition: all 0.3s;
    height: 100%;
    display: flex;
    flex-direction: column;
}
.specialty-card:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-5px);
}
.specialty-image {
    width: 100%;
    height: 180px;
    object-fit: cover;
}
.specialty-content {
    padding: 15px;
    display: flex;
    flex-direction: column;
    flex-grow: 1;
}
.specialty-content h4 {
    margin: 0 0 10px;
    font-size: 16px;
    color: #303133;
}
.specialty-desc {
    font-size: 14px;
    color: #606266;
    margin: 0 0 10px;
    line-height: 1.5;
    display: -webkit-box;
    -webkit-line-clamp: 2; /* 限制两行 */
    -webkit-box-orient: vertical;
    overflow: hidden;
    flex-grow: 1;
}
.specialty-type {
    font-size: 14px;
    color: #909399;
    margin: 0 0 15px;
}
.specialty-content .el-button {
    width: 100%;
    margin-top: auto;
}

/* 分页 */
.pagination-container {
    text-align: center;
    padding: 20px 0;
    width: 100%;
}

/* 响应式调整 */
@media (max-width: 768px) {
    .search-input {
        width: 90%;
    }

    .specialty-image {
        height: 160px;
    }

    .specialty-card {
        margin-bottom: 15px;
    }
}

@media (max-width: 576px) {
    .specialties {
        padding: 15px 10px;
    }

    .search-input {
        width: 100%;
    }

    .specialty-image {
        height: 200px;
    }

    .specialty-content {
        padding: 12px;
    }

    .specialty-content h4 {
        font-size: 16px;
    }

    .specialty-desc {
        -webkit-line-clamp: 3; /* 在小屏幕上显示更多内容 */
    }

    .el-pagination {
        padding: 0;
    }
}
</style>