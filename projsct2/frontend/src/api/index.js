import request from './request'

// 用户登录
export const login = (data) => {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 用户注册
export const register = (data) => {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

// 用户注册管理员
export const registerAdmin = (data) => {
  return request({
    url: '/auth/register-admin',
    method: 'post',
    data
  })
}

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

// 获取所有分类
export const getCategories = () => {
  return request({
    url: '/categories',
    method: 'get'
  })
}

// 获取当前用户信息
export const getUserProfile = () => {
  return request({
    url: '/auth/me',
    method: 'get'
  })
}

// 上传封面图片
export const uploadCover = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/files/upload',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
