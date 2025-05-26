/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.loginn;

/**
 *
 * @author lab_services_student
 */
 import static org.junit.Assert.*;
import org.junit.Test;

public class LoginnTest {

    @Test
    public void testCheckUserName_Valid() {
        assertTrue(Loginn.checkUserName("ky_1"));
    }

    @Test
    public void testCheckUserName_InvalidTooLong() {
        assertFalse(Loginn.checkUserName("kyle_123"));
    }

    @Test
    public void testCheckUserName_MissingUnderscore() {
        assertFalse(Loginn.checkUserName("kyle"));
    }

    @Test
    public void testCheckPasswordComplexity_Valid() {
        assertTrue(Loginn.checkPasswordComplexity("Ch&se@ke99!"));
    }

    @Test
    public void testCheckPasswordComplexity_MissingUppercase() {
        assertFalse(Loginn.checkPasswordComplexity("ch&se@ke99!"));
    }

    @Test
    public void testCheckPasswordComplexity_MissingDigit() {
        assertFalse(Loginn.checkPasswordComplexity("Ch&se@ke!!"));
    }

    @Test
    public void testCheckPasswordComplexity_MissingSpecialChar() {
        assertFalse(Loginn.checkPasswordComplexity("Chaseke99"));
    }

    @Test
    public void testCheckPasswordComplexity_TooShort() {
        assertFalse(Loginn.checkPasswordComplexity("Ch@9!"));
    }

    @Test
    public void testCheckCellphoneNumber_Valid() {
        assertTrue(Loginn.checkCellphoneNumber("+2712345678"));
    }

    @Test
    public void testCheckCellphoneNumber_MissingPlus() {
        assertFalse(Loginn.checkCellphoneNumber("2712345678"));
    }

    @Test
    public void testCheckCellphoneNumber_TooShort() {
        assertFalse(Loginn.checkCellphoneNumber("+27123"));
    }

    @Test
    public void testCheckCellphoneNumber_ContainsLetters() {
        assertFalse(Loginn.checkCellphoneNumber("+27ABC1234"));
    }

    @Test
    public void testRegisterUser_Valid() {
        assertNull(Loginn.registerUser("ky_1", "AnyPassword"));
    }

    @Test
    public void testRegisterUser_InvalidUsername() {
        String result = Loginn.registerUser("kyle", "AnyPassword");
        assertNotNull(result);
        assertTrue(result.contains("Username is not correctly formatted"));
    }
}
