package com.example.userauthenticationservice.exceptions;

public class InvalidTokenException extends Exception{
    public InvalidTokenException(String message) {
        super(message);
    }
}