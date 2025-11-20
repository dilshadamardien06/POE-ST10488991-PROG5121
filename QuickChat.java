import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * QuickChat class - Main messaging system with arrays and JSON storage
 * Handles the chat interface and message management
 */
public class QuickChat {
    private List<Message> messages = new ArrayList<>();
    private int totalMessagesSent = 0;
    // Arrays for Part 3 requirements
    private String[] sentMessages;
    private String[] disregardedMessages;
    private String[] storedMessages;
    private String[] messageHashes;
    private String[] messageIDs;
    
    private int sentCount = 0;
    private int disregardedCount = 0;
    private int storedCount = 0;
    
    public QuickChat() {
        // Initialize arrays with reasonable size
        sentMessages = new String[100];
        disregardedMessages = new String[100];
        storedMessages = new String[100];
        messageHashes = new String[100];
        messageIDs = new String[100];
    }
    
    public void startMessagingSystem(Scanner scanner) {
        // Load stored messages from JSON
        loadStoredMessagesFromJSON();
        
        // Welcome message with JOptionPane as required
        showColorfulNotification("🚀 Welcome to QuickChat!", 
            "Welcome to QuickChat! Let's start messaging.", "info");
        
        // Main menu loop
        boolean running = true;
        while (running) {
            String menuChoice = showMenuDialog();
            
            if (menuChoice == null) {
                running = false;
                continue;
            }
            
            switch (menuChoice) {
                case "1":
                    sendMessages(scanner);
                    break;
                case "2":
                    showRecentMessages();
                    break;
                case "3":
                    displayMessageReport();
                    break;
                case "4":
                    searchFeatures(scanner);
                    break;
                case "5":
                    running = false;
                    showColorfulNotification("👋 Goodbye!", 
                        "Thank you for using QuickChat!", "success");
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    break;
                default:
                    showColorfulNotification("❌ Invalid Option", 
                        "Please choose a valid option (1-5)", "error");
            }
        }
    }
    
    private String showMenuDialog() {
        String menu = "<html><div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 20px; border-radius: 10px; color: white; text-align: center;'>" +
                     "<h2 style='margin: 0;'>🚀 QuickChat Menu</h2>" +
                     "</div>" +
                     "<div style='padding: 15px; background: #f8f9fa; border-radius: 5px; margin-top: 10px;'>" +
                     "<p style='color: #2c3e50; margin: 10px 0;'><b>1)</b> 📤 Send Messages</p>" +
                     "<p style='color: #2c3e50; margin: 10px 0;'><b>2)</b> 📨 Show Recent Messages</p>" +
                     "<p style='color: #2c3e50; margin: 10px 0;'><b>3)</b> 📊 Display Message Report</p>" +
                     "<p style='color: #2c3e50; margin: 10px 0;'><b>4)</b> 🔍 Search Features</p>" +
                     "<p style='color: #2c3e50; margin: 10px 0;'><b>5)</b> 🚪 Quit</p>" +
                     "</div>" +
                     "</html>";
        
        return JOptionPane.showInputDialog(null, menu, "💬 QuickChat System", JOptionPane.QUESTION_MESSAGE);
    }
    
    private void sendMessages(Scanner scanner) {
        String numMessagesStr = JOptionPane.showInputDialog(null, 
            "How many messages do you wish to send?", "📤 Send Messages", JOptionPane.QUESTION_MESSAGE);
        
        if (numMessagesStr == null) return;
        
        try {
            int numMessages = Integer.parseInt(numMessagesStr);
            
            if (numMessages <= 0) {
                showColorfulNotification("⚠️ Invalid Number", "Please enter a positive number", "warning");
                return;
            }
            
            showColorfulNotification("📝 Preparing Messages", 
                "Preparing to send " + numMessages + " message(s)", "info");
            
            // For loop as required - runs for assigned number of messages
            for (int i = 0; i < numMessages; i++) {
                System.out.println("\n=== Message " + (i + 1) + " of " + numMessages + " ===");
                
                Message message = new Message();
                
                // Set recipient
                System.out.print("Enter recipient cell number: ");
                String recipient = scanner.nextLine();
                message.setRecipient(recipient);
                
                // Set message content
                System.out.print("Enter your message (max 250 characters): ");
                String messageContent = scanner.nextLine();
                message.setMessageContent(messageContent);
                
                // Generate message ID and hash using loop counter
                message.generateMessageID();
                message.createMessageHash(i + 1); // Using loop counter as required
                
                // Display message options
                int choice = displayMessageOptions(scanner);
                
                // Process user choice and populate arrays
                processMessageChoice(choice, message, i + 1);
            }
            
            // Save to JSON file
            saveMessagesToJSON();
            
            showColorfulNotification("✅ Session Complete", 
                "Total messages sent: " + totalMessagesSent + "\n" +
                "Stored messages: " + storedCount + "\n" +
                "Disregarded messages: " + disregardedCount, "success");
                
        } catch (NumberFormatException e) {
            showColorfulNotification("❌ Invalid Input", "Please enter a valid number", "error");
        }
    }
    
