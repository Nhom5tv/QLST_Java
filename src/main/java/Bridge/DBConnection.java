/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bridge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author DUNG LE
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/sieuthi";
    private static final String User = "root";
    private static final String Password = "";
    public  static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL , User , Password);
    }
}
