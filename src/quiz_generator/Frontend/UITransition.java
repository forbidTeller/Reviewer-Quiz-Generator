package quiz_generator.Frontend;

import quiz_generator.Backend.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

class UITransition extends MainComponent
{
    // CONSTRUCTOR
    
    UITransition (Backend backend, Frontend frontend)
    {
        super (backend, frontend);
    }
    
    void quickLoadingScreen (JFrame frame)
    {
        if (frontend.getPanel() != null)
        {
            changeComponents (frame);
            
            frontend.setPanel (null);
            frontend.setNextPanel (FrontendSharedData.Page.LOADING_SCREEN);
            frontend.setPanel (background());
            
            frame.add (frontend.getPanel());
            frame.revalidate();
            frame.repaint();
        }
        
        JPanel loadingPanel = new JPanel (null);
        loadingPanel.setOpaque (false);
        loadingPanel.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        ImageIcon icon = Worker.getDecoratorImage ("/quiz_generator/Design/14.png", 349, 593);
        
        JLabel label = new JLabel (icon);
        label.setBounds (Worker.getBounds (791, 243, 349, 593));
        
        JLabel gifLabel = new JLabel (Worker.getGif ("/quiz_generator/Design/15.gif", 262, 262));
        gifLabel.setBounds (Worker.getBounds (834, 393, 262, 262));
        
        loadingPanel.add (gifLabel);
        loadingPanel.add (label);
        
        frontend.getPanel().add (loadingPanel);
        frontend.getPanel().revalidate();
        frontend.getPanel().repaint();
        
        javax.swing.Timer timer = new javax.swing.Timer (5500, e ->
        {
            frontend.getPanel().remove (loadingPanel);
            frontend.getPanel().revalidate();
            frontend.getPanel().repaint();
            
        });
        
        timer.setRepeats (false);
        timer.start();
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    void mainLoadingScreen (JFrame frame)
    {
        if (frontend.getPanel() != null)
        {
            changeComponents (frame);
            
            frontend.setPanel (null);
            frontend.setNextPanel (FrontendSharedData.Page.MAIN_LOADING_SCREEN);
            frontend.setPanel (background());
            
            frame.add (frontend.getPanel());
            frame.revalidate();
            frame.repaint();
        }
        
        JPanel loadingPanel = new JPanel (null);
        loadingPanel.setOpaque (false);
        loadingPanel.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/45.png", "/quiz_generator/Design/46.png", "/quiz_generator/Design/47.png", "/quiz_generator/Design/48.png"};
        int[] x = {290, 90};
        
        ImageIcon[] icons;
        
        JLabel[] decorators = new JLabel[decoratorImageName.length];
        
        for (int i = 0; i < decoratorImageName.length; i++)
        {
            icons = Worker.getDecoratorImages (decoratorImageName, x[0], x[1]);
            decorators[i] = new JLabel (icons[i]);
            
            switch(i)
            {
                case 2 ->
                {
                    decorators[i].setBounds (Worker.getBounds (366, 88, x[0], x[1]));
                    
                    x[0] = 341;
                    x[1] = 392;
                }
                case 3 -> decorators[i].setBounds (Worker.getBounds (1212, -55, x[0], x[1]));
            }
        }
        
        JLabel gifLabel = new JLabel (Worker.getGif ("/quiz_generator/Design/49.gif", 402, 351));
        gifLabel.setBounds (Worker.getBounds (733, 142, 402, 351));
        
        JProgressBar loadingBar = ImageProgressBar.getProgressBar (decoratorImageName, 1186, 144);
        loadingBar.setFont (Worker.getFont ("/quiz_generator/Font/LilitaOne-Regular.ttf", 0, 51.9f));
        loadingBar.setStringPainted (true);
        loadingBar.setBounds (Worker.getBounds (366, 570, 1186, 144));
        
        String[] loadingPhrases = {"<html><div style='text-align: center;'>Getting ready</div></html>", 
            "<html><div style='text-align: center;'>Preparing pen and paper</div></html>", 
            "<html><div style='text-align: center;'>Thinking deeply</div></html>", 
            "<html><div style='text-align: center;'>Extracting main topics</div></html>", 
           "<html><div style='text-align: center;'>Deep relaxation</div></html>", 
           "<html><div style='text-align: center;'>Choose wisely</div></html>",
        "<html><div style='text-align: center;'>Focus locked in</div></html>",
        "<html><div style='text-align: center;'>Having fun</div></html>"};
        
        JLabel phraseLabel = new JLabel (loadingPhrases[0], SwingConstants.CENTER);
        phraseLabel.setFont (Worker.getFont ("/quiz_generator/Font/Inter_18pt-Regular.ttf", 1, 35.2f));
        phraseLabel.setForeground (Color.WHITE);
        phraseLabel.setBounds (Worker.getBounds (820, 820, 253, 90));
        
        javax.swing.Timer timer = new javax.swing.Timer (2000, e ->
        {
            int currentIndex = Arrays.asList (loadingPhrases).indexOf (phraseLabel.getText()), nextIndex = (currentIndex + 1) % loadingPhrases.length;
            
            phraseLabel.setText (loadingPhrases[nextIndex]);
            
        });
        
        timer.start();
        
        new Thread (() ->
        {
            for (int i = 0; i < 101; i++)
            {
                final int run = i;
                
                SwingUtilities.invokeLater (() ->
                {
                    loadingBar.setValue (run);
                    loadingBar.setString (run + "%");
                    
                });
                
                try
                {
                    Thread.sleep (200);
                }
                catch (InterruptedException e)
                {
                    // Ignore
                }
            }
            
            SwingUtilities.invokeLater (() ->
            {
                timer.stop();
                
                frontend.getPanel().remove (loadingPanel);
                frontend.getPanel().revalidate();
                frontend.getPanel().repaint();
                
            });
            
        }).start();
        
        loadingPanel.add (loadingBar);
        
        for (JLabel decorator : decorators)
        {
            loadingPanel.add (decorator);
        }
        
        loadingPanel.add (gifLabel);
        loadingPanel.add (phraseLabel);
        
        frontend.getPanel().add (loadingPanel);
        frontend.getPanel().revalidate();
        frontend.getPanel().repaint();
    }
    
    void readySetUpScreen (JFrame frame)
    {
        if (frontend.getPanel() != null)
        {
            changeComponents (frame);
            
            frontend.setPanel (null);
            frontend.setNextPanel ((backend.getIndex()[1] == 1) ? FrontendSharedData.Page.SET_PAGE_1 : (backend.getIndex()[1] == 2) ? FrontendSharedData.Page.SET_PAGE_2 : FrontendSharedData.Page.SET_PAGE_3);
            frontend.setPanel (background());
            
            frame.add (frontend.getPanel());
            frame.revalidate();
            frame.repaint();
        }
        
        JPanel loadingPanel = new JPanel (null);
        loadingPanel.setOpaque (false);
        loadingPanel.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        JLabel gifLabel =  new JLabel (Worker.getGif ("/quiz_generator/Design/77.gif", 1640, 864));
        gifLabel.setBounds (Worker.getBounds (139, 108, 1640, 864));
        
        loadingPanel.add (gifLabel);
        
        frontend.getPanel().add (loadingPanel);
        frontend.getPanel().revalidate();
        frontend.getPanel().repaint();
        
        javax.swing.Timer timer = new javax.swing.Timer (8000, e ->
        {
            frontend.getPanel().remove (loadingPanel);
            frontend.getPanel().revalidate();
            frontend.getPanel().repaint();
            
        });
        
        timer.setRepeats (false);
        timer.start();
    }
    
    void resultCheckerScreen (JFrame frame)
    {
        int delay = ((!frontend.getState().contains (FrontendSharedData.Condition.CORRECT) || frontend.getState().contains (FrontendSharedData.Condition.CORRECT)) && !frontend.getState().contains (FrontendSharedData.Condition.TIMEOUT)) ? 500 : 0;
        
        javax.swing.Timer delayTimer = new javax.swing.Timer (delay, e ->
        {
            if (frontend.getPanel() != null)
            {
                changeComponents (frame);
            
                frontend.setPanel (null);
                frontend.setNextPanel ((backend.getIndex()[1] == 1) ? FrontendSharedData.Page.SET_PAGE_1 : (backend.getIndex()[1] == 2) ? FrontendSharedData.Page.SET_PAGE_2 : FrontendSharedData.Page.SET_PAGE_3);
                frontend.setPanel (background());
            
                frame.add (frontend.getPanel());
                frame.revalidate();
                frame.repaint();
            }
            
            JPanel gifPanel = new JPanel (null);
            gifPanel.setOpaque (false);
            gifPanel.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
            String[] paths = {"/quiz_generator/Design/79.gif", "/quiz_generator/Design/80.gif", "/quiz_generator/Design/81.gif"};
        
            final JLabel gifLabel;
        
            if (!frontend.getState().contains (FrontendSharedData.Condition.TIMEOUT))
            {
                if (!frontend.getState().contains (FrontendSharedData.Condition.CORRECT))
                {
                    gifLabel = new JLabel (Worker.getGif (paths[1], 1669, 864));
                    gifLabel.setBounds (Worker.getBounds (125, 108, 1669, 864));
                }
                else
                {
                    gifLabel = new JLabel (Worker.getGif (paths[0], 1648, 864));
                    gifLabel.setBounds (Worker.getBounds (175, 108, 1648, 864));
                    
                    backend.setScore (backend.getScore() + 1);
                    
                    frontend.getState().remove (FrontendSharedData.Condition.CORRECT);
                }
            }
            else
            {
                gifLabel = new JLabel (Worker.getGif (paths[2], 1658, 864));
                gifLabel.setBounds (Worker.getBounds (130, 108, 1658, 864));
            
                frontend.getState().remove (FrontendSharedData.Condition.TIMEOUT);
            }
        
            gifPanel.add (gifLabel);
        
            frontend.getPanel().add (gifPanel);
            frontend.getPanel().revalidate();
            frontend.getPanel().repaint();
        
            javax.swing.Timer timer = new javax.swing.Timer (5000, e_2 ->
            {
                frontend.getPanel().remove (gifPanel);
                frontend.getPanel().revalidate();
                frontend.getPanel().repaint();
            
            });
        
            timer.setRepeats (false);
            timer.start();
            
        });
        
        delayTimer.setRepeats (false);
        delayTimer.start();
    }
    
    void finalLoadingScreen (JFrame frame)
    {
        if (frontend.getPanel() != null)
        {
            changeComponents (frame);
            
            frontend.setPanel (null);
            frontend.setNextPanel (FrontendSharedData.Page.FINAL_LOADING_SCREEN);
            frontend.setPanel (background());
            
            frame.add (frontend.getPanel());
            frame.revalidate();
            frame.repaint();
        }
        
        JPanel loadingPanel = new JPanel (null);
        loadingPanel.setOpaque (false);
        loadingPanel.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/121.png", "/quiz_generator/Design/48.png"};
        int[] x = {451, 197};
        
        ImageIcon[] icons;
        
        JLabel[] decorators = new JLabel[decoratorImageName.length];
        
        for (int i = 0; i < decoratorImageName.length; i++)
        {
            icons = Worker.getDecoratorImages (decoratorImageName, x[0], x[1]);
            decorators[i] = new JLabel (icons[i]);
            
            switch(i)
            {
                case 0 ->
                {
                    decorators[i].setBounds (Worker.getBounds (268, 24, x[0], x[1]));
                    
                    x[0] = 341;
                    x[1] = 392;
                }
                case 1 -> decorators[i].setBounds (Worker.getBounds (1212, -55, x[0], x[1]));
            }
        }
                
        JLabel gifLabel = new JLabel (Worker.getGif ("/quiz_generator/Design/119.gif", 1413, 795));
        gifLabel.setBounds (Worker.getBounds (253, 142, 1413, 795));
        
        for (JLabel decorator : decorators)
        {
            loadingPanel.add (decorator);
        }
        
        loadingPanel.add (gifLabel);
        
        frontend.getPanel().add (loadingPanel);
        frontend.getPanel().revalidate();
        frontend.getPanel().repaint();
        
        javax.swing.Timer timer = new javax.swing.Timer (15500, e ->
        {
            frontend.getPanel().remove (loadingPanel);
            frontend.getPanel().revalidate();
            frontend.getPanel().repaint();
            
        });
        
        timer.setRepeats (false);
        timer.start();
    }
    
    void afterEffects (List<JComponent> components, JLayeredPane layeredPane)
    {
        JLabel gifLabel = new JLabel (Worker.getGif ("/quiz_generator/Design/128.gif", 1920, 1080));
        gifLabel.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        components.add (gifLabel);
        
        javax.swing.Timer timer = new javax.swing.Timer (5000, e ->
        {
            layeredPane.remove (gifLabel);
            layeredPane.revalidate();
            layeredPane.repaint();
            
        });
        
        timer.setRepeats (false);
        timer.start();
    }
}