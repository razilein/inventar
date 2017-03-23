package de.sg.computerinsel.tools.service;

import org.junit.Before;

/**
 * @author Sita Geßner
 */
public class HomepageInventarSearchServiceImplTest extends BaseInventarSearchServceImplTest {

    @Before
    public void setUp() {
        super.setUp(new HomepageInventarSearchServiceImpl());
    }

}
