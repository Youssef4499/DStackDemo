import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class image_not_loading_user {
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
    public void testImageUserScenario() throws InterruptedException {
        // --- البداية: انتظار2 ثوانٍ بعد فتح صفحة تسجيل الدخول لرؤية الواجهة ---
        Thread.sleep(2000);

        // 1. الضغط على حقل اختيار اسم المستخدم لفتح القائمة
        WebElement usernameDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
        usernameDropdown.click();

        // انتظار 1 ثوانٍ لرؤية القائمة وهي تفتح
        Thread.sleep(1000);

        // 2. كتابة اسم المستخدم (image_not_loading_user) والضغط على Enter
        actions.sendKeys("image_not_loading_user").sendKeys(Keys.ENTER).perform();

        // انتظار 1 ثوانٍ لمشاهدة اختيار اليوزر بنجاح
        Thread.sleep(1000);

        // 3. الضغط على حقل اختيار كلمة السر وكتابة الباسورد الافتراضي والضغط على Enter
        WebElement passwordDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        actions.moveToElement(passwordDropdown).click().sendKeys("testingisfun99").sendKeys(Keys.ENTER).perform();


        // 4. الضغط على زر تسجيل الدخول (LOG IN)
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn")));
        loginButton.click();

        // 5. التحقق من نجاح الدخول بظهور اسم المستخدم الجديد في الأعلى
        WebElement loggedInUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".username")));
        Assert.assertEquals(loggedInUser.getText(), "image_not_loading_user", "خطأ: لم يتم تسجيل الدخول بنجاح!");

        // انتظار 2 ثوانٍ لرؤية الصفحة الرئيسية بعد الدخول بنجاح وقبل عمل الفلترة
        Thread.sleep(2000);

        // 6. الفلترة بطريقة مختلفة (ترتيب السعر تصاعدياً من الأقل للأعلى عبر قائمة Order by)
        WebElement orderDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sort select")));
        Select selectOrder = new Select(orderDropdown);
        selectOrder.selectByValue("lowestprice"); // اختيار الترتيب من الأقل للأعلى

        //انتظار 3 ثوانٍ لرؤية المنتجات
        Thread.sleep(3000);

        // 7. جلب جميع أزرار "Add to cart" المتاحة بالترتيب الجديد
        java.util.List<WebElement> addToCartButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".shelf-item__buy-btn")
        ));

        // 8. عمل Scroll والنزول لأسفل للوصول للمنتج الرابع وإضافته للكارت
        if (addToCartButtons.size() >= 4) {
            // لعمل Scroll تلقائي  ثم الضغط عليه
            actions.moveToElement(addToCartButtons.get(3)).click().perform();
        } else {
            Assert.fail("خطأ: لم يتم العثور على 4 منتجات في الصفحة لعمل التست!");
        }

        // انتظار 3 ثوانٍ لرؤية السلة وهي مفتوحة وبها المنتج
        Thread.sleep(3000);

        // 9. التحقق السريع من أن المنتج تمت إضافته للسلة بنجاح أولاً
        WebElement cartQuantityBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bag__quantity")));
        Assert.assertEquals(cartQuantityBadge.getText(), "1", "خطأ: المنتج لم يظهر داخل الكارت!");

        // انتظار 3 ثوانٍ إضافية للتأكيد
        Thread.sleep(3000);

        // 10. انتظار ظهور زر الـ Checkout في أسفل السلة والضغط عليه
        WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".buy-btn")));
        actions.moveToElement(checkoutButton).click().perform();

        // انتظار 4 ثوانٍ أخيرة لمشاهدة تأثير الضغطة والانتقال للخطوة التالية
        Thread.sleep(4000);
    }

//    @AfterMethod
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
}
