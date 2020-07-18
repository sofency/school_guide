package com.xpu.school_guide.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.xpu.school_guide.dto.ResultMsg;
import com.xpu.school_guide.enums.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    RedisTemplate<String,String> redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    public LoginInterceptor(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //处理登录凭证
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o) throws Exception {
        String token = httpServletRequest.getParameter("token");//登录的token
        String s = redisTemplate.opsForValue().get(token);
        if(s==null){//说明缓存失效
            ResultMsg resultMsg = new ResultMsg();
            resultMsg.setStatus(Code.UN_USE.getStatus());
            resultMsg.setMsg(Code.UN_USE.getMsg());
            String string = JSONObject.toJSONString(resultMsg);
            returnJson(response,string);
        }
       if(!Objects.equals(token, "")){
           return true;
       }else{
           return false;
       }
    }
    private void returnJson(HttpServletResponse response, String json) throws Exception{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);

        } catch (IOException e) {
            log.error("response error", e);
        }
    }
}