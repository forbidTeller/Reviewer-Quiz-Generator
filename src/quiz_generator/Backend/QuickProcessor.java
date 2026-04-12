package quiz_generator.Backend;

import java.util.List;

public class QuickProcessor 
{
    public static String[] triggeredChoice (BackendSharedData backend, List<String> highlightedTerms) throws Exception
    {
        return switch (backend.getIndex()[1])
        {
            case 1 -> QuizTypeProcessor.multipleChoice (backend, highlightedTerms);
            case 2 -> QuizTypeProcessor.identification (backend, highlightedTerms);
            case 3 -> QuizTypeProcessor.trueFalse (backend, highlightedTerms);
            default -> null;
        };
    }
    
    public static String addLineBreaks (BackendSharedData backend, String text)
    {
        String[] words = text.split ("\\s+");
        
        StringBuilder sb = new StringBuilder ("<html>");
        
        int wordsInLine = 0, lineLength = 0;
        
        for (int i = 0; i < words.length; i++)
        {
            String word = words[i];
            
            if (wordsInLine == 5 || (lineLength + word.length()) > 35)
            {
                sb.append ("<br>");
                
                wordsInLine = 0;
                lineLength = 0;
            }
            
            sb.append (word);
            
            wordsInLine++;
            lineLength += word.length();
            
            if (i < words.length - 1)
            {
                sb.append (" ");
                
                lineLength++;
            }
        }
        
        switch (backend.getIndex()[1])
        {
            case 1, 2 -> sb.append("?");
            case 3 -> sb.append(".");
        }
        
        sb.append ("</html>");
        
        return sb.toString();
    }
}