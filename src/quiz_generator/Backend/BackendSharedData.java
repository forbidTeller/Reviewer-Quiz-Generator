package quiz_generator.Backend;

import java.util.List;
import java.util.*;
import java.io.*;

public class BackendSharedData 
{
    private File selectedFile;
    private String fileName;
    private String[] choices;
    private int size = 0, score = 0;
    private final int[] index = {0, 0, 0};
    private boolean usedOnce = true;
    
    private final Map<String, String> map = new HashMap<>();
    private final Map<String, String[]> savedChoices = new HashMap<>();
    private Map<String, String[]> extractStored;
    private List<String> questions, answers;
    
    // GETTERS
    
    public File getSelectedFile() { return selectedFile; }
    public String getFileName() { return fileName; }
    public String[] getChoices() { return choices; }
    public int getSize() { return size; }
    public int getScore() { return score; }
    public int[] getIndex() { return index; }
    public boolean isUsedOnce() { return usedOnce; }
    
    public Map<String, String> getMap() { return map; }
    public Map<String, String[]> getSavedChoices() { return savedChoices; }
    public Map<String, String[]> getExtractStored() { return extractStored; }
    public List<String> getQuestions() { return questions; }
    public List<String> getAnswers() { return answers; }
    
    // SETTERS
    
    public void setSelectedFile (File selectedFile) { this.selectedFile = selectedFile; }
    public void setFileName (String fileName) { this.fileName = fileName; }
    public void setChoices (String[] choices) { this.choices = choices; }
    public void setSize (int size) { this.size = size; }
    public void setScore (int score) { this.score = score; }
    public void setUsedOnce (boolean usedOnce) { this.usedOnce = usedOnce; }
    
    public void setExtractStored (Map<String, String[]> extractStored) { this.extractStored = extractStored; }
    public void setQuestions (List<String> questions) { this.questions = questions; }
    public void setAnswers (List<String> answers) { this.answers = answers; }
    
    // RESET METHODS
    
    public void partialReset (List<String> highlightedTerms)
    {
        index[0] = 0;
        index[1] = 0;
        index[2] = 0;
        
        score = 0;
        
        highlightedTerms.clear();
        
        map.clear();
        answers.clear();
        savedChoices.clear();
        choices = null;
        usedOnce = true;
    }
    
    public void multipleChoiceReset (List<String> highlightedTerms)
    {
        index[0] = 0;
        index[1] = 0;
        index[2] = 0;
        
        highlightedTerms.clear();
        
        map.clear();
        choices = null;
        savedChoices.clear();
    }
    
    public void identificationReset (List<String> highlightedTerms)
    {
        index[1] = 0;
        index[2] = 0;
        
        highlightedTerms.clear();
        
        map.clear();
        choices = null;
    }
    
    public void trueFalseReset (List<String> highlightedTerms)
    {
        index[1] = 0;
        index[2] = 0;
        
        highlightedTerms.clear();
        
        map.clear();
        extractStored.clear();
        choices = null;
        savedChoices.clear();
    }
    
    public void retryReset()
    {
        questions.clear();
        answers.clear();
        
        score = 0;
        
        usedOnce = true;
    }
}