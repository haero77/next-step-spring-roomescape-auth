package roomescape.auth.application;

import org.springframework.http.HttpStatus;
import roomescape.domain.common.exception.CustomErrorCode;

public class InvalidLoginRequestException extends UnauthorizedException {

    protected InvalidLoginRequestException(final String message) {
        super(HttpStatus.UNAUTHORIZED.value(), CustomErrorCode.A401, message);
    }
}
