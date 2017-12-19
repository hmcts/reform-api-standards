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

    // those headers are always filtered out
    public static final Set<String> BASE_SENSITIVE_HEADERS =
        ImmutableSet.of(
            "ServiceAuthorization"
        );

    private final Set<String> sensitiveHeaders;

    // region constructors
    /**
     * Create a new {@link SensitiveHeadersRequestTraceFilter} instance.
     * @param repository the trace repository
     * @param properties the trace properties
     */
    public SensitiveHeadersRequestTraceFilter(
        TraceRepository repository,
        TraceProperties properties
    ) {
        super(repository, properties);
        sensitiveHeaders = BASE_SENSITIVE_HEADERS;
    }

    /**
     * Create a new {@link SensitiveHeadersRequestTraceFilter} instance.
     * @param repository the trace repository
     * @param properties the trace properties
     * @param additionalCustomHeaders additional headers that should also be filtered out
     */
    public SensitiveHeadersRequestTraceFilter(
        TraceRepository repository,
        TraceProperties properties,
        Set<String> additionalCustomHeaders
    ) {
        super(repository, properties);
        sensitiveHeaders = Sets.union(BASE_SENSITIVE_HEADERS, additionalCustomHeaders);
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
