package CommonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class BaseClass {
    static Properties properties = new Properties();
    final static String properties_filename = "application.properties";

    static {
        File src = new File(properties_filename);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(src);
            properties.load(fileInputStream);
        } catch (FileNotFoundException e) {
            System.out.println("properites");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String property) {
        return properties.getProperty(property);
    }

}

