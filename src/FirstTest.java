import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

import static org.junit.Assert.fail;

public class FirstTest {
    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "and80");
        capabilities.setCapability("platformName", "8.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "C:\\Users\\Roman\\IdeaProjects\\JavaAppiumAutomation\\apks\\org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);

    }

    @After
    public void tearDown() {
        driver.quit();
    }


    @Test
    public void firsTest() {

        waitForElementAndClick(
                By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
                "Cannot find button Skip",
                5
        );


        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find Search Wikipedia input",
                5);

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementPresent(
                By.xpath("//*[@resource-id=\"org.wikipedia:id/page_list_item_description\" and @text=\"Object-oriented programming language\"]"),
                "Cannot find 'Object-oriented programming language' topic searching by 'Java'",
                15
        );
    }


    @Test
    public void testCancelSearch() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
                "Cannot find button Skip",
                5
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find Search Wikipedia input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search field",
                5
        );

        waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "X still present on the page",
                5
        );
    }

    @Test
    public void testCompareArticleTitle() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
                "Cannot find button Skip",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find Search Wikipedia input",
                5);

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id=\"org.wikipedia:id/page_list_item_description\" and @text=\"Object-oriented programming language\"]"),
                "Cannot find Search Wikipedia input",
                5);


        WebElement title_element = waitForElementPresent(
                By.xpath("//android.widget.TextView[@text=\"Java (programming language)\"]"),
                "Cannot find article title",
                15
        );

        String article_title = title_element.getAttribute("text");
        Assert.assertEquals(
                "We see unexpected title",
                "Java (programming language)",
                article_title
        );


    }

    //  ДЗ Тест, который проверяет, что поле ввода для поиска статьи содержит текст (в разных версиях приложения это могут быть тексты "Search..." или "Search Wikipedia",
    //  правильным вариантом следует считать тот, которые есть сейчас)
    @Test
    public void testSearchFieldContainsExpectedText() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
                "Cannot find button Skip",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find Search Wikipedia input",
                5);

        WebElement searchField = waitForElementPresent(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find the search field",
                5
        );

        String actualText = searchField.getText();

        if (!actualText.equals("Search...") && !actualText.equals("Search Wikipedia")) {
            fail("Search field does not contain the expected text. Found: " + actualText);
        }
    }


    // ДЗ Написать тест, который ищет какое-то слово
    //Убеждается, что найдено несколько статей
    //Отменяет поиск
    //Убеждается, что результат поиска пропал
    @Test
    public void testCancelSearchAndVerifyResultsDisappeared() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
                "Cannot find button Skip",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find Search Wikipedia input",
                5
        );

        // Вводим ключевое слово для поиска
        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Python",
                "Cannot find search input",
                5
        );

        // Убеждаемся, что найдено как минимум два элемента
        waitForElementPresent(
                By.xpath("//android.widget.TextView[@resource-id=\"org.wikipedia:id/page_list_item_title\" and @text=\"Python\"]"),
                "Cannot find the first search result",
                5
        );

        waitForElementPresent(
                By.xpath("//android.widget.TextView[@resource-id=\"org.wikipedia:id/page_list_item_title\" and @text=\"Python (programming language)\"]"),
                "Cannot find the second search result",
                5
        );

        // Отменяем поиск
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search",
                5
        );

        // Убеждаемся, что результаты поиска исчезли
        waitForElementNotPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']"),
                "Search results are still visible on the page",
                5
        );
    }












    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));

    }

    private WebElement waitForElementPresent(By by, String error_message) {
        return waitForElementPresent(by, error_message, 5);
    }

    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }


    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));

    }

    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;

    }

    // ДЗ метод который проверяет наличие ожидаемого текста у элемента.
    private WebElement assertElementHasText(By by, String expectedText, String error_message) {
        WebElement element = waitForElementPresent(by, error_message);
        String actualText = element.getText();
        if (!actualText.equals(expectedText)) {
            throw new AssertionError(error_message + " Expected: '" + expectedText + "', but found: '" + actualText + "'");
        }
        return element;
    }

}
