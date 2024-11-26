package com.softserve.edu10;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;


public class GreenCitySignInTest {

    @FindBy(xpath = "//img[@alt ='sing in button' and contains(@class, 'ubs-header-sing-in-img')]")
    private WebElement signInButton;

    @FindBy(css = ".ng-star-inserted > h1")
    private WebElement welcomeText;
    @FindBy(css = ".ng-star-inserted > h2")
    private WebElement signInDetailsText;

    @FindBy(css = "form > label[for ='email']")
    private WebElement emailLabel;
    @FindBy(css = "input#email")
    private WebElement emailPlaceholder;
    @FindBy(css = "form > label[for ='password']")
    private WebElement passwordLabel;
    @FindBy(css = "input#password")
    private WebElement passwordPlaceholder;

    @FindBy(id = "email")
    private WebElement emailInput;
    @FindBy(id = "password")
    private WebElement passwordInput;
    @FindBy(css = ".greenStyle")
    private WebElement signInSubmitButton;
    @FindBy(css = "p.name")
    private WebElement name;

    @FindBy(css = "li .header_user-name")
    private WebElement menu;
    @FindBy(css = "li[aria-label='sign-out'] a")
    private WebElement signOut;

    @FindBy(css = ".alert-general-error")
    private WebElement errorMessage;

    @FindBy(xpath = "//*[@id='pass-err-msg']/app-error/div")
    private WebElement errorPassword;

    @FindBy(xpath = "//*[@id='email-err-msg']/app-error/div")
    private WebElement errorEmail;

    @FindBy(css = "img[alt='close button']")
    private WebElement closeButton;

