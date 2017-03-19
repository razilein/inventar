package de.sg.computerinsel.tools.service;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import de.sg.computerinsel.tools.InventarPropertyUtils;
import de.sg.computerinsel.tools.model.Ware;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@Slf4j
public class InventarSearchServiceImpl implements InventarSearchService {

    private final Properties settings;

    private ChromeDriver driver;

    public InventarSearchServiceImpl() {
        this.settings = InventarPropertyUtils.loadSettings();
        init();
        login();
        checkLogin();
    }

    private void init() {
        setChromeDriverPath();
        driver = new ChromeDriver();
        driver.get(InventarPropertyUtils.getUrl(settings));
    }

    private void login() {
        final WebElement fieldUsername = driver.findElement(By.name("user_name"));
        final WebElement fieldPassword = driver.findElement(By.name("password"));

        fieldUsername.sendKeys(InventarPropertyUtils.getUsername(settings));
        fieldPassword.sendKeys(InventarPropertyUtils.getPassword(settings));

        driver.findElement(By.name("login")).click();
    }

    private void checkLogin() {
        try {
            driver.findElement(By.name("search"));
            log.debug("Login erfolgreich");
        } catch (final Exception e) {
            log.error("Login nicht erfolgreich: {}, {}", e.getMessage(), e);
        }
    }

    @Override
    public boolean existsWareByEan(final Ware ware) {
        driver.get(InventarPropertyUtils.getUrl(settings) + "&search=" + ware.getEanNummer());
        waitForPageToLoad();
        final List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'Ware konnte nicht gefunden werden!')]"));
        return list.isEmpty() ? checkSpalteErn(ware) : false;
    }

    private void waitForPageToLoad() {
        try {
            Thread.sleep(3000L);
        } catch (final InterruptedException e) {
            log.error(e.getMessage(), e);
        }

    }

    private boolean checkSpalteErn(final Ware ware) {
        boolean containsCompleteErn = false;
        for (final WebElement tr : driver.findElements(By.cssSelector("tr"))) {
            final List<WebElement> tds = tr.findElements(By.cssSelector("td"));
            if (tds.size() != 10) {
                continue;
            }
            if (StringUtils.equalsIgnoreCase(tds.get(1).getText().trim(), ware.getEanNummer().trim())) {
                containsCompleteErn = true;
                break;
            }
        }
        return containsCompleteErn;
    }

    private void setChromeDriverPath() {
        System.setProperty("webdriver.chrome.driver", InventarPropertyUtils.getPathChromedriver(settings));
    }

    public Properties getSettings() {
        return settings;
    }

}
