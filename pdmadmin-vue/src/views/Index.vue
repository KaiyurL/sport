<template>
  <el-container class="layout-container">
    <el-aside width="220px">
      <div class="logo">
        <span>运动健康系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="el-menu-vertical"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item v-if="!isAdmin" index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>首页概览</span>
        </el-menu-item>
        <el-menu-item v-if="!isAdmin" index="/sport">
          <el-icon><Bicycle /></el-icon>
          <span>运动记录</span>
        </el-menu-item>
        <el-menu-item v-if="!isAdmin" index="/health">
          <el-icon><FirstAidKit /></el-icon>
          <span>健康数据</span>
        </el-menu-item>
        <el-menu-item v-if="!isAdmin" index="/user">
          <el-icon><User /></el-icon>
          <span>个人中心</span>
        </el-menu-item>
        <el-menu-item v-if="isAdmin" index="/admin">
          <el-icon><User /></el-icon>
          <span>管理员管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentRouteName }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <el-avatar :size="32" icon="UserFilled" />
              <span class="username">{{ username }}</span>
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Odometer, Bicycle, FirstAidKit, User, ArrowDown, UserFilled } from '@element-plus/icons-vue';
import { clearLocalStorage, getLocalStorage } from '@/utils/utils';
import Constants from '@/utils/constants';

const route = useRoute();
const router = useRouter();
const username = ref('User');
const isAdmin = ref(false);

const activeMenu = computed(() => {
  return route.path;
});

const currentRouteName = computed(() => {
  return route.meta.title || '';
});

onMounted(() => {
  const userInfoStr = getLocalStorage('user_info');
  if (userInfoStr) {
    try {
      const userInfo = JSON.parse(userInfoStr);
      if (userInfo && userInfo.username) {
         username.value = userInfo.username;
      }
    } catch (e) {
      console.error('Parse user info error', e);
      // 清除错误的缓存数据，防止持续报错
      clearLocalStorage();
      router.push('/login');
    }
  }
  isAdmin.value = getLocalStorage('login_role') === 'admin';
});

const handleCommand = (command: string) => {
  if (command === 'logout') {
    clearLocalStorage();
    router.push('/login');
  } else if (command === 'profile') {
    router.push('/user');
  }
};
</script>

<style scoped lang="less">
.layout-container {
  height: 100vh;

  .el-aside {
    background-color: #304156;
    color: #fff;
    display: flex;
    flex-direction: column;

    .logo {
      height: 60px;
      line-height: 60px;
      text-align: center;
      font-size: 20px;
      font-weight: bold;
      background-color: #2b3649;
      color: #fff;
    }

    .el-menu {
      border-right: none;
    }
  }

  .el-header {
    background-color: #fff;
    border-bottom: 1px solid #dcdfe6;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    height: 60px;

    .header-right {
      .el-dropdown-link {
        display: flex;
        align-items: center;
        cursor: pointer;
        color: #606266;

        .username {
          margin: 0 8px;
        }
      }
    }
  }

  .el-main {
    background-color: #f0f2f5;
    padding: 20px;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
