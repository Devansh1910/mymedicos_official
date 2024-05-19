package com.medical.my_medicos.activities.news;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class News {
    private String label, thumbnail, description, url, date, type,subject;

    private String documentId; // Add this line
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public News(String documentId,String label, String thumbnail, String description, String date, String url,String type, String subject) {
        this.documentId = documentId; // Add this line
        this.label = label;
        this.thumbnail = thumbnail;
        this.description = description;
        this.date = date;
        this.subject = subject;
        this.url = url;
        this.type = type;
    }

    public String getDocumentId() {
        return documentId;
    }
    public String getLabel() {
        return label;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public String getSubject() {
        return subject;
    }


    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }


    public String getFormattedDate() {
        long timestamp = convertStringToTimestamp(date);

        // Get the current time in milliseconds
        long currentTime = System.currentTimeMillis();

        // Calculate the difference between the current time and the news timestamp
        long timeDifference = currentTime - timestamp;

        // Use DateUtils.getRelativeTimeSpanString to get a human-readable time difference
        return DateUtils.getRelativeTimeSpanString(
                timestamp,
                currentTime,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString();
    }

    // Helper method to convert the string date to a timestamp
    private long convertStringToTimestamp(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date = dateFormat.parse(dateString);

            return date != null ? date.getTime() : 0L;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
