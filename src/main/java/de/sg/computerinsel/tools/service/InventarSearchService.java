package de.sg.computerinsel.tools.service;

import de.sg.computerinsel.tools.model.Ware;

/**
 * @author Sita Geßner
 */
public interface InventarSearchService {

    boolean existsWareByEan(Ware ware);

}
