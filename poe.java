import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * PROG5121 POE Part 1, 2 & 3 - Registration, Login and Messaging System
 * Main application file that links all components
 * Student: Dilshad Amardien (ST10488991)
 */
public class poe {
    public static void main(String[] args) {
        System.out.println("🚀 Starting QuickChat Application...");
        System.out.println("=====================================");
        
        // Initialize the login system
        Login loginSystem = new Login();
        Scanner scanner = new Scanner(System.in);
        
        // Show welcome message
        showWelcomeMessage();
        
        // Registration process
        System.out.println("\n" + "=".repeat(40));
        System.out.println("📝 REGISTRATION");
        System.out.println("=".repeat(40));
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        System.out.print("Enter cell phone number: ");
        String cellPhone = scanner.nextLine();
        
        // Register user
        String registrationResult = loginSystem.registerUser(username, password, cellPhone);
        System.out.println("\n" + registrationResult);
        
        // If registration was successful, proceed to login and messaging
        if (registrationResult.equals("User registered successfully.")) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("🔐 LOGIN");
            System.out.println("=".repeat(40));
            
            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();
            
            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();
            
            // Attempt login
            boolean loginSuccess = loginSystem.loginUser(loginUsername, loginPassword);
            String loginStatus = loginSystem.returnLoginStatus(loginSuccess, "Dilshad", "Amardien");
            System.out.println("\n" + loginStatus);
            
            // If login successful, proceed to messaging system
            if (loginSuccess) {
                showSuccessNotification("Login Successful!", "Welcome to QuickChat " + loginUsername + "!");
                
                // Initialize and start QuickChat system
                QuickChat quickChat = new QuickChat();
                quickChat.startMessagingSystem(scanner);
            } else {
                showErrorNotification("Login Failed", "Invalid username or password. Please try again.");
            }
        } else {
            showErrorNotification("Registration Failed", "Please fix the errors and try again.");
        }
        
        scanner.close();
        System.out.println("\n=====================================");
        System.out.println("👋 QuickChat Application Closed");
        System.out.println("=====================================");
    }
    
    private static void showWelcomeMessage() {
        String welcomeMessage = 
            "=====================================\n" +
            "🚀 WELCOME TO QUICKCHAT MESSAGING SYSTEM\n" +
            "=====================================\n" +
            "Student: Dilshad Amardien (ST10488991)\n" +
            "Module: PROG5121 Programming 1A\n" +
            "=====================================";
        
        System.out.println(welcomeMessage);
    }
    
    private static void showSuccessNotification(String title, String message) {
        JOptionPane.showMessageDialog(null, message, "✅ " + title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static void showErrorNotification(String title, String message) {
        JOptionPane.showMessageDialog(null, message, "❌ " + title, JOptionPane.ERROR_MESSAGE);
    }
}