import org.openqa.selenium.WebDriver;

import java.time.Duration;

/**
 * Alternative wait options besides WebDriverWait.
 * Use WebDriverWait/FluentWait for "wait until condition"; use these only when appropriate.
 */
public final class WaitHelper {

    private WaitHelper() {}

    /**
     * Simple fixed delay. No condition – waits the full time regardless.
     * Use sparingly; prefer explicit/fluent wait for element visibility/clickability.
     */
    public static void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Wait interrupted", e);
        }
    }

    /**
     * Same as above with millisecond precision.
     */
    public static void sleepMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Wait interrupted", e);
        }
    }

    /**
     * Set implicit wait on the driver. Every findElement/findElements will wait up to this duration.
     * Call once (e.g. in BaseTest @BeforeClass). Do not mix with long explicit waits.
     */
    public static void setImplicitWait(WebDriver driver, Duration duration) {
        driver.manage().timeouts().implicitlyWait(duration);
    }
}
