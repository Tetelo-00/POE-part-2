/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.loginn;

/**
 *
 * @author lab_services_student
 */
public class Loginn {

   

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                // Example usage of the registration function
        String username = "kyl_1";
        String password = "Ch&se@ke99!";
        String result = registerUser(username, password);
        System.out.println(result);
    }

    // Checks if the username contains an underscore and is no more than 5 characters long
    public static boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // Checks if the password meets complexity requirements:
    // at least 8 characters, includes an uppercase letter, a number, and a special character
    public static boolean checkPasswordComplexity(String password) {
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        return password.length() >= 8 && hasUppercase && hasDigit && hasSpecialChar;
    }

    // Validates that the phone number starts with "+" and contains only digits after that
    public static boolean checkCellphoneNumber(String phoneNumber) {
        return phoneNumber.startsWith("+") && phoneNumber.length() >= 10 && phoneNumber.matches("\\+\\d+");
    }

    // Registers the user by validating the username and password using the above checks
    public static String registerUser(String username, String password) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        return null;
    }
}
