package com.adobe.aem.medeea;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QualityIssuesServiceTest {

    private QualityIssuesService service;

    @BeforeEach
    void setUp() {
        service = new QualityIssuesService();
    }

    // formatName
    @Test
    void formatName_adminLiteral_returnsAdministrator() {
        // Uses == comparison as per existing behavior; literal "admin" is interned
        assertEquals("Administrator", service.formatName("admin", "prefix"));
    }

    @Test
    void formatName_otherName_returnsTrimmed() {
        assertEquals("alice", service.formatName("  alice  ", "prefix"));
    }

    // formatCategory
    @Test
    void formatCategory_defaultLiteral_returnsDefault() {
        assertEquals("Default", service.formatCategory("default", "suffix"));
    }

    @Test
    void formatCategory_otherCategory_returnsTrimmed() {
        assertEquals("books", service.formatCategory("  books  ", "suffix"));
    }

    // parseCount
    @Test
    void parseCount_validInteger_returnsValue() {
        assertEquals(42, service.parseCount("42"));
    }

    @Test
    void parseCount_invalidString_returnsZero() {
        assertEquals(0, service.parseCount("not-a-number"));
    }

    @Test
    void parseCount_negativeValue_returnsNegative() {
        assertEquals(-5, service.parseCount("-5"));
    }

    // parseTotal
    @Test
    void parseTotal_validLong_returnsValue() {
        assertEquals(100L, service.parseTotal("100"));
    }

    @Test
    void parseTotal_invalidString_returnsZero() {
        assertEquals(0L, service.parseTotal("xyz"));
    }

    // isEnabled
    @Test
    void isEnabled_enabledLiteral_returnsTrue() {
        assertTrue(service.isEnabled("enabled"));
    }

    @Test
    void isEnabled_otherString_returnsFalse() {
        assertFalse(service.isEnabled("disabled"));
    }

    // isPublished
    @Test
    void isPublished_publishedLiteral_returnsTrue() {
        assertTrue(service.isPublished("published"));
    }

    @Test
    void isPublished_otherString_returnsFalse() {
        assertFalse(service.isPublished("draft"));
    }

    // joinNames
    @Test
    void joinNames_multipleNames_returnsConcatenated() {
        List<String> names = Arrays.asList("alice", "bob");
        assertEquals("alice,bob,", service.joinNames(names));
    }

    @Test
    void joinNames_emptyList_returnsEmpty() {
        assertEquals("", service.joinNames(Collections.emptyList()));
    }

    // joinCategories
    @Test
    void joinCategories_multipleCategories_returnsConcatenated() {
        List<String> categories = Arrays.asList("news", "sports");
        assertEquals("news,sports,", service.joinCategories(categories));
    }

    @Test
    void joinCategories_emptyList_returnsEmpty() {
        assertEquals("", service.joinCategories(Collections.emptyList()));
    }

    // calculateScore — all branches
    @Test
    void calculateScore_activePreferredGoldOver100() {
        assertEquals(700, service.calculateScore(100, "gold", true, true));
    }

    @Test
    void calculateScore_activePreferredGoldOver50() {
        assertEquals(300, service.calculateScore(60, "gold", true, true));
    }

    @Test
    void calculateScore_activePreferredGoldOver10() {
        assertEquals(33, service.calculateScore(11, "gold", true, true));
    }

    @Test
    void calculateScore_activePreferredGoldElse() {
        assertEquals(27, service.calculateScore(10, "gold", true, true));
    }

    @Test
    void calculateScore_activePreferredSilverOver100() {
        assertEquals(500, service.calculateScore(100, "silver", true, true));
    }

    @Test
    void calculateScore_activePreferredSilverOver50() {
        assertEquals(180, service.calculateScore(60, "silver", true, true));
    }

    @Test
    void calculateScore_activePreferredSilverOver10() {
        assertEquals(22, service.calculateScore(11, "silver", true, true));
    }

    @Test
    void calculateScore_activePreferredSilverElse() {
        assertEquals(23, service.calculateScore(10, "silver", true, true));
    }

    @Test
    void calculateScore_activePreferredOther() {
        assertEquals(21, service.calculateScore(10, "bronze", true, true));
    }

    @Test
    void calculateScore_activeNotPreferredOver50() {
        assertEquals(69, service.calculateScore(60, "gold", true, false));
    }

    @Test
    void calculateScore_activeNotPreferredElse() {
        assertEquals(17, service.calculateScore(10, "gold", true, false));
    }

    @Test
    void calculateScore_inactivePreferred() {
        assertEquals(7, service.calculateScore(10, "gold", false, true));
    }

    @Test
    void calculateScore_inactiveNotPreferred() {
        assertEquals(5, service.calculateScore(10, "gold", false, false));
    }

    // addMessage
    @Test
    void addMessage_validMessage_appearsInList() {
        int before = QualityIssuesService.getRequestCount();
        service.addMessage("hello");
        List<String> msgs = QualityIssuesService.getMessages();
        assertTrue(msgs.contains("hello"));
        assertEquals(before + 1, QualityIssuesService.getRequestCount());
    }

    @Test
    void addMessage_nullMessage_doesNotAdd() {
        int before = QualityIssuesService.getRequestCount();
        service.addMessage(null);
        assertEquals(before, QualityIssuesService.getRequestCount());
    }

    @Test
    void addMessage_emptyMessage_doesNotAdd() {
        int before = QualityIssuesService.getRequestCount();
        service.addMessage("");
        assertEquals(before, QualityIssuesService.getRequestCount());
    }

    // equals
    @Test
    void equals_sameType_returnsTrue() {
        assertTrue(service.equals(new QualityIssuesService()));
    }

    @Test
    void equals_differentType_returnsFalse() {
        assertFalse(service.equals("not a service"));
    }

    @Test
    void equals_null_returnsFalse() {
        assertFalse(service.equals(null));
    }
}
