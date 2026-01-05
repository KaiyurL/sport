import { createRouter, createWebHistory } from 'vue-router'
import Constants from '@/utils/constants';
import { getLocalStorage, clearLocalStorage } from '@/utils/utils'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/dashboard',
      component: () => import('../views/Index.vue'),
      children: [
        {
          path: '/dashboard',
          name: 'Dashboard',
          component: () => import('../views/dashboard/Index.vue'),
          meta: { title: '首页' }
        },
        {
          path: '/sport',
          name: 'SportList',
          component: () => import('../views/sport/List.vue'),
          meta: { title: '运动记录' }
        },
        {
          path: '/health',
          name: 'HealthList',
          component: () => import('../views/health/List.vue'),
          meta: { title: '健康数据' }
        },
        {
          path: '/user',
          name: 'UserProfile',
          component: () => import('../views/user/Profile.vue'),
          meta: { title: '个人中心' }
        },
        {
          path: '/admin',
          name: 'Admin',
          component: () => import('../views/admin/List.vue'),
          meta: { title: '管理员管理' }
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue'),
    },
    {
      path: '/admin/login',
      name: 'AdminLogin',
      component: () => import('../views/admin/AdminLogin.vue'),
    },
    // 404 page
    {
        path: '/:pathMatch(.*)*',
        redirect: '/dashboard'
    }
  ],
})

// ----------------------- 路由加载前 -----------------------
router.beforeEach(async (to, from, next) => {
  // 设置标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 个人运动健康系统`;
  }

  // 登录页面不需要验证直接跳转
  if (to.path === Constants.PAGE_ADMIN_LOGIN || to.path === '/admin/login') {
    next();
    return;
  }
  // 验证是否登录有token
  const token = getLocalStorage(Constants.USER_TOKEN);
  if (!token) {
    clearLocalStorage();
    next({ path: Constants.PAGE_ADMIN_LOGIN });
    return;
  }
  const role = getLocalStorage('login_role');
  if (to.path.startsWith('/admin') && role !== 'admin') {
    next({ path: '/dashboard' });
    return;
  }
  next();
});

export default router
