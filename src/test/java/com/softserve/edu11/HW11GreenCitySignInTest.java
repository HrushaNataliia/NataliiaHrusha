package com.softserve.edu11;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HW11GreenCitySignInTest {

    private static final String BASE_URL = "http://localhost:4205/#/greenCity";
    private static final long IMPLICITLY_WAIT_SECONDS = 1L;
    private WebDriver driver;

    @FindBy(css = "img.ubs-header-sing-in-img")
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


    @BeforeAll
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
        PageFactory.initElements(driver, this);
        System.out.println("@BeforeAll executed");
        //  driver.manage().window().maximize();

    }

    @BeforeEach
    public void setupThis() {
        driver.get(BASE_URL);
        System.out.println("\t@BeforeEach executed");

    }

    @AfterEach
    public void tearThis() throws InterruptedException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            if (name.isDisplayed()) {
                wait.until(ExpectedConditions.visibilityOf(menu));
                menu.click();

                wait.until(ExpectedConditions.visibilityOf(signOut));
                signOut.click();

                System.out.println("Sign out completed.");
            }
        } catch (org.openqa.selenium.NoSuchElementException | org.openqa.selenium.TimeoutException e) {
            System.out.println("Sign out not required or timed out: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            driver.manage().deleteAllCookies();
            System.out.println("\t@AfterEach executed");
        }
    }


    @DisplayName("Verify that title is correct")
    @Test
    public void verifyTitle() {
        try {
            assertEquals("GreenCity", driver.getTitle());
            assertThat(driver.getTitle(), is("GreenCity"));
            System.out.println("Title is " + driver.getTitle());
        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    @DisplayName("Verify that error message is displayed and 'Sing in' button is disabled with invalid email")
    @ParameterizedTest
    @CsvSource(value = {
            "samplestesgreencity.com:Перевірте, чи правильно вказано вашу адресу електронної пошти",
            "'':Введіть пошту",
            "'      ':Введіть пошту"

    }, delimiter = ':')
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

    @DisplayName("Verify that error message is displayed and 'Sign in' button is disabled with invalid password")
    @ParameterizedTest
    @CsvSource({
            "'', EMPTY_PASSWORD",
            "'     ', SHORT_PASSWORD",
            "1, SHORT_PASSWORD",
            "1234567, SHORT_PASSWORD",
            "123456789012345678901, LONG_PASSWORD"
    })
    public void passwordNotValid(String password, PasswordErrorMessages errorMessage) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();
            passwordInput.sendKeys(password);
            passwordLabel.click();
            wait.until(ExpectedConditions.visibilityOf(errorPassword));
            assertThat(errorPassword.getText(), containsString(errorMessage.getMessage()));
            System.out.println("'" + errorMessage.getMessage() + "'" + " error message appears after inputting invalid password.");
            assertThat(signInSubmitButton.getAttribute("disabled"), is("true"));
            System.out.println("'Sign in' button is disabled.");
            if (closeButton.isDisplayed()) {
                closeButton.click();
            }
        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @DisplayName("Verify that error message is displayed and 'Sing in' button is disabled with blank email and password")
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

    @DisplayName("Verify that error message is displayed and 'Sing in' button is disabled when entering data of an unregistered user")
    @ParameterizedTest
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
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


    private static Stream<Arguments> textProvider() {
        return Stream.of(
                Arguments.of(
                        "З поверненням!",
                        "Будь ласка, внесiть свої дані для входу.",
                        "Електронна пошта",
                        "Пароль",
                        "example@email.com",
                        "Пароль",
                        "Увійти"
                )
        );
    }

    @DisplayName("Verify text of elements on the sign-in page")
    @ParameterizedTest(name = "{index} => welcomeText={0}, detailsText={1}, emailLabel={2}, passwordLabel={3}, emailPlaceholder={4}, passwordPlaceholder={5}, submitButtonText={6}")
    @MethodSource("textProvider")
    void assertText(String expectedWelcomeText,
                    String expectedDetailsText,
                    String expectedEmailLabel,
                    String expectedPasswordLabel,
                    String expectedEmailPlaceholder,
                    String expectedPasswordPlaceholder,
                    String expectedSubmitButtonText) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(signInButton));
            signInButton.click();
            System.out.println("'Sign in' button was clicked");

            wait.until(ExpectedConditions.visibilityOf(welcomeText));
            assertEquals(expectedWelcomeText, welcomeText.getText());
            assertEquals(expectedDetailsText, signInDetailsText.getText());
            System.out.println("'Welcome text' and 'Details text' are correct");

            assertEquals(expectedEmailLabel, emailLabel.getText());
            assertEquals(expectedPasswordLabel, passwordLabel.getText());
            System.out.println("'Email' and 'Password' labels are correct");

            assertEquals(expectedEmailPlaceholder, emailInput.getAttribute("placeholder"));
            assertEquals(expectedPasswordPlaceholder, passwordInput.getAttribute("placeholder"));
            System.out.println("'Email' and 'Password' placeholders are correct");

            assertEquals(expectedSubmitButtonText, signInSubmitButton.getText());
            System.out.println("'Sign In' button text is correct");
        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    public static Object[] validSignInData() {
        return new Object[]{
                new ValidLoginUser("emailfortest@gmail.com", "Qwerty!2345", "NataliiaHrusha")
        };
    }

    @DisplayName("Verify that Login is successful with valid email and password")
    @ParameterizedTest(name = "{index} => email={0}, password={1}, name={2}")
    @MethodSource("validSignInData")
    public void signIn(ValidLoginUser user) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(signInButton));
            signInButton.click();
            System.out.println("'Sign in' button was clicked");

            wait.until(ExpectedConditions.visibilityOf(emailInput));
            emailInput.sendKeys(user.getEmail());
            wait.until(ExpectedConditions.visibilityOf(passwordInput));
            passwordInput.sendKeys(user.getPassword());

            signInSubmitButton.click();

            wait.until(ExpectedConditions.visibilityOf(name));
            assertEquals(user.getUsername(), name.getText());
            System.out.println("Login successful");
        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    @AfterAll
    public void tear() {
        driver.quit();
        System.out.println("Browser was closed");
    }

}