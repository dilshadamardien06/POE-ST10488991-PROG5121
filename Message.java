import javax.swing.JOptionPane;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Message class for QuickChat messaging system
 * Handles message creation, validation, display, and notifications
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
        this.sender = "You"; // Default sender
    }
    
    public Message(String sender) {
        this.sender = sender;
    }
    
    // ===== NOTIFICATION METHODS =====
    
    // Success notification
    private void showSuccessNotification(String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "✅ Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Error notification
    private void showErrorNotification(String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "❌ Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    // Warning notification
    private void showWarningNotification(String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "⚠️ Warning", 
            JOptionPane.WARNING_MESSAGE);
    }
    
    // Message sent notification with details
    public void showMessageSentNotification() {
        String notification = String.format(
            "💬 Message Sent Successfully!\n\n" +
            "📱 To: %s\n" +
            "🆔 Message ID: %s\n" +
            "🔗 Hash: %s\n" +
            "📝 Message: %s\n" +
            "⏰ Time: %s",
            this.recipient,
            this.messageID,
            this.messageHash,
            this.messageContent.length() > 50 ? 
                this.messageContent.substring(0, 50) + "..." : this.messageContent,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );
        
        JOptionPane.showMessageDialog(null, 
            notification, 
            "🚀 Message Delivered", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // New message notification (for incoming messages)
    public void showNewMessageNotification() {
        String notification = String.format(
            "📩 New Message!\n\n" +
            "👤 From: %s\n" +
            "💭 %s",
            this.sender,
            this.messageContent.length() > 30 ? 
                this.messageContent.substring(0, 30) + "..." : this.messageContent
        );
        
        JOptionPane.showMessageDialog(null, 
            notification, 
            "💬 QuickChat", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Confirmation dialog
    private boolean showConfirmationDialog(String question) {
        int response = JOptionPane.showConfirmDialog(null, 
            question, 
            "❓ Confirm Action", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        return response == JOptionPane.YES_OPTION;
    }
    
    // ===== EXISTING MESSAGE METHODS WITH NOTIFICATION ENHANCEMENTS =====
    
    // Check if message ID is valid (not more than 10 characters)
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }
    
    // Check recipient cell number format
    public int checkRecipientCell() {
        if (recipient == null) return -1;
        
        // Remove any spaces or special characters
        String cleanRecipient = recipient.replaceAll("[^0-9+]", "");
        
        // Check if starts with international code and has proper length
        if (cleanRecipient.startsWith("+27") && cleanRecipient.length() == 12) {
            return 1; // Valid
        } else if (cleanRecipient.startsWith("027") && cleanRecipient.length() == 11) {
            return 1; // Valid
        } else {
            return 0; // Invalid
        }
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
    
    // Generate random message ID
    public void generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        
        this.messageID = id.toString();
        System.out.println("Message ID generated: " + this.messageID);
        
        // Show notification for ID generation
        showSuccessNotification("Message ID Generated: " + this.messageID);
    }
    
    // Send message with enhanced notifications
    public String sendMessage() {
        // Check message length
        if (messageContent != null && messageContent.length() > 250) {
            int excessChars = messageContent.length() - 250;
            String errorMsg = "Message exceeds 250 characters by " + excessChars + ", please reduce size.";
            showErrorNotification(errorMsg);
            return errorMsg;
        }
        
        // Check recipient
        int recipientCheck = checkRecipientCell();
        if (recipientCheck != 1) {
            String errorMsg = "Cell phone number incorrectly formatted or does not contain international code. Please correct the number and try again.";
            showErrorNotification(errorMsg);
            return errorMsg;
        }
        
        // Check if message content is empty
        if (messageContent == null || messageContent.trim().isEmpty()) {
            String errorMsg = "Message content cannot be empty.";
            showErrorNotification(errorMsg);
            return errorMsg;
        }
        
        this.isSent = true;
        this.isStored = false;
        
        // Show success notification with message details
        showMessageSentNotification();
        
        return "Message successfully sent.";
    }
    
    // Store message with notification
    public void storeMessage() {
        this.isStored = true;
        this.isSent = false;
        showSuccessNotification("💾 Message stored successfully for later sending.");
    }
    
    // Disregard message with confirmation
    public String disregardMessage() {
        if (showConfirmationDialog("Are you sure you want to disregard this message?")) {
            showWarningNotification("Message disregarded. 🗑️");
            return "Press 0 to delete message.";
        } else {
            showSuccessNotification("Message kept as draft. 📝");
            return "Message kept as draft.";
        }
    }
    
    // Print message details using JOptionPane with enhanced formatting
    public void printMessageDetails() {
        String details = String.format(
            "💬 Message Details\n\n" +
            "🆔 Message ID: %s\n" +
            "🔗 Message Hash: %s\n" +
            "📱 Recipient: %s\n" +
            "📝 Message: %s\n" +
            "📊 Status: %s\n" +
            "⏰ Generated: %s",
            messageID,
            messageHash,
            recipient,
            messageContent,
            (isSent ? "✅ Sent" : (isStored ? "💾 Stored" : "📝 Draft")),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        
        JOptionPane.showMessageDialog(null, details, "📋 Message Information", JOptionPane.INFORMATION_MESSAGE);
        
        // Also print to console for logging
        System.out.println("=== Message Details ===");
        System.out.println("Message ID: " + messageID);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + messageContent);
        System.out.println("Status: " + (isSent ? "SENT" : (isStored ? "STORED" : "DRAFT")));
        System.out.println("======================");
    }
    
    // Return formatted message string
    public String printMessages() {
        return String.format(
            "MessageID: %s | Hash: %s | Recipient: %s | Message: %s | Status: %s",
            messageID,
            messageHash,
            recipient,
            (messageContent != null && messageContent.length() > 50 ? 
                messageContent.substring(0, 50) + "..." : messageContent),
            (isSent ? "SENT" : (isStored ? "STORED" : "DRAFT"))
        );
    }
    
    // Simulate receiving a message (for testing)
    public void simulateIncomingMessage() {
        showNewMessageNotification();
    }
    
    // Validate message before sending
    public boolean validateMessage() {
        if (messageContent == null || messageContent.trim().isEmpty()) {
            showErrorNotification("Message content cannot be empty.");
            return false;
        }
        
        if (messageContent.length() > 250) {
            showErrorNotification("Message exceeds 250 characters.");
            return false;
        }
        
        if (checkRecipientCell() != 1) {
            showErrorNotification("Invalid recipient cell number.");
            return false;
        }
        
        if (messageID == null || !checkMessageID()) {
            showErrorNotification("Invalid message ID.");
            return false;
        }
        
        return true;
    }
    
    // Quick send method with validation
    public boolean quickSend() {
        if (validateMessage()) {
            String result = sendMessage();
            return result.equals("Message successfully sent.");
        }
        return false;
    }
    
    // ===== GETTERS AND SETTERS WITH VALIDATION =====
    
    public void setRecipient(String recipient) {
        this.recipient = recipient;
        
        // Validate recipient when set
        if (recipient != null && !recipient.trim().isEmpty()) {
            int validation = checkRecipientCell();
            if (validation != 1) {
                showWarningNotification("Recipient number may be invalid. Please verify: " + recipient);
            }
        }
    }
    
    public void setMessageContent(String messageContent) {
        if (messageContent.length() > 250) {
            showWarningNotification("Message truncated to 250 characters.");
            this.messageContent = messageContent.substring(0, 250);
        } else {
            this.messageContent = messageContent;
        }
    }
    
    public void setSent(boolean sent) {
        this.isSent = sent;
        if (sent) {
            this.isStored = false;
        }
    }
    
    public void setSender(String sender) {
        this.sender = sender;
    }
    
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageContent() { return messageContent; }
    public boolean isSent() { return isSent; }
    public boolean isStored() { return isStored; }
    public String getSender() { return sender; }
    
    // Utility method to get message summary
    public String getSummary() {
        return String.format("To: %s | Message: %s", 
            recipient, 
            messageContent.length() > 30 ? messageContent.substring(0, 30) + "..." : messageContent
        );
    }
}