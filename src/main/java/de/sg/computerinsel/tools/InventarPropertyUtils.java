package de.sg.computerinsel.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@UtilityClass
@Slf4j
public class InventarPropertyUtils {

    private static final String FOLDER_PROPERTIES = "settings";

    private static final String FILENAME_PROPERTIES = "inventar.properties";

    public static final String PROP_URL = "url";

    public static final String PROP_USER = "user";

    public static final String PROP_PW = "password";

    private static final String PROP_PATH_CHROMEDRIVER = "path.chromedriver";

    public static Properties loadSettings() {
        Properties props = null;
        try (final FileReader reader = new FileReader(new File(FilenameUtils.concat(FOLDER_PROPERTIES, FILENAME_PROPERTIES)))) {
            props = new Properties();
            props.load(reader);
        } catch (final FileNotFoundException e) {
            log.error("Datei: {} konnte nicht gefunden werden.", FILENAME_PROPERTIES, e.getMessage());
        } catch (final IOException e) {
            log.error("Fehler beim Schreiben der Datei: {}.", FILENAME_PROPERTIES, e.getMessage());
        }
        return props;
    }

    public static String getUsername(final Properties properties) {
        return properties.getProperty(PROP_USER);
    }

    public static String getPassword(final Properties properties) {
        return properties.getProperty(PROP_PW);
    }

    public static String getUrl(final Properties properties) {
        return properties.getProperty(PROP_URL);
    }

    public static String getPathChromedriver(final Properties properties) {
        return properties.getProperty(PROP_PATH_CHROMEDRIVER);
    }
}
