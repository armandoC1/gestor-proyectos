package com.example.Gestor_Proyectos_API.seguridad.repositorios;

import com.example.Gestor_Proyectos_API.seguridad.modelos.Organizador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizadorRepository extends JpaRepository<Organizador, Long> {
    Optional<Organizador> findByCorreo(String email);
    boolean existsByCorreo(String email);
}