    private int displayMessageOptions(Scanner scanner) {
        System.out.println("\n=== Message Options ===");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Choose an option: ");
        
        return scanner.nextInt();
    }
    
    private void processMessageChoice(int choice, Message message, int messageNumber) {
        switch (choice) {
            case 1: // Send message
                String sendResult = message.sendMessage();
                System.out.println(sendResult);
                
                if (sendResult.equals("Message successfully sent.")) {
                    message.setSent(true);
                    messages.add(message);
                    totalMessagesSent++;
                    // Add to sent messages array
                    if (sentCount < sentMessages.length) {
                        sentMessages[sentCount] = message.getMessageContent();
                        messageHashes[sentCount] = message.getMessageHash();
                        messageIDs[sentCount] = message.getMessageID();
                        sentCount++;
                    }
                    
                    // Display full message details
                    message.printMessageDetails();
                }
                break;
                
            case 2: // Disregard message
                String disregardResult = message.disregardMessage();
                System.out.println(disregardResult);
                
                // Add to disregarded messages array
                if (disregardedCount < disregardedMessages.length) {
                    disregardedMessages[disregardedCount] = message.getMessageContent();
                    disregardedCount++;
                }
                break;
                
            case 3: // Store message
                message.storeMessage();
                messages.add(message);
                
                // Add to stored messages array
                if (storedCount < storedMessages.length) {
                    storedMessages[storedCount] = message.getMessageContent();
                    storedCount++;
                }
                
                System.out.println("Message successfully stored.");
                break;
                
            default:
                System.out.println("Invalid option. Message disregarded.");
        }
    }
    
    // ===== PART 3 FEATURES =====
    
