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

public class existing_orders_user {
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
    public void testExistingOrdersScenario() throws InterruptedException {
        // ننتظر ثواني عشان نتأكد إن الصفحة فتحت بشكل كامل قدامنا
        Thread.sleep(3000);

        // بنفتح قائمة اليوزر ونختار اسم الحساب المطلوب
        WebElement usernameDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
        usernameDropdown.click();
        Thread.sleep(1000);
        actions.sendKeys("existing_orders_user").sendKeys(Keys.ENTER).perform();

        // بندخل على خانة الباسورد ونكتب الرقم السري الموحد
        WebElement passwordDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        actions.moveToElement(passwordDropdown).click().sendKeys("testingisfun99").sendKeys(Keys.ENTER).perform();
        Thread.sleep(1000);

        // بنضغط على زرار تسجيل الدخول عشان ندخل الصفحة الرئيسية
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn")));
        loginButton.click();

        // بنعمل فحص سريع للتأكد إن الاسم الجديد ظهر صح في الشاشة فوق
        WebElement loggedInUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".username")));
        Assert.assertEquals(loggedInUser.getText(), "existing_orders_user");

        // بنجرب الفلترة من القائمة اللي على الشمال وبنختار OnePlus بالضغط المباشر
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@class='checkmark' and text()='OnePlus']")
        )).click();

        // ننتظر 3 ثواني كاملة لحد ما الصفحة تستقر واللجنة تشوف الفلترة نجحت
        Thread.sleep(3000);

        // بنعمل ريفريش كامل للصفحة عشان نلغي الفلتر ونرجع كل المنتجات بأمان وبدون أي أخطاء
        driver.navigate().refresh();

        // ننتظر ثانيتين للتأكد من اكتمال تحميل الصفحة مجدداً
        Thread.sleep(2000);

        // بننتظر مربع البحث الجديد يظهر ويكون جاهز للتفاعل بعد الـ Refresh
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[placeholder='Search']")
        ));

        // بنعمل كليك خفيف عليه الأول عشان نثبت الماوس جواه، وبعدين نمسح ونكتب
        searchBox.click();
        searchBox.clear();
        searchBox.sendKeys("iPhone");

        // بنضغط على زرار السيرش الرمادي عشان نأكد عملية البحث الأولية
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.bg-gray-100")
        ));
        searchButton.click();
        Thread.sleep(2000);

        // [هنا خطوة النزول]: بننزل بالصفحة لتحت براحة وخطوة خطوة عشان نشوف المنتجات بوضوح
        for (int i = 0; i < 4; i++) {
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(1000); // نزول تدريجي هادئ ومريح
        }
        Thread.sleep(3000);

        // بنطلع لفووق تاني بسرعة عشان نرجع لمربع البحث ونعمل التجربة التانية
        actions.sendKeys(Keys.HOME).perform();
        Thread.sleep(2000);

        // بنعيد تعريف مربع البحث من جديد هنا بعد ما طلعنا فوق عشان نتجنب الـ Stale Exception
        searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[placeholder='Search']")
        ));

        // بنحدد النص القديم ونمسحه بالكامل عشان نكتب الأرقام الجديدة بأمان
        searchBox.click();
        searchBox.sendKeys(Keys.CONTROL + "a");
        searchBox.sendKeys(Keys.BACK_SPACE);
        searchBox.sendKeys("123");
        Thread.sleep(2000);

        // بنعيد تعريف زرار السيرش برضه قبل ما نضغط عليه عشان يشتغل بضمان
        searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.bg-gray-100")
        ));
        searchButton.click();

        // ننتظر 4 ثواني لمشاهدة رد فعل الموقع مع الرقم العشوائي قبل نهاية التست
        Thread.sleep(4000);
    }

//    @AfterMethod
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
}