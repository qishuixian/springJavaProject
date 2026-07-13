<template>
  <div class="profile-container">
    <el-card>
      <template #header>
        <h2>个人信息</h2>
      </template>

      <div class="profile-content">
        <div class="profile-item">
          <el-avatar :size="64" style="background: #409eff; font-size: 28px">
            {{ userInfo?.username?.charAt(0)?.toUpperCase() || 'U' }}
          </el-avatar>
        </div>
        <div class="profile-item">
          <span class="label">用户名：</span>
          <span class="value">{{ userInfo?.username || '-' }}</span>
        </div>
        <div class="profile-item">
          <span class="label">角色：</span>
          <el-tag :type="userInfo?.role === 'ADMIN' ? 'danger' : 'primary'">
            {{ userInfo?.role === 'ADMIN' ? '管理员' : '普通用户' }}
          </el-tag>
        </div>
        <div class="profile-item">
          <span class="label">账户状态：</span>
          <el-tag type="success">正常</el-tag>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getUserInfo } from '../utils'

const userInfo = ref(getUserInfo())
</script>

<style scoped>
.profile-content {
  padding: 20px 0;
}

.profile-item {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.label {
  color: #606266;
  font-size: 14px;
  width: 80px;
}

.value {
  font-size: 14px;
  color: #303133;
}
</style>
