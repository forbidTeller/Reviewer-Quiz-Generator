package quiz_generator.Backend;

import java.util.List;
import java.util.*;
import java.io.*;
import java.text.*;

class Backup 
{
    // BACKUP CRUD FOR LEADERBOARD ONLY
    
    static List<String> loadForLeaderboard (String loadFileName)
    {
        List<String> list = new ArrayList<>();
        
        try
        {
            File file = new File (loadFileName);
            
            if (!file.exists())
            {
                file.createNewFile();
                
                return list;
            }
            
            try (BufferedReader br = new BufferedReader (new FileReader (file)))
            {
                String line;
                
                while ((line = br.readLine()) != null)
                {
                    try
                    {
                        String decrypt = new String (Base64.getDecoder().decode (line));
                        
                        list.add (decrypt);
                    }
                    catch (IllegalArgumentException e)
                    {
                        list.add ("-1");
                    }
                }
            }
        }
        catch (IOException e)
        {
            // Ignore
        }
        
        return list;
    }
    
    static void appendForLeaderboard (String appended, String saveFileName)
    {
        try
        {
            File file = new File (saveFileName);
            
            if (!file.exists()) file.createNewFile();
            
            try (FileWriter fw = new FileWriter (file, true))
            {
                String encrypt = Base64.getEncoder().encodeToString (appended.getBytes());
                
                fw.write (encrypt + "\n");
            }
        }
        catch (IOException e)
        {
            // Ignore
        }
    }
    
    static void overwriteForLeaderboard (List<String> list, String saveFileName)
    {
        try (FileWriter fw = new FileWriter (saveFileName, false))
        {
            for (String s : list)
            {
                String encrypt = Base64.getEncoder().encodeToString (s.getBytes());
                        
                fw.write (encrypt + "\n");
            }
        }
        catch (IOException e)
        {
            // Ignore
        }
    }
    
    static void clearForLeaderboard (String clearFileName)
    {
        try (FileWriter fw = new FileWriter (clearFileName, false))
        {
            fw.write ("");
        }
        catch (IOException e)
        {
            // Ignore
        }
    }
    
    // BACKUP CRUD FOR PREVIOUS SCORE HISTORY ONLY
    
    static List<String> loadForScoreHistory (String action, String targetName, String fieldName)
    {
        List<String> list = new ArrayList<>(), names = new ArrayList<>(), scores = new ArrayList<>(), totalScores = new ArrayList<>(), dates = new ArrayList<>();
        
        String[] files = {"name_history.csv", "score_history.csv", "totalScore_history.csv", "date_history.csv"};
        
        List[] targetList = {names, scores, totalScores, dates};
        
        for (int i = 0; i < files.length; i++)
        {
            try
            {
                File file = new File (files[i]);
                
                if (!file.exists()) file.createNewFile();
                
                try (BufferedReader br = new BufferedReader (new FileReader (file)))
                {
                    String line;
                    
                    while ((line = br.readLine()) != null)
                    {
                        try
                        {
                            String decrypt = new String (Base64.getDecoder().decode (line));
                            
                            targetList[i].add (decrypt);
                        }
                        catch (IllegalArgumentException e)
                        {
                            targetList[i].add ("-1");
                        }
                    }
                }
            }
            catch (IOException e)
            {
                // Ignore
            }
        }
        
        switch (action)
        {
            case "USER" ->
            {
                int safeLimit = Math.min (names.size(), Math.min (scores.size(), Math.min (totalScores.size(), dates.size())));
                
                for (int i = 0; i < safeLimit; i++)
                {
                    String name = names.get(i), score = scores.get(i), totalScore = totalScores.get(i), date = dates.get(i);
                    
                    if (!name.equals ("-1") && !score.equals ("-1") && !totalScore.equals ("-1") && !date.equals ("-1"))
                    {
                        try
                        {
                            int validation = Integer.parseInt (score.trim());
                            
                            if (validation > 0 && !list.contains (name)) list.add (name);
                        }
                        catch (NumberFormatException e)
                        {
                            // Ignore
                        }
                    }
                }
            }
            case "HISTORY" ->
            {
                int safeLimit = Math.min (names.size(), Math.min (scores.size(), Math.min (totalScores.size(), dates.size())));
                
                for (int i = safeLimit - 1; i >= 0; i--)
                {
                    String name = names.get(i), score = scores.get(i), totalScore = totalScores.get(i), date = dates.get(i);
                    
                    if (name.trim().equalsIgnoreCase (targetName.trim()))
                    {
                        if (!score.equals ("-1") && !score.equals ("-2") && !totalScore.equals ("-1") && !totalScore.equals ("-2") && !date.equals ("-1"))
                        {
                            list.add (date);
                            list.add (score);
                            list.add (totalScore);
                        }
                    }
                }
            }
            case "ALL" ->
            {
                if (fieldName.equals ("NAME")) list.addAll (names);
                if (fieldName.equals ("SCORE")) list.addAll (scores);
                if (fieldName.equals ("TOTAL_SCORE")) list.addAll (totalScores);
                if (fieldName.equals ("DATE")) list.addAll (dates);
            }
        }
        
        return list;
    }
    
