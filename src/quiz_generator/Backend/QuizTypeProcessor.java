package quiz_generator.Backend;

import java.util.List;
import java.util.regex.*;
import java.util.*;

public class QuizTypeProcessor 
{
    // EXTRACTED AND SEPARATED TERMS AND DEFINITIONS WILL BE USED TO CREATE THE THREE QUIZ TYPES
    
    static String[] multipleChoice (BackendSharedData backend, List<String> highlightedTerms) throws Exception
    {
        if (backend.isUsedOnce())
        {
            MainProcessor.extractTermAndDefinition (backend, highlightedTerms);
            
            backend.setUsedOnce (false);
        }
        
        String[] randomTerms = new String[4];
        
        List<String> values = new ArrayList<>(backend.getMap().values());
        Collections.shuffle (values);
        
        if (backend.getSize() != 0)
        {
            randomTerms[3] = backend.getAnswers().get (backend.getIndex()[0]);
            values.remove (backend.getAnswers().get (backend.getIndex()[0]));
            backend.getIndex()[0]++;
            backend.setSize (backend.getSize() - 1);
        }
        
        for (int i = 0; i < 3; i++)
        {
            randomTerms[i] = values.get(i);
        }
        
        List<String> temp = Arrays.asList (randomTerms);
        Collections.shuffle (temp);
        
        return temp.toArray (randomTerms);
    }
    
    static String[] identification (BackendSharedData backend, List<String> highlightedTerms) throws Exception
    {
        if (backend.isUsedOnce())
        {
            MainProcessor.extractTermAndDefinition (backend, highlightedTerms);
            
            backend.setUsedOnce (false);
        }
        
        if (backend.getAnswers() == null || backend.getAnswers().isEmpty()) throw new Exception();
        
        List<String> blanks = new ArrayList<>();
        
        for (String answer : backend.getAnswers())
        {
            String blank = answer.replaceAll ("[^ ]", "_ ").trim();
            
            blanks.add (blank);
        }
        
        return blanks.toArray (String[] :: new);
    }
    
    static String[] trueFalse (BackendSharedData backend, List<String> highlightedTerms) throws Exception
    {
        if (backend.isUsedOnce())
        {
            MainProcessor.extractTermAndDefinition (backend, highlightedTerms);
            
            backend.setUsedOnce (false);
        }
        
        if (backend.getAnswers() == null || backend.getAnswers().isEmpty()) throw new Exception();
        
        String[] answersArray = backend.getAnswers().toArray (String[] :: new);
        String[] randomizedAnswers = new String[answersArray.length];
        
        Random random = new Random();
        
        for (int i = 0; i < answersArray.length; i++)
        {
            randomizedAnswers[i] = answersArray[random.nextInt (answersArray.length)];
        }
        
        return randomizedAnswers;
    }
    
    static String reconstructTrueFalse (String term, String definition)
    {
        if (term == null || definition == null) return "";
        
        String def = definition.trim();
        
        Pattern pattern = Pattern.compile ("(?i)^(.*?)(\\b(is|are|was|were)\\s+(called|known as|referred to as|termed)\\b)\\s*$");
        
        Matcher m = pattern.matcher (def);
        
        if (m.find()) return m.group(1).trim() + " " + m.group(2).trim() + " " + term;
        if (def.matches ("(?i)^(is|are|was|were)\\b.*")) return term + " " + def;
        if (def.matches ("(?i)^(uses|leverages|creates|shows|marks|facilitates|enables|allows|helps|stores|translates)\\b.*")) return term + " " + def;
        if (def.matches ("(?i)^(the|a|an|this|these|those)\\b.*")) return term + " IS " + def;
        
        return term + " IS " + def;
    }
    
    public static Map<String, String[]> extractTrueFalse (BackendSharedData backend)
    {
        Map<String, String[]> stored = new HashMap<>();
        
        List<String> correctList = new ArrayList<>();
        List<String> randomizedList = new ArrayList<>();
        
        for (int i = 0; i < backend.getQuestions().size(); i++)
        {
            String x = reconstructTrueFalse (backend.getAnswers().get(i), backend.getQuestions().get(i));
            String y = (i < backend.getQuestions().size() / 2) ? reconstructTrueFalse (backend.getChoices()[i], backend.getQuestions().get(i)) : reconstructTrueFalse (backend.getAnswers().get(i), backend.getQuestions().get(i));
            
            correctList.add(x);
            randomizedList.add(y);
        }
        
        Collections.shuffle (randomizedList);
        
        stored.put ("correctArray", correctList.toArray (String[] :: new));
        stored.put ("randomizedArray", randomizedList.toArray (String[] :: new));
        
        return stored;
    }
}