package com.xpu.school_guide.dto;

import lombok.Data;

/**
 * @author sofency
 * @date 2020/5/12 9:45
 * @package IntelliJ IDEA
 * @description
 */
@Data
public class GroupsDTO {
    private Integer groupId;//步骤的id
    private Boolean status;//是否完成
    private String groupName;//步骤名字
    private String groupText;//步骤的详细的信息
    private String taskName;//任务名字
    private String siteName;//地点的名字
    private String siteLo;//经纬度
    private String siteIo;//经纬度
}
