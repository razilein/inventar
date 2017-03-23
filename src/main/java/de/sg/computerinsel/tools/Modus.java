package de.sg.computerinsel.tools;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Sita Geßner
 */
@Getter
@AllArgsConstructor
public enum Modus {

    HOMEPAGE("H", "Prüfung Homepage Computer-Insel"), PILOT("P", "Prüfung Händlerseite Pilot");

    private final String code;

    private final String beschreibung;

    public static Modus getModusByCode(final String code) {
        return Stream.of(Modus.values()).filter(m -> StringUtils.equals(m.getCode(), code)).findFirst().orElse(HOMEPAGE);
    }

    @Override
    public String toString() {
        return beschreibung;
    }

}
