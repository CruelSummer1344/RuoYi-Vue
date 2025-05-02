<template>
  <div class="chat-container">
    <div class="chat-header">
      <h2>在线客服</h2>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <template v-else>
        <div class="staff-list" v-if="!currentStaff">
          <div v-if="onlineStaff.length === 0" class="no-staff">
            暂无在线客服
          </div>
          <div v-else v-for="staff in onlineStaff" :key="staff.staffId" 
               class="staff-item" 
               :class="{ 'staff-item-disabled': !staff.online }"
               @click="staff.online ? startChat(staff) : null">
            <img :src="staff.avatar || '/default-avatar.png'" class="staff-avatar">
            <div class="staff-info">
              <div class="staff-name">{{ staff.staffName }}</div>
              <div class="staff-title">{{ staff.title }}</div>
            </div>
            <div class="staff-status" :class="{ online: staff.online, offline: !staff.online }">
              {{ staff.online ? '在线' : '离线' }}
            </div>
          </div>
        </div>
        <div v-else class="current-staff">
          <img :src="currentStaff.avatar || '/default-avatar.png'" class="staff-avatar">
          <div class="staff-info">
            <div class="staff-name">{{ currentStaff.staffName }}</div>
            <div class="staff-title">{{ currentStaff.title }}</div>
          </div>
          <button class="end-chat-btn" @click="endChat">结束对话</button>
        </div>
      </template>
    </div>

    <div class="chat-main" v-if="currentStaff">
      <div class="message-list" ref="messageList">
        <div v-if="messages.length === 0" class="no-messages">
          开始新的对话
        </div>
        <div v-else v-for="(message, index) in messages" :key="index" 
             :class="['message-item', message.senderId === userId ? 'sent' : 'received']">
          <div class="message-content">{{ message.content }}</div>
        </div>
      </div>
      <div class="input-area">
        <textarea v-model="newMessage" placeholder="请输入消息..." @keyup.enter="sendMessage"></textarea>
        <button @click="sendMessage">发送</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { getOnlineStaff } from '@/api/chat'

