package com.pdmadmin.pdmadmin.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.pdmadmin.pdmadmin.entity.User;
import com.pdmadmin.pdmadmin.response.R;
import com.pdmadmin.pdmadmin.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    @SaCheckLogin
    public R<User> getUserInfo() {
        long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        user.setPassword(null); // 脱敏
        return R.data(user);
    }

    @Operation(summary = "修改用户信息")
    @PostMapping("/update")
    @SaCheckLogin
    public R<Void> update(@RequestBody User user) {
        long userId = StpUtil.getLoginIdAsLong();
        user.setId(userId);
        // 防止修改敏感字段，如用户名
        user.setUsername(null);
        userService.updateById(user);
        return R.success();
    }
}
