/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc315finalproject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author colbysadams
 */
public class CSC315FinalProjectMain
{

    private static Connection connect;
    private static Statement statement;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {

        Scanner inFile;

        try
        {

            Class.forName("com.mysql.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://localhost/AirTravel", "CSC315USER", "password");

            statement = connect.createStatement();

            statement.execute("delete from ReservationFlight_T;");
            statement.execute("delete from FrequentFlyerMembership_T;");

            statement.execute("delete from FlightInstance_T;");
            statement.execute("delete from FlightPath_T;");
            statement.execute("delete from PaymentMethod_T;");
            statement.execute("delete from Plane_T;");
            statement.execute("delete from Preference_T;");

            statement.execute("delete from Airport_T;");
            statement.execute("delete from Reservation_T;");
            statement.execute("delete from AirlineCarrier_T;");
            statement.execute("delete from Customer_T;");

            inFile = new Scanner(new FileReader("buildDatabase.txt"));
            inFile.useDelimiter(",");

            //PrintWriter writer = new PrintWriter(new FileWriter("build.txt"));
            while (inFile.hasNextLine())

                statement.addBatch(inFile.nextLine());

            statement.executeBatch();
        }
        catch (FileNotFoundException | SQLException | ClassNotFoundException e)
        {
            Logger.getLogger(CSC315FinalProjectMain.class.getName()).log(Level.SEVERE, null, e);
        }

        try
        {

            statement.close();
            connect.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CSC315FinalProjectMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
