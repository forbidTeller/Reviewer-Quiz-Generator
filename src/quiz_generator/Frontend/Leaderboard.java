package quiz_generator.Frontend;

import quiz_generator.Backend.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

class Leaderboard extends MainComponent
{
    private final UITransition transition;
    private final QuizConfigurator configurator;
    
     // CONSTRUCTOR
    
    public Leaderboard (BackendSharedData backend, FrontendSharedData frontend, UITransition transition, QuizConfigurator configurator)
    {
        super (backend, frontend);
        
        this.transition = transition;
        this.configurator = configurator;
    }
    
    JLayeredPane showLeaderboard (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/129.png", "/quiz_generator/Design/130.png", "/quiz_generator/Design/132.png", "/quiz_generator/Design/134.png"};
        int[] x = {1729, 860, 920, 266};
        
        ImageIcon[] icons;
        
        JLabel[] decorators = new JLabel[decoratorImageName.length];
        JLabel[] hitboxes = new JLabel[decoratorImageName.length];
        
        for (int i = 0; i < decoratorImageName.length; i++)
        {
            icons = Worker.getDecoratorImages (decoratorImageName, x[0], x[1]);
            decorators[i] = new JLabel (icons[i]);
            
            hitboxes[i] = new JLabel();
            hitboxes[i].setOpaque (false);
            
            switch(i)
            {
                case 0 ->
                {
                    decorators[i].setBounds (Worker.getBounds (107, 43, x[0], x[1]));
                    
                    x[0] = 337;
                    x[1] = 121;
                }
                case 1 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (251, x[2], x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (251, x[2], x[0], x[1]));
                }
                case 2 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (789, x[2], x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (789, x[2], x[0], x[1]));
                }
                case 3 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (1326, x[2], x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1326, x[2], x[0], x[1]));          
                }
            }
        }
        
        List<String> names = Database.loadForLeaderboard ("NAME");
        List<String> scores = Database.loadForLeaderboard ("SCORE");
        
        List<String> validNames = new ArrayList<>(), validScores = new ArrayList<>();
        
        for (int i = 0; i < scores.size(); i++)
        {
            int currentScore = Integer.parseInt (scores.get(i));
            
            if (currentScore > 0)
            {
                validNames.add (names.get(i));
                validScores.add (scores.get(i));
            }
        }
        
        String[] nameArray = validNames.toArray (String[] :: new);
        String[] scoreArray = validScores.toArray (String[] :: new);
        
        for (int i = 0; i < scoreArray.length - 1; i++)
        {
            for (int j = 0; j < scoreArray.length - i - 1; j++)
            {
                int prevScore = Integer.parseInt (scoreArray[j]), currentScore = Integer.parseInt (scoreArray[j + 1]);
                
                if (prevScore < currentScore)
                {
                    String tempScore = scoreArray[j];
                    scoreArray[j] = scoreArray[j + 1];
                    scoreArray[j + 1] = tempScore;
                    
                    String tempName = nameArray[j];
                    nameArray[j] = nameArray[j + 1];
                    nameArray[j + 1] = tempName;
                }
            }
        }
        
        int limit = Math.min (nameArray.length, 10), startY = x[3];
                
        JLabel[] text = new JLabel[limit];
        
        for (int i = 0; i < limit; i++)
        {
            if (i == 5) x[3] = startY;
            
            text[i] = new JLabel ("<html>" + nameArray[i] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Score: " + scoreArray[i] + "</html>", SwingConstants.CENTER);
            text[i].setFont (Worker.getFont ("/quiz_generator/Font/Chewy-Regular.ttf", 1, 28.6f));
            text[i].setForeground ((nameArray[i].equals (frontend.getName())) ? new Color (50, 205, 50) : new Color (254, 237, 232));
            text[i].setBounds ((i >= 5) ? Worker.getBounds (1255, x[3], 392, 32) : Worker.getBounds (488, x[3], 392, 32));
            
            x[3] += 117;
        }
        
        for (int i = 0; i < decoratorImageName.length; i++)
        {
            final int j = i;
            
            switch(i)
            {
                case 1 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/131.png", 337, 121);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/130.png", 337, 121);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                    });
                }
                case 2 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/133.png", 337, 121);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/132.png", 337, 121);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            frontend.state.add (FrontendSharedData.Condition.RETRY_IS_CLICKED);
                            
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
                                    configurator.notificationPanel (frame, layout, highlightedTerms);
                                }
                                
                            }.execute();
                        }
                        
                    });
                }
                case 3 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/135.png", 337, 121);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/134.png", 337, 121);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            frontend.state.add (FrontendSharedData.Condition.EXIT_IS_CLICKED);
                            
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
                                    configurator.notificationPanel (frame, layout, highlightedTerms);
                                }
                                
                            }.execute();
                        }
                        
                    });
                }
            }
        }
        
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.addAll (Arrays.asList (text));
        
        transition.afterEffects (components, layeredPane);
        
        components.addAll (Arrays.asList (hitboxes));
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        return layeredPane;
    }
    
    JLayeredPane userListSearching (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        return layeredPane;
    }
    
    JLayeredPane individualScoreView (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        return layeredPane;
    }
}