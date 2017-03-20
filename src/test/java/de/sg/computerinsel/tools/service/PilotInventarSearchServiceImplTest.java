package de.sg.computerinsel.tools.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.sg.computerinsel.tools.model.Ware;

/**
 * @author Sita Geßner
 */
public class PilotInventarSearchServiceImplTest extends BaseInventarSearchServceImplTest {

    @Before
    public void setUp() {
        super.setUp(new PilotInventarSearchServiceImpl());
    }

    @Test
    public void shouldGetVerkaufspreis() {
        final Ware ware = createWare(EAN);
        service.existsWareByEan(ware);
        assertNotNull(((PilotInventarSearchServiceImpl) service).getEinkaufspreis());
    }

}
