package com.alex.schwartzman.fivehundredpx.exception;

public class DeveloperErrorException extends ServerErrorException {

    @SuppressWarnings("unused")
    public DeveloperErrorException(String errorCode, String errorDetails) {
        super(errorCode, errorDetails);
    }
}
