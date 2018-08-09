package com.example.demo.model;

/**
 * @author qiang220316
 */
public class UserGrade {
    private String id;

    private String name;

    private String description;

    private String code;

    private Integer type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public UserGrade() {
    }

    public UserGrade(String id, String name, String description, String code, Integer type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.code = code;
        this.type = type;
    }
}