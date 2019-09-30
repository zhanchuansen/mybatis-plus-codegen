package com.suncompass.basic.codegen.entity.vo;

public class TemplateEntity {

    private String name;
    private String path;

    public TemplateEntity(String name, String path) {
        this.setName(name);
        this.setPath(path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
