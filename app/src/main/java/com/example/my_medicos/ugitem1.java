package com.example.my_medicos;

public class ugitem1 {

    String docname,doctitle,docdesciption;
    int imageview;



    public ugitem1(String docname, String docpos, int imageview, String doctitle, String docpresenter) {
        this.docname = docname;
//        this.imageview = imageview;
        this.doctitle=doctitle;
    }
    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getDocdescripiton() {
        return docdesciption;
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

    public void setImage(int imageview) {
        this.imageview = imageview;
    }
}
