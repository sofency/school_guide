package com.xpu.school_guide.dto;

import lombok.Data;

/**
 * @author sofency
 * @date 2020/5/11 17:14
 * @package IntelliJ IDEA
 * @description
 */
@Data
public class GroupDetailDTO {
    private String taskName;//任务名称
    private String groupName;// 任务名
    private String groupText;// 任务的详细信息
    private int siteId;// 地点的id
    private String siteName;// 地点名称
    private String siteLo;// 经度
    private String siteIo;// 维度
}
