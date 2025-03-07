package com.lvmc.constant;


public enum MetricType {
    COUNTER("counter"),
    GAUGE("gauge");
    private String value;

    MetricType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public static MetricType valueOfName(String value) {
        switch (value) {
            case "counter":
                return COUNTER;
            case "gauge":
                return GAUGE;
            default:
                return null;
        }
    }
}
