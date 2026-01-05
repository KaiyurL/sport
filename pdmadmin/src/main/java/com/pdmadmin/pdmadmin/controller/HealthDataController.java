package com.pdmadmin.pdmadmin.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pdmadmin.pdmadmin.entity.HealthData;
import com.pdmadmin.pdmadmin.response.R;
import com.pdmadmin.pdmadmin.service.HealthDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "健康数据管理")
@RestController
@RequestMapping("/api/health")
public class HealthDataController {

    @Resource
    private HealthDataService healthDataService;

    @Operation(summary = "新增健康数据")
    @PostMapping("/add")
    @SaCheckLogin
    public R<Void> add(@RequestBody HealthData data) {
        data.setUserId(StpUtil.getLoginIdAsLong());
        if (data.getMeasureTime() == null) {
            data.setMeasureTime(LocalDateTime.now());
        }
        // 计算BMI
        if (data.getHeightCm() != null && data.getWeightKg() != null && data.getHeightCm() > 0) {
             double heightInMeters = data.getHeightCm() / 100.0;
             data.setBmi(data.getWeightKg() / (heightInMeters * heightInMeters));
        }
        healthDataService.save(data);
        return R.success();
    }

    @Operation(summary = "删除健康数据")
    @PostMapping("/delete")
    @SaCheckLogin
    public R<Void> delete(@RequestParam Long id) {
        HealthData data = healthDataService.getById(id);
        if (data == null || !data.getUserId().equals(StpUtil.getLoginIdAsLong())) {
            return R.error("无权操作");
        }
        healthDataService.removeById(id);
        return R.success();
    }

    @Operation(summary = "分页查询健康数据")
    @GetMapping("/list")
    @SaCheckLogin
    public R<Page<HealthData>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String startDate,
                                    @RequestParam(required = false) String endDate) {
        Page<HealthData> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<HealthData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthData::getUserId, StpUtil.getLoginIdAsLong());
        if (startDate != null && !startDate.isEmpty()) {
            queryWrapper.ge(HealthData::getMeasureTime, LocalDateTime.parse(startDate.replace(" ", "T")));
        }
        if (endDate != null && !endDate.isEmpty()) {
            queryWrapper.le(HealthData::getMeasureTime, LocalDateTime.parse(endDate.replace(" ", "T")));
        }
        queryWrapper.orderByDesc(HealthData::getMeasureTime);
        return R.data(healthDataService.page(page, queryWrapper));
    }
}
