package quiz_generator.Frontend;

import javax.swing.*;
import java.awt.*;

class Background extends JPanel
{
    private ImageIcon icon;
    
    Background (String filePath)
    {
        icon = new ImageIcon (getClass().getResource (filePath));
        setLayout (null);
    }
    
    @Override
    protected void paintComponent (Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage (icon.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
    
    void setBackgroundImage (String filePath)
    {
        icon = new ImageIcon (getClass().getResource (filePath));
        repaint();
    }
}