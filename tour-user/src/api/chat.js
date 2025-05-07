import request from '@/utils/request'

// 获取客服列表
export function getStaffList() {
  return request({
    url: '/chat/staff/list',
    method: 'get'
  })
}

// 获取客服详情
export function getStaffInfo(staffId) {
  return request({
    url: `/chat/staff/${staffId}`,
    method: 'get'
  })
}

// 获取历史会话记录
export function getChatHistory(params) {
  return request({
    url: '/chat/history',
    method: 'get',
    params
  })
}

// 获取未读消息数量
export function getUnreadCount() {
  return request({
    url: '/chat/unread/count',
    method: 'get'
  })
}

// 标记消息为已读
export function markAsRead(messageIds) {
  return request({
    url: '/chat/messages/read',
    method: 'post',
    data: { messageIds }
  })
}

// 评价客服
export function rateStaff(data) {
  return request({
    url: '/chat/staff/rate',
    method: 'post',
    data
  })
}

// 提交反馈
export function submitFeedback(data) {
  return request({
    url: '/chat/feedback',
    method: 'post',
    data
  })
}

// WebSocket 事件定义
export const WS_EVENTS = {
  // 客户端发送的事件
  CLIENT: {
    // 发送消息
    SEND_MESSAGE: 'message',
    // 结束会话
    END_SESSION: 'endSession',
    // 标记消息已读
    MARK_READ: 'markRead',
    // 评价客服
    RATE_STAFF: 'rateStaff'
  },
  // 服务端发送的事件
  SERVER: {
    // 接收消息
    RECEIVE_MESSAGE: 'message',
    // 客服状态更新
    STAFF_STATUS: 'staffStatus',
    // 会话结束通知
    SESSION_ENDED: 'sessionEnded',
    // 系统通知
    SYSTEM_NOTICE: 'systemNotice'
  }
}

// WebSocket 配置
export const WS_CONFIG = {
  // WebSocket 连接地址
  url: process.env.VUE_APP_WS_URL || 'ws://localhost:8080/chat',
  // 重连次数
  reconnectAttempts: 5,
  // 重连延迟（毫秒）
  reconnectDelay: 3000,
  // 心跳间隔（毫秒）
  heartbeatInterval: 30000,
  // 心跳超时时间（毫秒）
  heartbeatTimeout: 10000
} 