package com.example.Gestor_Proyectos_API.seguridad.configuracion;

import com.example.Gestor_Proyectos_API.seguridad.servicios.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFiler extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void  doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        try {
            final String token = obtenerTokenDelRequest(request);
            final String correo;

            if (token == null){
                System.out.println("Falta el token en la solicitud");
                filterChain.doFilter(request, response);
                return;
            }

            correo = jwtService.getUsernameFromToken(token);
            System.out.println("usuario del token " +correo );

            if (correo != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(correo);
                System.out.println("usuario en el sistema" + userDetails.getUsername());

                if (jwtService.isTokenValid(token, userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception e){
            e.printStackTrace();
            filterChain.doFilter(request, response);
        }
    }

    private String obtenerTokenDelRequest(HttpServletRequest request){
        final String authHeader =  request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("Authorization header: " + authHeader);
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }
}
