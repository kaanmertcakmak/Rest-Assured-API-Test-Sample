package api_test;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.testng.annotations.Test;
import utils.APITestCase;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class TrendyolAPITest extends APITestCase {

    private Map<String, Object> jsonAsMap;

    @Test
    public void getBaseUrlAndCheckStatusCode(){
        when().
                get(API_ROOT +"/books").then().assertThat().statusCode(200);
    }

    @Test
    public void verifyAPIStartsWithEmptyStore(){
        when().
                get(API_ROOT +"/books").
                then().
                assertThat().
                body("size()", equalTo(0));
    }

    @Test
    public void verifyTitleAndAuthorAreRequired() {
        try{
            jsonAsMap = new HashMap<>();
            jsonAsMap.put("author", "Kaan Mert Cakmak");
            given().
                    contentType(ContentType.JSON).
                    body(jsonAsMap).
                    when().
                    put(API_ROOT +"/books");
        }catch (Exception e){
            Assert.assertEquals(e.getMessage(), "Field 'title' is required");
        }
    }

    @Test
    public void verifyTitleAndAuthorCannotBeEmpty() {
        try{
            jsonAsMap = new HashMap<>();
            jsonAsMap.put("author", ""); // We send author data as empty
            jsonAsMap.put("title", "Trendyol Test");
            given().
                    contentType(ContentType.JSON).
                    body(jsonAsMap).
                    when().
                    put(API_ROOT +"/books");
        }catch (Exception e){
            Assert.assertEquals(e.getMessage(), "Field 'author' cannot be empty");
        }
    }

    @Test
    public void verifyIdFieldIsReadOnly() {
        try{
            jsonAsMap = new HashMap<>();
            jsonAsMap.put("id", 1);
            jsonAsMap.put("author", "Kaan Mert");
            jsonAsMap.put("title", "Trendyol Test");
            given().
                    contentType(ContentType.JSON).
                    body(jsonAsMap).
                    when().
                    put(API_ROOT +"/books");
        }catch (Exception e){
            Assert.assertEquals(e.getMessage(), "Bad Request");
        }
    }

    @Test
    public void verifyCreateNewBookViaPUT() {
        jsonAsMap = new HashMap<>();
        jsonAsMap.put("author", "Kaan Mert Cakmak");
        jsonAsMap.put("title", "Trendyol Test");
        String id = given().
                    contentType(ContentType.JSON).
                    body(jsonAsMap).
                    when().
                    put(API_ROOT +"/books/").
                    then().
                    assertThat().
                    body("author", equalTo("Kaan Mert Cakmak")).
                    and().
                    body("title", equalTo("Trendyol Test")).
                    extract().body().jsonPath().get("id"); // we gathered id param in order to use it in next tests

        // After created we make sure if the resource can received by GET requests
        when().
                get(API_ROOT +"/books/" + id).
                then()
                .assertThat().
                body("id", equalTo(id)).
                and()
                .body("author", equalTo("Kaan Mert Cakmak")).
                and()
                .body("title", equalTo("Trendyol Test"));
    }

    @Test
    public void verifyDublicateDataCanNotBeCreated() {
        jsonAsMap = new HashMap<>();
        jsonAsMap.put("author", "Kaan Mert Cakmak");
        jsonAsMap.put("title", "Trendyol Test");
        try{
            given().
                    contentType(ContentType.JSON).
                    body(jsonAsMap).
                    when().
                    put(API_ROOT +"/books");
        }catch(Exception e){
            Assert.assertEquals(e.getMessage(), ": Another book with similar title and author already exists.");
        }

    }
}
