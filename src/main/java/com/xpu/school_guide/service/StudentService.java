package com.xpu.school_guide.service;

import com.xpu.school_guide.dto.*;
import com.xpu.school_guide.enums.Code;
import com.xpu.school_guide.mapper.*;
import com.xpu.school_guide.pojo.*;
import com.xpu.school_guide.utils.SessionCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sofency
 * @date 2020/5/12 6:13
 * @package IntelliJ IDEA
 * @description
 */
@Service
public class StudentService {
    private CollegesMapper collegesMapper;
    private GroupsMapper groupsMapper;
    private TasksMapper tasksMapper;
    private SitesMapper sitesMapper;
    private StudentInfoMapper studentInfoMapper;
    private StudentTasksGroupsMapper studentTasksGroupsMapper;
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    public StudentService(CollegesMapper collegesMapper,
                             GroupsMapper groupsMapper,
                             TasksMapper tasksMapper,
                             StudentTasksGroupsMapper studentTasksGroupsMapper,
                             SitesMapper sitesMapper,RedisTemplate<String,Object> redisTemplate,
                          SessionCodeUtils sessionCodeUtils,
                          StudentInfoMapper studentInfoMapper) {
        this.collegesMapper = collegesMapper;
        this.groupsMapper= groupsMapper;
        this.tasksMapper = tasksMapper;
        this.sitesMapper = sitesMapper;
        this.redisTemplate = redisTemplate;
        this.studentTasksGroupsMapper=studentTasksGroupsMapper;
        this.studentInfoMapper = studentInfoMapper;
    }

    /**
     * 获取学院的详细信息
     * @return
     */
    public List<Colleges> getInstitutes(){

        List<Colleges> colleges = new ArrayList<>();
        Set<String> keys = redisTemplate.keys("college::*");
        for(String key:keys){
            colleges.add((Colleges) redisTemplate.opsForValue().get(key));
        }
        if(colleges.size()==0){
            colleges = collegesMapper.selectByExample(null);
        }
        return colleges;
    }
    /**
     * 根据学院 的id获取学院的所有除开学任务外的任务
     * 注意添加任务的时候判断当前学院是否添加过任务如果添加过提醒删除
     * @param collegeId
     * @return
     */
    public List<TaskDTO> getTask(int collegeId,String openId,boolean isFirst){
        //获取到所有的task
        TasksExample tasksExample = new TasksExample();
        tasksExample.createCriteria().andCollegeIdEqualTo(collegeId).andIsFirstEqualTo(isFirst).andIsDeleteEqualTo(false);
        List<Tasks> tasks = tasksMapper.selectByExample(tasksExample);//根据学院获取到所有的任务

        //获取到学生已经完成的操作

        List<TaskDTO> taskDTOS = new ArrayList<>();
        for(Tasks task:tasks){

            StudentTasksGroupsExample student = new StudentTasksGroupsExample();
            student.createCriteria().andOpenIdEqualTo(openId).andTaskIdEqualTo(task.getId());
            List<StudentTasksGroups> studentGroups = studentTasksGroupsMapper.selectByExample(student);
            List<Integer> finishGroupId = null;
            //已经完成的任务集合
            if(studentGroups!=null&&studentGroups.size()!=0){
                finishGroupId = studentGroups.stream().map(StudentTasksGroups::getGroupId).collect(Collectors.toList());
            }

            GroupsExample groupsExample = new GroupsExample();
            groupsExample.createCriteria().andTasksIdEqualTo(task.getId());
            List<Groups> allGroups = groupsMapper.selectByExample(groupsExample);
            TaskDTO taskDTO = new TaskDTO();
            if(finishGroupId==null||finishGroupId.size()!=allGroups.size()){
                taskDTO.setStatus(false);//还有步骤没有完成
            }else{
                taskDTO.setStatus(true);
            }
            taskDTO.setTasks(task);
            taskDTOS.add(taskDTO);
        }
        return taskDTOS;
    }

    /**
     * 根据taskId获取任务的详细信息
     * @param taskId
     * @return
     */
    public List<GroupsDTO> getGroup(int taskId, String openId){
        List<GroupsDTO> groupsDTOS = new ArrayList<>();

        GroupsExample groupsExample = new GroupsExample();
        groupsExample.createCriteria().andTasksIdEqualTo(taskId);
        //返回所有的步骤
        List<Groups> groups = groupsMapper.selectByExample(groupsExample);
        if(groups==null) return null;
        //获取任务的信息
        Tasks tasks = tasksMapper.selectByPrimaryKey(taskId);

        //获取学生已经完成的信息
        StudentTasksGroupsExample example = new StudentTasksGroupsExample();
        example.createCriteria().andTaskIdEqualTo(taskId).andOpenIdEqualTo(openId);
        List<StudentTasksGroups> studentTasksGroups = studentTasksGroupsMapper.selectByExample(example);
        //已经完成的步骤Id
        List<Integer> finishGroupIds = studentTasksGroups.stream().map(StudentTasksGroups::getGroupId).collect(Collectors.toList());
        //查询到
        for(Groups groups1:groups){
            Integer siteId = groups1.getSiteId();
            Sites sites = sitesMapper.selectByPrimaryKey(siteId);
            GroupsDTO groupsDTO = new GroupsDTO();
            groupsDTO.setGroupName(groups1.getGroupName());
            groupsDTO.setGroupText(groups1.getGroupText());
            groupsDTO.setGroupId(groups1.getId());
            groupsDTO.setSiteName(sites.getSiteName());
            groupsDTO.setSiteIo(sites.getSiteIo());
            groupsDTO.setTaskName(tasks.getTaskName());
            groupsDTO.setSiteLo(sites.getSiteLo());
            groupsDTO.setStatus(finishGroupIds.contains(groups1.getId()));
            groupsDTOS.add(groupsDTO);
        }
        return groupsDTOS;
    }

    /**
     * 根据经纬度获取位置的信息
     * @param siteId
     * @return
     */
    public Sites getPlace(int siteId){
        return sitesMapper.selectByPrimaryKey(siteId);
    }


    public Boolean insert(StudentInfo studentInfo){
        String openId = studentInfo.getOpenId();

        StudentInfo studentInfo1 = studentInfoMapper.selectByPrimaryKey(openId);
        if(studentInfo1!=null){
            return false;
        }else{
            studentInfoMapper.insert(studentInfo);
            return true;
        }
    }
    /**
     * 设置任务步骤完成
     */
    public Boolean finish(StudentTasksGroups studentTasksGroups){
        int insert = studentTasksGroupsMapper.insert(studentTasksGroups);
        return insert > 0;
    }


    /**
     * 根据openId获取用户信息表中是否用户的信息
     */
    public StudentInfo isHaveInfo(String openId){
        return studentInfoMapper.selectByPrimaryKey(openId);
    }
}