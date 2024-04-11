package com.medical.my_medicos.activities.notifications.model;

import android.app.Notification;

public class PushNotification {
    private NotificationDataJobs data;
    private String to;

    public PushNotification(NotificationDataJobs data, String to){
        this.data = data;
        this.to = to;
    }

    public NotificationDataJobs getData() {
        return data;
    }

    public void setData(NotificationDataJobs dataJobs) {
        this.data = dataJobs;
    }

    public String getTo() {
        return to;
    }
}
