package quiz_generator.Frontend;

import quiz_generator.Backend.*;

import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import java.time.*;
import java.time.format.*;

class Leaderboard extends MainComponent
{
    private final UITransition transition;
    private final QuizConfigurator configurator;
    
     // CONSTRUCTOR
    
    Leaderboard (Backend backend, Frontend frontend, UITransition transition, QuizConfigurator configurator)
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
        
        int safeLimit = Math.min (names.size(), scores.size());
        
        for (int i = 0; i < safeLimit; i++)
        {
            try
            {
                int currentScore = Integer.parseInt (scores.get(i));
            
                if (currentScore > 0)
                {
                    validNames.add (names.get(i));
                    validScores.add (scores.get(i));
                }
            }
            catch (NumberFormatException e)
            {
                // Ignore
            }
        }
        
        String[] nameArray = validNames.toArray (String[] :: new), scoreArray = validScores.toArray (String[] :: new);
        
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
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            Database.setInUserScoreHistory (true);
                            
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
                                    changeComponents (frame);
                                    
                                    frontend.setPanel (null);
                                    frontend.setNextPanel (FrontendSharedData.Page.SCORE_HISTORY_PAGE_1);
                                    frontend.setPanel (background());
                                    frontend.getPanel().setLayout (layout);
                                    frontend.getPanel().add (userListSearching (frame, layout, highlightedTerms), "userListSearching");
                                    
                                    layout.show (frontend.getPanel(), "userListSearching");
                                    
