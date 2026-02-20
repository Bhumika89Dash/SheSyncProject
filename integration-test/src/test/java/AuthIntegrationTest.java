import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:4004";

    }

    // actual test
    @Test
    public void shouldReturnOKWithValidToken() {
        // there are 3 tests inside this method
        // 1. ARRANGE : any setup that this test needs in order to work 100%
        // 2. ACT
        // 3. ASSERT

        // properties we are going in the requests that our test runs
        String loginPayLoad = """
                {
                     "email":"testuser@test.com",
                     "password":"password123"
                 }
                """;


        //2. ACT - code that actually runs the test
        Response response = given()
                .contentType("application/json")
                .body(loginPayLoad)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().response();

        System.out.println("Generated Token: " + response.jsonPath()
                .getString("token"));
    }

    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin() {
        String loginPayload = """
          {
            "email": "invalid_user@test.com",
            "password": "wrongpassword"
          }
        """;

        given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }



}