package com.medical.my_medicos.activities.notifications.model;

public class NotificationDataJobs {

    public String title,Organiser,documentid;

    public NotificationDataJobs (String title, String organiser, String documentid) {

        this.title = title;
        this.Organiser = organiser;
    }

    //.....Title.........

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //.....Organiser......

    public String getOrganiser() {
        return Organiser;
    }

    public void setOrganiser(String organiser) {
        Organiser = organiser;
    }

    //.....Document ID.....

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }
}
