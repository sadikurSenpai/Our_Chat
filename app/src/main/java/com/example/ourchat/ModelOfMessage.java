package com.example.ourchat;

public class ModelOfMessage {
    String nameOfUser,picLinkOfUser,messageOfUser,idOfKey,mail;

    public ModelOfMessage() {
    }

    public ModelOfMessage(String nameOfUser, String picLinkOfUser, String messageOfUser, String idOfKey,String mail) {
        this.nameOfUser = nameOfUser;
        this.picLinkOfUser = picLinkOfUser;
        this.messageOfUser = messageOfUser;
        this.idOfKey = idOfKey;
        this.mail=mail;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public String getPicLinkOfUser() {
        return picLinkOfUser;
    }

    public String getMessageOfUser() {
        return messageOfUser;
    }

    public String getIdOfKey() {
        return idOfKey;
    }
    public String getMail() {
        return mail;
    }
}
