package com.pdmadmin.pdmadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("health_data")
public class HealthData {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Double weightKg;
    private Double heightCm;
    private Double bmi;
    private Integer heartRateBpm;
    private java.time.LocalDateTime measureTime;
    private String notes;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
