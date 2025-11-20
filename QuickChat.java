import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * QuickChat class - Main messaging system with Arrays (No ArrayList)
 * Linked to: poe.java (main) and Message.java
 */
public class QuickChat {
    // Arrays for Part 3 requirements - NO ARRAYLISTS
    private Message[] allMessages = new Message[100];  // Array to store all messages
    private String[] sentMessages = new String[100];   // Array for sent message content
    private String[] disregardedMessages = new String[100]; // Array for disregarded messages
    private String[] storedMessages = new String[100]; // Array for stored messages
    private String[] messageHashes = new String[100];  // Array for message hashes
    private String[] messageIDs = new String[100];     // Array for message IDs
    
    // Counters for each array
    private int totalMessagesCount = 0;
    private int totalMessagesSent = 0;
    private int sentCount = 0;
    private int disregardedCount = 0;
    private int storedCount = 0;
    private int messageCounter = 0;
    
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
        
        // Check if we have enough space in arrays
        if (totalMessagesCount + numMessages > allMessages.length) {
            System.out.println("❌ Cannot add " + numMessages + " messages. Maximum capacity is " + 
                             (allMessages.length - totalMessagesCount) + " more messages.");
            return;
        }
        
        System.out.println("📝 Preparing to send " + numMessages + " message(s)...");
        
        // For loop as required - runs for assigned number of messages
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");
            
            // Create new message object
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
            message.createMessageHash(messageCounter + 1);
            messageCounter++;
            
            // Show message options
            int action = showMessageOptions(scanner);
            processMessageAction(action, message);
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
    
