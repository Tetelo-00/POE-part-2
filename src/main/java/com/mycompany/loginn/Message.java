/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginn;

/**
 *
 * @author lab_services_student
 */
    public class Message {
    private final String recipient;
    private final String messageText;

    // Constructor
    public Message(String recipient, String messageText) {
        if (!checkRecipientCell(recipient)) {
            throw new IllegalArgumentException("Invalid recipient number.");
        }
        this.recipient = recipient;
        this.messageText = messageText;
    }

    // Validates recipient phone number
    public static boolean checkRecipientCell(String phone) {
        return phone != null && phone.startsWith("+") && phone.length() <= 11 && phone.matches("\\+\\d+");
    }

    // Displays message details
    public String displayMessageDetails() {
        return "To: " + recipient + "\nMessage: " + messageText;
    }
}


