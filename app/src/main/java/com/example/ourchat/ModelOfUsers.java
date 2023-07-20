package com.example.ourchat;

public class ModelOfUsers {
    String userName,id,pictureLink;

    public ModelOfUsers(String userName, String id, String pictureLink) {
        this.userName = userName;
        this.id = id;
        this.pictureLink = pictureLink;
    }

    public ModelOfUsers() {
    }

    public String getUserName() {
        return userName;
    }

    public String getId() {
        return id;
    }

    public String getPictureLink() {
        return pictureLink;
    }
}
