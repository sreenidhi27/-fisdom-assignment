package CommonUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JSONReader {

    public static JsonObject readJSONFile(String filePath) throws FileNotFoundException {
        return JsonParser.parseReader(new FileReader(filePath)).getAsJsonObject();
    }

}