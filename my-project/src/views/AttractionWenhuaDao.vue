<template>
  <div class="attraction-detail-flex">
    <div class="main-info">
      <div class="image-section">
        <img :src="mainImage" alt="景区图片" class="main-image" />
      </div>
      <div class="info-section">
        <h2>黄河口文化旅游岛</h2>
        <p>
          黄河口文化旅游岛位于东营市黄河入海口附近，是集黄河文化、渔家风情、生态观光于一体的特色旅游胜地。岛上设有文化展馆、民俗体验区、渔家美食街等，游客可体验黄河文化、品尝地道美食，感受独特的渔家生活。
        </p>
        <el-button type="primary" size="large" @click="reserveDialogVisible = true">立即预约</el-button>
      </div>
    </div>
    <div class="tabs-section">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="游览路线" name="route">
          <ul class="route-list">
            <li v-for="(item, idx) in routes" :key="idx">
              <span class="route-title">{{ item.title }}</span>
              <span class="route-desc">{{ item.desc }}</span>
            </li>
          </ul>
        </el-tab-pane>
        <el-tab-pane label="游客须知" name="notice">
          <div class="notice-content">
            <ul>
              <li>请遵守景区管理规定，文明游览。</li>
              <li>岛上部分区域为湿地，请勿随意采摘动植物。</li>
              <li>注意防晒、防蚊虫，建议穿舒适鞋服。</li>
              <li>如遇恶劣天气，部分项目可能暂停开放。</li>
            </ul>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog
      title="预约提示"
      :visible.sync="reserveDialogVisible"
      width="30%"
      center
    >
      <span>预约成功！我们期待您的到来。</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="reserveDialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'AttractionWenhuaDao',
  data() {
    return {
      mainImage: 'https://img2.baidu.com/it/u=1111111111,2222222222&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=533',
      reserveDialogVisible: false,
      activeTab: 'route',
      routes: [
        { title: '文化体验线', desc: '参观黄河文化展馆-渔家民俗体验-品尝渔家美食' },
        { title: '生态观光线', desc: '湿地观鸟-岛上骑行-观赏黄河入海奇观' },
        { title: '亲子休闲线', desc: '亲子互动区-渔家手工DIY-沙滩娱乐' }
      ]
    }
  }
}
</script>

<style scoped>
.attraction-detail-flex {
  width: 80%;
  margin: 40px auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  padding: 30px;
}
.main-info {
  display: flex;
  align-items: flex-start;
  margin-bottom: 30px;
}
.image-section {
  flex: 0 0 320px;
  margin-right: 30px;
}
.main-image {
  width: 320px;
  height: 220px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.info-section {
  flex: 1;
}
.info-section h2 {
  font-size: 24px;
  color: #1890ff;
  margin-bottom: 15px;
}
.info-section p {
  font-size: 15px;
  color: #333;
  line-height: 1.7;
  margin-bottom: 20px;
}
.tabs-section {
  margin-top: 10px;
}
.route-list {
  padding-left: 0;
  list-style: none;
}
.route-list li {
  margin-bottom: 12px;
  font-size: 15px;
  color: #444;
}
.route-title {
  font-weight: bold;
  color: #1890ff;
  margin-right: 10px;
}
.notice-content ul {
  padding-left: 18px;
}
.notice-content li {
  font-size: 15px;
  color: #666;
  margin-bottom: 8px;
}
</style> 