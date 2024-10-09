import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Duration;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void testSwipeArticle() {
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
                "Appium",
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id=\"org.wikipedia:id/page_list_item_title\" and @text=\"Appium\"]"),
                "Cannot find  Appium article in search",
                5);

////android.widget.TextView[@resource-id="org.wikipedia:id/page_list_item_title" and @text="Appium"]
        waitForElementPresent(
                By.xpath("(//android.widget.TextView[@text=\"Appium\"])[1]"),
                "Cannot find article title",
                15
        );

//        swipeUpToFindElement(
//                By.xpath("//*[@text='View article in browser']"),
//                "Cannot find the end of the article title",
//                20
//        );


    }

    @Test
    public void saveFirstArticleToMyList() {
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


        waitForElementPresent(
                By.xpath("//android.widget.TextView[@text=\"Java (programming language)\"]"),
                "Cannot find article title",
                25
        );


        waitForElementAndClick(
                By.id("org.wikipedia:id/page_save"),
                "Cannot find button to save article options",
                10
        );

        waitForElementAndClick(
                By.xpath("//android.widget.Button[@resource-id=\"org.wikipedia:id/snackbar_action\"]"),
                "Cannot find button 'Add to list'",
                5
        );


        String name_of_folder = "Learning programming";
        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/text_input"),
                name_of_folder,
                "Cannot find text into article folder input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Cannot find button to save article options",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@text='View list']"),
                "Cannot find button to show View list",
                5
        );
        swipeElementToLeft(
                By.xpath("//*[@text = 'Java (programming language)']"),
                "Cannot find save article"
        );

        waitForElementPresent(
                By.xpath("//*[@text = 'Java (programming language)']"),
                "Cannot delete save article",
                5
        );


    }

    @Test
    public void testAmountOfNotEmptySearch() {

        waitForElementAndClick(
                By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
                "Cannot find button Skip",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find Search Wikipedia input",
                5);


        String search_line = "Linkin Park Diskography";
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                search_line,
                "Cannot find search input",
                5
        );

        //String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_title']";
        String search_result_locator = "//androidx.recyclerview.widget.RecyclerView[@resource-id=\"org.wikipedia:id/search_results_list\"]/android.view.ViewGroup";
        waitForElementPresent(
                By.xpath(search_result_locator),
                "Cannot find anything by the request " + search_line,
                15
        );

        int amount_of_search_results = getAmountOfElements(
                By.xpath(search_result_locator)
        );

        Assert.assertTrue(
                "We found too few results!",
                amount_of_search_results > 0
        );

    }


    @Test
    public void testAmountOfEmptySearch() {

        waitForElementAndClick(
                By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
                "Cannot find button Skip",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find Search Wikipedia input",
                5);


        String search_line = "fghjghkjhkrgghgrh";
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                search_line,
                "Cannot find search input",
                5
        );

        String search_result_locator = "org.wikipedia:id/search_results_list";
        String empty_result_label = "//*[@text = 'No results']";

        waitForElementPresent(
                By.xpath(empty_result_label),
                "Cannon find empty result label by the request " + search_line,
                15
        );

        assertElementNotPresent(
                By.xpath(search_result_locator),
                "We've found some results by request " + search_line
        );





    }

    @Test
    public void testChangesScreenOrientationOnSearchResult(){

        waitForElementAndClick(
                By.id("org.wikipedia:id/fragment_onboarding_skip_button"),
                "Cannot find button Skip",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find Search Wikipedia input",
                5);

        String search_line = "Java";
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                search_line,
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id=\"org.wikipedia:id/page_list_item_description\" and @text=\"Object-oriented programming language\"]"),
                "Cannot find 'Object-oriented programming language' topic searching by " + search_line,
                15);

        String title_before_rotation = waitForElementAndGetAttribute(
                By.xpath("//android.widget.TextView[@text=\"Java (programming language)\"]"),
                "text",
                "Cannon find title of article",
                15

        );

        driver.rotate(ScreenOrientation.LANDSCAPE);

        String title_after_rotation = waitForElementAndGetAttribute(
                By.xpath("//android.widget.TextView[@text=\"Java (programming language)\"]"),
                "text",
                "Cannon find title of article",
                15

        );

        Assert.assertEquals(
                "Article title have been changed after screen rotations ",
                title_before_rotation,
                title_after_rotation
        );

        driver.rotate(ScreenOrientation.LANDSCAPE);

        String title_after_second_rotation = waitForElementAndGetAttribute(
                By.xpath("//android.widget.TextView[@text=\"Java (programming language)\"]"),
                "text",
                "Cannon find title of article",
                15

        );

        Assert.assertEquals(
                "Article title have been changed after screen rotations ",
                title_before_rotation,
                title_after_second_rotation
        );


    }

    @Test
    public void testSearchArticleBackground(){

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
                "Cannot find Search Wikipedia input",
                5
        );



        driver.runAppInBackground(java.time.Duration.ofSeconds(5));

        waitForElementPresent(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find article after returning from background",
                10
        );


    }
    @Test
    public void testOnboardingSwipe(){

        swipeElementToLeft(
                By.xpath("//android.widget.ScrollView[@resource-id=\"org.wikipedia:id/scrollView\"]"),
                "Cannot swipe to second onboarding screen"
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

//    protected void swipeUp(int timeOfSwipe) {
//        TouchAction action = new TouchAction(driver);
//        Dimension size = driver.manage().window().getSize();
//        int x = size.width / 2;
//        int startY = (int) (size.width * 0.8);
//        int endY = (int) (size.width * 0.2);
//
//        action
//                .press(x, startY)
//                .waitAction(timeOfSwipe)
//                .moveTo(x, endY)
//                .release()
//                .perform();
//
//    }

    public void swipeDown(int timeOfScroll)
    {
        // Получаем размеры экрана устройства.
        Dimension size = driver.manage().window().getSize();

        // Определяем начальную координату по оси Y (примерно 70% от высоты экрана).
        int startY = (int) (size.height * 0.70);

        // Определяем конечную координату по оси Y (примерно 30% от высоты экрана).
        int endY = (int) (size.height * 0.30);

        // Определяем центральную координату по оси X (половина ширины экрана).
        int centerX = size.width / 2;

        // Создаем объект, представляющий палец для выполнения свайпа.
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        // Создаем последовательность действий для выполнения свайпа.
        Sequence swipe = new Sequence(finger, 1)

                // Двигаем палец на начальную позицию (центр по X, startY по Y).
                .addAction(finger.createPointerMove(Duration.ofSeconds(0),
                        PointerInput.Origin.viewport(), centerX, startY))

                // Палец прикасается к экрану.
                .addAction(finger.createPointerDown(0))

                // Палец двигается к конечной точке (центр по X, endY по Y).
                .addAction(finger.createPointerMove(Duration.ofMillis(timeOfScroll),
                        PointerInput.Origin.viewport(), centerX, endY))

                // Убираем палец с экрана.
                .addAction(finger.createPointerUp(0));

        // Выполняем последовательность действий (свайп вниз).
        driver.perform(Arrays.asList(swipe));
    }


    protected void swipeUpQuick() {
        swipeUp(200);
    }

//    protected void swipeUpToFindElement(By by, String error_message, int max_swipes) {
//        int already_swiped = 0;
//        while (driver.findElements(by).size() == 0) {
//            if (already_swiped > max_swipes) {
//                waitForElementPresent(by, "Cannot find element by swiping up. \n" + error_message, 0);
//                return;
//            }
//            swipeUpQuick();
//            ++already_swiped;
//        }
//
//    }
//
//    protected void swipeElementToLeft(By by, String error_message) {
//        WebElement element = waitForElementPresent(
//                by,
//                error_message,
//                10);
//
//        int left_x = element.getLocation().getX();
//        int right_x = left_x + element.getSize().getWidth();
//        int upper_y = element.getLocation().getY();
//        int lower_y = upper_y + element.getSize().getWidth();
//        int middle_y = (upper_y + lower_y) / 2;
//
//        TouchAction action = new TouchAction(driver);
//        action
//                .press(right_x, middle_y)
//                .waitAction(300)
//                .moveTo(left_x, middle_y)
//                .release()
//                .perform();
//
//    }

    public void swipeElementToLeft(By locator_with_type, String error_message) {

        // Находим элемент на экране, ожидая его появления в течение 10 секунд.
        WebElement element = waitForElementPresent(locator_with_type, error_message, 10);

        // Получаем координаты элемента на экране.
        Point location = element.getLocation();
        // Получаем размеры элемента (ширину и высоту).
        Dimension size = element.getSize();

        // Координата по оси X левой границы элемента.
        int left_x = location.getX();
        // Координата по оси X правой границы элемента.
        int right_x = left_x + size.getWidth();
        // Координата по оси Y верхней границы элемента.
        int upper_y = location.getY();
        // Координата по оси Y нижней границы элемента.
        int lower_y = upper_y + size.getHeight();
        // Координата по оси Y средней линии элемента.
        int middle_y = upper_y + (size.getHeight() / 2);

        // Начальная координата по оси X для свайпа (чуть левее правого края элемента).
        int start_x = right_x - 20;
        // Конечная координата по оси X для свайпа (чуть правее левого края элемента).
        int end_x = left_x + 20;
        // Начальная координата по оси Y для свайпа (по центру элемента).
        int start_y = middle_y;
        // Конечная координата по оси Y для свайпа (также по центру элемента).
        int end_y = middle_y;

        // Выполняем свайп с начальной точки до конечной с заданной продолжительностью.
        this.swipe(
                new Point(start_x, start_y),
                new Point(end_x, end_y),
                Duration.ofMillis(550)  // Устанавливаем продолжительность свайпа 550 миллисекунд.
        );
    }

    protected void swipe(Point start, Point end, Duration duration) {

        // Создаем объект, представляющий палец для выполнения свайпа.
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        // Создаем последовательность действий для выполнения свайпа.
        Sequence swipe = new Sequence(finger, 1);

        // Добавляем действие для перемещения пальца к начальной точке.
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start.x, start.y));
        // Добавляем действие для нажатия на экран в начальной точке.
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        // Добавляем действие для перемещения пальца из начальной точки в конечную в течение заданного времени.
        swipe.addAction(finger.createPointerMove(duration, PointerInput.Origin.viewport(), end.x, end.y));
        // Добавляем действие для отпускания пальца от экрана в конечной точке.
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Выполняем последовательность действий (свайп).
        this.driver.perform(Arrays.asList(swipe));
    }










    private int getAmountOfElements(By by) {
        List elements = driver.findElements(by);
        return elements.size();


    }

    private void assertElementNotPresent(By by, String error_message) {
        int amount_of_elements = getAmountOfElements(by);
        if (amount_of_elements > 0) {
            String default_message = "An element '" + by.toString() + "' supposed to be not present";
            throw new AssertionError(default_message + " " + error_message);
        }

    }

    private String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds ){
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);

    }

    protected void backgroundApp(int seconds)
    {

        driver.runAppInBackground(Duration.ofSeconds(seconds));
    }

}
