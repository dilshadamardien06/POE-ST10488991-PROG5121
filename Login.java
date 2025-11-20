import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Login class - Handles user registration and authentication
 * Linked to: poe.java (main application)
 */
public class Login {
    private String storedUsername;
    private String storedPassword;
    private String storedCellPhone;
    
    // Check if username contains underscore and is no more than 5 characters
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }
    
    // Check password meets complexity requirements
    public boolean checkPasswordComplexity(String password) {
        // At least 8 characters
        if (password.length() < 8) return false;
        
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        
        return hasCapital && hasNumber && hasSpecial;
    }
    
    // Check cell phone number format
    public boolean checkCellPhoneNumber(String cellPhone) {
        // Regex for South African numbers with country code (+27 or 027) followed by 9 digits
        String regex = "^(\\+27|027)[0-9]{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cellPhone);
        return matcher.matches();
    }
    
    // Register user with validation
    public String registerUser(String username, String password, String cellPhone) {
        StringBuilder message = new StringBuilder();
        boolean usernameValid = false;
        boolean passwordValid = false;
        boolean cellphoneValid = false;
        
        // Check username
        if (!checkUserName(username)) {
            message.append("❌ Username is not correctly formatted. Must contain '_' and be ≤5 characters.\n");
        } else {
            message.append("✅ Username successfully captured.\n");
            usernameValid = true;
        }
        
        // Check password
        if (!checkPasswordComplexity(password)) {
            message.append("❌ Password must have: 8+ chars, capital letter, number, and special character.\n");
        } else {
            message.append("✅ Password successfully captured.\n");
            passwordValid = true;
        }
        
        // Check cell phone
        if (!checkCellPhoneNumber(cellPhone)) {
            message.append("❌ Cell phone number must start with +27/027 and have 9 digits after.\n");
        } else {
            message.append("✅ Cell phone number successfully added.\n");
            cellphoneValid = true;
        }
        
        // If all validations passed, store the credentials
        if (usernameValid && passwordValid && cellphoneValid) {
            this.storedUsername = username;
            this.storedPassword = password;
            this.storedCellPhone = cellPhone;
            return "User registered successfully.";
        }
        
        return message.toString();
    }
    
    // Verify login credentials
    public boolean loginUser(String username, String password) {
        return username.equals(storedUsername) && password.equals(storedPassword);
    }
    
    // Return login status message
    public String returnLoginStatus(boolean loginStatus, String firstName, String lastName) {
        if (loginStatus) {
            return "🎉 Welcome " + firstName + " " + lastName + ", it is great to see you again!";
        } else {
            return "❌ Username or password incorrect, please try again.";
        }
    }
    
    // Getters for testing purposes
    public String getStoredUsername() {
        return storedUsername;
    }
    
    public String getStoredPassword() {
        return storedPassword;
    }
    
    public String getStoredCellPhone() {
        return storedCellPhone;
    }
}