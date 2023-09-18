package com.example.my_medicos;

public class cmeitem1 {

    String docname,docpos,doctitle,docpresenter,email,date,time,venue;
    int imageview;

    public cmeitem1() {
    }

    public cmeitem1(String docname, String docpos, int imageview,String doctitle,String docpresenter) {
        this.docname = docname;
        this.docpos = docpos;
        this.imageview = imageview;
        this.doctitle=doctitle;
        this.docpresenter=docpresenter;

    }

    public String getDocname() {
        return docname;
    }
    public String getvenue() {
        return venue;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getEmail() {
        return email;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getDocpos() {
        return docpos;
    }

    public void setDocpos(String docpos) {
        this.docpos = docpos;
    }

    public int getImage() {
        return imageview;
    }

    public String getDoctitle() {
        return doctitle;
    }

    public void setDoctitle(String doctitle) {
        this.doctitle = doctitle;
    }

    public String getDocpresenter() {
        return docpresenter;
    }

    public void setDocpresenter(String docpresenter) {
        this.docpresenter = docpresenter;
    }


    public void setImage(int imageview) {
        this.imageview = imageview;
    }
}
