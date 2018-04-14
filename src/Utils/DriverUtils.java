package Utils;


import com.gargoylesoftware.htmlunit.WebClient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DriverUtils {
    private static WebClient client;

    public static WebDriver getPhantomDriver() {
        DesiredCapabilities caps = DesiredCapabilities.phantomjs();
        ArrayList<String> cliArgsCap = new ArrayList<>();
        cliArgsCap.add("--web-security=false");
        cliArgsCap.add("--ssl-protocol=any");
        cliArgsCap.add("--ignore-ssl-errors=true");
        caps.setCapability(
                PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        caps.setCapability(
                PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,
                new String[]{"--logLevel=2"});


        caps.setJavascriptEnabled(true);
        caps.setCapability("takesScreenshot", true);
        caps.setCapability(
                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                DriverUtils.class.getClassLoader().getResource("/driver/phantomjs.exe").getPath());
        WebDriver driver = new PhantomJSDriver(caps);
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        return driver;
    }

    public static WebClient getHtmlUnitClient() {
        if (client == null) {
            client = new WebClient();
        }
        return client;
    }
}
