package quiz_generator.Frontend;

import javax.swing.text.*;
 
class LimitFilter extends DocumentFilter
{
    private final int limit;
    private final boolean isNumeric;
    
    LimitFilter (int limit, boolean isNumeric)
    {
        this.limit = limit;
        this.isNumeric = isNumeric;
    }
    
    @Override
    public void insertString (FilterBypass fb, int offSet, String str, AttributeSet attr) throws BadLocationException
    {
        if (str == null) return;
        
        if (!str.matches ("\\d*") && isNumeric) return;
        
        if ((fb.getDocument().getLength() + str.length()) <= limit) super.insertString (fb, offSet, str, attr);
    }
    
    @Override
    public void replace (FilterBypass fb, int offSet, int length, String text, AttributeSet attrs) throws BadLocationException
    {
        if (text == null) return;
        
        if (!text.matches ("\\d*") && isNumeric) return;
        
        if ((fb.getDocument().getLength() + text.length() - length) <= limit) super.replace (fb, offSet, length, text, attrs);
    }
}