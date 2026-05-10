package quiz_generator.Frontend;

import quiz_generator.Backend.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

class QuizTimer extends MainComponent
{
    private final QuizTypes quizTypes;
    private final UITransition transition;
    private final Correctors correctors;
    
    String setTimerLabel;
    int timer;
    
     // CONSTRUCTOR
    
    QuizTimer (Backend backend, Frontend frontend, QuizTypes quizTypes, UITransition transition, Correctors correctors)
    {
        super (backend, frontend);
        
        this.quizTypes = quizTypes;
        this.transition = transition;
        this.correctors = correctors;
    }
    
    void setTimerLimit (JFrame frame, CardLayout layout, List<String> highlightedTerms)
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
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/69.png", "/quiz_generator/Design/70.png", "/quiz_generator/Design/72.png", "/quiz_generator/Design/74.png"};
        int[] x = {1226, 703, 571};
        
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
                    decorators[i].setBounds (Worker.getBounds (346, 173, x[0], x[1]));
                    
                    x[0] = 576;
                    x[1] = 111;
                }
                case 1 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (x[2], 398, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (x[2], 398, x[0], x[1]));
                }
                case 2 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (x[2], 561, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (x[2], 541, x[0], x[1]));
                }
                case 3 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (x[2], 725, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (x[2], 725, x[0], x[1]));
                }
            }
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/71.png", 576, 111);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/70.png", 576, 111);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            setTimerLabel = "0:30";
                            timer = 30;
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/73.png", 576, 111);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/72.png", 576, 111);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            setTimerLabel = "1:00";
                            timer = 60;
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/75.png", 576, 111);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/74.png", 576, 111);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            setTimerLabel = "1:30";
                            timer = 90;
                        }
                        
                    });
                }
            }
        }
        
        for (int i = 1; i < 4; i++)
        {
            hitboxes[i].addMouseListener (new MouseAdapter()
            {
                @Override
                public void mousePressed (MouseEvent e)
                {
                    transition.readySetUpScreen (frame);
                    
                    new SwingWorker<Void, Void>()
                    {
                        @Override
                        protected Void doInBackground() throws Exception
                        {
                            Thread.sleep (8000);
                            
                            return null;
                        }
                        
                        @Override
                        protected void done()
                        {
                            changeComponents (frame);
                            
                            frontend.setPanel (null);
                            frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                            frontend.setPanel (background());
                            frontend.getPanel().setLayout (layout);
                            
                            String label = (backend.getIndex()[1] == 1) ? "showMultipleChoice" : (backend.getIndex()[1] == 2) ? "showIdentification" : "showTrueFalse";
                                            
                            switch (backend.getIndex()[1])
                            {
                                case 1 -> frontend.getPanel().add (quizTypes.showMultipleChoice (frame, layout, highlightedTerms), label);                  
                                case 2 -> frontend.getPanel().add (quizTypes.showIdentification (frame, layout, highlightedTerms), label);                                                                                                                                    
                                case 3 -> frontend.getPanel().add (quizTypes.showTrueFalse (frame, layout, highlightedTerms), label);                                                                                                                                                    
                            }                                           

                            layout.show (frontend.getPanel(), label);
                            
                            frame.add (frontend.getPanel());
                            frame.revalidate();
                            frame.repaint();
                        }
                        
                    }.execute();
                }
                
            });
        }
          
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.addAll (Arrays.asList (hitboxes));
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        frontend.getPanel().add (layeredPane);
        frontend.getPanel().revalidate();
        frontend.getPanel().repaint();
    }
    
    void timerLimit (JFrame frame, CardLayout layout, List<String> highlightedTerms, List<JComponent> components, boolean isTrueFalseQuiz)
    {
        ImageIcon timerImage = Worker.getDecoratorImage ("/quiz_generator/Design/78.png", 228, 256);
        
        JLabel timerLabel = new JLabel (timerImage);
        
        JLabel time = new JLabel (setTimerLabel);
        time.setFont (Worker.getFont ("/quiz_generator/Font/Alice-Regular.ttf", 1, 42.2f));
        time.setForeground (Color.WHITE);
        
        if (isTrueFalseQuiz)
        {
            timerLabel.setBounds (Worker.getBounds (534, 18, 228, 256));
            time.setBounds (Worker.getBounds (606, 128, 163, 60));
        }
        else
        {
            timerLabel.setBounds (Worker.getBounds (59, 38, 228, 256));
            time.setBounds (Worker.getBounds (132, 148, 163, 60));
        }
        
        int[] total = {timer};
        
        frontend.setClock (new javax.swing.Timer (1000, e ->
        {
            int minutes = total[0] / 60, seconds = total[0] % 60;
            
            time.setText (String.format ("%1d:%02d", minutes, seconds));
            
            total[0]--;
            
            if (total[0] < 10) time.setForeground (Color.RED);       
            
            if (total[0] < 0)
            {
                time.setText ("TIMES UP!");
                time.setFont (Worker.getFont ("/quiz_generator/Font/Alice-Regular.ttf", 1, 21.2f));
                time.setForeground (Color.WHITE);
                time.setBounds ((isTrueFalseQuiz) ? Worker.getBounds (596, 128, 163, 60) : Worker.getBounds (120, 148, 163, 60));
            }
            
            if (total[0] < -1)
            {
                frontend.getClock().stop();
                frontend.getState().add (FrontendSharedData.Condition.TIMEOUT);
                
                backend.getIndex()[2]++;
                
                if (backend.getIndex()[1] == 2 || backend.getIndex()[1] == 3) backend.setSize (backend.getSize() - 1);
                
                transition.resultCheckerScreen (frame);
                
                new SwingWorker<Void, Void>()
                {
                    @Override
                    protected Void doInBackground() throws Exception
                    {
                        Thread.sleep (3500);
                        
                        return null;
                    }
                    
                    @Override
                    protected void done()
                    {
                        changeComponents (frame);
                        
                        frontend.setPanel (null);
                        frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                        frontend.setPanel (background());
                        frontend.getPanel().setLayout (layout);
                        
                        String label = (backend.getIndex()[1] == 1) ? "correctAnswerMultipleChoice" : (backend.getIndex()[1] == 2) ? "correctAnswerIdentification" : "correctAnswerTrueFalse";
                        
                        switch (backend.getIndex()[1])
                        {
                            case 1 -> frontend.getPanel().add (correctors.correctAnswerMultipleChoice (frame, layout, highlightedTerms), label);
                            case 2 -> frontend.getPanel().add (correctors.correctAnswerIdentification (frame, layout, highlightedTerms), label);
                            case 3 -> frontend.getPanel().add (correctors.correctAnswerTrueFalse (frame, layout, highlightedTerms), label);
                        }
                        
                        layout.show (frontend.getPanel(), label);
                        
                        frame.add (frontend.getPanel());
                        frame.revalidate();
                        frame.repaint();
                    }
                    
                }.execute();
            }
            
        }));
        
        frontend.getClock().start();
        
        components.add (timerLabel);
        components.add (time);
    }
}