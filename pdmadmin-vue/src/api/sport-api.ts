import { post, get } from '@/utils/request';

// 运动记录列表
export const getSportList = (params: any) => {
  return get('/api/sport/list', params);
};

// 新增运动记录
export const addSportRecord = (data: any) => {
  return post('/api/sport/add', data);
};

// 修改运动记录
export const updateSportRecord = (data: any) => {
  return post('/api/sport/update', data);
};

// 删除运动记录
export const deleteSportRecord = (id: number) => {
  return post(`/api/sport/delete?id=${id}`, {}); 
};

// 统计概览
export const getStatOverview = () => {
  return get('/api/stat/overview');
};

// 按类型统计
export const getStatByType = () => {
  return get('/api/stat/byType');
};
