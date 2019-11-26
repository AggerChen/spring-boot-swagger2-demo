package com.agger.swagger2Demo.controller;

import com.agger.swagger2Demo.vo.ResultVO;
import com.agger.swagger2Demo.vo.UserVO;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: UserController
 * @description: 用户管理控制类
 * @author: chenhx
 * @create: 2019-11-25 14:20
 **/
@Api(value="用户管理",tags = "用户管理控制")
@RestController
@RequestMapping("/user")
public class UserController {

    private final static List<UserVO> userList = new ArrayList<>();
    {
        userList.add(new UserVO(1L,"张三",20,"aaaa@qq.com",new Date()));
        userList.add(new UserVO(2L,"李四",25,"bbbb@qq.com",new Date()));
    }

    @ApiOperation("查询用户列表")
    @GetMapping("/list")
    public ResultVO<List<UserVO>> getUserList(){
        ResultVO result = new ResultVO();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setData(userList);
        return result;
    }

    /**
     * @Title: addUser
     * @Description: 参数传入一个model对象，不需要使用其他注解描述此类参数，只需要在model类中使用类注解
     * @author chenhx
     * @date 2019-11-26 17:12:18
     */
    @ApiOperation("新增用户")
    @PostMapping("/add")
    public ResultVO addUser(@RequestBody UserVO user){
        if(user!=null){
            userList.add(user);
        }

        ResultVO result = new ResultVO();
        result.setCode(0);
        result.setMsg("新增成功");
        return result;
    }

    /**
     * @Title: queryUserById
     * @Description: 使用@ApiParam注解在方法参数上写参数的说明，对代码入侵比较高
     * @author chenhx
     * @date 2019-11-26 17:09:36
     */
    @ApiOperation("通过id查询用户")
    @PostMapping("/queryUserById")
    public ResultVO<UserVO> queryUserById(
            @RequestBody
            @ApiParam(name="用户id",value="传入用户id",required=true)  Long userId){
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

    /**
     * @Title: queryUserByName
     * @Description: 使用@ApiImplicitParam注解只在方法上描述参数，对源代码没有侵入，比较推荐
     * @author chenhx
     * @date 2019-11-26 17:09:36
     */
    @ApiOperation("通过name查询用户")
    @ApiImplicitParam(name = "用户userName",value = "传入用户userName",required = true)
    @PostMapping("/queryUserByName")
    public ResultVO<UserVO> queryUserByName(@RequestBody String userName){
        UserVO u = null;
        if(StringUtils.isNotBlank(userName)){
            for(UserVO user:userList){
                if(userName.equals(user.getUserName())){
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

    /**
     * @Title: queryUserEmail
     * @Description: 参数在路径中的接口方法，需要指定@ApiImplicitParam属性paramType为path
     * @author chenhx
     * @date 2019-11-26 17:09:36
     */
    @ApiOperation("查询用户的email")
    @PostMapping("/queryUserEmail/{id}")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "long", paramType = "path")
    public ResultVO<String> queryUserEmail( @PathVariable("id") Long userId){
        String email = null;
        if(userId!=null){
            for(UserVO user:userList){
                if(user.getUserId()==userId){
                    email = user.getEmail();
                    break;
                }
            }
        }
        ResultVO result = new ResultVO();
        if(StringUtils.isBlank(email)){
            result.setCode(-1);
            result.setMsg("查询的用户不存在");
        }else{
            result.setCode(0);
            result.setMsg("查询成功");
            result.setData(email);
        }
        return result;
    }
}
