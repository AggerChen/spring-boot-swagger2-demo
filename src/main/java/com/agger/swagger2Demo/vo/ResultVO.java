package com.agger.swagger2Demo.vo;

import lombok.Data;

/**
 * @program: ResultVO
 * @description: TODO
 * @author: chenhx
 * @create: 2019-11-25 14:26
 **/
@Data
public class ResultVO<T> {
    private Integer code = -1;     // 返回状态码
    private String msg;            // 返回信息
    private T data;                // 返回数据

    public ResultVO() {
    }

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
