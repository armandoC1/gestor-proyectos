package com.example.Gestor_Proyectos_API.seguridad.dto.Empleados;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpleadosExit {

    private Long idEmpleado;

    private String name;

    private String user;

    private Boolean disponibility;

    private String contact;

    private String position;
}
