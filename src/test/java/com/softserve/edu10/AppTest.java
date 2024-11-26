package com.softserve.edu10;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AppTest {
    public static void main(String[] args) {
        WebDriver driver  = new ChromeDriver();

        try {
            driver.get("http://localhost:4205/#/greenCity");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//img[@alt ='sing in button' and contains(@class, 'ubs-header-sing-in-img')]")));
            signInButton.click();
            System.out.println("'Sign in' button was clicked");

            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
            emailInput.sendKeys("emailfortest@gmail.com");
            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            passwordInput.sendKeys("Qwerty!2345");

            WebElement logInButton = driver.findElement(By.className("greenStyle"));
            logInButton.click();
            System.out.println("'Submit Sign in' button was clicked");
        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
 //       finally {
 //           driver.quit();
 //           System.out.println("Browser was closed");
//        }
    }
}
