package com.xpu.school_guide.dto;

import lombok.Data;

import java.util.List;

/**
 * @author sofency
 * @date 2020/5/18 20:10
 * @package IntelliJ IDEA
 * @description
 */
@Data
public class GroupsDetailDTO {
    private List<GroupManagerDTO> list;
    private String taskName;//项目名
    private String taskText;//详细信息
    private int taskId;//任务的Id
}
