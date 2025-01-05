package roomescape.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import roomescape.auth.application.dto.LoginRequest;
import roomescape.support.RestAssuredTestSupport;

@Component
public class LoginFixture extends RestAssuredTestSupport {

    /**
     * 로그인 요청
     *
     * @return accessToken
     */
    public String login(final LoginRequest loginRequest) {
        final ValidatableResponse loginResponse = RestAssured
                .given().log().all()
                .body(loginRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/login")
                .then().log().all()
                .statusCode(200); // 로그인 성공 검증

        return loginResponse.extract().cookie("accessToken");
    }

    /**
     * 로그인 요청
     *
     * @return accessToken
     */
    public String login(final String email, final String password) {
        return login(new LoginRequest(email, password));
    }
}
