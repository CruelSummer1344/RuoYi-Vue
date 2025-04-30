<template>
  <div class="contact">
    <h2 class="title">联系客服</h2>
    <el-card class="chat-container" shadow="hover">
      <div slot="header" class="chat-header">
        <span>在线客服</span>
        <el-tag :type="staffOnline ? 'success' : 'danger'" size="small">
          {{ staffOnline ? '在线' : '离线' }}
        </el-tag>
      </div>
      <div class="chat-messages" ref="messageContainer">
        <div
            v-for="(message, index) in messages"
            :key="index"
            :class="['message', message.from === 'user' ? 'user-message' : 'staff-message']"
        >
          <span class="sender">{{ message.from === 'user' ? '我' : '客服' }}:</span>
          <span class="content">{{ message.text }}</span>
          <span class="time">{{ message.time }}</span>
        </div>
      </div>
      <el-form class="chat-input" @submit.native.prevent="sendMessage">
        <el-input
            v-model="newMessage"
            placeholder="输入消息..."
            :disabled="!staffOnline"
            @keyup.enter.native="sendMessage"
        >
          <el-button slot="append" icon="el-icon-s-promotion" :disabled="!staffOnline" @click="sendMessage">
            发送
          </el-button>
        </el-input>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import io from 'socket.io-client'

export default {
  name: 'Contact',
  data() {
    return {
      socket: null,
      staffOnline: false,
      messages: [], // 仅在前端内存中存储消息
      newMessage: ''
    }
  },
  mounted() {
    this.initSocket()
    this.scrollToBottom()
  },
  beforeDestroy() {
    if (this.socket) {
      this.socket.disconnect()
    }
  },
  methods: {
    initSocket() {
      // 连接 RuoYi-Vue 的 WebSocket 端点
      this.socket = io('http://localhost:8080/chat', {
        transports: ['websocket', 'polling'],
        path: '/socket.io',
        query: {
          token: localStorage.getItem('token') // 使用 RuoYi 的 token（可选）
        }
      })

      this.socket.on('connect', () => {
        this.$message.success('已连接到客服系统')
      })

      this.socket.on('message', (message) => {
        this.messages.push({
          from: 'staff',
          text: message,
          time: this.formatTime(new Date())
        })
        this.$nextTick(() => this.scrollToBottom())
      })

      this.socket.on('staffStatus', (status) => {
        this.staffOnline = status === 'true' || status === true
      })

      this.socket.on('connect_error', () => {
        this.$message.error('连接客服系统失败，请稍后重试')
      })
    },
    sendMessage() {
      if (!this.newMessage.trim() || !this.staffOnline) return
      const message = {
        from: 'user',
        text: this.newMessage,
        time: this.formatTime(new Date())
      }
      this.messages.push(message)
      this.socket.emit('message', this.newMessage)
      this.newMessage = ''
      this.$nextTick(() => this.scrollToBottom())
    },
    formatTime(date) {
      return date.toLocaleTimeString('zh-CN', { hour12: false })
    },
    scrollToBottom() {
      const container = this.$refs.messageContainer
      if (container) {
        container.scrollTop = container.scrollHeight
      }
    }
  }
}
</script>

<style scoped>
.contact {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.title {
  text-align: center;
  margin-bottom: 20px;
  color: #303133;
}

.chat-container {
  border-radius: 8px;
  height: 600px;
  display: flex;
  flex-direction: column;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  color: #303133;
}

.chat-messages {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
  background: #f5f7fa;
  border-bottom: 1px solid #e8e8e8;
}

.message {
  margin: 10px 0;
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
}

.user-message {
  justify-content: flex-end;
}

.staff-message {
  justify-content: flex-start;
}

.sender {
  font-weight: bold;
  color: #606266;
  margin-right: 5px;
}

.content {
  background: #fff;
  padding: 8px 12px;
  border-radius: 4px;
  max-width: 70%;
  word-break: break-word;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.user-message .content {
  background: #409eff;
  color: #fff;
}

.time {
  font-size: 12px;
  color: #909399;
  margin-left: 10px;
  align-self: flex-end;
}

.chat-input {
  padding: 15px;
}

.chat-input .el-input {
  width: 100%;
}
</style>