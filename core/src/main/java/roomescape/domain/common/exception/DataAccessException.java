package roomescape.domain.common.exception;

public class DataAccessException extends RuntimeException{

    public DataAccessException(final String message) {
        super(message);
    }
}
