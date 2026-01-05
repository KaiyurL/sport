package com.pdmadmin.pdmadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdmadmin.pdmadmin.entity.SportRecord;
import com.pdmadmin.pdmadmin.mapper.SportRecordMapper;
import com.pdmadmin.pdmadmin.service.SportRecordService;
import org.springframework.stereotype.Service;

@Service
public class SportRecordServiceImpl extends ServiceImpl<SportRecordMapper, SportRecord> implements SportRecordService {
}
