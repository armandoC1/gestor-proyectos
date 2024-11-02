package com.example.Gestor_Proyectos_API.seguridad.dto.Empleados;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpleadoLogin {
    private String user;

    private String password;
}
