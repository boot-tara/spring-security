package com.boot.security.util;

import com.boot.security.model.LoginUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {
    //上下文获取登陆用户
    public static LoginUser getLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            if(authentication instanceof AnonymousAuthenticationToken){
                //匿名游客
                return null;
            }
            else if(authentication instanceof UsernamePasswordAuthenticationToken){
                return  (LoginUser) authentication.getPrincipal();
            }
        }
        return null;
    }
}
