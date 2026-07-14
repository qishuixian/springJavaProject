import request from './request'

// 重新导出 book 模块的所有接口
export * from './book'

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
