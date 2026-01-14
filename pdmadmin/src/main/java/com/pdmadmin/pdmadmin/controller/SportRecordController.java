package com.pdmadmin.pdmadmin.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pdmadmin.pdmadmin.entity.SportRecord;
import com.pdmadmin.pdmadmin.response.R;
import com.pdmadmin.pdmadmin.service.SportRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Tag(name = "运动记录管理")
@RestController
@RequestMapping("/api/sport")
public class SportRecordController {

    @Resource
    private SportRecordService sportRecordService;

    @Operation(summary = "新增运动记录")
    @PostMapping("/add")
    @SaCheckLogin
    public R<Void> add(@RequestBody SportRecord record) {
        record.setUserId(StpUtil.getLoginIdAsLong());
        if (record.getRecordTime() == null) {
            record.setRecordTime(LocalDateTime.now());
        }
        if (record.getDurationMin() == null) {
            record.setDurationMin(0);
        }
        if (hasOverlap(record.getUserId(), record.getRecordTime(), record.getDurationMin(), null)) {
            return R.error("运动记录时间与已有记录重叠");
        }
        sportRecordService.save(record);
        return R.success();
    }

    @Operation(summary = "删除运动记录")
    @PostMapping("/delete")
    @SaCheckLogin
    public R<Void> delete(@RequestParam Long id) {
        // 校验权限：只能删除自己的
        SportRecord record = sportRecordService.getById(id);
        if (record == null || !record.getUserId().equals(StpUtil.getLoginIdAsLong())) {
            return R.error("无权操作");
        }
        sportRecordService.removeById(id);
        return R.success();
    }

    @Operation(summary = "修改运动记录")
    @PostMapping("/update")
    @SaCheckLogin
    public R<Void> update(@RequestBody SportRecord record) {
        SportRecord dbRecord = sportRecordService.getById(record.getId());
        if (dbRecord == null || !dbRecord.getUserId().equals(StpUtil.getLoginIdAsLong())) {
            return R.error("无权操作");
        }
        record.setUserId(StpUtil.getLoginIdAsLong());
        if (record.getRecordTime() == null) {
            record.setRecordTime(dbRecord.getRecordTime());
        }
        if (record.getDurationMin() == null) {
            record.setDurationMin(Objects.requireNonNullElse(dbRecord.getDurationMin(), 0));
        }
        if (hasOverlap(record.getUserId(), record.getRecordTime(), record.getDurationMin(), record.getId())) {
            return R.error("运动记录时间与已有记录重叠");
        }
        sportRecordService.updateById(record);
        return R.success();
    }

    @Operation(summary = "分页查询运动记录")
    @GetMapping("/list")
    @SaCheckLogin
    public R<Page<SportRecord>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) Long sportTypeId,
                                     @RequestParam(required = false) String startDate,
                                     @RequestParam(required = false) String endDate) {
        Page<SportRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SportRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SportRecord::getUserId, StpUtil.getLoginIdAsLong());
        queryWrapper.eq(sportTypeId != null, SportRecord::getSportTypeId, sportTypeId);
        if (startDate != null && !startDate.isEmpty()) {
            queryWrapper.ge(SportRecord::getRecordTime, LocalDateTime.parse(startDate.replace(" ", "T")));
        }
        if (endDate != null && !endDate.isEmpty()) {
            queryWrapper.le(SportRecord::getRecordTime, LocalDateTime.parse(endDate.replace(" ", "T")));
        }
        queryWrapper.orderByDesc(SportRecord::getRecordTime);
        return R.data(sportRecordService.page(page, queryWrapper));
    }

    private boolean hasOverlap(Long userId, LocalDateTime start, Integer durationMin, Long excludeId) {
        LocalDateTime end = start.plusMinutes(durationMin == null ? 0 : durationMin);
        LambdaQueryWrapper<SportRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SportRecord::getUserId, userId);
        wrapper.lt(SportRecord::getRecordTime, end);
        wrapper.apply("DATE_ADD(record_time, INTERVAL duration_min MINUTE) > {0}", start);
        if (excludeId != null) {
            wrapper.ne(SportRecord::getId, excludeId);
        }
        return sportRecordService.count(wrapper) > 0;
    }
}
