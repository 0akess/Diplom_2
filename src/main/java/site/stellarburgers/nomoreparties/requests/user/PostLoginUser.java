package site.stellarburgers.nomoreparties.requests.user;

import io.restassured.response.ValidatableResponse;
import site.stellarburgers.nomoreparties.model.User;
import site.stellarburgers.nomoreparties.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class PostLoginUser extends BaseSpecForRequest {

    public ValidatableResponse loginUser(User loginUser) {

        return given()
                .spec(baseSpec())
                .and()
                .body(loginUser)
                .when()
                .post("/api/auth/login")
                .then();
    }
}