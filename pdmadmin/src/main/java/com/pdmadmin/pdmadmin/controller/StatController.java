package com.pdmadmin.pdmadmin.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pdmadmin.pdmadmin.entity.SportRecord;
import com.pdmadmin.pdmadmin.response.R;
import com.pdmadmin.pdmadmin.service.SportRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "数据统计")
@RestController
@RequestMapping("/api/stat")
public class StatController {

    @Resource
    private SportRecordService sportRecordService;

    @Operation(summary = "统计运动消耗与时长")
    @GetMapping("/overview")
    @SaCheckLogin
    public R<Map<String, Object>> getOverview() {
        long userId = StpUtil.getLoginIdAsLong();
        
        QueryWrapper<SportRecord> query = new QueryWrapper<>();
        query.select("IFNULL(SUM(duration_min), 0) as totalDuration", "IFNULL(SUM(calories), 0) as totalCalories")
             .eq("user_id", userId);
        
        Map<String, Object> result = sportRecordService.getMap(query);
        return R.data(result);
    }
    
    @Operation(summary = "按运动类型统计")
    @GetMapping("/byType")
    @SaCheckLogin
    public R<List<Map<String, Object>>> getByType() {
        long userId = StpUtil.getLoginIdAsLong();
        // 需联表查询获取类型名称，暂用简单聚合
        QueryWrapper<SportRecord> query = new QueryWrapper<>();
        query.select("sport_type as typeName", "SUM(duration_min) as totalDuration", "SUM(calories) as totalCalories")
             .eq("user_id", userId)
             .groupBy("sport_type");
             
        return R.data(sportRecordService.listMaps(query));
    }
}
