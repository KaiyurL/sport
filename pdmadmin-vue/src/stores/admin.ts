import { ref, computed, reactive } from 'vue'
import { defineStore } from 'pinia'
import { getLocalStorage,saveLocalStorage,clearLocalStorage } from '@/utils/utils';
import LocalStorageKeyConst from '@/utils/constants';
export const useAdminStore = defineStore('admin', () => {
  const token = ref('');
  const admin = reactive({
      id:'',
      username:'',
      name:'',
      tel:''
  })
   
  const getToken = computed(() => {
    if(token){
        return token;
     }
     return getLocalStorage(LocalStorageKeyConst.USER_TOKEN)
  })


  function setAdminLoginInfo(data : any){
    admin.id = data.id;
    admin.username = data.username;
    admin.name = data.name;
    admin.tel = data.tel;
    
    token.value = data.token;
    console.log("save token",token.value);
    saveLocalStorage(LocalStorageKeyConst.USER_TOKEN,data.token);
  }

  function loginOut(){
    token.value = '';
    admin.id = "";
    admin.username = "";
    admin.name = "";
    admin.tel =  "";
    clearLocalStorage();
  }

 
  return { token, admin,getToken,setAdminLoginInfo,loginOut }
})