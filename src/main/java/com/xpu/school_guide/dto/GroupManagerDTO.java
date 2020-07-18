package com.xpu.school_guide.dto;

import lombok.Data;

/**
 * @author sofency
 * @date 2020/5/18 20:11
 * @package IntelliJ IDEA
 * @description
 */
@Data
public class GroupManagerDTO {
    private String groupName;//任务名
    private String groupText;//任务的详细信息
    private int groupOrder;//步骤的顺序
    private int siteId;
    private int groupId;//步骤的id
    private String siteName;
}
