package com.example.my_medicos.adapter.ug.items;

public class ugitem1 {

    String docname,doctitle,docdesciption,date,docpos,pdf;
    int imageview;



    public ugitem1(String docname, String docpos, String pdf, String doctitle, String docdesciption,String date) {
        this.docname = docname;
//        this.imageview = imageview;
        this.doctitle=doctitle;
        this.date=date;
        this.docpos=docpos;
        this.pdf=pdf;
        this.docdesciption=docdesciption;
    }
    public String getDocname() {
        return docname;
    }
    public String getPdf() {
        return pdf;
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
