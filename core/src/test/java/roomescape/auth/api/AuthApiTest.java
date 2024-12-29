package roomescape.auth.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import roomescape.auth.application.dto.LoginRequest;
import roomescape.domain.user.domain.User;
import roomescape.domain.user.domain.UserRepository;
import roomescape.domain.user.domain.UserRole;
import roomescape.support.RestAssuredTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class AuthApiTest extends RestAssuredTestSupport {

    @Autowired
    UserRepository userRepository;

    @DisplayName("로그인에 성공 시 로그인 토큰(accessToken)을 발급한다")
    @Test
    void issue_login_token() {
        // given
        final User user = User.builder()
                .role(UserRole.CUSTOMER)
                .email("email")
                .password("password")
                .name("name")
                .build();
        userRepository.save(user);

        final LoginRequest loginRequest = new LoginRequest("email", "password");

        // when
        final ValidatableResponse actual = RestAssured
                .given().log().all()
                .body(loginRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/login")
                .then().log().all();

        // then
        final Response response = actual
                .statusCode(200)
                .extract()
                .response();

        final String accessToken = response.getCookie("accessToken");
        assertThat(accessToken).isNotBlank();
    }
}