    private void processMessageAction(int action, Message message) {
        switch (action) {
            case 1: // Send message
                String result = message.sendMessage();
                System.out.println("📨 " + result);
                
                if (result.equals("Message successfully sent.")) {
                    message.setSent(true);
                    
                    // Add to allMessages array
                    allMessages[totalMessagesCount] = message;
                    totalMessagesCount++;
                    
                    // Add to sent messages array
                    sentMessages[sentCount] = message.getMessageContent();
                    messageHashes[sentCount] = message.getMessageHash();
                    messageIDs[sentCount] = message.getMessageID();
                    sentCount++;
                    
                    totalMessagesSent++;
                }
                break;
                
            case 2: // Disregard message
                String disregardResult = message.disregardMessage();
                System.out.println("🗑️ " + disregardResult);
                
                // Add to allMessages array
                allMessages[totalMessagesCount] = message;
                totalMessagesCount++;
                
                // Add to disregarded array
                disregardedMessages[disregardedCount] = message.getMessageContent();
                disregardedCount++;
                break;
                
            case 3: // Store message
                message.storeMessage();
                
                // Add to allMessages array
                allMessages[totalMessagesCount] = message;
                totalMessagesCount++;
                
                // Add to stored array
                storedMessages[storedCount] = message.getMessageContent();
                storedCount++;
                
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
        
        System.out.println("📂 All Messages in System: " + totalMessagesCount);
        System.out.println("✅ Sent Messages: " + sentCount);
        System.out.println("💾 Stored Messages: " + storedCount);
        System.out.println("❌ Disregarded Messages: " + disregardedCount);
        System.out.println("📈 Total Processed: " + totalMessagesSent);
        
        // Find and display longest message
        String longestMessage = findLongestMessage();
        if (longestMessage != null) {
            System.out.println("📏 Longest Message: \"" + 
                (longestMessage.length() > 50 ? longestMessage.substring(0, 50) + "..." : longestMessage) + "\"");
            System.out.println("   Length: " + longestMessage.length() + " characters");
        }
        
        // Display all message hashes
        System.out.println("\n🔗 Message Hashes:");
        for (int i = 0; i < sentCount; i++) {
            if (messageHashes[i] != null) {
                System.out.println("  " + (i + 1) + ". " + messageHashes[i]);
            }
        }
        
        // Display array usage
        System.out.println("\n💾 Array Capacity Usage:");
        System.out.println("   All Messages: " + totalMessagesCount + "/" + allMessages.length);
        System.out.println("   Sent Messages: " + sentCount + "/" + sentMessages.length);
        System.out.println("   Stored Messages: " + storedCount + "/" + storedMessages.length);
    }
    
    private void searchAndManageMessages(Scanner scanner) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("🔍 SEARCH & MANAGE MESSAGES");
        System.out.println("=".repeat(40));
        
        System.out.println("1) 🔎 Search by Message ID");
        System.out.println("2) 👤 Search by Recipient");
        System.out.println("3) 📋 Display All Messages");
        System.out.println("4) 🗑️ Delete by Message Hash");
        System.out.println("5) ↩️ Back to Main Menu");
        System.out.print("Choose option (1-5): ");
        
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
                displayAllMessages();
                break;
            case 4:
                deleteByMessageHash(scanner);
                break;
            case 5:
                return;
            default:
                System.out.println("❌ Invalid option.");
        }
    }
    
    private void searchByMessageID(Scanner scanner) {
        System.out.print("Enter Message ID to search: ");
        String searchID = scanner.nextLine();
        
        boolean found = false;
        for (int i = 0; i < totalMessagesCount; i++) {
            if (allMessages[i] != null && allMessages[i].getMessageID() != null && 
                allMessages[i].getMessageID().equals(searchID)) {
                System.out.println("✅ Message Found:");
                System.out.println("   📱 Recipient: " + allMessages[i].getRecipient());
                System.out.println("   💬 Message: " + allMessages[i].getMessageContent());
                System.out.println("   🔗 Hash: " + allMessages[i].getMessageHash());
                System.out.println("   📊 Status: " + (allMessages[i].isSent() ? "SENT" : 
                    (allMessages[i].isStored() ? "STORED" : "DISREGARDED")));
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
        for (int i = 0; i < totalMessagesCount; i++) {
            if (allMessages[i] != null && allMessages[i].getRecipient() != null && 
                allMessages[i].getRecipient().contains(recipient)) {
                count++;
                String status = allMessages[i].isSent() ? "SENT" : 
                              (allMessages[i].isStored() ? "STORED" : "DISREGARDED");
                System.out.println("   " + count + ". [" + status + "] " + allMessages[i].getMessageContent());
            }
        }
        
        if (count == 0) {
            System.out.println("❌ No messages found for this recipient.");
        } else {
            System.out.println("   Found " + count + " message(s)");
        }
    }
    
    private void displayAllMessages() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📋 ALL MESSAGES IN SYSTEM");
        System.out.println("=".repeat(50));
        
        if (totalMessagesCount == 0) {
            System.out.println("No messages in the system.");
            return;
        }
        
        for (int i = 0; i < totalMessagesCount; i++) {
            if (allMessages[i] != null) {
                String status = allMessages[i].isSent() ? "✅ SENT" : 
                              (allMessages[i].isStored() ? "💾 STORED" : "❌ DISREGARDED");
                String preview = allMessages[i].getMessageContent().length() > 30 ? 
                    allMessages[i].getMessageContent().substring(0, 30) + "..." : 
                    allMessages[i].getMessageContent();
                
                System.out.println((i + 1) + ". " + status + " | To: " + allMessages[i].getRecipient());
                System.out.println("   Message: " + preview);
                System.out.println("   Hash: " + allMessages[i].getMessageHash());
                System.out.println("   ID: " + allMessages[i].getMessageID());
                System.out.println("   ---");
            }
        }
    }
    
    private void deleteByMessageHash(Scanner scanner) {
        System.out.print("Enter Message Hash to delete: ");
        String hash = scanner.nextLine();
        
        boolean deleted = false;
        for (int i = 0; i < totalMessagesCount; i++) {
            if (allMessages[i] != null && allMessages[i].getMessageHash() != null && 
                allMessages[i].getMessageHash().equals(hash)) {
                
                String content = allMessages[i].getMessageContent();
                String recipient = allMessages[i].getRecipient();
                
                // Remove from allMessages array by shifting elements
                for (int j = i; j < totalMessagesCount - 1; j++) {
                    allMessages[j] = allMessages[j + 1];
                }
                allMessages[totalMessagesCount - 1] = null;
                totalMessagesCount--;
                
                // Also remove from specific arrays
                removeFromArray(sentMessages, sentCount, content);
                removeFromArray(storedMessages, storedCount, content);
                removeFromArray(disregardedMessages, disregardedCount, content);
                
                System.out.println("✅ Message deleted: \"" + 
                    (content.length() > 30 ? content.substring(0, 30) + "..." : content) + "\"");
                System.out.println("   Recipient: " + recipient);
                deleted = true;
                break;
            }
        }
        
        if (!deleted) {
            System.out.println("❌ No message found with hash: " + hash);
        }
    }
    
    // Helper method to remove item from array
    private void removeFromArray(String[] array, int count, String content) {
        for (int i = 0; i < count; i++) {
            if (array[i] != null && array[i].equals(content)) {
                // Shift elements to remove the item
                for (int j = i; j < count - 1; j++) {
                    array[j] = array[j + 1];
                }
                array[count - 1] = null;
                break;
            }
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
            "Now using ARRAYS (No ArrayList) for Part 3 requirements!\n\n" +
            "You can now:\n" +
            "• Send messages to recipients\n" +
            "• Store messages for later\n" +
            "• Search and manage your messages\n" +
            "• View detailed reports with arrays", 
            "💬 QuickChat with Arrays", 
            JOptionPane.INFORMATION_MESSAGE);
        
        System.out.println("\n🎉 QuickChat Messaging System Activated!");
        System.out.println("✨ Now using ARRAYS instead of ArrayList");
        System.out.println("=====================================");
    }
    
    private void showExitNotification() {
        JOptionPane.showMessageDialog(null, 
            "Thank you for using QuickChat!\n\n" +
            "Array Usage Summary:\n" +
            "• Total messages: " + totalMessagesCount + "/" + allMessages.length + "\n" +
            "• Messages sent: " + sentCount + "\n" +
            "• Messages stored: " + storedCount + "\n" +
            "• Array capacity used: " + ((totalMessagesCount * 100) / allMessages.length) + "%", 
            "🚪 QuickChat Closed", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("🚀 QUICKCHAT MAIN MENU (ARRAYS)");
        System.out.println("=".repeat(40));
        System.out.println("1) 📤 Send Messages");
        System.out.println("2) 📨 Show Recent Messages");
        System.out.println("3) 📊 Display System Report");
        System.out.println("4) 🔍 Search & Manage Messages");
        System.out.println("5) 🚪 Exit QuickChat");
        System.out.println("=".repeat(40));
        System.out.println("Array Usage: " + totalMessagesCount + "/" + allMessages.length + " messages");
        System.out.println("=".repeat(40));
    }
    
    // Getters for testing - NOW RETURNING ARRAYS
    public int returnTotalMessages() { return totalMessagesSent; }
    public Message[] getAllMessages() { return allMessages; }
    public String[] getSentMessages() { return sentMessages; }
    public String[] getStoredMessages() { return storedMessages; }
    public String[] getDisregardedMessages() { return disregardedMessages; }
    public String[] getMessageHashes() { return messageHashes; }
    public String[] getMessageIDs() { return messageIDs; }
    public int getTotalMessagesCount() { return totalMessagesCount; }
    public int getSentCount() { return sentCount; }
    public int getStoredCount() { return storedCount; }
    public int getDisregardedCount() { return disregardedCount; }
}