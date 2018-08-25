package com.cnblogs.hellxz.controller;

import com.cnblogs.hellxz.entity.User;
import com.cnblogs.hellxz.entity.group.A;
import com.cnblogs.hellxz.entity.group.B;
import com.cnblogs.hellxz.myutils.ValidatorUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 校验工具类测试Controller
 */
@RestController
@RequestMapping("api")
public class ValidateController {

    /**
     * 检查User对象注解在A组内的校验
     */
    @PostMapping("/validationTest1")
    public String validationTest1(@RequestBody User user){
        String valid = ValidatorUtils.validEntity(user, A.class);
        if(null != valid) return valid;
        //do somethings
        return "校验通过";
    }

    /**
     * 检查User对象注解在B组内的校验
     */
    @PostMapping("/validationTest2")
    public String validationTest2(@RequestBody User user){
        String valid = ValidatorUtils.validEntity(user, B.class);
        if(null != valid) return valid;
        //do somethings
        return "校验通过";
    }

    /**
     * 检查User对象注解在A组和B组内的校验
     * ps: 是A组内校验加上B组内校验，不是校验同时在两个组！
     */
    @PostMapping("/validationTest3")
    public String validationTest3(@RequestBody User user){
        String valid = ValidatorUtils.validEntity(user, A.class, B.class);
        if(null != valid) return valid;
        //do somethings
        return "校验通过";
    }

    /**
     * 校验入参对象中指定字段
     * 其中分组可以不传,如果传的话,请注意该字段必须在该组下,否则不会被校验
     */
    @PostMapping("/validationTest4")
    public String validationTest4(@RequestBody User user){
        String valid = ValidatorUtils.validProperty(user,"age", B.class);
        if(null != valid) return valid;
        //do somethings
        return "校验通过";
    }

    /**
     * 验证上边Test4的说法
     */
    @PostMapping("/validationTest5")
    public String validationTest5(@RequestBody User user){
        String valid = ValidatorUtils.validProperty(user,"age", A.class);
        if(null != valid) return valid;
        //do somethings
        return "校验通过";
    }
}
