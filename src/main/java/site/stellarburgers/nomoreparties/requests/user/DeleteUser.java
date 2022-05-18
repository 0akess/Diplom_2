package site.stellarburgers.nomoreparties.requests.user;

import io.restassured.response.ValidatableResponse;
import site.stellarburgers.nomoreparties.requests.BaseSpecForRequest;


import static io.restassured.RestAssured.given;

public class DeleteUser extends BaseSpecForRequest {

    public ValidatableResponse deleteUser(String token) {

        return given()
                .spec(baseSpecWithToken(token))
                .and()
                .delete("/api/auth/user")
                .then();
    }
}