package uk.gov.hmcts.reform.api.deprecated;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.apache.http.HttpHeaders.WARNING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.reform.api.deprecated.DeprecatedApiInterceptor.DEPRECATED_AND_REMOVED;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ComponentScan("uk.gov.hmcts.reform.api.deprecated")
@WebAppConfiguration
public class DeprecatedApiInterceptorTest {
    @Inject
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenDeprecatedEndpointRequested_thenReturnsWarning() throws Exception {
        ResultActions result = mockMvc.perform(get("/deprecated")).andExpect(status().isOk());
        result.andReturn().getResponse().getHeader(WARNING).contains("deprecated");
    }

    @Test
    public void whenNotDeprecatedEndpointRequested_thenDoesnotReturnWarning() throws Exception {
        mockMvc.perform(get("/notDeprecated"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist(WARNING));
    }

    @Test
    public void whenDeprecatedClassRequested_thenReturnsWarning() throws Exception {
        mockMvc.perform(get("/depclass"))
                .andExpect(status().isOk())
                .andExpect(header().string(WARNING, StringContains.containsString(DEPRECATED_AND_REMOVED)));
    }
}
