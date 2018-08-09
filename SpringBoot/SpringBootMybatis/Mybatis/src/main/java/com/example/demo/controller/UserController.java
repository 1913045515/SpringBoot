package com.example.demo.controller;

import com.example.demo.mapper.UserGradeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author qiang220316
 * @date 2018/8/9
 */

@RestController
public class UserController {

    @Autowired(required = false)
    private UserGradeMapper userGradeMapper;

    @RequestMapping("mybatis")
    public Object selectAllUser(){
       return userGradeMapper.selectAll();
    }
}
