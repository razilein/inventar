package de.sg.computerinsel.tools.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.sg.computerinsel.tools.model.Ware;

/**
 * @author Sita Geßner
 */
public abstract class BaseInventarSearchServceImplTest {

    protected static final String EAN = "649528754592";

    protected static final String EAN_NOT_EXISTS = "sadasdafgfdhht";

    protected InventarSearchService service;

    public void setUp(final InventarSearchService service) {
        this.service = service;
    }

    @Test
    public void shouldExistsWareByEan() {
        final Ware ware = createWare(EAN);
        assertTrue(service.existsWareByEan(ware));
    }

    @Test
    public void shouldNotExistsWareByEan() {
        final Ware ware = createWare(EAN_NOT_EXISTS);
        assertFalse(service.existsWareByEan(ware));
    }

    protected Ware createWare(final String ean) {
        final Ware ware = new Ware();
        ware.setEanNummer(ean);
        return ware;
    }

}
