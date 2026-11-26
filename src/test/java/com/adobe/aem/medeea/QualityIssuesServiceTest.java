package com.adobe.aem.medeea;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link QualityIssuesService}.
 */
public class QualityIssuesServiceTest {

    private QualityIssuesService service;

    @Before
    public void setUp() {
        service = new QualityIssuesService();
    }

    // ---- formatName ----

    @Test
    public void formatName_regularName_returnsTrimmed() {
        assertEquals("alice", service.formatName("  alice  ", ""));
    }

    @Test
    public void formatName_emptyPrefix_doesNotAffectResult() {
        assertEquals("bob", service.formatName("bob", "ignored"));
    }

    // ---- formatCategory ----

    @Test
    public void formatCategory_regularCategory_returnsTrimmed() {
        assertEquals("news", service.formatCategory("  news  ", ""));
    }

    @Test
    public void formatCategory_emptyCategory_returnsEmpty() {
        assertEquals("", service.formatCategory("   ", "sfx"));
    }

    // ---- parseCount ----

    @Test
    public void parseCount_validInteger_returnsValue() {
        assertEquals(42, service.parseCount("42"));
    }

    @Test
    public void parseCount_negativeInteger_returnsValue() {
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

    // ---- parseTotal ----

    @Test
    public void parseTotal_validLong_returnsValue() {
        assertEquals(999999999L, service.parseTotal("999999999"));
    }

    @Test
    public void parseTotal_invalidString_returnsZero() {
        assertEquals(0L, service.parseTotal("abc"));
    }

    // ---- isEnabled ----

    @Test
    public void isEnabled_enabledLiteral_returnsTrue() {
        // Note: implementation uses == (reference equality); this test covers the
        // behaviour as written — only interned literals match.
        assertTrue(service.isEnabled("enabled"));
    }

    @Test
    public void isEnabled_otherValue_returnsFalse() {
        assertFalse(service.isEnabled("disabled"));
    }

    @Test
    public void isEnabled_null_returnsFalse() {
        assertFalse(service.isEnabled(null));
    }

    // ---- isPublished ----

    @Test
    public void isPublished_publishedLiteral_returnsTrue() {
        assertTrue(service.isPublished("published"));
    }

    @Test
    public void isPublished_otherValue_returnsFalse() {
        assertFalse(service.isPublished("draft"));
    }

    // ---- joinNames ----

    @Test
    public void joinNames_twoNames_returnsCsvWithTrailingComma() {
        List<String> names = Arrays.asList("alice", "bob");
        assertEquals("alice,bob,", service.joinNames(names));
    }

    @Test
    public void joinNames_emptyList_returnsEmptyString() {
        assertEquals("", service.joinNames(Collections.<String>emptyList()));
    }

    @Test
    public void joinNames_singleName_returnsNameWithComma() {
        assertEquals("only,", service.joinNames(Collections.singletonList("only")));
    }

    // ---- calculateScore ----

    @Test
    public void calculateScore_activePreferredGoldHigh_returnsValueTimes7() {
        assertEquals(707, service.calculateScore(101, "gold", true, true));
        assertEquals(770, service.calculateScore(110, "gold", true, true));
    }

    @Test
    public void calculateScore_activePreferredGoldMid_returnsValueTimes5() {
        assertEquals(300, service.calculateScore(60, "gold", true, true));
    }

    @Test
    public void calculateScore_activePreferredGoldLow_returnsValueTimes3() {
        assertEquals(45, service.calculateScore(15, "gold", true, true));
    }

    @Test
    public void calculateScore_activePreferredGoldVeryLow_returnsValuePlus17() {
        assertEquals(27, service.calculateScore(10, "gold", true, true));
    }

    @Test
    public void calculateScore_activePreferredSilverHigh_returnsValueTimes5() {
        assertEquals(600, service.calculateScore(120, "silver", true, true));
    }

    @Test
    public void calculateScore_activePreferredSilverMid_returnsValueTimes3() {
        assertEquals(180, service.calculateScore(60, "silver", true, true));
    }

    @Test
    public void calculateScore_activePreferredSilverLow_returnsValueTimes2() {
        assertEquals(30, service.calculateScore(15, "silver", true, true));
    }

    @Test
    public void calculateScore_activePreferredSilverVeryLow_returnsValuePlus13() {
        assertEquals(23, service.calculateScore(10, "silver", true, true));
    }

    @Test
    public void calculateScore_activePreferredOther_returnsValuePlus11() {
        assertEquals(26, service.calculateScore(15, "bronze", true, true));
    }

    @Test
    public void calculateScore_activeNotPreferredHigh_returnsValuePlus9() {
        assertEquals(60, service.calculateScore(51, "gold", true, false));
    }

    @Test
    public void calculateScore_activeNotPreferredLow_returnsValuePlus7() {
        assertEquals(17, service.calculateScore(10, "gold", true, false));
    }

    @Test
    public void calculateScore_notActivePreferred_returnsValueMinus3() {
        assertEquals(7, service.calculateScore(10, "gold", false, true));
    }

    @Test
    public void calculateScore_notActiveNotPreferred_returnsValueMinus5() {
        assertEquals(5, service.calculateScore(10, "gold", false, false));
    }

    // ---- addMessage ----

    @Test
    public void addMessage_validMessage_doesNotThrow() {
        // Verifies the method completes without exception for a non-empty message.
        service.addMessage("hello");
    }

    @Test
    public void addMessage_nullMessage_doesNotThrow() {
        service.addMessage(null);
    }

    @Test
    public void addMessage_emptyMessage_doesNotThrow() {
        service.addMessage("");
    }

    // ---- getStatus / setStatus ----

    @Test
    public void getStatus_defaultValue_returnsNew() {
        assertEquals("new", service.getStatus());
    }

    @Test
    public void setStatus_updatesValue() {
        service.setStatus("active");
        assertEquals("active", service.getStatus());
    }
}
