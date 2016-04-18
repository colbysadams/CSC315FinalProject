/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc315finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author colbysadams
 */
public class CSC315FinalProjectMain
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {

        try
        {
            //System.out.println("0");
            String Query = "select * from AirPort_T order by AirportCode";
            //System.out.println("1");
            String insert = "insert into Airport_T values ('MSY', 'Louis Armstrong','New Orleans', 'USA', 5)";
            //System.out.println("2");
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("3");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/AirTravel", "CSC315USER", "password");
            System.out.println("4");
            Statement statement = connect.createStatement();
            System.out.println("5");
            //st.execute(insert);
            System.out.println("6");
            ResultSet resultSet = statement.executeQuery(Query);

            while (resultSet.next())
            {

                String sname = resultSet.getString("AirportCode");

                System.out.println(sname);
            }
            resultSet.close();
            statement.close();
            connect.close();

        }
        catch (Exception e)
        {
            System.out.println("crap...");
        }

    }
}
