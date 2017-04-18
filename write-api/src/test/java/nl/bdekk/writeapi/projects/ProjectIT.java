package nl.bdekk.writeapi.projects;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import nl.bdekk.writeapi.TestConstants;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class ProjectIT {

    @Test
    public void getEmptyProjects() {
        Response response = given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(TestConstants.URL + "/project").andReturn();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        Assert.assertEquals(response.getBody(), new Object[]{});
    }

}
