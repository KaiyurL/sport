import{get,post} from '@/utils/request'

export const adminApi = { 
    /**
     * 
     * @param param 增加管理员
     * @returns 
     */
    add:(param:any)=>{
        return post('/admin/add',param)
    },
    /**
     * 
     * @param param 修改管理员
     * @returns 
     */
    update:(param:any)=>{
        return post('/admin/update',param)
    },
    /**
     * 
     * @param param 删除管理员
     * @returns 
     */
    delete:(ids:any)=>{
        return post('/admin/del?ids=' + ids,{})
    },

    /**
     * 
     * @param param 获取管理员列表
     * @returns 
     */
     queryPageList:(param:any,pageNum:any,pageSize:any)=>{
        return post('/admin/list?pageNum=' + pageNum + '&pageSize=' + pageSize,param)
    },

    // 登录
    login: (param: any) => {
        return post('/admin/login', param);
    },

    // 退出登录
    loginout: () => {
        return post('/admin/loginout', {});
    }


}
