package com.pdmadmin.pdmadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdmadmin.pdmadmin.entity.SysConfig;
import com.pdmadmin.pdmadmin.mapper.SysConfigMapper;
import com.pdmadmin.pdmadmin.service.SysConfigService;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {
}
