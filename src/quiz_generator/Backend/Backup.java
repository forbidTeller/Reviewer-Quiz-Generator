package quiz_generator.Backend;

import java.util.List;
import java.util.*;
import java.io.*;

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
                    list.add (line);
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
                fw.write (appended + "\n");
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
                fw.write (s + "\n");
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
}
