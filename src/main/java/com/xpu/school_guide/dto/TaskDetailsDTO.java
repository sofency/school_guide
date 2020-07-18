package com.xpu.school_guide.dto;

import com.xpu.school_guide.pojo.Groups;
import lombok.Data;

import java.util.List;

/**
 * @author sofency
 * @date 2020/5/18 16:46
 * @package IntelliJ IDEA
 * @description
 */

@Data
public class TaskDetailsDTO {
    private List<Groups> groups;
    private String taskName;//项目名
    private String taskText;//详细的信息
    private int taskOrder;//任务序号
    private String openId;//根据
    private int collegeId;//学院号
    private Integer taskId;//任务的id
}
