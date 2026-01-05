package com.pdmadmin.pdmadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("config")
public class SysConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String cfgKey;
    private String cfgValue;
    private String description;
    private java.time.LocalDateTime updatedAt;
}
