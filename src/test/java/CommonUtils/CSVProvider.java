package CommonUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class CSVProvider {
    @DataProvider(name = "csv")
    public static Object[][] createData_from_csv(final ITestContext context, final Method method) {

        List<String[]> testData = new ArrayList<>();
        String filePath = null;
        DataFileParameters parameters = method.getAnnotation(DataFileParameters.class);
        if (parameters != null) {
            filePath = parameters.path();
            if (isEmpty(parameters.path())) {
                throw new RuntimeException("PATH cannot be empty");
            }
        }

        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withSkipLines(1).build();

            String[] data;
            while ((data = reader.readNext()) != null) {
                testData.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Object[][] testDataArray = new String[testData.size()][];
        testData.toArray(testDataArray);
        return testDataArray;
    }

}
