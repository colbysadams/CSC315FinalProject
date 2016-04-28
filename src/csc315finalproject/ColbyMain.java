/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc315finalproject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author colbysadams
 */
public class ColbyMain
{

    private static Connection connect;
    private static Statement statement;
    private static String databaseName;
    private static String statementString;
    private static String userName;
    private static String password;
    private static String serverURL;
    private static String hostName;
    private static String dataFileString;
    private static String tableFileString;
    private static String queryFileString;
    private static int pageSize;

    public static void main(String[] args) throws IOException, SQLException
    {

        Scanner scan = null;
        databaseName = "AIRTRAVEL";
        serverURL = "jdbc:mysql://";
        tableFileString = "tables.txt";
        queryFileString = "queries.txt";
        dataFileString = "data.txt";
        pageSize = 200;
        boolean connected = false;

        /*
         * Take Argument for dataFile from CommandLine
         *
         */
        if (args.length != 3)
        {
            System.out.println("incorrect arguments: <table file> <data file> <query file>");
            System.exit(-1);
        }

        tableFileString = args[0];
        dataFileString = args[1];
        queryFileString = args[2];

        //Paths dataFilePath = Paths.

        /*
         * uncomment "do" and "while" once prompts are in place
         */
        scan = new Scanner(System.in);
        do
        {

            /*
             * prompt user for serverURL, EX (localhost)
             */
            System.out.print("Enter MySQL Server Host (example: localhost): ");

            hostName = scan.nextLine();

            /*
             * get userName from user
             */
            System.out.print("Enter User Name for Server: ");
            userName = scan.nextLine();

            /*
             * get password from user
             */
            System.out.print("Enter password: ");
            password = scan.nextLine();

            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("Connecting to server...");
                connect = DriverManager.getConnection(serverURL + hostName, userName, password);
                System.out.println("Connected");

                try
                {
                    statement = connect.createStatement();
                    statement.executeUpdate("CREATE DATABASE " + databaseName);

                }
                catch (SQLException e1)
                {
                    statement.executeUpdate("DROP SCHEMA " + databaseName);
                    statement.executeUpdate("CREATE DATABASE " + databaseName);
                }
                connected = true;
            }
            catch (SQLException e)
            {

                System.out.println("Couldn't Connect to Server...");

            }
            catch (ClassNotFoundException ex)
            {
                Logger.getLogger(ColbyMain.class.getName()).log(Level.SEVERE, null, ex);
            }

        } while (!connected);
        try
        {
            connect.close();
            statement.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ColbyMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        //now connect to database, fill with data and execute queries
        try
        {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            try
            {
                connect = DriverManager.getConnection(serverURL + hostName + "/" + databaseName, userName, password);
            }
            catch (SQLException e1)
            {
                System.out.println("Couldn't Connect to Database...");
                //connect.close();
                Logger.getLogger(ColbyMain.class.getName()).log(Level.SEVERE, null, e1);
            }
            System.out.println("Connected");

            System.out.println("Building Tables...");
            statement = connect.createStatement();

            scan = new Scanner(new FileReader(new File(tableFileString)));
            scan.useDelimiter(";");

            //create tables
            while (scan.hasNext())
            {
                statementString = scan.next();
                System.out.println(statementString);
                statement.executeUpdate(statementString);
            }

            System.out.println("Tables Created!");
            System.out.println("Populating Data From " + dataFileString + "...");

            scan = new Scanner(new FileReader(new File(dataFileString)));

            //fill tables with data
            while (scan.hasNextLine())
            {
                statementString = scan.nextLine();

                statement.executeUpdate(statementString);
            }

            System.out.println("\nData Populated!\n");

            System.out.println("Executing Queries...");

            scan = new Scanner(new FileReader(new File(queryFileString)));
            scan.useDelimiter(";");
            ResultSet queryResults;

            while (scan.hasNext())
            {
                statementString = scan.next();
                System.out.println(statementString);
                queryResults = statement.executeQuery(statementString);

                queryPrint(queryResults);
            }

        }
        catch (SQLException e)
        {

            System.out.print("Exception was thrown...");
            System.out.println(statementString);
            Logger.getLogger(ColbyMain.class.getName()).log(Level.SEVERE, null, e);

        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(ColbyMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        //close connections
        finally
        {
            try
            {

                connect.close();
                statement.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(ColbyMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //gets user input for queries where there a ton of results
    private static boolean getUserStop()
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("\nDisplay more results? ('N' to Stop): ");
        String s = "";
        if (scan.hasNextLine())
        {
            s = scan.nextLine();
            if (s.isEmpty())
                return false;
            return s.equalsIgnoreCase("n");
        }
        return false;
    }

    //prints query results
    private static void queryPrint(ResultSet queryResults) throws SQLException
    {
        String resultString;
        int cols = queryResults.getMetaData().getColumnCount();
        String[] colNames = new String[cols];
        int[] colSizes = new int[cols];

        //print column names
        System.out.print("\n\n| ");
        for (int i = 0; i < cols; ++i)
        {

            colNames[i] = queryResults.getMetaData().getColumnLabel(i + 1);
            colSizes[i] = colNames[i].length();
            System.out.printf(String.format("%" + -(colSizes[i]) + "s", colNames[i]));
            System.out.print(" | ");
        }
        System.out.println();
        String dash = "-";
        for (int i = 0; i < cols; ++i)
            for (int j = 0; j < colSizes[i] + 3; ++j)
                dash += "-";
        System.out.println(dash);

        int resultCount = 0;

        //print each row all pretty like
        while (queryResults.next())
        {
            resultCount++;
            if (resultCount % pageSize == 0)
                if (getUserStop())
                    break;

            System.out.print("| ");
            for (int i = 1; i <= cols; i++)
            {
                resultString = queryResults.getString(i);
                //trim long strings to look pretty
                if (resultString.length() > colSizes[i - 1])
                    resultString = resultString.substring(0, colSizes[i - 1]);
                System.out.printf(String.format("%" + -(colSizes[i - 1]) + "s",
                                                resultString));
                System.out.print(" | ");
            }
            System.out.println();

        }
        System.out.println("Results Displayed: " + resultCount);

    }
}
