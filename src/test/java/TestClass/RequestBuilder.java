package TestClass;

import CommonUtils.JSONReader;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import java.io.FileNotFoundException;

public class RequestBuilder {
    public static JsonObject getAuthRequest(String userName, String password,String filePath) throws FileNotFoundException{
        JsonObject jsonObject = JSONReader.readJSONFile(filePath);
        jsonObject.addProperty("username",userName);
        jsonObject.addProperty("password",password);
        return jsonObject;
    }

    public static JsonObject getCreateBookingRequest(String firstname, String lastname, String totalprice, String depositpaid, String checkin, String checkout, String additionalneeds, String filePath) throws FileNotFoundException {
        JsonObject jsonFileObject = JSONReader.readJSONFile(filePath);
        jsonFileObject.addProperty("firstname", firstname);
        jsonFileObject.addProperty("lastname", lastname);
        if (StringUtils.isNumeric(totalprice)) {
            jsonFileObject.addProperty("totalprice", Integer.parseInt(totalprice));
        } else {
            jsonFileObject.addProperty("totalprice", totalprice);
        }
        jsonFileObject.addProperty("depositpaid", depositpaid);
        jsonFileObject.getAsJsonObject("bookingdates").addProperty("checkin", checkin);
        jsonFileObject.getAsJsonObject("bookingdates").addProperty("checkout", checkout);
        jsonFileObject.addProperty("additionalneeds", additionalneeds);

        return jsonFileObject;
    }


    public static JsonObject getUpdateBookingRequest(String firstname, String lastname, String totalprice, String depositpaid, String checkin, String checkout, String additionalneeds, String filePath) throws FileNotFoundException {
        JsonObject jsonFileObject = JSONReader.readJSONFile(filePath);
        jsonFileObject.addProperty("firstname", firstname);
        jsonFileObject.addProperty("lastname", lastname);
        if (StringUtils.isNumeric(totalprice)) {
            jsonFileObject.addProperty("totalprice", Integer.parseInt(totalprice));
        } else {
            jsonFileObject.addProperty("totalprice", totalprice);
        }
        jsonFileObject.addProperty("depositpaid", depositpaid);
        jsonFileObject.getAsJsonObject("bookingdates").addProperty("checkin", checkin);
        jsonFileObject.getAsJsonObject("bookingdates").addProperty("checkout", checkout);
        jsonFileObject.addProperty("additionalneeds", additionalneeds);

        return jsonFileObject;
    }


    public static JsonObject getPartialBookingRequest(String firstname, String lastname, String filePath) throws FileNotFoundException {
        JsonObject jsonFileObject = JSONReader.readJSONFile(filePath);
        jsonFileObject.addProperty("firstname", firstname);
        jsonFileObject.addProperty("lastname", lastname);

        jsonFileObject.remove("totalprice");
        jsonFileObject.remove("depositpaid");
        jsonFileObject.remove("bookingdates");
        jsonFileObject.remove("additionalneeds");

        return jsonFileObject;
    }
}