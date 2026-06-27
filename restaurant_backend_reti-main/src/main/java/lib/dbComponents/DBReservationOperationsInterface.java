package lib.dbComponents;

import java.sql.Date;
import java.time.LocalDate;

/******** Interface for the ReservtionOperations class ********/
public interface DBReservationOperationsInterface {

    // Methods
    // Method to collect the total number of customers
    int getTotalNumberOfCustomers(LocalDate date);

    // Method to insert a new reservation into the database
    void insertReservation(String reservationName, int nSeats, String date);

    // Method to delete a reservation from the database
    void deleteReservation(String reservationName);

    // Method to retrieve all the reservations to eventually delete them
    String[] getReservations();

    // Method to collect the total number of orders
    int getTotalReservationsNumber(LocalDate date);

    // Method to retrieve reservations from the database
    Object[][] getReservationData();

    // Method to retrieve the current data's reservations from the database
    Object[][] getReservationOnCurrentData();

    // Method to retrieve data from a row to enable the dynamic refresh of the reservations table (adding a reservation)
    int getDataFromRows(String columnName, String tableName, String reservationName, Date sqlDate);
}