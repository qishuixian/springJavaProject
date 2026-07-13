<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2>{{ isLogin ? '用户登录' : '用户注册' }}</h2>
      </template>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" style="width: 100%">
            {{ isLogin ? '登录' : '注册' }}
          </el-button>
        </el-form-item>
      </el-form>
      <div class="toggle-link">
        <a @click="toggleMode">{{ isLogin ? '没有账号？去注册' : '已有账号？去登录' }}</a>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { login, register } from '../api'
import { setToken } from '../utils'

const isLogin = ref(true)
const formRef = ref(null)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
  ]
}

const toggleMode = () => {
  isLogin.value = !isLogin.value
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    const apiFunc = isLogin.value ? login : register
    const res = await apiFunc(form)

    if (res.code === 200) {
      ElMessage.success(isLogin.value ? '登录成功' : '注册成功')
      if (isLogin.value && res.data?.token) {
        setToken(res.data.token)
        // 跳转到图书列表页
        window.location.href = '/books'
      } else if (!isLogin.value) {
        // 注册成功后切换到登录模式
        isLogin.value = true
        formRef.value?.resetFields()
      }
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
}

.login-card h2 {
  text-align: center;
  margin: 0;
}

.toggle-link {
  text-align: center;
  margin-top: 10px;
}

.toggle-link a {
  color: #409eff;
  cursor: pointer;
  text-decoration: none;
}

.toggle-link a:hover {
  text-decoration: underline;
}
</style>
