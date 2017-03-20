package de.sg.computerinsel.tools.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import de.sg.computerinsel.tools.Modus;
import de.sg.computerinsel.tools.model.Ware;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@Slf4j
@Getter
public class CheckInventarServiceImpl implements CheckInventarService {

    private static final DecimalFormat DF = new DecimalFormat("#,##0.00");

    private final InventarSearchService inventarSearchService;

    private final File directory;

    private final Modus modus;

    public CheckInventarServiceImpl(final File directory, final Modus modus) {
        this.directory = directory;
        this.modus = modus;
        if (modus == Modus.HOMEPAGE) {
            this.inventarSearchService = new HomepageInventarSearchServiceImpl();
        } else if (modus == Modus.PILOT) {
            this.inventarSearchService = new PilotInventarSearchServiceImpl();
        } else {
            throw new UnsupportedOperationException("Modus " + modus + "nicht implementiert.");
        }
    }

    @Override
    public void start() {
        if (modus == Modus.HOMEPAGE) {
            homepage();
        } else if (modus == Modus.PILOT) {
            pilot();
        }
    }

    private void homepage() {
        final Collection<File> files = listFiles();
        final List<Ware> waren = getWaren(files, true);
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
        return getWaren(files, false);
    }

    private List<Ware> getWaren(final Collection<File> files, final boolean copyOnException) {
        final List<Ware> waren = new ArrayList<>();
        for (final File file : files) {
            try {
                final Ware ware = new Ware(FileUtils.readLines(file), file);
                waren.add(ware);
                log.info("Ware gefunden: {}", ware);
            } catch (final IOException e) {
                log.info("Datei '{}' konnte nicht verarbeitet werden.", file.getAbsolutePath());
                log.debug(e.getMessage(), e);
                if (copyOnException) {
                    copyFehlendeWare(file);
                }
            } catch (final IllegalArgumentException e) {
                log.info("Datei '{}' konnte nicht verarbeitet werden, da die Anzahl der in der Datei enthaltenen Werte ungültig ist.",
                        file.getAbsolutePath());
                if (copyOnException) {
                    copyFehlendeWare(file);
                }
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

    private void logFehlendeWaren(final List<Ware> waren) {
        logWaren(waren, "Folgende Waren konnten nicht gefunden werden:");
    }

    private void pilot() {
        final Collection<File> files = listFiles();
        final List<Ware> waren = getWaren(files);
        final List<Ware> aktualisierteWaren = getAktualisierteWaren(waren);
        logAktualisierteWaren(aktualisierteWaren);
        createAktualisierteWaren(aktualisierteWaren);
    }

    private List<Ware> getAktualisierteWaren(final List<Ware> waren) {
        final List<Ware> aktualisierteWaren = new ArrayList<>();
        waren.forEach(w -> {
            try {
                if (inventarSearchService.existsWareByEan(w)) {
                    final BigDecimal einkaufspreis = ((PilotInventarSearchServiceImpl) inventarSearchService).getEinkaufspreis();
                    w.setEinkaufspreis(DF.format(einkaufspreis));
                    w.setVerkaufspreis(berechneVerkaufspreis(einkaufspreis));
                    aktualisierteWaren.add(w);
                }
            } catch (final Exception e) {
                log.error("Fehler beim Verarbeiten von: {}", w);
            }
        });
        return aktualisierteWaren;
    }

    private String berechneVerkaufspreis(final BigDecimal einkaufspreis) {
        return DF.format(einkaufspreis.multiply(new BigDecimal("1.19").multiply(new BigDecimal("1.1"))));
    }

    private void logAktualisierteWaren(final List<Ware> waren) {
        logWaren(waren, "Folgende Waren müssen wurden gefunden und aktualisiert:");
    }

    void createAktualisierteWaren(final List<Ware> waren) {
        final File aktualisierteWarenVerzeichnis = new File(directory, "AktualisierteWaren");
        aktualisierteWarenVerzeichnis.mkdir();
        for (final Ware ware : waren) {
            final File file = new File(aktualisierteWarenVerzeichnis, ware.getName());
            try {
                FileUtils.writeLines(file, getLines(ware));
            } catch (final IOException e) {
                log.error("Fehler beim Schreiben der Datei {} : {} {}", ware.getDatei().getAbsolutePath(), e.getMessage(), e);
            }
        }
    }

    private Collection<?> getLines(final Ware ware) {
        return Arrays.asList(ware.getAnzahl(), ware.getVerkaufspreis(), ware.getEinkaufspreis(), ware.getEanNummer());
    }

    private void logWaren(final List<Ware> waren, final String logText) {
        log.debug(logText);
        for (final Ware ware : waren) {
            log.debug(ware.toString());
        }
    }

}
