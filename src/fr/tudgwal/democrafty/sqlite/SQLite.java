package fr.tudgwal.democrafty.sqlite;


import fr.tudgwal.democrafty.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.sql.PreparedStatement;


public class SQLite {
    private static ConnectionHandler database;

    // CREATE TABLE
    private static final String CREATE_MAIN_TABLE = "CREATE TABLE IF NOT EXISTS democraftyTable(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, startdate DATE NOT NULL, enddate DATE NOT NULL)";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $table (id INTEGER PRIMARY KEY AUTOINCREMENT, player TEXT NOT NULL, choice TEXT NOT NULL, date DATE NOT NULL)";

    // INSERT
    private static final String INSERT_TABLE = "INSERT INTO democraftyTable(name, startdate, enddate) VALUES (?, ?, ?)";
    private static final String INSERT_VOTE = "INSERT INTO $table (player, choice, date) VALUES (?, ?, ?)";

    // UPDATE
    private static final String UPDATE_DATE = "UPDATE democraftyTable SET startdate=?, enddate=? WHERE name=?";

    // DELETE
    private static final String DELETE_VOTE = "DELETE FROM democraftyTable WHERE name=?";
    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS $table";

    // SELECT
    private static final String TABLE_INFO = "SELECT * FROM democraftyTable WHERE name=?";
    private static final String LIST_VOTE = "SELECT name FROM democraftyTable";
    private static final String CHECK_VOTE = "SELECT player FROM $table WHERE player=?";
    private static final String GET_RESULT =  "SELECT choice AS CHOICE, COUNT(*) AS TOTAL FROM $table GROUP BY choice ORDER BY COUNT(*) DESC";
    private static final String CHECK_TABLE = "SELECT name FROM democraftyTable WHERE name=?";
    private static final String GET_DATE = "SELECT $type AS TYPE FROM democraftyTable WHERE name=?";



    public SQLite(JavaPlugin plugin) {
        this.database = new ConnectionHandler(plugin.getDataFolder() + "/democrafty.db");
    }

    public static List<String> getVotes() {
        database.connect();
        PreparedStatement preparedStatement = null;
        List<String> res = new ArrayList();
        try {
            preparedStatement = database.getConnection().prepareStatement(LIST_VOTE);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                res.add(resultSet.getString("name"));
            }
        } catch (SQLException e){
            Utils.log(e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }
        return res;
    }

    public static String getDate(String table, String type) {
        database.connect();
        String res = "";
        String query = GET_DATE;
        if (type.equalsIgnoreCase("begin"))
            query = query.replace("$type", "startdate");
        else if (type.equalsIgnoreCase("end"))
            query = query.replace("$type", "enddate");
        try {
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            preparedStatement.setString(1, table);
            ResultSet resultSet = preparedStatement.executeQuery();
            res = resultSet.getString("TYPE");
        } catch (SQLException e){
            Utils.log(e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }
        return res;
    }

    public static String checkName(String table, String name) {
        database.connect();
        String res = "";
        String query = CHECK_VOTE;
        query = query.replace("$table", table);
        try {
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            res = resultSet.getString("player");
        } catch (SQLException e){
            Utils.log(e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }
        return res;
    }

    public static void playerVote(String table, String pName, String pChoice, String date){
        database.connect();
        String query = INSERT_VOTE;
        query = query.replace("$table", table);
        try {
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            preparedStatement.setString(1, pName);
            preparedStatement.setString(2, pChoice);
            preparedStatement.setString(3, date);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            Utils.log(e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }
    }

    public static void createNewVote(String[] args) {
        database.connect();
        try {
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_TABLE);
            preparedStatement.setString(1, args[1]);
            preparedStatement.setString(2, args[2]);
            preparedStatement.setString(3, args[3]);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            Utils.log(e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }

    }

    public static void updateVote(String[] args) {
        database.connect();
        try {
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_DATE);
            preparedStatement.setString(1, args[2]);
            preparedStatement.setString(2, args[3]);
            preparedStatement.setString(3, args[1]);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            Utils.log(e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }
    }

    public static void createTable(String args[]){
        database.connect();
        String query = CREATE_TABLE;
        query = query.replace("$table", args[1]);
        try {
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            Utils.log(e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }
    }

    public static List<String> getInfo(String name) {
        database.connect();
        PreparedStatement preparedStatement = null;
        List<String> res = null;
        try {
            preparedStatement = database.getConnection().prepareStatement(TABLE_INFO);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                res = null;
            res = Arrays.asList(resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
        } catch (SQLException e){
            Utils.log(e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }
        return res;
    }

    public static String getResult(String table) {
        database.connect();
        String res = "";
        PreparedStatement preparedStatement = null;
        String query = GET_RESULT;
        query = query.replace("$table", table);
        try {
            preparedStatement = database.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                    res +=  "- " + resultSet.getString("CHOICE") + " > " +  resultSet.getInt("TOTAL") + "\n";
            }
        } catch (SQLException e){
            Utils.log(e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }
        return res;
    }

    public static void deleteTable(String table){
        database.connect();
        String query = DELETE_TABLE;
        query = query.replace("$table", table);
        try {
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            Utils.log(e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }
    }

    public static String tableExist(String name){
        database.connect();
        String res = "";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = database.getConnection().prepareStatement(CHECK_TABLE);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            res = resultSet.getString("name");
        } catch (SQLException e){
            Utils.log(e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }
        return res;
    }

    public static void deleteInMainTable(String name){
        database.connect();
        try {
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_VOTE);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            Utils.log(e.getSQLState() + " | " + e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }
    }

    public void createMainTable(){
        database.connect();
        try {
            PreparedStatement preparedStatement = database.getConnection().prepareStatement(CREATE_MAIN_TABLE);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            Utils.log(e.getMessage(), Level.SEVERE);
        } finally {
            database.close();
        }
    }

}
