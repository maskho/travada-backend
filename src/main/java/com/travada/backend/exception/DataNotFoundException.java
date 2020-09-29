package com.travada.backend.exception;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(Long id){
        super("Data dengan id: "+id+" tidak ada di database");
    }
    public DataNotFoundException(String nama){
        super("Data "+nama+" tidak ada di database");
    }
}

