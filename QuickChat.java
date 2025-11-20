import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * QuickChat class - Main messaging system with Part 2 & 3 features
 * Linked to: poe.java (main) and Message.java
 */
public class QuickChat {
    private List<Message> messages = new ArrayList<>();
    private int totalMessagesSent = 0;
    
    // Arrays for Part 3 requirements
    private String[] sentMessages = new String[100];
    private String[] disregardedMessages = new String[100];
    private String[] storedMessages = new String[100];
    private String[] messageHashes = new String[100];
    private String[] messageIDs = new String[100];
    
    private int sentCount = 0;
    private int disregardedCount = 0;
    private int storedCount = 0;
    
    public void startMessagingSystem(Scanner scanner) {
        showWelcomeNotification();
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            System.out.print("Choose an option (1-5): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer
                
                switch (choice) {
                    case 1:
                        sendMessages(scanner);
                        break;
                    case 2:
                        showRecentMessages();
                        break;
                    case 3:
                        displayMessageReport();
                        break;
                    case 4:
                        searchAndManageMessages(scanner);
                        break;
                    case 5:
                        running = false;
                        showExitNotification();
                        break;
                    default:
                        System.out.println("❌ Invalid option. Please choose 1-5.");
                }
            } catch (Exception e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    private void sendMessages(Scanner scanner) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("📤 SEND MESSAGES");
        System.out.println("=".repeat(40));
        
        System.out.print("How many messages do you wish to send? ");
        int numMessages = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        
        if (numMessages <= 0) {
            System.out.println("❌ Please enter a positive number.");
            return;
        }
        
        System.out.println("📝 Preparing to send " + numMessages + " message(s)...");
        
        // For loop as required - runs for assigned number of messages
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");
            
            Message message = new Message();
            
            // Get recipient
            System.out.print("Enter recipient cell number (+27/027 format): ");
            String recipient = scanner.nextLine();
            message.setRecipient(recipient);
            
            // Get message content
            System.out.print("Enter your message (max 250 characters): ");
            String messageContent = scanner.nextLine();
            message.setMessageContent(messageContent);
            
            // Generate message ID and hash using loop counter
            message.generateMessageID();
            message.createMessageHash(i + 1);
            
            // Show message options
            int action = showMessageOptions(scanner);
            processMessageAction(action, message, i + 1);
        }
        
        System.out.println("\n✅ Session completed! Total messages sent: " + totalMessagesSent);
    }
    
    private int showMessageOptions(Scanner scanner) {
        System.out.println("\n💡 Message Options:");
        System.out.println("1) 🚀 Send Message Now");
        System.out.println("2) ❌ Disregard Message");
        System.out.println("3) 💾 Store for Later");
        System.out.print("Choose action (1-3): ");
        
        return scanner.nextInt();
    }
    
    private void processMessageAction(int action, Message message, int messageNumber) {
        switch (action) {
            case 1: // Send message
                String result = message.sendMessage();
                System.out.println("📨 " + result);
                
                if (result.equals("Message successfully sent.")) {
                    message.setSent(true);
                    messages.add(message);
                    totalMessagesSent++;
                    
                    // Add to arrays
                    if (sentCount < sentMessages.length) {
                        sentMessages[sentCount] = message.getMessageContent();
                        messageHashes[sentCount] = message.getMessageHash();
                        messageIDs[sentCount] = message.getMessageID();
                        sentCount++;
                    }
                }
                break;
                
            case 2: // Disregard message
                String disregardResult = message.disregardMessage();
                System.out.println("🗑️ " + disregardResult);
                
                // Add to disregarded array
                if (disregardedCount < disregardedMessages.length) {
                    disregardedMessages[disregardedCount] = message.getMessageContent();
                    disregardedCount++;
                }
                break;
                
            case 3: // Store message
                message.storeMessage();
                messages.add(message);
                
                // Add to stored array
                if (storedCount < storedMessages.length) {
                    storedMessages[storedCount] = message.getMessageContent();
                    storedCount++;
                }
                System.out.println("💾 Message stored successfully.");
                break;
                
            default:
                System.out.println("❌ Invalid action. Message disregarded.");
        }
    }
    
    private void showRecentMessages() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("📨 RECENTLY SENT MESSAGES");
        System.out.println("=".repeat(40));
        
