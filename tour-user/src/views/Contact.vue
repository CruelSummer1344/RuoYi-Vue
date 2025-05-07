<template>
  <div class="service-container">
    <div class="chat-container">
      <div class="chat-header">
        <h3>在线客服</h3>
      </div>
      <div class="chat-messages" ref="messageContainer">
        <div v-for="(message, index) in messages" :key="index"
             :class="['message', message.userType === 'user' ? 'message-right' : 'message-left']">
          <div class="message-content">
            <div class="message-sender">{{ message.userType === 'user' ? '我' : '客服' }}</div>
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
            class="message-textarea"
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
      userType: 'user'
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
.service-container {
  height: calc(100vh - 84px);
  padding: 40px 20px;
  max-width: 1400px;
  margin: 0 auto;
  background: #f8fafc;
  box-sizing: border-box;
}

.chat-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: box-shadow 0.3s ease;
}

.chat-container:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.chat-header {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
  background: #fff;
}

.chat-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2a44;
  text-align: center;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f8fafc;
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
  padding: 16px;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
}

.message-content:hover {
  transform: translateY(-3px);
}

.message-right .message-content {
  background: #eff6ff;
  border-bottom-right-radius: 4px;
}

.message-left .message-content {
  background: #ffffff;
  border-bottom-left-radius: 4px;
}

.message-sender {
  font-weight: 600;
  margin-bottom: 8px;
  color: #1f2a44;
  font-size: 14px;
}

.message-text {
  word-break: break-word;
  color: #1f2a44;
  line-height: 1.6;
  font-size: 14px;
}

.message-time {
  font-size: 12px;
  color: #64748b;
  margin-top: 8px;
  text-align: right;
}

.chat-input {
  padding: 20px;
  display: flex;
  gap: 16px;
  border-top: 1px solid #f0f0f0;
  background: #fff;
}

.message-textarea {
  width: 100%;
  border-radius: 8px;
}

.message-textarea >>> .el-textarea__inner {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 12px;
  line-height: 1.6;
  resize: none;
}

.chat-input .el-button {
  align-self: flex-end;
  border-radius: 8px;
  height: 46px;
  padding: 0 20px;
  background: #2563eb;
  border-color: #2563eb;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.chat-input .el-button:hover {
  background: #1d4ed8;
  border-color: #1d4ed8;
  transform: translateY(-2px);
}

/* 响应式调整 */
@media (max-width: 992px) {
  .service-container {
    padding: 30px 15px;
  }
}

@media (max-width: 768px) {
  .message-content {
    max-width: 80%;
  }
}

@media (max-width: 576px) {
  .service-container {
    padding: 20px 10px;
    height: calc(100vh - 60px);
  }

  .chat-input {
    padding: 15px;
    flex-direction: column;
    gap: 10px;
  }

  .chat-input .el-button {
    width: 100%;
    align-self: center;
  }

  .message-content {
    max-width: 90%;
  }
}
</style>