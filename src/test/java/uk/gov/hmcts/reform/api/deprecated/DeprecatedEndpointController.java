package uk.gov.hmcts.reform.api.deprecated;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeprecatedEndpointController {

    @RequestMapping("/deprecated")
    @APIDeprecated(name = "Deprecated Endpoint",
            expiryDate = "01/06/2018",
            docLink = "https://example.org/docs/api/deprecated/",
            note = "Just a note, upgrade your client code!!!")
    public String deprecated() {
        return "Greetings from deprecated Class Controller!";
    }


    @RequestMapping("/notDeprecated")
    public String notDeprecated() {
        return "Greetings from deprecated Class Controller!";
    }

}
