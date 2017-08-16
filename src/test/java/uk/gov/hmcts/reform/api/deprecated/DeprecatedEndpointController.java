package uk.gov.hmcts.reform.api.deprecated;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeprecatedEndpointController {

    @RequestMapping("/deprecated")
    @APIDeprecated(name = "Deprecated Endpoint",
            expireDate = "01/06/2018",
            docLink = "https://artifactory.reform.hmcts.net/artifactory/ccdata-local/case-data-store-api/",
            note = "Just a note, upgrade your client code!!!")
    public String deprecated() {
        return "Greetings from deprecated Class Controller!";
    }


    @RequestMapping("/notDeprecated")
    public String notDeprecated() {
        return "Greetings from deprecated Class Controller!";
    }

}
