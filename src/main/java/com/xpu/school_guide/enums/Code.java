package com.xpu.school_guide.enums;

/**
 * @author sofency
 * @date 2020/5/11 16:05
 * @package IntelliJ IDEA
 * @description
 */
public enum  Code {
    UN_USE(400,"请求失效 请重新登录"),
    CODE_SUCCESS(200,"获取成功"),
    CODE_FAIL(404,"获取失败"),
    CHANGE_FAIL(404,"无能修改别人的地点"),
    NO_SITES(404,"你还没有添加地点"),
    TASK_ADD(200,"添加完成"),
    TASK_ADD_FAIL(200,"添加完成"),
    TASK_NULL(404,"没有任务"),
    TASK_UPDATE(200,"更新成功"),
    TASK_UPDATE_FAIL(404,"更新失败"),
    TASK_FINISH(200,"任务完成"),
    SESSION_UN_KNOW(404,"session异常"),
    UN_KNOW_EXCEPTION(404,"未知异常"),
    DELETE_SUCCESS(200,"删除成功"),
    DELETE_FAIL(404,"删除失败"),
    LOGIN_SUCCESS(200,"登录成功"),
    LOGIN_FAIL(404,"登录失败"),
    SEARCH_SUCCESS(200,"查询成功"),

    SEARCH_FAIL(404,"查询失败"),
    REGISTER_SUCCESS(200,"获取成功"),
    REGISTERED(404,"抱歉你已经注册过"),
    SEARCH_NONE(404,"未查询到合适数据");

    private String msg;
    private int status;

    public String getMsg() {
        return msg;
    }

    public int getStatus() {
        return status;
    }

    Code(int status, String msg) {
        this.msg = msg;
        this.status = status;
    }
}
