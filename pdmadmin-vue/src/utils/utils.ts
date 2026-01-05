import Constants from '@/utils/constants';

/**
 * 保存数据到 localStorage
 */
export const saveLocalStorage = (key: string, value: string) => {
  localStorage.setItem(key, value);
};

/**
 * 从 localStorage 获取数据
 */
export const getLocalStorage = (key: string) => {
  return localStorage.getItem(key) || '';
};

/**
 * 清空 localStorage
 */
export const clearLocalStorage = () => {
  localStorage.clear();
};

/**
 * 拼接文件 URL
 */
export const getFileUrl = (url: string) => {
  return Constants.BASE_URL + '/' + url;
};

/**
 * 将多个 URL 转换为文件列表
 */
export const urls2Filelist = (url: any) => {
  const tlist: any = [];
  if (url) {
    url.split(',').map((item: string) => {
      let url = getFileUrl(item);
      const file = {
        name: item,
        url: url
      };
      tlist.push(file);
    });
  }
  return tlist;
};