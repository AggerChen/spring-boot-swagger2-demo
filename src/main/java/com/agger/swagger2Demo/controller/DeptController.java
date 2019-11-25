package com.agger.swagger2Demo.controller;

import com.agger.swagger2Demo.vo.DeptVO;
import com.agger.swagger2Demo.vo.ResultVO;
import com.agger.swagger2Demo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @classname: DeptController
 * @description: 部门管理
 * @author chenhx
 * @date 2019-11-25 21:36:17
 */
@Api("部门管理")
@RestController
@RequestMapping("dept")
public class DeptController {

    private final static List<DeptVO> deptList = new ArrayList<>();
    {
        deptList.add(new DeptVO(100L,"软件部"));
        deptList.add(new DeptVO(200L,"财务部"));
    }

    @ApiOperation("查询部门列表")
    @GetMapping("list")
    public ResultVO<List<DeptVO>> getDeptList(){

        ResultVO result = new ResultVO();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setData(deptList);
        return result;
    }

    @ApiOperation("新增部门")
    @PostMapping("add")
    public ResultVO addDept(@RequestBody DeptVO dept){
        if(dept!=null){
            deptList.add(dept);
        }

        ResultVO result = new ResultVO();
        result.setCode(0);
        result.setMsg("新增成功");
        return result;
    }

}
