package de.sg.computerinsel.tools.model;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

/**
 * @author Sita Geßner
 */
@Data
public class Ware {

    private String anzahl;

    private String name;

    private String verkaufspreis;

    private String einkaufspreis;

    private String eanNummer;

    private File datei;

    public Ware(final List<String> data, final File datei) {
        this(datei);
        if (data == null || data.size() < 3 || data.size() > 4) {
            throw new IllegalArgumentException("Ungültige Anzahl an Werten.");
        }
        anzahl = data.get(0);
        verkaufspreis = data.get(1);
        einkaufspreis = data.get(2);
        if (data.size() == 4) {
            eanNummer = StringUtils.trimToNull(data.get(3));
            if (StringUtils.isBlank(eanNummer)) {
                throw new IllegalArgumentException("Keine EAN-Nummer gefunden.");
            }
        }
    }

    public Ware(final File datei) {
        this.datei = datei;
        name = datei.getName();
    }

}
