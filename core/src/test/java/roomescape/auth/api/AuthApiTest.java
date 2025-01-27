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
import roomescape.fixture.LoginFixture;
import roomescape.support.RestAssuredTestSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

class AuthApiTest extends RestAssuredTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LoginFixture loginFixture;

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

    // todo: 로그인 실패 시 401 반환하는 API 테스트 추가

    // todo: logout API 테스트 추가

    @DisplayName("로그인한 유저의 인증 정보 조회 시 유저 이름과 권한 반환")
    @Test
    void login_check() {
        // given
        final User user = User.builder()
                .role(UserRole.CUSTOMER)
                .email("email")
                .password("password")
                .name("user-name")
                .build();
        userRepository.save(user);

        final String accessToken = loginFixture.login(user);

        // when
        final ValidatableResponse actual = RestAssured
                .given().log().all()
                .cookie("accessToken", accessToken) // fixme: 인증이 필요한 모든 API에 대해 accessToken을 추가해야하는 번거로움을 어떻게 해결할지?
                .when().get("/api/login/check")
                .then().log().all();

        // then
        actual
                .statusCode(200)
                .body("data.userName", equalTo("user-name"))
                .body("data.userRole", equalTo(UserRole.CUSTOMER.name()))
                .extract()
                .response();
    }
}
