package de.sg.computerinsel.tools.service;

import java.io.IOException;
import java.util.Properties;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import de.sg.computerinsel.tools.InventarPropertyUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@Slf4j
public class InventarSearchServiceImpl implements InventarSearchService {

    private final Properties settings;

    private WebClient webClient;

    public InventarSearchServiceImpl() {
        this.settings = InventarPropertyUtils.loadSettings();
        initWebClientAndLogin();
    }

    private void initWebClientAndLogin() {
        this.webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
        webClient.setJavaScriptEnabled(true);
        webClient.setCssEnabled(false);
        webClient.setRedirectEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        try {
            final HtmlPage page = webClient.getPage(InventarPropertyUtils.getUrl(settings));
            login(page);
            checkLogin(page);
        } catch (FailingHttpStatusCodeException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void login(final HtmlPage page) throws IOException {
        final HtmlElement fieldUsername = page.getElementByName("user_name");
        final HtmlElement fieldPassword = page.getElementByName("password");

        fieldUsername.setTextContent(InventarPropertyUtils.getUsername(settings));
        fieldPassword.setTextContent(InventarPropertyUtils.getPassword(settings) + "\n");
        page.getElementByName("login").click();
    }

    private void checkLogin(final HtmlPage page) {
        try {
            final HtmlElement fieldSearch = page.getElementByName("search");
            log.debug("Suchfeld gefunden.");
        } catch (final ElementNotFoundException e) {
            throw new IllegalStateException("Suchfeld konnte nicht gefunden werden. Möglicherweise war der Login nicht erfolgreich.", e);
        }
    }

    public Properties getSettings() {
        return settings;
    }

}
