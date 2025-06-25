/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginn;

/**
 *
 * @author lab_services_student
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class messageManipulator {
    // File name where messages are stored
    private static final String FILE_NAME = "stored_messages.json";

    /**
     * Loads messages from the JSON file.
     * @return JSONArray of messages, or an empty array if an error occurs.
     */
    public static JSONArray loadMessages() {
        try {
            // Read the file content as a string
            String content = new String(Files.readAllBytes(Paths.get(FILE_NAME)));
            // Wrap content in square brackets to form a valid JSON array
            return new JSONArray("[" + content.strip().replaceAll(",\\s*$", "") + "]");
        } catch (Exception e) {
            // Return an empty array if file not found or JSON is invalid
            return new JSONArray();
        }
    }

    /**
     * Saves an array of messages to the JSON file.
     * @param messages JSONArray containing message objects to be saved.
     */
    public static void saveMessages(JSONArray messages) {
        try (FileWriter fw = new FileWriter(FILE_NAME)) {
            // Write each message in formatted JSON, separated by commas
            for (int i = 0; i < messages.length(); i++) {
                fw.write(messages.getJSONObject(i).toString(4) + (i < messages.length() - 1 ? ",\n" : ""));
            }
        } catch (IOException e) {
            System.err.println("Error saving messages: " + e.getMessage());
        }
    }

    /**
     * Finds a single message using its ID.
     * @param id MessageID to search for.
     * @return JSONObject of the matching message, or null if not found.
     */
    public static JSONObject findMessageByID(String id) {
        JSONArray messages = loadMessages();
        for (Object obj : messages) {
            JSONObject msg = (JSONObject) obj;
            if (msg.getString("MessageID").equalsIgnoreCase(id)) {
                return msg;
            }
        }
        return null;
    }

    /**
     * Finds all messages sent to a particular recipient.
     * @param recipient The recipient to search for.
     * @return JSONArray of all messages sent to the given recipient.
     */
    public static JSONArray findMessagesByRecipient(String recipient) {
        JSONArray messages = loadMessages();
        JSONArray result = new JSONArray();
        for (Object obj : messages) {
            JSONObject msg = (JSONObject) obj;
            if (msg.getString("Recipient").equals(recipient)) {
                result.put(msg);
            }
        }
        return result;
    }

    /**
     * Deletes a message from the file using its SHA-256 hash.
     * @param hash The hash of the message to delete.
     * @return true if the message was found and deleted; false otherwise.
     */
    public static boolean deleteMessageByHash(String hash) {
        JSONArray messages = loadMessages();
        for (int i = 0; i < messages.length(); i++) {
            if (messages.getJSONObject(i).getString("Hash").equals(hash)) {
                messages.remove(i); // Remove the matched message
                saveMessages(messages); // Save updated list back to file
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the message with the longest content.
     * @return JSONObject of the message with the longest text.
     */
    public static JSONObject getLongestMessage() {
        JSONArray messages = loadMessages();
        JSONObject longest = null;
        for (Object obj : messages) {
            JSONObject msg = (JSONObject) obj;
            if (longest == null || msg.getString("Message").length() > longest.getString("Message").length()) {
                longest = msg; // Update if this message is longer
            }
        }
        return longest;
    }

    /**
     * Generates a SHA-256 hash of a given string.
     * @param text The input string.
     * @return The hash as a hex string, or "HASH_ERROR" if hashing fails.
     */
    public static String generateHash(String text) {
        try {
            // Create SHA-256 hash from the input text
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            // Convert bytes to hex
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return "HASH_ERROR";
        }
    }
}