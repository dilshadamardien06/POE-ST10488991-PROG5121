/**
 * Unit tests for the Login class
 * Tests all methods with the specified test data
 */
public class LoginTest {
    Login login = new Login();
    
    // Test username validation
    @Test
    public void testCheckUserNameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kvl_1"));
    }
        
                    private void assertTrue(boolean checkUserName) {
               
                throw new UnsupportedOperationException("Unimplemented method 'assertTrue'");
            }
        
            @Test
    public void testCheckUserNameIncorrectlyFormatted() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
            }
            
            private void assertFalse(boolean checkUserName) {
                       
                        throw new UnsupportedOperationException("Unimplemented method 'assertFalse'");
                    }
            // Test password complexity
    @Test
    public void testCheckPasswordComplexityValid() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }
    
    @Test
    public void testCheckPasswordComplexityInvalid() {
        assertTrue(login.checkPasswordComplexity("password"));
    }
    
    // Test cell phone validation
    @Test
    public void testCheckCellPhoneNumberValid() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }
    
    @Test
    public void testCheckCellPhoneNumberInvalid() {
        assertTrue(login.checkCellPhoneNumber("08966553"));
    }
    
    // Test registration process
    @Test
    public void testRegisterUserSuccessful() {
        String result = login.registerUser("kvl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("User registered successfully.", result);
            }
            
            private void assertEquals(String string, String result) {
              
                throw new UnsupportedOperationException("Unimplemented method 'assertEquals'");
            }
        
            @Test
    public void testRegisterUserFailed() {
        String result = login.registerUser("kyle!!!!!!!", "password", "08966553");
        assertTrue(result.contains("Username is not correctly formatted"));
        assertTrue(result.contains("Password is not correctly formatted"));
        assertTrue(result.contains("Cell phone number incorrectly formatted"));
    }
    
    // Test login functionality
    @Test
    public void testLoginUserSuccessful() {
        login.registerUser("kvl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kvl_1", "Ch&&sec@ke99!"));
    }
    
    @Test
    public void testLoginUserFailed() {
        login.registerUser("kvl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("wronguser", "wrongpass"));
    }
    
    // Test login status messages
    @Test
    public void testReturnLoginStatusSuccessful() {
        String result = login.returnLoginStatus(true, "Kyle", "Viljoen");
        assertEquals("Welcome Kyle, Viljoen it is great to see you again.", result);
    }
    
    @Test
    public void testReturnLoginStatusFailed() {
        String result = login.returnLoginStatus(false, "Kyle", "Viljoen");
        assertEquals("Username or password incorrect, please try again.", result);
    }
}