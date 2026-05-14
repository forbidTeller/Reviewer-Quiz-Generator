package quiz_generator.Frontend;

import quiz_generator.Backend.*;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import java.io.*;

public class Start extends MainComponent
{  
    // UI MANAGERS
    
    private final UITransition transition;
    private final QuizConfigurator configurator;
    private final QuizTypes quizTypes;
    private final QuizTimer quizTimer;
    private final Correctors correctors;
    private final Leaderboard leaderboard;
    
    // CONSTRUCTOR
    
    public Start()
    {
        super (new BackendSharedData(), new FrontendSharedData());
                
        transition = new UITransition (backend, frontend);
        configurator = new QuizConfigurator (backend, frontend, transition, this);
        quizTypes = new QuizTypes (backend, frontend, transition, configurator, this);
        correctors = new Correctors (backend, frontend, quizTypes);
        quizTimer = new QuizTimer (backend, frontend, quizTypes, transition, correctors);
        leaderboard = new Leaderboard (backend, frontend, transition, configurator);
        
        quizTypes.setCorrectors (correctors);
        quizTypes.setQuizTimer (quizTimer);
        configurator.setLeaderboard (leaderboard);
    }
    
    public void runSystem()
    {
        JFrame frame = formatFrame();     
        CardLayout layout = new CardLayout();    
        List<String> highlightedTerms = new ArrayList<>();
        
        frontend.getState().clear();
        frontend.setPanel (background());
        frontend.getPanel().setLayout (layout);
        frontend.getPanel().add (getPage1 (frame, layout, highlightedTerms), "page1");
        
        layout.show (frontend.getPanel(), "page1");
        
        frame.add (frontend.getPanel());
        frame.setVisible (true);
    }
    
