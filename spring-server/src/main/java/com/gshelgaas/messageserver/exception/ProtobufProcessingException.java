package com.gshelgaas.messageserver.exception;

public class ProtobufProcessingException extends RuntimeException {
    public ProtobufProcessingException(String message) {
        super(message);
    }

    public ProtobufProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}