import request from '@/utils/request'

// 查询景点列表
export function listSpot(query) {
  return request({
    url: '/spot/spot/list',
    method: 'get',
    params: query
  })
}

// 查询景点详细
export function getSpot(spotId) {
  return request({
    url: '/spot/spot/' + spotId,
    method: 'get'
  })
}

// 新增景点
export function addSpot(data) {
  return request({
    url: '/spot/spot',
    method: 'post',
    data: data
  })
}

// 修改景点
export function updateSpot(data) {
  return request({
    url: '/spot/spot',
    method: 'put',
    data: data
  })
}

// 删除景点
export function delSpot(spotId) {
  return request({
    url: '/spot/spot/' + spotId,
    method: 'delete'
  })
}
