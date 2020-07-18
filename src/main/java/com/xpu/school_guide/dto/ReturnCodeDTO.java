package com.xpu.school_guide.dto;

import lombok.Data;

/**
 * @author sofency
 * @date 2020/6/24 0:04
 * @package IntelliJ IDEA
 * @description
 */
@Data
public class ReturnCodeDTO {
    private String token;//返回的token
    private int status;//返回的状态码
    private Object data;//返回的数据信息
}
