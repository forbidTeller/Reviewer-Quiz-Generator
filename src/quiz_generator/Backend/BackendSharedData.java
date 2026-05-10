package quiz_generator.Backend;

import java.util.List;
import java.util.*;
import java.io.*;

public class BackendSharedData implements Backend
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
    
    @Override public File getSelectedFile() { return selectedFile; }
    @Override public String getFileName() { return fileName; }
    @Override public String[] getChoices() { return choices; }
    @Override public int getSize() { return size; }
    @Override public int getScore() { return score; }
    @Override public int[] getIndex() { return index; }
    @Override public boolean isUsedOnce() { return usedOnce; }
    
    @Override public Map<String, String> getMap() { return map; }
    @Override public Map<String, String[]> getSavedChoices() { return savedChoices; }
    @Override public Map<String, String[]> getExtractStored() { return extractStored; }
    @Override public List<String> getQuestions() { return questions; }
    @Override public List<String> getAnswers() { return answers; }
    
    @Override public void setSelectedFile (File selectedFile) { this.selectedFile = selectedFile; }
    @Override public void setFileName (String fileName) { this.fileName = fileName; }
    @Override public void setChoices (String[] choices) { this.choices = choices; }
    @Override public void setSize (int size) { this.size = size; }
    @Override public void setScore (int score) { this.score = score; }
    @Override public void setUsedOnce (boolean usedOnce) { this.usedOnce = usedOnce; }
    
    @Override public void setExtractStored (Map<String, String[]> extractStored) { this.extractStored = extractStored; }
    @Override public void setQuestions (List<String> questions) { this.questions = questions; }
    @Override public void setAnswers (List<String> answers) { this.answers = answers; }
    
    // RESET METHODS
    
    @Override
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
    
    @Override
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
    
    @Override
    public void identificationReset (List<String> highlightedTerms)
    {
        index[1] = 0;
        index[2] = 0;
        
        highlightedTerms.clear();
        
        map.clear();
        choices = null;
    }
    
    @Override
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
    
    @Override
    public void retryReset()
    {
        questions.clear();
        answers.clear();
        
        score = 0;
        
        usedOnce = true;
    }
}