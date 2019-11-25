package com.agger.swagger2Demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @classname: DeptVO
 * @description: 部门类
 * @author chenhx
 * @date 2019-11-25 21:37:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("部门类")
public class DeptVO implements Serializable {

    @ApiModelProperty( value= "部门id",required = true,example = "100",notes = "部门id")
    private Long deptId;
    @ApiModelProperty(value = "部门名称",required = true,example = "开发部",notes = "部门名称")
    private String deptName;

}
