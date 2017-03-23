package de.sg.computerinsel.tools.service;

import java.math.BigDecimal;

/**
 * @author Sita Geßner
 */
public interface PilotInventarSearchService extends InventarSearchService {

    BigDecimal getEinkaufspreis();

}
