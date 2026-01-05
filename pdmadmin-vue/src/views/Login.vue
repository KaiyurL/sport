<template>
  <div class="login-container">
    <div class="login-box">
      <div class="title">个人运动健康系统</div>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="User" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="Lock" show-password />
            </el-form-item>
            <el-button type="primary" class="w-100" @click="handleLogin" :loading="loading">登录</el-button>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <el-form :model="registerForm" :rules="rules" ref="registerFormRef">
            <el-form-item prop="username">
              <el-input v-model="registerForm.username" placeholder="用户名" prefix-icon="User" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="registerForm.password" type="password" placeholder="密码" prefix-icon="Lock" show-password />
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input v-model="registerForm.confirmPassword" type="password" placeholder="确认密码" prefix-icon="Lock" />
            </el-form-item>
             <el-form-item prop="phone">
              <el-input v-model="registerForm.phone" placeholder="手机号" prefix-icon="Iphone" />
            </el-form-item>
            <el-button type="success" class="w-100" @click="handleRegister" :loading="loading">注册</el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { User, Lock, Iphone } from '@element-plus/icons-vue';
import { login, register } from '@/api/user-api';
import { saveLocalStorage } from '@/utils/utils';
import Constants from '@/utils/constants';

const router = useRouter();
const activeTab = ref('login');
const loading = ref(false);
const loginFormRef = ref();
const registerFormRef = ref();

const loginForm = reactive({
  username: '',
  password: ''
});

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  phone: ''
});

const validatePass2 = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致!'));
  } else {
    callback();
  }
};

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [{ validator: validatePass2, trigger: 'blur' }]
};

const handleLogin = async () => {
  if (!loginFormRef.value) return;
  await loginFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      loading.value = true;
      try {
        const res = await login(loginForm) as any;
        if (res.code === 200) {
          saveLocalStorage(Constants.USER_TOKEN, res.data.token);
          saveLocalStorage('user_info', JSON.stringify(res.data));
          saveLocalStorage('login_role', 'user');
          ElMessage.success('登录成功');
          router.push('/');
        }
      } catch (e) {
        // error handled in interceptor
      } finally {
        loading.value = false;
      }
    }
  });
};

const handleRegister = async () => {
  if (!registerFormRef.value) return;
  await registerFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      loading.value = true;
      try {
        const { confirmPassword, ...params } = registerForm;
        const res = await register(params) as any;
        if (res.code === 200) {
          ElMessage.success('注册成功，请登录');
          activeTab.value = 'login';
        }
      } catch (e) {
         // error handled
      } finally {
        loading.value = false;
      }
    }
  });
};
</script>

<style scoped lang="less">
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url('@/assets/images/login/bg.jpg') no-repeat center center;
  background-size: cover;

  .login-box {
    width: 400px;
    padding: 30px;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 8px;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);

    .title {
      text-align: center;
      font-size: 24px;
      font-weight: bold;
      margin-bottom: 20px;
      color: #333;
    }

    .w-100 {
      width: 100%;
    }
  }
}
</style>
