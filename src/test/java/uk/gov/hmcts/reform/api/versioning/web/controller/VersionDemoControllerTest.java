package uk.gov.hmcts.reform.api.versioning.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.reform.api.versioning.app.AppConfiguration;
import uk.gov.hmcts.reform.api.versioning.web.WebConfiguration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfiguration.class, WebConfiguration.class})
public class VersionDemoControllerTest {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private MockHttpSession session;
    @Autowired
    private MockHttpServletRequest request;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldReturnLatestIfVersionNotPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/version")
                .accept("application/vnd.uk.gov.hmcts.test+json"))
                .andExpect(status().isOk())
                .andExpect(header().string("content-type", "application/vnd.uk.gov.hmcts.test+json;version=2.1.0"))
                .andExpect(jsonPath("version", equalTo("1.0.1")))
                .andExpect(jsonPath("generatedByApiVersion", equalTo("2.1.0")));
    }

    @Test
    public void shouldReturnLatestMajorVersionForCallWithTildeMajorVersion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/version")
                .accept("application/vnd.uk.gov.hmcts.test+json;version=~1"))
                .andExpect(status().isOk())
                .andExpect(header().string("content-type", "application/vnd.uk.gov.hmcts.test+json;version=1.10.0"))
                .andExpect(jsonPath("version", equalTo("1.0.0")))
                .andExpect(jsonPath("generatedByApiVersion", equalTo("1.10.0")));
    }

    @Test
    public void shouldReturnLatestPatchVersionForCallWithTildeMajorMinorVersion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/version")
                .accept("application/vnd.uk.gov.hmcts.test+json;version=~1.9"))
                .andExpect(status().isOk())
                .andExpect(header().string("content-type", "application/vnd.uk.gov.hmcts.test+json;version=1.9.5"))
                .andExpect(jsonPath("version", equalTo("1.0.0")))
                .andExpect(jsonPath("generatedByApiVersion", equalTo("1.9.5")));
    }

    @Test
    public void shouldReturnExactVersionForCallWithExactVersion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/version")
                .accept("application/vnd.uk.gov.hmcts.test+json;version=1.9.2"))
                .andExpect(status().isOk())
                .andExpect(header().string("content-type", "application/vnd.uk.gov.hmcts.test+json;version=1.9.2"))
                .andExpect(jsonPath("version", equalTo("1.0.0")))
                .andExpect(jsonPath("generatedByApiVersion", equalTo("1.9.2")));
    }

    @Test
    public void shouldReturnNotFoundForInvalidVersion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/version")
                .accept("application/vnd.uk.gov.hmcts.test+json;version=0.0.2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnLatestVersionIfResourceHeaderIsMissing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/version")
                .accept(MediaType.ALL))
                .andExpect(header().string("content-type", "application/vnd.uk.gov.hmcts.test+json;version=2.1.0"))
                .andExpect(jsonPath("version", equalTo("1.0.1")))
                .andExpect(jsonPath("generatedByApiVersion", equalTo("2.1.0")));
    }
}