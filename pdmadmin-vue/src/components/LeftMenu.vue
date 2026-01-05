<template>
    <div class="pdm-sidebar__inner">
        <el-menu :collapse-transition="false" class="pdm-sidebar__menu" background-color="#1a1a1a"
            active-text-color="#fff" text-color="#8a979e" @open="handleOpen" @close="handleClose">
            <!-- 首页 -->
            <el-menu-item index="welcome" @click="$router.push({ path: '/Index' })">
                <el-icon>
                    <document />
                </el-icon>
                <span slot="title">首页</span>
            </el-menu-item>

            <el-menu-item index="AboutView" @click="$router.push({ path: '/AboutView' })">
                <el-icon>
                    <document />
                </el-icon>
                <span slot="title">首页2</span>
            </el-menu-item>

            <!-- 组织管理 -->
            <el-sub-menu index="管理员信息管理" :popper-class="'pdm-sidebar--dark-popper'">
                <template #title>
                    <el-icon>
                        <document />
                    </el-icon>
                    <span>管理员信息管理</span>
                </template>
                <el-menu-item index="admin" @click="$router.push({ path: '/admin' })">
                    <el-icon>
                        <document />
                    </el-icon>
                    <span>管理员信息管理</span>
                </el-menu-item>
            </el-sub-menu>

            <!-- 部门管理 -->
            <el-sub-menu index="组织管理2" :popper-class="'pdm-sidebar--dark-popper'">
                <template #title>
                    <el-icon>
                        <document />
                    </el-icon>
                    <span>组织管理</span>
                </template>
                <el-menu-item index="MisDept" @click="$router.push({ name: 'MisDept' })">
                    <el-icon>
                        <document />
                    </el-icon>
                    <span>部门管理</span>
                </el-menu-item>
            </el-sub-menu>

            <!-- 可能重复的项，建议去重 -->
            <el-sub-menu index="组织管理3" :popper-class="'pdm-sidebar--dark-popper'">
                <template #title>
                    <el-icon>
                        <document />
                    </el-icon>
                    <span>组织管理</span>
                </template>
                <el-menu-item index="MisDept" @click="$router.push({ name: 'MisDept' })">
                    <el-icon>
                        <document />
                    </el-icon>
                    <span>部门管理</span>
                </el-menu-item>
            </el-sub-menu>

        </el-menu>
    </div>
</template>

<script lang="ts" setup>
import {
    Document,
    Menu as IconMenu,
    Location,
    Setting,
} from '@element-plus/icons-vue'

const handleOpen = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
}

const handleClose = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
}
</script>