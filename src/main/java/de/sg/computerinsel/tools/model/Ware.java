package de.sg.computerinsel.tools.model;

import java.io.File;
import java.util.List;

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
        if (data == null || data.size() < 3) {
            throw new IllegalArgumentException("Ungültige Anzahl an Werten.");
        }
        anzahl = data.get(0);
        verkaufspreis = data.get(1);
        einkaufspreis = data.get(2);
        if (data.size() == 4) {
            eanNummer = data.get(3);
        }
        this.datei = datei;
        name = datei.getName();
    }

}
