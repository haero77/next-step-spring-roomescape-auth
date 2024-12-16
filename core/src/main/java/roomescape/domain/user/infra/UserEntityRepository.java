package roomescape.domain.user.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.domain.user.domain.User;
import roomescape.domain.user.domain.UserRepository;

@Repository
@RequiredArgsConstructor
public class UserEntityRepository implements UserRepository {

    private final UserJdbcRepository userJdbcRepository;

    @Override
    public User save(final User user) {
        return userJdbcRepository.save(user);
    }
}
