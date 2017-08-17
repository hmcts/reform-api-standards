package uk.gov.hmcts.reform.api.versioning.web.controller;


import uk.gov.hmcts.reform.api.versioning.spring.version.VersionedResource;
import uk.gov.hmcts.reform.api.versioning.web.model.ReturnedObject;
import uk.gov.hmcts.reform.api.versioning.web.model.ReturnedObject_v1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
//@VersionedResource(media = "application/vnd.uk.gov.hmcts.test")
public class VersionDemoController {
    private Logger logger = LoggerFactory.getLogger(VersionDemoController.class);

    @RequestMapping(value = {"/version"}, method = RequestMethod.GET,
                    produces = {"application/vnd.uk.gov.hmcts.test+json;version=1.5.0"})
    @VersionedResource(media = "application/vnd.uk.gov.hmcts.test", supported="1.5.0")
    @ResponseBody
    public ReturnedObject functionVersion_v150() {
        logger.info("called /version with v1.5.0");
        return new ReturnedObject("some version of a service", "1.5.0");
    }

    @RequestMapping(value = {"/version"}, method = RequestMethod.GET,
                    produces = {"application/vnd.uk.gov.hmcts.test+json;version=1.9.0"})
    @VersionedResource(media = "application/vnd.uk.gov.hmcts.test", supported="1.9.0")
    @ResponseBody
    public ReturnedObject functionVersion_v190() {
        logger.info("called /version with v1.9.0");
        return new ReturnedObject("result has more fields => functinality changed => Patch version MUST be reset to 0 when minor version is incremented","1.9.0");
    }

    @RequestMapping(value = {"/version"}, method = RequestMethod.GET,
//                    headers = "accept=application/vnd.uk.gov.hmcts.test;version=1.9.2"),
                    produces = {"application/vnd.uk.gov.hmcts.test+json;version=1.9.2"})
    @VersionedResource(media = "application/vnd.uk.gov.hmcts.test", supported="1.9.2")
    @ResponseBody
    public ReturnedObject functionVersion_v192() {
        logger.info("called /version with v1.9.2");
        return new ReturnedObject("bug fix prev version: 1.9.0","1.9.2");
    }

    @RequestMapping(value = {"/version"}, method = RequestMethod.GET,
                    produces = {"application/vnd.uk.gov.hmcts.test+json;version=1.9.5"})
    @VersionedResource(media = "application/vnd.uk.gov.hmcts.test", supported="1.9.5")
    @ResponseBody
    public ReturnedObject functionVersion_v195() {
        logger.info("called /version with v1.9.5");
        return new ReturnedObject("bug fix prev version: 1.9.2","1.9.5");
    }

    @RequestMapping(value = {"/version"}, method = RequestMethod.GET,
                    produces = {"application/vnd.uk.gov.hmcts.test+json;version=1.10.0"})
    @VersionedResource(media = "application/vnd.uk.gov.hmcts.test", supported="1.10.0")
    @ResponseBody
    public ReturnedObject functionVersion_v1100() {
        logger.info("called /version with v1.10.0");
        return new ReturnedObject("internal algorithm completely changed BUT same response format => minor version increase","1.10.0");
    }

    @RequestMapping(value = {"/version"}, method = RequestMethod.GET,
                    produces = {"application/vnd.uk.gov.hmcts.test+json;version=2.0.0"})
    @VersionedResource(media = "application/vnd.uk.gov.hmcts.test", supported="2.0.0")
    @ResponseBody
    public ReturnedObject_v1 functionVersion_v200() {
        logger.info("called /version with v2.0.0");
        return new ReturnedObject_v1("contract changed => Patch and minor version MUST be reset to 0 when major version is incremented","2.0.0");
    }

    @RequestMapping(value = {"/version"}, method = RequestMethod.GET,
                    produces = {"application/vnd.uk.gov.hmcts.test+json;version=2.1.0"})
    @VersionedResource(media = "application/vnd.uk.gov.hmcts.test", supported="2.1.0")
    @ResponseBody
    public ReturnedObject_v1 functionVersion_v210() {
        logger.info("called /version with v2.1.0");
        return new ReturnedObject_v1("latest version","2.1.0");
    }

}
