import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class testone {

    public static void main() throws MalformedURLException {
        try {
            openapp();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void openapp() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("OnePlus 10 Pro 5G");
        options.setUdid("666c1f50");
        options.setPlatformName("Android");
        options.setPlatformVersion("15");
        options.setAppPackage("com.dubizzle.horizontal");
        options.setAppActivity("com.dubizzle.horizontal.activities.MainActivity");
        options.setAppWaitActivity("*");
        options.setAutomationName("UiAutomator2"); // Required for Appium 2.x
        options.setCapability("autoGrantPermissions", true);

        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);

        System.out.println("Dubizzle app launched successfully!");

        // 1. Wait up to 10 seconds for the element to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

// 2. Locate the tab by its text
        WebElement propertyForRent = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.TextView[@text='Property for Rent']")
        ));
    }


}
