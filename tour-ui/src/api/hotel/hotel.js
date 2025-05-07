import request from '@/utils/request'

// 查询酒店表列表
export function listHotel(query) {
  return request({
    url: '/hotel/hotel/list',
    method: 'get',
    params: query
  })
}

// 查询酒店表详细
export function getHotel(hotelId) {
  return request({
    url: '/hotel/hotel/' + hotelId,
    method: 'get'
  })
}

// 新增酒店表
export function addHotel(data) {
  return request({
    url: '/hotel/hotel',
    method: 'post',
    data: data
  })
}

// 修改酒店表
export function updateHotel(data) {
  return request({
    url: '/hotel/hotel',
    method: 'put',
    data: data
  })
}

// 删除酒店表
export function delHotel(hotelId) {
  return request({
    url: '/hotel/hotel/' + hotelId,
    method: 'delete'
  })
}
