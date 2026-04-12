package quiz_generator.Backend;

import org.apache.poi.xwpf.usermodel.*;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.List;
import java.util.regex.*;
import java.util.*;
import java.io.*;

class MainProcessor 
{
    // THE PROCESS WHERE TERMS AND DEFINITIONS ARE BOTH EXTRACTED AND SEPARATED
    
    static String supportedFileName (BackendSharedData backend, List<String> highlightedTerms) throws Exception
    {
        String text;
        
        if (backend.getFileName().endsWith (".txt"))
        {
            StringBuilder sb = new StringBuilder();
            
            try (BufferedReader br = new BufferedReader (new FileReader (backend.getSelectedFile()))) 
            {
                Pattern marker = Pattern.compile ("\\*\\*(.*?)\\*\\*");
                
                String line;
                
                while ((line = br.readLine()) != null)
                {
                    if (line.trim().isEmpty()) continue;
                    
                    Matcher m = marker.matcher (line);
                    
                    while (m.find())
                    {
                        String term = m.group(1).trim();
                        
                        if (!term.isEmpty()) highlightedTerms.add (term);
                    }
                    
                    String cleaned = line.replaceAll ("\\*\\*(.*?)\\*\\*", "$1");
                    
                    if (!cleaned.matches (".*[.!?]$")) cleaned = cleaned + ".";
                    
                    sb.append (cleaned).append(" ");
                }
            }
            
            text = sb.toString();
        }
        else if (backend.getFileName().endsWith (".docx"))
        {
            try (FileInputStream fis = new FileInputStream (backend.getSelectedFile()); 
                 XWPFDocument doc = new XWPFDocument (fis))
            {                
                StringBuilder sb = new StringBuilder();
                
                for (XWPFParagraph paragraph : doc.getParagraphs())
                {
                    List<XWPFRun> runs = paragraph.getRuns();
                    
                    if (runs != null)
                    {
                        StringBuilder currentHighlight = new StringBuilder();
                        
                        boolean inHighlight = false;
                        
                        for (XWPFRun run : runs)
                        {
                            String runText = run.text();
                            
                            if (runText == null) runText = "";
                            
                            if (run.isBold())
                            {
                                if (inHighlight)
                                {
                                    currentHighlight.append(" ").append (runText);
                                }
                                else
                                {
                                    currentHighlight.setLength(0);
                                    currentHighlight.append (runText);
                                    
                                    inHighlight = true;
                                }
                            }
                            else
                            {
                                if (inHighlight)
                                {
                                    String detected = currentHighlight.toString().trim();
                                    
                                    if (!detected.isEmpty()) highlightedTerms.add (detected);
                                    
                                    currentHighlight.setLength(0);
                                    
                                    inHighlight = false;
                                }
                            }
                            
                            sb.append (runText).append(" ");
                        }
                        
                        if (inHighlight && currentHighlight.length() > 0)
                        {
                            String detected = currentHighlight.toString().trim();
                            
                            if (!detected.isEmpty()) highlightedTerms.add (detected);
                        }
                    }
                    
                    sb.append ("\n");
                }
                
                text = sb.toString();
                
                doc.close();
                fis.close();
            }
        }
        else
        {
            try (PDDocument document = PDDocument.load (backend.getSelectedFile()))
            {
                HighlightedPDF hPDF = new HighlightedPDF();
                
                text = hPDF.getText (document);
                
                highlightedTerms.clear();
                highlightedTerms.addAll (hPDF.getHighlightedTerms());
                
                document.close();
            }
        }
        
        return text;
    }
    
