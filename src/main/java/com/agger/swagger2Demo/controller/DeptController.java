package com.agger.swagger2Demo.controller;

import com.agger.swagger2Demo.vo.DeptVO;
import com.agger.swagger2Demo.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @classname: DeptController
 * @description: 部门管理
 * @author chenhx
 * @date 2019-11-25 21:36:17
 */
@Api(value="部门管理",tags = "部门管理控制")
@RestController
@RequestMapping("/dept")
public class DeptController {

    private final static List<DeptVO> deptList = new ArrayList<>();
    {
        deptList.add(new DeptVO(100L,"软件部"));
        deptList.add(new DeptVO(200L,"财务部"));
    }

    @ApiOperation("查询部门列表")
    @GetMapping("/list")
    public ResultVO<List<DeptVO>> getDeptList(){

        ResultVO result = new ResultVO();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setData(deptList);
        return result;
    }

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

}
