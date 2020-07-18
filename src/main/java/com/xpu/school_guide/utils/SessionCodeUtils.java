package com.xpu.school_guide.utils;

import com.alibaba.fastjson.JSONObject;
import com.xpu.school_guide.dto.AuthorizeCode;
import com.xpu.school_guide.dto.SessionCode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sofency
 * @date 2020/3/25 20:33
 * @package IntelliJ IDEA
 * @description
 */
@Component
public class SessionCodeUtils {
    //根据code 到微信后台拿取数据
    /**
     * 根据code获取openId
     * @param code
     * @return  返回sessionKey  openid token
     */
    public  SessionCode getSessionCode(String code){
        //获取授权的id
        AuthorizeCode authorizeCode = new AuthorizeCode();
        authorizeCode.setJs_code(code);
        System.out.println(authorizeCode.toString());
        OkHttpClient client = new OkHttpClient();
        //构建请求体
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/jscode2session?js_code="
                        +authorizeCode.getJs_code()+"&appid="
                        +authorizeCode.getAppid()+"&secret="
                        +authorizeCode.getSecret()+"&grant_type="
                        +authorizeCode.getGrant_type())
                .get()
                .header("content-type","application/x-www-form-urlencoded")
                .build();
        //进行响应
        try(Response response = client.newCall(request).execute()){
            String str = response.body().string();
            JSONObject jsonObject =JSONObject.parseObject(str);
            String openId = (String) jsonObject.get("openid");
            String sessionKey = (String) jsonObject.get("session_key");
            SessionCode sessionCode = new SessionCode();//返回的是sessionCode
            sessionCode.setOpenId(openId);
            sessionCode.setSessionKey(sessionKey);
            return sessionCode;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("出现异常");
        }
        return null;
    }
}
