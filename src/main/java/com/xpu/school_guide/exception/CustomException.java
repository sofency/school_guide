package com.xpu.school_guide.exception;

import com.xpu.school_guide.dto.ResultMsg;
import com.xpu.school_guide.enums.Code;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sofency
 * @date 2020/5/12 6:20
 * @package IntelliJ IDEA
 * @description
 */
@ControllerAdvice
public class CustomException {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResultMsg handlerControllerException(HttpServletRequest request,
                                         Throwable ex,
                                         HttpServletResponse response) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setStatus(Code.UN_KNOW_EXCEPTION.getStatus());
        resultMsg.setMsg(Code.UN_KNOW_EXCEPTION.getMsg());
        return resultMsg;
    }
}
