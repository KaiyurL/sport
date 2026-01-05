<template>
  <div class="profile-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
        </div>
      </template>
      <el-form :model="form" label-width="100px" v-loading="loading">
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio label="male">男</el-radio>
            <el-radio label="female">女</el-radio>
            <el-radio label="other">其他</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUpdate">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getUserInfo, updateUserInfo } from '@/api/user-api';

const loading = ref(false);
const form = reactive({
  id: undefined,
  username: '',
  nickname: '',
  phone: '',
  gender: ''
});

const loadInfo = async () => {
  loading.value = true;
  try {
    const res = await getUserInfo() as any;
    if (res.code === 200) {
      Object.assign(form, res.data);
    }
  } finally {
    loading.value = false;
  }
};

const handleUpdate = async () => {
  const res = await updateUserInfo(form) as any;
  if (res.code === 200) {
    ElMessage.success('修改成功');
    loadInfo();
  }
};

onMounted(() => {
  loadInfo();
});
</script>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: 20px auto;
}
</style>
