package quiz_generator.Backend;

import java.util.List;
import java.util.*;
import java.sql.*;

public class Database 
{
    public static boolean inUserScoreHistory = false;
    private static boolean isDBVerified = false, isSync = false;
    
    // CHECK AND CREATE DATABASE AND TABLE IF NOT EXISTS
    
    static void verifyDatabase()
    {
        if (isDBVerified) return;
            
        String url = "jdbc:mysql://localhost:3306/", user = "root", pass = "";
        
        try (Connection connection = DriverManager.getConnection (url, user, pass);
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate ("CREATE DATABASE IF NOT EXISTS quiz_generator");
            statement.execute ("USE quiz_generator");
            
            String createTableLeaderboard =
            """
                 CREATE TABLE IF NOT EXISTS leaderboard_records
                 (
                      ID INT AUTO_INCREMENT PRIMARY KEY,
                      NAME VARCHAR(100) NOT NULL,
                      SCORE VARCHAR(100) NOT NULL
                 );
            """;
            
            String createTableUser = 
            """
                 CREATE TABLE IF NOT EXISTS user_records
                 (
                      ID INT AUTO_INCREMENT PRIMARY KEY,
                      NAME VARCHAR(100) NOT NULL
                 );                                                                                      
            """;
            
            String createTablePoint =
            """
                 CREATE TABLE IF NOT EXISTS point_records
                 (
                      ID INT AUTO_INCREMENT PRIMARY KEY,
                      USER_ID INT NOT NULL,
                      SCORE VARCHAR(100) NOT NULL,
                      TOTAL_SCORE VARCHAR(100) NOT NULL,
                      FOREIGN KEY (USER_ID) REFERENCES user_records(ID) ON DELETE CASCADE
                 );               
            """;
            
            statement.executeUpdate (createTableLeaderboard);
            statement.executeUpdate (createTableUser);
            statement.executeUpdate (createTablePoint);
            
            isDBVerified = true;
        }
        catch (SQLException e)
        {
            // Ignore
        }
    }
    
    // CONNECT TO DATABASE
    
    static Connection getConnection() throws SQLException
    {
        if (!isDBVerified) verifyDatabase();
        
        String url = "jdbc:mysql://localhost:3306/quiz_generator", user = "root", pass = "";
        
        Connection connection = DriverManager.getConnection (url, user, pass);
        
        if (!isSync)
        {
            isSync = true;
            
            if (inUserScoreHistory) syncForScoreHistory (connection); else syncForLeaderboard (connection);
        }
        
        return connection;
    }
    
    // ACT AS A DATA GUARDIAN (FOR LEADERBOARD ONLY)
    
    static void syncForLeaderboard (Connection connection)
    {
        List<String> names = Backup.loadForLeaderboard ("name_leaderboard.csv");
        List<String> scores = Backup.loadForLeaderboard ("score_leaderboard.csv");
        
        boolean isCorrupted = names.size() != scores.size();
        
        if (isCorrupted || names.isEmpty())
        {
            try (Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery ("SELECT NAME, SCORE FROM leaderboard_records"))
            {
                Backup.clearForLeaderboard ("name_leaderboard.csv");
                Backup.clearForLeaderboard ("score_leaderboard.csv");
                
                while (rs.next())
                {
                    Backup.appendForLeaderboard (rs.getString ("NAME"), "name_leaderboard.csv");
                    Backup.appendForLeaderboard (rs.getString ("SCORE"), "score_leaderboard.csv");
                }
            }
            catch (SQLException e)
            {
                // Ignore
            }
            
            return;
        }
        
        try (Statement statement = connection.createStatement())
        {
            statement.execute ("TRUNCATE TABLE leaderboard_records");
            
            String sql = "INSERT INTO leaderboard_records (NAME, SCORE) VALUES (?, ?)";
            
            try (PreparedStatement ps = connection.prepareStatement (sql))
            {
                for (int i = 0; i < names.size(); i++)
                {
                    ps.setString (1, names.get(i));
                    ps.setString (2, scores.get(i));
                    
                    ps.executeUpdate();
                }
            }
        }
        catch (SQLException e)
        {
            // Ignore
        }
    }
     
    // CRUD (BUT NO DELETION)FOR LEADERBOARD ONLY
    
    public static List<String> loadForLeaderboard (String fieldName)
    {
        List<String> list = new ArrayList<>();
        
        String sql = "SELECT * FROM leaderboard_records";
        
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery (sql))
        {
            while (rs.next())
            {
                String records = rs.getString (fieldName);
                
                list.add (records);
            }
        }
        catch (SQLException e)
        {
            if (fieldName.equals ("NAME")) return Backup.loadForLeaderboard ("name_leaderboard.csv");
            if (fieldName.equals ("SCORE")) return Backup.loadForLeaderboard ("score_leaderboard.csv");
        }
        
