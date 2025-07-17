package org.spring.metaquery.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SqlDialect {
    TRINO("trino"),
    SPARK("spark");

    private final String value;

    SqlDialect(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
