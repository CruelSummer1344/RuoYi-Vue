import request from '@/utils/request'

// 查询水样列表
export function listSample(query) {
  return request({
    url: '/sample/sample/list',
    method: 'get',
    params: query
  })
}

// 查询水样详细
export function getSample(sampleId) {
  return request({
    url: '/sample/sample/' + sampleId,
    method: 'get'
  })
}

// 新增水样
export function addSample(data) {
  return request({
    url: '/sample/sample',
    method: 'post',
    data: data
  })
}

// 修改水样
export function updateSample(data) {
  return request({
    url: '/sample/sample',
    method: 'put',
    data: data
  })
}

// 删除水样
export function delSample(sampleId) {
  return request({
    url: '/sample/sample/' + sampleId,
    method: 'delete'
  })
}
