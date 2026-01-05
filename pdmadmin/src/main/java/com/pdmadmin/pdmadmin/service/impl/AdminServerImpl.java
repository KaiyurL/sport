package com.pdmadmin.pdmadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdmadmin.pdmadmin.entity.Admin;
import com.pdmadmin.pdmadmin.mapper.AdminMapper;
import com.pdmadmin.pdmadmin.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServerImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
