package com.rakibofc.androidtoolbox;

public class SystemInfo {

    public String fieldName;
    public String fieldData;

    public SystemInfo(String fieldName, String fieldData) {
        this.fieldName = fieldName;
        this.fieldData = fieldData;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldData() {
        return fieldData;
    }

    public void setFieldData(String fieldData) {
        this.fieldData = fieldData;
    }
}
