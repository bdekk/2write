package nl.bdekk.productapi.projects;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import nl.bdekk.productapi.TestUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class FileIT {

    @Test
    public void getFile() {
        Response response = given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(TestUtils.URL + "/project/1/file/1").andReturn();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(200, statusCode);
        Assert.assertNotEquals("", response.getBody().print());
    }

    @Test
    public void updateFile() {
        JsonNode json = TestUtils.readFileToJSON("valid-file.json");

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put(TestUtils.URL + "/project/1/file/23")
                .then()
                .statusCode(200)
                .body("content", equalTo("Content"), "name", equalTo("file1"));
    }

    @Test
    @Ignore
    public void createFile() {
        JsonNode json = TestUtils.readFileToJSON("valid-file.json");

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(TestUtils.URL + "/project/1/file")
                .then()
                .statusCode(201)
                .body("name", equalTo("file1.md"));
    }
}
