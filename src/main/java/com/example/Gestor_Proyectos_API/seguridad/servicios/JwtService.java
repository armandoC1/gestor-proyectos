package com.example.Gestor_Proyectos_API.seguridad.servicios;

import com.example.Gestor_Proyectos_API.seguridad.modelos.Empleados;
import com.example.Gestor_Proyectos_API.seguridad.modelos.Organizador;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String getToken(Object usuario) {
        String username;
        String rol;

        if (usuario instanceof Organizador) {
            Organizador organizador = (Organizador) usuario;
            username = organizador.getUsername();
            rol = "organizador";
        }
        // Verificar si el usuario es un empleado
        else if (usuario instanceof Empleados) {
            Empleados empleado = (Empleados) usuario;
            username = empleado.getUsername();
            rol = "empleado";
        } else {
            throw new IllegalArgumentException("Tipo de usuario no soportado");
        }

        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rol", rol);
        return generarToken(extraClaims, username);
    }

    private String generarToken(HashMap<String, Object> extraClaims, String username) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora de validez
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String login = getUsernameFromToken(token);
        return (login.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

}
