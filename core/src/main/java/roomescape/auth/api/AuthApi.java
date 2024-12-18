package roomescape.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.application.AuthService;
import roomescape.auth.application.dto.LoginRequest;
import roomescape.global.rest.ApiResponse;

@RestController
@RequiredArgsConstructor
public class AuthApi {

    private final AuthService authService;

    @PostMapping("/api/login")
    public ApiResponse<Void> login(@RequestBody final LoginRequest loginRequest) {
        authService.createAuthToken(loginRequest);
    }
}
