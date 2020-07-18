package com.xpu.school_guide.controller.common;

import com.xpu.school_guide.dto.*;
import com.xpu.school_guide.enums.Code;
import com.xpu.school_guide.enums.LoginStatus;
import com.xpu.school_guide.pojo.StudentInfo;
import com.xpu.school_guide.service.LoginService;
import com.xpu.school_guide.service.StudentService;
import com.xpu.school_guide.utils.SessionCodeUtils;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author sofency
 * @date 2020/5/11 15:41
 * @package IntelliJ IDEA
 * @description
 */
@RestController
public class LoginController {
    private LoginService loginService;
    private SessionCodeUtils sessionCodeUtils;
    private StudentService studentService;
    private RedisTemplate<String,String> redisTemplate;


    @Autowired
    public LoginController(LoginService loginService,
                           SessionCodeUtils sessionCodeUtils,
                           RedisTemplate<String,String> redisTemplate,
                           StudentService studentService) {
        this.loginService = loginService;
        this.sessionCodeUtils = sessionCodeUtils;
        this.redisTemplate = redisTemplate;
        this.studentService = studentService;
    }
    //管理员登录
    @RequestMapping("/login")
    public ResultMsg login(LoginDTO loginDTO){
        String account = loginDTO.getAdmin();
        String password = loginDTO.getPassword();
        ResultMsg resultMsg = loginService.login(account, password);
        return resultMsg;
    }

    //普通用户登录
    //根据code请求微信的后台拿取openId和session_key
    @RequestMapping("/getCode")
    public ResultMsg getCode(String code){
        SessionCode sessionCode = sessionCodeUtils.getSessionCode(code);
        ResultMsg resultMsg = new ResultMsg();
        ReturnCodeDTO returnCodeDTO = new ReturnCodeDTO();
        if(sessionCode.getOpenId()!=null){
            String password = sessionCode.getOpenId()+"::"+sessionCode.getSessionKey();
            String token = UUID.randomUUID().toString();//更改为UUID的cookie
            returnCodeDTO.setToken(token);//存储token

            redisTemplate.opsForValue().set(token,password,3, TimeUnit.HOURS);//设置三小时过期

            //根据openId查询数据库用户信息表中是否有用户的信息
            StudentInfo studentInfo = studentService.isHaveInfo(sessionCode.getOpenId());
            if(studentInfo!=null){//说明注册过信息
                int collegeId = studentInfo.getCollegeId();//获取学院号
                List<TaskDTO> taskDTOS = studentService.getTask(collegeId,sessionCode.getOpenId(),true);
                if(taskDTOS!=null&&taskDTOS.size()!=0){//说明有该同学的信息
                    //但是不知道是否完成任务
                    if(taskDTOS.get(0).isStatus()){//任务已经完成
                        returnCodeDTO.setStatus(LoginStatus.FINISH.getStatus());
                    }else{//任务没有完成  获取到开学任务中的所有步骤
                        returnCodeDTO.setStatus(LoginStatus.UN_FINISH.getStatus());
                    }
                    returnCodeDTO.setData(collegeId);
                }else{
                    returnCodeDTO.setStatus(LoginStatus.REGISTER.getStatus());//仅仅注册过信息
                }
            }else{//说明没有注册过信息
                returnCodeDTO.setStatus(LoginStatus.UN_REGISTER.getStatus());//仅仅注册过信息
            }
            resultMsg.setData(returnCodeDTO);
            resultMsg.setStatus(Code.CODE_SUCCESS.getStatus());
            resultMsg.setMsg(Code.CODE_SUCCESS.getMsg());
        }else{
            resultMsg.setStatus(Code.CODE_FAIL.getStatus());
            resultMsg.setMsg(Code.CODE_FAIL.getMsg());
        }
        return resultMsg;
    }
}
