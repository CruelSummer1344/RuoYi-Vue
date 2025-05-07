import request from '@/utils/request'

// 查询评论列表
export function listComments(query) {
  return request({
    url: '/comments/comments/list',
    method: 'get',
    params: query
  })
}

// 查询评论详细
export function getComments(commentId) {
  return request({
    url: '/comments/comments/' + commentId,
    method: 'get'
  })
}

// 新增评论
export function addComments(data) {
  return request({
    url: '/comments/comments',
    method: 'post',
    data: data
  })
}

// 修改评论
export function updateComments(data) {
  return request({
    url: '/comments/comments',
    method: 'put',
    data: data
  })
}

// 删除评论
export function delComments(commentId) {
  return request({
    url: '/comments/comments/' + commentId,
    method: 'delete'
  })
}