    private static WebDriver driver;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:4205/#/greenCity");

    }

    @BeforeEach
    public void initPageElements() {
        PageFactory.initElements(driver, this);
    }

    @Test
    public void verifyTitle() {
        try {
            Assertions.assertEquals("GreenCity", driver.getTitle());
            assertThat(driver.getTitle(), is("GreenCity"));
            System.out.println("Title is " + driver.getTitle());
        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @ParameterizedTest
    @CsvSource({
            "emailfortest@gmail.com, Qwerty!2345"

    })
    public void signIn(String email, String password) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(signInButton));
            signInButton.click();
            System.out.println("'Sign in' button was clicked");

            wait.until(ExpectedConditions.visibilityOf(welcomeText));
            assertThat(welcomeText.getText(), is("З поверненням!"));
            assertThat(signInDetailsText.getText(), is("Будь ласка, внесiть свої дані для входу."));
            System.out.println("'Welcome text' is correct");

            assertThat(emailLabel.getText(), is("Електронна пошта"));
            assertThat(passwordLabel.getText(), is("Пароль"));
            System.out.println("'Email' and 'Password' labels are correct");

            String emailPlaceholderValue = emailInput.getAttribute("placeholder");
            assertThat(emailPlaceholderValue, is("example@email.com"));
            String passwordPlaceholderValue = passwordInput.getAttribute("placeholder");
            assertThat(passwordPlaceholderValue, is("Пароль"));
            System.out.println("'Email' and 'Password' placeholders are correct");

            wait.until(ExpectedConditions.visibilityOf(emailInput));
            emailInput.sendKeys(email);
            wait.until(ExpectedConditions.visibilityOf(passwordInput));
            passwordInput.sendKeys(password);

            assertThat(signInSubmitButton.getText(), is("Увійти"));
            System.out.println("'Увійти' button is correct");
            signInSubmitButton.click();
            wait.until(ExpectedConditions.visibilityOf(name));
            assertThat(name.getText(), is("NataliiaHrusha"));
            System.out.println("Login successful");
            wait.until(ExpectedConditions.visibilityOf(menu));
            menu.click();
            wait.until(ExpectedConditions.visibilityOf(signOut));
            signOut.click();
            System.out.println("Sign out");

        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }


    }


    @ParameterizedTest
    @CsvSource({
            "samplestesgreencity.com, Перевірте, чи правильно вказано вашу адресу електронної пошти",
            "'', Введіть пошту",
            "'      ', Введіть пошту"

    })
    public void emailNotValid(String email, String message) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(signInButton));
            signInButton.click();
            emailInput.sendKeys(email);
            emailLabel.click();
            wait.until(ExpectedConditions.visibilityOf(errorEmail));
            assertThat(errorEmail.getText(), containsString(message));
            System.out.println("'" + message + "'" + " error message appears after inputting invalid email");
            assertThat(signInSubmitButton.getAttribute("disabled"), is("true"));
            signInSubmitButton.click();
            wait.until(ExpectedConditions.invisibilityOf(name));
            System.out.println("'Sing in' button is disabled");
            closeButton.click();

        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    @ParameterizedTest
    @CsvSource({
            "'', Введіть пароль",
            "'     ', Пароль повинен містити принаймі 8 символів без пробілів",
            "1, Пароль повинен містити принаймі 8 символів без пробілів",
            "1234567, Пароль повинен містити принаймі 8 символів без пробілів",
            "123456789012345678901, Пароль повинен містити менше 20 символів без пробілів.",
            "1234567, Пароль повинен містити принаймі 8 символів без пробілів",


    })
    public void passwordNotValid(String password, String message) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(signInButton));
            signInButton.click();
            passwordInput.sendKeys(password);
            passwordLabel.click();
            wait.until(ExpectedConditions.visibilityOf(errorPassword));
            assertThat(errorPassword.getText(), containsString(message));
            System.out.println("'" + message + "'" + " error message appears after inputting invalid password");
            assertThat(signInSubmitButton.getAttribute("disabled"), is("true"));
            signInSubmitButton.click();
            wait.until(ExpectedConditions.invisibilityOf(name));
            System.out.println("'Sing in' button is disabled");
            closeButton.click();
        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    @ParameterizedTest
    @CsvSource({
            "'', '', Введіть пошту, Будь ласка заповніть всі обов'язкові поля"

    })
    public void signInValid(String email, String password, String emailMessage, String passwordMessage) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(signInButton));
            signInButton.click();

            emailInput.sendKeys(email);
            emailLabel.click();
            wait.until(ExpectedConditions.visibilityOf(errorEmail));
            assertThat(errorEmail.getText(), containsString(emailMessage));
            System.out.println("'" + emailMessage + "'" + " error message appears after inputting invalid email");

            passwordInput.sendKeys(password);
            passwordLabel.click();
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            assertThat(errorMessage.getText(), containsString(passwordMessage));
            System.out.println("'" + passwordMessage + "'" + " error message appears after inputting invalid password");
            assertThat(signInSubmitButton.getAttribute("disabled"), is("true"));
            signInSubmitButton.click();
            wait.until(ExpectedConditions.invisibilityOf(name));
            System.out.println("'Sing in' button is disabled");
            closeButton.click();
        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    @ParameterizedTest
    @CsvSource({
            "emailfortest@gmail.com, Qwerty!23455, Введено невірний email або пароль",
            "emailfortest1@gmail.com, Qwerty!2345, Введено невірний email або пароль"

    })
    public void signInNotValid(String email, String password, String passwordMessage) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(signInButton));
            signInButton.click();

            emailInput.sendKeys(email);
            emailLabel.click();

            passwordInput.sendKeys(password);
            passwordLabel.click();

            wait.until(ExpectedConditions.elementToBeClickable(signInSubmitButton));
            signInSubmitButton.click();

            wait.until(ExpectedConditions.invisibilityOf(name));
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            assertThat(errorMessage.getText(), containsString(passwordMessage));
            System.out.println("'" + passwordMessage + "'" + " error message appears after inputting invalid password or email");

            closeButton.click();
        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
        System.out.println("Browser was closed");
    }

}