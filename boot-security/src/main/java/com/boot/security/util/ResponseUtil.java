package com.boot.security.util;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;

//没有responsebody向前端传输json数据的工具类
public class ResponseUtil {

    public static void responseJSON(HttpServletResponse response,int status,Object data){

        try {
            response.setStatus(status);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(data));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
