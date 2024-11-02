package com.example.Gestor_Proyectos_API.seguridad.configuracion;

import com.example.Gestor_Proyectos_API.seguridad.repositorios.OrganizadorRepository;
import com.example.Gestor_Proyectos_API.seguridad.repositorios.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AplicationConfig {

    @Autowired
    private OrganizadorRepository organizadorRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailService() {
        return username -> {
            UserDetails user = organizadorRepository.findByCorreo(username)
                    .orElse(null);

            if (user != null) {
                return user;
            }

            user = empleadoRepository.findByUser(username)
                    .orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado"));

            return user;
        };
    }
}
