package com.medical.my_medicos.activities.fmge.model;

public class MaterialFMGE {
    private String label, description, url, date;
    public MaterialFMGE(String name, String status,String url,String date) {

        this.label = name;
        this.description = status;
        this.url = url;
        this.date = date;
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String name) {
        this.label = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String status) {
        this.description = status;
    }

    public String getUrl() {
        if (url != null && url.length() > 10) {
            return url.substring(0, 10);
        } else {
            return url;
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

