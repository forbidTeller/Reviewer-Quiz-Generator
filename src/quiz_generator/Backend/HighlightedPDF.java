package quiz_generator.Backend;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.util.List;
import java.util.*;
import java.io.*;

class HighlightedPDF extends PDFTextStripper
{
    List<String> highlightedTerms = new ArrayList<>();
    
    public HighlightedPDF() throws Exception
    {
        super();
    }
    
    @Override
    protected void writeString (String text, List<TextPosition> textPositions)
    {
        StringBuilder sb = new StringBuilder();
        StringBuilder highlightedWord = new StringBuilder();
        
        boolean lastHighlighted = false;
        
        for (TextPosition position : textPositions)
        {
            String unicode = position.getUnicode();
            
            boolean isHighlighted = false;
            
            try
            {
                isHighlighted = position.getFont().getName().toLowerCase().contains ("bold");
            }
            catch (Exception e)
            {
                // Ignore
            }
            
            if (isHighlighted)
            {
                highlightedWord.append (unicode);
                
                lastHighlighted = true;
            }
            else
            {
                if (lastHighlighted && highlightedWord.length() > 0)
                {
                    String candidate = highlightedWord.toString().trim();
                    
                    if (!candidate.isEmpty()) highlightedTerms.add (candidate);
                    
                    highlightedWord.setLength(0);
                }
                
                lastHighlighted = false;
            }
            
            sb.append (unicode);
        }
        
        try
        {
            super.writeString (sb.toString());
        }
        catch (IOException e)
        {
            // Ignore
        }
    }
    
    List<String> getHighlightedTerms()
    {
        return highlightedTerms;
    }
}