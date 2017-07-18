package hello;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.zafarkhaja.semver.Version;

@RestController
public class HelloController {
	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
	Pattern pattern = Pattern.compile("version=\"(.*?)\"");

	// #Accept:
	// application/vnd.uk.gov.hmcts.<micro-service-name>.<domain-object-name>.<...?>+json;
	// version="^1.0.1"
	@RequestMapping(value = "/versions", 
					produces = { "application/json",
								 "application/vnd.uk.gov.hmcts.test+json;q=0.1;version=1.0.1" })
	@ResponseBody
	public String version_v101(@RequestHeader(value = "Accept", required = false) String acceptHeader) {
		logger.info("************ Called with following Accept header: " + acceptHeader);

		Matcher matcher = pattern.matcher(acceptHeader);
		logger.info("************ regexp result: " + matcher.group(1));
		if (matcher.find())
			return matchingVersion(matcher.group(1), new String[] { "1.0.1", "1.3.4", "2.5.0" });

		return new String("v1.0.1 endpoint");
	}

	@RequestMapping(value = "/versions", 
					produces = { "application/json", "application/vnd.uk.gov.hmcts.test+json;q=0.9;version=1.3.4"})
	@ResponseBody
	public String version_v134(@RequestHeader(value = "Accept", required = false) String acceptHeader) {
		logger.info("************ Called with following Accept header: " + acceptHeader);
		Pattern pattern = Pattern.compile("version=\"(.*?)\"");
		Matcher matcher = pattern.matcher(acceptHeader);
		if (matcher.find()) {
			logger.info("************ " + matcher.group(1));
			return matchingVersion(matcher.group(1), new String[] { "1.0.1", "1.3.4", "2.5.0" });
			// new ArrayList<String>(Arrays.asList("1.0.1", "1.3.4", "2.5"))
		} else
			logger.info("************ No version found!");

		return new String("v1.3.4 endpoint");
	}

	@RequestMapping(value = "/versions", 
					produces = { "application/json", 
								 "application/vnd.uk.gov.hmcts.test.v2+json",
								 "application/vnd.uk.gov.hmcts.test.v2.5.0+json;version=2.5.0" })
	@ResponseBody
	public String versions_v250(@RequestHeader(value = "Accept", required = false) String acceptHeader) {
		logger.info("************ Called with following Accept header: " + acceptHeader);
		Pattern pattern = Pattern.compile("version=\"(.*?)\"");
		Matcher matcher = pattern.matcher(acceptHeader);
		if (matcher.find()) {
			logger.info("************ " + matcher.group(1));
			return matchingVersion(matcher.group(1), new String[] { "1.0.1", "1.3.4", "2.5.0" });
			// new ArrayList<String>(Arrays.asList("1.0.1", "1.3.4", "2.5"))
		} else
			logger.info("************ No version found!");

		return new String("v2.5.0 endpoint");
	}

	private String matchingVersion(String version, String[] listOfVersionsToMatchTo) {
		List<Version> toMatch = Arrays.asList(listOfVersionsToMatchTo)
									.stream()
									.map(x -> Version.valueOf(x))
									.collect(Collectors.toList());
		
		for (Iterator<Version> it = toMatch.iterator(); it.hasNext(); )
	        if (!it.next().satisfies(version))
	            it.remove();

		logger.info("Satisfying list: "+ toMatch);

		return toMatch.toString();
	}

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@RequestMapping(value = "/method6", produces = { "application/json", "application/xml",
			"application/vnd.uk.gov.hmcts.test+json" }, consumes = "text/html")
	@ResponseBody
	public String method6() {
		return "method6";
	}

	@RequestMapping(value = "/method8/{id:[\\d]+}/{name}")
	@ResponseBody
	public String method8(@PathVariable("id") long id, @PathVariable("name") String name) {
		return "method8 with id= " + id + " and name=" + name;
	}

}