                                    frame.add (frontend.getPanel());
                                    frame.revalidate();
                                    frame.repaint();
                                }
                                
                            }.execute();
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
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/133.png", 337, 121);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/132.png", 337, 121);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/133.png", 337, 121);
                            decorators[j].setIcon (decoratorImage);
                            
                            frontend.getState().add (FrontendSharedData.Condition.RETRY_IS_CLICKED);
                            
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
                            
                            javax.swing.Timer watcher = new javax.swing.Timer (100, e_2 ->
                            {
                                if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) == 0)
                                {
                                    decorators[j].setIcon (Worker.getDecoratorImage ("/quiz_generator/Design/132.png", 337, 121));
                                    
                                    ((javax.swing.Timer) e_2.getSource()).stop();
                                }
                                
                            });
                            
                            watcher.start();
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
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/135.png", 337, 121);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/134.png", 337, 121);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/135.png", 337, 121);
                            decorators[j].setIcon (decoratorImage);
                            
                            frontend.getState().add (FrontendSharedData.Condition.EXIT_IS_CLICKED);
                            
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
                            
                            javax.swing.Timer watcher = new javax.swing.Timer (100, e_2 ->
                            {
                                if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) == 0)
                                {
                                    decorators[j].setIcon (Worker.getDecoratorImage ("/quiz_generator/Design/134.png", 337, 121));
                                    
                                    ((javax.swing.Timer) e_2.getSource()).stop();
                                }
                                
                            });
                            
                            watcher.start();
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
        
        String[] decoratorImageName = {"/quiz_generator/Design/138.png", "/quiz_generator/Design/148.png"};
        int[] x = {1337, 1047};
        
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
                    decorators[i].setBounds (Worker.getBounds (291, 33, x[0], x[1]));
                    
                    x[0] = 209;
                    x[1] = 88;
                }
                case 1 -> decorators[i].setBounds (Worker.getBounds (44, 54, x[0], x[1]));
            }
        }
        
        JLabel hitbox = new JLabel();
        hitbox.setCursor (new Cursor (Cursor.HAND_CURSOR));
        hitbox.setOpaque (false);
        hitbox.setBounds (Worker.getBounds (44, 54, 209, 88));
        
        JTextField searchField = new JTextField();
        searchField.setFont (Worker.getFont ("/quiz_generator/Font/Quicksand-Regular.ttf", 0, 29.4f));
        searchField.setText ("Search names...");
        searchField.setForeground (Color.DARK_GRAY);
        searchField.setBorder (null);
        searchField.setOpaque (false);
        searchField.setBounds (Worker.getBounds (521, 304, 935, 46));
        
        AbstractDocument ad = (AbstractDocument) searchField.getDocument();
        ad.setDocumentFilter (new LimitFilter (15, false));
        
        JScrollPane[] scrollPane = new JScrollPane[1];
        
        List<String> names = Database.loadForScoreHistory ("USER", null, null);
        
        if (names.isEmpty())
        {
            searchField.setEditable (false);
            searchField.setFocusable (false);
            
            JPanel panel = new JPanel (new GridBagLayout());
            panel.setOpaque (false);
            
            JLabel label = new JLabel ("No one's here..", SwingConstants.CENTER);
            label.setFont (Worker.getFont ("/quiz_generator/Font/Dekko-Regular.ttf", 1, 42.6f));
            label.setForeground (Color.DARK_GRAY);
            
            panel.add (label);
            
            scrollPane[0] = new JScrollPane (panel);
            scrollPane[0].setBorder (BorderFactory.createEmptyBorder());
            scrollPane[0].getViewport().setOpaque (false);
            scrollPane[0].setOpaque (false);
            scrollPane[0].setBounds (Worker.getBounds (386, 402, 1130, 518));
        }
        else
        {
            scrollPane[0] = Displayer.displayUser (frame, layout, highlightedTerms, names, this);
        }
        
        layeredPane.add (scrollPane[0], Integer.valueOf(2));
        
        hitbox.addMouseListener (new MouseAdapter()
        {
            @Override
            public void mouseEntered (MouseEvent e)
            {
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/149.png", 209, 88);
                decorators[1].setIcon (decoratorImage);
            }
            
            @Override
            public void mouseExited (MouseEvent e)
            {
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/148.png", 209, 88);
                decorators[1].setIcon (decoratorImage);
            }
            
            @Override
            public void mousePressed (MouseEvent e)
            {
                Database.setInUserScoreHistory (false);
                
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
                        changeComponents (frame);
                        
                        frontend.setPanel (null);
                        frontend.setNextPanel (FrontendSharedData.Page.IN_LEADERBOARD);
                        frontend.setPanel (background());
                        frontend.getPanel().setLayout (layout);
                        frontend.getPanel().add (showLeaderboard (frame, layout, highlightedTerms), "showLeaderboard");
                        
                        layout.show (frontend.getPanel(), "showLeaderboard");
                        
                        frame.add (frontend.getPanel());
                        frame.revalidate();
                        frame.repaint();
                    }
                    
                }.execute();
            }
            
        });
        
        if (!names.isEmpty())
        {
            searchField.addMouseListener (new MouseAdapter()
            {
                @Override
                public void mousePressed (MouseEvent e)
                {
                    String check = searchField.getText().trim();
                
                    if (check.equals ("Search names...") && searchField.getForeground().equals (Color.DARK_GRAY)) searchField.setText("");
               
                    searchField.setForeground (Color.BLACK);
                }
            
            });
        }
        
        MouseAdapter focusLoss = new MouseAdapter()
        {
            @Override
            public void mousePressed (MouseEvent e)
            {
                String check = searchField.getText().trim();
                
                if (check.isEmpty())
                {
                    searchField.setForeground (Color.DARK_GRAY);
                    searchField.setText ("Search names...");
                }
                
                frontend.getPanel().requestFocusInWindow();
            }
            
        };
        
        ad.addDocumentListener (new DocumentListener()
        {
            @Override
            public void insertUpdate (DocumentEvent e)
            {
                filterList();
            }
            
            @Override
            public void removeUpdate (DocumentEvent e)
            {
                filterList();
            }
            
            @Override
            public void changedUpdate (DocumentEvent e)
            {
                filterList();
            }
            
            private void filterList()
            {
                String query = searchField.getText().toUpperCase().trim();
                
                List<String> filter = new ArrayList<>();
                
                if ((query.equalsIgnoreCase ("Search names...") && searchField.getForeground().equals (Color.DARK_GRAY)) || query.isEmpty())
                {
                    filter.addAll (names);
                }
                else
                {
                    List<String> priorityList = new ArrayList<>(), lastList = new ArrayList<>();
                    
                    for (String name : names)
                    {
                        if (name.startsWith (query))
                        {
                            priorityList.add (name);
                        }
                        else if (name.contains (query))
                        {
                            lastList.add (name);
                        }
                    }
                    
                    filter.addAll (priorityList);
                    filter.addAll (lastList);
                }
                
                layeredPane.remove (scrollPane[0]);
                
                if (filter.isEmpty())
                {
                    JPanel panel = new JPanel (new GridBagLayout());
                    panel.setOpaque (false);
                    
                    panel.addMouseListener (focusLoss);
                   
                    JLabel label = new JLabel ("Nothing matches '" + query + "'", SwingConstants.CENTER);
                    label.setFont (Worker.getFont ("/quiz_generator/Font/Dekko-Regular.ttf", 1, 42.6f));
                    label.setForeground (Color.DARK_GRAY);
                    
                    panel.add (label);
                    
                    scrollPane[0] = new JScrollPane (panel);
                    scrollPane[0].setBorder (BorderFactory.createEmptyBorder());
                    scrollPane[0].getViewport().setOpaque (false);
                    scrollPane[0].setOpaque (false);
                    scrollPane[0].setBounds (Worker.getBounds (386, 402, 1130, 518));
                }
                else
                {
                    scrollPane[0] = Displayer.displayUser (frame, layout, highlightedTerms, filter, Leaderboard.this);
                }
                
                layeredPane.add (scrollPane[0], Integer.valueOf(2));
                layeredPane.revalidate();
                layeredPane.repaint();
            }
            
        });
        
        frontend.getPanel().addMouseListener (focusLoss);
        
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.add (searchField);
        components.add (hitbox);
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
          
        return layeredPane;
    }
    
    JLayeredPane individualScoreView (JFrame frame, CardLayout layout, List<String> highlightedTerms, String name)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/160.png", "/quiz_generator/Design/163.png", "/quiz_generator/Design/171.png", "/quiz_generator/Design/173.png", "/quiz_generator/Design/185.png"};
        int[] x = {305, 30, 1597, 234, 41};
        
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
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (1561, 463, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1561, 463, x[0], x[1]));
                    
                    x[0] = 234;
                    x[1] = 283;
                }
                case 1 ->
                {
                    decorators[i].setBounds (Worker.getBounds (1597, 509, x[0], x[1]));
                    
                    x[0] = 244;
                    x[1] = 99;
                }
                case 2 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (1647, 58, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1647, 58, x[0], x[1]));
                    
                    x[0] = 525;
                    x[1] = 104;
                }
                case 3 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (64, 264, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (64, 264, x[0], x[1]));
                    
                    x[0] = 1028;
                    x[1] = 68;
                }
                case 4 -> decorators[i].setBounds (Worker.getBounds (387, 530, x[0], x[1]));
            }
        }
        
        decorators[1].setVisible (false);
           
        JLabel username = new JLabel (name);
        username.setFont (Worker.getFont ("/quiz_generator/Font/Arimo-Regular.ttf", 0, 71.0f));
        username.setForeground (Color.BLACK);
        username.setBounds (Worker.getBounds (259, 58, 510, 113));
        
        JLabel dropLabel = new JLabel ("ALL");
        dropLabel.setFont (Worker.getFont ("/quiz_generator/Font/Arimo-Regular.ttf", 1, 19.4f));
        dropLabel.setForeground (Color.BLACK);
        dropLabel.setBounds (Worker.getBounds (1688, 463, 50, 30));
        
        JLabel[] dropHitboxes = new JLabel[7];
        
        for (int i = 0; i < 7; i++)
        {
            dropHitboxes[i] = new JLabel();
            dropHitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
            dropHitboxes[i].setOpaque (false);
            
            switch(i)
            {
                case 0 -> dropHitboxes[i].setBounds (Worker.getBounds (x[2], 509, x[3], x[4]));
                case 1 -> dropHitboxes[i].setBounds (Worker.getBounds (x[2], 550, x[3], x[4]));
                case 2 -> dropHitboxes[i].setBounds (Worker.getBounds (x[2], 590, x[3], x[4]));
                case 3 -> dropHitboxes[i].setBounds (Worker.getBounds (x[2], 630, x[3], x[4]));
                case 4 -> dropHitboxes[i].setBounds (Worker.getBounds (x[2], 670, x[3], x[4]));
                case 5 -> dropHitboxes[i].setBounds (Worker.getBounds (x[2], 711, x[3], x[4]));
                case 6 -> dropHitboxes[i].setBounds (Worker.getBounds (x[2], 751, x[3], x[4]));
            }
            
            dropHitboxes[i].setVisible (false);
        }
        
        List<String> records = Database.loadForScoreHistory ("HISTORY", name, null);
        
        JScrollPane[] scrollPane = new JScrollPane[1];
        scrollPane[0] = Displayer.displayScoreHistory (records);
        
        layeredPane.add (scrollPane[0], Integer.valueOf(2));
        
        boolean[] toggle = {false};
        
        for (int i = 0; i < decoratorImageName.length; i++)
        {
            final int j = i;
            
            switch(i)
            {
                case 0 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/161.png", 305, 30);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon toggleOff = Worker.getDecoratorImage ("/quiz_generator/Design/160.png", 305, 30);
                            ImageIcon toggleOn = Worker.getDecoratorImage ("/quiz_generator/Design/162.png", 305, 30);
                            
                            decorators[j].setIcon ((toggle[0]) ? toggleOn : toggleOff);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            toggle[0] = !toggle[0];
                            
                            if (toggle[0])
                            {
                                ImageIcon toggleOn = Worker.getDecoratorImage ("/quiz_generator/Design/162.png", 305, 30);
                                decorators[j].setIcon (toggleOn);
                            
                                decorators[1].setVisible (true);
                                
                                for (JLabel dropHitbox : dropHitboxes)
                                {
                                    dropHitbox.setVisible (true);
                                }
                                
                                dropLabel.setForeground (Color.GRAY);
                            }
                            else
                            {
                                ImageIcon toggleOff = Worker.getDecoratorImage ("/quiz_generator/Design/160.png", 305, 30);
                                decorators[j].setIcon (toggleOff);
                            
                                decorators[1].setVisible (false);
                                
                                for (JLabel dropHitbox : dropHitboxes)
                                {
                                    dropHitbox.setVisible (false);
                                }
                                
                                dropLabel.setForeground (Color.BLACK);
                            }
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/172.png", 244, 99);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/171.png", 244, 99);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
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
                                    changeComponents (frame);
                                    
                                    frontend.setPanel (null);
                                    frontend.setNextPanel (FrontendSharedData.Page.SCORE_HISTORY_PAGE_1);
                                    frontend.setPanel (background());
                                    frontend.getPanel().setLayout (layout);
                                    frontend.getPanel().add (userListSearching (frame, layout, highlightedTerms), "userListSearching");
                                    
                                    layout.show (frontend.getPanel(), "userListSearching");
                                    
                                    frame.add (frontend.getPanel());
                                    frame.revalidate();
                                    frame.repaint();
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
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/174.png", 525, 104);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/173.png", 525, 104);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/174.png", 525, 104);
                            decorators[j].setIcon (decoratorImage);
                            
                            frontend.getState().add (FrontendSharedData.Condition.DELETE_IS_CLICKED);
                            
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
                                    configurator.notificationPanel (frame, layout, highlightedTerms, name);
                                }
                                
                            }.execute();
                            
                            javax.swing.Timer watcher = new javax.swing.Timer (100, e_2 ->
                            {
                                if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) == 0)
                                {
                                    decorators[j].setIcon (Worker.getDecoratorImage ("/quiz_generator/Design/173.png", 525, 104));
                                    
                                    ((javax.swing.Timer) e_2.getSource()).stop();
                                }
                                
                            });
                            
                            watcher.start();
                        }
                        
                    });
                }
            }
        }
        
        for (int i = 0; i < 7; i++)
        {                   
            dropHitboxes[i].setVisible (true);
                                
            switch(i)
            {
                case 0 ->
                {
                    dropHitboxes[i].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/164.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                                            
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/163.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                                            
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            dropLabel.setText ("ALL");
                            dropLabel.setFont (Worker.getFont ("/quiz_generator/Font/Arimo-Regular.ttf", 1, 19.4f));
                            dropLabel.setForeground (Color.BLACK);
                            dropLabel.setBounds (Worker.getBounds (1688, 463, 50, 30));
                            
                            decorators[1].setVisible (false);
                            decorators[4].setVisible (true);
                            
                            for (JLabel dropHitbox : dropHitboxes)
                            {
                                dropHitbox.setVisible (false);
                            }
                            
                            layeredPane.remove (scrollPane[0]);
                            
                            List<String> records = Database.loadForScoreHistory ("HISTORY", name, null);
                            
                            scrollPane[0] = Displayer.displayScoreHistory (records);
                            
                            layeredPane.add (scrollPane[0], Integer.valueOf(2));
                            layeredPane.revalidate();
                            layeredPane.repaint();
                        }
                                            
                    });
                }
                case 1 ->
                {
                    dropHitboxes[i].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/165.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                                            
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/163.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                                            
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            dropLabel.setText ("DATE");
                            dropLabel.setFont (Worker.getFont ("/quiz_generator/Font/Arimo-Regular.ttf", 1, 19.4f));
                            dropLabel.setForeground (Color.BLACK);
                            dropLabel.setBounds (Worker.getBounds (1679, 463, 68, 30));
                            
                            decorators[1].setVisible (false);
                            
                            for (JLabel dropHitbox : dropHitboxes)
                            {
                                dropHitbox.setVisible (false);
                            }
                            
                            String selector = ImageDateSelector.getDateSelector (frame);
                            
                            if (selector != null)
                            {
                                DateTimeFormatter uiFormat = DateTimeFormatter.ofPattern ("MM-dd-yyyy");
                                DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern ("yyyy-MM-dd");
                                
                                String target = LocalDate.parse (selector, uiFormat).format (dataFormat);
                                
                                List<String> records = Database.loadForScoreHistory ("HISTORY", name, null), filter = new ArrayList<>();
                                
                                for (int i = 0; i < records.size(); i += 3)
                                {
                                    String date = records.get(i);
                                    
                                    if (date.startsWith (target))
                                    {
                                        filter.add (records.get(i));
                                        filter.add (records.get (i + 1));
                                        filter.add (records.get (i + 2));
                                    }
                                }
                                
                                layeredPane.remove (scrollPane[0]);
                                
                                if (filter.isEmpty())
                                {
                                    decorators[4].setVisible (false);
                                    
                                    scrollPane[0] = Displayer.displayNoResultFound();
                                }
                                else
                                {
                                    decorators[4].setVisible (true);
                                    
                                    scrollPane[0] = Displayer.displayScoreHistory (filter);
                                }
                                
                                layeredPane.add (scrollPane[0], Integer.valueOf(2));
                                layeredPane.revalidate();
                                layeredPane.repaint();
                            }
                            else
                            {
                                dropLabel.setText ("ALL");
                                dropLabel.setFont (Worker.getFont ("/quiz_generator/Font/Arimo-Regular.ttf", 1, 19.4f));
                                dropLabel.setForeground (Color.BLACK);
                                dropLabel.setBounds (Worker.getBounds (1688, 463, 50, 30));
                            
                                decorators[1].setVisible (false);
                                decorators[4].setVisible (true);
                            
                                for (JLabel dropHitbox : dropHitboxes)
                                {
                                    dropHitbox.setVisible (false);
                                }
                            
                                layeredPane.remove (scrollPane[0]);
                            
                                List<String> records = Database.loadForScoreHistory ("HISTORY", name, null);
                            
                                scrollPane[0] = Displayer.displayScoreHistory (records);
                            
                                layeredPane.add (scrollPane[0], Integer.valueOf(2));
                                layeredPane.revalidate();
                                layeredPane.repaint();
                            }
                        }
                                            
                    });
                }
                case 2 ->
                {
                    dropHitboxes[i].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/166.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/163.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            dropLabel.setText ("HIGHEST-LOWEST SCORE");
                            dropLabel.setFont (Worker.getFont ("/quiz_generator/Font/Arimo-Regular.ttf", 1, 12.4f));
                            dropLabel.setForeground (Color.BLACK);
                            dropLabel.setBounds (Worker.getBounds (1617, 469, 217, 19));
                            
                            decorators[1].setVisible (false);
                            decorators[4].setVisible (true);
                            
                            for (JLabel dropHitbox : dropHitboxes)
                            {
                                dropHitbox.setVisible (false);
                            }
                            
                            layeredPane.remove (scrollPane[0]);
                            
                            List<String> records = Database.loadForScoreHistory ("HISTORY", name, null);
                            
                            List<String[]> grouped = new ArrayList<>();
                            
                            for (int i = 0; i < records.size(); i += 3)
                            {
                                grouped.add (new String[] { records.get(i), records.get (i + 1), records.get (i + 2) });
                            }
                            
                            grouped.sort ((a, b) ->
                            {
                                try
                                {
                                    int scoreA = Integer.parseInt (a[1]), scoreB = Integer.parseInt (b[1]);
                                    
                                    return Integer.compare (scoreB, scoreA);
                                }
                                catch (NumberFormatException e_2)
                                {
                                    return 0;
                                }
                                
                            });
                            
                            List<String> sorted = new ArrayList<>();
                            
                            for (String[] group : grouped)
                            {
                                sorted.add (group[0]);
                                sorted.add (group[1]);
                                sorted.add (group[2]);
                            }
                            
                            boolean execute = true;
                            
                            if (records.size() > 3)
                            {
                                String firstScore = records.get(1);
                                
                                for (int i = 4; i < records.size(); i += 3)
                                {
                                    if (!records.get(i).equals (firstScore))
                                    {
                                        execute = false;
                                        
                                        break;
                                    }
                                }
                            }
                            
                            if (execute)
                            {
                                decorators[4].setVisible (false);
                                
                                scrollPane[0] = Displayer.displayNoResultFound();
                            }
                            else
                            {
                                decorators[4].setVisible (true);
                                
                                scrollPane[0] = Displayer.displayScoreHistory (sorted);
                            }
                            
                            layeredPane.add (scrollPane[0], Integer.valueOf(2));
                            layeredPane.revalidate();
                            layeredPane.repaint();
                        }
                        
                    });
                }
                case 3 ->
                {
                    dropHitboxes[i].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/167.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/163.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            dropLabel.setText ("LOWEST-HIGHEST SCORE");
                            dropLabel.setFont (Worker.getFont ("/quiz_generator/Font/Arimo-Regular.ttf", 1, 12.4f));
                            dropLabel.setForeground (Color.BLACK);
                            dropLabel.setBounds (Worker.getBounds (1617, 469, 217, 19));
                            
                            decorators[1].setVisible (false);
                            decorators[4].setVisible (true);
                            
                            for (JLabel dropHitbox : dropHitboxes)
                            {
                                dropHitbox.setVisible (false);
                            }
                            
                            layeredPane.remove (scrollPane[0]);
                            
                            List<String> records = Database.loadForScoreHistory ("HISTORY", name, null);
                            
                            List<String[]> grouped = new ArrayList<>();
                            
                            for (int i = 0; i < records.size(); i += 3)
                            {
                                grouped.add (new String[] { records.get(i), records.get (i + 1), records.get (i + 2) });
                            }
                            
                            grouped.sort ((a, b) ->
                            {
                                try
                                {
                                    int scoreA = Integer.parseInt (a[1]), scoreB = Integer.parseInt (b[1]);
                                    
                                    return Integer.compare (scoreA, scoreB);
                                }
                                catch (NumberFormatException e_2)
                                {
                                    return 0;
                                }
                                
                            });
                            
                            List<String> sorted = new ArrayList<>();
                            
                            for (String[] group : grouped)
                            {
                                sorted.add (group[0]);
                                sorted.add (group[1]);
                                sorted.add (group[2]);
                            }
                            
                            boolean execute = true;
                            
                            if (records.size() > 3)
                            {
                                String firstScore = records.get(1);
                                
                                for (int i = 4; i < records.size(); i++)
                                {
                                    if (!records.get(i).equals (firstScore))
                                    {
                                        execute = false;
                                        
                                        break;
                                    }
                                }
                            }
                            
                            if (execute)
                            {
                                decorators[4].setVisible (false);
                                
                                scrollPane[0] = Displayer.displayNoResultFound();
                            }
                            else
                            {
                                decorators[4].setVisible (true);
                                
                                scrollPane[0] = Displayer.displayScoreHistory (sorted);
                            }
                            
                            layeredPane.add (scrollPane[0], Integer.valueOf(2));
                            layeredPane.revalidate();
                            layeredPane.repaint();
                        }
                        
                    });
                }
                case 4 ->
                {
                    dropHitboxes[i].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/168.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/163.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            dropLabel.setText ("PERFECT SCORE");
                            dropLabel.setFont (Worker.getFont ("/quiz_generator/Font/Arimo-Regular.ttf", 1, 16.4f));
                            dropLabel.setForeground (Color.BLACK);
                            dropLabel.setBounds (Worker.getBounds (1628, 467, 186, 25));
                            
                            decorators[1].setVisible (false);
                            decorators[4].setVisible (true);
                            
                            for (JLabel dropHitbox : dropHitboxes)
                            {
                                dropHitbox.setVisible (false);
                            }
                            
                            layeredPane.remove (scrollPane[0]);
                            
                            List<String> records = Database.loadForScoreHistory ("HISTORY", name, null), filter = new ArrayList<>();
                            
                            for (int i = 0; i < records.size(); i += 3)
                            {
                                try
                                {
                                    int score = Integer.parseInt (records.get (i + 1)), totalScore = Integer.parseInt (records.get (i + 2));
                                    
                                    if (score == totalScore)
                                    {
                                        filter.add (records.get(i));
                                        filter.add (records.get (i + 1));
                                        filter.add (records.get (i + 2));
                                    }
                                }
                                catch (NumberFormatException e_2)
                                {
                                    // Ignore
                                }
                            }
                            
                            if (filter.isEmpty())
                            {
                                decorators[4].setVisible (false);
                                
                                scrollPane[0] = Displayer.displayNoResultFound();
                            }
                            else
                            {
                                decorators[4].setVisible (true);
                                
                                scrollPane[0] = Displayer.displayScoreHistory (filter);
                            }
                            
                            layeredPane.add (scrollPane[0], Integer.valueOf(2));
                            layeredPane.revalidate();
                            layeredPane.repaint();
                        }
                        
                    });
                }
                case 5 ->
                {
                    dropHitboxes[i].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/169.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/163.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            dropLabel.setText ("PASSED");
                            dropLabel.setFont (Worker.getFont ("/quiz_generator/Font/Arimo-Regular.ttf", 1, 19.4f));
                            dropLabel.setForeground (Color.BLACK);
                            dropLabel.setBounds (Worker.getBounds (1661, 463, 104, 30));
                            
                            decorators[1].setVisible (false);
                            decorators[4].setVisible (true);
                            
                            for (JLabel dropHitbox : dropHitboxes)
                            {
                                dropHitbox.setVisible (false);
                            }
                            
                            layeredPane.remove (scrollPane[0]);
                            
                            List<String> records = Database.loadForScoreHistory ("HISTORY", name, null), filter = new ArrayList<>();
                            
                            for (int i = 0; i < records.size(); i += 3)
                            {
                                try
                                {
                                    int score = Integer.parseInt (records.get (i + 1)), totalScore = Integer.parseInt (records.get (i + 2));
                                    
                                    if (score >= totalScore / 2)
                                    {
                                        filter.add (records.get(i));
                                        filter.add (records.get (i + 1));
                                        filter.add (records.get (i + 2));
                                    }
                                }
                                catch (NumberFormatException e_2)
                                {
                                    // Ignore
                                }
                            }
                            
                            if (filter.isEmpty())
                            {
                                decorators[4].setVisible (false);
                                
                                scrollPane[0] = Displayer.displayNoResultFound();
                            }
                            else
                            {
                                decorators[4].setVisible (true);
                                
                                scrollPane[0] = Displayer.displayScoreHistory (filter);
                            }
                            
                            layeredPane.add (scrollPane[0], Integer.valueOf(2));
                            layeredPane.revalidate();
                            layeredPane.repaint();
                        }
                        
                    });
                }
                case 6 ->
                {
                    dropHitboxes[i].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/170.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/163.png", 234, 283);
                            decorators[1].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            dropLabel.setText ("NEEDS IMPROVEMENT");
                            dropLabel.setFont (Worker.getFont ("/quiz_generator/Font/Arimo-Regular.ttf", 1, 14.4f));
                            dropLabel.setForeground (Color.BLACK);
                            dropLabel.setBounds (Worker.getBounds (1617, 468, 214, 22));
                            
                            decorators[1].setVisible (false);
                            decorators[4].setVisible (true);
                            
                            for (JLabel dropHitbox : dropHitboxes)
                            {
                                dropHitbox.setVisible (false);
                            }
                            
                            layeredPane.remove (scrollPane[0]);
                            
                            List<String> records = Database.loadForScoreHistory ("HISTORY", name, null), filter = new ArrayList<>();
                            
                            for (int i = 0; i < records.size(); i += 3)
                            {
                                try
                                {
                                    int score = Integer.parseInt (records.get (i + 1)), totalScore = Integer.parseInt (records.get (i + 2));
                                    
                                    if (score < totalScore / 2)
                                    {
                                        filter.add (records.get(i));
                                        filter.add (records.get (i + 1));
                                        filter.add (records.get (i + 2));
                                    }
                                }
                                catch (NumberFormatException e_2)
                                {
                                    // Ignore
                                }
                            }
                            
                            if (filter.isEmpty())
                            {
                                decorators[4].setVisible (false);
                                
                                scrollPane[0] = Displayer.displayNoResultFound();
                            }
                            else
                            {
                                decorators[4].setVisible (true);
                                
                                scrollPane[0] = Displayer.displayScoreHistory (filter);
                            }
                            
                            layeredPane.add (scrollPane[0], Integer.valueOf(2));
                            layeredPane.revalidate();
                            layeredPane.repaint();
                        }
                        
                    });
                }
            }
        }
        
        MouseAdapter focusLoss = new MouseAdapter()
        {
            @Override
            public void mousePressed (MouseEvent e)
            {
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/160.png", 305, 30);
                decorators[0].setIcon (decoratorImage);
                
                decorators[1].setVisible (false);
                
                for (JLabel dropHitbox : dropHitboxes)
                {
                    dropHitbox.setVisible (false);
                }
                
                dropLabel.setForeground (Color.BLACK);
            }
            
        };
        
        hitboxes[3].addMouseListener (focusLoss);
        
        frontend.getPanel().addMouseListener (focusLoss);
        
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.add (username);
        components.add (dropLabel);
        components.addAll (Arrays.asList (hitboxes));
        components.addAll (Arrays.asList (dropHitboxes));
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        return layeredPane;
    }
}