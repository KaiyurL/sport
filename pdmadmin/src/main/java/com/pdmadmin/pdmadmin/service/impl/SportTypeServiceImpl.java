package com.pdmadmin.pdmadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdmadmin.pdmadmin.entity.SportType;
import com.pdmadmin.pdmadmin.mapper.SportTypeMapper;
import com.pdmadmin.pdmadmin.service.SportTypeService;
import org.springframework.stereotype.Service;

@Service
public class SportTypeServiceImpl extends ServiceImpl<SportTypeMapper, SportType> implements SportTypeService {
}
