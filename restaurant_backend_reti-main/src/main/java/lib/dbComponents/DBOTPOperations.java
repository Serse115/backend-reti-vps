package lib.dbComponents;

import java.sql.*;
import java.time.LocalDateTime;

/******** CLASS FOR DATABASE OTP RELATED OPERATIONS ********/
public class DBOTPOperations implements DBOTPOperationsInterface {

    /**** Fields ****/
    // Variables
    private final DBConnectionClassInterface connector;         // Connector object


    /**** Constructors ****/
    public DBOTPOperations(final DBConnectionClassInterface connector) {
        this.connector = connector;
    }


    /**** Methods ****/
    // Method to create the OTP code made by 6 numerical digits
    public String generateOTP() {

        int code = (int)(Math.random() * 900000) + 100000; // Genera tra 100000 e 999999
        return String.valueOf(code);
    }

    // Method to save the OTP, checking if it exists already and in case switch it and update its duration
    public boolean saveOTP(String email, String code) {

        Connection conn = this.connector.returnConnection();
        if (conn == null) return false;

        // Il codice scade tra 5 minuti
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);
        Timestamp otpExpiry = Timestamp.valueOf(expiry);

        // Usiamo ON DUPLICATE KEY UPDATE per MySQL così gestiamo sia insert che update in una query
        String sql = """
            INSERT INTO otp_codes (email, otp_code, expiry_time)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE otp_code = VALUES(otp_code), expiry_time = VALUES(expiry_time)
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, code);
            pstmt.setTimestamp(3, otpExpiry);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to verify the OTP and return if the verification turned out correct or not or if it's expired
    public boolean verifyOTP(String email, String inputCode) {

        Connection conn = this.connector.returnConnection();
        if (conn == null) return false;

        String sql = "SELECT otp_code, expiry_time FROM otp_codes WHERE email = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbCode = rs.getString("otp_code");
                Timestamp expiry = rs.getTimestamp("expiry_time");

                // Controllo 1: Il codice corrisponde?
                // Controllo 2: L'ora attuale è precedente alla scadenza?
                if (dbCode.equals(inputCode) && expiry.after(new Timestamp(System.currentTimeMillis()))) {
                    // Una volta verificato, cancelliamo l'OTP per sicurezza (usa-e-getta)
                    deleteOTP(email);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to clean the expired OTPs from the DB
    public void cleanExpiredOTPs() {
        String sql = "DELETE FROM otp_codes WHERE expiry_time < CURRENT_TIMESTAMP";
        try (PreparedStatement pstmt = this.connector.returnConnection().prepareStatement(sql)) {
            int deleted = pstmt.executeUpdate();
            if (deleted > 0) {
                System.out.println("Cleaned " + deleted + " expired OTP codes from database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sub method to delete the OTPs linked to the chosen email
    private void deleteOTP(String email) {

        String sql = "DELETE FROM otp_codes WHERE email = ?";
        try (PreparedStatement pstmt = this.connector.returnConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
