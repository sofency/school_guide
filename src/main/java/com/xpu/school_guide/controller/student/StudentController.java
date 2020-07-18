package com.xpu.school_guide.controller.student;

import com.xpu.school_guide.dto.GroupsDTO;
import com.xpu.school_guide.dto.ResultMsg;
import com.xpu.school_guide.dto.TaskDTO;
import com.xpu.school_guide.enums.Code;
import com.xpu.school_guide.pojo.Colleges;
import com.xpu.school_guide.pojo.Sites;
import com.xpu.school_guide.pojo.StudentInfo;
import com.xpu.school_guide.pojo.StudentTasksGroups;
import com.xpu.school_guide.service.StudentService;
import com.xpu.school_guide.utils.DataResolveUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author sofency
 * @date 2020/5/11 16:26
 * @package IntelliJ IDEA
 * @description
 */
@RestController
public class StudentController {
    StudentService studentService;
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    public StudentController(StudentService studentService,RedisTemplate<String,String> redisTemplate) {
        this.studentService= studentService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取学院的详细信息
     * @return
     */
    @RequestMapping("/getInstitutes")
    public ResultMsg getInstitutes(String token,int collegeId){

        ResultMsg resultMsg = new ResultMsg();
        List<Colleges> colleges = studentService.getInstitutes();
        // 将列表
        //处理排序的问题
        int cnt = 0;
        Colleges colleges2 = colleges.get(0);
        for(Colleges colleges1:colleges){
            if(colleges1.getId()==collegeId){
                Colleges temp=colleges1;
                colleges.set(0,temp);
                colleges.set(cnt,colleges2);
            }
            cnt++;
        }
        resultMsg.setStatus(Code.SEARCH_SUCCESS.getStatus());
        resultMsg.setMsg(Code.SEARCH_SUCCESS.getMsg());
        resultMsg.setData(colleges);
        return resultMsg;
    }
    /**
     * 根据学院 的id获取学院的任务详情
     * 注意添加任务的时候判断当前学院是否添加过任务如果添加过提醒删除
     * @param collegeId
     * @return
     */
    @RequestMapping("/getTask")
    public ResultMsg getTask(@RequestParam("collegeId") int collegeId,
                             @RequestParam("token") String token,
                             @RequestParam("isFirst") Boolean isFirst){
        String openId = getToken(token);
        ResultMsg resultMsg = new ResultMsg();
        List<TaskDTO> taskDTOList = studentService.getTask(collegeId,openId,isFirst);
        if(taskDTOList!=null&&taskDTOList.size()!=0){
            for(TaskDTO taskDTO:taskDTOList){
                int taskId = taskDTO.getTasks().getId();
                List<GroupsDTO> groups = studentService.getGroup(taskId, openId);
                taskDTO.setGroups(groups);
            }
            resultMsg.setData(taskDTOList);
            resultMsg.setMsg(Code.SEARCH_SUCCESS.getMsg());
            resultMsg.setStatus(Code.SEARCH_SUCCESS.getStatus());
        }else{
            resultMsg.setMsg(Code.TASK_NULL.getMsg());
            resultMsg.setStatus(Code.TASK_NULL.getStatus());
        }
        return resultMsg;
    }

    /**
     * 根据groupId获取任务的详细信息
     * @param taskId
     * @return
     */
    @RequestMapping("/getGroup")
    public ResultMsg getGroup(@RequestParam("taskId") int taskId,@RequestParam("token") String token){
        String openId = getToken(token);
        ResultMsg resultMsg = new ResultMsg();
        List<GroupsDTO> group = studentService.getGroup(taskId, openId);
        resultMsg.setMsg(Code.SEARCH_SUCCESS.getMsg());
        resultMsg.setStatus(Code.SEARCH_SUCCESS.getStatus());
        resultMsg.setData(group);
        return resultMsg;
    }
    /**
     * 根据经纬度获取位置的信息
     * @param siteId
     * @return
     */
    @RequestMapping("/getPlace")
    public ResultMsg getPlace(@RequestParam("siteId") int siteId,@RequestParam("token") String token){
        ResultMsg resultMsg = new ResultMsg();
        Sites place = studentService.getPlace(siteId);
        resultMsg.setMsg(Code.SEARCH_SUCCESS.getMsg());
        resultMsg.setStatus(Code.SEARCH_SUCCESS.getStatus());
        resultMsg.setData(place);
        return resultMsg;
    }

    //根据步骤Id
    @RequestMapping("/finishGroup")
    public ResultMsg finish(StudentTasksGroups studentTasksGroups,@RequestParam("token") String token){
        ResultMsg resultMsg = new ResultMsg();
        //判断用户是否为学院的人
        String openId = getToken(token);
        studentTasksGroups.setOpenId(openId);
        Boolean finish = studentService.finish(studentTasksGroups);
        resultMsg.setData(finish);
        resultMsg.setMsg(Code.TASK_FINISH.getMsg());
        resultMsg.setStatus(Code.TASK_FINISH.getStatus());
        return resultMsg;
    }

    @RequestMapping("/register")
    public ResultMsg register(StudentInfo studentInfo, @RequestParam("token") String token){
        ResultMsg resultMsg = new ResultMsg();
        String openId = getToken(token);
        studentInfo.setOpenId(openId);
        int info = DataResolveUtils.getInfo(studentInfo.getStudentId());
        studentInfo.setCollegeId(info);
        Boolean insert = studentService.insert(studentInfo);
        if(insert){
            resultMsg.setStatus(Code.REGISTER_SUCCESS.getStatus());
            resultMsg.setMsg(Code.REGISTER_SUCCESS.getMsg());
            resultMsg.setData(info);//返回学院号
        }else{
            resultMsg.setStatus(Code.REGISTERED.getStatus());
            resultMsg.setMsg(Code.REGISTERED.getMsg());
        }
        return resultMsg;
    }
    //获取openId
    public String getToken(String token){
        String s = redisTemplate.opsForValue().get(token);
        String[] split = s.split("::");
        return split[0];
    }
}
