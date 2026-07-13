google:ds_python_interpreter(code='''
code_content = """package com.example.test;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class LoginTest {
    WebDriver driver;


    @BeforeMethod
    public void setUp() {
        // تأكد من ضبط المسار الصحيح لملف chromedriver.exe في جهازك
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }


    @Test
    public void testLogin() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }


    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
"""


with open("LoginTest.java", "w", encoding="utf-8") as f:
    f.write(code_content)
''')