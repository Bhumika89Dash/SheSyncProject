package com.patientmanage.patientservice.exception;

import java.util.UUID;

public class IdNotExistsException extends RuntimeException{
    public IdNotExistsException(String ex, UUID id){
        super(ex+" "+id);
    }
}