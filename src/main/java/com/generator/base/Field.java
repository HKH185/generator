package com.generator.base;

/**
* @author houkaihong
* @Date 15:01 2019/10/22
* @description：TODO
*/
public class Field {
    //列明user_id
    private String columnName;
    //列注释：用户id
    private String columnComment;
    //列的数据类型：bigint
    private String xmlJDBCType;
    //java数据类型：long
    private String xmlJavaType;
    //java数据类型：long
    private String javaJavaType;
    //java属性名：userId
    private String javaField;
    private String regex;
    private int datasize;
    private boolean nullable;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getXmlJDBCType() {
        return xmlJDBCType;
    }

    public void setXmlJDBCType(String xmlJDBCType) {
        this.xmlJDBCType = xmlJDBCType;
    }

    public String getXmlJavaType() {
        return xmlJavaType;
    }

    public void setXmlJavaType(String xmlJavaType) {
        this.xmlJavaType = xmlJavaType;
    }

    public String getJavaJavaType() {
        return javaJavaType;
    }

    public void setJavaJavaType(String javaJavaType) {
        this.javaJavaType = javaJavaType;
    }

    public String getJavaField() {
        return javaField;
    }

    public void setJavaField(String javaField) {
        this.javaField = javaField;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public int getDatasize() {
        return datasize;
    }

    public void setDatasize(int datasize) {
        this.datasize = datasize;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
