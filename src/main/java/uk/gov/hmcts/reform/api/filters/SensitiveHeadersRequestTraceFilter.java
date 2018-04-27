package uk.gov.hmcts.reform.api.filters;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.springframework.boot.actuate.trace.http.HttpExchangeTracer;
import org.springframework.boot.actuate.trace.http.Include;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Removes HMCTS specific headers before they are added to the trace.
 */
public class SensitiveHeadersRequestTraceFilter extends HttpExchangeTracer {

    // those headers are always filtered out
    public static final Set<String> BASE_SENSITIVE_HEADERS =
        ImmutableSet.of(
            "ServiceAuthorization"
        );

    private final Set<String> sensitiveHeaders;

    // region constructors
    /**
     * Create a new {@link SensitiveHeadersRequestTraceFilter} instance.
     * @param includes options for HTTP tracing
     */
    public SensitiveHeadersRequestTraceFilter(Set<Include> includes) {
        super(includes);
        sensitiveHeaders = BASE_SENSITIVE_HEADERS;
    }

    /**
     * Create a new {@link SensitiveHeadersRequestTraceFilter} instance.
     * @param includes options for HTTP tracing
     * @param additionalCustomHeaders additional headers that should also be filtered out
     */
    public SensitiveHeadersRequestTraceFilter(
        Set<Include> includes,
        Set<String> additionalCustomHeaders
    ) {
        super(includes);
        sensitiveHeaders = Sets.union(BASE_SENSITIVE_HEADERS, additionalCustomHeaders);
    }
    // endregion

    @Override
    protected void postProcessRequestHeaders(Map<String, List<String>> headers) {
        headers
            .keySet()
            .stream()
            .filter(this::shouldBeRemoved)
            .collect(toSet())
            .forEach(headers::remove);
    }

    private boolean shouldBeRemoved(String header) {
        return sensitiveHeaders
            .stream()
            .anyMatch(header::equalsIgnoreCase);
    }
}
