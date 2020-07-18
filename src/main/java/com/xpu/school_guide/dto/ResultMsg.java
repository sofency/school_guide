package com.xpu.school_guide.dto;

import lombok.Data;

/**
 * @author sofency
 * @date 2020/5/11 15:43
 * @package IntelliJ IDEA
 * @description
 */
@Data
public class ResultMsg {
    private Object data;//数据
    private int status;//状态
    private String msg;//消息
}
