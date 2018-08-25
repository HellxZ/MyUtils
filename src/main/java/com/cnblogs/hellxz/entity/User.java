package com.cnblogs.hellxz.entity;

import com.cnblogs.hellxz.entity.group.A;
import com.cnblogs.hellxz.entity.group.B;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class User {

    @NotBlank(message = "用户名不能为空串",groups = A.class)
    private String username;
    @NotNull(message = "年龄不能为空",groups = B.class)
    private String age;
    @NotBlank(message = "身高不能为空", groups = {A.class, B.class})
    private String height;
    @NotEmpty(message = "孩子列表不能为空")
    private List<Object> childs;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public List<Object> getChilds() {
        return childs;
    }

    public void setChilds(List<Object> childs) {
        this.childs = childs;
    }
}
