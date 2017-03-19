package de.sg.computerinsel.tools.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import de.sg.computerinsel.tools.model.Ware;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@Slf4j
@Getter
public class CheckInventarServiceImpl implements CheckInventarService {

    private final InventarSearchService inventarSearchService;

    private final File directory;

    public CheckInventarServiceImpl(final File directory) {
        this.directory = directory;
        this.inventarSearchService = new InventarSearchServiceImpl();
    }

    @Override
    public void start() {
        final Collection<File> files = listFiles();
        final List<Ware> waren = getWaren(files);
        final List<Ware> fehlendeWaren = getFehlendeWaren(waren);
        if (log.isDebugEnabled()) {
            logFehlendeWaren(fehlendeWaren);
        }
        copyFehlendeWaren(fehlendeWaren);
    }

    private Collection<File> listFiles() {
        return FileUtils.listFiles(directory, null, true);
    }

    private List<Ware> getWaren(final Collection<File> files) {
        final List<Ware> waren = new ArrayList<>();
        for (final File file : files) {
            try {
                final Ware ware = new Ware(FileUtils.readLines(file), file);
                waren.add(ware);
                log.info("Ware gefunden: {}", ware);
            } catch (final IOException e) {
                log.info("Datei '{}' konnte nicht verarbeitet werden.", file.getAbsolutePath());
                log.debug(e.getMessage(), e);
                copyFehlendeWare(file);
            } catch (final IllegalArgumentException e) {
                log.info("Datei '{}' konnte nicht verarbeitet werden, da die Anzahl der in der Datei enthaltenen Werte ungültig ist.",
                        file.getAbsolutePath());
                copyFehlendeWare(file);
            }
        }
        return waren;
    }

    private List<Ware> getFehlendeWaren(final List<Ware> waren) {
        return waren.stream().filter(w -> !inventarSearchService.existsWareByEan(w)).collect(Collectors.toList());
    }

    private void copyFehlendeWaren(final List<Ware> fehlendeWaren) {
        for (final Ware ware : fehlendeWaren) {
            copyFehlendeWare(ware);
        }
    }

    private void copyFehlendeWare(final Ware ware) {
        try {
            FileUtils.copyFile(ware.getDatei(), new File(new File(directory, "FehlendeWaren"), ware.getDatei().getName()));
        } catch (final IOException e) {
            log.error("Fehler beim Schreiben der Datei {} : {} {}", ware.getDatei().getAbsolutePath(), e.getMessage(), e);
        }
    }

    private void copyFehlendeWare(final File datei) {
        copyFehlendeWare(new Ware(datei));
    }

    private void logFehlendeWaren(final List<Ware> fehlendeWaren) {
        log.debug("Folgende Waren konnten nicht gefunden werden:");
        for (final Ware ware : fehlendeWaren) {
            log.debug(ware.toString());
        }
    }

}
