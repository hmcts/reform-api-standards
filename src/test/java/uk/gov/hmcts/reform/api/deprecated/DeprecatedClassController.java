package uk.gov.hmcts.reform.api.deprecated;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@APIDeprecated(name = "Deprecated API set",
        expiryDate = "30/09/2018",
        docLink = "https://example.org/docs/api/deprecated/",
        note = "Just a note, upgrade your client code!!!")
public class DeprecatedClassController {

    @RequestMapping("/depclass")
    public String deprecated() {
        return "Greetings from deprecated Class Controller!";
    }

}
