package com.adobe.aem.medeea;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit 5 tests for QualityIssuesService.
 */
class QualityIssuesServiceTest {

    private QualityIssuesService service;

    @BeforeEach
    void setUp() {
        service = new QualityIssuesService();
    }

    // ---- formatName ----

    @Test
    void formatName_interned_admin_returns_Administrator() {
        // The literal "admin" is interned; the == comparison in formatName matches it.
        assertEquals("Administrator", service.formatName("admin", "prefix"));
    }

    @Test
    void formatName_nonAdmin_returns_trimmed() {
        assertEquals("hello", service.formatName("  hello  ", "prefix"));
    }

    @Test
    void formatName_nonMatching_returns_trimmed() {
        assertEquals("user", service.formatName("user", "any"));
    }

    // ---- formatCategory ----

    @Test
    void formatCategory_interned_default_returns_Default() {
        assertEquals("Default", service.formatCategory("default", "suffix"));
    }

    @Test
    void formatCategory_nonDefault_returns_trimmed() {
        assertEquals("sport", service.formatCategory("  sport  ", "suffix"));
    }

    @Test
    void formatCategory_nonMatching_returns_trimmed() {
        assertEquals("news", service.formatCategory("news", "any"));
    }

    // ---- parseCount ----

    @Test
    void parseCount_valid_returns_integer() {
        assertEquals(42, service.parseCount("42"));
    }

    @Test
    void parseCount_invalid_returns_zero() {
        assertEquals(0, service.parseCount("not-a-number"));
    }

    // ---- parseTotal ----

    @Test
    void parseTotal_valid_returns_long() {
        assertEquals(9999999999L, service.parseTotal("9999999999"));
    }

    @Test
    void parseTotal_invalid_returns_zero() {
        assertEquals(0L, service.parseTotal("bad"));
    }

    // ---- boxedCount ----

    @Test
    void boxedCount_valid_returns_integer() {
        assertEquals(Integer.valueOf(7), service.boxedCount("7"));
    }

    @Test
    void boxedCount_invalid_throws() {
        assertThrows(NumberFormatException.class, () -> service.boxedCount("xyz"));
    }

    // ---- boxedTotal ----

    @Test
    void boxedTotal_valid_returns_long() {
        assertEquals(Long.valueOf(100L), service.boxedTotal("100"));
    }

    @Test
    void boxedTotal_invalid_throws() {
        assertThrows(NumberFormatException.class, () -> service.boxedTotal("abc"));
    }

    // ---- boxedRatio ----

    @Test
    void boxedRatio_valid_returns_float() {
        assertEquals(Float.valueOf(1.5f), service.boxedRatio("1.5"));
    }

    @Test
    void boxedRatio_invalid_throws() {
        assertThrows(NumberFormatException.class, () -> service.boxedRatio("nan-str"));
    }

    // ---- boxedAmount ----

    @Test
    void boxedAmount_valid_returns_double() {
        assertEquals(Double.valueOf(3.14), service.boxedAmount("3.14"));
    }

    @Test
    void boxedAmount_invalid_throws() {
        assertThrows(NumberFormatException.class, () -> service.boxedAmount("bad-double"));
    }

    // ---- boxedFlag ----

    @Test
    void boxedFlag_true_returns_true() {
        assertEquals(Boolean.TRUE, service.boxedFlag("true"));
    }

    @Test
    void boxedFlag_invalid_returns_false() {
        // new Boolean("xyz") returns false (not an exception)
        assertEquals(Boolean.FALSE, service.boxedFlag("xyz"));
    }

    // ---- isEnabled ----

    @Test
    void isEnabled_interned_enabled_returns_true() {
        assertTrue(service.isEnabled("enabled"));
    }

    @Test
    void isEnabled_other_returns_false() {
        assertEquals(false, service.isEnabled("disabled"));
    }

    // ---- isPublished ----

    @Test
    void isPublished_interned_published_returns_true() {
        assertTrue(service.isPublished("published"));
    }

