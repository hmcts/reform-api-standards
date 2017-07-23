package hello;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.github.zafarkhaja.semver.Version;

@RestController
public class HelloController {
	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
	Pattern pattern = Pattern.compile("version=\"(.*?)\"");
	
	@Autowired
	ApplicationContext context;;

	@RequestMapping(value = "/versions", 
					//consumes = { "application/vnd.uk.gov.hmcts.test+json;version=1.0.1" },
					produces = { "application/vnd.uk.gov.hmcts.test+json;version=1.0.1" } )
	@ResponseBody
	public String version_v101(@RequestHeader(value = "Accept", required = false) String acceptHeader,
	                            NativeWebRequest request) throws HttpMediaTypeNotAcceptableException 
	{
	    Map<String, ContentNegotiationStrategy> map = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, ContentNegotiationStrategy.class, true, false);
	    map.forEach((k, v) -> System.out.printf("%s=%s%n", k, v.getClass().getSimpleName()));
		ContentNegotiationManager m = (ContentNegotiationManager) map.get("mvcContentNegotiationManager");
		List<ContentNegotiationStrategy> strategies = m.getStrategies();
	    strategies.forEach(s-> System.out.println(s.getClass().getName()));
	    /*for (ContentNegotiationStrategy cns : strategies) {
            if (cns instanceof VersionAwareHeaderContentNegotiationStrategy) {
                List<MediaType> list = ((VersionAwareHeaderContentNegotiationStrategy) cns).resolveMediaTypes(request);
                System.out.println("-- Accept header content configured with custom Negotiation Strategy --");
                list.forEach(l -> System.out.println(l.getParameters()));
            }
        }*/   
	    
		return new String("endpoint v1.0.1: SemVer lib result: "+ match(acceptHeader, request.getNativeRequest(HttpServletRequest.class)));
	}

    @ResponseBody
    @RequestMapping(value = "/versions", 
                    produces = { "application/vnd.uk.gov.hmcts.test+json;version=1.3.0" } )
	public String version_v131(@RequestHeader(value = "Accept", required = false) String acceptHeader,
								HttpServletRequest request) {
		return new String("endpoint v1.3.0: SemVer lib result: "+ match(acceptHeader, request));
	}

    @ResponseBody
	@RequestMapping(value = "/versions", 
					produces = {"application/vnd.uk.gov.hmcts.test.v1+json;version=1.3.4"})
	public String version_v134(@RequestHeader(value = "Accept", required = false) String acceptHeader,
								HttpServletRequest request) {
		return new String("endpoint v1.3.4: SemVer lib result: "+ match(acceptHeader, request));
	}

    @ResponseBody
	@RequestMapping(value = "/versions", 
					produces = {"application/vnd.uk.gov.hmcts.test+json;version=2.5.0"})
	public String version_v250(@RequestHeader(value = "Accept", required = false) String acceptHeader,
								HttpServletRequest request) {
		return new String("endpoint v2.5.0: SemVer lib result: "+ match(acceptHeader, request));
	}

	private String match(String acceptHeader, HttpServletRequest request) {
		logger.info(request.getRequestURI()+ " called with Accept:" + acceptHeader);
		Matcher matcher = pattern.matcher(acceptHeader);
		if (matcher.find())
			return matchingVersion(matcher.group(1), new String[] { "1.0.1", "1.3.1", "1.3.4", "2.5.0" });
		return "no match";
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

/*	@RequestMapping(value = "/method6", produces = { "application/json", "application/xml",
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
*/
}
 