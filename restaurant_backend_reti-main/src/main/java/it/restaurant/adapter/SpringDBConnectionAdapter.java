package it.restaurant.adapter;

import lib.dbComponents.DBConnectionClassInterface;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SpringDBConnectionAdapter implements DBConnectionClassInterface {


    /**** Fields ****/
    private final DataSource dataSource;



    /**** Constructors ****/
    public SpringDBConnectionAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    
    /**** Methods ****/
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public boolean connectToDB(String user, String password) {
        return false;
    }

    @Override
    public Connection returnConnection() {
        return null;
    }

    @Override
    public void closeConnectionToDB() {

    }

    @Override
    public boolean checkServerConnection(String user, String password) {
        return false;
    }
}