    static void appendForScoreHistory (String name, String score, String totalScore)
    {
        String currentDate = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").format (new Date());
        
        String[] files = {"name_history.csv", "score_history.csv", "totalScore_history.csv", "date_history.csv"};
        String[] data = {name, score, totalScore, currentDate};
        
        for (int i = 0; i < files.length; i++)
        {
            try
            {
                File file = new File (files[i]);
                
                if (!file.exists()) file.createNewFile();
                
                try (FileWriter fw = new FileWriter (file, true))
                {
                    String encrypt = Base64.getEncoder().encodeToString (data[i].getBytes());
                    
                    fw.write (encrypt + "\n");
                }
            }
            catch (IOException e)
            {
                // Ignore
            }
        }
    }
    
    static void overwriteForScoreHistory (List<String> list, String saveFileName)
    {
        overwriteForLeaderboard (list, saveFileName);
    }
    
    static void clearForScoreHistory (String name)
    {
        List<String> names = loadForScoreHistory ("ALL", null, "NAME");
        List<String> scores = loadForScoreHistory ("ALL", null, "SCORE");
        List<String> totalScores = loadForScoreHistory ("ALL", null, "TOTAL_SCORE");
        List<String> dates = loadForScoreHistory ("ALL", null, "DATE");
        
        List<String> validNames = new ArrayList<>(), validScores = new ArrayList<>(), validTotalScores = new ArrayList<>(), validDates = new ArrayList<>();
        
        int safeLimit = Math.min (names.size(), Math.min (scores.size(), Math.min (totalScores.size(), dates.size())));
        
        for (int i = 0; i < safeLimit; i++)
        {
            if (!names.get(i).trim().equalsIgnoreCase (name.trim()))
            {
                validNames.add (names.get(i));
                validScores.add (scores.get(i));
                validTotalScores.add (totalScores.get(i));
                validDates.add (dates.get(i));
            }
            else
            {
                validNames.add (names.get(i));
                validScores.add ("-2");
                validTotalScores.add ("-2");
                validDates.add (dates.get(i));
            }
        }
        
        boolean wipedOut = true;
        
        for (String validScore : validScores)
        {
            if (!validScore.equals ("-2"))
            {
                wipedOut = false;
                
                break;
            }
        }
        
        if (validNames.isEmpty() || wipedOut)
        {
            validNames.clear();
            validScores.clear();
            validTotalScores.clear();
            validDates.clear();
            
            validNames.add ("WIPED");
            validScores.add ("-1");
            validTotalScores.add ("-1");
            validDates.add ("1970-01-01");
        }
        
        overwriteForScoreHistory (validNames, "name_history.csv");
        overwriteForScoreHistory (validScores, "score_history.csv");
        overwriteForScoreHistory (validTotalScores, "totalScore_history.csv");
        overwriteForScoreHistory (validDates, "date_history.csv");
    }
}