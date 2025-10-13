import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * QuickChat class - Main messaging system
 * Handles the chat interface and message management
 */
public class QuickChat {
    private List<Message> messages = new ArrayList<>();
    private int totalMessagesSent = 0;
    private int messageCounter = 0;
    
    public void startMessagingSystem(Scanner scanner) {
        System.out.println("\nWelcome to QuickChat.");
        
        // Main menu loop
        boolean running = true;
        while (running) {
            System.out.println("\n=== QuickChat Menu ===");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            
            int menuChoice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (menuChoice) {
                case 1:
                    sendMessages(scanner);
                    break;
                case 2:
                    showRecentMessages();
                    break;
                case 3:
                    running = false;
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void sendMessages(Scanner scanner) {
        System.out.print("\nHow many messages do you wish to send? ");
        int numMessages = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n=== Message " + (i + 1) + " ===");
            
            Message message = new Message();
            
            // Set recipient
            System.out.print("Enter recipient cell number: ");
            String recipient = scanner.nextLine();
            message.setRecipient(recipient);
            
            // Set message content
            System.out.print("Enter your message (max 250 characters): ");
            String messageContent = scanner.nextLine();
            message.setMessageContent(messageContent);
            
            // Generate message ID and hash
            message.generateMessageID();
            message.createMessageHash(messageCounter + 1);
            
            // Display message options
            int choice = displayMessageOptions(scanner);
            
            // Process user choice
            processMessageChoice(choice, message);
        }
        
        // Display total messages sent
        System.out.println("\nTotal number of messages sent: " + totalMessagesSent);
    }
    
    private int displayMessageOptions(Scanner scanner) {
        System.out.println("\nMessage Options:");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Choose an option: ");
        
        return scanner.nextInt();
    }
    
    private void processMessageChoice(int choice, Message message) {
        switch (choice) {
            case 1: // Send message
                String sendResult = message.sendMessage();
                System.out.println(sendResult);
                
                if (sendResult.equals("Message successfully sent.")) {
                    message.setSent(true);
                    messages.add(message);
                    totalMessagesSent++;
                    messageCounter++;
                    
                    // Display full message details
                    message.printMessageDetails();
                }
                break;
                
            case 2: // Disregard message
                String disregardResult = message.disregardMessage();
                System.out.println(disregardResult);
                break;
                
            case 3: // Store message
                message.storeMessage();
                messages.add(message);
                System.out.println("Message successfully stored.");
                break;
                
            default:
                System.out.println("Invalid option. Message disregarded.");
        }
    }
    
    private void showRecentMessages() {
        System.out.println("\n=== Recently Sent Messages ===");
        
        if (messages.isEmpty()) {
            System.out.println("No messages sent yet.");
            return;
        }
        
        int count = 0;
        for (Message message : messages) {
            if (message.isSent()) {
                System.out.println((count + 1) + ". " + message.printMessages());
                count++;
            }
        }
        
        if (count == 0) {
            System.out.println("No sent messages found.");
        }
    }
    
    // Method to return total number of messages sent
    public int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    // Method to get all messages (for testing)
    public List<Message> getMessages() {
        return messages;
    }
}