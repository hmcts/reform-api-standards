package uk.gov.hmcts.reform.api.filters;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class SensitiveHeadersRequestTraceFilterTest {

    @Test
    public void should_filter_out_sensitive_headers_ignoring_case() {

        // given
        Map<String, Object> headers = new HashMap<>();

        headers.put("OK Header 1", "xxx");
        headers.put("OK Header 2", "xxx");
        headers.put("OK Header 3", "xxx");

        SensitiveHeadersRequestTraceFilter
            .DEFAULT_SENSITIVE_HEADERS
            .forEach(sensitive -> {
                headers.put(sensitive, "some_value");
                headers.put(sensitive.toLowerCase(Locale.ENGLISH), "some_value");
                headers.put(sensitive.toUpperCase(Locale.ENGLISH), "some_value");
            });

        // when
        new SensitiveHeadersRequestTraceFilter(null, null)
            .postProcessRequestHeaders(headers);

        // then
        assertThat(headers)
            .containsOnly(
                entry("OK Header 1", "xxx"),
                entry("OK Header 2", "xxx"),
                entry("OK Header 3", "xxx")
            );
    }

    @Test
    public void should_use_custom_headers_provided_in_constructor() {

        // given
        Set<String> customSensitiveHeaders = ImmutableSet.of(
            "A",
            "B"
        );

        Map<String, Object> headers = new HashMap<>(ImmutableMap.of(
            "A", "1",
            "B", "2",
            "b", "2",
            "C", "3",
            "D", "4"
        ));

        // when
        new SensitiveHeadersRequestTraceFilter(null, null, customSensitiveHeaders)
            .postProcessRequestHeaders(headers);

        // then
        assertThat(headers)
            .containsExactly(
                entry("C", "3"),
                entry("D", "4")
            );
    }
}
