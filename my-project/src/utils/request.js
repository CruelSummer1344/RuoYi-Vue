import axios from 'axios';
import {Notification, MessageBox, Message} from 'element-ui';
import store from '@/store';
import {getToken} from '@/utils/auth';
import errorCode from '@/utils/errorCode';
import {tansParams} from '@/utils/ruoyi';
import cache from '@/plugins/cache';
import md5 from 'md5';
import router from '@/router';

export let isRelogin = {show: false};

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';

const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API || 'http://localhost:8080', timeout: 30000
});

const filterDynamicData = (data) => {
    if (!data || data instanceof FormData || data instanceof File) return data;
    try {
        const parsedData = typeof data === 'string' ? JSON.parse(data) : data;
        if (typeof parsedData !== 'object' || parsedData === null) return data;
        delete parsedData.timestamp;
        delete parsedData._t;
        return JSON.stringify(parsedData);
    } catch (e) {
        console.warn('Dynamic data filtering failed:', e);
        return typeof data === 'object' ? JSON.stringify(data) : data;
    }
};

service.interceptors.request.use(config => {
    const isToken = config.headers?.isToken === false;
    const isRepeatSubmit = config.headers?.repeatSubmit === false;

    if (getToken() && !isToken) {
        config.headers['Authorization'] = `Bearer ${getToken()}`;
    }

    if (config.method === 'get' && config.params) {
        let url = config.url + '?' + tansParams(config.params);
        url = url.slice(0, -1);
        config.params = {};
        config.url = url;
    }

    if (!isRepeatSubmit && ['post', 'put'].includes(config.method)) {
        const requestObj = {
            url: config.url, data: filterDynamicData(config.data), time: new Date().getTime()
        };

        const requestSize = new TextEncoder().encode(JSON.stringify(requestObj)).length;
        const limitSize = 5 * 1024 * 1024;
        if (requestSize >= limitSize) {
            console.warn(`[${config.url}]: 请求数据大小超出5M限制`);
            return config;
        }

        const sessionObj = cache.session.getJSON('sessionObj') || {};
        const requestId = md5(`${requestObj.url}:${requestObj.data}`);
        const interval = config.headers?.submitInterval || 3000;

        if (sessionObj[requestId] && requestObj.time - sessionObj[requestId].time < interval) {
            console.warn(`[${requestObj.url}]: 数据正在处理，请勿重复提交`);
            delete sessionObj[requestId];
            cache.session.setJSON('sessionObj', sessionObj);
            return Promise.reject(new Error('数据正在处理，请勿重复提交'));
        }

        sessionObj[requestId] = requestObj;
        cache.session.setJSON('sessionObj', sessionObj);
    }
    return config;
}, error => {
    console.error('Request error:', error);
    return Promise.reject(error);
});

service.interceptors.response.use(res => {
    if (res.config && ['post', 'put'].includes(res.config.method.toLowerCase())) {
        cache.session.remove('sessionObj');
    }

    const code = res.data.code || 200;
    const msg = errorCode[code] || res.data.msg || errorCode['default'];

    if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
        return res.data;
    }

    if (code === 401) {
        if (!isRelogin.show) {
            isRelogin.show = true;
            MessageBox.confirm('登录状态已过期，请重新登录', '系统提示', {
                confirmButtonText: '重新登录', cancelButtonText: '取消', type: 'warning'
            })
                .then(() => {
                    isRelogin.show = false;
                    store.dispatch('LogOut').then(() => {
                        router.push('/login');
                    });
                })
                .catch(() => {
                    isRelogin.show = false;
                });
        }
        return Promise.reject(new Error('登录已过期'));
    } else if (code === 500) {
        Message({message: msg, type: 'error'});
        return Promise.reject(new Error(msg));
    } else if (code === 601) {
        Message({message: msg, type: 'warning'});
        return Promise.reject('error');
    } else if (code !== 200) {
        Notification.error({title: msg});
        return Promise.reject('error');
    }
    return res.data;
}, error => {
    if (error.config && ['post', 'put'].includes(error.config.method.toLowerCase())) {
        cache.session.remove('sessionObj');
    }

    let {message} = error;
    if (message === 'Network Error') {
        message = '后端接口连接异常';
    } else if (message.includes('timeout')) {
        message = '系统接口请求超时';
    } else if (message.includes('Request failed with status code')) {
        message = `系统接口${message.substr(message.length - 3)}异常`;
    }
    Message({message, type: 'error', duration: 5000});
    return Promise.reject(error);
});

export default service;