package com.desenvolvigames.applications.witalk.fcm.services;

/**
 * Created by Joao on 15/06/2017.
 */

public class NotificationData {

    private String imageName;
    private String id;
    private String title;
    private String textMessage;
    private String sound;

    public NotificationData(String imageName, String id, String title, String textMessage, String sound) {
        this.imageName = imageName;
        this.id = id;
        this.title = title;
        this.textMessage = textMessage;
        this.sound = sound;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getImageName() {
        return imageName;
    }

    public String getSound() {
        return sound;
    }


}