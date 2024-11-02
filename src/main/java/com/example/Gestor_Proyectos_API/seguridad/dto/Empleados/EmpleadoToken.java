package com.example.Gestor_Proyectos_API.seguridad.dto.Empleados;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmpleadoToken {
    private String token;
}
