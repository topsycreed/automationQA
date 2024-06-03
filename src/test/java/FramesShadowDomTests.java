import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selectors.shadowCss;
import static com.codeborne.selenide.Selectors.shadowDeepCss;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.switchTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FramesShadowDomTests {
    @Test
    void iframeTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/iframes.html");
        SelenideElement iframeElement = $(By.id("my-iframe"));
        SelenideElement insideIframeElement = $(By.className("lead"));
        assertThrows(ElementNotFound.class, insideIframeElement::click);
        //переключение на iframe по локатору или номеру
        switchTo().frame(iframeElement);
        assertThrows(ElementNotFound.class, iframeElement::click);
        assertTrue(insideIframeElement.getText().contains("Lorem ipsum dolor sit amet"));
        switchTo().defaultContent();
        assertTrue(iframeElement.getAttribute("src").contains("content.html"));
    }

    @Test
    void frameTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/frames.html");
        SelenideElement frameElement = $(By.name("frame-body"));
        SelenideElement insideFrameElement = $(By.className("lead"));
        assertThrows(ElementNotFound.class, insideFrameElement::click);
        switchTo().frame(frameElement);
        assertThrows(ElementNotFound.class, frameElement::click);
        assertTrue(insideFrameElement.getText().contains("Lorem ipsum dolor sit amet"));
        switchTo().defaultContent();
        assertTrue(frameElement.getAttribute("src").contains("content.html"));
    }

    @Test
    void shadowDomTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/shadow-dom.html");
        SelenideElement shadowDomElement = $(By.id("content"));
        SelenideElement insideShadowDomElement = $(By.cssSelector("p"));
        SearchContext shadowRoot = shadowDomElement.getShadowRoot();
        WebElement insideShadowDomElementFromRoot = shadowRoot.findElement(By.cssSelector("p"));
        assertThrows(ElementNotFound.class, insideShadowDomElement::click);
        assertEquals("Hello Shadow DOM", insideShadowDomElementFromRoot.getText());
    }

    @Test
    void shadowDomSelenideTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/shadow-dom.html");
        SelenideElement insideShadowDomElement = $(shadowCss("p", "#content"));
        assertEquals("Hello Shadow DOM", insideShadowDomElement.getText());
    }

    @Test
    void shadowDomSelenideDeepSearchTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/shadow-dom.html");
        SelenideElement insideShadowDomElement = $(shadowDeepCss("p"));
        assertEquals("Hello Shadow DOM", insideShadowDomElement.getText());
    }
}
