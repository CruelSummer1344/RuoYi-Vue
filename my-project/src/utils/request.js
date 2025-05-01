import axios from 'axios'
import { Notification, MessageBox, Message, Loading } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams } from "@/utils/ruoyi";
import cache from '@/plugins/cache'

// 是否显示重新登录
export let isRelogin = { show: false };

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 10000
})

// 新增：动态字段过滤函数
const filterDynamicData = (data) => {
  try {
    const parsedData = typeof data === 'string' ? JSON.parse(data) : data;
    // 删除示例动态字段（根据实际业务调整）
    delete parsedData.timestamp;
    delete parsedData._t;
    return JSON.stringify(parsedData);
  } catch (e) {
    return typeof data === 'object' ? JSON.stringify(data) : data;
  }
};

// request拦截器
service.interceptors.request.use(config => {
  const isToken = (config.headers || {}).isToken === false
  const isRepeatSubmit = (config.headers || {}).repeatSubmit === false

  if (getToken() && !isToken) {
    config.headers['Authorization'] = 'Bearer ' + getToken()
  }

  if (config.method === 'get' && config.params) {
    let url = config.url + '?' + tansParams(config.params);
    url = url.slice(0, -1);
    config.params = {};
    config.url = url;
  }

  if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
    // 修改点1：使用过滤后的数据
    const requestObj = {
      url: config.url,
      data: filterDynamicData(config.data), // 过滤动态字段
      time: new Date().getTime()
    }

    const requestSize = Object.keys(JSON.stringify(requestObj)).length;
    const limitSize = 5 * 1024 * 1024;
    if (requestSize >= limitSize) {
      console.warn(`[${config.url}]: 请求数据大小超出允许的5M限制，无法进行防重复提交验证。`)
      return config;
    }

    const sessionObj = cache.session.getJSON('sessionObj')

    // 修改点2：放宽时间间隔至3秒
    if (sessionObj) {
      const interval = 3000; // 从1秒改为3秒
      const isSameRequest =
          sessionObj.url === requestObj.url &&
          sessionObj.data === requestObj.data &&
          (requestObj.time - sessionObj.time) < interval

      if (isSameRequest) {
        console.warn(`[${sessionObj.url}]: 数据正在处理，请勿重复提交`)
        return Promise.reject(new Error('数据正在处理，请勿重复提交'))
      }
    }
    cache.session.setJSON('sessionObj', requestObj)
  }
  return config
}, error => {
  console.log(error)
  return Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(
    res => {
      // 修改点3：响应成功后清理缓存
      if (res.config && ['post', 'put'].includes(res.config.method.toLowerCase())) {
        cache.session.remove('sessionObj')
      }

      const code = res.data.code || 200;
      const msg = errorCode[code] || res.data.msg || errorCode['default']

      if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
        return res.data
      }

      if (code === 401) {
        store.dispatch('LogOut').then(() => {
          location.href = '/'
        })
      } else if (code === 500) {
        Message({ message: msg, type: 'error' })
        return Promise.reject(new Error(msg))
      } else if (code === 601) {
        Message({ message: msg, type: 'warning' })
        return Promise.reject('error')
      } else if (code !== 200) {
        Notification.error({ title: msg })
        return Promise.reject('error')
      } else {
        return res.data
      }
    },
    error => {
      // 修改点4：请求失败后也清理缓存
      if (error.config && ['post', 'put'].includes(error.config.method.toLowerCase())) {
        cache.session.remove('sessionObj')
      }

      let { message } = error
      if (message === "Network Error") {
        message = "后端接口连接异常"
      } else if (message.includes("timeout")) {
        message = "系统接口请求超时"
      } else if (message.includes("Request failed with status code")) {
        message = "系统接口" + message.substr(message.length - 3) + "异常"
      }
      Message({ message: message, type: 'error', duration: 5000 })
      return Promise.reject(error)
    }
)

export default service
