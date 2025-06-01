/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author lifeo
 */
public class LoggerUtil {
    
    public static void logStatus(String action, String target, boolean success, String errorMessage) {
        if (success) {
            System.out.println(action + " " + target + " successfully.");
        } else {
            System.err.println("Failed to " + action.toLowerCase() + " " + target + ".");
            if (errorMessage != null && !errorMessage.isBlank()) {
                System.err.println("   Reason: " + errorMessage);
            }
        }
    }
}
