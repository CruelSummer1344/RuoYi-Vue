import request from '@/utils/request'

// 查询旅游项目列表
export function listProject(query) {
  return request({
    url: '/projects/projects/list',
    method: 'get',
    params: query
  })
}

// 查询旅游项目详细
export function getProject(projectId) {
  return request({
    url: '/projects/projects/' + projectId,
    method: 'get'
  })
}

// 新增旅游项目
export function addProject(data) {
  return request({
    url: '/projects/projects',
    method: 'post',
    data: data
  })
}

// 修改旅游项目
export function updateProject(data) {
  return request({
    url: '/projects/projects',
    method: 'put',
    data: data
  })
}

// 删除旅游项目
export function delProject(projectId) {
  return request({
    url: '/projects/projects/' + projectId,
    method: 'delete'
  })
} 