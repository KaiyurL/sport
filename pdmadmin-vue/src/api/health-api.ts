import { post, get } from '@/utils/request';

// 健康数据列表
export const getHealthList = (params: any) => {
  return get('/api/health/list', params);
};

// 新增健康数据
export const addHealthData = (data: any) => {
  return post('/api/health/add', data);
};

// 删除健康数据
export const deleteHealthData = (id: number) => {
    // 后端使用@RequestParam接收id，post请求需传参为 params 或 data 为 formData，
    // 这里如果后端用 @RequestParam，Axios POST 需要用 qs.stringify 或 params
    // 为简化，建议后端改为 @RequestBody 或前端传 params
    return post(`/api/health/delete?id=${id}`, {});
};
