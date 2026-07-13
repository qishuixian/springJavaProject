<template>
  <div class="booklist-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>图书列表（全部）</h2>
          <div>
            <el-button type="primary" @click="goToBooks">分页管理</el-button>
            <el-button @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </template>

      <el-table :data="books" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="书名" />
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="categoryName" label="分类" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === '可借阅' ? 'success' : 'warning'">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div class="footer-info">共 {{ books.length }} 本图书</div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getBooks } from '../api'
import { removeToken } from '../utils'

const router = useRouter()
const books = ref([])
const loading = ref(false)

const fetchBooks = async () => {
  loading.value = true
  try {
    const res = await getBooks()
    if (res.code === 200) {
      books.value = res.data
    }
  } catch (error) {
    console.error('获取图书列表失败:', error)
    ElMessage.error('获取图书列表失败')
  } finally {
    loading.value = false
  }
}

const goToBooks = () => {
  router.push('/books')
}

const handleLogout = () => {
  removeToken()
  router.push('/login')
}

onMounted(() => {
  fetchBooks()
})
</script>

<style scoped>
.booklist-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
}

.footer-info {
  margin-top: 15px;
  text-align: right;
  color: #909399;
  font-size: 14px;
}
</style>
