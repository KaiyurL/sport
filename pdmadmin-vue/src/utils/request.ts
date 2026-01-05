import axios, { type AxiosRequestConfig } from 'axios';
import { saveLocalStorage, getLocalStorage, clearLocalStorage } from '@/utils/utils';
import Constants from '@/utils/constants';
import { ElMessage, ElMessageBox } from 'element-plus';

// token的名称 需要和服务器端配置的对应
const TOKEN_NAME = 'pdmtoken';

// 创建axios对象
const request = axios.create({
  baseURL: import.meta.env.VITE_APP_API_URL,
});

// 退出系统
function logout() {
  clearLocalStorage();
  location.href = '/login';
}

//======================================添加请求拦截器====================================================
request.interceptors.request.use(
  (config) => {
    // 添加token
    const token = getLocalStorage(Constants.USER_TOKEN);
    if (token) {
      config.headers[TOKEN_NAME] = token;
      // 兼容Bearer Token格式
      config.headers['Authorization'] = `Bearer ${token}`;
    }else{
        delete config.headers[TOKEN_NAME];
        delete config.headers['Authorization'];
    }
    return config;
  },
  (error: any) => {
    // 对请求错误做什么
    return Promise.reject(error);
  }
)
// ======================================添加响应拦截器====================================================
request.interceptors.response.use(
  (response) => {
    // 根据content-type，判断是否为json数据
    let contentType = response.headers['content-type'] ? response.headers['content-type'] : response.headers['Content-Type'];
    if (contentType.indexOf('application/json') === -1) {
      return Promise.resolve(response);
    }

    // 如果返回的数据类型是Blob
    if (response.data && response.data instanceof Blob) {
      return Promise.reject(response.data);
    }

    const res = response.data;
    if (res.code && res.code !== 200) {
      // `token`过期或者账号已在别处登录
      if (res.code === 11012 || res.code === 11011 || res.code === 401) {
        ElMessage.closeAll();
        ElMessage.error('您没有登录或登录已过期，请重新登录');
        setTimeout(logout, 300);
        return Promise.reject(response);
      }

      // 长时间未操作系统，需要重新登录
      if (res.code === 30001) {
        ElMessageBox.confirm('您需要重新登陆', '确认退出登录', {
          confirmButtonText: '重新登陆',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          logout();
        });
        setTimeout(logout, 3000);
        return Promise.reject(response);
      }

      ElMessage.closeAll();
      if (res.message) {
        ElMessage.error(res.message);
      }
      return Promise.reject(response);
    } else {
      return Promise.resolve(res);
    }
  },
  (error) => {
    // 对响应错误做什么
    if (error.message.indexOf('timeout') !== -1) {
      ElMessage.closeAll();
      ElMessage.error('网络超时');
    } else if (error.message === 'Network Error') {
      ElMessage.closeAll();
      ElMessage.error('网络连接错误');
    } else if (error.message.indexOf('Request') !== -1) {
      ElMessage.closeAll();
      ElMessage.error('网络发生错误');
    }
    return Promise.reject(error);
  },
);

// =======================================通用请求封装====================================================
export const http = (config: AxiosRequestConfig<any>) => {
  return request.request(config);
};

// get请求
export const get = (url: string, params?: any) => {
  return http({ url, method: 'get', params });
};

// post请求
export const post = (url: string, data: {}) => {
  return http({
    data,
    url,
    method: 'post',
  });
};
