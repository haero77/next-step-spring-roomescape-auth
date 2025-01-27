package roomescape.auth.application;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.application.dto.JwtPayload;
import roomescape.auth.application.dto.LoginRequest;
import roomescape.auth.application.dto.LoginToken;
import roomescape.auth.exception.InvalidLoginRequestException;
import roomescape.auth.support.JwtProvider;
import roomescape.auth.support.JwtSupporter;
import roomescape.domain.user.domain.User;
import roomescape.domain.user.domain.UserReader;
import roomescape.domain.user.domain.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserReader userReader;
    private final JwtProvider jwtProvider;
    private final JwtSupporter jwtSupporter;

    public LoginToken createLoginToken(final LoginRequest loginRequest) {
        final Optional<User> userOpt = userRepository.findByEmail(loginRequest.email());
        if (userOpt.isEmpty()) {
            throw new InvalidLoginRequestException("email {%s} is invalid".formatted(loginRequest.email()));
        }

        final User user = userOpt.get();
        verifyPasswordMatches(user, loginRequest.password());

        final String token = jwtProvider.createToken(new JwtPayload(user.getRole(), user.getEmail()));
        return new LoginToken(token);
    }

    public User getUserByLoginToken(LoginToken loginToken) {
        Claims claims = jwtSupporter.extractClaims(loginToken);
        String email = claims.get("email", String.class);
        return userReader.getByEmail(email);
    }

    private void verifyPasswordMatches(final User user, final String password) {
        if (!user.matchesPassword(password)) {
            throw new InvalidLoginRequestException("password not matched");
        }
    }
}
