package quiz_generator.Frontend;

import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.*;
import java.awt.*;

class ImageScrollBar 
{
    static void getScrollBar (JScrollPane scrollPane, String[] scrollBarImages, int size)
    {
        scrollPane.setOpaque (false);
        scrollPane.getViewport().setOpaque (false);
        scrollPane.setBorder (BorderFactory.createEmptyBorder());
        
        scrollPane.getVerticalScrollBar().setUI (new BasicScrollBarUI()
        {
            @Override
            protected JButton createIncreaseButton (int orientation)
            {
                JButton button = new JButton();
                button.setIcon (Worker.getDecoratorImage (scrollBarImages[0], size, size));
                button.setRolloverIcon (Worker.getDecoratorImage (scrollBarImages[1], size, size));
                button.setContentAreaFilled (false);
                button.setBorderPainted (false);
                button.setFocusPainted (false);
                
                return button;
            }
            
            @Override
            protected JButton createDecreaseButton (int orientation)
            {
                JButton button = new JButton();
                button.setIcon (Worker.getDecoratorImage (scrollBarImages[2], size, size));
                button.setRolloverIcon (Worker.getDecoratorImage (scrollBarImages[3], size, size));
                button.setContentAreaFilled (false);
                button.setBorderPainted (false);
                button.setFocusPainted (false);
                
                return button;
            }
            
            @Override
            protected void paintTrack (Graphics g, JComponent c, Rectangle trackBounds)
            {
                Image trackImage = Worker.getDecoratorImage (scrollBarImages[4], trackBounds.width, trackBounds.height).getImage();
                
                g.drawImage (trackImage, trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, null);
            }
            
            @Override
            protected void paintThumb (Graphics g, JComponent c, Rectangle thumbBounds)
            {
                Image thumbImage;
                
                if (isThumbRollover())
                {
                    thumbImage = Worker.getDecoratorImage (scrollBarImages[6], thumbBounds.width, thumbBounds.height).getImage();
                }
                else
                {
                    thumbImage = Worker.getDecoratorImage (scrollBarImages[5], thumbBounds.width, thumbBounds.height).getImage();
                }
                
                g.drawImage (thumbImage, thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, null);
            }
            
        });
        
        scrollPane.getVerticalScrollBar().setPreferredSize (new Dimension (size, Integer.MAX_VALUE));
        scrollPane.getVerticalScrollBar().setUnitIncrement (16);
    }
}