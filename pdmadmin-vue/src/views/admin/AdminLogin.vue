<template>
  <div class="admin-login-container">
    <div class="admin-login-box">
      <div class="title">管理员登录</div>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item prop="userpwd" label="密码">
          <el-input v-model="form.userpwd" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item prop="captchaCode" label="验证码">
          <div class="captcha-row">
            <el-input v-model="form.captchaCode" placeholder="请输入验证码" />
            <img :src="captchaImage" alt="验证码" class="captcha-img" @click="loadCaptcha" />
          </div>
        </el-form-item>
        <div class="options-row">
          <el-checkbox v-model="remember">记住密码</el-checkbox>
        </div>
        <el-button type="primary" class="w-100" @click="handleLogin" :loading="loading">登录</el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { adminApi } from '@/api/admin-api';
import { commonApi } from '@/api/common-api';
import { saveLocalStorage, getLocalStorage } from '@/utils/utils';
import Constants from '@/utils/constants';
import { useAdminStore } from '@/stores/admin';

const router = useRouter();
const loading = ref(false);
const formRef = ref();
const captchaImage = ref('');
const remember = ref(false);

const form = reactive({
  username: '',
  userpwd: '',
  captchaId: '',
  captchaCode: ''
});

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  userpwd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  captchaCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
};

const loadCaptcha = async () => {
  const res: any = await commonApi.getCaptcha();
  if (res.code === 200) {
    form.captchaId = res.data.captchaId;
    captchaImage.value = res.data.captchaImage;
  }
};

onMounted(() => {
  loadCaptcha();
  const flag = getLocalStorage('admin_remember');
  remember.value = flag === 'true';
  if (remember.value) {
    const cred = getLocalStorage('admin_cred');
    if (cred) {
      try {
        const obj = JSON.parse(cred);
        form.username = obj.username || '';
        form.userpwd = obj.userpwd || '';
      } catch (_) {}
    }
  }
});

const handleLogin = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) return;
    loading.value = true;
    try {
      const res: any = await adminApi.login(form);
      if (res.code === 200) {
        if (remember.value) {
          saveLocalStorage('admin_remember', 'true');
          saveLocalStorage('admin_cred', JSON.stringify({ username: form.username, userpwd: form.userpwd }));
        } else {
          saveLocalStorage('admin_remember', 'false');
          saveLocalStorage('admin_cred', '');
        }
        const store = useAdminStore();
        store.setAdminLoginInfo(res.data);
        saveLocalStorage(Constants.USER_TOKEN, res.data.token);
        saveLocalStorage('login_role', 'admin');
        ElMessage.success('登录成功');
        router.push('/admin');
      } else {
        loadCaptcha();
      }
    } catch (e) {
      loadCaptcha();
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style scoped lang="less">
.admin-login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f7fa;
}
.admin-login-box {
  width: 380px;
  padding: 24px;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}
.title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 16px;
  text-align: center;
}
.w-100 {
  width: 100%;
}
.captcha-row {
  display: flex;
  gap: 10px;
  align-items: center;
}
.captcha-img {
  height: 36px;
  cursor: pointer;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}
.options-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 8px 0 16px;
}
</style>
