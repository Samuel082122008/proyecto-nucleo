package co.edu.unbosque.proyectonucleo.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Valida el token JWT y deja al usuario autenticado usando solo los datos del
 * token (sin consultar la base en cada peticion).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")
				&& SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				Claims c = jwtUtil.leer(header.substring(7));
				String rol = c.get("rol", String.class);
				if (rol != null) {
					Object d = c.get("docenteId");
					Long docenteId = d instanceof Number n ? n.longValue() : null;
					UsuarioAutenticado principal = new UsuarioAutenticado(c.getSubject(), rol, docenteId);

					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
							principal, null, List.of(new SimpleGrantedAuthority("ROLE_" + rol)));
					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			} catch (JwtException | IllegalArgumentException e) {
				// Token invalido o vencido: queda sin autenticar (401)
			}
		}

		filterChain.doFilter(request, response);
	}
}