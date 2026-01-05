package com.pdmadmin.pdmadmin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pdmadmin.pdmadmin.entity.User;
import com.pdmadmin.pdmadmin.response.R;
import com.pdmadmin.pdmadmin.service.UserService;
import com.pdmadmin.pdmadmin.util.CaptchaCache;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;
    
    @Resource
    private CaptchaCache captchaCache;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    @CrossOrigin
    public R<Void> register(@RequestBody User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        if (userService.count(queryWrapper) > 0) {
            return R.error("用户名已存在");
        }
        userService.save(user);
        return R.success("注册成功");
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    @CrossOrigin
    public R<Map<String, Object>> login(@RequestBody User user) {
        // 校验验证码（可选，暂时跳过或者复用Admin的逻辑）
         if (user.getCaptchaId() != null && user.getCaptchaCode() != null) {
             if (!captchaCache.validateCaptcha(user.getCaptchaId(), user.getCaptchaCode())) {
                 return R.error("验证码错误");
             }
         }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        queryWrapper.eq(User::getPassword, user.getPassword());
        User dbUser = userService.getOne(queryWrapper);
        
        if (dbUser == null) {
            return R.error("用户名或密码错误");
        }
        
        StpUtil.login(dbUser.getId());
        
        Map<String, Object> map = new HashMap<>();
        map.put("token", StpUtil.getTokenValue());
        map.put("uid", dbUser.getId());
        map.put("username", dbUser.getUsername());
        map.put("nickname", dbUser.getNickname());
        
        return R.data(map);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    @CrossOrigin
    public R<Void> logout() {
        StpUtil.logout();
        return R.success();
    }
}
