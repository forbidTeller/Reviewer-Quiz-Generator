package quiz_generator.Frontend;

import quiz_generator.Backend.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

class QuizConfigurator extends MainComponent
{
    private final UITransition transition;
    private final Start start;
    private Leaderboard leaderboard;
    
    private JTextField lockedInput = null;
    
    // CONSTRUCTOR
    
    QuizConfigurator (BackendSharedData backend, FrontendSharedData frontend, UITransition transition, Start start)
    {
        super (backend, frontend);
        
        this.transition = transition;
        this.start = start;
    }
    
    // A SETTER FOR LEADERBOARD OBJECT
    
    void setLeaderboard (Leaderboard leaderboard) { this.leaderboard = leaderboard; }
    
    void reusableFileErrorScreen (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        if (frontend.getPanel() != null)
        {
            changeComponents (frame);
            
            frontend.setPanel (null);
            frontend.setNextPanel (FrontendSharedData.Page.ERROR_SCREEN);
            frontend.setPanel (background());
            
            frame.add (frontend.getPanel());
            frame.revalidate();
            frame.repaint();
        }
        
        JPanel notifPanel = new JPanel (null);
        notifPanel.setOpaque (false);
        notifPanel.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/17.png", "/quiz_generator/Design/21.png"};
        int[] x = {895, 654, 512};
        
        if (frontend.state.contains (FrontendSharedData.Condition.FILE_EXISTED))
        {
            decoratorImageName[0] = "/quiz_generator/Design/18.png";
            
            x[0] = 955;
            x[2] = 482;
        }
        
        if (frontend.state.contains (FrontendSharedData.Condition.TOO_SMALL))
        {
            decoratorImageName[0] = "/quiz_generator/Design/19.png";
                
            x[0] = 926;
            x[2] = 496;
        }
            
        if (frontend.state.contains (FrontendSharedData.Condition.TOO_LARGE))
        {
            decoratorImageName[0] = "/quiz_generator/Design/20.png";
                
            x[0] = 930;
            x[2] = 494;
        }
           
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
                    decorators[i].setBounds (Worker.getBounds (x[2], 137, x[0], x[1]));
                    
                    x[0] = 310;
                    x[1] = 85;
                    x[2] = 804;
                }
                case 1 -> decorators[i].setBounds (Worker.getBounds (x[2], 831, x[0], x[1]));
            }
        }
        
        JLabel hitbox = new JLabel();
        hitbox.setCursor (new Cursor (Cursor.HAND_CURSOR));
        hitbox.setOpaque (false);
        hitbox.setBounds (Worker.getBounds (804, 831, 310, 85));
        
        hitbox.addMouseListener (new MouseAdapter()
        {
            @Override
            public void mouseEntered (MouseEvent e)
            {
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/22.png", 310, 85);
                decorators[1].setIcon (decoratorImage);
            }
            
            @Override
            public void mouseExited (MouseEvent e)
            {
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/21.png", 310, 85);
                decorators[1].setIcon (decoratorImage);
            }
            
            @Override
            public void mousePressed (MouseEvent e)
            {
                backend.setSize(0);
                backend.getMap().clear();
                
                transition.quickLoadingScreen (frame);
                
                new SwingWorker<Void, Void>()
                {
                    @Override
                    protected Void doInBackground() throws Exception
                    {
                        Thread.sleep (5000);
                        
                        return null;
                    }
                    
                    @Override
                    protected void done()
                    {
                        frontend.state.remove (FrontendSharedData.Condition.FILE_EXISTED);
                        frontend.state.remove (FrontendSharedData.Condition.TOO_SMALL);
                        frontend.state.remove (FrontendSharedData.Condition.TOO_LARGE);
                        
                        changeComponents (frame);
                        
                        frontend.setPanel (null);
                        frontend.setNextPanel (FrontendSharedData.Page.MAIN_PAGE);
                        frontend.setPanel (background());
                        frontend.getPanel().setLayout (layout);
                        frontend.getPanel().add (start.getPage1 (frame, layout, highlightedTerms));
                        
                        layout.show (frontend.getPanel(), "page1");
                        
                        frame.add (frontend.getPanel());
                        frame.revalidate();
                        frame.repaint();
                    }
                    
                }.execute();
            }
            
        });
        
        notifPanel.add (hitbox);
        notifPanel.add (decorators[1]);
        notifPanel.add (decorators[0]);
        
        frontend.getPanel().add (notifPanel);
        frontend.getPanel().revalidate();
        frontend.getPanel().repaint();
    }
    
    void instructionPanel (JFrame frame)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
       
        String[] decoratorImageName = {"/quiz_generator/Design/52.png", "/quiz_generator/Design/53.png", "/quiz_generator/Design/55.png"};
        int[] x = {1041, 946, 476, 80};
        
        if (frontend.state.contains (FrontendSharedData.Condition.IS_TEXT))
        {
            decoratorImageName[0] = "/quiz_generator/Design/57.png";
            decoratorImageName[1] = "/quiz_generator/Design/58.png";
        }
        
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
                    decorators[i].setBounds (Worker.getBounds (x[2], x[3], x[0], x[1]));
                    
                    x[0] = 153;
                    x[1] = 39;
                    x[2] = (frontend.state.contains (FrontendSharedData.Condition.IS_TEXT)) ? 767 : 1077;
                    x[3] = 665;             
                   
                }
                case 1 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (x[2], x[3], x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (x[2], x[3], x[0], x[1]));
                    
                    x[0] = 124;
                    x[1] = 124;
                    x[2] = 1427;
                    x[3] = 58;
                }
                case 2 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (x[2], x[3], x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (x[2], x[3], x[0], x[1]));
                }
            }
        }
        
        if (frontend.state.contains (FrontendSharedData.Condition.IS_TEXT))
        {
            hitboxes[1].addMouseListener (new MouseAdapter()
            {
                @Override
                public void mouseEntered (MouseEvent e)
                {
                    ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/59.png", 153, 39);
                    decorators[1].setIcon (decoratorImage);
                }
                            
                @Override
                public void mouseExited (MouseEvent e)
                {
                    ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/58.png", 153, 39);
                    decorators[1].setIcon (decoratorImage);
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
                            frame.getLayeredPane().remove (layeredPane);
                            
                            frontend.state.remove (FrontendSharedData.Condition.IS_TEXT);
                                        
                            instructionPanel (frame);
                        }
                                    
                    }.execute();
                }
                            
            });
        }
        else
        {
            hitboxes[1].addMouseListener (new MouseAdapter()
            {
                @Override
                public void mouseEntered (MouseEvent e)
                {
                    ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/54.png", 153, 39);
                    decorators[1].setIcon (decoratorImage);
                }
                            
                @Override
                public void mouseExited (MouseEvent e)
                {
                    ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/53.png", 153, 39);
                    decorators[1].setIcon (decoratorImage);
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
                            frame.getLayeredPane().remove (layeredPane);
                            
                            frontend.state.add (FrontendSharedData.Condition.IS_TEXT);
                            
                            instructionPanel (frame);
                        }
                        
                    }.execute();
                }
                            
            });
        }
        
        hitboxes[2].addMouseListener (new MouseAdapter()
        {
            @Override
            public void mouseEntered (MouseEvent e)
            {
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/56.png", 124, 124);
                decorators[2].setIcon (decoratorImage);
            }
            
            @Override
            public void mouseExited (MouseEvent e)
            {
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/55.png", 124, 124);
                decorators[2].setIcon (decoratorImage);
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
                        if (frontend.state.contains (FrontendSharedData.Condition.IS_TEXT)) frontend.state.remove (FrontendSharedData.Condition.IS_TEXT);
                        
                        frame.getLayeredPane().remove (layeredPane);
                        frame.getLayeredPane().revalidate();
                        frame.getLayeredPane().repaint();
                    }
                    
                }.execute();
            }
            
        });
        
        layeredPane.addMouseListener (new MouseAdapter()
        {
            @Override
            public void mousePressed (MouseEvent e)
            {
                // Ignore
                
                // THIS AVOIDS CLICKING THROUGH PAGE1 METHOD
            }
            
        });
        
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.addAll (Arrays.asList (hitboxes));
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        frame.getLayeredPane().add (layeredPane, JLayeredPane.POPUP_LAYER);
        frame.getLayeredPane().revalidate();
        frame.getLayeredPane().repaint();
    }
    
    void pausePanel (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/85.png", "/quiz_generator/Design/86.png", "/quiz_generator/Design/88.png"};
        int[] x = {1026, 703};
        
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
                    decorators[i].setBounds (Worker.getBounds (447, 195, x[0], x[1]));
                    
                    x[0] = 576;
                    x[1] = 111;
                }
                case 1 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (691, 456, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (691, 456, x[0], x[1]));
                }
                case 2 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (691, 627, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (691, 627, x[0], x[1]));
                }
            }
        }
        
        JPanel clickBlocker = new JPanel();
        clickBlocker.setBackground (new Color (0, 0, 0, 150));
        clickBlocker.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        final boolean[] mouseLock = {false};
        
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
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/87.png", 576, 111);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/86.png", 576, 111);
                            decorators[j].setIcon (decoratorImage);
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
                                    frontend.getClock().start();
                                    
                                    frame.getLayeredPane().remove (layeredPane);
                                    frame.getLayeredPane().revalidate();
                                    frame.getLayeredPane().repaint();
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
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/89.png", 576, 111);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/88.png", 576, 111);
                            decorators[j].setIcon (decoratorImage);
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
                                    frame.getLayeredPane().remove (layeredPane);
                                    
                                    frontend.state.add (FrontendSharedData.Condition.QUIT_IS_CLICKED);
                                    
                                    notificationPanel (frame, layout, highlightedTerms);
                                }
                                
                            }.execute();
                        }
                        
                    });
                }
            }
        }
        
        clickBlocker.addMouseListener (new MouseAdapter()
        {
            @Override
            public void mousePressed (MouseEvent e)
            {
                e.consume();
            }
            
        });
         
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.addAll (Arrays.asList (hitboxes));
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        layeredPane.add (clickBlocker, Integer.valueOf(-1));
        
        frame.getLayeredPane().add (layeredPane, JLayeredPane.POPUP_LAYER);
        frame.getLayeredPane().revalidate();
        frame.getLayeredPane().repaint();
    }
    
    void notificationPanel (JFrame frame, CardLayout layout, List<String> highlightedTerms, JTextField input)
    {
        lockedInput = input;
        
        notificationPanel (frame, layout, highlightedTerms);
    }
    
    void notificationPanel (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"", "/quiz_generator/Design/93.png", "/quiz_generator/Design/95.png"};
        int[] x = {1709, 893};
        
        decoratorImageName[0] = ((frontend.state.contains (FrontendSharedData.Condition.RETRY_IS_CLICKED)) ?  "/quiz_generator/Design/91.png" : (frontend.state.contains (FrontendSharedData.Condition.EXIT_IS_CLICKED)) ? "/quiz_generator/Design/92.png" : (frontend.state.contains (FrontendSharedData.Condition.IS_OVERWRITE)) ? "/quiz_generator/Design/136.png" : "/quiz_generator/Design/90.png");
        
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
                    decorators[i].setBounds (Worker.getBounds (115, 132, x[0], x[1]));
                    
                    x[0] = 566;
                    x[1] = 101;
                }
                case 1 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (647, 526, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (647, 526, x[0], x[1]));
                }
                case 2 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (647, 662, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (647, 662, x[0], x[1]));
                }
            }
        }
        
        JPanel clickBlocker = new JPanel();
        clickBlocker.setBackground (new Color (0, 0, 0, 150));
        clickBlocker.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        final boolean[] mouseLock = {false};
        
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
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/94.png", 566, 101);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/93.png", 566, 101);
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
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/96.png", 566, 101);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/95.png", 566, 101);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                    });
                }
            }
        }
        
        for (int i = 0; i < decoratorImageName.length; i++)
        {
            final int j = i;
            
            if (frontend.state.contains (FrontendSharedData.Condition.QUIT_IS_CLICKED))
            {
                switch(i)
                {
                    case 1 ->
                    {
                        hitboxes[j].addMouseListener (new MouseAdapter()
                        {
                            @Override
                            public void mousePressed (MouseEvent e)
                            {
                                if (mouseLock[0]) return;
                                
                                mouseLock[0] = true;
                                
                                frame.getLayeredPane().remove (layeredPane);
                                    
                                backend.partialReset (highlightedTerms);
                                
                                transition.quickLoadingScreen (frame);
                                
                                new SwingWorker<Void, Void>()
                                {
                                    @Override
                                    protected Void doInBackground() throws Exception
                                    {
                                        Thread.sleep (5000);
                                        
                                        return null;
                                    }
                                    
                                    @Override
                                    protected void done()
                                    {
                                        frontend.state.clear();
                                        frontend.state.add (FrontendSharedData.Condition.FILE_EXISTED);
                                        
                                        changeComponents (frame);
                                        
                                        frontend.setPanel (null);
                                        frontend.setNextPanel (FrontendSharedData.Page.PAGE_2);
                                        frontend.setPanel (background());
                                        frontend.getPanel().setLayout (layout);
                                        frontend.getPanel().add (start.getPage2 (frame, layout, highlightedTerms), "page2");
                                        
                                        layout.show (frontend.getPanel(), "page2");
                                        
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
                                        frame.getLayeredPane().remove (layeredPane);
                                        
                                        frontend.state.remove (FrontendSharedData.Condition.QUIT_IS_CLICKED);
                                        
                                        pausePanel (frame, layout, highlightedTerms);
                                    }
                                    
                                }.execute();
                            }
                            
                        });
                    }
                }
            }
            
            if (frontend.state.contains (FrontendSharedData.Condition.IS_OVERWRITE))
            {
                switch(i)
                {
                    case 1 ->
                    {
                        hitboxes[j].addMouseListener (new MouseAdapter()
                        {
                            @Override
                            public void mousePressed (MouseEvent e)
                            {
                                if (mouseLock[0]) return;
                                
                                mouseLock[0] = true;
                                
                                frame.getLayeredPane().remove (layeredPane);
                                
                                Database.overwriteForLeaderboard (frontend.getName(), String.valueOf (backend.getScore()), String.valueOf (backend.getQuestions().size()));
                                Database.appendForScoreHistory (frontend.getName(), String.valueOf (backend.getScore()), String.valueOf (backend.getQuestions().size()));
                                                                
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
                                        frontend.state.remove (FrontendSharedData.Condition.IS_OVERWRITE);
                                        
                                        changeComponents (frame);
                                        
                                        frontend.setPanel (null);
                                        frontend.setNextPanel (FrontendSharedData.Page.IN_LEADERBOARD);
                                        frontend.setPanel (background());
                                        frontend.getPanel().setLayout (layout);
                                        frontend.getPanel().add (leaderboard.showLeaderboard (frame, layout, highlightedTerms), "showLeaderboard");
                                        
                                        layout.show (frontend.getPanel(), "showLeaderboard");
                                        
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
                                        frontend.state.remove (FrontendSharedData.Condition.IS_OVERWRITE);
                                        
                                        if (lockedInput != null)
                                        {
                                            lockedInput.setEditable (true);
                                            lockedInput.setFocusable (true);
                                            lockedInput = null;
                                        }
                                        
                                        frame.getLayeredPane().remove (layeredPane);
                                        frame.getLayeredPane().revalidate();
                                        frame.getLayeredPane().repaint();
                                    }
                                    
                                }.execute();
                            }
                            
                        });
                    }
                }
            }
            
            if (frontend.state.contains (FrontendSharedData.Condition.RETRY_IS_CLICKED))
            {
                switch(i)
                {
                    case 1 ->
                    {
                        hitboxes[j].addMouseListener (new MouseAdapter()
                        {
                            @Override
                            public void mousePressed (MouseEvent e)
                            {
                                if (mouseLock[0]) return;
                                
                                mouseLock[0] = true;
                                
                                frame.getLayeredPane().remove (layeredPane);
                                
                                backend.retryReset();
                                
                                transition.quickLoadingScreen (frame);
                                
                                new SwingWorker<Void, Void>()
                                {
                                    @Override
                                    protected Void doInBackground() throws Exception
                                    {
                                        Thread.sleep (5000);
                                        
                                        return null;
                                    }
                                    
                                    @Override
                                    protected void done()
                                    {
                                        frontend.state.clear();
                                        frontend.state.add (FrontendSharedData.Condition.FILE_EXISTED);
                                        
                                        changeComponents (frame);
                                        
                                        frontend.setPanel (null);
                                        frontend.setNextPanel (FrontendSharedData.Page.PAGE_2);
                                        frontend.setPanel (background());
                                        frontend.getPanel().setLayout (layout);
                                        frontend.getPanel().add (start.getPage2 (frame, layout, highlightedTerms), "page2");
                                        
                                        layout.show (frontend.getPanel(), "page2");
                                        
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
                                        frontend.state.remove (FrontendSharedData.Condition.RETRY_IS_CLICKED);
                                        
                                        frame.getLayeredPane().remove (layeredPane);
                                        frame.getLayeredPane().revalidate();
                                        frame.getLayeredPane().repaint();
                                    }
                                    
                                }.execute();
                            }
                            
                        });
                    }
                }
            }
            
            if (frontend.state.contains (FrontendSharedData.Condition.EXIT_IS_CLICKED))
            {
                switch(i)
                {
                    case 1 ->
                    {
                        hitboxes[j].addMouseListener (new MouseAdapter()
                        {
                            @Override
                            public void mousePressed (MouseEvent e)
                            {
                                if (mouseLock[0]) return;
                                
                                mouseLock[0] = true;
                                
                                frame.getLayeredPane().remove (layeredPane);
                                
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
                                        System.exit(0);
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
                                        frontend.state.remove (FrontendSharedData.Condition.EXIT_IS_CLICKED);
                                        
                                        frame.getLayeredPane().remove (layeredPane);
                                        frame.getLayeredPane().revalidate();
                                        frame.getLayeredPane().repaint();
                                    }
                                    
                                }.execute();
                            }
                            
                        });
                    }
                }
            }
        }
        
        clickBlocker.addMouseListener (new MouseAdapter()
        {
            @Override
            public void mousePressed (MouseEvent e)
            {
                e.consume();
            }
            
        });
        
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.addAll (Arrays.asList (hitboxes));
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        layeredPane.add (clickBlocker, Integer.valueOf(-1));
        
        frame.getLayeredPane().add (layeredPane, JLayeredPane.POPUP_LAYER);
        frame.getLayeredPane().revalidate();
        frame.getLayeredPane().repaint();
    }
}