package com.xpu.school_guide.dto;

import com.xpu.school_guide.pojo.Tasks;
import lombok.Data;

import java.util.List;

/**
 * @author sofency
 * @date 2020/5/14 17:30
 * @package IntelliJ IDEA
 * @description
 */
@Data
public class TaskDTO {
    Tasks tasks;
    List<GroupsDTO> groups;
    boolean status;
}
