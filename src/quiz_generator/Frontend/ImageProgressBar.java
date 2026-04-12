package quiz_generator.Frontend;

import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.*;
import java.awt.*;

class ImageProgressBar 
{
    static JProgressBar getProgressBar (String[] paths, int width, int height)
    {
        JProgressBar progressBar = new JProgressBar (0, 100);
        progressBar.setBorderPainted (false);
        progressBar.setOpaque (false);
        
        Image emptyBarImage = Worker.getDecoratorImage (paths[0], width, height).getImage();
        Image filledBarImage = Worker.getDecoratorImage (paths[1], width, height).getImage();
        
        progressBar.setUI (new BasicProgressBarUI()
        {
            @Override
            protected void paintDeterminate (Graphics g, JComponent c)
            {
                Graphics2D g2D = (Graphics2D) g.create();
                
                int barWidth = progressBar.getWidth(), barHeight = progressBar.getHeight();
                
                g2D.drawImage (emptyBarImage, 0, 0, barWidth, barHeight, null);
                
                double percent = progressBar.getPercentComplete();
                int fillWidth = (int) (barWidth * percent);
                
                g2D.setClip (0, 0, fillWidth, barHeight);
                g2D.drawImage (filledBarImage, 0, 0, barWidth, barHeight, null);
                g2D.setClip (null);
                
                if (progressBar.isStringPainted())
                {
                    String text = progressBar.getString();
                    
                    g2D.setRenderingHint (RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2D.setFont (progressBar.getFont());
                        
                    FontMetrics fm = g2D.getFontMetrics();
                        
                    int textWidth = fm.stringWidth (text), textHeight = fm.getAscent(), textX = (barWidth - textWidth) / 2, textY = (barHeight - fm.getHeight()) / 2 + textHeight;
                        
                    Color endColor = new Color (52, 50, 199);
                        
                    GradientPaint gradientBrush = new GradientPaint (textX, 0, Color.BLACK, textX + textWidth, 0, endColor);
                        
                    g2D.setPaint (gradientBrush);
                    g2D.drawString (text, textX, textY);
                }
                
                g2D.dispose();
            }
                      
        });
        
        return progressBar;
    }
}