package com.example.Gestor_Proyectos_API.seguridad.dto.Organizadores;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrganizadoresRegister {

    private String name;

    private String email;

    private String password;
}
