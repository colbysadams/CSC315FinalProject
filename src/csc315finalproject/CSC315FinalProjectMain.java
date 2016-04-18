/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc315finalproject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author colbysadams
 */
public class CSC315FinalProjectMain
{

    private static Connection connect;
    private static ResultSet resultSet;
    private static Statement statement;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {

        String airportFile = "airports.txt";
//        String airlineFile = args[1];
//        String routeFile = args[2];

        try
        {

            Scanner inFile = new Scanner(new FileReader(airportFile));

            try
            {

                //String Query = "select * from AirPort_T order by AirportCode";
                //String insert = "insert into Airport_T values ('MSY', 'Louis Armstrong','New Orleans', 'USA', 5)";
                Class.forName("com.mysql.jdbc.Driver");

                connect = DriverManager.getConnection("jdbc:mysql://localhost/AirTravel", "CSC315USER", "password");

                statement = connect.createStatement();

                //ResultSet resultSet = statement.executeQuery(Query);
//                while (resultSet.next())
//                {
//
//                    String sname = resultSet.getString("AirportCode");
//
//                    System.out.println(sname);
//                }
            }
            catch (Exception e)
            {
                System.out.println("crap...");
            }

            String FAACode;
            String name;
            String city;
            String country;
            int phoneNumber = 1234567890;

            inFile.useDelimiter(",");

            while (inFile.hasNext())
            {
                inFile.next();
                name = inFile.next();
                name = name.replace('"', '\'');
                city = inFile.next();
                city = city.replace('"', '\'');
                country = inFile.next();
                country = country.replace('"', '\'');
                FAACode = inFile.next();
                FAACode = FAACode.replace('"', '\'');
                if (FAACode.equals("''"))
                {
                    inFile.nextLine();
                    continue;
                }
                try
                {
                    statement.execute(("insert into Airport_T values (" + FAACode
                            + ", " + name + ", " + city + ", " + country
                            + ", " + phoneNumber + ");"));
                }
                catch (SQLException e)
                {

                }
                phoneNumber++;
                inFile.nextLine();
            }

            inFile = new Scanner(new FileReader("airlines.txt"));
            inFile.useDelimiter(",");
            String callSign;
            while (inFile.hasNext())
            {
                inFile.next();
                name = inFile.next();
                name = name.replace('"', '\'');
                inFile.next();
                inFile.next();
                FAACode = inFile.next();
                FAACode = FAACode.replace('"', '\'');
                callSign = inFile.next();
                callSign = callSign.replace('"', '\'');
                country = inFile.next();
                country = country.replace('"', '\'');

                if (FAACode.equals("''"))
                {
                    inFile.nextLine();
                    continue;
                }
                try
                {
                    statement.execute(("insert into AirlineCarrier_T "
                            + "(AirlineName, AirlineCountry, PhoneNumber, AirlineCode, AirlineCallSign) "
                            + "values (" + name + ", " + country + ", " + phoneNumber
                            + ", " + FAACode + ", " + callSign + ");"));
                }
                catch (SQLException ex)
                {
                    //If already in database, skip
                }

                phoneNumber++;
                inFile.nextLine();
            }

        }
        catch (FileNotFoundException e)
        {

        }

        try
        {
            resultSet.close();
            statement.close();
            connect.close();
        }
        catch (Exception e)
        {
        }

    }
}
