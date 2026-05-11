package quiz_generator.Frontend;

import quiz_generator.Backend.*;

import javax.swing.text.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

class QuizTypes extends MainComponent
{
    private QuizTimer quizTimer;
    private final UITransition transition;
    private Correctors correctors;
    private final QuizConfigurator configurator;
    private final Start start;
    
    // CONSTRUCTOR
    
    QuizTypes (Backend backend, Frontend frontend, UITransition transition, QuizConfigurator configurator, Start start)
    {
        super (backend, frontend);
        
        this.transition = transition;
        this.configurator = configurator;
        this.start = start;
    }
    
    // A SETTER FOR OTHER OBJECT. THIS AVOIDS ERROR WHEN OBJECT IS NOT EXIST YET
    
    void setQuizTimer (QuizTimer quizTimer) { this.quizTimer = quizTimer; }
    void setCorrectors (Correctors correctors) { this.correctors = correctors; }
    
    JLayeredPane showMultipleChoice (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {               
        if (backend.getIndex()[2] == backend.getQuestions().size()) backend.multipleChoiceReset (highlightedTerms);
        
        if (backend.getSize() == 0)
        {
            transition.finalLoadingScreen (frame);
            
            new SwingWorker<Void, Void>()
            {
                @Override
                protected Void doInBackground() throws Exception
                {
                    Thread.sleep (15000);
                    
                    return null;
                }
                
                @Override
                protected void done()
                {
                    changeComponents (frame);
                    
                    frontend.setPanel (null);
                    frontend.setNextPanel (FrontendSharedData.Page.PAGE_4);
                    frontend.setPanel (background());
                    frontend.getPanel().setLayout (layout);
                    frontend.getPanel().add (start.getPage4 (frame, layout, highlightedTerms), "page4");
                    
                    layout.show (frontend.getPanel(), "page4");
                    
                    frame.add (frontend.getPanel());
                    frame.revalidate();
                    frame.repaint();
                }
                
            }.execute();
        }
        
        if (frontend.getState().contains (FrontendSharedData.Condition.IS_REUSED))
        {
            try
            {
                backend.setChoices (null);
                backend.getSavedChoices().clear();
                backend.setChoices (QuickProcessor.triggeredChoice (backend, highlightedTerms));
                backend.getSavedChoices().put ("SLOT - 1", backend.getChoices());
            }
            catch (Exception e)
            {
                // Ignore
            }
        }
        
        if (!frontend.getState().contains (FrontendSharedData.Condition.IS_REUSED)) frontend.getState().add (FrontendSharedData.Condition.IS_REUSED);
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/61.png", "/quiz_generator/Design/62.png", "/quiz_generator/Design/62.png", "/quiz_generator/Design/62.png", "/quiz_generator/Design/62.png", "/quiz_generator/Design/66.png"};
        int[] x = {1190, 452, 42, 55};
        
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
                    decorators[i].setBounds (Worker.getBounds (364, 127, x[0], x[1]));
                    
                    x[0] = 481;
                    x[1] = 117;
                }
                case 1 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (455, 647, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (455, 647, x[0], x[1]));
                }
                case 2 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (455, 832, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (455, 832, x[0], x[1]));
                }
                case 3 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (987, 647, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (987, 647, x[0], x[1]));
                }
                case 4 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (987, 832, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (987, 832, x[0], x[1]));
                    
                    x[0] = 100;
                    x[1] = 100;
                }
                case 5 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (1669, 108, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1669, 108, x[0], x[1]));
                    
                    x[0] = 280;
                    x[1] = 117;
                }
            }
        }
        
        String wrappedText = "<html><div style='text-align: center;'>" + QuickProcessor.addLineBreaks (backend, backend.getQuestions().get (backend.getIndex()[2])) + "</div></html>";
                
        JLabel question = new JLabel (wrappedText, SwingConstants.CENTER);
        question.setFont (Worker.getFont ("/quiz_generator/Font/Alice-Regular.ttf", 1, 40.8f));
        question.setForeground (Color.WHITE);       
        question.setBounds (Worker.getBounds (469, 233, 982, 245));
        
        String[] stringLetters = {"A.", "B.", "C.", "D."};
        
        JLabel[] letters = new JLabel[stringLetters.length];
        JLabel[] options = new JLabel[stringLetters.length];
        
        for (int i = 0; i < stringLetters.length; i++)
        {
            letters[i] = new JLabel (stringLetters[i]);
            letters[i].setFont (Worker.getFont ("/quiz_generator/Font/Alice-Regular.ttf", 1, 35.0f));
            letters[i].setForeground (Color.BLACK);
            
            String rawText = backend.getChoices()[i];
               
            options[i] = new JLabel();
            options[i].setVerticalAlignment (SwingConstants.CENTER);
            options[i].setForeground (Color.WHITE);
            
            Worker.autoScaleLabel ("/quiz_generator/Font/Alice-Regular.ttf", options[i], rawText, 35.0f, x[0], x[1]);
                       
            switch(i)
            {
                case 0 ->
                {
                    letters[i].setBounds (Worker.getBounds (547, 675, x[2], x[3]));
                    options[i].setBounds (Worker.getBounds (623, 647, x[0], x[1]));
                }
                case 1 ->
                {
                    letters[i].setBounds (Worker.getBounds (547, 861, x[2], x[3]));
                    options[i].setBounds (Worker.getBounds (623, 832, x[0], x[1]));
                }
                case 2 ->
                {
                    letters[i].setBounds (Worker.getBounds (1078, 675, x[2], x[3]));
                    options[i].setBounds (Worker.getBounds (1154, 647, x[0], x[1]));
                }
                case 3 ->
                {
                    letters[i].setBounds (Worker.getBounds (1078, 861, x[2], x[3]));
                    options[i].setBounds (Worker.getBounds (1154, 832, x[0], x[1]));
                }
            }
        }
        
        final boolean[] mouseLock = {false};
        
        for (int i = 0; i < decoratorImageName.length; i++)
        {
            final int j = i;
            
            switch(i)
            {
                case 1, 2, 3, 4 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {  
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/63.png", 481, 117);
                            decorators[j].setIcon (decoratorImage);
                        }
                
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/62.png", 481, 117);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            mouseLock[0] = true;
                            
                            frontend.getClock().stop();
                            
                            String check = backend.getChoices()[j - 1];
                            
                            if (check.equals (backend.getAnswers().get (backend.getIndex()[2])))
                            {
                                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/64.png", 481, 117);
                                decorators[j].setIcon (decoratorImage);
                                
                                frontend.getState().add (FrontendSharedData.Condition.CORRECT);
                                        
                                backend.getIndex()[2]++;
                                        
                                transition.resultCheckerScreen (frame);
                                
                                new SwingWorker<Void, Void>()
                                {
                                    @Override
                                    protected Void doInBackground() throws Exception
                                    {
                                        Thread.sleep (4000);
                                        
                                        return null;
                                    }
                                    
                                    @Override
                                    protected void done()
                                    {
                                        try
                                        {
                                            changeComponents (frame);
                                            
                                            frontend.setPanel (null);
                                            frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                                            frontend.setPanel (background());
                                            frontend.getPanel().setLayout (layout);
                                            frontend.getPanel().add (showMultipleChoice (frame, layout, highlightedTerms), "showMultipleChoice");
                                            
                                            layout.show (frontend.getPanel(), "showMultipleChoice");
                                            
                                            frame.add (frontend.getPanel());
                                            frame.revalidate();
                                            frame.repaint();
                                        }
                                        catch (Exception e)
                                        {
                                            // Ignore
                                        }
                                    }
                                    
                                }.execute();
                            }
                            else
                            {
                                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/65.png", 481, 117);
                                decorators[j].setIcon (decoratorImage);
                                                             
                                transition.resultCheckerScreen (frame);
                                
                                new SwingWorker<Void, Void>()
                                {
                                    @Override
                                    protected Void doInBackground() throws Exception
                                    {
                                        Thread.sleep (4000);
                                        
                                        return null;
                                    }
                                    
                                    @Override
                                    protected void done()
                                    {
                                        backend.getIndex()[2]++;
                                        
                                        changeComponents (frame);
                                        
                                        frontend.setPanel (null);
                                        frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                                        frontend.setPanel (background());
                                        frontend.getPanel().setLayout (layout);
                                        frontend.getPanel().add (correctors.correctAnswerMultipleChoice (frame, layout, highlightedTerms), "correctAnswerMultipleChoice");
                                                                            
                                        layout.show (frontend.getPanel(), "correctAnswerMultipleChoice");
                                        
                                        frame.add (frontend.getPanel());
                                        frame.revalidate();
                                        frame.repaint();
                                    }
                                    
                                }.execute();
                            }
                        }
                
                    });
                }
                case 5 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/67.png", 100, 100);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/66.png", 100, 100);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/68.png", 100, 100);
                            decorators[j].setIcon (decoratorImage);
                            
                            frontend.getClock().stop();
                            
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
                                    configurator.pausePanel (frame, layout, highlightedTerms);
                                }
                                
                            }.execute();
                            
                            javax.swing.Timer watcher = new javax.swing.Timer (100, e_2 ->
                            {
                                if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) == 0)
                                {
                                    decorators[j].setIcon (Worker.getDecoratorImage ("/quiz_generator/Design/66.png", 100, 100));
                                    
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
        components.add (question);
        components.addAll (Arrays.asList (letters));
        components.addAll (Arrays.asList (options));
        components.addAll (Arrays.asList (hitboxes));
        
        quizTimer.timerLimit (frame, layout, highlightedTerms, components, false);
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        return layeredPane;
    }
    
    JLayeredPane showIdentification (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        if (backend.getIndex()[2] == backend.getQuestions().size()) backend.identificationReset (highlightedTerms);
        
        if (backend.getSize() == 0)
        {
            transition.finalLoadingScreen (frame);
            
            new SwingWorker<Void, Void>()
            {
                @Override
                protected Void doInBackground() throws Exception
                {
                    Thread.sleep (15000);
                    
                    return null;
                }
                
                @Override
                protected void done()
                {
                    changeComponents (frame);
                    
                    frontend.setPanel (null);
                    frontend.setNextPanel (FrontendSharedData.Page.PAGE_4);
                    frontend.setPanel (background());
                    frontend.getPanel().setLayout (layout);
                    frontend.getPanel().add (start.getPage4 (frame, layout, highlightedTerms), "page4");
                    
                    layout.show (frontend.getPanel(), "page4");
                    
                    frame.add (frontend.getPanel());
                    frame.revalidate();
                    frame.repaint();
                }
                
            }.execute();
        }
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/98.png", "/quiz_generator/Design/99.png", "/quiz_generator/Design/101.png", "/quiz_generator/Design/66.png"};
        int[] x = {1253, 419};
        
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
                    decorators[i].setBounds (Worker.getBounds (352, 208, x[0], x[1]));
                    
                    x[0] = 787;
                    x[1] = 144;
                }
                case 1 ->
                {
                    decorators[i].setBounds (Worker.getBounds (522, 820, x[0], x[1]));
                    
                    x[0] = 123;
                    x[1] = 130;
                }
                case 2 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (1362, 827, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1362, 827, x[0], x[1]));
                    
                    x[0] = 100;
                    x[1] = 100;
                }
                case 3 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (1669, 108, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1669, 108, x[0], x[1]));
                }
            }
        }
        
        String wrappedText = "<html><div style='text-align: center;'>" + QuickProcessor.addLineBreaks (backend, backend.getQuestions().get (backend.getIndex()[2])) + "</div></html>";
        
        JLabel question = new JLabel (wrappedText, SwingConstants.CENTER);
        question.setFont (Worker.getFont ("/quiz_generator/Font/Alice-Regular.ttf", 1, 40.8f));
        question.setForeground (Color.WHITE);
        question.setBounds (Worker.getBounds (469, 293, 982, 245));
        
        JLabel blankAnswer = new JLabel (backend.getChoices()[backend.getIndex()[2]], SwingConstants.CENTER);
        blankAnswer.setFont (Worker.getFont ("/quiz_generator/Font/Alice-Regular.ttf", 1, 40.8f));
        blankAnswer.setForeground (Color.WHITE);
        blankAnswer.setBounds (Worker.getBounds (480, 590, 982, 245));
        
        JTextField input = new JTextField();
        input.setFont (Worker.getFont ("/quiz_generator/Font/Alice-Regular.ttf", 1, 39.8f));
        input.setText ("Your answer here...");
        input.setForeground (Color.GRAY);
        input.setBorder (null);
        input.setOpaque (false);
        input.setBounds (Worker.getBounds (687, 861, 598, 63));
        
        AbstractDocument ad = (AbstractDocument) input.getDocument();
        ad.setDocumentFilter (new LimitFilter (25, false));
        
        JLabel typingIcon = new JLabel (Worker.getGif ("/quiz_generator/Design/103.gif", 113, 23));
        typingIcon.setBounds (Worker.getBounds (538, 881, 113, 23));
        
        typingIcon.setVisible (false);
        
        final boolean[] mouseLock = {false};
                 
        for (int i = 0; i < decoratorImageName.length; i++)
        {
            final int j = i;
            
            switch(i)
            {
                case 2 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/102.png", 123, 130);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/101.png", 123, 130);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            String check = input.getText().toUpperCase().trim();
                            
                            if ((check.equalsIgnoreCase ("Your answer here...") && input.getForeground().equals (Color.GRAY)) || check.isEmpty())
                            {
                                // Ignore
                            }
                            else if (check.equals (backend.getAnswers().get (backend.getIndex()[2])))
                            {
                                mouseLock[0] = true;
                                
                                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/99.png", 787, 144);
                                decorators[1].setIcon (decoratorImage);
                                
                                input.setForeground (Color.GREEN);
                                input.setEditable (false);
                                input.setFocusable (false);
                                
                                typingIcon.setVisible (false);
                                
                                frontend.getClock().stop();
                                frontend.getState().add (FrontendSharedData.Condition.CORRECT);
                                
                                backend.getIndex()[2]++;
                                backend.setSize (backend.getSize() - 1);
                                
                                transition.resultCheckerScreen (frame);
                                
                                new SwingWorker<Void, Void>()
                                {
                                    @Override
                                    protected Void doInBackground() throws Exception
                                    {
                                        Thread.sleep (4000);
                                        
                                        return null;
                                    }
                                    
                                    @Override
                                    protected void done()
                                    {
                                        try
                                        {
                                            changeComponents (frame);
                                            
                                            frontend.setPanel (null);
                                            frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                                            frontend.setPanel (background());
                                            frontend.getPanel().setLayout (layout);
                                            frontend.getPanel().add (showIdentification (frame, layout, highlightedTerms), "showIdentification");
                                            
                                            layout.show (frontend.getPanel(), "showIdentification");
                                            
                                            frame.add (frontend.getPanel());
                                            frame.revalidate();
                                            frame.repaint();
                                        }
                                        catch (Exception e)
                                        {
                                            // Ignore
                                        }
                                    }
                                    
                                }.execute();
                            }
                            else
                            {
                                mouseLock[0] = true;
                                
                                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/99.png", 787, 144);
                                decorators[1].setIcon (decoratorImage);
                                
                                input.setForeground (Color.RED);
                                input.setEditable (false);
                                input.setFocusable (false);
                                
                                typingIcon.setVisible (false);
                                
                                frontend.getClock().stop();
                                                                                            
                                transition.resultCheckerScreen (frame);
                                
                                new SwingWorker<Void, Void>()
                                {
                                    @Override
                                    protected Void doInBackground() throws Exception
                                    {
                                        Thread.sleep (4000);
                                        
                                        return null;
                                    }
                                    
                                    @Override
                                    protected void done()
                                    {
                                        backend.getIndex()[2]++;
                                        backend.setSize (backend.getSize() - 1);
                                         
                                        changeComponents (frame);
                                        
                                        frontend.setPanel (null);
                                        frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                                        frontend.setPanel (background());
                                        frontend.getPanel().setLayout (layout);
                                        frontend.getPanel().add (correctors.correctAnswerIdentification (frame, layout, highlightedTerms), "correctAnswerIdentification");
                                                                                       
                                        layout.show (frontend.getPanel(), "correctAnswerIdentification");
                                        
                                        frame.add (frontend.getPanel());
                                        frame.revalidate();
                                        frame.repaint();
                                    }
                                    
                                }.execute();
                            }
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
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/67.png", 100, 100);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/66.png", 100, 100);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/68.png", 100, 100);
                            decorators[j].setIcon (decoratorImage);
                            
                            frontend.getClock().stop();
                            
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
                                    configurator.pausePanel (frame, layout, highlightedTerms);
                                }
                                
                            }.execute();
                            
                            javax.swing.Timer watcher = new javax.swing.Timer (100, e_2 ->
                            {
                                if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) == 0)
                                {
                                    decorators[j].setIcon (Worker.getDecoratorImage ("/quiz_generator/Design/66.png", 100, 100));
                                    
                                    ((javax.swing.Timer) e_2.getSource()).stop();
                                }
                                
                            });
                            
                            watcher.start();
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
                if (!input.isEditable()) return;
                
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/100.png", 787, 144);
                decorators[1].setIcon (decoratorImage);
                
                String check = input.getText().trim();
                
                if (check.equals ("Your answer here...") && input.getForeground().equals (Color.GRAY)) input.setText("");
                
                input.setForeground (Color.WHITE);
                
                typingIcon.setVisible (true);
            }
            
        });
        
        MouseAdapter focusLoss = new MouseAdapter()
        {
            @Override
            public void mousePressed (MouseEvent e)
            {
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/99.png", 787, 144);
                decorators[1].setIcon (decoratorImage);
                
                String check = input.getText().trim();
                
                if (check.isEmpty())
                {
                    input.setText ("Your answer here...");
                    input.setForeground (Color.GRAY);
                }
                
                typingIcon.setVisible (false);
                
                frontend.getPanel().requestFocusInWindow();
            }
            
        };
        
        hitboxes[3].addMouseListener (focusLoss);
        
        frontend.getPanel().addMouseListener (focusLoss);
        
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.addAll (Arrays.asList (hitboxes));
        components.add (question);
        components.add (blankAnswer);
        components.add (input);
        components.add (typingIcon);
        
        quizTimer.timerLimit (frame, layout, highlightedTerms, components, false);
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        return layeredPane;
    }
    
    JLayeredPane showTrueFalse (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        if (backend.getIndex()[2] == backend.getQuestions().size()) backend.trueFalseReset (highlightedTerms);
        
        if (backend.getSize() == 0)
        {
            transition.finalLoadingScreen (frame);
            
            new SwingWorker<Void, Void>()
            {
                @Override
                protected Void doInBackground() throws Exception
                {
                    Thread.sleep (15000);
                    
                    return null;
                }
                
                @Override
                protected void done()
                {
                    changeComponents (frame);
                    
                    frontend.setPanel (null);
                    frontend.setNextPanel (FrontendSharedData.Page.PAGE_4);
                    frontend.setPanel (background());
                    frontend.getPanel().setLayout (layout);
                    frontend.getPanel().add (start.getPage4 (frame, layout, highlightedTerms), "page4");
                    
                    layout.show (frontend.getPanel(), "page4");
                    
                    frame.add (frontend.getPanel());
                    frame.revalidate();
                    frame.repaint();
                }
                
            }.execute();
        }
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        if (backend.isUsedOnce())
        {
            backend.setExtractStored (QuizTypeProcessor.extractTrueFalse (backend));
            backend.setUsedOnce (false);
        }
        
        String[] correctArray = backend.getExtractStored().get ("correctArray"), randomizedArray = backend.getExtractStored().get ("randomizedArray");
        
        backend.getSavedChoices().put ("SLOT - 1", correctArray);
        backend.getSavedChoices().put ("SLOT - 2", randomizedArray);
        
        String[] decoratorImageName = {"/quiz_generator/Design/105.png", "/quiz_generator/Design/106.png", "/quiz_generator/Design/110.png", "/quiz_generator/Design/66.png"};
        int[] x = {1129, 641};
        
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
                    decorators[i].setBounds (Worker.getBounds (84, 330, x[0], x[1]));
                    
                    x[0] = 502;
                    x[1] = 141;
                }
                case 1 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (1298, 434, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1298, 434, x[0], x[1]));
                    
                    x[0] = 519;
                    x[1] = 153;
                }
                case 2 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (1293, 645, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1293, 645, x[0], x[1]));
                    
                    x[0] = 100;
                    x[1] = 100;
                }
                case 3 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (1669, 108, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1669, 108, x[0], x[1]));
                }
            }
        }
        
        String wrappedText = "<html><div style='text-align: center;'>" + QuickProcessor.addLineBreaks (backend, randomizedArray[backend.getIndex()[2]]) + "</div></html>";
        
        JLabel statement = new JLabel (wrappedText, SwingConstants.CENTER);
        statement.setFont (Worker.getFont ("/quiz_generator/Font/Alice-Regular.ttf", 1, 48.0f));
        statement.setForeground (Color.WHITE);
        statement.setBounds (Worker.getBounds (154, 470, 988, 388));
        
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
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/107.png", 502, 141);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/106.png", 502, 141);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            mouseLock[0] = true;
                            
                            frontend.getClock().stop();
                            
                            boolean setCorrect = false;
                            
                            for (String correct : correctArray)
                            {
                                if (randomizedArray[backend.getIndex()[2]].equals (correct))
                                {
                                    setCorrect = true;
                                    
                                    break;
                                }
                            }
                            
                            if (setCorrect)
                            {
                                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/108.png", 502, 141);
                                decorators[j].setIcon (decoratorImage);
                                        
                                frontend.getState().add (FrontendSharedData.Condition.CORRECT);
                                
                                backend.getIndex()[2]++;
                                backend.setSize (backend.getSize() - 1);
                                
                                transition.resultCheckerScreen (frame);
                                
                                new SwingWorker<Void, Void>()
                                {
                                    @Override
                                    protected Void doInBackground() throws Exception
                                    {
                                        Thread.sleep (4000);
                                        
                                        return null;
                                    }
                                    
                                    @Override
                                    protected void done()
                                    {
                                        try
                                        {
                                            changeComponents (frame);
                                            
                                            frontend.setPanel (null);
                                            frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                                            frontend.setPanel (background());
                                            frontend.getPanel().setLayout (layout);
                                            frontend.getPanel().add (showTrueFalse (frame, layout, highlightedTerms), "showTrueFalse");
                                            
                                            layout.show (frontend.getPanel(), "showTrueFalse");
                                            
                                            frame.add (frontend.getPanel());
                                            frame.revalidate();
                                            frame.repaint();
                                        }
                                        catch (Exception e)
                                        {
                                            // Ignore
                                        }
                                    }
                                    
                                }.execute();
                            }
                            else
                            {
                                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/109.png", 502, 141);
                                decorators[j].setIcon (decoratorImage);
                                                                                   
                                transition.resultCheckerScreen (frame);
                                
                                new SwingWorker<Void, Void>()
                                {
                                    @Override
                                    protected Void doInBackground() throws Exception
                                    {
                                        Thread.sleep (4000);
                                        
                                        return null;
                                    }
                                    
                                    @Override
                                    protected void done()
                                    {
                                        backend.getIndex()[2]++;
                                        backend.setSize (backend.getSize() - 1);
                                        
                                        changeComponents (frame);
                                        
                                        frontend.setPanel (null);
                                        frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                                        frontend.setPanel (background());
                                        frontend.getPanel().setLayout (layout);
                                        frontend.getPanel().add (correctors.correctAnswerTrueFalse (frame, layout, highlightedTerms), "correctAnswerTrueFalse");
                                                                               
                                        layout.show (frontend.getPanel(), "correctAnswerTrueFalse");
                                        
                                        frame.add (frontend.getPanel());
                                        frame.revalidate();
                                        frame.repaint();
                                    }
                                    
                                }.execute();
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
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/111.png", 519, 153);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/110.png", 519, 153);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            if (mouseLock[0]) return;
                            
                            mouseLock[0] = true;
                            
                            frontend.getClock().stop();
                            
                            boolean setCorrect = false;
                            
                            for (String correct : correctArray)
                            {
                                if (randomizedArray[backend.getIndex()[2]].equals (correct))
                                {
                                    setCorrect = true;
                                    
                                    break;
                                }
                            }
                            
                            if (setCorrect)
                            {
                                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/113.png", 519, 153);
                                decorators[j].setIcon (decoratorImage);
                                                             
                                transition.resultCheckerScreen (frame);
                                
                                new SwingWorker<Void, Void>()
                                {
                                    @Override
                                    protected Void doInBackground() throws Exception
                                    {
                                        Thread.sleep (4000);
                                        
                                        return null;
                                    }
                                    
                                    @Override
                                    protected void done()
                                    {
                                        backend.getIndex()[2]++;
                                        backend.setSize (backend.getSize() - 1);
                                        
                                        changeComponents (frame);
                                        
                                        frontend.setPanel (null);
                                        frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                                        frontend.setPanel (background());
                                        frontend.getPanel().setLayout (layout);
                                        frontend.getPanel().add (correctors.correctAnswerTrueFalse (frame, layout, highlightedTerms), "correctAnswerTrueFalse");
                                                                                                            
                                        layout.show (frontend.getPanel(), "correctAnswerTrueFalse");
                                        
                                        frame.add (frontend.getPanel());
                                        frame.revalidate();
                                        frame.repaint();
                                    }
                                    
                                }.execute();
                            }
                            else
                            {
                                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/112.png", 519, 153);
                                decorators[j].setIcon (decoratorImage);
                                
                                frontend.getState().add (FrontendSharedData.Condition.CORRECT);
                                
                                backend.getIndex()[2]++;
                                backend.setSize (backend.getSize() - 1);
                                
                                transition.resultCheckerScreen (frame);
                                
                                new SwingWorker<Void, Void>()
                                {
                                    @Override
                                    protected Void doInBackground() throws Exception
                                    {
                                        Thread.sleep (4000);
                                        
                                        return null;
                                    }
                                    
                                    @Override
                                    protected void done()
                                    {
                                        try
                                        {
                                            changeComponents (frame);
                                            
                                            frontend.setPanel (null);
                                            frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                                            frontend.setPanel (background());
                                            frontend.getPanel().setLayout (layout);
                                            frontend.getPanel().add (showTrueFalse (frame, layout, highlightedTerms), "showTrueFalse");
                                            
                                            layout.show (frontend.getPanel(), "showTrueFalse");
                                            
                                            frame.add (frontend.getPanel());
                                            frame.revalidate();
                                            frame.repaint();
                                        }
                                        catch (Exception e)
                                        {
                                            // Ignore
                                        }
                                    }
                                    
                                }.execute();
                            }
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
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/67.png", 100, 100);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/66.png", 100, 100);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) > 0) return;
                            
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/68.png", 100, 100);
                            decorators[j].setIcon (decoratorImage);
                            
                            frontend.getClock().stop();
                            
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
                                    configurator.pausePanel (frame, layout, highlightedTerms);
                                }
                                
                            }.execute();
                            
                            javax.swing.Timer watcher = new javax.swing.Timer (100, e_2 ->
                            {
                                if (frame.getLayeredPane().getComponentCountInLayer (JLayeredPane.POPUP_LAYER) == 0)
                                {
                                    decorators[j].setIcon (Worker.getDecoratorImage ("/quiz_generator/Design/66.png", 100, 100));
                                    
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
        components.addAll (Arrays.asList (hitboxes));
        components.add (statement);
        
        quizTimer.timerLimit (frame, layout, highlightedTerms, components, true);
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        return layeredPane;
    }
}