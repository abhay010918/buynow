package com.buynow.exeptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String massage){
        super(massage);
    }
}
