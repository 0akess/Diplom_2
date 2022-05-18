package site.stellarburgers.nomoreparties.requests.order;

import io.restassured.response.ValidatableResponse;
import site.stellarburgers.nomoreparties.model.Ingredients;
import site.stellarburgers.nomoreparties.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class PostCreateOrder extends BaseSpecForRequest {

    public ValidatableResponse createOrder(Ingredients createOrder, String token) {

        return given()
                .spec(baseSpecWithToken(token))
                .and()
                .body(createOrder)
                .when()
                .post("/api/orders")
                .then();

    }
}