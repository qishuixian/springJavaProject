import request from './request'

// 获取图书列表
export const getBooks = (params) => {
  return request({
    url: '/books',
    method: 'get',
    params
  })
}

// 分页获取图书列表
export const getBooksByPage = (params) => {
  return request({
    url: '/books/page',
    method: 'get',
    params
  })
}

// 分页搜索图书
export const searchBooksByPage = (params) => {
  return request({
    url: '/books/page/search',
    method: 'get',
    params
  })
}

// 获取图书详情
export const getBookById = (id) => {
  return request({
    url: `/books/${id}`,
    method: 'get'
  })
}

// 新增图书
export const createBook = (data) => {
  return request({
    url: '/books',
    method: 'post',
    data
  })
}

// 更新图书
export const updateBook = (id, data) => {
  return request({
    url: `/books/${id}`,
    method: 'put',
    data
  })
}

// 删除图书
export const deleteBook = (id) => {
  return request({
    url: `/books/${id}`,
    method: 'delete'
  })
}

