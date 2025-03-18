package com.springRemind.spring_remind.advice.exception;

public class AccessdeniedException extends RuntimeException{
    public AccessdeniedException(String msg, Throwable t) {
        super(msg, t);
    }

    public AccessdeniedException(String msg) {
        super(msg);
    }

    public AccessdeniedException() {
        super();
    }
}
