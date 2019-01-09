package com.cnblogs.hellxz.controller;

import com.cnblogs.hellxz.entity.User;
import com.cnblogs.hellxz.entity.group.A;
import com.cnblogs.hellxz.entity.group.B;
import com.cnblogs.hellxz.myutils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.*;
import java.util.Set;

/**
 * 校验工具类测试Controller
 *
 * @author Hellxz
 */
@RestController
@RequestMapping("api")
public class ValidateController {

    private static Logger log = LoggerFactory.getLogger(ValidateController.class);

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

    /**
     * 使用BindingResult与@Valid配合的方式
     * 拿到错误信息，带部分堆栈
     */
    @PostMapping("/validationTest6")
    public String validationTest6(@RequestBody @Valid User user, BindingResult result){
        if(result.hasErrors()){
            //取一条错误信息
            ObjectError next = result.getAllErrors().iterator().next();
            log.error("error={}", next);
            //后边可以自己返回错误信息也可以自定义
            return next.toString();
        }
        //do somethings
        return "校验通过";
    }

    /**
     * 使用BindingResult与@Valid配合的方式
     * 只拿到错误信息
     */
    @PostMapping("/validationTest7")
    public String validationTest7(@RequestBody @Valid User user, BindingResult result){
        if(result.hasErrors()){
            //取一条错误信息
            ObjectError next = result.getAllErrors().iterator().next();
            String defaultMessage = next.getDefaultMessage();
            log.error("error={}", defaultMessage);
            //后边可以自己返回错误信息也可以自定义
            return defaultMessage;
        }
        //do somethings
        return "校验通过";
    }

    /**
     * 使用方法7抽的工具类
     */
    @PostMapping("/validationTest8")
    public String validationTest8(@RequestBody @Valid User user, BindingResult result){
        String errorMsg = ValidatorUtils.validEntity(result);
        if(StringUtils.isNotBlank(errorMsg)) return errorMsg;
        //do somethings
        return "校验通过";
    }

    /**
     * 使用Validator未抽取工具类时的实现
     */
    @PostMapping("/validationTest9")
    public String validationTest9(@RequestBody User user){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        if(!validate.isEmpty()){
            ConstraintViolation<User> next = validate.iterator().next();
            String message = next.getMessage();
            log.error("error={}",message);
            return message;
        }
        //do somethings
        return "校验通过";
    }

}
