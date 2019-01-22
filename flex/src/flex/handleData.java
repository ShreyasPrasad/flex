package flex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class handleData {

    private Statement execute;
    private Connection instanceDB;
    private PreparedStatement prep;

    public handleData() {
        try {
            String url = "jdbc:derby://localhost:1527/flexuserdata";
            instanceDB = DriverManager.getConnection(url,"shreyas","shreyas23");
            execute = instanceDB.createStatement();
            prep = instanceDB.prepareStatement("SElECT USERNAME, PASSWORD FROM SHREYAS.FLEXUSERS where USERNAME=? and PASSWORD=?");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUser(String username, String password, String email) {
        try {
            execute.execute("INSERT INTO SHREYAS.FLEXUSERS VALUES('" + username + "','"
                    + password + "','" + email + "')");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean signIn(String username, String password) {
        try {
            prep.setObject(1, username);
            prep.setObject(2, password);
            ResultSet results = prep.executeQuery();
            if (results.next()) {
                
                return true;
            } else {
                
                return false;
            }

        } catch (SQLException e) {
           
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void closeConnection() throws SQLException {
        if (execute != null) {
            execute.close();
        }
        if (instanceDB != null) {
            instanceDB.close();
        }
        if (prep != null) {
            prep.close();
        }
    }
}
