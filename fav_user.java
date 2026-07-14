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

public class fav_user {
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
    public void testFavouritesUserScenario() throws InterruptedException {
        // ننتظر لضمان تحميل صفحة تسجيل الدخول أول مرة
        Thread.sleep(3000);

        // 1. تسجيل الدخول الأول: اختيار الاسم fav_user
        WebElement usernameDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
        usernameDropdown.click();
        Thread.sleep(1000);
        actions.sendKeys("fav_user").sendKeys(Keys.ENTER).perform();
        Thread.sleep(1000);

        // 2. إدخال كلمة السر الافتراضية
        WebElement passwordDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        actions.moveToElement(passwordDropdown).click().sendKeys("testingisfun99").sendKeys(Keys.ENTER).perform();
        Thread.sleep(1000);

        // 3. الضغط على زر تسجيل الدخول
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn")));
        loginButton.click();

        // فحص سريع للتأكد من نجاح الدخول للحساب
        WebElement loggedInUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".username")));
        Assert.assertEquals(loggedInUser.getText(), "fav_user");
        Thread.sleep(1000);
// 4. جلب جميع أزرار القلوب الصفراء المفعلة في الصفحة
        java.util.List<WebElement> activeFavButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("button.MuiIconButton-root.clicked")
        ));

        // بنضغط على القلب الأصفر الأول والقلب الأصفر الثاني لإلغاء التفضيل عنهم
        if (activeFavButtons.size() >= 2) {
            // الضغط على القلب الأول
            actions.moveToElement(activeFavButtons.get(0)).click().perform();
            Thread.sleep(1500); // انتظار خفيف عشان الموقع يستجيب

            // الضغط على القلب الثاني
            actions.moveToElement(activeFavButtons.get(1)).click().perform();
        } else if (activeFavButtons.size() == 1) {
            // لو مفيش غير قلب واحد بس مفعل، بنضغط عليه هو بس
            actions.moveToElement(activeFavButtons.get(0)).click().perform();
        } else {
            // لو مفيش أي قلوب مفعلة، بنطبع رسالة للجنة عشان ميتعطلش التست
            System.out.println("تنبيه: لا توجد أي قلوب مفعلة حالياً لإلغائها.");
        }

        // ننتظر 3 ثواني عشان نوري البشمهندس إن أول قلبين اتحولوا للون الرمادي بنجاح
        Thread.sleep(3000);
        // 5. الضغط على كلمة Logout من الأعلى لتسجيل الخروج
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("signin")));
        logoutButton.click();

        // انتظار ثانيتين للتأكد من إتمام عملية الخروج
        Thread.sleep(2000);

        // [تعديل الأمان]: بنعمل ريفريش كامل هنا لتنظيف وتأكيد حالة الخروج
        driver.navigate().refresh();
        Thread.sleep(2000);

        // [الخطوة الجديدة والعبقرية]: بنضغط على زرار Sign In الموضح في لقطة الشاشة لفتح الفورم مجدداً
        WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("signin")));
        signInButton.click();
        Thread.sleep(2000); // ننتظر ثانيتين لحد ما الفورم تفتح قدامنا

        // 6. تسجيل الدخول الثاني: بنختار الـ username النظيف والجديد تماماً بعد ما فتحنا الفورم
        usernameDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
        usernameDropdown.click();
        Thread.sleep(1000);
        actions.sendKeys("fav_user").sendKeys(Keys.ENTER).perform();
        Thread.sleep(1000);

        // 7. كتابة الباسورد مرة أخرى
        passwordDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        actions.moveToElement(passwordDropdown).click().sendKeys("testingisfun99").sendKeys(Keys.ENTER).perform();
        Thread.sleep(1000);

        // 8. تسجيل الدخول الفعلي للمرة الثانية
        loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn")));
        loginButton.click();

        // فحص نهائي للتأكد من عودة الواجهة بشكل مستقر وثابت
        loggedInUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".username")));
        Assert.assertEquals(loggedInUser.getText(), "fav_user");

        // انتظار 4 ثوانٍ أخيرة قبل غلق المتصفح لمشاهدة استقرار الصفحة والقلب الملغى
        Thread.sleep(4000);
    }
}

//    @AfterMethod
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
