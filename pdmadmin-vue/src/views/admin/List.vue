<template>
    <div class="list-container">
        <!-- 搜索表单 -->
        <el-form :inline="true" class="demo-form-inline">
            <el-form-item label="昵称">
                <el-input v-model="queryForm.nickname" placeholder="昵称"></el-input>
            </el-form-item>

            <el-form-item label="电话">
                <el-input v-model="queryForm.phone" placeholder="电话"></el-input>
            </el-form-item>

            <el-form-item>
                <el-button @click="onSearch" type="primary">查询</el-button>
                <el-button @click="add" type="primary">新增</el-button>
                <el-button @click="confirmDel('')" type="danger">批量删除</el-button>
            </el-form-item>
        </el-form>

        <el-divider border-style="dashed"></el-divider>
        <!-- 数据表格 -->
        <el-table border :data="datalist" v-loading="listLoading" style="width: 100%"
            :header-cell-style="{ background: '#f5f7fa' }" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column header-align="center" align="center" prop="username" label="用户名" />
            <el-table-column header-align="center" align="center" prop="nickname" label="昵称" />
            <el-table-column header-align="center" align="center" prop="gender" label="性别" />
            <el-table-column header-align="center" align="center" prop="phone" label="电话" />
            <el-table-column header-align="center" align="center" prop="status" label="状态" />
            <el-table-column header-align="center" align="center" label="操作" width="150">
                <template #default="scope">
                    <el-button type="primary" size="small" @click="update(scope.row)">编辑</el-button>
                    <el-button type="danger" size="small" @click="confirmDel(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination
            v-model:current-page="queryForm.pageNum"
            v-model:page-size="queryForm.pageSize"
            :total="total"
            :page-sizes="[10, 20, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentPageChange"
            style="float: right; margin: 10px 20px 20px 0;"></el-pagination>


        <Addorupdate @refresh="getList" ref="operateRef"></Addorupdate>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { adminApi } from '@/api/admin-api';
import { onMounted } from 'vue';
import Constans from '@/utils/constants';
import Addorupdate from './Addorupdate.vue';
import { ElMessage, ElMessageBox } from 'element-plus'

//列表数据
const datalist = ref([]);
//列表加载状态
const listLoading = ref(false);
//数据总条数
const total = ref(0);
//定义添加或修改组件对象
const operateRef = ref();
//多选数据
const multipeSelection = ref([]);
//表单初始值
const queryFormState = {
    nickname: '',
    phone: '',
    pageNum: 1,
    pageSize: Constans.PAGE_SIZE
};
//查询表单条件对象
const queryForm = reactive({ ...queryFormState });

async function getList() {
    try {

        listLoading.value = true;
        let responseModel = await adminApi.queryPageList(queryForm, queryForm.pageNum, queryForm.pageSize);

        datalist.value = responseModel.data.records;
        total.value = responseModel.data.total;

    } catch (error) {

    } finally {
        listLoading.value = false;
    }
}

//查询方法
function onSearch() {
    queryForm.pageNum = 1;
    getList();
}

//分页方法
function handleCurrentPageChange(val: number) {
    queryForm.pageNum = val;
    getList();
}

function handleSizeChange(val: number) {
    queryForm.pageSize = val;
    queryForm.pageNum = 1;
    getList();
}

//新增方法
function add() {
    operateRef.value.showModel();

}

//修改方法
function update(row: any) {
    operateRef.value.showModel(row);

}

//删除方法
function confirmDel(id: any) {
    ElMessageBox.confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        del(id);
    }).catch(() => {
        ElMessage({
            type: 'info',
            message: '已取消删除'
        });
    })
}

async function del(id: any) {
    try {

        listLoading.value = true;
        let ids = id ? [id] : multipeSelection.value.map((item: any) => item.id);
        if (!ids.length) {
            ElMessage.warning('请选择要删除的数据');
            return;
        }
        await adminApi.delete(ids);
        ElMessage.success('删除成功');
        getList();

    } catch (error) {

    } finally {
        listLoading.value = false;
    }
}

function handleSelectionChange(val: any) {
    multipeSelection.value = val;
}

onMounted(() => {
    getList();
});


</script>
