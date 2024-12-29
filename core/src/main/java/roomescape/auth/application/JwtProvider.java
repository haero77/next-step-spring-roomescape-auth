package roomescape.auth.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.application.dto.JwtPayload;
import roomescape.domain.common.ClockHolder;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expiration-time}")
    private long validityInMilliseconds;

    private final ClockHolder clockHolder;

    public String createToken(final JwtPayload jwtPayload) {
        final Date issuedAt = Date.from(clockHolder.getCurrentSeoulZonedDateTime().toInstant());
        final Date expirationDate = new Date(issuedAt.getTime() + this.validityInMilliseconds);

        return Jwts.builder()
                .claim("role", jwtPayload.role())
                .claim("email", jwtPayload.email())
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(this.secretKey.getBytes()))
                .compact();
    }
}
