package com.base.tesis_backend.config;

import com.base.tesis_backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Esta clase se encarga de =
//Interceptar Cada solicitud Http
//Extraer el token JWT del encabezado Authorizarion
//Validar el token
//Si el token es valido, establece el usuario autenticado en el contexto de seguridad de Spring
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        //permitir preflight request de CORS
        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        String userEmail = null;
        String jwtToken = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7); //quitamos el "Bearer "

            if (jwtUtil.isTokenValid(jwtToken)) {
                userEmail = jwtUtil.extractToken(jwtToken);
            } else {
                System.out.println("Token JWT Invalido o Expirado");
            }
        }

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //validamos por consola que el usuario que queremos autenticar sea el correcto
            //TEST
            System.out.println("Usuario autenticado: " + userEmail);

            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
