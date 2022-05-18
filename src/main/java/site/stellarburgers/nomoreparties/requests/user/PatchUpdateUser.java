package site.stellarburgers.nomoreparties.requests.user;

import io.restassured.response.ValidatableResponse;
import site.stellarburgers.nomoreparties.model.User;
import site.stellarburgers.nomoreparties.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class PatchUpdateUser extends BaseSpecForRequest {

    public ValidatableResponse updateUser(User user, String token) {

        return given()
                .spec(baseSpecWithToken(token))
                .and()
                .body(user)
                .when()
                .patch("/api/auth/user")
                .then();
    }
}