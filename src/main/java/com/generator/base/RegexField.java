package com.generator.base;

/**
* @author houkaihong
* @Date 15:01 2019/10/22
* @description：TODO
*/
public class RegexField {
    private String name;
    private String key;

    public RegexField(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString() {
        return name;
    }
}
