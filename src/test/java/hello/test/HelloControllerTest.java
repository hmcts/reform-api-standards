package hello.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class HelloControllerTest {

	private static final String URL_PREFIX = "http://localhost:8080";

	/*
     * v1.0.1 is in the list of custom ContentNegotiationStrategy class
     */
	@Test
	public void whenGetRequestv101_thenReturns101() {
	    given()
	      .accept("application/vnd.uk.gov.hmcts.test+json;version=1.0.1")
	    .when()
	      .get(URL_PREFIX + "/versions")
	    .then()
	      .and().statusCode(200)
	      .and().assertThat().body(containsString("1.0.1"));
	}

    /**
     * v1.3.0 is in the list of custom ContentNegotiationStrategy class
     */
    @Test
    public void whenGetRequest_v130_thenReturns130() {
        given()
            .accept("application/vnd.uk.gov.hmcts.test+json;version=1.3.0")
        .when()
            .get(URL_PREFIX + "/versions")
        .then()
            .and().statusCode(200)
            .and().assertThat().body(containsString("1.3.0"));
    }

    /**
     * v1.3.4 is NOT in the list of custom ContentNegotiationStrategy class
     */
    @Test
    public void whenGetRequest_v134_thenReturns134() {
        given()
            .accept("application/vnd.uk.gov.hmcts.test+json;version=1.3.4")
        .when()
            .get(URL_PREFIX + "/versions")
        .then()
            .and().statusCode(200)
            .and().assertThat().body(containsString("1.3.4"));
    }

    @Test
    public void whenGetRequest_v100_thenReturnsHTTP406() {
        given()
            .accept("application/vnd.uk.gov.hmcts.test+json;version=1.0.0")
        .when()
            .get(URL_PREFIX + "/versions")
        .then()
            .and().statusCode(406);
    }
	
}
