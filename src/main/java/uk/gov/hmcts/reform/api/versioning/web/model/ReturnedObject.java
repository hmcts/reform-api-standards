package uk.gov.hmcts.reform.api.versioning.web.model;


public class ReturnedObject {
    private String description;
    private String generatedByApiVersion;
    private final String version = "1.0.0";

    public ReturnedObject(String description, String version) {
        this.description = description;
        this.generatedByApiVersion = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeneratedByApiVersion() {
        return generatedByApiVersion;
    }

    public void setGeneratedByApiVersion(String generatedByApiVersion) {
        this.generatedByApiVersion = generatedByApiVersion;
    }

    public String getVersion() {
        return version;
    }
}
