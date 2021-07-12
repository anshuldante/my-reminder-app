package com.ava.myreminderapp.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum RecurrenceType {
    YEAR("Year(s)"),
    MONTH("Months(s)"),
    DAY("Day(s)"),
    HOUR("Hour(s)"),
    MINUTE("Minute(s)");

    private static final Map<String, RecurrenceType> ENUM_MAP =
            new HashMap<>(Arrays.stream(RecurrenceType.values())
                    .collect(Collectors.toMap(RecurrenceType::getValue, rt -> rt)));

    private final String value;

    RecurrenceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RecurrenceType getRecurrenceTypeByValue(String value) {
        return ENUM_MAP.get(value);
    }
}