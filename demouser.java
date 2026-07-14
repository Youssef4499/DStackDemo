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

public class demouser {
    WebDriver driver;
    WebDriverWait wait;
    Actions actions;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // إعداد الـ Wait والـ Actions بشكل مركزي
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);

        // الانتقال لرابط الموقع الجديد
        driver.get("https://bstackdemo.com/signin");
    }

    @Test
    public void testBrowserStackLogin() throws InterruptedException {
        // 1. انتظار ظهور حقل اختيار اسم المستخدم والضغط عليه
        WebElement usernameDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
        usernameDropdown.click();

        // 2. كتابة اسم المستخدم (demouser) والضغط على Enter
        actions.sendKeys("demouser").sendKeys(Keys.ENTER).perform();

        // 3. انتظار ظهور حقل اختيار كلمة السر والضغط عليه
        WebElement passwordDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        actions.moveToElement(passwordDropdown).click().sendKeys("testingisfun99").sendKeys(Keys.ENTER).perform();

        // 4. انتظار زر الـ Log In والضغط عليه
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn")));
        loginButton.click();

        // 5. التحقق من نجاح الدخول بظهور اسم المستخدم
        WebElement loggedInUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".username")));
        Assert.assertEquals(loggedInUser.getText(), "demouser", "خطأ: لم يتم تسجيل الدخول بنجاح!");

        // 6. خطوة الفلترة من اليسار: تفعيل فلتر Apple
        WebElement appleFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@class='checkmark' and text()='Apple']")
        ));
        appleFilter.click();

        // انتظار ثانية ونصف لرؤية الفلترة
        Thread.sleep(1500);

        // إلغاء الفلتر بالضغط عليه مجدداً
        appleFilter.click();
        Thread.sleep(1000);
// 7. جلب أزرار المفضلة لمنتجات Apple المفلترة باستخدام كلاس الزر الصحيح الموضح بالـ DOM
        java.util.List<WebElement> favButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("button.MuiIconButton-root")
        ));
        if (favButtons.size() >= 2) {
            favButtons.get(1).click();
        } else {
            Assert.fail("خطأ: لم يتم العثور على منتج ثانٍ لإضافته للمفضلة!");
        }
        Thread.sleep(1000);

        // 8. إضافة أول منتج للكارت
        WebElement firstAddToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".shelf-item__buy-btn")
        ));
        firstAddToCartButton.click();

        // 9. التحقق من تحديث عداد الكارت إلى "1"
        WebElement cartBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".bag__quantity")
        ));
        Assert.assertEquals(cartBadge.getText(), "1", "خطأ: لم يتم إضافة المنتج إلى الكارت بنجاح!");
    }
        }
//    }
//    @AfterMethod
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }





