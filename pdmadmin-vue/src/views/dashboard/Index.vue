<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <template #header>
            <div class="card-header">
              <span>总运动时长</span>
              <el-tag type="success">分钟</el-tag>
            </div>
          </template>
          <div class="card-body">
            <span class="num">{{ stats.totalDuration || 0 }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <template #header>
            <div class="card-header">
              <span>总消耗热量</span>
              <el-tag type="warning">千卡</el-tag>
            </div>
          </template>
          <div class="card-body">
            <span class="num">{{ stats.totalCalories || 0 }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <template #header>
            <div class="card-header">
              <span>本周运动次数</span>
              <el-tag type="primary">次</el-tag>
            </div>
          </template>
          <div class="card-body">
            <span class="num">{{ stats.weekCount || 0 }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>运动类型分布</span>
          </template>
          <div id="typeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
       <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>近期体重变化</span>
          </template>
          <div id="weightChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import * as echarts from 'echarts';
import { getStatOverview, getStatByType } from '@/api/sport-api';
import { getHealthList } from '@/api/health-api';

const stats = ref<any>({});

const initTypeChart = (data: any[]) => {
  const chartDom = document.getElementById('typeChart');
  if (!chartDom) return;
  const myChart = echarts.init(chartDom);
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center'
    },
    series: [
      {
        name: '运动类型',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data
      }
    ]
  };
  myChart.setOption(option);
};

const initWeightChart = (data: any[]) => {
     const chartDom = document.getElementById('weightChart');
  if (!chartDom) return;
  const myChart = echarts.init(chartDom);
  // Prepare data: reverse to show timeline left to right if desc
  const sortedData = [...data].sort((a, b) => new Date(a.measureTime).getTime() - new Date(b.measureTime).getTime());
  
  const dates = sortedData.map(item => item.measureTime.substring(0, 10));
  const weights = sortedData.map(item => item.weightKg);

  const option = {
    xAxis: {
      type: 'category',
      data: dates
    },
    yAxis: {
      type: 'value',
      name: 'kg'
    },
    series: [
      {
        data: weights,
        type: 'line',
        smooth: true
      }
    ]
  };
  myChart.setOption(option);
}

const loadData = async () => {
  try {
    // 1. Overview
    const resOverview = await getStatOverview() as any;
    if (resOverview.code === 200) {
      stats.value = resOverview.data;
    }

    // 2. Type Chart
    const resType = await getStatByType() as any;
    if (resType.code === 200) {
      // 假设返回格式 [{type_name: 'Run', total_duration: 100}, ...]
      // 需根据后端实际返回调整
      const chartData = resType.data.map((item: any) => ({
        value: item.totalDuration || item.count, // 这里假设用时长或次数
        name: item.typeName
      }));
      initTypeChart(chartData);
    }

    // 3. Weight Chart (Get health list)
    const resHealth = await getHealthList({ pageNum: 1, pageSize: 10 }) as any; // Get latest 10
    if (resHealth.code === 200) {
        initWeightChart(resHealth.data.records);
    }

  } catch (e) {
    console.error(e);
  }
};

onMounted(() => {
  loadData();
});
</script>

<style scoped lang="less">
.dashboard-container {
  .stat-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .card-body {
      text-align: center;
      padding: 20px 0;
      .num {
        font-size: 32px;
        font-weight: bold;
        color: #409EFF;
      }
    }
  }
  .mt-20 {
    margin-top: 20px;
  }
}
</style>
