package com.agger.swagger2Demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: User
 * @description: TODO
 * @author: chenhx
 * @create: 2019-11-25 14:20
 **/
@Data
@ApiModel("用户类")
public class User {

    @ApiModelProperty( value= "用户id",required = true,example = "123",notes = "用户id")
    private Long userId;
    @ApiModelProperty(value = "用户姓名",required = true,example = "张三",notes = "用户姓名")
    private String userName;
    @ApiModelProperty(value = "用户年龄",required = true,example = "20",notes = "用户年龄")
    private Integer age;
    @ApiModelProperty(value = "用户邮箱",required = true,example = "123@qq.com",notes = "用户邮箱")
    private String email;
    @ApiModelProperty(value = "用户生日",required = true,example = "2019-11-25T06:58:37.318Z",notes = "用户生日")
    private Date birthday;

    public User() {
    }

    public User(Long userId, String userName, Integer age, String email, Date birthday) {
        this.userId = userId;
        this.userName = userName;
        this.age = age;
        this.email = email;
        this.birthday = birthday;
    }
}
