<template>
  <div class="chat-container">
    <div class="chat-header">
      <h2>客服聊天室</h2>
    </div>
    <div class="chat-messages" ref="messageContainer">
      <div v-for="(message, index) in messages" :key="message.time + '-' + index" class="message">
        <div class="message-content">
          <div class="message-sender">{{ getSenderDisplayName(message) }}</div>
          <div class="message-text">{{ message.content }}</div>
          <div class="message-time">{{ formatTime(message.time) }}</div>
        </div>
      </div>
    </div>
    <div class="chat-input">
      <el-input
        v-model="inputMessage"
        type="textarea"
        :rows="3"
        placeholder="请输入消息"
        @keyup.enter.native="sendMessage"
      />
      <el-button type="primary" @click="sendMessage">发送</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChatRoom',
  data() {
    return {
      ws: null,
      messages: [],
      inputMessage: '',
      userId: 'user_' + Math.random().toString(36).substr(2, 8),
      senderName: '客服-' + Math.floor(Math.random() * 1000),
      role: 'admin', // 固定角色：admin
      targetRole: 'user' // 指定发送给 user
    }
  },
  methods: {
    formatTime(timestamp) {
      return new Date(parseInt(timestamp)).toLocaleTimeString('zh-CN', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    },
    getSenderDisplayName(message) {
      if (message.sender === this.senderName) {
        return '我';
      }
      return message.sender || '用户';
    },
    sendMessage() {
      if (!this.inputMessage.trim()) return;

      const message = {
        type: 'message',
        content: this.inputMessage,
        sender: this.senderName,
        time: Date.now(),
        role: this.role, // 当前用户角色
        targetRole: this.targetRole // 指定发送给 user
      };

      if (this.ws && this.ws.readyState === WebSocket.OPEN) {
        this.ws.send(JSON.stringify(message));
        this.inputMessage = '';
      } else {
        console.error('WebSocket连接未就绪');
      }
    },
    initWebSocket() {
      this.ws = new WebSocket(`ws://${window.location.host}/websocket/chat`);

      this.ws.onopen = () => {
        console.log('管理端 WebSocket 连接成功！');
      };

      this.ws.onmessage = (event) => {
        try {
          const message = JSON.parse(event.data);
          console.log('管理端收到消息:', message);
          // 只接收来自 user 的消息
          if (message.role === 'user' && message.targetRole === 'admin') {
            this.messages.push(message);
            this.$nextTick(() => {
              this.scrollToBottom();
            });
          }
        } catch (err) {
          console.error('消息解析失败:', err);
        }
      };

      this.ws.onclose = () => {
        console.log('管理端 WebSocket 断开，尝试重连...');
        setTimeout(() => this.initWebSocket(), 3000);
      };
    },
    scrollToBottom() {
      const container = this.$refs.messageContainer;
      if (container) {
        container.scrollTop = container.scrollHeight;
      }
    }
  },
  created() {
    this.initWebSocket();
  },
  beforeDestroy() {
    if (this.ws) {
      this.ws.close();
    }
  }
}
</script>
<style scoped>
.chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 20px;
  box-sizing: border-box;
}

.chat-header {
  text-align: center;
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 4px;
  margin: 10px 0;
}

.message {
  margin-bottom: 15px;
  display: flex;
}

.message-content {
  max-width: 70%;
  padding: 10px;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 1px 2px rgba(0,0,0,0.1);
}

.message-sender {
  font-weight: bold;
  margin-bottom: 5px;
  color: #333;
}

.message-text {
  word-break: break-word;
}

.message-time {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.chat-input {
  display: flex;
  gap: 10px;
  padding: 10px 0;
}

.chat-input .el-button {
  align-self: flex-end;
}
</style>
