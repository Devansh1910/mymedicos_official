package com.example.my_medicos;

    public class cmeitem1 {

        String docname,docpos,rate;
        int image;

    public cmeitem1() {
    }

    public cmeitem1(String docname, String docpos, int image,String rate) {
        this.docname = docname;
        this.docpos = docpos;
        this.image = image;
        this.rate = rate;
    }

    public String getDocname() {
        return docname;
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
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
    public String getrate() {
            return rate;
        }
}
