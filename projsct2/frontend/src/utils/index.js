/**
 * 存储 token
 */
export const setToken = (token) => {
  localStorage.setItem('token', token)
}

/**
 * 获取 token
 */
export const getToken = () => {
  return localStorage.getItem('token')
}

/**
 * 移除 token
 */
export const removeToken = () => {
  localStorage.removeItem('token')
}

/**
 * 存储用户信息
 */
export const setUserInfo = (userInfo) => {
  localStorage.setItem('userInfo', JSON.stringify(userInfo))
}

/**
 * 获取用户信息
 */
export const getUserInfo = () => {
  const info = localStorage.getItem('userInfo')
  return info ? JSON.parse(info) : null
}

/**
 * 移除用户信息
 */
export const removeUserInfo = () => {
  localStorage.removeItem('userInfo')
}

/**
 * 退出登录 - 清除所有存储
 */
export const logout = () => {
  removeToken()
  removeUserInfo()
}
