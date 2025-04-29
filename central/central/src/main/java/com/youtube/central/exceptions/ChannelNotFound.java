package com.youtube.central.exceptions;

public class ChannelNotFound extends RuntimeException{
    public ChannelNotFound(String message)
    {
        super(message);
    }
}
