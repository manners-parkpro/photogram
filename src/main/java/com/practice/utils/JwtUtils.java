package com.practice.utils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class JwtUtils {

    public static final String AUTHORITIES_KEY = "auth";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    public static final String BEARER_TYPE = "Bearer ";

    public static final long ACCESS_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000L; // 1일
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
}
