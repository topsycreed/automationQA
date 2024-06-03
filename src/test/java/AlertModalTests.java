import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.confirm;
import static com.codeborne.selenide.Selenide.dismiss;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.prompt;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AlertModalTests {
    @Test
    void simpleAlertHandlingTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        // Триггерим появление alert
        $(By.id("my-alert")).click();
        // Ожидаем появления alert
        WebDriverWait wait = new  WebDriverWait(getWebDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());
        //Переключаемся на alert и взаимодействуем с ним
        Alert alert = switchTo().alert();
        assertEquals("Hello world!", alert.getText());
        //закрываем окно с подтверждением
        alert.accept();
    }

    @Test
    void simpleAlertSelenideHandlingTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        // Триггерим появление alert
        $(By.id("my-alert")).click();
        // Ожидаем появления alert
        WebDriverWait wait = new  WebDriverWait(getWebDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());
        //Подтверждаем без переключения через метод Selenide
        confirm();
        wait.until(alertIsNotPresent());
    }

    private static ExpectedCondition<Boolean> alertIsNotPresent() {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    driver.switchTo().alert();
                    return false;
                } catch (NoAlertPresentException e) {
                    return true;
                }
            }

            @Override
            public String toString() {
                return "alert should be not present";
            }
        };
    }

    @Test
    void confirmAlertHandlingTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        // Триггерим появление alert
        $(By.id("my-confirm")).click();
        // Ожидаем появления alert
        WebDriverWait wait = new  WebDriverWait(getWebDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());
        //Переключаемся на alert и взаимодействуем с ним
        Alert alert = switchTo().alert();
        assertEquals("Is this correct?", alert.getText());
        //закрываем окно с подтверждением
        confirm();
        SelenideElement confirmText = $(By.id("confirm-text"));
        assertEquals("You chose: true", confirmText.getText());
    }

    @Test
    void dismissAlertHandlingTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        // Триггерим появление alert
        $(By.id("my-confirm")).click();
        // Ожидаем появления alert
        WebDriverWait wait = new  WebDriverWait(getWebDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());
        //Переключаемся на alert и взаимодействуем с ним
        Alert alert = switchTo().alert();
        assertEquals("Is this correct?", alert.getText());
        //закрываем окно с отклонением
        dismiss();
        SelenideElement confirmText = $(By.id("confirm-text"));
        assertEquals("You chose: false", confirmText.getText());
    }

    @Test
    void promptAlertHandlingTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        // Триггерим появление alert
        $(By.id("my-prompt")).click();
        // Ожидаем появления alert
        WebDriverWait wait = new  WebDriverWait(getWebDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());
        //Переключаемся на alert и взаимодействуем с ним
        Alert alert = switchTo().alert();
        assertEquals("Please enter your name", alert.getText());
        //вводим текст и закрываем окно с подтверждением
        String text = "Text";
        alert.sendKeys(text);
        alert.accept();
        SelenideElement promptText = $(By.id("prompt-text"));
        assertEquals("You typed: " + text, promptText.getText());
    }

    @Test
    void promptSelenideAlertHandlingTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        // Триггерим появление alert
        $(By.id("my-prompt")).click();
        // Ожидаем появления alert
        WebDriverWait wait = new  WebDriverWait(getWebDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());
        //Переключаемся на alert и взаимодействуем с ним
        Alert alert = switchTo().alert();
        assertEquals("Please enter your name", alert.getText());
        //вводим текст и закрываем окно с подтверждением
        String text = "Text";
        prompt(text);
        SelenideElement promptText = $(By.id("prompt-text"));
        assertEquals("You typed: " + text, promptText.getText());
    }

    @Test
    void confirmModalWindowTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        // Триггерим появление модального окна
        $(By.id("my-modal")).click();
        // Проверяем что диалоговое окно открылось
        SelenideElement modalDialog = $(By.className("modal-dialog"));
        modalDialog.shouldBe(visible);
        // Работаем с элементами внутри модального окна
        SelenideElement save = $(By.xpath("//button[normalize-space() = 'Save changes']"));
        SelenideElement modalStatus = $(By.id("example-modal"));
        modalStatus.shouldHave(attribute("role", "dialog"));
        SelenideElement modalTitle = $(By.id("exampleModalLabel"));
        modalTitle.shouldHave(text("Modal title"));
        sleep(1000);//https://selenide.org/2019/12/20/advent-calendar-big-wait-theory/
        save.click();
        // проверяем текст после подтвержения
        modalDialog.shouldNotBe(visible);
        SelenideElement modalText = $(By.id("modal-text"));
        assertEquals("You chose: Save changes", modalText.getText());
    }

    @Test
    void dismissModalWindowTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        // Триггерим появление модального окна
        $(By.id("my-modal")).click();
        // Проверяем что диалоговое окно открылось
        SelenideElement modalDialog = $(By.className("modal-dialog"));
        modalDialog.shouldBe(visible);
        // Работаем с элементами внутри модального окна
        SelenideElement close = $(By.xpath("//button[text() = 'Close']"));
        sleep(1000);
        close.click();
        // проверяем текст после закрытия
        modalDialog.shouldNotBe(visible);
        SelenideElement modalText = $(By.id("modal-text"));
        assertEquals("You chose: Close", modalText.getText());
    }
}
