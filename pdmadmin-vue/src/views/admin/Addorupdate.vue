<template>
    <el-dialog v-model="visible" :title="!form.id ? '新增' : '修改'" width="40%">
        <el-form :rules="rules" :model="form" ref="formRef" label-width="100px">
            <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" :readonly="ureadonly" style="width: 50%;" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" type="password" style="width: 50%;" />
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
                <el-input v-model="form.nickname" style="width: 50%;" />
            </el-form-item>
            <el-form-item label="性别" prop="gender">
                <el-select v-model="form.gender" style="width: 50%;" placeholder="请选择">
                    <el-option label="男" value="男" />
                    <el-option label="女" value="女" />
                </el-select>
            </el-form-item>
            <el-form-item label="电话" prop="phone">
                <el-input v-model="form.phone" style="width: 50%;" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select v-model="form.status" style="width: 50%;" placeholder="请选择">
                    <el-option :label="'启用'" :value="1" />
                    <el-option :label="'禁用'" :value="0" />
                </el-select>
            </el-form-item>


        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="visible = false">取消</el-button>
                <el-button type="primary" @click="onSubmit()" :loading="btnLoading">
                    确定
                </el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup lang="ts">
// @ts-ignore
import { reactive, ref } from 'vue'
import { adminApi } from '../../api/admin-api'
import { ElMessage } from 'element-plus'

// 暴露给父组件的方法
defineExpose({ showModel })

const visible = ref(false);
const btnLoading = ref(false);
const emit = defineEmits(['refresh'])
const ureadonly = ref(false)

function showModel(row: any) {
    if (row) {
        ureadonly.value = true
        Object.assign(form, row)
    } else {
        ureadonly.value = false
        Object.assign(form, formDefault)
        Object.assign(form, formDefault);
    }
    visible.value = true;
}

/**
 * 文件上传值改变后触发的方法，拿到全部上传的文件 获取 url
 * @param fileList
 */
function uploadFileChange(fileList: any) {}

// 表单
const formRef = ref();

// 表单初始数据
const formDefault = {
    id: undefined,
    username: undefined,
    password: undefined,
    nickname: undefined,
    gender: undefined,
    phone: undefined,
    status: 1
}

let form = reactive({ ...formDefault })

const rules = reactive({
    username: [
        {
            required: true,
            message: '请输入用户名',
            trigger: 'blur',
        },
    ],
    userpwd: [
        {
            required: true,
            message: '请输入密码',
            trigger: 'blur',
        },
    ],
    nickname: [
        {
            required: true,
            message: '请输入昵称',
            trigger: 'blur',
        },
    ],
    phone: [
        {
            required: true,
            message: '请输入电话',
            trigger: 'blur',
        },
    ],
    gender: [
        {
            required: true,
            message: '请选择性别',
            trigger: 'blur',
        },
    ],
    status: [
        {
            required: true,
            message: '请选择状态',
            trigger: 'change',
        },
    ],
})
function onSubmit() {
    formRef.value.validate().then(async () => {
        try {
            btnLoading.value = true;
            if (form.id) {
                await adminApi.update(form);
            } else {
                await adminApi.add(form);
            }
            ElMessage.success(`${form.id ? "修改" : "新增"} 成功`);
            btnLoading.value = false;
            visible.value = false;
            emit('refresh');

        } catch (e) {
        } finally {
            btnLoading.value = false;
        }

    })

    console.log('form:', form)

}



</script>