export default {
  name: 'Contact',
  setup() {
    const socket = ref(null)
    const onlineStaff = ref([])
    const currentStaff = ref(null)
    const messages = ref([])
    const newMessage = ref('')
    const userId = ref(localStorage.getItem('userId'))
    const messageList = ref(null)
    const loading = ref(true)
    const error = ref(null)

    // 初始化WebSocket连接
    const initWebSocket = () => {
      const wsUrl = `ws://${window.location.host}/ws/chat?userId=${userId.value}`
      socket.value = new WebSocket(wsUrl)

      socket.value.onopen = () => {
        console.log('WebSocket连接已建立')
        // 连接成功后重新获取在线客服列表
        fetchOnlineStaff()
      }

      socket.value.onmessage = (event) => {
        try {
          const message = JSON.parse(event.data)
          // 处理不同类型的消息
          switch (message.type) {
            case 'text':
            case 'image':
              messages.value.push(message)
              break
            case 'status':
              // 更新客服在线状态
              updateStaffStatus(message.staffId, message.online)
              break
            case 'error':
              console.error('服务器错误:', message.content)
              break
          }
          nextTick(() => {
            scrollToBottom()
          })
        } catch (e) {
          console.error('消息解析错误:', e)
        }
      }

      socket.value.onclose = () => {
        console.log('WebSocket连接已关闭')
        // 连接关闭后更新所有客服为离线状态
        onlineStaff.value.forEach(staff => {
          staff.online = false
        })
      }

      socket.value.onerror = (error) => {
        console.error('WebSocket错误:', error)
      }
    }

    // 更新客服在线状态
    const updateStaffStatus = (staffId, online) => {
      const staff = onlineStaff.value.find(s => s.staffId === staffId)
      if (staff) {
        staff.online = online
      }
    }

    // 获取在线客服列表
    const fetchOnlineStaff = async () => {
      loading.value = true
      error.value = null
      try {
        const response = await getOnlineStaff()
        if (response && response.data) {
          onlineStaff.value = response.data.map(staff => ({
            ...staff,
            online: true // 从后端获取的客服默认都是在线状态
          }))
        } else {
          onlineStaff.value = []
        }
      } catch (err) {
        console.error('获取在线客服列表失败:', err)
        error.value = '获取客服列表失败，请稍后重试'
        onlineStaff.value = []
      } finally {
        loading.value = false
      }
    }

    // 开始对话
    const startChat = (staff) => {
      if (!staff.online) {
        console.log('客服不在线，无法开始对话')
        return
      }
      currentStaff.value = staff
      messages.value = []
      if (socket.value) {
        try {
          const message = {
            type: 'start',
            staffId: staff.staffId,
            userId: userId.value
          }
          socket.value.send(JSON.stringify(message))
        } catch (e) {
          console.error('发送开始对话消息失败:', e)
        }
      }
    }

    // 结束对话
    const endChat = () => {
      if (socket.value && currentStaff.value) {
        try {
          const message = {
            type: 'end',
            staffId: currentStaff.value.staffId,
            userId: userId.value
          }
          socket.value.send(JSON.stringify(message))
        } catch (e) {
          console.error('发送结束对话消息失败:', e)
        }
      }
      currentStaff.value = null
      messages.value = []
    }

    // 发送消息
    const sendMessage = () => {
      if (!newMessage.value.trim()) {
        return
      }
      if (!socket.value || !currentStaff.value) {
        console.error('未连接到服务器或未选择客服')
        return
      }
      if (!currentStaff.value.online) {
        console.error('客服已离线，无法发送消息')
        return
      }
      try {
        const message = {
          type: 'text',
          content: newMessage.value,
          receiverId: currentStaff.value.staffId,
          senderId: userId.value
        }
        socket.value.send(JSON.stringify(message))
        messages.value.push({
          content: newMessage.value,
          senderId: userId.value
        })
        newMessage.value = ''
        nextTick(() => {
          scrollToBottom()
        })
      } catch (e) {
        console.error('发送消息失败:', e)
      }
    }

    // 滚动到底部
    const scrollToBottom = () => {
      if (messageList.value) {
        messageList.value.scrollTop = messageList.value.scrollHeight
      }
    }

    onMounted(() => {
      initWebSocket()
    })

    onUnmounted(() => {
      if (socket.value) {
        socket.value.close()
      }
    })

    return {
      socket,
      onlineStaff,
      currentStaff,
      messages,
      newMessage,
      userId,
      messageList,
      loading,
      error,
      startChat,
      endChat,
      sendMessage
    }
  }
}
</script>

<style scoped>
.chat-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.chat-header {
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.staff-list {
  margin-top: 15px;
}

.staff-item {
  display: flex;
  align-items: center;
  padding: 10px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.staff-item:hover {
  background-color: #f5f5f5;
}

.staff-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 15px;
}

.staff-info {
  flex: 1;
}

.staff-name {
  font-weight: bold;
  margin-bottom: 4px;
}

.staff-title {
  color: #666;
  font-size: 14px;
}

.staff-status {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
}

.staff-status.online {
  background-color: #e6f7ff;
  color: #1890ff;
}

.staff-status.offline {
  background-color: #f5f5f5;
  color: #999;
}

.current-staff {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.end-chat-btn {
  padding: 6px 12px;
  background-color: #f5f5f5;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.end-chat-btn:hover {
  background-color: #e6e6e6;
}

.chat-main {
  height: 500px;
  display: flex;
  flex-direction: column;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 15px;
  background-color: #f9f9f9;
}

.message-item {
  margin-bottom: 15px;
  max-width: 70%;
}

.message-item.sent {
  margin-left: auto;
}

.message-item.received {
  margin-right: auto;
}

.message-content {
  padding: 10px 15px;
  border-radius: 18px;
  display: inline-block;
}

.message-item.sent .message-content {
  background-color: #1890ff;
  color: white;
}

.message-item.received .message-content {
  background-color: #f0f0f0;
  color: #333;
}

.input-area {
  padding: 15px;
  border-top: 1px solid #eee;
  display: flex;
  align-items: flex-end;
}

.input-area textarea {
  flex: 1;
  height: 60px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: none;
  margin-right: 10px;
}

.input-area button {
  padding: 8px 20px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.input-area button:hover {
  background-color: #40a9ff;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.error {
  text-align: center;
  padding: 20px;
  color: #f56c6c;
}

.no-staff {
  text-align: center;
  padding: 20px;
  color: #666;
}

.no-messages {
  text-align: center;
  padding: 20px;
  color: #666;
}

.staff-item-disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>