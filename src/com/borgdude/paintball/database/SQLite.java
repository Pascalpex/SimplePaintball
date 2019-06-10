package com.borgdude.paintball.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import com.borgdude.paintball.Main;


public class SQLite extends Database{
    String dbname;
    public SQLite(Main instance){
        super(instance);
        dbname = plugin.getConfig().getString("SQLite.Filename", "player_stats"); // Set the table name here e.g player_kills
    }

    public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS player_stats (" + // make sure to put your table name in here too.
            "`player_id` varchar(150) PRIMARY KEY NOT NULL," + // This creates the different colums you will save data too. varchar(32) Is a string, int = integer
            "`wins` int(11) NOT NULL," +
            "`kills` int(11) NOT NULL" +
            ");"; // we can search by player, and get kills and total. If you some how were searching kills it would provide total and player.


    // SQL creation stuff, You can leave the blow stuff untouched.
    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
}