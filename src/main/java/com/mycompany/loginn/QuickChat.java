/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginn;

/**
 *
 * @author lab_services_student
 */
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.JSONArray;
import org.json.JSONObject;

public class QuickChat {
    private static int totalMessagesSent = 0;
    private static final String STORED_MESSAGES_FILE = "stored_messages.json";
    private static final String DISREGARDED_MESSAGES_FILE = "disregarded_messages.json";

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat");

        boolean loggedIn = false;
        String username = null;

        while (!loggedIn) {
            username = JOptionPane.showInputDialog("Enter username:");
            String password = JOptionPane.showInputDialog("Enter password:");

            if (username == null || password == null) {
                JOptionPane.showMessageDialog(null, "Login cancelled. Exiting.");
                System.exit(0);
            }

            if (!Loginn.checkUserName(username)) {
                JOptionPane.showMessageDialog(null, "Username is not correctly formatted.\n" +
                        "It must contain an underscore and be no more than five characters long.");
                continue;
            }

            if (!Loginn.checkPasswordComplexity(password)) {
                JOptionPane.showMessageDialog(null, "Password is not correctly formatted.\n" +
                        "It must be at least 8 characters long, contain an uppercase letter, a number, and a special character.");
                continue;
            }

            loggedIn = true;
        }

        totalMessagesSent = loadMessages(STORED_MESSAGES_FILE).length();

        while (true) {
            String mainMenu = """
                    Main Menu:
                    1. Send Messages
                    2. View Messages Sent
                    3. Message Tools
                    4. Quit
                    Enter choice (1-4):
                    """;

            String input = JOptionPane.showInputDialog(mainMenu);
            if (input == null) {
                JOptionPane.showMessageDialog(null, "No input detected. Exiting.");
                System.exit(0);
            }

            int choice;
            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number between 1 and 4.");
                continue;
            }

