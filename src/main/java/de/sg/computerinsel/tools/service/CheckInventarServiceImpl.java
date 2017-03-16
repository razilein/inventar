package de.sg.computerinsel.tools.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    }

    private Collection<File> listFiles() {
        return FileUtils.listFiles(directory, null, true);
    }

    private List<Ware> getWaren(final Collection<File> files) {
        final List<Ware> waren = new ArrayList<>();
        for (final File file : files) {
            try {
                waren.add(new Ware(FileUtils.readLines(file)));
            } catch (final IOException e) {
                log.info("Datei '{}' konnte nicht verarbeitet werden.", file.getAbsolutePath());
                log.debug(e.getMessage(), e);
            } catch (final IllegalArgumentException e) {
                log.info("Datei '{}' konnte nicht verarbeitet werden, da die Anzahl der in der Datei enthaltenen Werte ungültig ist.",
                        file.getAbsolutePath());
            }
        }
        return waren;
    }

    private List<Ware> getFehlendeWaren(final List<Ware> waren) {
        return null;
    }

}