    // Display all sent messages
    private void showRecentMessages() {
        if (sentCount == 0) {
            showColorfulNotification("📨 No Messages", "No sent messages found.", "info");
            return;
        }
        
        StringBuilder messageList = new StringBuilder();
        messageList.append("<html><div style='background: #f8f9fa; padding: 15px; border-radius: 5px;'>");
        messageList.append("<h3 style='color: #2c3e50;'>📨 Sent Messages</h3>");
        
        for (int i = 0; i < sentCount; i++) {
            messageList.append(String.format("<p style='color: #34495e; margin: 5px 0;'><b>%d.</b> %s</p>", 
                i + 1, sentMessages[i]));
        }
        messageList.append("</div></html>");
        
        JOptionPane.showMessageDialog(null, messageList.toString(), "📊 Sent Messages", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Display comprehensive report
    private void displayMessageReport() {
        StringBuilder report = new StringBuilder();
        report.append("<html><div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 15px; border-radius: 10px; color: white; text-align: center;'>");
        report.append("<h2 style='margin: 0;'>📊 Message Report</h2>");
        report.append("</div>");
        report.append("<div style='padding: 15px; background: #f8f9fa; border-radius: 5px; margin-top: 10px;'>");
        
        report.append(String.format("<p style='color: #27ae60;'><b>✅ Sent Messages:</b> %d</p>", sentCount));
        report.append(String.format("<p style='color: #3498db;'><b>💾 Stored Messages:</b> %d</p>", storedCount));
        report.append(String.format("<p style='color: #e74c3c;'><b>❌ Disregarded Messages:</b> %d</p>", disregardedCount));
        report.append(String.format("<p style='color: #f39c12;'><b>📈 Total Processed:</b> %d</p>", sentCount + storedCount + disregardedCount));
        
        // Show longest message
        String longestMessage = findLongestMessage();
        if (longestMessage != null) {
            report.append(String.format("<p style='color: #9b59b6;'><b>📏 Longest Message:</b> %s</p>", 
                longestMessage.length() > 50 ? longestMessage.substring(0, 50) + "..." : longestMessage));
        }
        
        report.append("</div></html>");
        
        JOptionPane.showMessageDialog(null, report.toString(), "📈 System Report", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Search features
    private void searchFeatures(Scanner scanner) {
        String searchMenu = "<html><div style='background: #f8f9fa; padding: 15px; border-radius: 5px;'>" +
                           "<h3 style='color: #2c3e50;'>🔍 Search Features</h3>" +
                           "<p style='color: #34495e;'><b>1)</b> Search by Message ID</p>" +
                           "<p style='color: #34495e;'><b>2)</b> Search by Recipient</p>" +
                           "<p style='color: #34495e;'><b>3)</b> Delete by Message Hash</p>" +
                           "<p style='color: #34495e;'><b>4)</b> Back to Main Menu</p>" +
                           "</div></html>";
        
        String choice = JOptionPane.showInputDialog(null, searchMenu, "🔍 Search Options", JOptionPane.QUESTION_MESSAGE);
        
        if (choice == null) return;
        
        switch (choice) {
            case "1":
                searchByMessageID(scanner);
                break;
            case "2":
                searchByRecipient(scanner);
                break;
            case "3":
                deleteByMessageHash(scanner);
                break;
            case "4":
                return;
            default:
                showColorfulNotification("❌ Invalid Option", "Please choose 1-4", "error");
        }
    }
    
    private void searchByMessageID(Scanner scanner) {
        String searchID = JOptionPane.showInputDialog(null, 
            "Enter Message ID to search:", "🔍 Search by ID", JOptionPane.QUESTION_MESSAGE);
        
        if (searchID == null) return;
        
        StringBuilder results = new StringBuilder();
        results.append("<html><div style='background: #f8f9fa; padding: 15px; border-radius: 5px;'>");
        results.append("<h4 style='color: #2c3e50;'>Search Results for ID: ").append(searchID).append("</h4>");
        
        boolean found = false;
        for (Message msg : messages) {
            if (msg.getMessageID() != null && msg.getMessageID().equals(searchID)) {
                results.append(String.format("<p style='color: #27ae60;'><b>📱 Recipient:</b> %s</p>", msg.getRecipient()));
                results.append(String.format("<p style='color: #3498db;'><b>💬 Message:</b> %s</p>", msg.getMessageContent()));
                found = true;
                break;
            }
        }
        
        if (!found) {
            results.append("<p style='color: #e74c3c;'>No messages found with that ID.</p>");
        }
        
        results.append("</div></html>");
        JOptionPane.showMessageDialog(null, results.toString(), "🔍 Search Results", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void searchByRecipient(Scanner scanner) {
        String recipient = JOptionPane.showInputDialog(null, 
            "Enter recipient number to search:", "🔍 Search by Recipient", JOptionPane.QUESTION_MESSAGE);
        
        if (recipient == null) return;
        
        StringBuilder results = new StringBuilder();
        results.append("<html><div style='background: #f8f9fa; padding: 15px; border-radius: 5px;'>");
        results.append("<h4 style='color: #2c3e50;'>Messages for: ").append(recipient).append("</h4>");
        
        int count = 0;
        for (Message msg : messages) {
            if (msg.getRecipient() != null && msg.getRecipient().contains(recipient)) {
                count++;
                results.append(String.format("<p style='color: #34495e; margin: 8px 0; padding: 8px; background: #e8f4fd; border-radius: 3px;'>" +
                    "<b>%d.</b> %s</p>", count, msg.getMessageContent()));
            }
        }
        
        if (count == 0) {
            results.append("<p style='color: #e74c3c;'>No messages found for this recipient.</p>");
        }
        
        results.append("</div></html>");
        JOptionPane.showMessageDialog(null, results.toString(), "🔍 Recipient Messages", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteByMessageHash(Scanner scanner) {
        String hash = JOptionPane.showInputDialog(null, 
            "Enter Message Hash to delete:", "🗑️ Delete Message", JOptionPane.QUESTION_MESSAGE);
        
        if (hash == null) return;
        
        boolean deleted = false;
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            if (msg.getMessageHash() != null && msg.getMessageHash().equals(hash)) {
                String messageContent = msg.getMessageContent();
                messages.remove(i);
                
                // Remove from arrays
                for (int j = 0; j < sentCount; j++) {
                    if (sentMessages[j] != null && sentMessages[j].equals(messageContent)) {
                        sentMessages[j] = null;
                        break;
                    }
                }
                
                showColorfulNotification("✅ Message Deleted", 
                    "Message \"" + (messageContent.length() > 30 ? messageContent.substring(0, 30) + "..." : messageContent) + 
                    "\" successfully deleted.", "success");
                deleted = true;
                break;
            }
        }
        
        if (!deleted) {
            showColorfulNotification("❌ Not Found", "No message found with that hash.", "error");
        }
    }
    
    // ===== UTILITY METHODS =====
    
    private String findLongestMessage() {
        String longest = "";
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i] != null && sentMessages[i].length() > longest.length()) {
                longest = sentMessages[i];
            }
        }
        return longest.isEmpty() ? null : longest;
    }
    
    private void showColorfulNotification(String title, String message, String type) {
        String color = "#3498db"; // default blue
        String emoji = "💡";
        
        switch (type.toLowerCase()) {
            case "success":
                color = "#27ae60";
                emoji = "✅";
                break;
            case "error":
                color = "#e74c3c";
                emoji = "❌";
                break;
            case "warning":
                color = "#f39c12";
                emoji = "⚠️";
                break;
        }
        
        String htmlMessage = String.format(
            "<html><div style='background: %s; color: white; padding: 15px; border-radius: 8px; text-align: center;'>" +
            "<h3 style='margin: 0;'>%s %s</h3>" +
            "<p style='margin: 10px 0 0 0;'>%s</p>" +
            "</div></html>",
            color, emoji, title, message
        );
        
        JOptionPane.showMessageDialog(null, htmlMessage, "💬 QuickChat", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // ===== JSON STORAGE METHODS =====
    
    private void saveMessagesToJSON() {
        try {
            JSONArray jsonArray = new JSONArray();
            
            for (Message message : messages) {
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put("messageID", message.getMessageID());
                jsonMessage.put("messageHash", message.getMessageHash());
                jsonMessage.put("recipient", message.getRecipient());
                jsonMessage.put("messageContent", message.getMessageContent());
                jsonMessage.put("isSent", message.isSent());
                jsonMessage.put("isStored", message.isStored());
                jsonMessage.put("sender", message.getSender());
                
                jsonArray.put(jsonMessage);
            }
            
            try (FileWriter file = new FileWriter("messages.json")) {
                file.write(jsonArray.toString(4)); // Indented JSON
                System.out.println("Messages saved to messages.json");
            }
            
        } catch (IOException e) {
            System.out.println("Error saving messages to JSON: " + e.getMessage());
        }
    }
    
    private void loadStoredMessagesFromJSON() {
        try {
            if (!Files.exists(Paths.get("messages.json"))) {
                return;
            }
            
            String content = new String(Files.readAllBytes(Paths.get("messages.json")));
            JSONArray jsonArray = new JSONArray(content);
            
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonMessage = jsonArray.getJSONObject(i);
                Message message = new Message();
                
                message.setRecipient(jsonMessage.getString("recipient"));
                message.setMessageContent(jsonMessage.getString("messageContent"));
                // Note: We can't regenerate the same IDs, so we'll create new ones
                message.generateMessageID();
                message.createMessageHash(i + 1);
                message.setSent(jsonMessage.getBoolean("isSent"));
                message.setStored(jsonMessage.getBoolean("isStored"));
                
                messages.add(message);
                
                // Add to appropriate arrays
                if (message.isSent() && sentCount < sentMessages.length) {
                    sentMessages[sentCount] = message.getMessageContent();
                    sentCount++;
                } else if (message.isStored() && storedCount < storedMessages.length) {
                    storedMessages[storedCount] = message.getMessageContent();
                    storedCount++;
                }
            }
            
            System.out.println("Loaded " + jsonArray.length() + " messages from JSON");
            
        } catch (IOException e) {
            System.out.println("Error loading messages from JSON: " + e.getMessage());
        }
    }
    
    // ===== GETTERS FOR TESTING =====
    
    public int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public String[] getSentMessages() { return sentMessages; }
    public String[] getDisregardedMessages() { return disregardedMessages; }
    public String[] getStoredMessages() { return storedMessages; }
    public String[] getMessageHashes() { return messageHashes; }
    public String[] getMessageIDs() { return messageIDs; }
    public int getSentCount() { return sentCount; }
    public int getDisregardedCount() { return disregardedCount; }
    public int getStoredCount() { return storedCount; }

    // Simple Message implementation used by QuickChat so instances can be created
    public static class Message {
        private String recipient;
        private String messageContent;
        private String messageID;
        private String messageHash;
        private String sender = "QuickChatUser";
        private boolean isSent = false;
        private boolean isStored = false;

        public Message() { }

        public void setRecipient(String recipient) { this.recipient = recipient; }
        public void setMessageContent(String messageContent) { this.messageContent = messageContent; }
        public void generateMessageID() { this.messageID = java.util.UUID.randomUUID().toString(); }
        public void createMessageHash(int seed) {
            int base = (messageContent != null) ? messageContent.hashCode() : 0;
            this.messageHash = Integer.toHexString(base ^ seed);
        }
        public String sendMessage() {
            this.isSent = true;
            return "Message successfully sent.";
        }
        public String disregardMessage() {
            this.isSent = false;
            this.isStored = false;
            return "Message disregarded.";
        }
        public void storeMessage() { this.isStored = true; }
        public void setSent(boolean sent) { this.isSent = sent; }
        public void setStored(boolean stored) { this.isStored = stored; }
        public boolean isSent() { return isSent; }
        public boolean isStored() { return isStored; }
        public String getMessageContent() { return messageContent; }
        public String getMessageHash() { return messageHash; }
        public String getMessageID() { return messageID; }
        public String getRecipient() { return recipient; }
        public String getSender() { return sender; }
        public void printMessageDetails() {
            System.out.println("Message ID: " + messageID);
            System.out.println("Message Hash: " + messageHash);
            System.out.println("Recipient: " + recipient);
            System.out.println("Content: " + messageContent);
            System.out.println("Sent: " + isSent + ", Stored: " + isStored);
        }
    }
}