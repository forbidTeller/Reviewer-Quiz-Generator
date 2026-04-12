package quiz_generator.Frontend;

import javax.swing.*;
import java.awt.*;
import java.io.*;

class Worker 
{
    // REPEATED WORKLOADS INTO ONE METHOD / REUSABLE
    
    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    static final double SCALEX = screenSize.width / 1920.0;
    static final double SCALEY = screenSize.height / 1080.0;
    
    static Rectangle getBounds (int x, int y, int width, int height)
    {
        int dynamicX = (int) (x * SCALEX), dynamicY = (int) (y * SCALEY), dynamicWidth = (int) (width * SCALEX), dynamicHeight = (int) (height * SCALEY);
        
        return new Rectangle (dynamicX, dynamicY, dynamicWidth, dynamicHeight);
    }
    
    static ImageIcon getDecoratorImage (String path, int width, int height)
    {
        int dynamicWidth = (int) (width * SCALEX), dynamicHeight = (int) (height * SCALEY);
                
        ImageIcon decoratorImage = new ImageIcon (Worker.class.getResource (path));
        ImageIcon newDecoratorImage = new ImageIcon (decoratorImage.getImage().getScaledInstance (dynamicWidth, dynamicHeight, Image.SCALE_SMOOTH));
        
        return newDecoratorImage;
    }
    
    static ImageIcon[] getDecoratorImages (String[] paths, int width, int height)
    {
        int dynamicWidth = (int) (width * SCALEX), dynamicHeight = (int) (height * SCALEY);
        
        ImageIcon[] newDecoratorImages = new ImageIcon[paths.length];
        
        for (int i = 0; i < paths.length; i++)
        {
            ImageIcon decoratorImage = new ImageIcon (Worker.class.getResource (paths[i]));
            Image decoratorImg = decoratorImage.getImage().getScaledInstance (dynamicWidth, dynamicHeight, Image.SCALE_SMOOTH);
            newDecoratorImages[i] = new ImageIcon (decoratorImg);
        }
        
        return newDecoratorImages;
    }
    
    static String getGif (String path, int width, int height)
    {
        int dynamicWidth = (int) (width * SCALEX), dynamicHeight = (int) (height * SCALEY);
        
        String resource = Worker.class.getResource (path).toString();
        String gif = "<html><img src = '" + resource + "' width = '" + dynamicWidth + "' height = '" + dynamicHeight + "'></html>";
        
        return gif;
    }
    
    static Font getFont (String path, int format, float size)
    {
        try
        {
            InputStream fontStream = Worker.class.getResourceAsStream (path);
            
            Font baseFont = Font.createFont (Font.TRUETYPE_FONT, fontStream);
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont (baseFont);
            
            return baseFont.deriveFont (format, size);
        }
        catch (FontFormatException | IOException e)
        {
            return new Font ("SanSerif", Font.PLAIN, (int) size);
        }
    }
    
    static void autoScaleLabel (String fontPath, JLabel label, String text, float fontSize, int unscaledWidth, int unscaledHeight)
    {
        int maxWidth = (int) (unscaledWidth * SCALEX), maxHeight = (int) (unscaledHeight * SCALEY);
        float size = fontSize;
        
        while (size > 10.0f)
        {
            Font font = getFont (fontPath, 1, size);
            
            FontMetrics fm = label.getFontMetrics (font);
            
            String[] words = text.split(" ");
            
            int lines = 1, curLineW = 0;
            boolean fits = true;
            
            for (String word : words)
            {
                int wordW = fm.stringWidth (word);
                
                if (wordW > maxWidth)
                {
                    fits = false;
                    
                    break;
                }
                
                if (curLineW + wordW > maxWidth)
                {
                    lines++;
                    
                    curLineW = wordW + fm.stringWidth(" ");
                }
                else
                {
                    curLineW += (wordW + fm.stringWidth(" "));
                }
            }
            
            if (fits && (lines * fm.getHeight() <= maxHeight)) break;
            
            size -= 1.0f;
        }
        
        label.setFont (getFont (fontPath, 1, size));
        label.setText ("<html><div style='width: " + maxWidth + "px; text-align: left;'>" + text + "</div></html>");
    }
}