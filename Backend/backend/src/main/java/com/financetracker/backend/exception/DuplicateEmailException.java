package com.financetracker.backend.exception;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String messgae){
        super(messgae);
    }
}
