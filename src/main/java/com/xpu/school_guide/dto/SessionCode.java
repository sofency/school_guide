package com.xpu.school_guide.dto;
import lombok.Data;

/**
 * @author sofency
 * @date 2020/3/23 8:58
 * @package IntelliJ IDEA
 * @description
 */
@Data
public class SessionCode{
    private Integer id;//用户的主键
    private String openId;
    private String sessionKey;
}
