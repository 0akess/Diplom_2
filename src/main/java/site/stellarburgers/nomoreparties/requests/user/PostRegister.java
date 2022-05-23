package site.stellarburgers.nomoreparties.requests.user;

import io.restassured.response.ValidatableResponse;
import site.stellarburgers.nomoreparties.model.User;
import site.stellarburgers.nomoreparties.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class PostRegister extends BaseSpecForRequest {

    public ValidatableResponse registerUser(User registerUser) {

        return given()
                .spec(baseSpec())
                .and()
                .body(registerUser)
                .when()
                .post("/api/auth/register")
                .then();
    }
}