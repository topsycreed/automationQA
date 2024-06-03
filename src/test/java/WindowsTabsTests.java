import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WindowType;

import java.util.Set;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WindowsTabsTests {
    private final static String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    @Test
    void newTabTest() {
        open(BASE_URL);
        assertEquals(BASE_URL, url());
        // Открываем новую вкладку и переключаемся на нее
        switchTo().newWindow(WindowType.TAB);
        // Работа с новой вкладкой
        open(BASE_URL + "web-form.html");
        assertEquals(BASE_URL + "web-form.html", url());
        Set<String> tabs = getWebDriver().getWindowHandles();
        // Проверяем что сейчас 2 вкладки
        assertEquals(2, tabs.size());
        // Закрываем новую вкладку
        closeWindow();
        // Переключаемся на первую вкладку
//        switchTo().window(0);
        assertEquals(BASE_URL, url());
    }

    @Test
    void switchTabsTest() {
        open(BASE_URL);
        assertEquals(BASE_URL, url());
        // Открываем новую вкладку и переключаемся на нее
        switchTo().newWindow(WindowType.TAB);
        // Работа с новой вкладкой
        open(BASE_URL + "web-form.html");
        assertEquals(BASE_URL + "web-form.html", url());
        Set<String> tabs = getWebDriver().getWindowHandles();
        // Проверяем что сейчас 2 вкладки
        assertEquals(2, tabs.size());
        // Переключаемся на первую вкладку
        switchTo().window(tabs.stream().toList().get(0));
        assertEquals(BASE_URL, url());
        // Переключаемся на вторую вкладку
        switchTo().window(tabs.stream().toList().get(1));
        assertEquals(BASE_URL + "web-form.html", url());
    }

    @Test
    void newWindowTest() {
        open(BASE_URL);
        // сохраняем идентификатор текущего окна как базовое
        String baseWindow = getWebDriver().getWindowHandle();
        assertEquals(BASE_URL, url());
        // Открываем новок окно и переключаемся на него
        switchTo().newWindow(WindowType.WINDOW);
        // Работа с новым окном
        open(BASE_URL + "web-form.html");
        assertEquals(BASE_URL + "web-form.html", url());
        // Проверяем что сейчас 2 окна
        assertEquals(2, getWebDriver().getWindowHandles().size());
        // Закрываем новое окно
        closeWindow();
        // Переключаемся на первое окно
        switchTo().window(baseWindow);
        assertEquals(BASE_URL, url());
        // Проверяем что сейчас 1 окно
        assertEquals(1, getWebDriver().getWindowHandles().size());
    }

    @Test
    void closeAllWindowsTest() {
        open(BASE_URL);
        assertEquals(BASE_URL, url());
        // Открываем новок окно и переключаемся на него
        switchTo().newWindow(WindowType.WINDOW);
        // Работа с новым окном
        open(BASE_URL + "web-form.html");
        assertEquals(BASE_URL + "web-form.html", url());
        Set<String> windowHandles = getWebDriver().getWindowHandles();
        // Проверяем что сейчас 2 окна
        assertEquals(2, windowHandles.size());
        // Закрываем все окна
        closeWebDriver();
        assertThrows(IllegalStateException.class, WebDriverRunner::getWebDriver);
    }

    @Test
    void switchWindowsTest() {
        open(BASE_URL);
        assertEquals(BASE_URL, url());
        // Открываем новок окно и переключаемся на него
        switchTo().newWindow(WindowType.WINDOW);
        // Работа с новым окном
        open(BASE_URL + "web-form.html");
        assertEquals(BASE_URL + "web-form.html", url());
        Set<String> windowHandles = getWebDriver().getWindowHandles();
        // Проверяем что сейчас 2 окна
        assertEquals(2, windowHandles.size());
        // Переключаемся на первое окно
        switchTo().window(windowHandles.stream().toList().get(0));
        assertEquals(BASE_URL, url());
        // Переключаемся на второе окно
        switchTo().window(windowHandles.stream().toList().get(1));
        assertEquals(BASE_URL + "web-form.html", url());
        // Проверяем что сейчас все еще 2 окна
        assertEquals(2, getWebDriver().getWindowHandles().size());
    }
}
