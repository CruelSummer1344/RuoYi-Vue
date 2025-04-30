import request from '@/utils/request'

// 查询考勤记录列表
export function listRecords(query) {
  return request({
    url: '/records/records/list',
    method: 'get',
    params: query
  })
}

// 查询考勤记录详细
export function getRecords(recordId) {
  return request({
    url: '/records/records/' + recordId,
    method: 'get'
  })
}

// 新增考勤记录
export function addRecords(data) {
  return request({
    url: '/records/records',
    method: 'post',
    data: data
  })
}

// 修改考勤记录
export function updateRecords(data) {
  return request({
    url: '/records/records',
    method: 'put',
    data: data
  })
}

// 删除考勤记录
export function delRecords(recordId) {
  return request({
    url: '/records/records/' + recordId,
    method: 'delete'
  })
}

export function clickSubmit(data) {
  return request({
    url: '/records/records/clickSubmit',
    method: 'post',
    data: data
  })
}
