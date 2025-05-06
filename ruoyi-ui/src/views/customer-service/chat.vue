<template>
  <div class="app-container">
    <div class="chat-container">
      <div class="chat-header">
        <h3>在线客服</h3>
      </div>
      <div class="chat-messages" ref="messageContainer">
        <div v-for="(message, index) in messages" :key="index"
             :class="['message', message.userType === 'admin' ? 'message-right' : 'message-left']">
          <div class="message-content">
            <div class="message-sender">{{ message.userType === 'admin' ? '我' : '用户' }}</div>
            <div class="message-text">{{ message.content }}</div>
            <div class="message-time">{{ message.time }}</div>
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
  </div>
</template>

<script>
export default {
  name: 'OnlineService',
  data() {
    return {
      ws: null,
      messages: [],
      inputMessage: '',
      userId: '',
      userType: 'admin'
    };
  },
  created() {
    this.userId = this.$store.state.user.userId;
    this.initWebSocket();
  },
  beforeDestroy() {
    this.closeWebSocket();
  },
  methods: {
    initWebSocket() {
      const wsUrl = `ws://localhost:8080/websocket/chat`;
      this.connectWebSocket(wsUrl, 1);
    },
    connectWebSocket(wsUrl, attempt) {
      console.log(`尝试连接 WebSocket: ${wsUrl} (第 ${attempt} 次)`);
      this.ws = new WebSocket(wsUrl);

      this.ws.onopen = () => {
        console.log('WebSocket连接已建立');
        this.ws.send(JSON.stringify({
          userId: this.userId,
          userType: this.userType,
          type: 'connect'
        }));
      };

      this.ws.onmessage = (event) => {
        console.log(event)
        const message = JSON.parse(event.data);
        this.messages.push(message);
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      };

      this.ws.onclose = (event) => {
        console.log('WebSocket连接已关闭', event.code, event.reason);
        if (attempt <= 5) {
          setTimeout(() => {
            this.connectWebSocket(wsUrl, attempt + 1);
          }, 3000);
        } else {
          console.error('WebSocket连接失败，已达到最大重试次数');
        }
      };

      this.ws.onerror = (error) => {
        console.error('WebSocket错误:', error);
      };
    },
    sendMessage() {
      if (!this.inputMessage.trim()) return;
      const message = {
        userId: this.userId,
        userType: this.userType,
        content: this.inputMessage,
        time: new Date().toLocaleTimeString(),
        type: 'message'
      };
      this.messages.push(message);
      this.ws.send(JSON.stringify(message));
      this.inputMessage = '';
    },
    closeWebSocket() {
      if (this.ws) {
        this.ws.close();
      }
    },
    scrollToBottom() {
      const container = this.$refs.messageContainer;
      container.scrollTop = container.scrollHeight;
    }
  }
};
</script>

<style scoped>
.app-container {
  height: calc(100vh - 84px);
  padding: 20px;
}

.chat-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.chat-header {
  padding: 20px;
  border-bottom: 1px solid #e6e6e6;
}

.chat-header h3 {
  margin: 0;
  color: #303133;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f5f5f5;
}

.message {
  margin-bottom: 20px;
  display: flex;
}

.message-left {
  justify-content: flex-start;
}

.message-right {
  justify-content: flex-end;
}

.message-content {
  max-width: 70%;
  padding: 10px;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.message-right .message-content {
  background: #e3f2fd;
}

.message-sender {
  font-weight: bold;
  margin-bottom: 5px;
  color: #666;
}

.message-text {
  word-break: break-word;
}

.message-time {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
  text-align: right;
}

.chat-input {
  padding: 20px;
  display: flex;
  gap: 10px;
  border-top: 1px solid #e6e6e6;
}

.chat-input .el-button {
  align-self: flex-end;
}
</style>
