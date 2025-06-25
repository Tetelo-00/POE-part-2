/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginn;

/**
 *
 * @author lab_services_student
 */
 
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Message {
    private final String recipient;
    private final String messageText;
    private final String messageID;
    private final String hash;

    public Message(String recipient, String messageText, int counter) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = "MSG" + (1000 + counter);
        this.hash = generateHash(recipient + messageText + messageID);
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getHash() {
        return hash;
    }

    public String displayMessageDetails() {
        return "ID: " + messageID + "\nRecipient: " + recipient + "\nMessage: " + messageText + "\nHash: " + hash;
    }

    private String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return "HASH_ERROR";
        }
    }
}
