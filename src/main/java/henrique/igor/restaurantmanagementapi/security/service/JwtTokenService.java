package henrique.igor.restaurantmanagementapi.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.InternalUnexpectedException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.UnauthorizedException;
import henrique.igor.restaurantmanagementapi.security.dto.UserDetailsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtTokenService {

    @Value("${custom.jwt.secret}")
    private String secret;

    @Value("${custom.jwt.expiration}")
    private Long expiration;

    @Value("${custom.jwt.issuer}")
    private String issuer;

    private final ZoneOffset zoneOffset = ZoneOffset.of("-03:00");

    public String generateToken(UserDetailsDTO user){
        try{
            var algorithm = Algorithm.HMAC256(this.secret);

            return JWT.create()
                    .withIssuer(this.issuer)
                    .withSubject(user.getUser().getUserId().toString())
                    .withIssuedAt(LocalDateTime.now().toInstant(this.zoneOffset))
                    .withExpiresAt(LocalDateTime.now().plusMinutes(this.expiration).toInstant(this.zoneOffset))
                    .withPayload(Map.of("user", user.toMap()))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new InternalUnexpectedException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    public UUID getUserId(String token){
        try{
            var  algorithm = Algorithm.HMAC256(this.secret);

            String id = JWT.require(algorithm)
                    .withIssuer(this.issuer)
                    .build()
                    .verify(token)
                    .getSubject();

            return UUID.fromString(id);

        } catch (JWTVerificationException jwtVerificationException) {
            throw new UnauthorizedException(jwtVerificationException);
        }
    }
}
