package uk.gov.hmcts.reform.api.versioning.web.model;

public class ReturnedObject_v1 extends ReturnedObject {
    private final String version = "1.0.1";

    public ReturnedObject_v1(String description, String version) {
        super(description, version);
    }

}
