package quiz_generator.Backend;

import java.util.List;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Database 
{
    private static boolean isDBVerified = false, isSync = false, inUserScoreHistory = false;
    private static final String TRACKER = "#VALID#_";
    
    public static void setInUserScoreHistory (boolean inUserScoreHistory) { Database.inUserScoreHistory = inUserScoreHistory; }
    
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
                      DATE VARCHAR(100) NOT NULL,
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
        
        try
        {
            Connection connection = DriverManager.getConnection (url, user, pass);
        
            if (!isSync)
            {
                isSync = true;
            
                if (inUserScoreHistory) syncForScoreHistory (connection); else syncForLeaderboard (connection);
            }
        
            return connection;
        }
        catch (SQLException e)
        {
            isSync = false;
            isDBVerified = false;
            
            throw e;
        }
    }
    
    // ACT AS A DATA GUARDIAN (FOR LEADERBOARD ONLY)
    
    static void syncForLeaderboard (Connection connection)
    {
        List<String> rawCsvNames = Backup.loadForLeaderboard ("name_leaderboard.csv");
        List<String> rawCsvScores = Backup.loadForLeaderboard ("score_leaderboard.csv");
        
        List<String> csvNames = new ArrayList<>(), csvScores = new ArrayList<>();
        
        int min = Math.min (rawCsvNames.size(), rawCsvScores.size());
        
        for (int i = 0; i < min; i++)
        {
            String csvName = rawCsvNames.get(i), csvScore = rawCsvScores.get(i);
            
            if ((csvName != null && !csvName.trim().isEmpty()) && (csvScore != null && !csvScore.trim().isEmpty()))
            {
                csvNames.add (csvName.trim());
                csvScores.add (csvScore.trim());
            }
        }
        
        List<String> dbNames = new ArrayList<>(), dbScores = new ArrayList<>();
        
        boolean dbCorrupted = false;
        
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery ("SELECT * FROM leaderboard_records ORDER BY ID"))
        {
            while (rs.next())
            {
                String name = rs.getString ("NAME"), score = rs.getString ("SCORE");
                
                if ((name != null && !name.trim().isEmpty()) && (score != null && !score.trim().isEmpty()))
                {
                    try
                    {
                        String decryptName = new String (Base64.getDecoder().decode (name.trim())), decryptScore = new String (Base64.getDecoder().decode (score.trim()));
                        
                        if (!decryptName.startsWith (TRACKER) || !decryptScore.startsWith (TRACKER)) throw new IllegalArgumentException();
                        
                        decryptName = decryptName.substring (TRACKER.length());
                        decryptScore = decryptScore.substring (TRACKER.length());
                        
                        if (Integer.parseInt (decryptScore) < 0) throw new IllegalArgumentException();
                        
                        dbNames.add (decryptName.trim());
                        dbScores.add (decryptScore.trim());
                    }
                    catch (IllegalArgumentException e)
                    {
                        dbCorrupted = true;
                        
                        break;
                    }
                }
            }
        }
        catch (SQLException e)
        {
            return;
        }
        
        if (dbCorrupted)
        {
            try (Statement statement = connection.createStatement())
            {
                statement.execute ("TRUNCATE TABLE leaderboard_records");
                
                String sql = "INSERT INTO leaderboard_records (NAME, SCORE) VALUES (?, ?)";
                
                try (PreparedStatement ps = connection.prepareStatement (sql))
                {
                    for (int i = 0; i < csvNames.size(); i++)
                    {
                        ps.setString (1, Base64.getEncoder().encodeToString ((TRACKER + csvNames.get(i)).getBytes()));
                        ps.setString (2, Base64.getEncoder().encodeToString ((TRACKER + csvScores.get(i)).getBytes()));
                        
                        ps.executeUpdate();
                    }
                }
            }
            catch (SQLException e)
            {
                // Ignore
            }
            
            return;
        }
        
        boolean csvCorrupted = csvNames.size() != csvScores.size();
        
        if (!csvCorrupted)
        {
            for (String check : csvScores)
            {
                try
                {
                    int validation = Integer.parseInt (check.trim());
                    
                    if (validation < 0)
                    {
                        csvCorrupted = true;
                        
                        break;
                    }
                }
                catch (NumberFormatException e)
                {
                    csvCorrupted = true;
                    
                    break;
                }
            }
        }
        
        if (csvCorrupted || csvNames.isEmpty())
        {
            Backup.overwriteForLeaderboard (dbNames, "name_leaderboard.csv");
            Backup.overwriteForLeaderboard (dbScores, "score_leaderboard.csv");
            
            return;
        }
        
        if (dbNames.isEmpty())
        {
            try (Statement statement = connection.createStatement())
            {
                statement.execute ("TRUNCATE TABLE leaderboard_records");
                
                String sql = "INSERT INTO leaderboard_records (NAME, SCORE) VALUES (?, ?)";
                
                try (PreparedStatement ps = connection.prepareStatement (sql))
                {
                    for (int i = 0; i < csvNames.size(); i++)
                    {
                        if (csvNames.get(i) != null && !csvNames.get(i).trim().isEmpty())
                        {
                            ps.setString (1, Base64.getEncoder().encodeToString ((TRACKER + csvNames.get(i)).getBytes()));
                            ps.setString (2, Base64.getEncoder().encodeToString ((TRACKER + csvScores.get(i)).getBytes()));
                            
                            ps.executeUpdate();
                        }
                    }
                }
            }
            catch (SQLException e)
            {
                // Ignore
            }
            
            return;
        }
        
        boolean csvTampered = false, dbOutdated = false;
        
        if (csvNames.size() < dbNames.size())
        {
            boolean legitOfflineWipe = false;
            
            if (csvNames.size() == 1 && csvScores.get(0).equals ("99"))
            {
                boolean dbDeadLock = true;
                
                if (dbScores.size() >= 10)
                {
                    for (String dbScore : dbScores)
                    {
                        if (!dbScore.equals ("99"))
                        {
                            dbDeadLock = false;
                            
                            break;
                        }
                    }
                }
                else
                {
                    dbDeadLock = false;
                }
                
                if (dbDeadLock) legitOfflineWipe = true;
            }
            
            if (legitOfflineWipe) dbOutdated = true; else csvTampered = true;
        }
        
        if (csvNames.size() > dbNames.size()) dbOutdated = true;
        
        for (int i = 0; i < dbNames.size(); i++)
        {
            String dbName = dbNames.get(i), dbScore = dbScores.get(i);
            
            int csvIndex = csvNames.indexOf (dbName);
            
            if (csvIndex != -1)
            {
                if (!csvScores.get (csvIndex).equals (dbScore)) dbOutdated = true;
            }
            else
            {
                csvTampered = true;
                
                break;
            }
        }
        
        if (csvTampered)
        {
            Backup.overwriteForLeaderboard (dbNames, "name_leaderboard.csv");
            Backup.overwriteForLeaderboard (dbScores, "score_leaderboard.csv");
        }
        else if (dbOutdated && !csvTampered)
        {
            try (Statement statement = connection.createStatement())
            {
                statement.execute ("TRUNCATE TABLE leaderboard_records");
                
                String sql = "INSERT INTO leaderboard_records (NAME, SCORE) VALUES (?, ?)";
                
                try (PreparedStatement ps = connection.prepareStatement (sql))
                {
                    for (int i = 0; i < csvNames.size(); i++)
                    {
                        ps.setString (1, Base64.getEncoder().encodeToString ((TRACKER + csvNames.get(i)).getBytes()));
                        ps.setString (2, Base64.getEncoder().encodeToString ((TRACKER + csvScores.get(i)).getBytes()));
                        
                        ps.executeUpdate();                      
                    }
                }
            }
            catch (SQLException e)
            {
                // Ignore
            }
        }
    }
    
    static void updateTrackerForLeaderboard (Connection connection)
    {
        List<String> names = new ArrayList<>(), scores = new ArrayList<>();
        
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery ("SELECT * FROM leaderboard_records ORDER BY ID ASC"))
        {
            while (rs.next())
            {
                String decryptName = new String (Base64.getDecoder().decode (rs.getString ("NAME").trim())), decryptScore = new String (Base64.getDecoder().decode (rs.getString ("SCORE").trim()));
                
                names.add (decryptName.substring (TRACKER.length()));
                scores.add (decryptScore.substring (TRACKER.length()));
            }
            
            Backup.overwriteForLeaderboard (names, "name_leaderboard.csv");
            Backup.overwriteForLeaderboard (scores, "score_leaderboard.csv");
        }
        catch (Exception e)
        {
            // Ignore
        }
    }
    
    // CRUD FOR LEADERBOARD ONLY
    
    public static List<String> loadForLeaderboard (String fieldName)
    {
        List<String> list = new ArrayList<>();
        
        String sql = "SELECT * FROM leaderboard_records ORDER BY ID ASC";
        
        try (Connection connection = getConnection())
        {
            syncForLeaderboard (connection);
            
            try (Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery (sql))
            {
                while (rs.next())
                {
                    String records = rs.getString (fieldName);
                
                    if (fieldName.equals ("NAME") || fieldName.equals ("SCORE"))
                    {
                        records = new String (Base64.getDecoder().decode (records.trim()));
                        records = records.substring (TRACKER.length());
                    }
                    
                    list.add (records);
                }
            }
        }
        catch (Exception e)
        {
            if (fieldName.equals ("NAME")) return Backup.loadForLeaderboard ("name_leaderboard.csv");
            if (fieldName.equals ("SCORE")) return Backup.loadForLeaderboard ("score_leaderboard.csv");
        }
        
        return list;
    }
    
    public static void appendForLeaderboard (String name, String score)
    {
        List<String> scores = loadForLeaderboard ("SCORE");
        
        if (scores.size() >= 10)
        {
            String[] scoreArray = scores.toArray (String[] :: new);
            
            for (int i = 0; i < scoreArray.length - 1; i++)
            {
                for (int j = 0; j < scoreArray.length - i - 1; j++)
                {
                    int prevScore = Integer.parseInt (scoreArray[j]), currentScore = Integer.parseInt (scoreArray[j + 1]);
                    
                    if (prevScore < currentScore)
                    {
                        String tempScore = scoreArray[j];
                        scoreArray[j] = scoreArray[j + 1];
                        scoreArray[j + 1] = tempScore;
                    }
                }
            }
            
            boolean deadLock = true;
            
            int incomingScore = Integer.parseInt (score);
            
            if (incomingScore == 99)
            {
                for (int i = 0; i < 10; i++)
                {
                    int prevScore = Integer.parseInt (scoreArray[i]);
                
                    if (prevScore != incomingScore)
                    {
                        deadLock = false;
                    
                        break;
                    }
                }
            
                if (deadLock) clearForLeaderboard();
            }
        }
        
        try (Connection connection = getConnection())
        {
            syncForLeaderboard (connection);
            
            String encryptName = Base64.getEncoder().encodeToString ((TRACKER + name).getBytes()), encryptScore = Base64.getEncoder().encodeToString ((TRACKER + score).getBytes());
            
            boolean exist = false;
            
            String checkSql = "SELECT ID FROM leaderboard_records WHERE NAME = ?";
            
            try (PreparedStatement checkPs = connection.prepareStatement (checkSql))
            {
                checkPs.setString (1, encryptName);
                
                try (ResultSet rs = checkPs.executeQuery())
                {
                    if (rs.next()) exist = true;
                }
            }
            
            if (exist)
            {
                String delete = "DELETE FROM leaderboard_records WHERE NAME = ?";
                
                try (PreparedStatement ps = connection.prepareStatement (delete))
                {
                    ps.setString (1, encryptName);
                    
                    ps.executeUpdate();
                }
                
                String insert = "INSERT INTO leaderboard_records (NAME, SCORE) VALUES (?, ?)";
                
                try (PreparedStatement ps = connection.prepareStatement (insert))
                {
                    ps.setString (1, encryptName);
                    ps.setString (2, encryptScore);
                    
                    ps.executeUpdate();
                }
            }
            else
            {
                String sql = "INSERT INTO leaderboard_records (NAME, SCORE) VALUES (?, ?)";
                
                try (PreparedStatement ps = connection.prepareStatement (sql))
                {
                    ps.setString (1, encryptName);
                    ps.setString (2, encryptScore);
                    
                    ps.executeUpdate();
                }
            }
            
            updateTrackerForLeaderboard (connection);
        }
        catch (SQLException e)
        {
            List<String> names = Backup.loadForLeaderboard ("name_leaderboard.csv");
            
            boolean exist = false;
            
            for (String n : names)
            {
                if (n.trim().equalsIgnoreCase (name.trim()))
                {
                    exist = true;
                    
                    break;
                }
            }
            
            if (exist)
            {
                overwriteForLeaderboard (name, score);
            }
            else
            {
                Backup.appendForLeaderboard (name, "name_leaderboard.csv");
                Backup.appendForLeaderboard (score, "score_leaderboard.csv");
            }
        }
    }
    
    public static void overwriteForLeaderboard (String name, String score)
    {
        try (Connection connection = getConnection())
        {
            syncForLeaderboard (connection);
            
            String encryptName = Base64.getEncoder().encodeToString ((TRACKER + name).getBytes()), encryptScore = Base64.getEncoder().encodeToString ((TRACKER + score).getBytes());
            
            String delete = "DELETE FROM leaderboard_records WHERE NAME = ?";
            
            try (PreparedStatement ps = connection.prepareStatement (delete))
            {
                ps.setString (1, encryptName);
                
                ps.executeUpdate();
            }
            
            String insert = "INSERT INTO leaderboard_records (NAME, SCORE) VALUES (?, ?)";
            
            try (PreparedStatement ps = connection.prepareStatement (insert))
            {
                ps.setString (1, encryptName);
                ps.setString (2, encryptScore);
                
                ps.executeUpdate();
            }
            
            updateTrackerForLeaderboard (connection);
        }
        catch (SQLException e)
        {
            List<String> names = Backup.loadForLeaderboard ("name_leaderboard.csv");
            List<String> scores = Backup.loadForLeaderboard ("score_leaderboard.csv");
            
            int safeLimit = Math.min (names.size(), scores.size());
            
            for (int i = 0; i < safeLimit; i++)
            {
                if (names.get(i).trim().equalsIgnoreCase (name.trim()))
                {
                    names.remove(i);
                    scores.remove(i);
                    
                    names.add (name.trim());
                    scores.add (score.trim());
                    
                    break;
                }
            }
            
            Backup.overwriteForLeaderboard (names, "name_leaderboard.csv");
            Backup.overwriteForLeaderboard (scores, "score_leaderboard.csv");
        }
    }
    
    static void clearForLeaderboard()
    {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement())
        {
            statement.execute ("TRUNCATE TABLE leaderboard_records");
        }
        catch (SQLException e)
        {
            // Ignore
        }
        
        Backup.clearForLeaderboard ("name_leaderboard.csv");
        Backup.clearForLeaderboard ("score_leaderboard.csv");
    }
    
    // ACT AS A DATA GUARDIAN (FOR PREVIOUS SCORE HISTORY ONLY)
    
    static void syncForScoreHistory (Connection connection)
    {
        List<String> rawCsvNames = Backup.loadForScoreHistory ("ALL", null, "NAME");
        List<String> rawCsvScores = Backup.loadForScoreHistory ("ALL", null, "SCORE");
        List<String> rawCsvTotalScores = Backup.loadForScoreHistory ("ALL", null, "TOTAL_SCORE");
        List<String> rawCsvDates = Backup.loadForScoreHistory ("ALL", null, "DATE");
        
        List<String> csvNames = new ArrayList<>(), csvScores = new ArrayList<>(), csvTotalScores = new ArrayList<>(), csvDates = new ArrayList<>();
        
        int min = Math.min (rawCsvNames.size(), Math.min (rawCsvScores.size(), Math.min (rawCsvTotalScores.size(), rawCsvDates.size())));
        
        for (int i = 0; i < min; i++)
        {
            String csvName = rawCsvNames.get(i), csvScore = rawCsvScores.get(i), csvTotalScore = rawCsvTotalScores.get(i), csvDate = rawCsvDates.get(i);
            
            if ((csvName != null && !csvName.trim().isEmpty()) && (csvScore != null && !csvScore.trim().isEmpty()) && (csvTotalScore != null && !csvTotalScore.trim().isEmpty()) && (csvDate != null && !csvDate.trim().isEmpty()))
            {
                csvNames.add (csvName.trim());
                csvScores.add (csvScore.trim());
                csvTotalScores.add (csvTotalScore.trim());
                csvDates.add (csvDate.trim());
            }
        }
        
        List<String> dbNames = new ArrayList<>(), dbScores = new ArrayList<>(), dbTotalScores = new ArrayList<>(), dbDates = new ArrayList<>();
        
        boolean dbCorrupted = false;
        
        String sql = "SELECT user.NAME, point.SCORE, point.TOTAL_SCORE, point.DATE FROM user_records user JOIN point_records point ON user.ID = point.USER_ID ORDER BY point.ID";
        
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery (sql))
        {
            while (rs.next())
            {
                String name = rs.getString ("NAME"), score = rs.getString ("SCORE"), totalScore = rs.getString ("TOTAL_SCORE"), date = rs.getString ("DATE");
                
                if ((name != null && !name.trim().isEmpty()) && (score != null && !score.trim().isEmpty()) && (totalScore != null && !totalScore.trim().isEmpty()) && (date != null && !date.trim().isEmpty()))
                {
                    try
                    {
                        String decryptName = new String (Base64.getDecoder().decode (name.trim())), decryptScore = new String (Base64.getDecoder().decode (score.trim())), decryptTotalScore = new String (Base64.getDecoder().decode (totalScore.trim())), decryptDate = new String (Base64.getDecoder().decode (date.trim()));
                        
                        if (!decryptName.startsWith (TRACKER) || !decryptScore.startsWith (TRACKER) || !decryptTotalScore.startsWith (TRACKER) || !decryptDate.startsWith (TRACKER)) throw new IllegalArgumentException();
                        
                        decryptName = decryptName.substring (TRACKER.length());
                        decryptScore = decryptScore.substring (TRACKER.length());
                        decryptTotalScore = decryptTotalScore.substring (TRACKER.length());
                        decryptDate = decryptDate.substring (TRACKER.length());
                        
                        if (Integer.parseInt (decryptScore.trim()) < 0 || Integer.parseInt (decryptTotalScore.trim()) < 0) throw new IllegalArgumentException();
                        
                        dbNames.add (decryptName.trim());
                        dbScores.add (decryptScore.trim());
                        dbTotalScores.add (decryptTotalScore.trim());
                        dbDates.add (decryptDate.trim());                    
                    }
                    catch (IllegalArgumentException e)
                    {
                        dbCorrupted = true;
                        
                        break;
                    }
                }
            }
        }
        catch (SQLException e)
        {
            return;
        }
        
        if (dbCorrupted)
        {
            try (Statement statement = connection.createStatement())
            {
                statement.executeUpdate ("DELETE FROM user_records");
                statement.executeUpdate ("ALTER TABLE user_records AUTO_INCREMENT = 1");
                statement.executeUpdate ("ALTER TABLE point_records AUTO_INCREMENT = 1");
                
                String userSql = "INSERT INTO user_records (NAME) VALUES (?)";
                String pointSql = "INSERT INTO point_records (USER_ID, SCORE, TOTAL_SCORE, DATE) VALUES (?, ?, ?, ?)";
                
                for (int i = 0; i < csvNames.size(); i++)
                {
                    try (PreparedStatement userPs = connection.prepareStatement (userSql, Statement.RETURN_GENERATED_KEYS))
                    {
                        userPs.setString (1, Base64.getEncoder().encodeToString ((TRACKER + csvNames.get(i)).getBytes()));
                        
                        userPs.executeUpdate();
                        
                        try (ResultSet rs = userPs.getGeneratedKeys())
                        {
                            if (rs.next())
                            {
                                int id = rs.getInt(1);
                                
                                try (PreparedStatement pointPs = connection.prepareStatement (pointSql))
                                {
                                    pointPs.setInt (1, id);
                                    pointPs.setString (2, Base64.getEncoder().encodeToString ((TRACKER + csvScores.get(i)).getBytes()));
                                    pointPs.setString (3, Base64.getEncoder().encodeToString ((TRACKER + csvTotalScores.get(i)).getBytes()));
                                    pointPs.setString (4, Base64.getEncoder().encodeToString ((TRACKER + csvDates.get(i)).getBytes()));
                                    
                                    pointPs.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
            catch (SQLException e)
            {
                // Ignore
            }
            
            return;
        }
        
        boolean csvCorrupted = csvNames.size() != csvScores.size() || csvScores.size() != csvTotalScores.size() || csvTotalScores.size() != csvDates.size();
        
        boolean legitOfflineWipe = false;
        
        if (!csvCorrupted && !csvNames.isEmpty())
        {
            if (csvNames.get(0).equals ("WIPED") && csvScores.get(0).equals ("-1") && csvTotalScores.get(0).equals ("-1"))
            {
                legitOfflineWipe = true;
                
                csvNames.remove(0);
                csvScores.remove(0);
                csvTotalScores.remove(0);
                csvDates.remove(0);
            }
        }
        
        if (!csvCorrupted)
        {
            for (int i = 0; i < csvScores.size(); i++)
            {
                try
                {
                    int[] validation = {Integer.parseInt (csvScores.get(i).trim()), Integer.parseInt (csvTotalScores.get(i).trim())};
                    
                    if (validation[0] < 0 || validation[1] < 0) 
                    {
                        if (validation[0] != -2 && validation[1] != -2)
                        {
                            csvCorrupted = true;
                            
                            break;
                        }
                    }
                }
                catch (NumberFormatException e)
                {
                    csvCorrupted = true;
                    
                    break;
                }
            }
        }
        
        if (csvCorrupted || (csvNames.isEmpty() && !legitOfflineWipe))
        {
            Backup.overwriteForScoreHistory (dbNames, "name_history.csv");
            Backup.overwriteForScoreHistory (dbScores, "score_history.csv");
            Backup.overwriteForScoreHistory (dbTotalScores, "totalScore_history.csv");
            Backup.overwriteForScoreHistory (dbDates, "date_history.csv");
            
            return;
        }
        
        if (legitOfflineWipe)
        {
            try (Statement statement = connection.createStatement())
            {
                statement.executeUpdate ("DELETE FROM user_records");
                statement.executeUpdate ("ALTER TABLE user_records AUTO_INCREMENT = 1");
                statement.executeUpdate ("ALTER TABLE point_records AUTO_INCREMENT = 1");
            }
            catch (SQLException e)
            {
                // Ignore
            }
            
            dbNames.clear();
            dbScores.clear();
            dbTotalScores.clear();
            dbDates.clear();
        }
        
        if (dbNames.isEmpty())
        {
            try (Statement statement = connection.createStatement())
            {
                statement.executeUpdate ("DELETE FROM user_records");
                statement.executeUpdate ("ALTER TABLE user_records AUTO_INCREMENT = 1");
                statement.executeUpdate ("ALTER TABLE point_records AUTO_INCREMENT = 1");
                
                String userSql = "INSERT INTO user_records (NAME) VALUES (?)"; 
                String pointSql = "INSERT INTO point_records (USER_ID, SCORE, TOTAL_SCORE, DATE) VALUES (?, ?, ?, ?)";
                
                for (int i = 0; i < csvNames.size(); i++)
                {
                    if (csvScores.get(i).equals ("-2")) continue;
                    
                    try (PreparedStatement userPs = connection.prepareStatement (userSql, Statement.RETURN_GENERATED_KEYS))
                    {
                        userPs.setString (1, Base64.getEncoder().encodeToString ((TRACKER + csvNames.get(i)).getBytes()));
                        
                        userPs.executeUpdate();
                        
                        try (ResultSet rs = userPs.getGeneratedKeys())
                        {
                            if (rs.next())
                            {
                                int id = rs.getInt(1);
                                
                                try (PreparedStatement pointPs = connection.prepareStatement (pointSql))
                                {
                                    pointPs.setInt (1, id);
                                    pointPs.setString (2, Base64.getEncoder().encodeToString ((TRACKER + csvScores.get(i)).getBytes()));
                                    pointPs.setString (3, Base64.getEncoder().encodeToString ((TRACKER + csvTotalScores.get(i)).getBytes()));
                                    pointPs.setString (4, Base64.getEncoder().encodeToString ((TRACKER + csvDates.get(i)).getBytes()));
                                    
                                    pointPs.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
            catch (SQLException e)
            {
                // Ignore
            }
            
            updateTrackerForScoreHistory (connection);
            
            return;
        }
        
        boolean csvTampered = false, dbOutdated = false;
        
        if (csvNames.size() < dbNames.size())
        {
            csvTampered = true;
        }
        else
        {
            for (int i = 0; i < dbNames.size(); i++)
            {
                if (!dbNames.get(i).equals (csvNames.get(i)) || !dbDates.get(i).equals (csvDates.get(i)))
                {
                    csvTampered = true;
                    
                    break;
                }
                
                if (!dbScores.get(i).equals (csvScores.get(i)) || !dbTotalScores.get(i).equals (csvTotalScores.get(i)))
                {
                    if (csvScores.get(i).equals ("-2") && csvTotalScores.get(i).equals ("-2"))
                    {
                        dbOutdated = true;
                    }
                    else
                    {
                        csvTampered = true;
                        
                        break;
                    }
                }
            }
            
            if (!csvTampered && csvNames.size() > dbNames.size()) dbOutdated = true;
        }
        
        if (csvTampered)
        {
            Backup.overwriteForScoreHistory (dbNames, "name_history.csv");
            Backup.overwriteForScoreHistory (dbScores, "score_history.csv");
            Backup.overwriteForScoreHistory (dbTotalScores, "totalScore_history.csv");
            Backup.overwriteForScoreHistory (dbDates, "date_history.csv");
        }
        else if (dbOutdated && !csvTampered)
        {
            try (Statement statement = connection.createStatement())
            {
                statement.executeUpdate ("DELETE FROM user_records");
                statement.executeUpdate ("ALTER TABLE user_records AUTO_INCREMENT = 1");
                statement.executeUpdate ("ALTER TABLE point_records AUTO_INCREMENT = 1");
                
                String userSql = "INSERT INTO user_records (NAME) VALUES (?)"; 
                String pointSql = "INSERT INTO point_records (USER_ID, SCORE, TOTAL_SCORE, DATE) VALUES (?, ?, ?, ?)";
                
                for (int i = 0; i < csvNames.size(); i++)
                {
                    if (csvScores.get(i).equals ("-2")) continue;
                    
                    try (PreparedStatement userPs = connection.prepareStatement (userSql, Statement.RETURN_GENERATED_KEYS))
                    {
                        userPs.setString (1, Base64.getEncoder().encodeToString ((TRACKER + csvNames.get(i)).getBytes()));
                        
                        userPs.executeUpdate();
                        
                        try (ResultSet rs = userPs.getGeneratedKeys())
                        {
                            if (rs.next())
                            {
                                int id = rs.getInt(1);
                                
                                try (PreparedStatement pointPs = connection.prepareStatement (pointSql))
                                {
                                    pointPs.setInt (1, id);
                                    pointPs.setString (2, Base64.getEncoder().encodeToString ((TRACKER + csvScores.get(i)).getBytes()));
                                    pointPs.setString (3, Base64.getEncoder().encodeToString ((TRACKER + csvTotalScores.get(i)).getBytes()));
                                    pointPs.setString (4, Base64.getEncoder().encodeToString ((TRACKER + csvDates.get(i)).getBytes()));
                                    
                                    pointPs.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
            catch (SQLException e)
            {
                // Ignore
            }
            
            updateTrackerForScoreHistory (connection);
        }
    }
    
    static void updateTrackerForScoreHistory (Connection connection)
    {
        List<String> names = new ArrayList<>(), scores = new ArrayList<>(), totalScores = new ArrayList<>(), dates = new ArrayList<>();
        
        String sql = "SELECT user.NAME, point.SCORE, point.TOTAL_SCORE, point.DATE FROM user_records user JOIN point_records point ON user.ID = point.USER_ID ORDER BY point.ID ASC";
        
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery (sql))
        {
            while (rs.next())
            {
                String decryptName = new String (Base64.getDecoder().decode (rs.getString ("NAME").trim())), decryptScore = new String (Base64.getDecoder().decode (rs.getString ("SCORE").trim())), decryptTotalScore = new String (Base64.getDecoder().decode (rs.getString ("TOTAL_SCORE").trim())), decryptDate = new String (Base64.getDecoder().decode (rs.getString ("DATE").trim()));
                
                names.add (decryptName.substring (TRACKER.length()));
                scores.add (decryptScore.substring (TRACKER.length()));
                totalScores.add (decryptTotalScore.substring (TRACKER.length()));
                dates.add (decryptDate.substring (TRACKER.length()));
            }
            
            if (names.isEmpty())
            {
                names.add ("WIPED");
                scores.add ("-1");
                totalScores.add ("-1");
                dates.add ("1970-01-01");
            }
            
            Backup.overwriteForScoreHistory (names, "name_history.csv");
            Backup.overwriteForScoreHistory (scores, "score_history.csv");
            Backup.overwriteForScoreHistory (totalScores, "totalScore_history.csv");
            Backup.overwriteForScoreHistory (dates, "date_history.csv");
        }
        catch (Exception e)
        {
            // Ignore
        }
    }
    
    // CRUD FOR PREVIOUS SCORE HISTORY ONLY
    
    public static List<String> loadForScoreHistory (String action, String targetName, String fieldName)
    {
        List<String> list = new ArrayList<>();
        
        String sql = "";
        
        switch (action)
        {
            case "USER" ->
            {
                sql = "SELECT DISTINCT NAME FROM user_records ORDER BY ID ASC";
            }
            case "HISTORY" ->
            {
                sql = "SELECT point.DATE AS DATE, point.SCORE AS SCORE, point.TOTAL_SCORE AS TOTAL_SCORE FROM user_records user JOIN point_records point ON user.ID = point.USER_ID WHERE user.NAME = ? ORDER BY point.ID DESC";
            }
        }
        
        try (Connection connection = getConnection())
        {
            syncForScoreHistory (connection);
            
            try (PreparedStatement ps = connection.prepareStatement (sql))
            {
                if (action.equals ("HISTORY")) ps.setString (1, Base64.getEncoder().encodeToString ((TRACKER + targetName).getBytes()));
                
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        switch (action)
                        {
                            case "USER" ->
                            {
                                String name = new String (Base64.getDecoder().decode (rs.getString ("NAME").trim()));
                                
                                list.add (name.substring (TRACKER.length()));
                            }
                            case "HISTORY" ->
                            {
                                String date = new String (Base64.getDecoder().decode (rs.getString ("DATE").trim())), score = new String (Base64.getDecoder().decode (rs.getString ("SCORE").trim())), totalScore = new String (Base64.getDecoder().decode (rs.getString ("TOTAL_SCORE").trim()));
                                
                                list.add (date.substring (TRACKER.length()));
                                list.add (score.substring (TRACKER.length()));
                                list.add (totalScore.substring (TRACKER.length()));
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            return Backup.loadForScoreHistory (action, targetName, fieldName);
        }
        
        return list;
    }
    
    public static void appendForScoreHistory (String name, String score, String totalScore)
    {
        String userSql = "INSERT INTO user_records (NAME) VALUES (?)";
        String pointSql = "INSERT INTO point_records (USER_ID, SCORE, TOTAL_SCORE, DATE) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = getConnection();
             PreparedStatement userPs = connection.prepareStatement (userSql, Statement.RETURN_GENERATED_KEYS))
        {
            syncForScoreHistory (connection);
            
            userPs.setString (1, Base64.getEncoder().encodeToString ((TRACKER + name).getBytes()));
            
            userPs.executeUpdate();
            
            try (ResultSet rs = userPs.getGeneratedKeys())
            {
                if (rs.next())
                {
                    int id = rs.getInt(1);
                    
                    try (PreparedStatement pointPs = connection.prepareStatement (pointSql))
                    {
                        pointPs.setInt (1, id);
                        pointPs.setString (2, Base64.getEncoder().encodeToString ((TRACKER + score).getBytes()));
                        pointPs.setString (3, Base64.getEncoder().encodeToString ((TRACKER + totalScore).getBytes()));
                        
                        String date = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").format (new java.util.Date());
                        
                        pointPs.setString (4, Base64.getEncoder().encodeToString ((TRACKER + date).getBytes()));
                        
                        pointPs.executeUpdate();
                    }
                }
            }
            
            updateTrackerForScoreHistory (connection);
        }
        catch (SQLException e)
        {
            Backup.appendForScoreHistory (name, score, totalScore);
        }
    }
    
    public static void clearForScoreHistory (String name)
    {
        String sql = "DELETE FROM user_records WHERE NAME = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement (sql))
        {
            syncForScoreHistory (connection);
            
            ps.setString (1, Base64.getEncoder().encodeToString ((TRACKER + name).getBytes()));
            
            ps.executeUpdate();
            
            updateTrackerForScoreHistory (connection);
        }
        catch (SQLException e)
        {
            Backup.clearForScoreHistory (name);
        }
    }
}