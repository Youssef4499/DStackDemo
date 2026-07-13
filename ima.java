package com.example.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;

public class LoginTest {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com/");
    }

    // 1. الاختبار الإيجابي الشامل (الدخول -> عنوان الصفحة -> فحص الصور -> الفلترة -> السلة والتبديل)
    @Test
    public void testCompleteUserFlowAndUIValidation() throws InterruptedException {
        // ---- فحص أساسي 1: التحقق من عنوان صفحة الدخول قبل أي شيء ----
        String loginPageTitle = driver.getTitle();
        Assert.assertEquals(loginPageTitle, "Swag Labs", "خطأ: عنوان صفحة الدخول غير صحيح!");

        // ---- خطوة تسجيل الدخول بالحساب الجديد ----
        driver.findElement(By.id("user-name")).sendKeys("error_user"); 
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "خطأ: لم يتم تسجيل الدخول بنجاح!");

        // ---- فحص أساسي 2 (المطلوب): التحقق من أن صور المنتجات تعمل وليست مكسورة ----
        List<WebElement> productImages = driver.findElements(By.cssSelector(".inventory_item_img img"));
        System.out.println("عدد الصور التي يتم فحصها: " + productImages.size());
        
        for (WebElement img : productImages) {
            String imageSrc = img.getAttribute("src");
            // التأكد من أن رابط الصورة موجود ولا يحتوي على صورة الـ "placeholder" المكسورة
            Assert.assertNotNull(imageSrc, "خطأ: توجد صورة لا تحتوي على رابط (src)!");
            Assert.assertFalse(imageSrc.contains("sl-404"), "خطأ: تم اكتشاف صورة مكسورة (Broken Image) في الموقع!");
        }

        // ---- خطوة الفلترة ----
        WebElement filterDropdown = driver.findElement(By.cssSelector(".product_sort_container"));
        Select selectFilter = new Select(filterDropdown);
        selectFilter.selectByValue("lohi");

        // ---- خطوة إضافة أول منتج إلى السلة ----
        driver.findElement(By.xpath("(//button[text()='Add to cart'])[1]")).click();

        // التأكد من أن السلة أصبح بها رقم "1"
        String cartBadgeBefore = driver.findElement(By.cssSelector(".shopping_cart_badge")).getText();
        Assert.assertEquals(cartBadgeBefore, "1", "خطأ: لم يتم إضافة المنتج للسلة!");

        // ---- خطوة تسجيل الخروج ----
        driver.findElement(By.id("react-burger-menu-btn")).click(); 
        Thread.sleep(1000); 
        driver.findElement(By.id("logout_sidebar_link")).click(); 

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/", "خطأ: لم يتم تسجيل الخروج بنجاح!");

        // ---- خطوة تسجيل الدخول مرة أخرى للتحقق من حفظ السلة ----
        driver.findElement(By.id("user-name")).sendKeys("error_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // ---- التحقق النهائي من ثبات السلة ----
        String cartBadgeAfter = driver.findElement(By.cssSelector(".shopping_cart_badge")).getText();
        Assert.assertEquals(cartBadgeAfter, "1", "خطأ: المنتج اختفى من السلة بعد إعادة تسجيل الدخول!");
        
        System.out.println("✅ تم فحص العنوان، سلامة الصور، الفلترة، والتحقق من الحفاظ على السلة بنجاح!");
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
        driver.findElement(By.id("user-name")).sendKeys("error_user");
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
        driver.findElement(By.id("user-name")).sendKeys("error_user");
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
    }
}