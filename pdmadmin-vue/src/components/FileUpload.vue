<template>
    <div class="clearfix">
      <el-upload :file-list="fileList" multiple :list-type="listType" :on-preview="handlePreview" class="avatar-uploader"
        :class="{ disabled: maxLimit }" :http-request="handleUpload" :on-remove="handleRemove"
        :before-upload="beforeUpload" :on-change="handleChange">
        <template v-if="listType === 'picture-card'">
  
          <div class="upload-text">
            <el-icon>
              <Plus />
            </el-icon>
            <div> {{ buttonText }}</div>
  
          </div>
        </template>
        <template v-if="listType === 'text'">
          <el-button link type="primary"> {{ buttonText }}</el-button>
        </template>
      </el-upload>
      <el-dialog :footer="null" v-model="previewVisible" @cancel="handleCancel">
        <img :src="previewUrl" alt="预览图片" style="width: 100%" />
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { computed, ref, watch } from 'vue';
  import { Plus } from '@element-plus/icons-vue'
  import { ElLoading, ElMessageBox, ElMessage } from 'element-plus'
  import { commonApi } from '@/api/common-api';
  const props = defineProps({
    value: String,
    //按钮文字
    buttonText: {
      type: String,
      default: '点击上传附件',
    },
    //是否显示按钮
    showUploadBtn: {
      type: Boolean,
      default: true,
    },
    //默认文件列表
    defaultFileList: {
      type: Array,
      default: () => [],
    },
    //是否可以上传多个文件
    multiple: {
      type: Boolean,
      default: false,
    },
    // 最多上传文件数量
    maxUploadSize: {
      type: Number,
      default: 10,
    },
    //单个文件最大值
    maxSize: {
      type: Number,
      default: 100,
    },
    // 上传的文件类型
    accept: {
      type: String,
      default: '',
    },
    // 文件列表的类型 'text' | 'picture' | 'picture-card'
    listType: {
      type: String,
      default: 'picture-card',
    },
  });
  
  // 图片类型的后缀名
  const imgFileType = ['jpg', 'jpeg', 'png', 'gif'];
  
  // 重新修改图片展示字段
  const files = computed(() => {
    let res = [];
    if (props.defaultFileList && props.defaultFileList.length > 0) {
      props.defaultFileList.forEach((element) => {
        // element.url = element.url;
        // element.name = element.name;
        res.push(element);
  
      });
      maxLimit.value = false;
      if (props.defaultFileList.length >= props.maxUploadSize) {
        maxLimit.value = true;
      }
      return res;
    }
    return res;
  });
  const previewVisible = ref(false);
  const maxLimit = ref(false);
  const fileList = ref([]);
  const previewUrl = ref('');
  watch(
    files,
    (value) => {
      fileList.value = value;
    },
    {
      immediate: true,
    }
  );
  /**
   * 定义emit函数，用于组件内部向父组件传递事件。
   */
  const emit = defineEmits(['update:value', 'change']);
  //文件上传
  const handleUpload = async (options) => {
     // 初始化加载动画，提高用户界面反馈
    const loading = ElLoading.service({
      lock: true,
      text: '处理中，请稍后',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      // 创建FormData对象，用于提交文件
      const formData = new FormData();
      formData.append('file', options.file);
       // 调用后端API上传文件
      let res = await commonApi.uploadFile(formData);
      // 处理返回的文件信息，准备添加到文件列表
      let file = res.data;
      file.url = file.url;
      file.filePath = file.filePath;
      // 将新文件添加到文件列表
      fileList.value.push(file);
      //触发change事件，更新父组件的状态
      emit('change', fileList.value);
    } catch (e) {
      ElMessage.error(e.toString());
    } finally {
      loading.close();
    }
  };
  
  
  function handleChange(file, files) {
    maxLimit.value = false;
    if (files.length >= props.maxUploadSize) {
      maxLimit.value = true;
    }
  }
  /**
   * 删除文件
   * @param file
   * @param files
   */
  function handleRemove(file, files) {
    fileList.value = files;
    maxLimit.value = false;
    if (files.length >= props.maxUploadSize) {
      maxLimit.value = true;
    }
    //触发change事件，更新父组件的状态
    emit('change', fileList.value);
  }
  
  function getFileSuffix(file) {
    const suffixIndex = file.name.lastIndexOf('.') + 1;
    const fileSuffix = file.name.substring(suffixIndex <= -1 ? 0 : suffixIndex);
    return fileSuffix;
  }
  
  function beforeUpload(file, files) {
    if (!files) {
      files = [];
    }
    if (fileList.value.length + files.length > props.maxUploadSize) {
      showErrorMsg(`最多支持上传 ${props.maxUploadSize} 个文件！`);
      return false;
    }
    if (props.accept) {
  
      const fileSuffix = getFileSuffix(file);
      if (props.accept.indexOf(fileSuffix) === -1) {
        showErrorMsg(`只支持上传 ${props.accept.replaceAll(',', ' ')} 格式的文件`);
        return false;
      }
    }
    const isLimitSize = file.size / 1024 / 1024 < props.maxSize;
    if (!isLimitSize) {
      showErrorMsg(`单个文件大小必须小于 ${props.maxSize} Mb`);
    }
    return isLimitSize;
  }
  
  const showErrorFlag = ref(true);
  const showErrorMsg = (content) => {
    if (showErrorFlag.value) {
      ElMessageBox.alert(content, '警告', {
        confirmButtonText: '确定',
        callback: (action) => {
          showErrorFlag.value = true;
        },
      })
      showErrorFlag.value = false;
    }
  };
  /**
   * 取消预览
   */
  function handleCancel() {
    previewVisible.value = false;
  }
  /**
   * 预览图片
   * @param file
   */ 
  const handlePreview = async (file) => {
  
    if (imgFileType.some((e) => e == getFileSuffix(file))) {
      previewUrl.value = file.url || file.preview;
      previewVisible.value = true;
    } else {
      //downLoadFile(file.fileKey);
    }
  };
  
  // ------------------------ 清空 上传 ------------------------
  function clear() {
    fileList.value = [];
  }
  /**
   * 向父组件暴露的方法
   */
  defineExpose({
    clear,
  });
  </script>
  
  
  
  <style scoped>
  ::v-deep(.disabled .el-upload--picture-card) {
    display: none;
  }
  </style>