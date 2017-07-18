package hello.test;

import static io.restassured.RestAssured.given;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.restassured.http.ContentType;

@RunWith(SpringJUnit4ClassRunner.class)
public class HelloControllerTest {

	private static final String URL_PREFIX = "http://localhost:8080";

	@Test
	public void givenServiceEndpoint_whenGetRequestFirstAPIVersion_then200() {
	    given()
	      .accept("application/vnd.uk.gov.hmcts.test.v1+json")
	    .when()
	      .get(URL_PREFIX + "/versions")
	    .then()
	      .contentType(ContentType.JSON).and().statusCode(200);
	}

/*	@Test
	public void givenServiceEndpoint_whenGetRequestSecondAPIVersion_then200() {
	    given()
	      .accept("application/vnd.uk.gov.hmcts.test+json;versoin=^1.0.1")
	    .when()
	      .get(URL_PREFIX + "/versions")
	    .then()
	      .contentType(ContentType.JSON).and().statusCode(200);
	}
*/	
}