        if (sentCount == 0) {
            System.out.println("No messages sent yet.");
            return;
        }
        
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i] != null) {
                System.out.println((i + 1) + ". " + sentMessages[i]);
            }
        }
    }
    
    private void displayMessageReport() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("📊 MESSAGE SYSTEM REPORT");
        System.out.println("=".repeat(40));
        
        System.out.println("✅ Sent Messages: " + sentCount);
        System.out.println("💾 Stored Messages: " + storedCount);
        System.out.println("❌ Disregarded Messages: " + disregardedCount);
        System.out.println("📈 Total Processed: " + (sentCount + storedCount + disregardedCount));
        
        // Find and display longest message
        String longestMessage = findLongestMessage();
        if (longestMessage != null) {
            System.out.println("📏 Longest Message: \"" + 
                (longestMessage.length() > 50 ? longestMessage.substring(0, 50) + "..." : longestMessage) + "\"");
        }
        
        // Display all message hashes
        System.out.println("\n🔗 Message Hashes:");
        for (int i = 0; i < sentCount; i++) {
            if (messageHashes[i] != null) {
                System.out.println("  " + (i + 1) + ". " + messageHashes[i]);
            }
        }
    }
    
    private void searchAndManageMessages(Scanner scanner) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("🔍 SEARCH & MANAGE MESSAGES");
        System.out.println("=".repeat(40));
        
        System.out.println("1) 🔎 Search by Message ID");
        System.out.println("2) 👤 Search by Recipient");
        System.out.println("3) 🗑️ Delete by Message Hash");
        System.out.println("4) ↩️ Back to Main Menu");
        System.out.print("Choose option (1-4): ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        
        switch (choice) {
            case 1:
                searchByMessageID(scanner);
                break;
            case 2:
                searchByRecipient(scanner);
                break;
            case 3:
                deleteByMessageHash(scanner);
                break;
            case 4:
                return;
            default:
                System.out.println("❌ Invalid option.");
        }
    }
    
    private void searchByMessageID(Scanner scanner) {
        System.out.print("Enter Message ID to search: ");
        String searchID = scanner.nextLine();
        
        boolean found = false;
        for (Message msg : messages) {
            if (msg.getMessageID() != null && msg.getMessageID().equals(searchID)) {
                System.out.println("✅ Message Found:");
                System.out.println("   📱 Recipient: " + msg.getRecipient());
                System.out.println("   💬 Message: " + msg.getMessageContent());
                System.out.println("   🔗 Hash: " + msg.getMessageHash());
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("❌ No message found with ID: " + searchID);
        }
    }
    
    private void searchByRecipient(Scanner scanner) {
        System.out.print("Enter recipient number to search: ");
        String recipient = scanner.nextLine();
        
        System.out.println("📨 Messages for " + recipient + ":");
        int count = 0;
        for (Message msg : messages) {
            if (msg.getRecipient() != null && msg.getRecipient().contains(recipient)) {
                count++;
                System.out.println("   " + count + ". " + msg.getMessageContent());
            }
        }
        
        if (count == 0) {
            System.out.println("❌ No messages found for this recipient.");
        }
    }
    
    private void deleteByMessageHash(Scanner scanner) {
        System.out.print("Enter Message Hash to delete: ");
        String hash = scanner.nextLine();
        
        boolean deleted = false;
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            if (msg.getMessageHash() != null && msg.getMessageHash().equals(hash)) {
                String content = msg.getMessageContent();
                messages.remove(i);
                
                // Remove from arrays
                for (int j = 0; j < sentCount; j++) {
                    if (sentMessages[j] != null && sentMessages[j].equals(content)) {
                        sentMessages[j] = null;
                        break;
                    }
                }
                
                System.out.println("✅ Message deleted: \"" + 
                    (content.length() > 30 ? content.substring(0, 30) + "..." : content) + "\"");
                deleted = true;
                break;
            }
        }
        
        if (!deleted) {
            System.out.println("❌ No message found with hash: " + hash);
        }
    }
    
    private String findLongestMessage() {
        String longest = "";
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i] != null && sentMessages[i].length() > longest.length()) {
                longest = sentMessages[i];
            }
        }
        return longest.isEmpty() ? null : longest;
    }
    
    private void showWelcomeNotification() {
        JOptionPane.showMessageDialog(null, 
            "🚀 Welcome to QuickChat Messaging System!\n\n" +
            "You can now:\n" +
            "• Send messages to recipients\n" +
            "• Store messages for later\n" +
            "• Search and manage your messages\n" +
            "• View detailed reports", 
            "💬 QuickChat Activated", 
            JOptionPane.INFORMATION_MESSAGE);
        
        System.out.println("\n🎉 QuickChat Messaging System Activated!");
        System.out.println("=====================================");
    }
    
    private void showExitNotification() {
        JOptionPane.showMessageDialog(null, 
            "Thank you for using QuickChat!\n\n" +
            "Summary:\n" +
            "• Messages sent: " + totalMessagesSent + "\n" +
            "• Total in system: " + messages.size() + "\n" +
            "Come back soon! 👋", 
            "🚪 QuickChat Closed", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("🚀 QUICKCHAT MAIN MENU");
        System.out.println("=".repeat(40));
        System.out.println("1) 📤 Send Messages");
        System.out.println("2) 📨 Show Recent Messages");
        System.out.println("3) 📊 Display System Report");
        System.out.println("4) 🔍 Search & Manage Messages");
        System.out.println("5) 🚪 Exit QuickChat");
        System.out.println("=".repeat(40));
    }
    
    // Getters for testing
    public int returnTotalMessages() { return totalMessagesSent; }
    public List<Message> getMessages() { return messages; }
    public String[] getSentMessages() { return sentMessages; }
    public int getSentCount() { return sentCount; }
}