package ru.ivanov.cucumber.ApiSpecifications;

import io.restassured.response.Response;
import ru.ivanov.cucumber.ApiModels.AuthRequest;
import ru.ivanov.cucumber.ApiModels.CreateBookingRequest;

import static io.restassured.RestAssured.given;

public class AuthManager {
    public static Response sendAuthRequest(AuthRequest request){
        return given()
                .spec(AuthSpecification.getAuthSpec())
                .body(request)
                .when()
                .post();
    }

    public static Response sendAuthRequestWithPayload(String payload){
        return given()
                .spec(AuthSpecification.getAuthSpec())
                .body(payload)
                .when()
                .post();
    }

    public static Response sendBookingRequest(CreateBookingRequest request){
        return given()
                .spec(AuthSpecification.postBookingSpec())
                .body(request)
                .when()
                .post();
    }

    public static Response sendDeleteBookingRequest(int bookingId, String authToken){
        return given()
                .spec(AuthSpecification.deleteBookingSpec(bookingId, authToken))
                .delete();
    }
}
