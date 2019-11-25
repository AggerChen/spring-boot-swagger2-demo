package com.agger.swagger2Demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: ResultVO
 * @description: TODO
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
