package com.practice.sharemate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateEmail(EmailAlreadyExistsException ex){
        return new ErrorResponse(ex.getMessage());

    }

    @ExceptionHandler(ItemHasAnotherUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAnotherUser(ItemHasAnotherUserException ex){
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundItem(ItemNotFoundException ex){
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundUser(UserNotFoundException ex){
        return new ErrorResponse(ex.getMessage());
    }
}
