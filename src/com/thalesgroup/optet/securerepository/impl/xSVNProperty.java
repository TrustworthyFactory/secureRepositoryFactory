package com.thalesgroup.optet.securerepository.impl;

/**
 * User: detonator
 * Date: Dec 6, 2010
 * Time: 3:54:20 PM
 */
public class xSVNProperty {
    private String name, value;

    public xSVNProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("xSVNProperty: %s = %s", name, value);
    }
}
