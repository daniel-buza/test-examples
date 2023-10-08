package com.example;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
public class BeforeTest {

    private static final Before BEFORE_INSTANCE = new Before();

    @Test
    public void testCall(final WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(get("/test-url").willReturn(ok(
                "<p class=\"resource-title\">\nFirst Title\n.*</p><p class=\"resource-title\">\nOther Title\n.*</p>")));

        final var titles = BEFORE_INSTANCE.getTitlesFromMainPage("http://localhost:"+wmRuntimeInfo.getHttpPort()+"/test-url");
        assertEquals(List.of("First Title", "Other Title"), titles);
    }
}
