package com.practice.sharemate.exception;

public class ItemHasAnotherUserException extends RuntimeException {
    public ItemHasAnotherUserException(String message) {
        super(message);
    }
}
