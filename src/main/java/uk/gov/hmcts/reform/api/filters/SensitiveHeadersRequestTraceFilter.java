package uk.gov.hmcts.reform.api.filters;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.springframework.boot.actuate.trace.TraceProperties;
import org.springframework.boot.actuate.trace.TraceRepository;
import org.springframework.boot.actuate.trace.WebRequestTraceFilter;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Removes HMCTS specific headers before they are added to the trace.
 */
public class SensitiveHeadersRequestTraceFilter extends WebRequestTraceFilter {

    public static final Set<String> DEFAULT_SENSITIVE_HEADERS =
        ImmutableSet.of(
            "ServiceAuthorization"
        );

    private final Set<String> sensitiveHeaders;

    // region constructors
    public SensitiveHeadersRequestTraceFilter(
        TraceRepository repository,
        TraceProperties properties
    ) {
        super(repository, properties);
        sensitiveHeaders = DEFAULT_SENSITIVE_HEADERS;
    }

    public SensitiveHeadersRequestTraceFilter(
        TraceRepository repository,
        TraceProperties properties,
        Set<String> additionalCustomHeaders
    ) {
        super(repository, properties);
        sensitiveHeaders = Sets.union(DEFAULT_SENSITIVE_HEADERS, additionalCustomHeaders);
    }
    // endregion

    @Override
    protected void postProcessRequestHeaders(Map<String, Object> headers) {
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
