package com.example.my_medicos;
public class cmeitem4 {

    String docname,docpos,doctitle,docpresenter,date,time,email,type;
    int imageview;



    public cmeitem4(String docname, String docpos, String date, String doctitle, String docpresenter,int imageview,String time,String email,String type) {
        this.docname = docname;
        this.docpos = docpos;
//        this.imageview = imageview;
        this.doctitle=doctitle;
        this.email=email;
        this.type=type;
        this.date=date;
        this.time=time;
        this.docpresenter=docpresenter;
    }

    public String getDocname() {
        return docname;
    }
    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getType() {
        return type;
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
