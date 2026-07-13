<template>
  <div class="books-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>图书管理</h2>
          <div>
            <el-button type="primary" @click="showAddDialog">新增图书</el-button>
          </div>
        </div>
      </template>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="请输入书名关键字"
          clearable
          style="width: 300px"
          @clear="handleSearch"
          @keyup.enter="handleSearch"
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>

      <!-- 图书表格 -->
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
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="showEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="bookForm" :rules="bookRules" ref="bookFormRef" label-width="80px">
        <el-form-item label="书名" prop="title">
          <el-input v-model="bookForm.title" placeholder="请输入书名" />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="bookForm.author" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="bookForm.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="bookForm.status" placeholder="请选择状态">
            <el-option label="可借阅" value="可借阅" />
            <el-option label="已借出" value="已借出" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getBooksByPage,
  searchBooksByPage,
  createBook,
  updateBook,
  deleteBook,
  getCategories
} from '../api'

const books = ref([])
const categories = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增图书')
const bookFormRef = ref(null)
const currentBookId = ref(null)
const searchKeyword = ref('')

// 分页参数（前端页码从 1 开始，后端从 0 开始）
const pagination = reactive({
  currentPage: 1,
  pageSize: 5,
  total: 0
})

const bookForm = reactive({
  title: '',
  author: '',
  categoryId: null,
  status: '可借阅'
})

const bookRules = {
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 获取图书列表（分页）
const fetchBooks = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.currentPage - 1, // 后端从 0 开始
      size: pagination.pageSize,
      sortField: 'id',
      sortDirection: 'asc'
    }

    const apiFunc = searchKeyword.value ? searchBooksByPage : getBooksByPage
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }

    const res = await apiFunc(params)
    if (res.code === 200) {
      books.value = res.data.content
      pagination.total = res.data.totalElements
    }
  } catch (error) {
    console.error('获取图书列表失败:', error)
    ElMessage.error('获取图书列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1
  fetchBooks()
}

// 重置搜索
const resetSearch = () => {
  searchKeyword.value = ''
  pagination.currentPage = 1
  fetchBooks()
}

// 每页条数变化
const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  fetchBooks()
}

// 当前页变化
const handleCurrentChange = (page) => {
  pagination.currentPage = page
  fetchBooks()
}

// 获取分类列表
const fetchCategories = async () => {
  try {
    const res = await getCategories()
    if (res.code === 200) {
      categories.value = res.data
      console.log('分类列表加载成功:', categories.value)
    } else {
      ElMessage.error('获取分类列表失败: ' + res.message)
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
    ElMessage.error('获取分类列表失败，请检查后端服务')
  }
}

const showAddDialog = () => {
  dialogTitle.value = '新增图书'
  currentBookId.value = null
  Object.assign(bookForm, { title: '', author: '', categoryId: null, status: '可借阅' })
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  dialogTitle.value = '编辑图书'
  currentBookId.value = row.id
  Object.assign(bookForm, {
    title: row.title,
    author: row.author,
    categoryId: row.categoryId || null,
    status: row.status
  })
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    await bookFormRef.value.validate()

    // 构造请求数据
    const requestData = { ...bookForm }
    if (currentBookId.value) {
      requestData.id = currentBookId.value
    }

    const apiFunc = currentBookId.value ? updateBook : createBook
    const params = currentBookId.value ? [currentBookId.value, requestData] : [requestData]
    const res = await apiFunc(...params)

    if (res.code === 200) {
      ElMessage.success(currentBookId.value ? '更新成功' : '新增成功')
      dialogVisible.value = false
      fetchBooks()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    console.error('保存失败:', error)
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这本图书吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await deleteBook(id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      // 若删除后当前页无数据且不是第一页，则回退一页
      if (books.value.length === 1 && pagination.currentPage > 1) {
        pagination.currentPage--
      }
      fetchBooks()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

onMounted(() => {
  fetchCategories()
  fetchBooks()
})
</script>

<style scoped>
.books-container {
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

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
