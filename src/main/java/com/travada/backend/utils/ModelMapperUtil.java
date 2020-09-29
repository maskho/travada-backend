package com.travada.backend.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperUtil {

    public ModelMapper modelMapperInit() {
        return new ModelMapper();
    }
}

