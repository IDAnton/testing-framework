package ru.ivanov.cucumber.ApiModels;

import java.math.BigDecimal;

public record CreateBookingRequest(String firstname, String lastname,
                                   BigDecimal totalprice, boolean depositpaid,
                                   Bookingdates bookingdates, String additionalneeds) { }
