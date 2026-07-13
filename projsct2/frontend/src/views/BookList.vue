<template>
  <div class="booklist-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>图书列表（全部）</h2>
        </div>
      </template>

      <el-table :data="books" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <el-image
              v-if="row.coverImage"
              :src="baseUrl + row.coverImage"
              style="width: 60px; height: 80px; cursor: pointer"
              fit="cover"
              @click="openPreview(baseUrl + row.coverImage)"
            />
            <span v-else style="color: #c0c4cc; font-size: 12px">暂无封面</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="书名" />
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="categoryName" label="分类" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '可借阅' ? 'success' : 'warning'">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="170" />
        <el-table-column prop="updatedAt" label="编辑时间" width="170" />
      </el-table>

      <div class="footer-info">共 {{ books.length }} 本图书</div>
    </el-card>

    <!-- 图片预览弹窗 -->
    <el-dialog v-model="previewVisible" title="封面预览" width="400px">
      <img :src="previewImage" style="width: 100%" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getBooks } from '../api'

const baseUrl = 'http://localhost:8085'
const books = ref([])
const loading = ref(false)
const previewImage = ref('')
const previewVisible = ref(false)

const openPreview = (url) => {
  previewImage.value = url
  previewVisible.value = true
}

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

onMounted(() => {
  fetchBooks()
})
</script>

<style scoped>
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
