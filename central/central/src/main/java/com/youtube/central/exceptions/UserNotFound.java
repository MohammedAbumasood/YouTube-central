package com.youtube.central.exceptions;


public class UserNotFound extends RuntimeException{
    public UserNotFound(String message){
        super(message);
    }
}
