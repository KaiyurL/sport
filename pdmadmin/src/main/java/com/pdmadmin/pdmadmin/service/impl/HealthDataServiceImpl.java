package com.pdmadmin.pdmadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdmadmin.pdmadmin.entity.HealthData;
import com.pdmadmin.pdmadmin.mapper.HealthDataMapper;
import com.pdmadmin.pdmadmin.service.HealthDataService;
import org.springframework.stereotype.Service;

@Service
public class HealthDataServiceImpl extends ServiceImpl<HealthDataMapper, HealthData> implements HealthDataService {
}
