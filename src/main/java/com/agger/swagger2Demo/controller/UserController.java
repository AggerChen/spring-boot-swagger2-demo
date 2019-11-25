package com.agger.swagger2Demo.controller;

import com.agger.swagger2Demo.vo.ResultVO;
import com.agger.swagger2Demo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: UserController
 * @description: TODO
 * @author: chenhx
 * @create: 2019-11-25 14:20
 **/
@Api("用户管理")
@RestController
@RequestMapping("user")
public class UserController {

    private final static List<UserVO> userList = new ArrayList<>();
    {
        userList.add(new UserVO(1L,"张三",20,"aaaa@qq.com",new Date()));
        userList.add(new UserVO(2L,"李四",25,"bbbb@qq.com",new Date()));
    }

    @ApiOperation("查询用户列表")
    @GetMapping("list")
    public ResultVO<List<UserVO>> getUserList(){

        ResultVO result = new ResultVO();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setData(userList);
        return result;
    }

    @ApiOperation("新增用户")
    @PostMapping("add")
    public ResultVO addUser(@RequestBody @ModelAttribute UserVO user){
        if(user!=null){
            userList.add(user);
        }

        ResultVO result = new ResultVO();
        result.setCode(0);
        result.setMsg("新增成功");
        return result;
    }

    @ApiOperation("查询用户")
    @ApiImplicitParam(name = "userId", value = "用户id")
    @PostMapping("queryUser")
    public ResultVO<UserVO> queryUser(@RequestBody Long userId){
        UserVO u = null;
        if(userId!=null){
            for(UserVO user:userList){
                if(user.getUserId()==userId){
                    u = user;
                    break;
                }
            }
        }

        ResultVO result = new ResultVO();
        if(u==null){
            result.setCode(-1);
            result.setMsg("查询的用户不存在");
        }else{
            result.setCode(0);
            result.setMsg("查询成功");
            result.setData(u);
        }
        return result;
    }
}
