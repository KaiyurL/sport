package com.pdmadmin.pdmadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sport_type")
public class SportType {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private java.math.BigDecimal caloriePerHour;
    private String description;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
