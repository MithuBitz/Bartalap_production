package com.example.bartalap.Chat;

public class MessageObject {

    String messageId, senderId, message;

    //Constructor of the object
    public MessageObject(String messageId, String senderId, String message){
        //make the global string of this object constructor
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
    }

    //Getter method for the String
    public String getMessageId() { return messageId; }
    public String getSenderId() { return senderId; }
    public String getMessage() { return message; }
}


