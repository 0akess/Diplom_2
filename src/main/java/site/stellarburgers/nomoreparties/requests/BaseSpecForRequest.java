package site.stellarburgers.nomoreparties.requests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseSpecForRequest {

    private final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    protected RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }

    protected RequestSpecification baseSpecWithToken(String token) {
        return new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }
}