package de.sg.computerinsel.tools.service;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import de.sg.computerinsel.tools.InventarPropertyUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@Slf4j
public abstract class BaseInventarSearchServiceImpl {

    protected final Properties settings;

    protected ChromeDriver driver;

    public BaseInventarSearchServiceImpl() {
        this.settings = InventarPropertyUtils.loadSettings();
        init();
        login();
        checkLogin();
    }

    protected abstract void init();

    protected abstract void login();

    protected void login(final By userSelector, final By passwordSelector, final By loginSlector, final String user,
            final String password) {
        final WebElement fieldUsername = driver.findElement(userSelector);
        final WebElement fieldPassword = driver.findElement(passwordSelector);

        fieldUsername.sendKeys(user);
        fieldPassword.sendKeys(password);

        driver.findElement(loginSlector).click();
    }

    protected abstract void checkLogin();

    protected void checkLogin(final By selector) {
        try {
            driver.findElement(selector);
            log.debug("Login erfolgreich");
        } catch (final Exception e) {
            log.error("Login nicht erfolgreich: {}, {}", e.getMessage(), e);
        }
    }

    protected void waitForPageToLoad() {
        try {
            Thread.sleep(1000L);
        } catch (final InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    protected void setChromeDriverPath() {
        System.setProperty("webdriver.chrome.driver", InventarPropertyUtils.getPathChromedriver(settings));
    }

}
