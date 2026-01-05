package com.pdmadmin.pdmadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sport_record")
public class SportRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long sportTypeId;
    private String sportType;
    private Integer durationMin;
    private java.math.BigDecimal calories;
    private java.time.LocalDateTime recordTime;
    private String notes;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
