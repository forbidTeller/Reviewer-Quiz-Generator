package quiz_generator.Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import java.text.*;

class Displayer 
{
    static JScrollPane displayUser (JFrame frame, CardLayout layout, List<String> highlightedTerms, List<String> names, Leaderboard leaderboard)
    {
        JPanel container = new JPanel();
        container.setLayout (new BoxLayout (container, BoxLayout.Y_AXIS));
        container.setOpaque (false);
        
        String[] nameArray = names.toArray (String[] :: new);
        
        for (String name : nameArray)
        {
            ImageIcon box = Worker.getDecoratorImage ("/quiz_generator/Design/139.png", 1070, 76);
            
            JLabel nameLabel = new JLabel (name, box, SwingConstants.CENTER);
            nameLabel.setHorizontalTextPosition (JLabel.CENTER);
            nameLabel.setVerticalTextPosition (JLabel.CENTER);
            nameLabel.setFont (Worker.getFont ("/quiz_generator/Font/Dekko-Regular.ttf", 1, 42.6f));
            nameLabel.setForeground (Color.DARK_GRAY);
            nameLabel.setBounds (Worker.getBounds (0, 0, 1070, 76));
            
            JLabel hitbox = new JLabel();
            hitbox.setCursor (new Cursor (Cursor.HAND_CURSOR));
            hitbox.setOpaque (false);
            hitbox.setBounds (Worker.getBounds (0, 0, 1070, 76));
            
            Dimension lockSize = new Dimension (1070, 76);
            
            JLayeredPane rowWrapper = new JLayeredPane();           
            rowWrapper.setPreferredSize (lockSize);
            rowWrapper.setMaximumSize (lockSize);
            rowWrapper.setMinimumSize (lockSize);
            
            rowWrapper.add (nameLabel, Integer.valueOf(0));
            rowWrapper.add (hitbox, Integer.valueOf(1));
            
            container.add (rowWrapper);
            
            final boolean[] mouseLock = {false};
            
            hitbox.addMouseListener (new MouseAdapter()
            {
                @Override
                public void mouseEntered (MouseEvent e)
                {
                    if (mouseLock[0]) return;
                    
                    ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/140.png", 1070, 76);
                    nameLabel.setIcon (decoratorImage);
                }
                
                @Override
                public void mouseExited (MouseEvent e)
                {
                    if (mouseLock[0]) return;
                    
                    ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/139.png", 1070, 76);
                    nameLabel.setIcon (decoratorImage);
                }
                
                @Override
                public void mousePressed (MouseEvent e)
                {
                    if (mouseLock[0]) return;
                    
                    mouseLock[0] = true;
                    
                    new SwingWorker<Void, Void>()
                    {
                        @Override
                        protected Void doInBackground() throws Exception
                        {
                            return null;
                        }
                        
                        @Override
                        protected void done()
                        {
                            leaderboard.changeComponents (frame);
                            
                            leaderboard.frontend.setPanel (null);
                            leaderboard.frontend.setNextPanel (FrontendSharedData.Page.SCORE_HISTORY_PAGE_2);
                            leaderboard.frontend.setPanel (leaderboard.background());
                            leaderboard.frontend.getPanel().setLayout (layout);
                            leaderboard.frontend.getPanel().add (leaderboard.individualScoreView (frame, layout, highlightedTerms, name), "individualScoreView");
                            
                            layout.show (leaderboard.frontend.getPanel(), "individualScoreView");
                            
                            frame.add (leaderboard.frontend.getPanel());
                            frame.revalidate();
                            frame.repaint();
                        }
                        
                    }.execute();
                }
                
            });
        }
        
        JPanel viewportPanel = new JPanel (new BorderLayout());
        viewportPanel.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 20));
        viewportPanel.setOpaque (false);
        viewportPanel.add (container, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane (viewportPanel);
        scrollPane.setBounds (Worker.getBounds (386, 402, 1130, 518));
        
        String[] scrollBarImages = {"/quiz_generator/Design/143.png", "/quiz_generator/Design/144.png", "/quiz_generator/Design/141.png", "/quiz_generator/Design/142.png", "/quiz_generator/Design/145.png", "/quiz_generator/Design/146.png", "/quiz_generator/Design/147.png"};
        
        ImageScrollBar.getScrollBar (scrollPane, scrollBarImages, 20);
        
        return scrollPane;
    }
    
    static JScrollPane displayScoreHistory (List<String> records)
    {
        JPanel container = new JPanel();
        container.setLayout (new BoxLayout (container, BoxLayout.Y_AXIS));
        container.setOpaque (false);
        
        String[] recordArray = records.toArray (String[] :: new);
        
        for (int i = 0; i < recordArray.length; i += 3)
        {
            String date = recordArray[i], score = recordArray[i + 1], totalScore = recordArray[i + 2];
            
            try
            {
                Date parsedDate = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").parse (date);
                
                date = new SimpleDateFormat ("MM/dd/yy").format (parsedDate);
            }
            catch (ParseException e)
            {
                // Ignore
            }
            
            String boxImage;
            
            if (i == 0) boxImage = "/quiz_generator/Design/151.png"; else boxImage = "/quiz_generator/Design/152.png";
            
            ImageIcon box = Worker.getDecoratorImage (boxImage, 1135, 103);
            
            JLabel label = new JLabel (box);
            label.setBounds (Worker.getBounds (0, 0, 1135, 103));
            
            String[] record = {date, score, totalScore};
            int[] x = {20, 440, 895}, width = {150, 100, 100};
            
            JLabel[] texts = new JLabel[record.length];
            
            for (int j = 0; j < record.length; j++)
            {
                texts[j] = new JLabel (record[j], SwingConstants.CENTER);
                texts[j].setFont (Worker.getFont ("/quiz_generator/Font/Nunito-Regular.ttf", 1, 25.0f));
                texts[j].setForeground (Color.BLACK);
                texts[j].setBounds (Worker.getBounds (x[j], 0, width[j], 103));
            }
            
            Dimension lockSize = new Dimension (1135, 103);
            
            JLayeredPane rowWrapper = new JLayeredPane();
            rowWrapper.setPreferredSize (lockSize);
            rowWrapper.setMaximumSize (lockSize);
            rowWrapper.setMinimumSize (lockSize);
            
            rowWrapper.add (label, Integer.valueOf(0));
            
            for (JLabel text : texts)
            {
                rowWrapper.add (text, Integer.valueOf(1));
            }
            
            container.add (rowWrapper);
        }
        
        JPanel viewportPanel = new JPanel (new BorderLayout());
        viewportPanel.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 30));
        viewportPanel.setOpaque (false);
        viewportPanel.add (container, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane (viewportPanel);
        scrollPane.setHorizontalScrollBarPolicy (ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds (Worker.getBounds (327, 586, 1225, 413));
        
        String[] scrollBarImages = {"/quiz_generator/Design/155.png", "/quiz_generator/Design/156.png", "/quiz_generator/Design/153.png", "/quiz_generator/Design/154.png", "/quiz_generator/Design/157.png", "/quiz_generator/Design/158.png", "/quiz_generator/Design/159.png"};
        
        ImageScrollBar.getScrollBar (scrollPane, scrollBarImages, 20);
        
        return scrollPane;
    }
    
    static JScrollPane displayNoResultFound()
    {
        JPanel panel = new JPanel (new GridBagLayout());
        panel.setOpaque (false);
        
        JLabel label = new JLabel ("No Result Found", SwingConstants.CENTER);
        label.setFont (Worker.getFont ("/quiz_generator/Font/Nunito-Regular.ttf", 1, 42.6f));
        label.setForeground (Color.BLACK);
        
        panel.add (label);
        
        JScrollPane scrollPane = new JScrollPane (panel);
        scrollPane.setBorder (BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque (false);
        scrollPane.setOpaque (false);
        scrollPane.setBounds (Worker.getBounds (327, 586, 1225, 413));
        
        return scrollPane;
    }
}