package com.springRemind.spring_remind.common;

public class CacheKey {

    public static final int DEFAULT_EXPIRE_SEC = 60;
    public static final String USER = "user";
    public static final int USER_EXPIRE_SEC = 60 * 5;
    public static final String BOARD = "board";
    public static final int BOARD_EXPIRE_SEC = 60 * 10;
    public static final String POST = "post";
    public static final String POSTS = "posts";
    public static final int POST_EXPIRE_SEC = 60 * 5;
}
