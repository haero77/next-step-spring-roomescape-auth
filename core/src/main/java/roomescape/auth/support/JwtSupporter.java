package roomescape.auth.support;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import roomescape.auth.application.dto.LoginToken;

/**
 * JwtProvider 컴포넌트가 수행하는 JWT 생성외의 작업(parsing, validate)을 수행하는 컴포넌트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSupporter {

    private final JwtConfig jwtConfig;

    public boolean isValidToken(final LoginToken loginToken) {
        if (!StringUtils.hasText(loginToken.accessToken())) {
            return false;
        }

        final String accessToken = loginToken.accessToken();

        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                    .build()
                    .parseSignedClaims(accessToken);

            return true;
        } catch (JwtException e) {
            log.warn("JWT parsing failed: %s".formatted(e.getMessage()), e);
            return false;
        }
    }
}
