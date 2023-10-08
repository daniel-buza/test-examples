package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class AfterTest {

    @Spy private After after = new After();

    @Test
    public void mainFlowFetchesAndExtractsContent() {
        doReturn("contents").when(after).getContentsOf("test-url");
        doReturn(List.of("title1", "title2")).when(after).extractTitlesFrom("contents");

        assertEquals(List.of("title1", "title2"), after.getTitlesFromMainPage("test-url"));
    }

    @Test
    public void extractContent() {
        final String htmlContent = "<p class=\"resource-title\">\nFirst Title\n.*</p><p class=\"resource-title\">\nOther Title\n.*</p>";
        assertEquals(List.of("First Title", "Other Title"), after.extractTitlesFrom(htmlContent));
    }
}