    private JLayeredPane page1 (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        ImageIcon mainDecorator = Worker.getDecoratorImage ("/quiz_generator/Design/2.png", 1427, 851);
        
        JLabel label = new JLabel (mainDecorator);
        label.setBounds (Worker.getBounds (276, 85, 1427, 851));
        
        String[] decoratorImageName = {"/quiz_generator/Design/3.png", "/quiz_generator/Design/5.png", "/quiz_generator/Design/5.png", "/quiz_generator/Design/5.png", "/quiz_generator/Design/7.png", "/quiz_generator/Design/9.png", "/quiz_generator/Design/11.png", "/quiz_generator/Design/50.png"};
        int[] x = {1045, 292};
        
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
                    
                    decorators[i].setBounds (Worker.getBounds (484, 426, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (484, 426, x[0], x[1]));
                    
                    x[0] = 362;
                    x[1] = 193;
                }
                case 1 ->
                {
                    decorators[i].setBounds (Worker.getBounds (413, 743, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (413, 743, x[0], x[1]));
                }
                case 2 ->
                {
                    decorators[i].setBounds (Worker.getBounds (815, 743, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (815, 743, x[0], x[1]));
                }
                case 3 ->
                {
                    decorators[i].setBounds (Worker.getBounds (1217, 743, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1217, 743, x[0], x[1]));
                    
                    x[0] = 45;
                    x[1] = 45;
                }
                case 4 -> decorators[i].setBounds (Worker.getBounds (442, 761, x[0], x[1]));
                case 5 ->
                {
                    decorators[i].setBounds (Worker.getBounds (849, 761, x[0], x[1]));
                    
                    x[0] += 4;
                }
                case 6 ->
                {
                    decorators[i].setBounds (Worker.getBounds (1251, 761, x[0], x[1]));
                    
                    x[0] = 235;
                    x[1] = 30;
                }
                case 7 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (880, 980, 235, 30));
                    hitboxes[i].setBounds (Worker.getBounds (880, 980, 235, 30));
                }
                
            }
        }
        
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/4.png", 1045, 292);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/3.png", 1045, 292);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            JFileChooser fileChooser = new JFileChooser();
                            
                            String currentDirectory = System.getProperty ("user.dir");
                            
                            fileChooser.setCurrentDirectory (new File (currentDirectory, "test subjects"));
                            fileChooser.setAcceptAllFileFilterUsed (false);
                            fileChooser.setMultiSelectionEnabled (false);
                            
                            FileNameExtensionFilter filter = new FileNameExtensionFilter ("Supported Files (.txt .docx .pdf)", "txt", "docx", "pdf");
                            fileChooser.setFileFilter (filter);
                            
                            int response = fileChooser.showOpenDialog (null);
                            
                            if (response == JFileChooser.APPROVE_OPTION)
                            {
                                backend.setSelectedFile (fileChooser.getSelectedFile());                                       
                                backend.setFileName (backend.getSelectedFile().getName().toLowerCase());
                                
                                if (!backend.getSelectedFile().exists())
                                {
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
                                            configurator.reusableFileErrorScreen (frame, layout, highlightedTerms);
                                        }
                                        
                                    }.execute();
                                }
                                else if (backend.getFileName().endsWith (".txt") || backend.getFileName().endsWith (".docx") || backend.getFileName().endsWith (".pdf"))
                                {
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
                                            frontend.getState().add (FrontendSharedData.Condition.FILE_EXISTED);
                                            
                                            changeComponents (frame);
                                            
                                            frontend.setPanel (null);
                                            frontend.setNextPanel (FrontendSharedData.Page.PAGE_2);
                                            frontend.setPanel (background());
                                            frontend.getPanel().setLayout (layout);
                                            frontend.getPanel().add (page2 (frame, layout, highlightedTerms), "page2");
                                            
                                            layout.show (frontend.getPanel(), "page2");
                                            
                                            frame.add (frontend.getPanel());
                                            frame.revalidate();
                                            frame.repaint();
                                        }
                                        
                                    }.execute();
                                }
                            }
                        }
                        
                    });
                }
                case 1 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/6.png", 362, 192);
                            decorators[j].setIcon (decoratorImage);                                                  
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/5.png", 362, 192);
                            decorators[j].setIcon (decoratorImage);
                            
                            ImageIcon decoratorImage_1 = Worker.getDecoratorImage ("/quiz_generator/Design/7.png", 45, 45);
                            decorators[4].setText ("");
                            decorators[4].setIcon (decoratorImage_1);
                            decorators[4].setBounds (Worker.getBounds (442, 761, 45, 45));
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            decorators[4].setIcon (null);
                            decorators[4].setText (Worker.getGif ("/quiz_generator/Design/8.gif", 102, 57));
                            decorators[4].setBounds (Worker.getBounds (418, 755, 102, 57));
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/6.png", 362, 192);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/5.png", 362, 192);
                            decorators[j].setIcon (decoratorImage);
                            
                            ImageIcon decoratorImage_1 = Worker.getDecoratorImage ("/quiz_generator/Design/9.png", 45, 45);
                            decorators[5].setText("");
                            decorators[5].setIcon (decoratorImage_1);
                            decorators[5].setBounds (Worker.getBounds (849, 761, 45, 45));
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            decorators[5].setIcon (null);
                            decorators[5].setText (Worker.getGif ("/quiz_generator/Design/10.gif", 102, 57));
                            decorators[5].setBounds (Worker.getBounds (821, 755, 102, 57));
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/6.png", 362, 192);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/5.png", 362, 192);
                            decorators[j].setIcon (decoratorImage);
                            
                            ImageIcon decoratorImage_1 = Worker.getDecoratorImage ("/quiz_generator/Design/11.png", 45 + 4, 45);
                            decorators[6].setText("");
                            decorators[6].setIcon (decoratorImage_1);
                            decorators[6].setBounds (Worker.getBounds (1251, 761, 45 + 4, 45));
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            decorators[6].setIcon (null);
                            decorators[6].setText (Worker.getGif ("/quiz_generator/Design/12.gif", 116, 65));
                            decorators[6].setBounds (Worker.getBounds (1218, 752, 116, 65));
                        }
                        
                    });
                }
                case 7 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/51.png", 235, 30);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/50.png", 235, 30);
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
                                    configurator.instructionPanel (frame);
                                }
                                
                            }.execute();
                        }
                        
                    });
                }
            }
        }
        
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.add (label);
        components.addAll (Arrays.asList (hitboxes));
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        return layeredPane;
    }
    
    private JLayeredPane page2 (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        ImageIcon mainDecorator = Worker.getDecoratorImage ("/quiz_generator/Design/24.png", 769, 853);
        
        JLabel label = new JLabel (mainDecorator);
        label.setBounds (Worker.getBounds (575, 108, 769, 853));
        
        String[] decoratorImageName = {"/quiz_generator/Design/25.png", "/quiz_generator/Design/27.png", "/quiz_generator/Design/29.png", "/quiz_generator/Design/31.png"};
        int[] x = {670, 126, 625};
        
        ImageIcon[] icons;
        
        JLabel[] decorators = new JLabel[decoratorImageName.length];
        JLabel[] hitboxes = new JLabel[decoratorImageName.length];
        
        for (int i = 0; i < decoratorImageName.length; i++)
        {
            icons = Worker.getDecoratorImages (decoratorImageName, x[0], x[1]);
            decorators[i] = new JLabel (icons[i]);
            
            hitboxes[i] = new JLabel();
            hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
            hitboxes[i].setOpaque (false);
            
            switch(i)
            {
                case 0 ->
                {
                    decorators[i].setBounds (Worker.getBounds (x[2], 343, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (x[2], 343, x[0], x[1]));
                }
                case 1 ->
                {
                    decorators[i].setBounds (Worker.getBounds (x[2], 504, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (x[2], 504, x[0], x[1]));
                }
                case 2 ->
                {
                    decorators[i].setBounds (Worker.getBounds (x[2], 666, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (x[2], 666, x[0], x[1]));
                    
                    x[0] = 235;
                    x[1] = 82;
                }
                case 3 ->
                {
                    decorators[i].setBounds (Worker.getBounds (x[2], 837, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (x[2], 837, x[0], x[1]));
                }
            }
        }
        
        final boolean[] mouseLock = {false};
        
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
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/26.png", 670, 126);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/25.png", 670, 126);
                            decorators[j].setIcon (decoratorImage);
                        }                       
                        
                    });
                }
                case 1 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/28.png", 670, 126);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/27.png", 670, 126);
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
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/30.png", 670, 126);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/29.png", 670, 126);
                            decorators[j].setIcon (decoratorImage);
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
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/32.png", 235, 82);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/31.png", 235, 82);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            mouseLock[0] = true;
                            
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
                                    frontend.getState().remove (FrontendSharedData.Condition.FILE_EXISTED);
                                    
                                    changeComponents (frame);
                                    
                                    frontend.setPanel (null);
                                    frontend.setNextPanel (FrontendSharedData.Page.MAIN_PAGE);
                                    frontend.setPanel (background());
                                    frontend.getPanel().setLayout (layout);
                                    frontend.getPanel().add (page1 (frame, layout, highlightedTerms), "page1");
                                    
                                    layout.show (frontend.getPanel(), "page1");
                                    
                                    frame.add (frontend.getPanel());
                                    frame.revalidate();
                                    frame.repaint();
                                }
                                
                            }.execute();
                        }
                        
                    });
                }
            }
        }
        
        for (int i = 1; i < 4; i++)
        {
            final int j = i;
            
            hitboxes[j - 1].addMouseListener (new MouseAdapter()
            {
                @Override
                public void mousePressed (MouseEvent e)
                {
                    if (mouseLock[0]) return;
                    
                    mouseLock[0] = true;
                    
                    frame.setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR));
                    
                    new SwingWorker<Void, Void>()
                    {
                        @Override
                        protected Void doInBackground() throws Exception
                        {
                            Thread.sleep (50);
                            
                            return null;
                        }
                        
                        @Override
                        protected void done()
                        {
                            try
                            {
                                backend.getIndex()[1] = j;
                                
                                changeComponents (frame);
                                
                                frontend.setPanel (null);
                                frontend.setNextPanel (FrontendSharedData.Page.PAGE_3);
                                frontend.setPanel (background());
                                frontend.getPanel().setLayout (layout);
                                frontend.getPanel().add (page3 (frame, layout, highlightedTerms));
                                
                                layout.show (frontend.getPanel(), "page3");
                                
                                frame.add (frontend.getPanel());
                                frame.revalidate();
                                frame.repaint();
                            }
                            catch (Exception e)
                            {
                                // Ignore
                            }
                            finally
                            {
                                frame.setCursor (Cursor.getDefaultCursor());
                            }
                        }
                        
                    }.execute();
                }
                
            });
        }
        
        List<JComponent> components = new ArrayList<>();
        
        components.add (label);
        components.addAll (Arrays.asList (decorators));
        components.addAll (Arrays.asList (hitboxes));
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
              
        return layeredPane;
    }
    
    private JLayeredPane page3 (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        try
        {
            backend.setChoices (QuickProcessor.triggeredChoice (backend, highlightedTerms));
            backend.setSize (backend.getAnswers().size());
            
            if (frontend.getState().contains (FrontendSharedData.Condition.FILE_EXISTED))
            {
                if (backend.getSize() > 99)
                {
                    frontend.getState().add (FrontendSharedData.Condition.TOO_LARGE);
                
                    throw new Exception();
                }
            
                if (backend.getSize() < 10)
                {
                    frontend.getState().add (FrontendSharedData.Condition.TOO_SMALL);
               
                    throw new Exception();
                }
            }
        }
        catch (Exception e)
        {
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
                    configurator.reusableFileErrorScreen (frame, layout, highlightedTerms);
            
                    backend.setUsedOnce (true);
                }
                
            }.execute();
        }
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        ImageIcon mainDecorator = Worker.getDecoratorImage ("/quiz_generator/Design/34.png", 1032, 641);
        
        JLabel label = new JLabel (mainDecorator);
        label.setBounds (Worker.getBounds (453, 145, 1032, 641));
        
        String[] decoratorImageName = {"/quiz_generator/Design/35.png", "/quiz_generator/Design/37.png", "/quiz_generator/Design/39.png", "/quiz_generator/Design/41.png"};
        int[] x = {77, 77};
        
        ImageIcon[] icons;
        
        JLabel[] decorators = new JLabel[decoratorImageName.length];
        JLabel[] hitboxes =  new JLabel[decoratorImageName.length];
        
        for (int i = 0; i < decoratorImageName.length; i++)
        {
            icons = Worker.getDecoratorImages (decoratorImageName, x[0], x[1]);
            decorators[i] = new JLabel (icons[i]);
            
            hitboxes[i] = new JLabel();
            hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
            hitboxes[i].setOpaque (false);
            
            switch(i)
            {
                case 0 ->
                {
                    decorators[i].setBounds (Worker.getBounds (1263, 553, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1263, 553, x[0], x[1]));
                    
                    x[0] = 86;
                    x[1] = 13;
                }
                case 1 ->
                {
                    decorators[i].setBounds (Worker.getBounds (589, 585, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (589, 585, x[0], x[1]));
                    
                    x[0] = 328;
                    x[1] = 106;
                }
                case 2 ->
                {
                    decorators[i].setBounds (Worker.getBounds (1137, 829, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1137, 829, x[0], x[1]));
                    
                    x[0] = 213;
                    x[1] = 106;
                }
                case 3 ->
                {
                    decorators[i].setBounds (Worker.getBounds (453, 829, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (453, 829, x[0], x[1]));
                }
            }
        }
        
        JTextField input = new JTextField();
        input.setFont (Worker.getFont ("/quiz_generator/Font/Montserrat-Regular.ttf", 0, 63.6f));
        input.setText (String.valueOf (backend.getSize()));
        input.setForeground (Color.WHITE);
        input.setBorder (null);
        input.setOpaque (false);
        input.setBounds (Worker.getBounds (910, 541, 114, 101));
        
        AbstractDocument ad = (AbstractDocument) input.getDocument();
        ad.setDocumentFilter (new LimitFilter (2, true));
        
        ImageIcon notifImage = Worker.getDecoratorImage ("/quiz_generator/Design/43.png", 450, 56);
        
        JLabel notifBox = new JLabel (notifImage);
        notifBox.setBounds (Worker.getBounds (734, 642, 450, 56));
        
        JLabel notifText = new JLabel();
        notifText.setFont (Worker.getFont ("/quiz_generator/Font/Montserrat-Regular.ttf", 0, 20f));
        notifText.setForeground (Color.GRAY);
        notifText.setOpaque (false);
        notifText.setBounds (Worker.getBounds (810, 654, 347, 32));
        
        notifBox.setVisible (false);
        notifText.setVisible (false);
        
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/36.png", 77, 77);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/35.png", 77, 77);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            String check = input.getText();
                            
                            if (!check.isEmpty())
                            {     
                                int contained = Integer.parseInt (check);
                                
                                backend.setSize ((contained < backend.getAnswers().size()) ? contained + 1 : backend.getAnswers().size());
                                
                                input.setText (String.format ("%02d", backend.getSize()));
                            }
                        }
                        
                    });
                }
                case 1 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/38.png", 86, 13);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/37.png", 86, 13);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            String check = input.getText();
                            
                            if (!check.isEmpty())
                            {
                                int contained = Integer.parseInt (check);
                                
                                backend.setSize ((contained > 10) ? contained - 1 : 10);
                                
                                input.setText (String.valueOf (backend.getSize()));
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/40.png", 328, 106);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/39.png", 328, 106);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            String check = input.getText();
                                         
                            if (check.isEmpty())
                            {
                                notifText.setText ("Cannot proceed with empty input");
                                
                                notifBox.setVisible (true);
                                notifText.setVisible (true);
                            }
                            else
                            {
                                int contained = Integer.parseInt (check);
                                
                                if (contained > backend.getAnswers().size())
                                {
                                    notifText.setText ("Maximum of " + backend.getAnswers().size() + " questions only");
                                
                                    notifBox.setVisible (true);
                                    notifText.setVisible (true);
                                }  
                                else if (contained < 10)
                                {
                                    notifText.setText ("Minimum of 10 questions only");
                                
                                    notifBox.setVisible (true);
                                    notifText.setVisible (true);
                                }
                                else
                                {
                                    backend.setSize (contained);
                                    backend.setQuestions (new ArrayList<>());
                                    
                                    for (String question : backend.getMap().keySet())
                                    {
                                        if (backend.getQuestions().size() == backend.getSize()) break;
                                        
                                        backend.getQuestions().add (question);
                                    }
                                    
                                    switch (backend.getIndex()[1])
                                    {
                                        case 1 ->
                                        {
                                            backend.getIndex()[0] = 0;
                                            backend.getAnswers().clear();
                                            backend.setAnswers (new ArrayList<>());
                                            
                                            for (String answer : backend.getMap().values())
                                            {
                                                if (backend.getAnswers().size() == backend.getSize()) break;
                                                
                                                backend.getAnswers().add (answer);
                                            }
                                            
                                            backend.setChoices (null);
                                            
                                            try
                                            {
                                                backend.setChoices (QuickProcessor.triggeredChoice (backend, highlightedTerms));
                                            }
                                            catch (Exception e_2)
                                            {
                                                // Ignore
                                            }
                                            
                                            backend.getSavedChoices().put ("SLOT - 1", backend.getChoices());
                                        }
                                        case 3 -> backend.setUsedOnce (true);
                                    }
                                    
                                    transition.mainLoadingScreen (frame);
                                    
                                    new SwingWorker<Void, Void>()
                                    {
                                        @Override
                                        protected Void doInBackground() throws Exception
                                        {
                                            Thread.sleep (20300);
                                            
                                            return null;
                                        }
                                        
                                        @Override
                                        protected void done()
                                        {                                                          
                                            quizTimer.setTimerLimit (frame, layout, highlightedTerms);
                                        }
                                        
                                    }.execute();
                                }
                            }
                            
                            frontend.getPanel().requestFocus();
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/42.png", 213, 106);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/41.png", 213, 106);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            backend.partialReset (highlightedTerms);
                            
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
                                    frontend.setNextPanel (FrontendSharedData.Page.PAGE_2);
                                    frontend.setPanel (background());
                                    frontend.getPanel().setLayout (layout);
                                    frontend.getPanel().add (page2 (frame, layout, highlightedTerms), "page2");
                                    
                                    layout.show (frontend.getPanel(), "page2");
                                    
                                    frame.add (frontend.getPanel());
                                    frame.revalidate();
                                    frame.repaint();
                                }
                                
                            }.execute();
                        }
                        
                    });
                }
            }
        }
        
        input.addMouseListener (new MouseAdapter()
        {
            @Override
            public void mousePressed (MouseEvent e)
            {
                notifBox.setVisible (false);
                notifText.setVisible (false);
            }
            
        });
        
        MouseAdapter focusLoss = new MouseAdapter()
        {
            @Override
            public void mousePressed (MouseEvent e)
            {
                frontend.getPanel().requestFocusInWindow();
                
                notifBox.setVisible (false);
                notifText.setVisible (false);
            }
            
        };
        
        hitboxes[0].addMouseListener (focusLoss);
        hitboxes[1].addMouseListener (focusLoss);
        
        frontend.getPanel().addMouseListener (focusLoss);
        
        List<JComponent> components = new ArrayList<>();
        
        components.add (label);
        components.addAll (Arrays.asList (decorators));
        components.addAll (Arrays.asList (hitboxes));
        components.add (input);
        components.add (notifBox);
        components.add (notifText);
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        return layeredPane;
    }
    
    private JLayeredPane page4 (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        ImageIcon mainDecorator = Worker.getDecoratorImage ("/quiz_generator/Design/123.png", 1620, 949);
        
        JLabel label = new JLabel (mainDecorator);
        label.setBounds (Worker.getBounds (150, 84, 1620, 949));
        
        String[] phrases = {String.valueOf (backend.getScore()), backend.getScore() + " out of " + backend.getQuestions().size(), ""};
        
        phrases[2] = (backend.getScore() == backend.getQuestions().size()) ? "EXCELLENT! WELL DONE!" : (backend.getScore() >= backend.getQuestions().size() / 2) ? "GREAT! KEEP GOING!" : "DON'T STOP! YOU CAN TRY AGAIN!";
        
        JLabel[] text = new JLabel[phrases.length];
        
        for (int i = 0; i < phrases.length; i++)
        {
            text[i] = new JLabel (phrases[i], SwingConstants.CENTER);
            
            switch(i)
            {
                case 0 ->
                {
                    text[i].setFont (Worker.getFont ("/quiz_generator/Font/Montserrat-Regular.ttf", 0, 65.0f));
                    text[i].setForeground (Color.WHITE);
                    text[i].setBounds (Worker.getBounds (958, 296, 94, 103));
                }
                case 1 ->
                {
                    text[i].setFont (Worker.getFont ("/quiz_generator/Font/LeagueSpartan-Regular.ttf", 1, 94.7f));
                    text[i].setForeground (Color.BLACK);
                    text[i].setBounds (Worker.getBounds (688, 421, 543, 118));
                }
                case 2 ->
                {
                    text[i].setFont (Worker.getFont ("/quiz_generator/Font/Schoolbell-Regular.ttf", 0, 39.0f));
                    text[i].setForeground (new Color (24, 0, 173));
                    text[i].setBounds (Worker.getBounds (631, 612, 658, 61));
                }
            }
        }
        
        JTextField input = new JTextField();
        input.setText ("Your username.");
        input.setFont (Worker.getFont ("/quiz_generator/Font/Antic-Regular.ttf", 0, 35.1f));
        input.setForeground (Color.GRAY);
        input.setBorder (null);
        input.setOpaque (false);
        input.setBounds (Worker.getBounds (831, 724, 247, 56));
        
        AbstractDocument ad = (AbstractDocument) input.getDocument();
        ad.setDocumentFilter (new LimitFilter (25, false));
        
        String[] decoratorImageName = {"/quiz_generator/Design/124.png", "/quiz_generator/Design/43.png"};
        int[] x = {229, 86};
        
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
                    decorators[i].setBounds (Worker.getBounds (845, 830, x[0], x[1]));
                    
                    x[0] = 450;
                    x[1] = 56;
                }
                case 1 ->
                {
                    decorators[i].setBounds (Worker.getBounds (751, 668, x[0], x[1]));
                    
                    decorators[i].setVisible (false);
                }
            }
        }
        
        JLabel notifText = new JLabel();
        notifText.setForeground (Color.GRAY);
        notifText.setBounds (Worker.getBounds (815, 684, 371, 25));
        
        notifText.setVisible (false);
        
        JLabel hitbox = new JLabel();
        hitbox.setCursor (new Cursor (Cursor.HAND_CURSOR));
        hitbox.setOpaque (false);
        hitbox.setBounds (Worker.getBounds (845, 830, 259, 86));
        
        hitbox.addMouseListener (new MouseAdapter()
        {
            @Override
            public void mouseEntered (MouseEvent e)
            {
                if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/125.png", 229, 86);
                decorators[0].setIcon (decoratorImage);
            }
            
            @Override
            public void mouseExited (MouseEvent e)
            {
                if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/124.png", 229, 86);
                decorators[0].setIcon (decoratorImage);
            }
            
            @Override
            public void mousePressed (MouseEvent e)
            {
                if (!input.isEditable()) return;
                
                frontend.setName (input.getText().toUpperCase().trim());
                
                if ((frontend.getName().equalsIgnoreCase ("Your username.") && input.getForeground().equals (Color.GRAY)) || frontend.getName().isEmpty())
                {
                    notifText.setText ("Please enter your username");
                    notifText.setFont (Worker.getFont ("/quiz_generator/Font/Montserrat-Regular.ttf", 0, 22.5f));
                    
                    decorators[1].setVisible (true);
                    notifText.setVisible (true);
                }
                else if (frontend.getName().matches (".*[^a-zA-Z ].*"))
                {
                    notifText.setText ("Username must not contain numbers and symbols");
                    notifText.setFont (Worker.getFont ("/quiz_generator/Font/Montserrat-Regular.ttf", 0, 13.5f));
                    
                    decorators[1].setVisible (true);
                    notifText.setVisible (true);
                }
                else if (frontend.getName().length() < 3 || frontend.getName().length() > 12)
                {
                    notifText.setText ("Username must be 3-12 characters");
                    notifText.setFont (Worker.getFont ("/quiz_generator/Font/Montserrat-Regular.ttf", 0, 20.5f));
                    
                    decorators[1].setVisible (true);
                    notifText.setVisible (true);
                }
                else
                {
                    decorators[1].setVisible (false);
                    notifText.setVisible (false);
                    
                    input.setEditable (false);
                    input.setFocusable (false);
                    
                    List<String> names = Database.loadForLeaderboard ("NAME");
                    
                    boolean nameExists = false;
                    
                    for (String name : names)
                    {
                        if (frontend.getName().equalsIgnoreCase (name.trim()))
                        {
                            nameExists = true;
                            
                            break;
                        }
                    }
                    
                    if (!nameExists)
                    {
                        Database.appendForLeaderboard (frontend.getName(), String.valueOf (backend.getScore()));
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
                    else
                    {
                        if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                        
                        ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/125.png", 229, 86);
                        decorators[0].setIcon (decoratorImage);
                        
                        frontend.getState().add (FrontendSharedData.Condition.IS_OVERWRITE);
                        
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
                                configurator.notificationPanel (frame, layout, highlightedTerms, input);
                            }
                            
                        }.execute();
                        
                        javax.swing.Timer watcher = new javax.swing.Timer (100, e_2 ->
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) == 0)
                            {
                                decorators[0].setIcon (Worker.getDecoratorImage ("/quiz_generator/Design/124.png", 229, 86));
                                
                                ((javax.swing.Timer) e_2.getSource()).stop();
                            }
                            
                        });
                        
                        watcher.start();
                    }
                }
            }
            
        });
        
        input.addMouseListener (new MouseAdapter()
        {
            @Override
            public void mousePressed (MouseEvent e)
            {
                if (!input.isEditable()) return;
                
                String check = input.getText().trim();
                
                if (check.equals ("Your username.") && input.getForeground().equals (Color.GRAY)) input.setText("");
                
                input.setForeground (Color.BLACK);
                
                decorators[1].setVisible (false);
                notifText.setVisible (false);
            }
            
        });
        
        MouseAdapter focusLoss = new MouseAdapter()
        {
            @Override
            public void mousePressed (MouseEvent e)
            {
                String check = input.getText().trim();
                
                if (check.isEmpty())
                {
                    input.setText ("Your username.");
                    input.setForeground (Color.GRAY);
                }
                
                decorators[1].setVisible (false);
                notifText.setVisible (false);
                
                frontend.getPanel().requestFocusInWindow();
            }
            
        };
        
        frontend.getPanel().addMouseListener (focusLoss);
        
        List<JComponent> components = new ArrayList<>();
        
        components.add (label);
        components.addAll (Arrays.asList (text));
        components.add (input);
        components.addAll (Arrays.asList (decorators));
        components.add (notifText);
        components.add (hitbox);
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        return layeredPane;
    }
    
    // GETTERS
    
    JLayeredPane getPage1 (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {  
        return page1 (frame, layout, highlightedTerms);
    }
    
    JLayeredPane getPage2 (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        return page2 (frame, layout, highlightedTerms);
    }
    
    JLayeredPane getPage4 (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        return page4 (frame, layout, highlightedTerms);
    }
}