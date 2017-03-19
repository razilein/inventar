package de.sg.computerinsel.tools;

import java.io.File;

import de.sg.computerinsel.tools.service.CheckInventarService;
import de.sg.computerinsel.tools.service.CheckInventarServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@Slf4j
public class Start {

    private static CheckInventarService checkInventar;

    public static void main(final String[] args) {
        if (args == null || args.length == 0) {
            log.error("Es wurde kein Pfad angegeben.");
        } else {
            final File dir = new File(args[0]);
            if (!dir.exists() || dir.isFile()) {
                log.error("Das angegebene Verzeichnis ist ungültig.");
            } else {
                checkInventar = new CheckInventarServiceImpl(dir);
                checkInventar.start();
            }
        }
    }

}
