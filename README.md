## SpringBoot使用Swagger2实现接口API文档

### 一、前言
前后端分离的项目开发中，后台写好的接口在跟前端对接测试的时候，往往很麻烦，需要自己编写接口文档，接口参数，响应数据等等。当接口更改后，又要手动去更改接口文档，久而久之就会忘记或者搞得接口和文档不一致。Swagger的诞生就是为了解决这个痛点，本文基于SpringBoot搭建Swagger2，更多的Swagger的版本区别啊，原理啊这里不多做赘述，直接上手。

本文档是根据网上巨多网友的分享整理出来博主觉得有用和自己的意见，欢迎交流。

欢迎访问博客查看详情 [https://blog.csdn.net/github_36086968/article/details/103265848](https://blog.csdn.net/github_36086968/article/details/103265848)

### 二、快速上手
#### 2.1 pom依赖
只展示重要依赖，其他的信息省略，需要查看完整的pom.xml请访问github下载源代码查看。
> pom.xml
```xml
<!--web依赖-->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!--热部署依赖-->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-devtools</artifactId>
	<scope>runtime</scope>
	<optional>true</optional>
</dependency>

<!--lombok工具-->
<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<optional>true</optional>
</dependency>

<!-- apache工具 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.9</version>
</dependency>

<!--swagger依赖-->
<dependency><!--添加Swagger依赖 -->
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger2</artifactId>
	<version>2.9.2</version>
</dependency>
<dependency><!--添加Swagger-UI依赖 -->
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger-ui</artifactId>
	<version>2.9.2</version>
</dependency>
```
#### 2.2 添加swagger配置类
在项目中创建一个swagger2配置来，用来填写默认配置。
添加类注解@EnableSwagger2表示开始swagger2配置。
添加类注解@Configuration表示是springBoot配置类
> Swagger2Config.java
```java
package com.agger.swagger2Demo.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: Swagger2Config
 * @description: swagger配置类
 * @author: chenhx
 * @create: 2019-11-25 14:14
 **/
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${swagger2.enable}")
    private boolean enable;             // 配置文件参数注入，用来表示是否开启API文档

    @Bean("userApis")
    public Docket userApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户模块")      // 分组
                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.agger.swagger2Demo.controller"))     // 匹配包路径
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))       // 匹配包含的指定注解
//               .paths(PathSelectors.regex("/user.*"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .enable(enable);   // 是否开启API
    }

    @Bean("deptApis")
    public Docket deptApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("部门模块")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.regex("/dept.*"))
                .build()
                .apiInfo(apiInfo())
                .enable(enable);
    }

    /**
     * 编写apiInfo配置
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API接口接口文档测试项目")
                .description("描述：编写一个基于springBoot的swagger2接口文档测试demo")
                .contact(new Contact("agger",null,null))
                .version("1.0.0")
                .build();
    }
}
```

1. 其中 @Value("${swagger2.enable}")表示从项目application.yml或application.properties中获取配置数据，用来控制是否显示API文档，因为我们一般只在开发和测试环境中需要文档接口，生产环境则需要禁用。你还可以使用profile配置来做成不同的配置，这里就简单演示一下，如下：
> application.yml
```yml
# 是否开启swagger API文档
swagger2:  
    enable: true
```
2. 可以编写多个@Bean("deptApis") 来注入多个分组文档，在文档的html右上角 Select a spec 可以分组查看。

#### 2.3 编写contoller
为了将controller中的接口形成文档，我们需要在controller类上、接口方法上、甚至model类上添加swagger相关注解。
其中最常用的注解有如下：
1. **@Api** : 作用在controller类上描述类作用    
    tags="说明该类的作用，可以在UI界面上看到的描述信息" 
    value="该参数没什么意义，不需要配置"
    
    >示例：@Api(value="用户管理",tags = "用户管理控制")
    
2. **@ApiOperation** ：作用在接口方法上，用于描述接口说明，可以在UI上看到做区分
    value="方法标题" 
    notes="方法的备注说明"
    
    >示例：@ApiOperation(value="新增用户")

3. **@ApiImplicitParam**：作用在接口方法上，用于描述接口参数信息（只适用于单一参数，不适用与多参数和model对象参数）
    name="参数名"
    value="参数的汉字说明、解释"
    required="参数是否必须传"
    paramType="参数放在哪个地方"
	    · header --> 请求参数的获取：@RequestHeader
	    · query --> 请求参数的获取：@RequestParam
	    · path（用于restful接口）--> 请求参数的获取：@PathVariable
	    · body（不常用）
	    · form（不常用）    
    dataType="参数类型，默认String，其它值dataType=Integer"
    defaultValue="参数的默认值"
    
    > 示例：@ApiImplicitParam(name = "userId", value = "用户id",required=true)
    
4. **@ApiImplicitParams**：作用在接口方法上，用于描述多个参数信息，与@@ApiImplicitParam一起使用

    > 示例：
    > @ApiImplicitParams({ 
    > @ApiImplicitParam(name="mobile",value="手机号",required=true), 
    > @ApiImplicitParam(name="password",value="密码",required=true),
    > @ApiImplicitParam(name="age",value="年龄",required=true,dataType="Integer")
    > })
    
5. **@ApiModel**：作用在model类上，用于接口参数是model对象时，使用@RequestBody场景，方便swagger解析成api文档
        value="model类名称
        description="model类详细描述"
       
      > 示例：@ApiModel("用户类")

6. **@ApiModelProperty**：作用在model属性上，用于描述属性字段基本信息
    value="字段名称"
    notes="字段具体描述"
    required="是否必须"
    example="示例值"
    
    > 示例：@ApiModelProperty(value = "用户姓名",required = true,example = "张三",notes = "用户姓名")
7. **@ApiResponse** ：作用在接口方法上，用于描述接口错误响应   
    code="响应状态码，比如400"
    message="响应信息，比如'请求参数错误'"
    response="抛出异常的类"
    
    > 示例：@ApiResponse(code=400,message = "参数错误")
    
8. **@ApiResponses**：作用在接口方法上，表示一组错误响应，与@ApiResponse结合使用

    > 示例：
    @ApiResponses({
	    @ApiResponse(code=400,message = "参数错误"),
	    @ApiResponse(code=405,message = "确实参数")
    })

9. **@ApiParam** ：作用在接口参数上，用于描述接口参数信息。此注解对代码入侵比较大，建议使用@ApiImplicitParam作用在方法上，来保持代码的简洁
    name="参数名称"
    value="参数的说明"
    required=true 是否必须
    
示例：
```java
@ApiOperation("查询用户")
@PostMapping("queryUser")
public ResultVO<UserVO> queryUser(
    @RequestBody
    @ApiParam(name="用户id",value="传入用户id",required=true)  Long userId){
        ...
}
```

接下来展示一个完整的UserController.java
> UserController.java
```java
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
```

接口如果是多参数的话，示例如下：
```java
/**
 * @Title: addDept
 * @Description: 接口多参数使用方式
 * @author chenhx
 * @date 2019-11-26 20:55:56
 */
@ApiOperation("新增部门")
@ApiImplicitParams({
		@ApiImplicitParam(name = "deptId",value = "部门id",required = true),
		@ApiImplicitParam(name = "deptName",value = "部门名称",required = true)
})
@PostMapping("/add")
public ResultVO addDept(Long deptId,String deptName){
	ResultVO result = new ResultVO();
	if(deptId!=null&& StringUtils.isNotBlank(deptName)){
		deptList.add(new DeptVO(deptId,deptName));
		result.setCode(0);
		result.setMsg("新增成功");
	}else{
		result.setMsg("新增失败");
	}
	return result;
}
```
示例中有使用到响应类如下：

> ResultVO.java
```java
package com.agger.swagger2Demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: ResultVO
 * @description: 接口响应类，也需要用@ApiModel修饰起来，才能在API中查看具体的返回值
 * @author: chenhx
 * @create: 2019-11-25 14:26
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("响应实体")
public class ResultVO<T> {
    @ApiModelProperty("返回状态码")
    private Integer code = -1;     // 返回状态码
    @ApiModelProperty("返回信息")
    private String msg;            // 返回信息
    @ApiModelProperty("返回数据")
    private T data;                // 返回数据

}
```

#### 2.4 访问UI查看生成的API
启动项目，访问地址 http://localhost:8080/swagger-ui.html 就可以查看到我们的API文档了。
1. 如果在配置文件中配置了多个@Bean分组，则在右上角可以切换分组显示接口API
![5e4ae2c35c8bffe117cf457e40cc406a.png](en-resource://database/1223:1)
2. 接口参数为model类型，如果model中有写example字段，则可以方便的显示请求示例。
3. 点击每个方法的 "Try it out" 按钮，则可以立即测试接口
![c5496cf7487527ba4262b978166d28b8.png](en-resource://database/1225:1)
![a1df012fec6d56044660dd61683774ac.png](en-resource://database/1227:1)
4. 参数在路径中方式的接口，也可以只管的查看到参数说明
![ff26b6766cd09dfd391fa67de3db2710.png](en-resource://database/1229:1)
5. 在接口的最下面有个Models分类，里面存放的是当前组中的model示例，方便前端人员查看字段。
![d28736f5e973c1d9490af11f4c3ebccf.png](en-resource://database/1231:1)
6. 接口多参数，分model方式的接口
![495c783ad25bdac7b363e2cf7b5a3b6e.png](en-resource://database/1233:1)

#### 2.5 注意事项
1. 想要接口和接口的响应中有具体的字段，则你的model必须使用@ApiModel注解表示，包括响应实体ResultVO。
2. 尽量不要在方法参数上使用注解@ApiParam来描述参数使用@ApiImplicitParam来替代，尽量保持原接口的整洁。
3. 本示例使用的是Swagger2，所以请注意你的版本是否跟我一致。

### 三、github示例地址
本文档是根据网上巨多网友的分享整理出来博主觉得有用和自己的意见，欢迎交流
本文档所使用的demo已经在github中，需要的请下载查看，欢迎star
 [https://github.com/AggerChen/spring-boot-swagger2-demo](https://github.com/AggerChen/spring-boot-swagger2-demo)


