<template>
  <div class="sport-list-container">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>运动记录</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增记录</el-button>
        </div>
      </template>

      <!-- Search/Filter -->
      <div class="filter-container">
        <el-select v-model="queryParams.sportTypeId" placeholder="运动类型" clearable style="width: 150px; margin-right: 10px;" @change="handleFilter">
          <el-option label="跑步" :value="1" />
          <el-option label="游泳" :value="2" />
          <el-option label="骑行" :value="3" />
          <el-option label="健身" :value="4" />
        </el-select>
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
        <el-table-column prop="sportType" label="运动类型" width="120" />
        <el-table-column prop="recordTime" label="开始时间" width="180">
          <template #default="scope">
            {{ scope.row.recordTime ? scope.row.recordTime.replace('T', ' ') : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="durationMin" label="时长(分)" width="100" />
        <el-table-column prop="calories" label="消耗(千卡)" width="100" />
        <el-table-column prop="notes" label="备注" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
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

    <!-- Dialog -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="运动类型" prop="sportTypeId">
          <el-select v-model="form.sportTypeId" placeholder="请选择" @change="handleTypeChange">
            <el-option label="跑步" :value="1" />
            <el-option label="游泳" :value="2" />
            <el-option label="骑行" :value="3" />
            <el-option label="健身" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择时间" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
         <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择时间" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="消耗热量" prop="calories">
          <el-input-number v-model="form.calories" :min="0" />
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input type="textarea" v-model="form.notes" />
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
import { getSportList, addSportRecord, updateSportRecord, deleteSportRecord } from '@/api/sport-api';
import dayjs from 'dayjs';

const loading = ref(false);
const tableData = ref([]);
const total = ref(0);
const dialogVisible = ref(false);
const dialogTitle = ref('新增记录');
const formRef = ref();

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  sportTypeId: undefined,
  dateRange: [] as string[],
  startDate: '',
  endDate: ''
});

const form = reactive({
  id: undefined,
  sportTypeId: undefined,
  sportType: '',
  startTime: '',
  endTime: '',
  calories: 0,
  notes: ''
});

const rules = {
  sportTypeId: [{ required: true, message: '请选择运动类型', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
};

const handleTypeChange = (val: number) => {
  const options: Record<number, string> = { 1: '跑步', 2: '游泳', 3: '骑行', 4: '健身' };
  form.sportType = options[val] || '';
};

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
  queryParams.sportTypeId = undefined;
  queryParams.dateRange = [];
  queryParams.startDate = '';
  queryParams.endDate = '';
  handleFilter();
};

const getList = async () => {
  loading.value = true;
  try {
    const res = await getSportList(queryParams) as any;
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
  dialogTitle.value = '新增记录';
  form.id = undefined;
  form.sportTypeId = undefined;
  form.sportType = '';
  form.startTime = '';
  form.endTime = '';
  form.calories = 0;
  form.notes = '';
  dialogVisible.value = true;
};

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑记录';
  form.id = row.id;
  form.sportTypeId = row.sportTypeId;
  form.sportType = row.sportType;
  form.calories = row.calories;
  form.notes = row.notes;
  
  // Convert recordTime and durationMin to startTime and endTime
  if (row.recordTime) {
    form.startTime = row.recordTime.replace('T', ' ');
    if (row.durationMin) {
      form.endTime = dayjs(form.startTime).add(row.durationMin, 'minute').format('YYYY-MM-DD HH:mm:ss');
    } else {
        form.endTime = form.startTime;
    }
  }
  
  dialogVisible.value = true;
};

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确认删除该记录吗?', '提示', {
    type: 'warning'
  }).then(async () => {
    const res = await deleteSportRecord(row.id) as any;
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
      const start = dayjs(form.startTime);
      const end = dayjs(form.endTime);
      const durationMin = end.diff(start, 'minute');
      
      if (durationMin < 0) {
        ElMessage.error('结束时间不能早于开始时间');
        return;
      }

      const params = {
        id: form.id,
        sportTypeId: form.sportTypeId,
        sportType: form.sportType,
        recordTime: form.startTime.replace(' ', 'T'),
        durationMin: durationMin,
        calories: form.calories,
        notes: form.notes
      };

      const api = form.id ? updateSportRecord : addSportRecord;
      const res = await api(params) as any;
      if (res.code === 200) {
        ElMessage.success(form.id ? '修改成功' : '新增成功');
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
