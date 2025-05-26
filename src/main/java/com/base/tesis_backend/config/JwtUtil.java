package com.base.tesis_backend.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

//Esto es una clase para:
//Generar los tokens JWT
//Validar los tokens JWT recibido
//Obtener el email desde el token.
@Component
public class JwtUtil {
    private static final String SECRET_KEY = "clave-super-secreta-para-el-jwt-que-debe-ser-muy-larga";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; //24hs

    //se encarga de devolver la clave secreta usada para firmar y validar los tokens JWT.
    //asegura que los tokens no fueron alterados y fueron emitidos por esta api.
    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    //Generamos el token JWT con el email como informacion principal
    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email) // Asignamos el "subject" del token (en este caso, el email)
                .setIssuedAt(new Date()) // Fecha en que se generó
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Cuándo expira
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firmamos el token
                .compact(); // Lo compactamos como un String
    }

    //Obtenemos el email del token, con este metodo vamos a saber que usuario esta haciendo
    //una peticion ACA HAGO EXTRACT DEL EMAIL NO DEL TOKEN
    public String extractToken(String token){
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //Valida si el token recibido esta bien firmado, no expiro y no fue alterado.
    public boolean isTokenValid(String token){
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
