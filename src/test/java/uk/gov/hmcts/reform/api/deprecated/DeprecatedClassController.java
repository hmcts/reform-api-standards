package uk.gov.hmcts.reform.api.deprecated;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@APIDeprecated(name = "Deprecated API set",
        expireDate = "30/09/2018",
        docLink = "https://artifactory.reform.hmcts.net/artifactory/ccdata-local/case-data-store-api/",
        note = "Just a note, upgrade your client code!!!")
public class DeprecatedClassController {

    @RequestMapping("/depclass")
    public String deprecated() {
        return "Greetings from deprecated Class Controller!";
    }

}
