import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class locked_user {
    WebDriver driver;
    WebDriverWait wait;
    Actions actions;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
        driver.get("https://bstackdemo.com/signin");
    }

    @Test
    public void testLockedOutUserScenario() throws InterruptedException {
        // ننتظر ثواني عشان نتأكد إن الصفحة فتحت بشكل كامل قدامنا
        Thread.sleep(3000);

        // 1. بنفتح قائمة اليوزر ونختار الحساب المحظور: locked_out_user
        WebElement usernameDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
        usernameDropdown.click();
        Thread.sleep(1000);
        actions.sendKeys("locked_out_user").sendKeys(Keys.ENTER).perform();
        Thread.sleep(1000);

        // 2. بندخل على خانة الباسورد ونكتب الرقم السري الموحد
        WebElement passwordDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        actions.moveToElement(passwordDropdown).click().sendKeys("testingisfun99").sendKeys(Keys.ENTER).perform();
        Thread.sleep(1000);

        // 3. بنضغط على زرار تسجيل الدخول (LOG IN)
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn")));
        loginButton.click();
        Thread.sleep(2000);

        // 4.  بننتظر ظهور رسالة الخطأ باللون الأحمر على الشاشة
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".api-error")
        ));

        // 5. بنقرأ نص رسالة الخطأ برمجياً ونتأكد إنها الرسالة المخصصة للحسابات المقفولة
        String expectedError = "Your account has been locked.";
        Assert.assertEquals(errorMessage.getText(), expectedError, "خطأ: رسالة حظر الحساب لم تظهر أو النص غير صحيح!");

        // ننتظر 4 ثواني كاملة عشان نوري الباشمهندس رسالة رفض الدخول باللون الأحمر على الشاشة
        Thread.sleep(4000);
    }

//    @AfterMethod
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
}