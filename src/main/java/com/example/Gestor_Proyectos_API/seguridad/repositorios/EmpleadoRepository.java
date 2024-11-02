package com.example.Gestor_Proyectos_API.seguridad.repositorios;

import com.example.Gestor_Proyectos_API.seguridad.modelos.Empleados;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleados, Long> {
    Optional<Empleados> findByUser(String user);
    boolean existsByUser(String user);
}
