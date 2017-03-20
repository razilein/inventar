package de.sg.computerinsel.tools.service;

import java.math.BigDecimal;
import java.util.List;

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
public class PilotInventarSearchServiceImpl extends BaseInventarSearchServiceImpl implements PilotInventarSearchService {

    public PilotInventarSearchServiceImpl() {
        super();
    }

    @Override
    protected void init() {
        setChromeDriverPath();
        driver = new ChromeDriver();
        driver.get(InventarPropertyUtils.getPilotUrl(settings));
    }

    @Override
    protected void login() {
        login(By.id("txtClientNumber"), By.id("txtPassword"), By.cssSelector("input[value='Login']"),
                InventarPropertyUtils.getPilotUsername(settings), InventarPropertyUtils.getPilotPassword(settings));
    }

    @Override
    protected void checkLogin() {
        checkLogin(By.id("searchfield"));
    }

    @Override
    public boolean existsWareByEan(final Ware ware) {
        driver.get(InventarPropertyUtils.getPilotUrl(settings) + ware.getEanNummer());
        waitForPageToLoad();
        final List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'Keine Artikel gefunden.')]"));
        if (!list.isEmpty()) {
            log.info("{} existiert nicht in Inventar.", ware);
        }
        return list.isEmpty();
    }

    @Override
    public BigDecimal getEinkaufspreis() {
        return normalizePreis(driver.findElement(By.cssSelector("#prodlist div.preis")).getText());
    }

    public static BigDecimal normalizePreis(final String preis) {
        return new BigDecimal(StringUtils.substring(preis, 1).trim());
    }

}
