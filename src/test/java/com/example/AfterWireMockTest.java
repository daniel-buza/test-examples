package com.example;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
public class AfterWireMockTest {

    private final After after = new After();

    @Test
    public void getContents_firesUpGet_andReturnsResultUnmodified(final WireMockRuntimeInfo wmRuntimeInfo) {
        final String testContent = "some totally random string content";
        stubFor(get("/test-url").willReturn(ok(testContent)));
        assertEquals(testContent, after.getContentsOf("http://localhost:" + wmRuntimeInfo.getHttpPort() + "/test-url"));
    }

}
