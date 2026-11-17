package com.adobe.aem.medeea;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for QualityIssuesService.
 */
public class QualityIssuesServiceTest {

    private QualityIssuesService service;

    @Before
    public void setUp() {
        service = new QualityIssuesService();
    }

    // ---- formatName --------------------------------------------------------

    @Test
    public void formatName_regularName_returnsTrimmed() {
        assertEquals("alice", service.formatName("  alice  ", "Mr."));
    }

    @Test
    public void formatName_emptyName_returnsEmpty() {
        assertEquals("", service.formatName("", ""));
    }

    // ---- formatCategory ----------------------------------------------------

    @Test
    public void formatCategory_regularCategory_returnsTrimmed() {
        assertEquals("books", service.formatCategory("  books  ", "cat"));
    }

    @Test
    public void formatCategory_emptyCategory_returnsEmpty() {
        assertEquals("", service.formatCategory("", ""));
    }

    // ---- parseCount --------------------------------------------------------

    @Test
    public void parseCount_validInteger_returnsParsedValue() {
        assertEquals(42, service.parseCount("42"));
    }

    @Test
    public void parseCount_negativeInteger_returnsParsedValue() {
        assertEquals(-7, service.parseCount("-7"));
    }

    @Test
    public void parseCount_invalidString_returnsZero() {
        assertEquals(0, service.parseCount("not-a-number"));
    }

    @Test
    public void parseCount_emptyString_returnsZero() {
        assertEquals(0, service.parseCount(""));
    }

    // ---- parseTotal --------------------------------------------------------

    @Test
    public void parseTotal_validLong_returnsParsedValue() {
        assertEquals(100L, service.parseTotal("100"));
    }

    @Test
    public void parseTotal_largeLong_returnsParsedValue() {
        assertEquals(9876543210L, service.parseTotal("9876543210"));
    }

    @Test
    public void parseTotal_invalidString_returnsZero() {
        assertEquals(0L, service.parseTotal("xyz"));
    }

    // ---- isEnabled ---------------------------------------------------------

    @Test
    public void isEnabled_otherValue_returnsFalse() {
        assertFalse(service.isEnabled("disabled"));
    }

    @Test
    public void isEnabled_nullValue_returnsFalse() {
        // null != "enabled" in reference comparison, so returns false without NPE
        assertFalse(service.isEnabled(null));
    }

    // ---- calculateScore ----------------------------------------------------

    @Test
    public void calculateScore_activePreferredGoldHighValue_returnsValueTimes7() {
        // value > 100, kind "gold", active=true, preferred=true
        // kind is compared by reference; passing the literal interned string
        assertEquals(700, service.calculateScore(100 + 1, "gold", true, true));
    }

    @Test
    public void calculateScore_activePreferredSilverMidValue_returnsValueTimes3() {
        // 10 < value <= 50, kind "silver"
        assertEquals(30 * 2, service.calculateScore(30, "silver", true, true));
    }

    @Test
    public void calculateScore_activePreferredOtherKind_returnsValuePlus11() {
        assertEquals(10 + 11, service.calculateScore(10, "bronze", true, true));
    }

    @Test
    public void calculateScore_activeNotPreferredHighValue_returnsValuePlus9() {
        assertEquals(60 + 9, service.calculateScore(60, "gold", true, false));
    }

    @Test
    public void calculateScore_activeNotPreferredLowValue_returnsValuePlus7() {
        assertEquals(10 + 7, service.calculateScore(10, "gold", true, false));
    }

    @Test
    public void calculateScore_notActivePreferred_returnsValueMinus3() {
        assertEquals(20 - 3, service.calculateScore(20, "gold", false, true));
    }

    @Test
    public void calculateScore_notActiveNotPreferred_returnsValueMinus5() {
        assertEquals(20 - 5, service.calculateScore(20, "gold", false, false));
    }

    // ---- addMessage / getMessages / getRequestCount ------------------------

    @Test
    public void addMessage_nonEmptyMessage_isStoredAndCountIncremented() {
        int beforeCount = QualityIssuesService.getRequestCount();
        int beforeSize = QualityIssuesService.getMessages().size();
        service.addMessage("hello");
        assertEquals(beforeCount + 1, QualityIssuesService.getRequestCount());
        assertEquals(beforeSize + 1, QualityIssuesService.getMessages().size());
        assertTrue(QualityIssuesService.getMessages().contains("hello"));
    }

    @Test
    public void addMessage_emptyMessage_isIgnored() {
        int beforeCount = QualityIssuesService.getRequestCount();
        int beforeSize = QualityIssuesService.getMessages().size();
        service.addMessage("");
        assertEquals(beforeCount, QualityIssuesService.getRequestCount());
        assertEquals(beforeSize, QualityIssuesService.getMessages().size());
    }

    @Test
    public void addMessage_nullMessage_isIgnored() {
        int beforeCount = QualityIssuesService.getRequestCount();
        service.addMessage(null);
        assertEquals(beforeCount, QualityIssuesService.getRequestCount());
    }

    @Test
    public void getMessages_returnsUnmodifiableList() {
        List<String> msgs = QualityIssuesService.getMessages();
        try {
            msgs.add("should fail");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    // ---- joinNames ---------------------------------------------------------

    @Test
    public void joinNames_multipleNames_returnsCsvWithTrailingComma() {
        List<String> names = Arrays.asList("Alice", "Bob", "Carol");
        assertEquals("Alice,Bob,Carol,", service.joinNames(names));
    }

    @Test
    public void joinNames_emptyList_returnsEmptyString() {
        assertEquals("", service.joinNames(Collections.emptyList()));
    }

    // ---- getStatus / setStatus ---------------------------------------------

    @Test
    public void getStatus_defaultValue_isNew() {
        assertEquals("new", service.getStatus());
    }

    @Test
    public void setStatus_updatesValue() {
        service.setStatus("active");
        assertEquals("active", service.getStatus());
    }
}
