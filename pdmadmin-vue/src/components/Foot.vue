<template>
  <div class="version">
    <a target="_blank" class="copyright" href="http://www.panduoma.com">©2020-{{ currentYear }}智能平台</a>
    <a target="_blank" class="version" href="http://www.panduoma.com">公众号：白日梦</a>
  </div>
</template>

<script setup lang="ts">
import dayjs from 'dayjs';

const currentYear = dayjs().year();
</script>

<style lang="less" scoped>
.version,
.copyright {
  font-size: 14px;
  color: rgba(0, 0, 0, 0.88);
  a:hover {
    color: #0a0e14;
  }
}
</style>