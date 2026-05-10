
package quiz_generator.Backend;

import java.util.List;
import java.util.*;
import java.io.*;

public interface Backend 
{
    // IDK.... ABSTRACTION + ENCAPSULATION APPROACH HAHAHAHAAHAHAHAHAHAHAHA
    
    File getSelectedFile();
    String getFileName();
    String[] getChoices();
    int getSize();
    int getScore();
    int[] getIndex();
    boolean isUsedOnce();
    
    Map<String, String> getMap();
    Map<String, String[]> getSavedChoices();
    Map<String, String[]> getExtractStored();
    List<String> getQuestions();
    List<String> getAnswers();
    
    void setSelectedFile (File selectedFile);
    void setFileName (String fileName);
    void setChoices (String[] choices);
    void setSize (int size);
    void setScore (int score);
    void setUsedOnce (boolean usedOnce);
    
    void setExtractStored (Map<String, String[]> extractStored);
    void setQuestions (List<String> questions);
    void setAnswers (List<String> answers);
    
    void partialReset (List<String> highlightedTerms);
    void multipleChoiceReset (List<String> highlightedTerms);
    void identificationReset (List<String> highlightedTerms);
    void trueFalseReset (List<String> highlightedTerms);
    void retryReset();
}