            switch (choice) {
                case 1 -> sendMessage();
                case 2 -> displayMessages();
                case 3 -> showMessageTools();
                case 4 -> System.exit(0);
                default -> JOptionPane.showMessageDialog(null, "Invalid selection.");
            }
        }
    }

    public static void showMessageTools() {
        while (true) {
            String toolsMenu = """
                    Message Tools:
                    1. Display Senders and Recipients
                    2. Display Longest Sent Message
                    3. Search by Message ID
                    4. Search by Recipient
                    5. Delete Message by Hash
                    6. Generate Full Sent Messages Report
                    7. Back to Main Menu
                    Enter choice (1-7):
                    """;

            String input = JOptionPane.showInputDialog(toolsMenu);
            if (input == null) {
                JOptionPane.showMessageDialog(null, "No input detected. Returning to Main Menu.");
                return;
            }

            int choice;
            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number between 1 and 7.");
                continue;
            }

            switch (choice) {
                case 1 -> displayMessages();
                case 2 -> displayLongestMessage();
                case 3 -> searchByMessageID();
                case 4 -> searchByRecipient();
                case 5 -> deleteByHash();
                case 6 -> fullReport();
                case 7 -> {
                    return; // back to main menu
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid option.");
            }
        }
    }

    public static void sendMessage() {
        String recipient = JOptionPane.showInputDialog("Enter recipient number (+...):");
        if (recipient == null || !recipient.startsWith("+") || recipient.length() > 11) {
            JOptionPane.showMessageDialog(null, "Invalid recipient format.");
            return;
        }

        String content = JOptionPane.showInputDialog("Enter your message:");
        if (content == null || content.isBlank()) {
            logSimpleMessage(DISREGARDED_MESSAGES_FILE, recipient != null ? recipient : "Unknown", content != null ? content : "");
            JOptionPane.showMessageDialog(null, "Message disregarded.");
            return;
        }

        String messageID = "MSG" + (1000 + totalMessagesSent);
        String hash = generateHash(content + recipient + messageID);

        JSONObject message = new JSONObject();
        message.put("Recipient", recipient);
        message.put("Message", content);
        message.put("MessageID", messageID);
        message.put("Hash", hash);

        appendMessageToFile(STORED_MESSAGES_FILE, message);
        totalMessagesSent++;

        JOptionPane.showMessageDialog(null, "Message sent and stored:\n" + message.toString(4));
    }

    public static void displayMessages() {
        JSONArray messages = loadMessages(STORED_MESSAGES_FILE);
        if (messages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages found.");
            return;
        }
        StringBuilder output = new StringBuilder();
        for (Object obj : messages) {
            JSONObject msg = (JSONObject) obj;
            output.append("Sender: User\nRecipient: ").append(msg.getString("Recipient"))
                    .append("\nMessage: ").append(msg.getString("Message"))
                    .append("\n---\n");
        }
        JOptionPane.showMessageDialog(null, output.toString());
    }

    public static void displayLongestMessage() {
        JSONArray messages = loadMessages(STORED_MESSAGES_FILE);
        JSONObject longest = null;
        for (Object obj : messages) {
            JSONObject msg = (JSONObject) obj;
            if (longest == null || msg.getString("Message").length() > longest.getString("Message").length()) {
                longest = msg;
            }
        }
        JOptionPane.showMessageDialog(null, "Longest Message:\n" + (longest != null ? longest.toString(4) : "None"));
    }

    public static void searchByMessageID() {
        String id = JOptionPane.showInputDialog("Enter Message ID:");
        if (id == null) {
            JOptionPane.showMessageDialog(null, "No ID entered.");
            return;
        }
        JSONArray messages = loadMessages(STORED_MESSAGES_FILE);
        for (Object obj : messages) {
            JSONObject msg = (JSONObject) obj;
            if (msg.getString("MessageID").equals(id)) {
                JOptionPane.showMessageDialog(null, msg.toString(4));
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Message ID not found.");
    }

    public static void searchByRecipient() {
        String recipient = JOptionPane.showInputDialog("Enter recipient:");
        if (recipient == null) {
            JOptionPane.showMessageDialog(null, "No recipient entered.");
            return;
        }
        JSONArray messages = loadMessages(STORED_MESSAGES_FILE);
        StringBuilder result = new StringBuilder();
        for (Object obj : messages) {
            JSONObject msg = (JSONObject) obj;
            if (msg.getString("Recipient").equals(recipient)) {
                result.append(msg.toString(4)).append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, result.isEmpty() ? "No messages found." : result.toString());
    }

    public static void deleteByHash() {
        String hash = JOptionPane.showInputDialog("Enter message hash to delete:");
        if (hash == null) {
            JOptionPane.showMessageDialog(null, "No hash entered.");
            return;
        }
        JSONArray messages = loadMessages(STORED_MESSAGES_FILE);
        for (int i = 0; i < messages.length(); i++) {
            if (messages.getJSONObject(i).getString("Hash").equals(hash)) {
                messages.remove(i);
                saveMessages(STORED_MESSAGES_FILE, messages);
                JOptionPane.showMessageDialog(null, "Message deleted.");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Hash not found.");
    }

    public static void fullReport() {
        JSONArray messages = loadMessages(STORED_MESSAGES_FILE);
        if (messages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages to report.");
            return;
        }
        StringBuilder report = new StringBuilder("--- Full Sent Messages Report ---\n");
        for (Object obj : messages) {
            JSONObject msg = (JSONObject) obj;
            report.append(msg.toString(4)).append("\n");
        }
        JOptionPane.showMessageDialog(null, report.toString());
    }

    // Utility Methods

    public static void appendMessageToFile(String filename, JSONObject message) {
        JSONArray messages = loadMessages(filename);
        messages.put(message);
        saveMessages(filename, messages);
    }

    public static JSONArray loadMessages(String filename) {
        try {
            if (!Files.exists(Paths.get(filename))) {
                return new JSONArray();
            }
            String content = new String(Files.readAllBytes(Paths.get(filename))).trim();
            if (content.isEmpty()) {
                return new JSONArray();
            }
            return new JSONArray(content);
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    public static void saveMessages(String filename, JSONArray messages) {
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(messages.toString(4));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving file: " + filename);
        }
    }

    public static void logSimpleMessage(String filename, String recipient, String content) {
        JSONObject json = new JSONObject();
        json.put("Recipient", recipient);
        json.put("Message", content);
        appendMessageToFile(filename, json);
    }

    public static String generateHash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(text.getBytes());
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

