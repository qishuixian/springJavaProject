<template>
  <div class="layout-container">
    <!-- 顶部 Header -->
    <div class="header">
      <div class="header-left">
        <div class="logo">
          <span class="logo-icon">B</span>
          <span class="logo-text">图书管理系统</span>
        </div>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <el-avatar :size="32" style="background: #409eff">
              {{ userInfo?.username?.charAt(0)?.toUpperCase() || 'U' }}
            </el-avatar>
            <span class="username">你好，{{ userInfo?.username || '用户' }}</span>
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人信息</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 主体区域 -->
    <div class="main-container">
      <!-- 左侧菜单 -->
      <div class="sidebar" :class="{ 'sidebar-collapsed': isCollapsed }">
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapsed"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
        >
          <el-menu-item index="/books">
            <el-icon><Document /></el-icon>
            <template #title>图书管理</template>
          </el-menu-item>
          <el-menu-item index="/booklist">
            <el-icon><List /></el-icon>
            <template #title>图书列表</template>
          </el-menu-item>
          <el-menu-item index="/profile">
            <el-icon><User /></el-icon>
            <template #title>个人信息</template>
          </el-menu-item>
        </el-menu>
        <div class="collapse-btn" @click="isCollapsed = !isCollapsed">
          <el-icon>
            <Fold v-if="!isCollapsed" />
            <Expand v-else />
          </el-icon>
        </div>
      </div>

      <!-- 右侧内容 -->
      <div class="content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Document, List, ArrowDown, Fold, Expand, User } from '@element-plus/icons-vue'
import { getUserInfo, logout } from '../utils'

const router = useRouter()
const route = useRoute()

const isCollapsed = ref(false)
const userInfo = ref(getUserInfo())

const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  if (command === 'logout') {
    logout()
    router.push('/login')
  } else if (command === 'profile') {
    router.push('/profile')
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  height: 60px;
  background: #fff;
  border-bottom: 1px solid #dcdfe6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 10;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background: #409eff;
  color: #fff;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 18px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.username {
  font-size: 14px;
  color: #606266;
}

.main-container {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.sidebar {
  width: 200px;
  background: #304156;
  display: flex;
  flex-direction: column;
  transition: width 0.3s;
}

.sidebar-collapsed {
  width: 64px;
}

.sidebar .el-menu {
  border-right: none;
  flex: 1;
}

.collapse-btn {
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #bfcbd9;
  border-top: 1px solid #3d4d5f;
}

.collapse-btn:hover {
  color: #409eff;
}

.content {
  flex: 1;
  overflow: auto;
  background: #f0f2f5;
  padding: 20px;
}
</style>
