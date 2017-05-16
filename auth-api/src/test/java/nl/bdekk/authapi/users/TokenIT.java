package nl.bdekk.authapi.users;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import nl.bdekk.authapi.TestUtils;
import org.junit.Ignore;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class TokenIT {

    @Test
    public void refreshToken() {
//        JsonNode json = TestUtils.readFileToJSON("valid-user.json");

        given()
                .contentType(ContentType.JSON)
//                .body(json)
                .when()
                .put(TestUtils.URL + "/auth/refresh")
                .then()
                .statusCode(200)
                .body("content", equalTo("Content"), "name", equalTo("file1"));
    }

    @Test
    @Ignore
    public void createToken() {
        JsonNode json = TestUtils.readFileToJSON("valid-user.json");

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(TestUtils.URL + "/auth")
                .then()
                .statusCode(201)
                .body("token", equalTo("Bearer "));
    }

}
