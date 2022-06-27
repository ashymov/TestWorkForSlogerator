package com.gmail.sashymov.TestWorkForSlogerator.uiTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.gmail.sashymov.TestWorkForSlogerator.settings.Constants.*;

public class UITest {


    WebDriver driver = new ChromeDriver();

    @Before
    public void setUp() {

        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver.exe");
        driver.manage().window().maximize();
        driver.get(UI_TEST_URL);



    }

    @Test
    public void UITest() {
        //авторизация
        driver.findElement(By.id("UserLogin_username")).sendKeys(ACCOUNT_LOGIN);
        driver.findElement(By.id("UserLogin_password")).sendKeys(ACCOUNT_PASSWORD);
        driver.findElement(By.name("yt0")).click();

        driver.findElement(By.linkText("Users")).click();
        //получаем список игроков
        driver.findElement(By.linkText("Players")).click();
        //сортируем по username
        driver.findElement(By.linkText("Username")).click();

    }

    @After
    public void close() throws InterruptedException {
        driver.close();
    }
}