package site.stellarburgers.nomoreparties.requests.order;

import io.restassured.response.ValidatableResponse;
import site.stellarburgers.nomoreparties.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class GetOrdersUser extends BaseSpecForRequest {

    public ValidatableResponse getOrdersUser(String token) {

        return given()
                .spec(baseSpecWithToken(token))
                .and()
                .get("/api/orders")
                .then();
    }
}