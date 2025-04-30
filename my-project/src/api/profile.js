import request from '@/utils/request'

// 模拟更新接口
export function updateProfile(data) {
    return request({
        url: '/api/profile/update',
        method: 'post',
        data
    });
}