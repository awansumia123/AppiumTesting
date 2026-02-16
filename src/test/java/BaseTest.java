import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import java.util.Collections;

public class BaseTest {

    public  AndroidDriver driver;
    public   AppiumDriverLocalService service;

    @BeforeClass
    public void ConfigureAppium() throws URISyntaxException, MalformedURLException {
        service = new AppiumServiceBuilder().withAppiumJS(new File("C:/Users/Sumia Awan/AppData/Roaming/npm/node_modules/appium/build/lib/main.js"))
                .withIPAddress("127.0.0.1").usingPort(4723).build();
        service.start();



        UiAutomator2Options options = new UiAutomator2Options();

        options.setDeviceName("OnePlus 10 Pro 5G");
        options.setUdid("666c1f50");
        options.setPlatformName("Android");
        options.setPlatformVersion("15");
        options.setAppPackage("com.dubizzle.horizontal");
        options.setAppActivity("com.dubizzle.horizontal.activities.routing.RoutingActivity");
        // This tells Appium to wait for BOTH the router and the main screen
        options.setCapability("appWaitActivity", "com.dubizzle.horizontal.activities.routing.RoutingActivity,com.dubizzle.horizontal.activities.MainActivity");
        // options.setAppActivity("com.dubizzle.horizontal.activities.MainActivity");
        options.setAppWaitActivity("*");
        options.setAutomationName("UiAutomator2"); // Required for Appium 2.x
        // Uninstall + reinstall app before session = fresh install every run
        // 1. Disable the uninstallation process
        options.setFullReset(false);

        // 2. Enable data clearing (Fast Reset)
         // This keeps the app installed but clears the login, cache, and history
         // so the 'Get Started' screen appears every time.
        options.setNoReset(false);
        options.setCapability("autoGrantPermissions", true);


        driver = new AndroidDriver(new URI("http://127.0.0.1:4723").toURL(), options);

        // Alternative to WebDriverWait: implicit wait – every findElement() waits up to this duration
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }
    public void performScrollAction() {
        // 1. Get the screen size of your OnePlus device
        Dimension size = driver.manage().window().getSize();

        // 2. Define coordinates (Center of screen horizontally)
        int startX = size.getWidth() / 2;

        // Start swipe at 70% of the screen height (bottom area)
        int startY = (int) (size.getHeight() * 0.7);

        // End swipe at 30% of the screen height (top area)
        int endY = (int) (size.getHeight() * 0.3);

        // 3. Create a PointerInput of type 'TOUCH' named 'finger'
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        // 4. Create a Sequence of actions
        Sequence scroll = new Sequence(finger, 1);

        // Move finger to start position
        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));

        // Finger goes down on the screen
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // Move finger to end position (taking 600ms for a smooth swipe)
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));

        // Finger lifts up
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // 5. Tell the driver to perform the sequence
        driver.perform(Collections.singletonList(scroll));
    }

    @AfterClass
    public void teardown()
    {

        if (driver != null) {
            driver.quit();
        }
        if (service != null) {
            service.stop();
        }


    }


}