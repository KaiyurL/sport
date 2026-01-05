<template>
    <div class="header">
        <div class="header-menu">
            <div class="header-logo">
                <div class="header-title">后台管理</div>
            </div>
            <div class="header-rinfo">
                当前用户：
                <span> {{ admin.name }}  </span>
                <span class="header-exit">
                    <a @click="loginOut()">退出</a>
                </span>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { adminApi } from '@/api/admin-api';
import { useAdminStore } from '@/stores/admin';
import { storeToRefs } from 'pinia';

const adminStore = useAdminStore();
const {admin} = storeToRefs(adminStore);
//退出登录
async function loginOut() {
    try {
        await adminApi.loginout();
    } catch (e) {

    } finally {
        useAdminStore().loginOut();
        location.href="/login";
    }
}
</script>
<style lang="less"></style>