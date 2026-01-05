package com.pdmadmin.pdmadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String nickname;
    private String gender;
    private Integer status;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;

    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String captchaId;
    
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String captchaCode;
}