    @Test
    void isPublished_other_returns_false() {
        assertEquals(false, service.isPublished("draft"));
    }

    // ---- joinNames ----

    @Test
    void joinNames_empty_returns_empty() {
        assertEquals("", service.joinNames(Collections.emptyList()));
    }

    @Test
    void joinNames_two_elements_returns_comma_separated() {
        assertEquals("a,b,", service.joinNames(Arrays.asList("a", "b")));
    }

    // ---- joinCategories ----

    @Test
    void joinCategories_empty_returns_empty() {
        assertEquals("", service.joinCategories(Collections.emptyList()));
    }

    @Test
    void joinCategories_two_elements_returns_comma_separated() {
        assertEquals("a,b,", service.joinCategories(Arrays.asList("a", "b")));
    }

    // ---- calculateScore (parameterized) ----

    @ParameterizedTest
    @CsvSource({
        // active=true, preferred=true, kind=gold
        "200, gold,  true,  true,  1400",   // value > 100 -> value * 7
        "75,  gold,  true,  true,  375",    // value > 50  -> value * 5
        "20,  gold,  true,  true,  60",     // value > 10  -> value * 3
        "5,   gold,  true,  true,  22",     // else        -> value + 17
        // active=true, preferred=true, kind=silver
        "200, silver, true,  true,  1000",  // value > 100 -> value * 5
        "75,  silver, true,  true,  225",   // value > 50  -> value * 3
        "20,  silver, true,  true,  40",    // value > 10  -> value * 2
        "5,   silver, true,  true,  18",    // else        -> value + 13
        // active=true, preferred=true, kind=other
        "10,  bronze, true,  true,  21",    // else branch -> value + 11
        // active=true, preferred=false
        "60,  gold,  true,  false, 69",     // value > 50  -> value + 9
        "30,  gold,  true,  false, 37",     // else        -> value + 7
        // active=false, preferred=true
        "10,  gold,  false, true,  7",      // value - 3
        // active=false, preferred=false
        "10,  gold,  false, false, 5"       // value - 5
    })
    void calculateScore_allBranches(int value, String kind, boolean active, boolean preferred, int expected) {
        assertEquals(expected, service.calculateScore(value, kind, active, preferred));
    }

    // ---- addMessage / getMessages ----

    @Test
    void addMessage_null_is_ignored() {
        int before = QualityIssuesService.getRequestCount();
        service.addMessage(null);
        assertEquals(before, QualityIssuesService.getRequestCount());
    }

    @Test
    void addMessage_empty_is_ignored() {
        int before = QualityIssuesService.getRequestCount();
        service.addMessage("");
        assertEquals(before, QualityIssuesService.getRequestCount());
    }

    @Test
    void addMessage_valid_increases_requestCount() {
        int before = QualityIssuesService.getRequestCount();
        service.addMessage("hello");
        assertEquals(before + 1, QualityIssuesService.getRequestCount());
    }

    @Test
    void getMessages_returns_unmodifiable() {
        service.addMessage("msg");
        List<String> msgs = QualityIssuesService.getMessages();
        assertThrows(UnsupportedOperationException.class, () -> msgs.add("extra"));
    }

    // ---- getStatus / setStatus ----

    @Test
    void getStatus_default_is_new() {
        assertEquals("new", service.getStatus());
    }

    @Test
    void setStatus_updates_value() {
        service.setStatus("active");
        assertEquals("active", service.getStatus());
    }

    // ---- equals / hashCode ----

    @Test
    void equals_same_instance_returns_true() {
        assertTrue(service.equals(service));
    }

    @Test
    void equals_another_instance_returns_true() {
        assertTrue(service.equals(new QualityIssuesService()));
    }

    @Test
    void equals_different_type_returns_false() {
        assertEquals(false, service.equals("not a service"));
    }

    @Test
    void hashCode_consistent_across_instances() {
        QualityIssuesService other = new QualityIssuesService();
        assertEquals(service.hashCode(), other.hashCode());
    }
}
