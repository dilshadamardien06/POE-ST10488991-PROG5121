import javax.swing.JOptionPane;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Message class for QuickChat messaging system
 * Handles message creation, validation, display, and notifications
 * Linked to: QuickChat.java
 */
public class Message {
    private String messageID;
    private String messageHash;
    private String recipient;
    private String messageContent;
    private boolean isSent;
    private boolean isStored;
    private String sender;
    
    public Message() {
        this.sender = "You";
    }
    
    public Message(String sender) {
        this.sender = sender;
    }
    
    // Generate random message ID
    public void generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        
        this.messageID = id.toString();
        System.out.println("🔑 Message ID generated: " + this.messageID);
    }
    
    // Create message hash
    public String createMessageHash(int messageNumber) {
        if (messageID == null || messageContent == null) {
            return "";
        }
        
        // Get first two numbers of message ID
        String firstTwo = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        
        // Get first and last words of message
        String[] words = messageContent.split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        // Remove punctuation from words
        firstWord = firstWord.replaceAll("[^a-zA-Z]", "").toUpperCase();
        lastWord = lastWord.replaceAll("[^a-zA-Z]", "").toUpperCase();
        
        this.messageHash = firstTwo + ":" + messageNumber + ":" + firstWord + lastWord;
        return this.messageHash;
    }
    
    // Send message with validation
    public String sendMessage() {
        // Check message length
        if (messageContent != null && messageContent.length() > 250) {
            int excessChars = messageContent.length() - 250;
            showErrorNotification("Message too long!", "Exceeds 250 characters by " + excessChars + " chars.");
            return "Message exceeds 250 characters by " + excessChars + ", please reduce size.";
        }
        
        // Check recipient
        if (!checkRecipientCell()) {
            showErrorNotification("Invalid Recipient", "Cell number must start with +27/027 and have 9 digits.");
            return "Cell phone number incorrectly formatted.";
        }
        
        // Check if message content is empty
        if (messageContent == null || messageContent.trim().isEmpty()) {
            showErrorNotification("Empty Message", "Message content cannot be empty.");
            return "Message content cannot be empty.";
        }
        
        this.isSent = true;
        this.isStored = false;
        
        // Show success notification
        showMessageSentNotification();
        
        return "Message successfully sent.";
    }
    
    // Store message
    public void storeMessage() {
        this.isStored = true;
        this.isSent = false;
        showSuccessNotification("Message Stored", "💾 Message stored for later sending.");
    }
    
    // Disregard message
    public String disregardMessage() {
        showWarningNotification("Message Disregarded", "🗑️ Message has been discarded.");
        return "Press 0 to delete message.";
    }
    
    // Check recipient cell number format
    public boolean checkRecipientCell() {
        if (recipient == null) return false;
        
        String cleanRecipient = recipient.replaceAll("[^0-9+]", "");
        return (cleanRecipient.startsWith("+27") && cleanRecipient.length() == 12) ||
               (cleanRecipient.startsWith("027") && cleanRecipient.length() == 11);
    }
    
    // Print message details
    public void printMessageDetails() {
        String status = isSent ? "✅ SENT" : (isStored ? "💾 STORED" : "📝 DRAFT");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        String details = 
            "💬 MESSAGE DETAILS\n" +
            "================\n" +
            "🆔 Message ID: " + messageID + "\n" +
            "🔗 Message Hash: " + messageHash + "\n" +
            "📱 Recipient: " + recipient + "\n" +
            "📝 Message: " + messageContent + "\n" +
            "📊 Status: " + status + "\n" +
            "⏰ Time: " + timestamp;
        
        JOptionPane.showMessageDialog(null, details, "📋 Message Information", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("\n" + details);
    }
    
    // Return formatted message string
    public String printMessages() {
        String preview = messageContent.length() > 50 ? messageContent.substring(0, 50) + "..." : messageContent;
        String status = isSent ? "SENT" : (isStored ? "STORED" : "DRAFT");
        
        return String.format("ID: %s | To: %s | %s | [%s]", 
            messageID, recipient, preview, status);
    }
    
    // Notification methods
    private void showSuccessNotification(String title, String message) {
        JOptionPane.showMessageDialog(null, message, "✅ " + title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showErrorNotification(String title, String message) {
        JOptionPane.showMessageDialog(null, message, "❌ " + title, JOptionPane.ERROR_MESSAGE);
    }
    
    private void showWarningNotification(String title, String message) {
        JOptionPane.showMessageDialog(null, message, "⚠️ " + title, JOptionPane.WARNING_MESSAGE);
    }
    
    private void showMessageSentNotification() {
        String notification = 
            "🚀 MESSAGE SENT SUCCESSFULLY!\n\n" +
            "📱 To: " + recipient + "\n" +
            "🆔 ID: " + messageID + "\n" +
            "🔗 Hash: " + messageHash + "\n" +
            "📝 Message: " + (messageContent.length() > 30 ? messageContent.substring(0, 30) + "..." : messageContent) + "\n" +
            "⏰ Time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        JOptionPane.showMessageDialog(null, notification, "💬 Message Delivered", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Getters and setters
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public void setMessageContent(String messageContent) { 
        if (messageContent.length() > 250) {
            this.messageContent = messageContent.substring(0, 250);
            showWarningNotification("Message Truncated", "Message was too long and has been shortened to 250 characters.");
        } else {
            this.messageContent = messageContent;
        }
    }
    public void setSent(boolean sent) { this.isSent = sent; }
    public void setStored(boolean stored) { this.isStored = stored; }
    public void setSender(String sender) { this.sender = sender; }
    
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageContent() { return messageContent; }
    public boolean isSent() { return isSent; }
    public boolean isStored() { return isStored; }
    public String getSender() { return sender; }
}