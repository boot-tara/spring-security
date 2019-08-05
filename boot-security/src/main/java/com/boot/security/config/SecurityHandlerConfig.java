package com.boot.security.config;

import com.boot.security.filter.TokenFilter;
import com.boot.security.model.LoginUser;
import com.boot.security.model.Token;
import com.boot.security.service.TokenService;
import com.boot.security.util.ResponseInfo;
import com.boot.security.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityHandlerConfig {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenFilter tokenFilter;

    //认证成功
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                LoginUser loginUser = (LoginUser) authentication.getPrincipal();
                //生成token
                Token token=tokenService.saveToken(loginUser);//loginuser 有了权限，uuid,进行缓存（有过期时间）     jwt根据uuid生成token
                ResponseUtil.responseJSON(response, HttpStatus.OK.value(),token);
            }
        };
    }

    //认证失败
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                String msg="";
                if(exception instanceof BadCredentialsException){
                 msg="密码错误";
                }
                else {
                    msg=exception.getMessage();
                }
                 //返回前端  对象转成json字符串
                ResponseInfo responseInfo=new ResponseInfo(HttpStatus.UNAUTHORIZED.value()+"",msg);
                ResponseUtil.responseJSON(response,HttpStatus.UNAUTHORIZED.value(),responseInfo);
            }
        };
    }

    //出现异常，针对匿名
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                ResponseInfo info = new ResponseInfo(HttpStatus.UNAUTHORIZED.value() + "", "请先登录");
                ResponseUtil.responseJSON(response, HttpStatus.UNAUTHORIZED.value(), info);
            }
        };
    }


    //退出登陆的处理器
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                ResponseInfo info = new ResponseInfo(HttpStatus.OK.value() + "", "退出成功");
                //从header中拿到根据token清缓存
               // String token=tokenFilter.getToken(request);
               // tokenService.deleteToken(token);
                ResponseUtil.responseJSON(response, HttpStatus.OK.value(), info);
            }
        };
    }
}
