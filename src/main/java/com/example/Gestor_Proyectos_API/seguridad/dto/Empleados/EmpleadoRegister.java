package com.example.Gestor_Proyectos_API.seguridad.dto.Empleados;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpleadoRegister {

    private Long idEmpleado;

    private String name;

    private String user;

    private String password;

    private String contact;

    private String position;
}
