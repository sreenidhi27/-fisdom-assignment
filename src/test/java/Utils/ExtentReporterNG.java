package Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


public class ExtentReporterNG  {


    private ExtentHtmlReporter extentHtmlReporter;
    private ExtentReports extentReports;
    public ExtentReports getExtentReports() {
        return extentReports;
    }


    public ExtentReports generateReport(String outputpath, String reportName){
        extentReports= new ExtentReports();
        extentHtmlReporter = new ExtentHtmlReporter(outputpath);
        extentHtmlReporter.loadConfig(System.getProperty("user.dir")+"/src/test/java/Utils/extent.xml");
        extentReports.attachReporter(extentHtmlReporter);
        extentHtmlReporter.config().setReportName(reportName);

        return extentReports;

    }
    public void flush(){
        extentReports.flush();
    }

}
