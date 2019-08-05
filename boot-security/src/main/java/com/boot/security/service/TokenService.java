package com.boot.security.service;

import com.boot.security.model.LoginUser;
import com.boot.security.model.Token;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    Token saveToken(LoginUser loginUser);

    void deleteToken(String token);

    LoginUser getLoginUser(String token);

    void refresh(LoginUser loginUser);
}
