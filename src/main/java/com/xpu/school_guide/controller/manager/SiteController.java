package com.xpu.school_guide.controller.manager;

import com.xpu.school_guide.dto.GroupManagerDTO;
import com.xpu.school_guide.dto.GroupsDetailDTO;
import com.xpu.school_guide.dto.ResultMsg;
import com.xpu.school_guide.dto.TaskDetailsDTO;
import com.xpu.school_guide.enums.Code;
import com.xpu.school_guide.mapper.GroupsMapper;
import com.xpu.school_guide.mapper.SitesMapper;
import com.xpu.school_guide.mapper.TasksMapper;
import com.xpu.school_guide.pojo.*;
import com.xpu.school_guide.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sofency
 * @date 2020/5/11 16:11
 * @package IntelliJ IDEA
 * @description
 */
@RestController
public class SiteController {
    private SiteService siteService;
    private TasksMapper tasksMapper;
    private GroupsMapper groupsMapper;
    private SitesMapper sitesMapper;

    @Autowired
    public SiteController(TasksMapper tasksMapper,SiteService siteService,GroupsMapper groupsMapper,SitesMapper sitesMapper) {
        this.tasksMapper = tasksMapper;
        this.siteService = siteService;
        this.groupsMapper = groupsMapper;
        this.sitesMapper = sitesMapper;
    }

    /**
     * 添加地点
     * @return
     */
    @RequestMapping("/addPlace")
    public ResultMsg addPlace(Sites sites){
        return siteService.addPlace(sites);
    }

    @RequestMapping("/editPlace")
    public ResultMsg updatePlace(Sites sites, @RequestParam("admin") String admin){//学院号
        ResultMsg resultMsg = new ResultMsg();
        String  creator = sites.getCreator();
        if(!"".equals(admin)&&admin.equals(creator)){
            siteService.updatePlace(sites);
        }else{
            resultMsg.setMsg(Code.CHANGE_FAIL.getMsg());
            resultMsg.setStatus(Code.CHANGE_FAIL.getStatus());
        }
        return resultMsg;
    }
    /**
     * 显示所有地点的详细信息
     * @return
     */
    @RequestMapping("/showSites")
    public ResultMsg showSites(){
        ResultMsg resultMsg = siteService.showSites();
        System.out.println(Thread.currentThread().getName());
        return resultMsg;
    }

    @RequestMapping("/deleteTaskById")
    public ResultMsg deleteTaskById(int taskId){
        return siteService.deleteTask(taskId);
    }

    @RequestMapping("/getTaskById")
    public ResultMsg getTaskById(int taskId){
        GroupsExample groupsExample = new GroupsExample();
        groupsExample.createCriteria().andTasksIdEqualTo(taskId);
        List<Groups> groups = groupsMapper.selectByExample(groupsExample);
        List<GroupManagerDTO> groupManagerDTOS = new ArrayList<>();
        for(Groups groups1:groups){
            Integer siteId = groups1.getSiteId();
            Sites sites = sitesMapper.selectByPrimaryKey(siteId);
            GroupManagerDTO groupManagerDTO = new GroupManagerDTO();
            groupManagerDTO.setGroupName(groups1.getGroupName());
            groupManagerDTO.setGroupText(groups1.getGroupText());
            groupManagerDTO.setGroupOrder(groups1.getGroupOrder());
            groupManagerDTO.setSiteId(sites.getId());
            groupManagerDTO.setSiteName(sites.getSiteName());
            groupManagerDTO.setGroupId(groups1.getId());
            groupManagerDTOS.add(groupManagerDTO);
        }

        GroupsDetailDTO groupsDetailDTO = new GroupsDetailDTO();
        groupsDetailDTO.setList(groupManagerDTOS);
        groupsDetailDTO.setTaskId(taskId);

        Tasks tasks = tasksMapper.selectByPrimaryKey(taskId);
        groupsDetailDTO.setTaskName(tasks.getTaskName());
        groupsDetailDTO.setTaskText(tasks.getTaskText());
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setData(groupsDetailDTO);
        resultMsg.setMsg(Code.SEARCH_SUCCESS.getMsg());
        resultMsg.setStatus(Code.SEARCH_SUCCESS.getStatus());
        return resultMsg;
    }

    @RequestMapping("/addTasks")
    public ResultMsg addTasks(@RequestBody TaskDetailsDTO taskDetailsDTO){
        ResultMsg resultMsg = new ResultMsg();
        //获取所有的步骤
        if(taskDetailsDTO==null) {
            resultMsg.setStatus(Code.UN_KNOW_EXCEPTION.getStatus());
            resultMsg.setMsg(Code.UN_KNOW_EXCEPTION.getMsg());
            return resultMsg;
        }

        List<Groups> groups = taskDetailsDTO.getGroups();
        Tasks tasks = new Tasks();
        tasks.setCollegeId(taskDetailsDTO.getCollegeId());
        tasks.setTaskName(taskDetailsDTO.getTaskName());
        tasks.setTaskOrder(taskDetailsDTO.getTaskOrder());
        tasks.setTaskText(taskDetailsDTO.getTaskText());
        tasks.setIsDelete(false);
        Integer taskId = taskDetailsDTO.getTaskId();//获取任务的id
        if(taskId==null) {//说明为插入数据
             if (groups == null) {
                 resultMsg.setStatus(404);
                 resultMsg.setMsg("步骤发送失败");
                 resultMsg.setData(null);
                 return resultMsg;
             }else {
                siteService.insertTask(tasks, groups);
             }
        }else{
            if (groups == null) {
                resultMsg.setStatus(404);
                resultMsg.setMsg("步骤发送失败");
                resultMsg.setData(null);
                return resultMsg;
            }else {
                siteService.updateTask(tasks,taskId,groups);
            }
        }
        resultMsg.setMsg(Code.TASK_UPDATE.getMsg());
        resultMsg.setData(Code.TASK_UPDATE.getStatus());
        return resultMsg;
    }

    //获取添加的任务
    @RequestMapping("/getTasks")
    public ResultMsg getTasks(int collegeId){
        System.out.println(Thread.currentThread().getName());
        TasksExample tasksExample = new TasksExample();
        tasksExample.createCriteria().andCollegeIdEqualTo(collegeId);
        List<Tasks> tasks = tasksMapper.selectByExample(tasksExample);
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setMsg(Code.SEARCH_SUCCESS.getMsg());
        resultMsg.setStatus(Code.SEARCH_SUCCESS.getStatus());
        resultMsg.setData(tasks);
        return resultMsg;
    }
}
