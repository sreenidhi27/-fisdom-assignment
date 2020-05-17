package TestClass;

import CommonUtils.BaseClass;
import CommonUtils.CSVProvider;
import CommonUtils.DataFileParameters;
import Utils.ExtentReporterNG;
import com.aventstack.extentreports.ExtentTest;
import com.google.gson.JsonObject;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;



public class Tests {

    String baseUrl = BaseClass.getProperty("BaseUrl");
    static String token = "YWRtaW46cGFzc3dvcmQxMjM=";
    static ArrayList<Integer> bookingIds = new ArrayList<>();


    static ExtentReporterNG extentReporterNG = new ExtentReporterNG();

    static {
        extentReporterNG.generateReport(System.getProperty("user.dir") + "/test-output/ExtentReportsTestNG.html", "Api Test");
    }

    @Test(description = "AuthTest", dataProvider = "csv", dataProviderClass = CSVProvider.class, suiteName = "Default Suite")
    @DataFileParameters(path = "resources/input-data/auth.csv")
    public void testAuth(String username, String password) throws FileNotFoundException {

        JsonObject jsonObject = RequestBuilder.getAuthRequest(username, password, "resources/request-json/auth.json");

        ExtentTest extentTest = extentReporterNG.getExtentReports().createTest("Auth Test");
        extentTest.createNode("Auth Test");

        String res = given().log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .when().post(baseUrl + "/auth")
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath jsonPath = new JsonPath(res);
        String tokenAuth = jsonPath.getString("token");
        System.out.println(tokenAuth);
        if (username.equals("admin") && password.equals("password123")) {
            assertNotNull(tokenAuth, "Valid");
        } else {
            assertNull(tokenAuth, "NotValid");
        }
        extentReporterNG.flush();
    }

    @Test(description = "CreateBooking", dataProvider = "csv", dataProviderClass = CSVProvider.class, suiteName = "Default Suite")
    @DataFileParameters(path = "resources/input-data/createbooking.csv")
    public void testCreateBooking(String firstname, String lastname, String totalprice, String depositpaid, String checkin, String checkout, String additionalneeds) throws FileNotFoundException {
        JsonObject requestPayload = RequestBuilder.getCreateBookingRequest(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds, "resources/request-json/createBooking.json");

        ExtentTest extentTest = extentReporterNG.getExtentReports().createTest("Create Booking Test");
        extentTest.createNode("Create Booking Test");

        String res = given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestPayload.toString())
                .when().post(baseUrl + "/booking")
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath jsonPath = new JsonPath(res);

        assertEquals(firstname, jsonPath.get("booking.firstname"), "Not Found");
        assertEquals(lastname, jsonPath.getString("booking.lastname"), "Not Found");

