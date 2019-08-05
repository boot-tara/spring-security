package com.boot.security.service.impl;

import com.boot.security.model.LoginUser;
import com.boot.security.model.Token;
import com.boot.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

//生成jwt的方法
@Service
public class TokenServiceImpl implements TokenService {
    private static final  String  LOING_USER_KEY="LOING_USER_KEY";

    //私钥
    @Value("${token.jwtSecret}")
    private String jwtSecret;             //注意  私钥存代码厘米，不要存在配置文件里面

    //缓存过期时间
    @Value("${token.expire.seconds}")  //7200秒
    private Integer expireSeconds;

    @Autowired
    private RedisTemplate<String, LoginUser> redisTemplate;

    @Override
    public Token saveToken(LoginUser loginUser) {
        loginUser.setToken(UUID.randomUUID().toString());
        //缓存redis
        cacheLoginUser(loginUser);
        //根据缓存的uuid生成jwttoken
        String jwtToken=createJWTToken(loginUser);
        return new Token(jwtToken,loginUser.getLoginTime());
    }

    private String createJWTToken(LoginUser loginUser) {
        Map<String,Object> claims=new HashMap<>();
        claims.put(LOING_USER_KEY,loginUser.getToken());  //不能放重要信息
        String jwtToken = Jwts.builder().addClaims(claims).signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
        return jwtToken;
    }


    private void cacheLoginUser(LoginUser loginUser) {
        loginUser.setLoginTime(new Date().getTime());
        loginUser.setExpireTime(new Date().getTime()+expireSeconds);
        redisTemplate.opsForValue().set("token:"+loginUser.getToken(),loginUser,expireSeconds, TimeUnit.SECONDS);
    }


    //退出登陆清空缓存
    @Override
    public void deleteToken(String token) {
        String uuid=getUUIDfromToken(token);
        if(StringUtils.isNotBlank(uuid)){
            //去缓存中查一次
            LoginUser loginUser = redisTemplate.opsForValue().get("token:" + uuid);
            if(loginUser!=null){
                redisTemplate.delete("token:"+uuid);
            }

        }

    }

    //解析token获取登陆用户
    @Override
    public LoginUser getLoginUser(String token) {
        String uuid = getUUIDfromToken(token);
        if(StringUtils.isNotBlank(uuid)){
            return  redisTemplate.opsForValue().get("token:" + uuid);
        }

        return null;
    }

    //刷新过期时间
    @Override
    public void refresh(LoginUser loginUser) {
        cacheLoginUser(loginUser);
    }


    //解析token
    private String getUUIDfromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return  (String)claims.get(LOING_USER_KEY);
    }
}
