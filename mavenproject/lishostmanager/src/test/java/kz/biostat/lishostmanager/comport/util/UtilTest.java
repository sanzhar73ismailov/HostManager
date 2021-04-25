package kz.biostat.lishostmanager.comport.util;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @org.junit.jupiter.api.Test
    void isInteger() {
        assertTrue(Util.isInteger("1"));
        assertTrue(Util.isInteger("123"));
        assertTrue(Util.isInteger("1231"));
        assertFalse(Util.isInteger("1231a"));
    }
}