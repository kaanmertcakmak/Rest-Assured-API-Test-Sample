package api_test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.example.Application;
import com.example.book.BookRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
public class RestAssuredAPITest extends AbstractTestNGSpringContextTests {

    public static final String AUTHOR = "Kaan Mert Cakmak";
    public static final String TITLE = "Integration Test";
    private Map<String, Object> jsonAsMap;

    @Autowired
    BookRepository bookRepository;

    @LocalServerPort
    private int ports;

    @BeforeClass
    public void init() {
        RestAssured.port = ports;
    }

    @AfterMethod
    public void cleanup() {
        bookRepository.deleteAll();
    }

    @Test
    public void getBaseUrlAndCheckStatusCode(){
        when().
                get( "/books").then().log().all().assertThat().statusCode(200);
    }

    @Test
    public void verifyAPIStartsWithEmptyStore(){
        when().
                get("/books").
                then().
                assertThat().
                body("size()", equalTo(0));
    }

    @Test
    public void verifyTitleIsRequired() {
        jsonAsMap = Collections.singletonMap("author", AUTHOR);
        Response response = given().
                contentType(ContentType.JSON).
                body(jsonAsMap).
                when().log().all().
                put("/books")
                .then().log().all().assertThat().statusCode(400)
                .extract().response();

        Assert.assertEquals(400, response.getBody().jsonPath().getInt("statusCode"));
        Assert.assertEquals("Field 'title' is mandatory", response.getBody().jsonPath().getString("validationErrors.message"));
    }

    @Test
    public void verifyAuthorIsRequired() {
        jsonAsMap = Collections.singletonMap("title", TITLE);
        Response response = given().
                contentType(ContentType.JSON).
                body(jsonAsMap).
                when().log().all().
                put("/books")
                .then().log().all().assertThat().statusCode(400)
                .extract().response();

        Assert.assertEquals(400, response.getBody().jsonPath().getInt("statusCode"));
        Assert.assertEquals("Field 'author' is mandatory", response.getBody().jsonPath().getString("validationErrors.message"));
    }

    @Test
    public void verifyAuthorCannotBeEmpty() {
        jsonAsMap = new HashMap<>();
        jsonAsMap.put("author", ""); // We send author data as empty
        jsonAsMap.put("title", TITLE);
        Response response = given().
                contentType(ContentType.JSON).
                body(jsonAsMap).
                when().log().all().
                put("/books")
                .then().log().all().assertThat().statusCode(400)
                .extract().response();

        Assert.assertEquals(400, response.getBody().jsonPath().getInt("statusCode"));
        Assert.assertEquals("Field 'author' is mandatory", response.getBody().jsonPath().getString("validationErrors.message"));
    }

    @Test
    public void verifyTitleCannotBeEmpty() {
        jsonAsMap = new HashMap<>();
        jsonAsMap.put("author", AUTHOR); // We send author data as empty
        jsonAsMap.put("title", "");
        Response response = given().
                contentType(ContentType.JSON).
                body(jsonAsMap).
                when().log().all().
                put("/books")
                .then().log().all().assertThat().statusCode(400)
                .extract().response();

        Assert.assertEquals(400, response.getBody().jsonPath().getInt("statusCode"));
        Assert.assertEquals("Field 'title' is mandatory", response.getBody().jsonPath().getString("validationErrors.message"));
    }

    @Test
    public void verifyIdFieldIsReadOnly() {
        jsonAsMap = new HashMap<>();
        jsonAsMap.put("id", 1);
        jsonAsMap.put("author", AUTHOR);
        jsonAsMap.put("title", TITLE);
        given().
                contentType(ContentType.JSON).
                body(jsonAsMap).
                when().log().all().
                put("/books")
                .then().log().all()
                .assertThat().statusCode(400)
                .and()
                .assertThat().body("validationErrors.message", equalTo("id parameter is read only"));
    }

    @Test
    public void verifyCreateNewBookViaPUT() {
        jsonAsMap = new HashMap<>();
        jsonAsMap.put("author", AUTHOR);
        jsonAsMap.put("title", TITLE);
        Integer id = given().
                    contentType(ContentType.JSON).
                    body(jsonAsMap).
                    when().log().all().
                    put("/books").
                    then().log().all().
                    assertThat().
                    body("author", equalTo(AUTHOR)).
                    and().
                    body("title", equalTo(TITLE)).
                    extract().body().jsonPath().get("id"); // we gathered id param in order to use it in next tests

        // After created we make sure if the resource can received by GET requests
        when().
                get("/books/" + id).
                then().log().all()
                .assertThat().
                body("id", equalTo(id)).
                and()
                .body("author", equalTo(AUTHOR)).
                and()
                .body("title", equalTo(TITLE));
    }

    @Test
    public void verifyDuplicateDataCanNotBeCreated() {
        jsonAsMap = new HashMap<>();
        jsonAsMap.put("author", AUTHOR);
        jsonAsMap.put("title", TITLE);
        given().
                contentType(ContentType.JSON).
                body(jsonAsMap).
                when().log().all().
                put("/books").
                then().log().all().
                assertThat().
                statusCode(200); // we gathered id param in order to use it in next tests

        given().
                contentType(ContentType.JSON).
                body(jsonAsMap).
                when().
                put("/books").then().log().all()
                .assertThat().statusCode(400)
                .and().assertThat().body("validationErrors.message", equalTo("There is already a book with same title and author"));
    }
}
