package roomescape.auth.exception;

import roomescape.domain.common.exception.BusinessException;
import roomescape.domain.common.exception.CustomErrorCode;

public class UnauthorizedException extends BusinessException {

    protected UnauthorizedException(final int statusCode, final CustomErrorCode errorCode, final String message) {
        super(statusCode, errorCode, message);
    }
}
