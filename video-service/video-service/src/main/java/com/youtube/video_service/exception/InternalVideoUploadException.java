package com.youtube.video_service.exception;

public class InternalVideoUploadException extends RuntimeException{
    public InternalVideoUploadException(String message){
        super(message);
    }
}
