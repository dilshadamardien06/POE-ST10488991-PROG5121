
   
    import java.util.Scanner;
    import java.util.regex.Pattern;
    import java.util.regex.Matcher;
    
    /**
     * PROG5121 POE Part 1 - Registration and Login System
     * 
     * This implementation follows all requirements including:
     * - Username validation (underscore, max 5 chars)
     * - Password complexity validation
     * - South African cell phone validation with regex (AI-generated)
     * - All required methods in the Login class
     * - Unit testing ready structure
     * 
     * AI Tool used for phone regex: ChatGPT
     * Reference: OpenAI. (2023). ChatGPT (Mar 14 version) [Large language model]. https://chat.openai.com/chat
     */
    
    // Main class to run the application
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
            
            // If registration was successful, proceed to login
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
            }
            
            scanner.close();
        }
    }
    
    // Login class with all required methods
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
        
        // Check cell phone number format (using AI-generated regex)
        public boolean checkCellPhoneNumber(String cellPhone) {
            // Regex generated with assistance from ChatGPT
            // Validates South African numbers with country code (+27 or 027) followed by 9 digits
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
    
    
