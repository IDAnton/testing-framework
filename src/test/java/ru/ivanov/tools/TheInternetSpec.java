package ru.ivanov.tools;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class TheInternetSpec {
    private final static String url = "https://the-internet.herokuapp.com";

    public static RequestSpecification getSuccessSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setBasePath("/status_codes/200")
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification getFailSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setBasePath("/status_codes/500")
                .setContentType(ContentType.JSON)
                .build();
    }
}