        if (StringUtils.isNumeric(totalprice)) {
            assertEquals(Integer.parseInt(totalprice), jsonPath.getInt("booking.totalprice"), "Not Equal");
        }
        if (jsonPath.get("bookingdates") != null) {
            assertEquals(checkin, jsonPath.getString("bookingdates.checkin"));
            assertEquals(checkout, jsonPath.getString("bookingdates.checkout"));
        }
        assertEquals(jsonPath.getString("booking.additionalneeds"), additionalneeds);
        extentReporterNG.flush();
    }

    @Test(description = "GET BOOKING", suiteName = "Default Suite")
    public void testGetBooking() {

        ExtentTest extentTest = extentReporterNG.getExtentReports().createTest("Get Booking Test");
        extentTest.createNode("Get Booking Test");

        String res = given().log().all()
                .header("Content-Type", "application/json")
                .when().get(baseUrl + "/booking")
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath jsonPath = new JsonPath(res);
        System.out.println(jsonPath);
        for (Object i : jsonPath.getList("")) {
            bookingIds.add((int) ((Map<Object, Object>) i).get("bookingid"));
        }
        System.out.println(bookingIds);
        extentReporterNG.flush();

    }

    @Test(description = "GetBookingID", suiteName = "Default Suite")
    public void testGetBookingId() {
        int random = new Random().nextInt(bookingIds.size());
        int bookingId = bookingIds.get(random);

        ExtentTest extentTest = extentReporterNG.getExtentReports().createTest("Get Booking ID Test");
        extentTest.createNode("Get Booking ID Test");

        String res = given().log().all()
                .header("Content-Type", "application/json").pathParam("id", bookingId)
                .when().get(baseUrl + "/booking/{id}")
                .then().assertThat().statusCode(200)
                .extract().response().asString();
        JsonPath jsonPath = new JsonPath(res);
        System.out.println(jsonPath.prettify());
        assertNotNull(jsonPath.getString("firstname"));
        assertNotNull(jsonPath.getString("lastname"));
        assertNotNull(jsonPath.getString("totalprice"));
        assertNotNull(jsonPath.getString("additionalneeds"));
        if (jsonPath.get("bookingdates") != null) {
            assertNotNull(jsonPath.getString("bookingdates.checkin"));
            assertNotNull(jsonPath.getString("bookingdates.checkout"));
        }
        extentReporterNG.flush();
    }


    @Test(description = "UpdateBooking", dataProvider = "csv", dataProviderClass = CSVProvider.class, suiteName  = "Default Suite")
    @DataFileParameters(path = "resources/input-data/update.csv")
    public void testUpdateBooking(String firstname, String lastname, String totalprice, String depositpaid, String checkin, String checkout, String additionalneeds) throws FileNotFoundException {
        JsonObject requestPayload = RequestBuilder.getUpdateBookingRequest(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds, "resources/request-json/updateBooking.json");
        int random = new Random().nextInt(bookingIds.size());
        int bookingId = bookingIds.get(random);

        ExtentTest extentTest = extentReporterNG.getExtentReports().createTest("Update Booking ID Test");
        extentTest.createNode("Update Booking ID Test");

        String res = given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Basic " + token)
                .body(requestPayload.toString()).pathParam("id",bookingId)
                .when().put(baseUrl + "/booking/{id}")
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath jsonPath = new JsonPath(res);

        assertEquals(firstname, jsonPath.getString("firstname"));
        assertEquals(lastname, jsonPath.getString("lastname"));
        if (StringUtils.isNumeric(totalprice)) {
            assertEquals(Integer.parseInt(totalprice), jsonPath.getInt("totalprice"));
        }
        assertEquals(jsonPath.getString("additionalneeds"), additionalneeds);
        if(jsonPath.get("bookingdates")!=null){
            assertEquals(checkin, jsonPath.getString("bookingdates.checkin"));
            assertEquals(checkout, jsonPath.getString("bookingdates.checkout"));
        }
        extentReporterNG.flush();
    }

    @Test(description = "PartialUpdateBooking", dataProvider = "csv", dataProviderClass = CSVProvider.class, suiteName  = "Default Suite")
    @DataFileParameters(path = "resources/input-data/partialupdate.csv")
    public void testPartialUpdateBooking(String firstname, String lastname) throws FileNotFoundException {
        JsonObject requestPayload = RequestBuilder.getPartialBookingRequest(firstname, lastname, "resources/request-json/partialUpdateBooking.json");
        int random = new Random().nextInt(bookingIds.size());
        int bookingId = bookingIds.get(random);

        ExtentTest extentTest = extentReporterNG.getExtentReports().createTest("Partial update Booking ID Test");
        extentTest.createNode("Partial update Booking ID Test");

        String res = given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Basic " + token)
                .body(requestPayload.toString()).pathParam("id",bookingId)
                .when().patch(baseUrl + "/booking/{id}")
                .then()
                .assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath jsonPath = new JsonPath(res);
        System.out.println(jsonPath.prettify());
        assertEquals(firstname, jsonPath.getString("firstname"));
        assertEquals(lastname, jsonPath.getString("lastname"));
        assertNotNull(jsonPath.getString("totalprice"));
        assertNotNull(jsonPath.getString("additionalneeds"));
        if(jsonPath.get("bookingdates")!=null){
            assertNotNull(jsonPath.getString("bookingdates.checkin"));
            assertNotNull(jsonPath.getString("bookingdates.checkout"));
        }
        extentReporterNG.flush();
    }

    @Test(description = "DeleteBooking",priority = 1, suiteName  = "Default Suite")
    public void testDeleteBooking() {
        int random = new Random().nextInt(bookingIds.size());
        int bookingId = bookingIds.get(random);
        ExtentTest extentTest = extentReporterNG.getExtentReports().createTest("Delete Booking ID Test");
        extentTest.createNode("Delete Booking ID Test");

        given().log().all().header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=").pathParam("id",bookingId)
                .when().delete(baseUrl + "/booking/{id}")
                .then().assertThat().statusCode(201).extract().toString();
        given().log().all().header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .when().delete(baseUrl + "/booking/" + bookingId)
                .then().assertThat().statusCode(405).extract().toString();
        extentReporterNG.flush();
    }


}
