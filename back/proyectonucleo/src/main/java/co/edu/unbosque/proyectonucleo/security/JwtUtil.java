package co.edu.unbosque.proyectonucleo.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	// Debe tener minimo 32 caracteres (256 bits) para HS256
	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration-ms:86400000}")
	private long expiracionMs;

	private SecretKey clave() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String generarToken(String username, String rol) {
		Date ahora = new Date();
		return Jwts.builder()
				.subject(username)
				.claim("rol", rol)
				.issuedAt(ahora)
				.expiration(new Date(ahora.getTime() + expiracionMs))
				.signWith(clave())
				.compact();
	}

	public String extraerUsername(String token) {
		return claims(token).getSubject();
	}

	public String extraerRol(String token) {
		return claims(token).get("rol", String.class);
	}

	public boolean esValido(String token) {
		try {
			claims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private Claims claims(String token) {
		return Jwts.parser().verifyWith(clave()).build().parseSignedClaims(token).getPayload();
	}
}