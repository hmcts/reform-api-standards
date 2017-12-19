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

public class SensitiveHeadersRequestTraceFilterTest {

    @Test
    public void should_filter_out_sensitive_headers_ignoring_case() {

        // given
        Map<String, Object> headers = new HashMap<>();

        headers.put("OK Header 1", "xxx");
        headers.put("OK Header 2", "xxx");
        headers.put("OK Header 3", "xxx");

        Set<String> customSensitiveHeaders =
            ImmutableSet.of(
                "Custom Sensitive Header 1",
                "Custom Sensitive Header 2"
            );

        Sets.union(SensitiveHeadersRequestTraceFilter.DEFAULT_SENSITIVE_HEADERS, customSensitiveHeaders)
            .forEach(sensitive -> {
                headers.put(sensitive, "some_value");
                headers.put(sensitive.toLowerCase(Locale.ENGLISH), "some_value");
                headers.put(sensitive.toUpperCase(Locale.ENGLISH), "some_value");
            });

        // when
        new SensitiveHeadersRequestTraceFilter(null, null, customSensitiveHeaders)
            .postProcessRequestHeaders(headers);

        // then
        assertThat(headers)
            .containsOnly(
                entry("OK Header 1", "xxx"),
                entry("OK Header 2", "xxx"),
                entry("OK Header 3", "xxx")
            );
    }
}
