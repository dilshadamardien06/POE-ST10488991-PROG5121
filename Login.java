
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Login class - Handles user registration and authentication
 */
class Login {
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
        
        // Contains capital letter
        boolean hasCapital = false;
        // Contains number
        boolean hasNumber = false;
        // Contains special character
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
        
        // Check username
        if (!checkUserName(username)) {
            message.append("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.\n");
        } else {
            message.append("Username successfully captured.\n");
        }
        
        // Check password
        if (!checkPasswordComplexity(password)) {
            message.append("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.\n");
        } else {
            message.append("Password successfully captured.\n");
        }
        
        // Check cell phone
        if (!checkCellPhoneNumber(cellPhone)) {
            message.append("Cell phone number incorrectly formatted or does not contain international code.\n");
        } else {
            message.append("Cell phone number successfully added.\n");
        }
        
        // If all validations passed, store the credentials
        if (checkUserName(username) && checkPasswordComplexity(password) && checkCellPhoneNumber(cellPhone)) {
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
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
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

