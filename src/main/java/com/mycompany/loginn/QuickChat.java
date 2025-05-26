/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginn;

/**
 *
 * @author lab_services_student
 */
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

public class QuickChat {

    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    public static void main(String[] args) {
        // Collect user's basic details
        String firstName = JOptionPane.showInputDialog("Enter your first name:");
        String lastName = JOptionPane.showInputDialog("Enter your last name:");

        // Username input and validation
        String username;
        while (true) {
            username = JOptionPane.showInputDialog("Create your username:");
            if (Loginn.checkUserName(username)) {
                JOptionPane.showMessageDialog(null, "Username successfully created.");
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Username must contain an underscore and be no more than 5 characters.");
            }
        }

        // Password input and validation
        String password;
        while (true) {
            password = JOptionPane.showInputDialog("Create your password:");
            if (Loginn.checkPasswordComplexity(password)) {
                JOptionPane.showMessageDialog(null, "Password successfully captured.");
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long and contain a capital letter, number, and special character.");
            }
        }

        // Phone number input and validation
        String phone;
        while (true) {
            phone = JOptionPane.showInputDialog("Enter your phone number (e.g. +27123456789):");
            if (Loginn.checkCellphoneNumber(phone)) {
                JOptionPane.showMessageDialog(null, "Phone number successfully added.");
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Phone number must start with + and be valid.");
            }
        }

        // Registration confirmation
        String result = Loginn.registerUser(username, password);
        if (result != null) {
            JOptionPane.showMessageDialog(null, result);
        }

        // Simulated login
        JOptionPane.showMessageDialog(null, "--- LOGIN SECTION ---");
        while (true) {
            String inputUsername = JOptionPane.showInputDialog("Login - Username:");
            String inputPassword = JOptionPane.showInputDialog("Login - Password:");

            if (inputUsername.equals(username) && inputPassword.equals(password)) {
                JOptionPane.showMessageDialog(null, "Login successful! Welcome " + firstName + " " + lastName + ".");
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Login failed. Try again.");
            }
        }

        // Main chat functionality
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat!");

        int messageLimit = Integer.parseInt(JOptionPane.showInputDialog("How many messages would you like to send?"));

        while (true) {
            String menu = "Choose an option:\n1) Send Message\n2) Show Recently Sent Messages\n3) Quit";
            int choice = Integer.parseInt(JOptionPane.showInputDialog(menu));

            switch (choice) {
                case 1 -> {
                    if (totalMessagesSent >= messageLimit) {
                        JOptionPane.showMessageDialog(null, "Message limit reached.");
                        break;
                    }

                    String recipient = JOptionPane.showInputDialog("Enter recipient's number (e.g. +27123456789):");
                    while (!Message.checkRecipientCell(recipient)) {
                        recipient = JOptionPane.showInputDialog("Invalid number. Must start with '+' and be up to 11 characters. Try again:");
                    }

                    String messageText = JOptionPane.showInputDialog("Enter your message:");

                    String[] options = {"Send", "Store to File", "Cancel"};
                    int action = JOptionPane.showOptionDialog(null, "Choose an action:",
                            "Message Options", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                            null, options, options[0]);

                    if (action == 2 || action == JOptionPane.CLOSED_OPTION) {
                        JOptionPane.showMessageDialog(null, "Message cancelled.");
                        break;
                    }

                    Message message = new Message(recipient, messageText);
                    sentMessages.add(message);
                    totalMessagesSent++;

                    if (action == 0) {
                        // Send message
                        JOptionPane.showMessageDialog(null, "Message sent:\n" + message.displayMessageDetails());

                        String delete = JOptionPane.showInputDialog("Press 0 to delete the message:");
                        if ("0".equals(delete)) {
                            sentMessages.remove(message);
                            totalMessagesSent--;
                            JOptionPane.showMessageDialog(null, "Message deleted.");
                        }

                    } else if (action == 1) {
                        // Store message in JSON file
                        try (FileWriter writer = new FileWriter("stored_messages.json", true)) {
                            JSONObject json = new JSONObject();
                            json.put("Recipient", recipient);
                            json.put("Message", messageText);
                            writer.write(json.toString(4) + ",\n");
                            JOptionPane.showMessageDialog(null, "Message saved:\n" + message.displayMessageDetails());
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null, "Error storing message: " + e.getMessage());
                        }
                    }
                }

                case 2 -> {
                    if (sentMessages.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No messages sent yet.");
                    } else {
                        StringBuilder history = new StringBuilder("Recent Messages:\n");
                        for (Message m : sentMessages) {
                            history.append(m.displayMessageDetails()).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, history.toString());
                    }
                }

                case 3 -> {
                    JOptionPane.showMessageDialog(null, "Total messages sent: " + totalMessagesSent + "\nGoodbye!");
                    System.exit(0);
                }

                default -> JOptionPane.showMessageDialog(null, "Invalid choice.");
            }
        }
    }
}
