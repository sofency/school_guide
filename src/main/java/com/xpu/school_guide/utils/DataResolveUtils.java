package com.xpu.school_guide.utils;

import org.springframework.stereotype.Component;

/**
 * @author sofency
 * @date 2020/6/24 0:45
 * @package IntelliJ IDEA
 * @description
 */
@Component
public class DataResolveUtils {
    public static int getInfo(String studentId){
        return Integer.valueOf(studentId.substring(3,5));
    }
}
