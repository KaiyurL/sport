package com.pdmadmin.pdmadmin.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pdmadmin.pdmadmin.entity.Admin;
import com.pdmadmin.pdmadmin.entity.User;
import com.pdmadmin.pdmadmin.entity.HealthData;
import com.pdmadmin.pdmadmin.entity.SportRecord;
import com.pdmadmin.pdmadmin.exception.BusinessException;
import com.pdmadmin.pdmadmin.response.R;
import com.pdmadmin.pdmadmin.response.ResponseCode;
import com.pdmadmin.pdmadmin.service.AdminService;
import com.pdmadmin.pdmadmin.service.UserService;
import com.pdmadmin.pdmadmin.service.HealthDataService;
import com.pdmadmin.pdmadmin.service.SportRecordService;
import com.pdmadmin.pdmadmin.util.CaptchaCache;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Tag(name = "管理员信息管理")
@RestController

public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private CaptchaCache captchaCache;
    @Resource
    private UserService userService;
    @Resource
    private HealthDataService healthDataService;
    @Resource
    private SportRecordService sportRecordService;
    @Operation(summary = "新增用户")
    @PostMapping("/admin/add")
    @CrossOrigin
    @SaCheckLogin
    public R<Void> add(@RequestBody User user){
        Long loginId = StpUtil.getLoginIdAsLong();
        if (adminService.getById(loginId) == null) {
            return R.error("无权操作");
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        if (userService.count(lambdaQueryWrapper) > 0){
            throw new BusinessException(ResponseCode.USERNAME_EXIST);
        }
        userService.save(user);
        return R.success();
    }

    @Operation(summary = "查看用户信息列表")
    @PostMapping("/admin/list")
    @CrossOrigin
    @SaCheckLogin
    public R<Page<User>> List(@RequestBody User user, @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        Long loginId = StpUtil.getLoginIdAsLong();
        if (adminService.getById(loginId) == null) {
            return R.error("无权操作");
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotBlank(user.getNickname()), User::getNickname, user.getNickname());
        lambdaQueryWrapper.like(StringUtils.isNotBlank(user.getPhone()), User::getPhone, user.getPhone());
        lambdaQueryWrapper.orderByDesc(User::getId);
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> resultPage = userService.page(page, lambdaQueryWrapper);
        return R.data(resultPage);
    }



    @Operation(summary = "修改用户信息")
    @PostMapping("/admin/update")
    @CrossOrigin
    @SaCheckLogin
    public R<Void> update(@RequestBody User user){
        Long loginId = StpUtil.getLoginIdAsLong();
        if (adminService.getById(loginId) == null) {
            return R.error("无权操作");
        }
        userService.updateById(user);
        return R.success();
    }

    @Operation(summary = "删除用户")
    @PostMapping("/admin/del")
    @CrossOrigin
    @SaCheckLogin
    @Transactional
    public R<Void> del(@RequestParam List<Long> ids){
        Long loginId = StpUtil.getLoginIdAsLong();
        if (adminService.getById(loginId) == null) {
            return R.error("无权操作");
        }
        healthDataService.remove(new LambdaQueryWrapper<HealthData>().in(HealthData::getUserId, ids));
        sportRecordService.remove(new LambdaQueryWrapper<SportRecord>().in(SportRecord::getUserId, ids));
        userService.removeByIds(ids);
        return R.success();
    }


    /**
     * 管理员登录
     * @return
     */
    @Operation(summary = "管理员登录")
    @PostMapping("/admin/login")
    @CrossOrigin

    public R<Admin> login(@RequestBody Admin admin){
        if (StringUtils.isBlank(admin.getCaptchaId()) || StringUtils.isBlank(admin.getCaptchaCode())) {
            throw new BusinessException(ResponseCode.CAPTCHA_ERROR);
        }
        boolean result = captchaCache.validateCaptcha(admin.getCaptchaId(), admin.getCaptchaCode());
        if (!result) {
            throw new BusinessException(ResponseCode.CAPTCHA_ERROR);
        }
        captchaCache.removeCaptcha(admin.getCaptchaId());
        //根据传入的用户名和密码去数据库查询
        LambdaQueryWrapper<Admin> adminWrapper = new LambdaQueryWrapper<>();
        adminWrapper.eq(Admin::getUsername,admin.getUsername());
        adminWrapper.eq(Admin::getUserpwd,admin.getUserpwd());
        admin = adminService.getOne(adminWrapper);
        //数据库没有数据，就提示用户名密码错误
        if(admin == null){
            throw new BusinessException(ResponseCode.USERNAME_USERPWD_ERROR);
        }
        //根据ID进行登录
        StpUtil.login(admin.getId());
        //获取当前登录会话的token，赋值给admin的token属性
        admin.setToken(StpUtil.getTokenValue());
        return R.data(admin);
    }


    /**
     * 管理员退出登录
     * @return
     */
    @CrossOrigin
    @Operation(summary = "管理员退出登录")
    @PostMapping("/admin/loginout")
    @SaCheckLogin
    public R<Void> loginout(){
        //退出当前会话
        StpUtil.logout();
        return R.success();
    }
}
