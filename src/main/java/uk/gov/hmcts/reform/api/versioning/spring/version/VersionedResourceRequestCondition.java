package uk.gov.hmcts.reform.api.versioning.spring.version;

import com.github.zafarkhaja.semver.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class VersionedResourceRequestCondition extends AbstractRequestCondition<VersionedResourceRequestCondition> {
    private Logger logger = LoggerFactory.getLogger(VersionedResourceRequestCondition.class);
    private final Set<Version> versions;
    private final String acceptedMediaType;

    public VersionedResourceRequestCondition(String acceptedMediaType, String supported) {
        this(acceptedMediaType, new HashSet<Version>(Arrays.asList(Version.valueOf(supported))));
    }

    public VersionedResourceRequestCondition(String acceptedMediaType, Collection<Version> versions) {
        this.acceptedMediaType = acceptedMediaType;
        this.versions = Collections.unmodifiableSet(new HashSet<Version>(versions));
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
        return new VersionedResourceRequestCondition(newMediaType, newVersions);
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

        if (type.getParameter("version") != null &&
            !type.getParameter("version").isEmpty()) {

            String logMessage="Serving: "+ versions.toString()+ "; Requested: "+ type.getParameter("version");

            if (acceptedMediaType.startsWith(type.getType())) {
                for (Version v : versions) {
                    if (v.satisfies(type.getParameter("version"))) {
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
