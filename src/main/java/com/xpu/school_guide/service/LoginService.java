package com.xpu.school_guide.service;

import com.xpu.school_guide.dto.ResultMsg;
import com.xpu.school_guide.enums.Code;
import com.xpu.school_guide.mapper.AdminInfoMapper;
import com.xpu.school_guide.pojo.AdminInfo;
import com.xpu.school_guide.pojo.AdminInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sofency
 * @date 2020/5/12 6:13
 * @package IntelliJ IDEA
 * @description
 */
@Service
public class LoginService {

    AdminInfoMapper adminInfoMapper;
    @Autowired
    public LoginService(AdminInfoMapper adminInfoMapper) {
        this.adminInfoMapper = adminInfoMapper;
    }

    /**
     * 管理员端登录
     * @param admin
     * @param password
     * @return
     */
    public ResultMsg login(String admin, String password){
        ResultMsg resultMsg = new ResultMsg();

        AdminInfoExample adminInfoExample = new AdminInfoExample();
        adminInfoExample.createCriteria().andAdminEqualTo(admin).andPasswordEqualTo(password);
        List<AdminInfo> adminInfos = adminInfoMapper.selectByExample(adminInfoExample);
        if(adminInfos!=null && adminInfos.size()!=0){
            resultMsg.setMsg(Code.LOGIN_SUCCESS.getMsg());
            resultMsg.setStatus(Code.LOGIN_SUCCESS.getStatus());
            resultMsg.setData(adminInfos.get(0));//将数据给前端
        }else{
            resultMsg.setMsg(Code.LOGIN_FAIL.getMsg());
            resultMsg.setStatus(Code.LOGIN_FAIL.getStatus());
        }
        return resultMsg;
    }
}
