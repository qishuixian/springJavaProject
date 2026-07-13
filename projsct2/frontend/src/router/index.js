import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../layout/Layout.vue'
import Books from '../views/Books.vue'
import BookList from '../views/BookList.vue'
import Profile from '../views/Profile.vue'
import { getToken } from '../utils'

const routes = [
  {
    path: '/',
    redirect: '/books'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    meta: { requiresAuth: true },
    children: [
      {
        path: 'books',
        name: 'Books',
        component: Books
      },
      {
        path: 'booklist',
        name: 'BookList',
        component: BookList
      },
      {
        path: 'profile',
        name: 'Profile',
        component: Profile
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = getToken()

  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/books')
  } else {
    next()
  }
})

export default router
