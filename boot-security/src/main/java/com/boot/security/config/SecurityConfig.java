package com.boot.security.config;


import com.boot.security.filter.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


@EnableGlobalMethodSecurity(prePostEnabled=true)  //开启注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //自动注入处理器
    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    TokenFilter tokenFilter;
    @Autowired
    UserDetailsService userDetailsService;

    //独特的密码加密
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //设置白名单
        http.authorizeRequests()
                .antMatchers("/","/updateMenu", "/*.html", "/favicon.ico", "/css/**", "/js/**", "/fonts/**", "/layui/**", "/img/**",
                        "/v2/api-docs/**", "/swagger-resources/**", "/webjars/**", "/pages/**", "/druid/**",
                        "/statics/**")   //这里设置页面全部放行，如果访问后台的方法无需登陆，吧具体后台的路径写上
                .permitAll().anyRequest().authenticated();
        //自定义登陆界面
        http.formLogin().loginPage("/login.html").loginProcessingUrl("/login").successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler)
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        //解决iframe问题
        http.headers().frameOptions().disable();
        http.headers().cacheControl();
        //
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //认证
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}
