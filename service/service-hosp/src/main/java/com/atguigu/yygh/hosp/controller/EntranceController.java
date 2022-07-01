package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/hosp")
@CrossOrigin
public class EntranceController {

    @PostMapping("/user/login")
    public Result login(){
        HashMap<String, String> map = new HashMap<>();
        map.put("token","admin");
        return Result.ok(map);
    }

    @GetMapping("/user/info")
    public Result userInfo(){
        HashMap<String, String> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }
}
