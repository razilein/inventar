package de.sg.computerinsel.tools.service;

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
public class HomepageInventarSearchServiceImpl extends BaseInventarSearchServiceImpl implements InventarSearchService {

    public HomepageInventarSearchServiceImpl() {
        super();
    }

    @Override
    protected void init() {
        setChromeDriverPath();
        driver = new ChromeDriver();
        driver.get(InventarPropertyUtils.getUrl(settings));
    }

    @Override
    protected void login() {
        login(By.name("user_name"), By.name("password"), By.name("login"), InventarPropertyUtils.getUsername(settings),
                InventarPropertyUtils.getPassword(settings));
    }

    @Override
    protected void checkLogin() {
        checkLogin(By.name("search"));
    }

    @Override
    public boolean existsWareByEan(final Ware ware) {
        driver.get(InventarPropertyUtils.getUrl(settings) + ware.getEanNummer());
        waitForPageToLoad();
        final List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'Ware konnte nicht gefunden werden!')]"));
        final boolean exists = list.isEmpty() ? checkSpalteErn(ware) : false;
        if (!exists) {
            log.info("{} existiert nicht in Inventar.", ware);
        }
        return exists;
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

}
