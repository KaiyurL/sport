<template>
  <div class="health-list-container">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>健康数据</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增数据</el-button>
        </div>
      </template>

      <!-- Search/Filter -->
      <div class="filter-container">
        <el-date-picker
          v-model="queryParams.dateRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD HH:mm:ss"
          style="margin-right: 10px;"
          @change="handleFilter"
        />
        <el-button type="primary" icon="Search" @click="handleFilter">查询</el-button>
        <el-button icon="Refresh" @click="resetFilter">重置</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="measureTime" label="测量时间" width="180">
            <template #default="scope">
                {{ scope.row.measureTime ? scope.row.measureTime.replace('T', ' ') : '' }}
            </template>
        </el-table-column>
        <el-table-column prop="weightKg" label="体重(kg)" width="120" />
        <el-table-column prop="heightCm" label="身高(cm)" width="120" />
        <el-table-column prop="bmi" label="BMI" width="100">
             <template #default="scope">
                <el-tag :type="getBMIType(scope.row.bmi)">{{ scope.row.bmi }}</el-tag>
            </template>
        </el-table-column>
        <el-table-column prop="heartRateBpm" label="心率(bpm)" width="120" />
        <el-table-column prop="notes" label="备注" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增健康数据" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="测量时间" prop="measureTime">
          <el-date-picker v-model="form.measureTime" type="datetime" placeholder="选择时间" value-format="YYYY-MM-DDTHH:mm:ss" />
        </el-form-item>
        <el-form-item label="体重(kg)" prop="weightKg">
          <el-input-number v-model="form.weightKg" :precision="1" :step="0.1" :max="300" />
        </el-form-item>
        <el-form-item label="身高(cm)" prop="heightCm">
          <el-input-number v-model="form.heightCm" :precision="1" :step="0.1" :max="300" />
        </el-form-item>
        <el-form-item label="心率" prop="heartRateBpm">
          <el-input-number v-model="form.heartRateBpm" :step="1" />
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input v-model="form.notes" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Search, Refresh } from '@element-plus/icons-vue';
import { getHealthList, addHealthData, deleteHealthData } from '@/api/health-api';

const loading = ref(false);
const tableData = ref([]);
const total = ref(0);
const dialogVisible = ref(false);
const formRef = ref();

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  dateRange: [] as string[],
  startDate: '',
  endDate: ''
});

const form = reactive({
  measureTime: '',
  weightKg: undefined,
  heightCm: undefined,
  heartRateBpm: undefined,
  notes: ''
});

const rules = {
  measureTime: [{ required: true, message: '请选择时间', trigger: 'change' }],
  weightKg: [{ required: true, message: '请输入体重', trigger: 'blur' }],
  heightCm: [{ required: true, message: '请输入身高', trigger: 'blur' }]
};

const getBMIType = (bmi: number) => {
    if(!bmi) return 'info';
    if(bmi < 18.5) return 'warning';
    if(bmi < 24) return 'success';
    if(bmi < 28) return 'warning';
    return 'danger';
}

const handleFilter = () => {
  if (queryParams.dateRange && queryParams.dateRange.length === 2) {
    queryParams.startDate = queryParams.dateRange[0] || '';
    queryParams.endDate = queryParams.dateRange[1] || '';
  } else {
    queryParams.startDate = '';
    queryParams.endDate = '';
  }
  queryParams.pageNum = 1;
  getList();
};

const resetFilter = () => {
  queryParams.dateRange = [];
  queryParams.startDate = '';
  queryParams.endDate = '';
  handleFilter();
};

const getList = async () => {
  loading.value = true;
  try {
    const res = await getHealthList(queryParams) as any;
    if (res.code === 200) {
      tableData.value = res.data.records;
      total.value = res.data.total;
    }
  } finally {
    loading.value = false;
  }
};

const handleSizeChange = (val: number) => {
  queryParams.pageSize = val;
  getList();
};

const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val;
  getList();
};

const handleAdd = () => {
  const now = new Date();
  const offset = now.getTimezoneOffset() * 60000;
  form.measureTime = new Date(now.getTime() - offset).toISOString().slice(0, 19);
  form.weightKg = undefined;
  form.heightCm = undefined;
  form.heartRateBpm = undefined;
  form.notes = '';
  dialogVisible.value = true;
};

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确认删除该记录吗?', '提示', {
    type: 'warning'
  }).then(async () => {
    const res = await deleteHealthData(row.id) as any;
    if (res.code === 200) {
      ElMessage.success('删除成功');
      getList();
    }
  });
};

const handleSubmit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      const res = await addHealthData(form) as any;
      if (res.code === 200) {
        ElMessage.success('新增成功');
        dialogVisible.value = false;
        getList();
      }
    }
  });
};

onMounted(() => {
  getList();
});
</script>

<style scoped>
.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.filter-container {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
