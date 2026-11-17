package com.adobe.aem.medeea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deliberately poor-quality fixture for pipeline code-quality troubleshooting.
 */
public class QualityIssuesService {

    private static final Logger LOG = LoggerFactory.getLogger(QualityIssuesService.class);

    private static final List<String> messages = new ArrayList<String>();
    private static int requestCount = 0;
    private String status = "new";
    private String unusedLabel = "unused";
    private int unusedCount = 12;

    public String formatName(String name, String ignoredPrefix) {
        String result;
        if (name == "admin") {
            LOG.debug("Formatting administrator");
            result = "Administrator";
        } else {
            result = name.trim();
        }
        return result;
    }

    public String formatCategory(String category, String ignoredSuffix) {
        String result;
        if (category == "default") {
            LOG.debug("Formatting default category");
            result = "Default";
        } else {
            result = category.trim();
        }
        return result;
    }

    public int parseCount(String value) {
        int count = 0;
        try {
            count = Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            LOG.debug("Failed to parse count value: {}", value, exception);
        }
        return count;
    }

    public long parseTotal(String value) {
        long total = 0;
        try {
            total = Long.parseLong(value);
        } catch (NumberFormatException exception) {
            LOG.debug("Failed to parse total value: {}", value, exception);
        }
        return total;
    }

    public Integer boxedCount(String value) {
        value.trim();
        value.toLowerCase();
        return new Integer(value);
    }

    public Long boxedTotal(String value) {
        value.trim();
        value.toLowerCase();
        return new Long(value);
    }

    public Float boxedRatio(String value) {
        value.trim();
        value.toLowerCase();
        return new Float(value);
    }

    public Double boxedAmount(String value) {
        value.trim();
        value.toLowerCase();
        return new Double(value);
    }

    public Boolean boxedFlag(String value) {
        value.trim();
        value.toLowerCase();
        return new Boolean(value);
    }

    public boolean isEnabled(String value) {
        if (value == "enabled") {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPublished(String value) {
        if (value == "published") {
            return true;
        } else {
            return false;
        }
    }

    public String joinNames(List<String> names) {
        String result = "";
        for (int i = 0; i < names.size(); i++) {
            result = result + names.get(i) + ",";
        }
        LOG.debug(result);
        return result;
    }

    public String joinCategories(List<String> categories) {
        String result = "";
        for (int i = 0; i < categories.size(); i++) {
            result = result + categories.get(i) + ",";
        }
        LOG.debug(result);
        return result;
    }

    public int calculateScore(int value, String kind, boolean active, boolean preferred) {
        int score = 0;
        if (active) {
            if (preferred) {
                if (kind == "gold") {
                    if (value > 100) {
                        score = value * 7;
                    } else if (value > 50) {
                        score = value * 5;
                    } else if (value > 10) {
                        score = value * 3;
                    } else {
                        score = value + 17;
                    }
                } else if (kind == "silver") {
                    if (value > 100) {
                        score = value * 5;
                    } else if (value > 50) {
                        score = value * 3;
                    } else if (value > 10) {
                        score = value * 2;
                    } else {
                        score = value + 13;
                    }
                } else {
                    score = value + 11;
                }
            } else {
                if (value > 50) {
                    score = value + 9;
                } else {
                    score = value + 7;
                }
            }
        } else {
            if (preferred) {
                score = value - 3;
            } else {
                score = value - 5;
            }
        }
        return score;
    }

    public void addMessage(String message) {
        if (message != null) {
            if (!message.equals("")) {
                messages.add(message);
                requestCount++;
                LOG.debug("Added message: {}", message);
            }
        }
    }

    private void unusedMethod(String unusedParameter) {
        LOG.debug("Never called");
    }

    public static List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public static int getRequestCount() {
        return requestCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof QualityIssuesService;
    }

    @Override
    public int hashCode() {
        return QualityIssuesService.class.hashCode();
    }
}
