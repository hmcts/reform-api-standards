package uk.gov.hmcts.reform.api.versioning.spring.version;

import com.github.zafarkhaja.semver.Version;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;

public class VersionedResourceRequestCondition extends AbstractRequestCondition<VersionedResourceRequestCondition> {
    private final Set<String> availableVersions;
    private Logger logger = LoggerFactory.getLogger(VersionedResourceRequestCondition.class);
    private final Set<Version> versions;
    private final String acceptedMediaType;

    public VersionedResourceRequestCondition(String acceptedMediaType, String supported,
            Set<String> availableVersions) {
        this(acceptedMediaType, new HashSet<Version>(Arrays.asList(Version.valueOf(supported))), availableVersions);
    }

    public VersionedResourceRequestCondition(String acceptedMediaType, Collection<Version> versions,
            Set<String> availableVersions) {
        this.acceptedMediaType = acceptedMediaType;
        this.versions = Collections.unmodifiableSet(new HashSet<Version>(versions));
        this.availableVersions = availableVersions;
        logger.debug("Annotated Mapping: "+ versions+ " -> Created: "+ this.toString());
    }

    @Override
    public VersionedResourceRequestCondition combine(VersionedResourceRequestCondition other) {
        logger.debug("Combining:\n{}\n{}", this, other);

        Set<Version> newVersions = new LinkedHashSet<Version>(this.versions);
        newVersions.addAll(other.versions);
        String newMediaType;
        if (StringUtils.hasText(this.acceptedMediaType)
            && StringUtils.hasText(other.acceptedMediaType)
            && !this.acceptedMediaType.equals(other.acceptedMediaType)) {
            throw new IllegalArgumentException("Both conditions should have the same media type. " + this.acceptedMediaType + " =!= " + other.acceptedMediaType);
        } else if (StringUtils.hasText(this.acceptedMediaType)) {
            newMediaType = this.acceptedMediaType;
        } else {
            newMediaType = other.acceptedMediaType;
        }
        // TODO: combine availableVersions
        return new VersionedResourceRequestCondition(newMediaType, newVersions, availableVersions);
    }

    @Override
    public VersionedResourceRequestCondition getMatchingCondition(HttpServletRequest request) {

        String mediaType = request.getHeader("Accept");
        MimeType type;
        try {
            type = MimeTypeUtils.parseMimeType(mediaType);
        }
        catch (InvalidMimeTypeException ex) {
            throw new IllegalArgumentException("Invalid media type \"" + mediaType + "\": " + ex.getMessage(), ex);
        }
        String requestedVersion = type.getParameter("version");
        Set<Version> allVersions = getAvailableVersionsForMimeType();

        // if no specific version was requested...
        if (requestedVersion == null || requestedVersion.isEmpty()) {

            // then look for the latest version and check it against "supported" value
            Version latestVersion = allVersions.stream().max(Version::compareTo).get();
            Version firstFound = versions.iterator().next();
            if (latestVersion.satisfies(firstFound.toString())) {
                return this;
            }
        }

        if (type.getParameter("version") != null &&
            !type.getParameter("version").isEmpty()) {

            String logMessage="Serving: "+ versions.toString()+ "; Requested: "+ type.getParameter("version");

            if (acceptedMediaType.startsWith(type.getType())) {
                for (Version v : allVersions) {
                    if (v.satisfies(requestedVersion)) {
                        logger.debug("Matching version found! "+ logMessage);
                        return this;
                    } else
                        logger.debug("No match! "+ logMessage);
                }
            } else {
                logger.debug("No match found for MediaType: "+ acceptedMediaType+ "! "+ logMessage);
            }
        }

        return null;
    }

    private Set<Version> getAvailableVersionsForMimeType() {
        return availableVersions.stream().map(Version::valueOf).sorted(Version::compareTo)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public int compareTo(VersionedResourceRequestCondition other, HttpServletRequest request) {
        return 0;
    }

    @Override
    protected Collection<?> getContent() {
        return versions;
    }

    @Override
    public String toString() {
        return "VersionedResourceRequestCondition{" +
                "versions=" + versions +
                ", acceptedMediaType='" + acceptedMediaType + '\'' +
                '}';
    }

    @Override
    protected String getToStringInfix() {
        return " && ";
    }
}
