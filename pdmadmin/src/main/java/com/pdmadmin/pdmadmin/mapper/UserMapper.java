package com.pdmadmin.pdmadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pdmadmin.pdmadmin.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
