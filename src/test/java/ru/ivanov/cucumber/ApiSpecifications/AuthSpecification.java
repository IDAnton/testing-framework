package ru.ivanov.cucumber.ApiSpecifications;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import ru.ivanov.cucumber.UrlConfigs.UrlConfigurator;

public class AuthSpecification {
    private final static String baseUrl = ConfigFactory.create(UrlConfigurator.class).APIUrl();

    public static RequestSpecification getAuthSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath("/auth")
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification postBookingSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath("/booking")
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification deleteBookingSpec(int bookingId, String authToken) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath("/booking/" + bookingId)
                .addHeader("Cookie", "token=" + authToken)
                .setContentType(ContentType.JSON)
                .build();
    }
}