package ${package}.api.controller;

import ${package}.Application;
import ${package}.api.constant.ApiConstants;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class PingControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Test
    public void check_api_is_running_via_ping() {
        given().auth().preemptive().basic(username, password)
                .when().get(ApiConstants.API_PING).then()
                .statusCode(200);
    }

    @Before
    public void setBaseUri () {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }
}
