import java.util.Scanner;

/**
 * PROG5121 POE Part 1 & 2 - Registration, Login and Messaging System
 * Main application file that links all components
 */
public class poe {
    public static void main(String[] args) {
        Login loginSystem = new Login();
        Scanner scanner = new Scanner(System.in);
        
        // Registration process
        System.out.println("=== REGISTRATION ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        System.out.print("Enter cell phone number: ");
        String cellPhone = scanner.nextLine();
        
        // Register user
        String registrationResult = loginSystem.registerUser(username, password, cellPhone);
        System.out.println(registrationResult);
        
        // If registration was successful, proceed to login and messaging
        if (registrationResult.equals("User registered successfully.")) {
            System.out.println("\n=== LOGIN ===");
            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();
            
            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();
            
            // Attempt login
            boolean loginSuccess = loginSystem.loginUser(loginUsername, loginPassword);
            String loginStatus = loginSystem.returnLoginStatus(loginSuccess, "Kyle", "Viljoen");
            System.out.println(loginStatus);
            
            // If login successful, proceed to messaging system
            if (loginSuccess) {
                QuickChat quickChat = new QuickChat();
                quickChat.startMessagingSystem(scanner);
            }
        }
        
        scanner.close();
    }
}
    
    
