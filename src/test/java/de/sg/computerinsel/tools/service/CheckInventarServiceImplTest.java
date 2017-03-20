package de.sg.computerinsel.tools.service;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.sg.computerinsel.tools.Modus;
import de.sg.computerinsel.tools.model.Ware;

/**
 * @author Sita Geßner
 */
public class CheckInventarServiceImplTest {

    private CheckInventarServiceImpl service;

    private File directory;

    @Before
    public void setUp() throws URISyntaxException {
        directory = new File(getClass().getClassLoader().getResource("inventar").getFile());
        service = new CheckInventarServiceImpl(directory, Modus.PILOT);
    }

    @Test
    public void shouldCreateAktualisierteWaren() throws Exception {
        final Ware ware = new Ware();
        ware.setEanNummer(BaseInventarSearchServceImplTest.EAN);
        ware.setEinkaufspreis("15,00");
        ware.setVerkaufspreis("17,00");
        ware.setAnzahl("1");
        ware.setName("ware");
        ware.setDatei(new File(directory, ware.getName()));
        service.createAktualisierteWaren(Collections.singletonList(ware));
    }

}
