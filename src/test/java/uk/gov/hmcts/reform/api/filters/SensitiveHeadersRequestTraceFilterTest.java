package uk.gov.hmcts.reform.api.filters;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static uk.gov.hmcts.reform.api.filters.SensitiveHeadersRequestTraceFilter.BASE_SENSITIVE_HEADERS;

public class SensitiveHeadersRequestTraceFilterTest {

    @Test
    public void should_filter_out_sensitive_headers_ignoring_case() {

        // given
        Map<String, Object> headers = new HashMap<>();

        headers.put("OK Header 1", "aaa");
        headers.put("OK Header 2", "bbb");

        Set<String> customSensitiveHeaders =
            ImmutableSet.of(
                "Custom Sensitive Header 1",
                "Custom Sensitive Header 2"
            );

        // add different variants of sensitive headers to a map
        Sets.union(BASE_SENSITIVE_HEADERS, customSensitiveHeaders)
            .forEach(it -> {
                headers.put(it, "some_value");
                headers.put(it.toLowerCase(Locale.ENGLISH), "some_value");
                headers.put(it.toUpperCase(Locale.ENGLISH), "some_value");
            });

        // when
        new SensitiveHeadersRequestTraceFilter(null, null, customSensitiveHeaders)
            .postProcessRequestHeaders(headers);

        // then
        assertThat(headers)
            .containsOnly(
                entry("OK Header 1", "aaa"),
                entry("OK Header 2", "bbb")
            );
    }
}
