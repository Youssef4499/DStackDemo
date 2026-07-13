
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
public class logi {
    public class LoginTest {
        WebDriver driver;

        @BeforeMethod
        public void setUp() {
            // تم الاستغناء عن System.setProperty للاعتماد على Selenium Manager التلقائي الحديث
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // بونص احترافي لانتظار العناصر
            driver.get("https://www.saucedemo.com/");
        }

        // 1. الاختبار الإيجابي (Positive Test) - تم تعديل المستخدم لـ standard_user لكي ينجح الدخول
        @Test
        public void testSuccessfulLogin() {
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();

            Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "خطأ: لم يتم الانتقال لصفحة المنتجات!");
        }

        // 2. الاختبارات السلبية (Negative Tests)
        @Test
        public void testInvalidUsername() {
            driver.findElement(By.id("user-name")).sendKeys("wrong_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();

            String errorMessage = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
            Assert.assertTrue(errorMessage.contains("Username and password do not match any user in this service"));
        }

        @Test
        public void testInvalidPassword() {
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("wrong_password");
            driver.findElement(By.id("login-button")).click();

            String errorMessage = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
            Assert.assertTrue(errorMessage.contains("Username and password do not match any user in this service"));
        }

        @Test
        public void testEmptyUsername() {
            driver.findElement(By.id("user-name")).sendKeys("");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();

            String errorMessage = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
            Assert.assertTrue(errorMessage.contains("Username is required"));
        }

        @Test
        public void testEmptyPassword() {
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("");
            driver.findElement(By.id("login-button")).click();

            String errorMessage = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
            Assert.assertTrue(errorMessage.contains("Password is required"));
        }

        @AfterMethod
        public void tearDown() {
            if (driver != null) {
                driver.quit();
            }
            // ---- كود الفلترة المطلوب إضافته ----

// 1. تحديد عنصر القائمة المنسدلة للفلترة
            org.openqa.selenium.WebElement filterDropdown = driver.findElement(By.cssSelector(".product_sort_container"));

// 2. استخدام مكتبة Select لاختيار ترتيب السعر (من الأقل للأعلى)
            org.openqa.selenium.support.ui.Select selectFilter = new Select(filterDropdown);
            selectFilter.selectByValue("lohi");

// 3. التحقق (Assertion) من أن الفلترة تم تطبيقها بنجاح
            String activeFilterText = selectFilter.getFirstSelectedOption().getText();
            Assert.assertEquals(activeFilterText, "Price (low to high)", "خطأ: لم يتم تطبيق فلتر السعر بنجاح!");

//    public class LoginPage {
//        private WebDriver driver;
//        private WebDriverWait wait;
//
//        // المحددات (Locators) بناءً على الـ Classes الخاصة بك
//        private By usernameDropdown = By.cssSelector(".css-1hwfws3");
//        private By passwordDropdown = By.cssSelector(".css-yk16xz-control");
//        private By loginButton = By.cssSelector(".Button_root__24MxS.Button_slim__2caxo");
//        private By logoutButton = By.id("logout");
//        private By errorMessage = By.cssSelector(".api-error"); // كلاس رسالة الخطأ في الموقع
//
//        public LoginPage(WebDriver driver) {
//            this.driver = driver;
//            this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        }
//
//        public void selectUsername(String username) {
//            if (username != null && !username.isEmpty()) {
//                WebElement element = driver.findElement(usernameDropdown);
//                element.click();
//                Actions actions = new Actions(driver);
//                actions.sendKeys(username).sendKeys(Keys.ENTER).perform();
//            }
//        }
//
//        public void selectPassword(String password) {
//            if (password != null && !password.isEmpty()) {
//                WebElement element = driver.findElement(passwordDropdown);
//                element.click();
//                Actions actions = new Actions(driver);
//                actions.sendKeys(password).sendKeys(Keys.ENTER).perform();
//            }
//        }
//
//        public void clickLogin() {
//            driver.findElement(loginButton).click();
//        }
//
//        public boolean isLogoutButtonDisplayed() {
//            try {
//                return wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton)).isDisplayed();
//            } catch (Exception e) {
//                return false;
//            }
//        }
//
//        public String getErrorMessageText() {
//            try {
//                return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
//            } catch (Exception e) {
//                return "";
//            }
//        }


