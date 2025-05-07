import request from '@/utils/request'

// 查询文化列表
export function listPromotions(query) {
  return request({
    url: '/promotions/promotions/list',
    method: 'get',
    params: query
  })
}

// 查询文化详细
export function getPromotions(promotionId) {
  return request({
    url: '/promotions/promotions/' + promotionId,
    method: 'get'
  })
}

// 新增文化
export function addPromotions(data) {
  return request({
    url: '/promotions/promotions',
    method: 'post',
    data: data
  })
}

// 修改文化
export function updatePromotions(data) {
  return request({
    url: '/promotions/promotions',
    method: 'put',
    data: data
  })
}

// 删除文化
export function delPromotions(promotionId) {
  return request({
    url: '/promotions/promotions/' + promotionId,
    method: 'delete'
  })
}
