package com.boot.security.filter;

import com.boot.security.model.LoginUser;
import com.boot.security.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Token过滤器  拦截所有的请求   这个也可以
 *
 * @author 小威老师
 *
 *         2017年10月14日
 */
@Component
public class TokenFilter implements Filter {

	private static final String TOKEN_KEY = "token";

	@Autowired
	private TokenService tokenService;
	@Autowired
	private UserDetailsService userDetailsService;
	private static final Long MINUTES_10 = 10 * 60 * 1000L;


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request1 = (HttpServletRequest) request;
		String token = getToken(request1);
		if (StringUtils.isNotBlank(token)) {

			LoginUser loginUser = tokenService.getLoginUser(token);//缓存中取得的用户
			if (loginUser != null) {
				loginUser = checkLoginTime(loginUser);//检测缓存是否宽过期
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser,
						null, loginUser.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);//为了上下文获取登录用户 UserUtil
			}
		}


		chain.doFilter(request, response);
	}
	/**
	 * 校验时间<br>
	 * 过期时间与当前时间对比，临近过期10分钟内的话，自动刷新缓存
	 *
	 * @param loginUser
	 * @return
	 */
	private LoginUser checkLoginTime(LoginUser loginUser) {
		long expireTime = loginUser.getExpireTime();
		long currentTime = System.currentTimeMillis();
		if (expireTime - currentTime <= MINUTES_10) {
			String token = loginUser.getToken();
			loginUser = (LoginUser) userDetailsService.loadUserByUsername(loginUser.getUsername());//防止伪造的token比如uuid泄露
			loginUser.setToken(token);//uuid不能变
			tokenService.refresh(loginUser);
		}
		return loginUser;
	}

	/**
	 * 根据参数或者header获取token
	 *
	 * @param request
	 * @return
	 */
	public  String getToken(HttpServletRequest request) {
		String token = request.getParameter(TOKEN_KEY);
		if (StringUtils.isBlank(token)) {//如果是空的
			token = request.getHeader(TOKEN_KEY);
		}

		return token;
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}



	@Override
	public void destroy() {

	}


}
