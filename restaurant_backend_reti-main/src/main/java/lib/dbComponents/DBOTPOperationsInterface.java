package lib.dbComponents;

/**** Interface for the DB OTP related operations ****/
public interface DBOTPOperationsInterface {

    // Methods
    // Method to create the OTP code
    String generateOTP();

    // Method to save the OTP into the DB for the chosen email the temporary check
    boolean saveOTP(String email, String code);

    // Method to verify the OTP for the chosen email and return true or false
    boolean verifyOTP(String email, String inputCode);

    // Method to clean the OTPs from the otp table that are expired
    void cleanExpiredOTPs();


}