    static String safeGroup (Matcher matcher, int index)
    {
        try
        {
            String groupText = matcher.group (index);
            
            return groupText == null ? "" : groupText.trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }
    
    static String normalizeTerm (String term)
    {
        if (term == null) return "";
        
        return term.replaceAll ("(?i)^(?:a|an|the)\\s+", "").trim();
    }
    
    static String pickLastHighlightInSentence (String sentence, List<String> highlightedTerms)
    {
        String last = "";
        
        for (String highlight : highlightedTerms)
        {
            if (highlight == null || highlight.isEmpty()) continue;
            
            Pattern pattern = Pattern.compile ("\\b" + Pattern.quote (highlight) + "\\b", Pattern.CASE_INSENSITIVE);
            
            if (pattern.matcher (sentence).find()) last = highlight;
        }
        
        return last;
    }
    
    static boolean appearsBeforeVerb (String sentence, String term)
    {
        Pattern verb = Pattern.compile ("\\b(is|are|means|refers to|leverages|represents|consists|involves|measures|describes|occurs|allows|helps|stores|translates|stands for|is defined as|can be defined as)\\b", Pattern.CASE_INSENSITIVE);
        
        Matcher m = verb.matcher (sentence);
        
        if (!m.find()) return false;
        
        int verbIndex = m.start(), termIndex = sentence.toLowerCase().indexOf (term.toLowerCase());
        
        return termIndex >= 0 && termIndex < verbIndex;
    }
    
    static void extractTermAndDefinition (BackendSharedData backend, List<String> highlightedTerms) throws Exception
    {
        String text = supportedFileName (backend, highlightedTerms);
        
        text = text.replaceAll ("(?<![.!?])\\r?\\n+", " ");
        text = text.replaceAll ("\\s+", " ").trim();
        
        String[] sentences = text.split ("(?<=[.!?])\\s+");
        
        Pattern pAppositive = Pattern.compile ("(?i)\\b([A-Za-z0-9\\-'' ]+?)\\b\\s*,\\s*(?:also called|also known as|called)\\s+(.*?)(?=, \\s*is\\s)\\s*,\\s*is\\s+(.*)");
        Pattern pTermFirst = Pattern.compile ("(?i)^\\s*([A-Za-z0-9\\-’' ]+?)\\s+(is|are|means|refers to|leverages|represents|consists|involves|measures|describes|occurs|allows|helps|stores|translates|stands for|is defined as|can be defined as)\\s+(.*)$");
        Pattern pDefinitionFirst = Pattern.compile ("(?i)^(.*?)\\s+(is called|is known as|is referred to as)\\s+([A-Za-z][A-Za-z0-9\\-’' ]+?)\\.?\\s*$");
        
        for (String sentence : sentences)
        {
            String s = sentence.trim();
            
            if (s.isEmpty()) continue;
            
            String rawTerm = "", definition = "";
            
            Matcher m;
            m = pAppositive.matcher(s);
            
            if (m.find())
            {
                rawTerm = safeGroup (m, 1);
                definition = safeGroup (m, 3);
            }
            else
            {
                m = pDefinitionFirst.matcher(s);
                
                if (m.find())
                {
                    String before = safeGroup (m, 1).trim(), connector = safeGroup (m, 2).trim();
                    
                    rawTerm = safeGroup (m, 3).trim();
                    definition = (before + " " + connector).trim();
                }
                else
                {
                    m = pTermFirst.matcher(s);
                    
                    if (m.find())
                    {
                        rawTerm = safeGroup (m, 1).trim();
                        
                        String connector = safeGroup (m, 2).trim(), rest = safeGroup (m, 3).trim();
                        
                        definition = (connector + " " + rest).trim();
                    }
                    else
                    {
                        continue;
                    }
                }
            }
            
            String termFromHighlight = pickLastHighlightInSentence (s, highlightedTerms);
            
            if (!termFromHighlight.isEmpty() && appearsBeforeVerb (s, termFromHighlight)) rawTerm = termFromHighlight;
            
            rawTerm = normalizeTerm (rawTerm).replaceAll ("\\.$", "").trim();
            definition = definition.replaceAll ("\\s+", " ").trim();
            definition = definition.replaceAll ("\\.$", "").trim();
            
            if (!rawTerm.isEmpty() && !definition.isEmpty())
            {
                definition = definition.replaceAll ("(?i)\\b" + Pattern.quote (rawTerm) + "\\b", "").trim();
                definition = definition.replaceAll ("\\s+", " ").trim();
            }
            
            if (!rawTerm.isEmpty() && !definition.isEmpty())
            {
                backend.getMap().put (definition.toUpperCase(), rawTerm.toUpperCase());
            }
        }
        
        backend.setAnswers (new ArrayList<>(backend.getMap().values()));
    }
}