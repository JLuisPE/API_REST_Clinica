package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    //Traer el valor de la variable de entorno
    @Value("${api.security.secret}")
    private String apiSecret;

    public String generarToken(Usuario usuario){
        try {
            // Otra Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            //String token = JWT.create()
            return JWT.create()
                    .withIssuer("voll med")
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            // Invalid Signing configuration / Couldn't convert Claims.
            throw new RuntimeException("error al generar token jwt", exception);
        }
    }

    public String getSubject(String token) {
        if(token == null){
            throw new RuntimeException();
        }
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret); // validacion de la firma
            verifier = JWT.require(algorithm)
                    .withIssuer("voll med") // validacion del emisor del token
                    .build()
                    .verify(token); // verificar el token
            verifier.getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido o expirado!");
        }
        // Validación
        if(verifier.getSubject() == null){
            throw new RuntimeException("Verifer invalido");
        }
        return verifier.getSubject();
    }

//    public String getSubject(String tokenJWT) {
//        try {
//            var algoritmo = Algorithm.HMAC256(apiSecret);
//            return JWT.require(algoritmo)
//                    .withIssuer("API Voll.med")
//                    .build()
//                    .verify(tokenJWT)
//                    .getSubject();
//        } catch (JWTVerificationException exception) {
//            throw new RuntimeException("Token JWT inválido o expirado!");
//        }
//    }

    //private Instant generarFechaExpiracion(Integer epiracion) { para pasar el dato como variable
    private Instant generarFechaExpiracion() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-06:00"));
    }
}
