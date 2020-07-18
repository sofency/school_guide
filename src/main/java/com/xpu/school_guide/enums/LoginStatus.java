package com.xpu.school_guide.enums;

/**
 * @author sofency
 * @date 2020/6/23 23:24
 * @package IntelliJ IDEA
 * @description
 */
public enum LoginStatus {
    REGISTER(0,"注册"),
    UN_REGISTER(1,"未注册"),
    FINISH(2,"开学任务已完成"),
    UN_FINISH(3,"开学任务未完成");
    private int status;
    private String msg;

    LoginStatus(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
