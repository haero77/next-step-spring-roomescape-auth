package roomescape.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.application.dto.LoginRequest;
import roomescape.auth.application.dto.LoginToken;
import roomescape.domain.user.domain.User;
import roomescape.domain.user.domain.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginToken createAuthToken(final LoginRequest loginRequest) {
        verifyValidLoginRequest(loginRequest);

    }

    private void verifyValidLoginRequest(final LoginRequest loginRequest) {
        final Optional<User> userOpt = userRepository.findByEmail(loginRequest.email());
        if (userOpt.isEmpty()) {
            throw new InvalidLoginRequestException("email {%s} is invalid".formatted(loginRequest.email()));
        }

        final User user = userOpt.get();
        if (user.matchesPassword(loginRequest.password())) {
            throw new InvalidLoginRequestException("password {%s} not matched".formatted(loginRequest.password()));
        }
    }
}
