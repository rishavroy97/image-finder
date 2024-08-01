package com.crawler.imagefinder.exceptions;

import java.io.Serial;

public class ServerSideException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    public ServerSideException(){
        super("Internal Server Error");
    }
}
