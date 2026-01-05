import { post, get } from '@/utils/request';

// 用户登录
export const login = (data: any) => {
  return post('/api/auth/login', data);
};

// 用户注册
export const register = (data: any) => {
  return post('/api/auth/register', data);
};

// 获取用户信息
export const getUserInfo = () => {
  return get('/api/user/info');
};

// 修改用户信息
export const updateUserInfo = (data: any) => {
  return post('/api/user/update', data);
};
