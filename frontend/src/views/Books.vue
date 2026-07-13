<template>
  <div class="books-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>图书管理系统</h2>
          <div>
            <el-button type="primary" @click="showAddDialog">新增图书</el-button>
            <el-button @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </template>

      <el-table :data="books" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="书名" />
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="status" label="状态" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="showEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="bookForm" :rules="bookRules" ref="bookFormRef" label-width="80px">
        <el-form-item label="书名" prop="title">
          <el-input v-model="bookForm.title" placeholder="请输入书名" />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="bookForm.author" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="bookForm.status" placeholder="请选择状态">
            <el-option label="可借阅" value="AVAILABLE" />
            <el-option label="已借出" value="BORROWED" />
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
import { getBooks, createBook, updateBook, deleteBook } from '../api'
import { removeToken } from '../utils'

const books = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增图书')
const bookFormRef = ref(null)
const currentBookId = ref(null)

const bookForm = reactive({
  title: '',
  author: '',
  status: 'AVAILABLE'
})

const bookRules = {
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const fetchBooks = async () => {
  try {
    const res = await getBooks()
    if (res.code === 200) {
      books.value = res.data
    }
  } catch (error) {
    console.error('获取图书列表失败:', error)
  }
}

const showAddDialog = () => {
  dialogTitle.value = '新增图书'
  currentBookId.value = null
  Object.assign(bookForm, { title: '', author: '', status: 'AVAILABLE' })
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  dialogTitle.value = '编辑图书'
  currentBookId.value = row.id
  Object.assign(bookForm, { title: row.title, author: row.author, status: row.status })
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    await bookFormRef.value.validate()
    const apiFunc = currentBookId.value ? updateBook : createBook
    const params = currentBookId.value ? [currentBookId.value, bookForm] : [bookForm]
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

const handleLogout = () => {
  removeToken()
  window.location.href = '/login'
}

onMounted(() => {
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
</style>
