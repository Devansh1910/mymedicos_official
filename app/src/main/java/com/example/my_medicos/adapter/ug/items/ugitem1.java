package com.example.my_medicos.adapter.ug.items;

public class ugitem1 {

    String docname,doctitle,docdesciption,date,docpos;
    int imageview;



    public ugitem1(String docname, String docpos, int imageview, String doctitle, String docdesciption,String date) {
        this.docname = docname;
//        this.imageview = imageview;
        this.doctitle=doctitle;
        this.date=date;
        this.docpos=docpos;
        this.docdesciption=docdesciption;
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
    public String getdate() {
        return date;
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
