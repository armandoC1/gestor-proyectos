package com.example.Gestor_Proyectos_API.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.*;

@Configuration
public class ModelMappedConfig {
    @Bean
    public ModelMapper modelMapper (){
        return new ModelMapper();
    }
}
