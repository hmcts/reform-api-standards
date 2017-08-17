package hello;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * our own implementation of HeaderContentNegotiationStrategy
 */
public class VersionAwareHeaderContentNegotiationStrategy implements ContentNegotiationStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(VersionAwareHeaderContentNegotiationStrategy.class);
    
    private CopyOnWriteArraySet<String> supportedVersionsList;

    public VersionAwareHeaderContentNegotiationStrategy(Collection<String> list) {
        this.supportedVersionsList = new CopyOnWriteArraySet<String>(list);
//        this.supportedVersionsList = new CopyOnWriteArraySet<Version>(list
//                                                                    .stream()
//                                                                    .map(x -> Version.valueOf(x))
//                                                                    .collect(Collectors.toSet()));
    }

    @Override
    public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException {

        String[] headerValueArray = webRequest.getHeaderValues(HttpHeaders.ACCEPT);
        if (headerValueArray == null) {
            return Collections.<MediaType>emptyList();
        }

        List<String> headerValues = Arrays.asList(headerValueArray);
        try {
            List<MediaType> mediaTypes = VersionAwareMediaType.parseMediaTypes(headerValues);
            //NOTE: not a good strategy here as VersionAwareMediaType is losing our implementation 
            //      of isComparableWith when casting to MediaType so only viable choice is to
            //      modify Spring's MimeType to handle versions as well
            List<MediaType> mediaTypes_compiled = new ArrayList<MediaType>();
//            MediaType.sortBySpecificityAndQuality(mediaTypes);
            /*
            //Print call stack to check how it got here for debug
            System.out.print("Call stack: "); 
            Arrays.asList(Thread.currentThread().getStackTrace()).forEach(s -> System.out.println("---> "+ s.toString()));*/
            logger.info("List of supported versions: "+ supportedVersionsList);
                
            for (Iterator<MediaType> iterator = mediaTypes.iterator(); iterator.hasNext();) {
                MediaType mt = iterator.next();
                logger.debug("Version: "+ mt.getParameter("version")+ 
                        ((supportedVersionsList.contains(mt.getParameter("version")))? "" : " not")+ " supported");
                
                if (!supportedVersionsList.contains(mt.getParameter("version"))) {
                    logger.debug("Removing "+ mt.getParameter("version")+ " from supported MediTypes list");
                    // NOTE: just realised we're actually modifying requestor's accepted list of types
                    //       we should actually remove from the other list (produces one) 
                    //iterator.remove();
                    mediaTypes_compiled.add(new MediaType(mt.getType(), "notsupported"));
                } else {
                    mediaTypes_compiled.add(mt);
                }
            }
            return mediaTypes_compiled;
        }
        catch (InvalidMediaTypeException ex) {
            throw new HttpMediaTypeNotAcceptableException(
                    "Could not parse 'Accept' header " + headerValues + ": " + ex.getMessage());
        }

    }

//    private String matchingVersion(String version) {
//        for (Iterator<Version> it = supportedVersionsList.iterator(); it.hasNext(); )
//            if (!it.next().satisfies(version))
//                it.remove();
//
//        logger.info("Satisfying list: "+ supportedVersionsList);
//
//        return supportedVersionsList.toString();
//    }

    
}