        return list;
    }
    
    public static void appendForLeaderboard (String name, String score, String totalScore)
    {
        String sql = "INSERT INTO leaderboard_records (NAME, SCORE) VALUES (?, ?)";
        
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement (sql))
        {
            ps.setString (1, name);
            ps.setString (2, score);
            
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            Backup.appendForLeaderboard (name, "name_leaderboard.csv");
            Backup.appendForLeaderboard (score, "score_leaderboard.csv");
            
            return;
        }
        
        List<String> names = new ArrayList<>(), scores = new ArrayList<>();
        
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery ("SELECT NAME, SCORE FROM leaderboard_records"))
        {
            while (rs.next())
            {
                names.add (rs.getString ("NAME"));
                scores.add (rs.getString ("SCORE"));
            }
            
            Backup.clearForLeaderboard ("name_leaderboard.csv");
            Backup.clearForLeaderboard ("score_leaderboard.csv");
            
            for (int i = 0; i < names.size(); i++)
            {
                Backup.appendForLeaderboard (names.get(i), "name_leaderboard.csv");
                Backup.appendForLeaderboard (scores.get(i), "score_leaderboard.csv");
            }
        }
        catch (SQLException e)
        {
            // Ignore
        }
    }
    
    public static void overwriteForLeaderboard (String name, String score, String totalScore)
    {
        String sql = "UPDATE leaderboard_records SET SCORE = ? WHERE NAME = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement (sql))
        {
            ps.setString (1, score);
            ps.setString (2, name);
            
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            List<String> names = Backup.loadForLeaderboard ("name_leaderboard.csv");
            List<String> scores = Backup.loadForLeaderboard ("score_leaderboard.csv");
            
            for (int i = 0; i < names.size(); i++)
            {
                if (names.get(i).equals (name))
                {
                    scores.set (i, score);
                    
                    break;
                }
            }
            
            Backup.overwriteForLeaderboard (names, "name_leaderboard.csv");
            Backup.overwriteForLeaderboard (scores, "score_leaderboard.csv");
            
            return;
        }
        
        List<String> names = new ArrayList<>(), scores = new ArrayList<>();
        
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery ("SELECT NAME, SCORE FROM leaderboard_records"))
        {
            while (rs.next())
            {
                names.add (rs.getString ("NAME"));
                scores.add (rs.getString ("SCORE"));
            }
            
            Backup.clearForLeaderboard ("name_leaderboard.csv");
            Backup.clearForLeaderboard ("score_leaderboard.csv");
            
            for (int i = 0; i < names.size(); i++)
            {
                Backup.appendForLeaderboard (names.get(i), "name_leaderboard.csv");
                Backup.appendForLeaderboard (scores.get(i), "score_leaderboard.csv");
            }
        }
        catch (SQLException e)
        {
            // Ignore
        }
    }
    
    // ACT AS A DATA GUARDIAN (FOR PREVIOUS SCORE HISTORY ONLY)
    
    static void syncForScoreHistory (Connection connection)
    {
        
    }
    
    // CRUD FOR PREVIOUS SCORE HISTORY ONLY
    
    public static List<String> loadForScoreHistory (String fieldName)
    {
        List<String> list = new ArrayList<>();
        
        String sql =
        """
             SELECT user_records.NAME, point_records.SCORE, point_records.TOTAL_SCORE
             FROM user_records
             JOIN point_records ON user_records.ID = point_records.USER_ID
        """;
        
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery (sql))
        {
            while (rs.next())
            {
                String records = rs.getString (fieldName);
                
                list.add (records);
            }
        }
        catch (SQLException e)
        {
            // Ignore
        }
        
        return list;
    }
    
    public static void appendForScoreHistory (String name, String score, String totalScore)
    {
        String userSql = "INSERT INTO user_records (NAME) VALUES (?)";
        String pointSql = "INSERT INTO point_records (USER_ID, SCORE, TOTAL_SCORE) VALUES (?, ?, ?)";
        
        try (Connection connection = getConnection();
             PreparedStatement userPs = connection.prepareStatement (userSql, Statement.RETURN_GENERATED_KEYS))
        {
            userPs.setString (1, name);
            userPs.executeUpdate();
            
            try (ResultSet rs = userPs.getGeneratedKeys())
            {
                if (rs.next())
                {
                    int generatedUserId = rs.getInt(1);
                    
                    try (PreparedStatement pointPs = connection.prepareStatement (pointSql))
                    {
                        pointPs.setInt (1, generatedUserId);
                        pointPs.setString (2, score);
                        pointPs.setString (3, totalScore);
                        
                        pointPs.executeUpdate();
                    }
                }
            }
        }
        catch (SQLException e)
        {
            // Ignore
        }
    }
    
    public static void clearForScoreHistory (String name)
    {
        String sql = "DELETE FROM user_records WHERE NAME = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement (sql))
        {
            ps.setString (1, name);
            
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            // Ignore
        }
    }
}
