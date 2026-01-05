package com.pdmadmin.pdmadmin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/sayhello")
    public String sayHello(@RequestParam String name){
        return "hello"+name;
    }
}
