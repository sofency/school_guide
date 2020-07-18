package com.xpu.school_guide.dto;

import lombok.Data;

/**
 * @author sofency
 * @date 2020/3/22 14:15
 * @package IntelliJ IDEA
 * @description
 */
@Data
public class AuthorizeCode {
    private String js_code;
    private String appid="wx8693b48c10533d9a";
    private String secret="53945fd3f6be0c430ba21f6ff2399caf";
    private String grant_type="authorization_code";
}
