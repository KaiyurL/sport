package com.pdmadmin.pdmadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

//和数据库对应的实体类
@Data
@TableName("admin")
@Schema(description = "管理员信息实体类")
public class Admin {
    /**
     * 主键ID
     * **/
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户名
     * **/
    @Schema(description = "用户名")
    private String username;
    /**
     * 密码
     * **/
    private String userpwd;
    /**
     * 姓名
     * **/
    private String name;
    /**
     * 性别
     * **/

    private String sex;
    /**
     * 电话
     * **/
    private String tel;
    /**
     * 头像
     * **/
    private String headurl;

    /**
     * 登录token
     */
    @Schema(description = "登录token")
    @TableField(exist = false) //不映射数据库字段
    private String token;
    /**
     * 验证码id
     */
    @Schema(description = "验证码ID")
    @TableField(exist = false)
    private String captchaId;

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    @TableField(exist = false)
    private String captchaCode;
}
