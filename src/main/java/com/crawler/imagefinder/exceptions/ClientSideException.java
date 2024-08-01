package com.crawler.imagefinder.exceptions;

import java.io.Serial;

public class ClientSideException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public ClientSideException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientSideException(String message) {
        super(message);
    }
}
