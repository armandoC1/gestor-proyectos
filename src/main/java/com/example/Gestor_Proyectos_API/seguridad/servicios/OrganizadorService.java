package com.example.Gestor_Proyectos_API.seguridad.servicios;

import com.example.Gestor_Proyectos_API.seguridad.dto.Organizadores.OrganizadoresExit;
import com.example.Gestor_Proyectos_API.seguridad.dto.Organizadores.OrganizadoresLogin;
import com.example.Gestor_Proyectos_API.seguridad.dto.Organizadores.OrganizadoresRegister;
import com.example.Gestor_Proyectos_API.seguridad.dto.Organizadores.OrganizadoresToken;
import com.example.Gestor_Proyectos_API.seguridad.modelos.Organizador;
import com.example.Gestor_Proyectos_API.seguridad.repositorios.OrganizadorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OrganizadorService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrganizadorRepository organizadorRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public OrganizadoresToken login (OrganizadoresLogin emailRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(emailRequest.getEmail(),
                emailRequest.getPassword()));
        Organizador organizador = organizadorRepository.findByCorreo(emailRequest.getEmail()).orElseThrow();
        String token = jwtService.getToken(organizador);
        return OrganizadoresToken.builder()
                .tokem(token)
                .build();
    }

    public OrganizadoresToken registro (OrganizadoresRegister organizadoresRegister){
        if (organizadorRepository.existsByCorreo(organizadoresRegister.getEmail())){
            throw new IllegalArgumentException("El correo ya está en uso");
        }
        Organizador organizador = Organizador.builder()
                .name(organizadoresRegister.getName())
                .email(organizadoresRegister.getEmail())
                .password(passwordEncoder.encode(organizadoresRegister.getPassword()))
                .build();

        organizador = organizadorRepository.save(organizador);

        String token = jwtService.getToken(organizador);

        return OrganizadoresToken.builder()
                .tokem(token)
                .build();
    }


}
