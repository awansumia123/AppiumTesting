import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.NoSuchElementException;

public class LaunchA extends BaseTest {

    // --- Wait alternatives (no WebDriverWait in some tests) ---

    // Option 1: WebDriverWait (explicit wait – wait until condition)
    private WebDriverWait getWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Option 2: FluentWait – custom timeout, polling interval, ignored exceptions
    private FluentWait<AndroidDriver> getFluentWait() {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);
    }

    // Option 3: Implicit wait is set in BaseTest – use driver.findElement() and it will wait up to that duration
    // Option 4: WaitHelper.sleepSeconds(n) or WaitHelper.sleepMillis(n) for fixed delay

    @Test(priority = 1, description = "Tap Get Started and Allow Access – uses FluentWait")
    public void testOnboardingGetStartedAndAllowAccess() {
        FluentWait<AndroidDriver> wait = getFluentWait();
        WebElement getStarted = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.Button[@resource-id=\"com.dubizzle.horizontal:id/getStartedButton\"]")));
        getStarted.click();

        WebElement allowAccess = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.Button[@resource-id=\"com.dubizzle.horizontal:id/allowAccessBTN\"]")));
        allowAccess.click();
    }

    @Test(priority = 2, description = "Continue with Email – uses FluentWait", dependsOnMethods = "testOnboardingGetStartedAndAllowAccess")
    public void testContinueWithEmail() {
        FluentWait<AndroidDriver> wait = getFluentWait();
        WebElement continueEmail = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.ScrollView/android.widget.LinearLayout/androidx.appcompat.widget.LinearLayoutCompat[3]")));
        continueEmail.click();
    }

    @Test(priority = 3, description = "Enter credentials and login – uses implicit wait + WaitHelper", dependsOnMethods = "testContinueWithEmail")
    public void testEnterCredentialsAndLogin() {
        // Rely on implicit wait (set in BaseTest): findElement waits up to 15s
        WebElement emailField = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id=\"com.dubizzle.horizontal:id/edtEmail\"]"));
        emailField.sendKeys("sumia.awan@dubizzlelabs.com");

        WebElement passwordField = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id=\"com.dubizzle.horizontal:id/edtPass\"]"));
        passwordField.sendKeys("Aws@0987");

        WebElement loginButton = driver.findElement(AppiumBy.xpath("//android.widget.Button[@resource-id=\"com.dubizzle.horizontal:id/btnLogin\"]"));
        loginButton.click();

        // Fixed delay: wait for navigation/loading after login (alternative to explicit wait)
        WaitHelper.sleepSeconds(2);
    }

    @Test(priority = 4, description = "Dismiss Biometrices ", dependsOnMethods = "testEnterCredentialsAndLogin")
    public void dismissBiometric() {
        WebElement dismissButton = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.dubizzle.horizontal:id/cancel_button\"]"));
        dismissButton.click();
        WaitHelper.sleepSeconds(2);
    }

    @Test(priority = 5, description = "Dismiss verification if it appears")
    public void dismissVerification() {
        try {
            // Use a shorter wait (e.g., 5 seconds) so we don't waste time if it's not there
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));

            WebElement dismissveri = shortWait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//android.widget.Button[@resource-id=\"com.dubizzle.horizontal:id/btn_skip\"]")));

            dismissveri.click();
            System.out.println("Verification popup found and dismissed.");
            WaitHelper.sleepSeconds(2);
        } catch (Exception e) {
            // If the element is not found, TestNG catches the error and we just move on
            System.out.println("Verification popup did not appear. Moving to next step.");
        }
    }

    @Test(priority = 6, description = "Open first listing", dependsOnMethods = "testEnterCredentialsAndLogin")
// Note: Changed dependsOnMethods to skip the optional verification step
    public void testOpenFirstListing() {
        WebElement firstListing = getFluentWait().until(d ->
                driver.findElement(By.xpath("(//android.widget.ImageView[@resource-id=\"com.dubizzle.horizontal:id/grid_item_image\"])[1]")));
        firstListing.click();
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test(priority = 7, description = "show results tap", dependsOnMethods = "testOpenFirstListing")
    public void clickShowResults() {
        // Wait for screen to settle
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Target the TextView or Button inside the FrameLayout for a more precise click
        WebElement showResults = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.id("com.dubizzle.horizontal:id/result_button")
        ));

        showResults.click();
        System.out.println("Tapped on Show Results button.");
    }

//    @Test(priority = 8, description = "Perform scroll on the LPV", dependsOnMethods = "clickShowResults")
//    public void scrollOnLPV() {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
//
//        // 1. Wait for the list AND the first item to ensure it's not an empty screen
//        wait.until(ExpectedConditions.visibilityOfElementLocated(
//                AppiumBy.id("com.dubizzle.horizontal:id/recycler")
//        ));
//
//        // Safety check for the first card to ensure data is loaded
//        wait.until(ExpectedConditions.presenceOfElementLocated(
//                AppiumBy.id("com.dubizzle.horizontal:id/listingItemRootContainer")
//        ));
//
//        System.out.println("LPV Recycler and items found. Starting scroll...");
//
//        for (int i = 0; i < 10; i++) {
//            performScrollAction();
//            WaitHelper.sleepMillis(800);
//        }

}