package quiz_generator.Frontend;

import quiz_generator.Backend.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

class Correctors extends MainComponent
{
    private final QuizTypes quizTypes;
    
    // CONSTRUCTOR
    
    Correctors (Backend backend, Frontend frontend, QuizTypes quizTypes)
    {
        super (backend, frontend);
        
        this.quizTypes = quizTypes;
    }
    
    JLayeredPane correctAnswerMultipleChoice (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/61.png", "/quiz_generator/Design/62.png", "/quiz_generator/Design/62.png", "/quiz_generator/Design/62.png", "/quiz_generator/Design/62.png", "/quiz_generator/Design/82.png", "/quiz_generator/Design/84.png"};
        int[] x = {1190, 452, 42, 55};
        
        int temp = backend.getIndex()[2] - 1;
        
        String[] choices = backend.getSavedChoices().get ("SLOT - 1");
        
        for (int i = 0; i < choices.length; i++)
        {
            if (choices[i].equals (backend.getAnswers().get (temp))) decoratorImageName[i + 1] = "/quiz_generator/Design/64.png";
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
                    decorators[i].setBounds (Worker.getBounds (364, 127, x[0], x[1]));
                    
                    x[0] = 481;
                    x[1] = 117;
                }
                case 1 -> decorators[i].setBounds (Worker.getBounds (455, 647, x[0], x[1]));
                case 2 -> decorators[i].setBounds (Worker.getBounds (455, 832, x[0], x[1]));
                case 3 -> decorators[i].setBounds (Worker.getBounds (987, 647, x[0], x[1]));
                case 4 ->
                {
                    decorators[i].setBounds (Worker.getBounds (987, 832, x[0], x[1]));
                    
                    x[0] = 100;
                    x[1] = 70;
                }
                case 5 ->
                {
                    decorators[i].setBounds (Worker.getBounds (1670, 122, x[0], x[1]));
                    
                    x[0] = 188;
                    x[1] = 293;
                }
                case 6 ->
                {
                    decorators[i].setBounds (Worker.getBounds (176, 597, x[0], x[1]));
                    
                    x[0] = 280;
                    x[1] = 117;
                }
            }
        }
        
        String wrappedText = "<html><div style='text-align: center;'>" + QuickProcessor.addLineBreaks (backend, backend.getQuestions().get (temp)) + "</div></html>";
        
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
            
            String rawText = choices[i];
            
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
        
        JLabel hitbox = new JLabel();
        hitbox.setCursor (new Cursor (Cursor.HAND_CURSOR));
        hitbox.setOpaque (false);
        hitbox.setBounds (Worker.getBounds (1670, 122, 100, 70));
        
        hitbox.addMouseListener (new MouseAdapter()
        {
            @Override
            public void mouseEntered (MouseEvent e)
            {
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/83.png", 100, 70);
                decorators[5].setIcon (decoratorImage);
            }
            
            @Override
            public void mouseExited (MouseEvent e)
            {
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/82.png", 100, 70);
                decorators[5].setIcon (decoratorImage);
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
                        try
                        {
                            changeComponents (frame);
                            
                            frontend.setPanel (null);
                            frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                            frontend.setPanel (background());
                            frontend.getPanel().setLayout (layout);
                            frontend.getPanel().add (quizTypes.showMultipleChoice (frame, layout, highlightedTerms), "showMultipleChoice");
                            
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
            
        });
        
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.add (question);
        components.addAll (Arrays.asList (letters));
        components.addAll (Arrays.asList (options));
        components.add (hitbox);
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        return layeredPane;
    }
    
    JLayeredPane correctAnswerIdentification (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/98.png", "/quiz_generator/Design/82.png", "/quiz_generator/Design/84.png"};
        int[] x = {1253, 419};
        
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
                    decorators[i].setBounds (Worker.getBounds (352, 208, x[0], x[1]));
                    
                    x[0] = 100;
                    x[1] = 70;
                }
                case 1 ->
                {
                    decorators[i].setBounds (Worker.getBounds (1670, 122, x[0], x[1]));
                    
                    x[0] = 188;
                    x[1] = 293;
                }
                case 2 -> decorators[i].setBounds (Worker.getBounds (579, 690, x[0], x[1]));
            }
        }
        
        int temp = backend.getIndex()[2] - 1;
        
        String wrappedText = "<html><div style='text-align: center;'>" + QuickProcessor.addLineBreaks (backend, backend.getQuestions().get (temp)) + "</div></html>";
        
        JLabel question = new JLabel (wrappedText, SwingConstants.CENTER);
        question.setFont (Worker.getFont ("/quiz_generator/Font/Alice-Regular.ttf", 1, 40.8f));
        question.setForeground (Color.WHITE);
        question.setBounds (Worker.getBounds (469, 293, 982, 245));
        
        JLabel shownAnswer = new JLabel (backend.getAnswers().get (temp), SwingConstants.CENTER);
        shownAnswer.setFont (Worker.getFont ("/quiz_generator/Font/Alice-Regular.ttf", 1, 40.8f));
        shownAnswer.setForeground (Color.GREEN);
        shownAnswer.setBounds (Worker.getBounds (480, 590, 982, 245));
        
        JLabel hitbox = new JLabel();
        hitbox.setCursor (new Cursor (Cursor.HAND_CURSOR));
        hitbox.setOpaque (false);
        hitbox.setBounds (Worker.getBounds (1670, 122, 100, 70));
        
        hitbox.addMouseListener (new MouseAdapter()
        {
            @Override
            public void mouseEntered (MouseEvent e)
            {
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/83.png", 100, 70);
                decorators[1].setIcon (decoratorImage);
            }
            
            @Override
            public void mouseExited (MouseEvent e)
            {
                ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/82.png", 100, 70);
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
                        try
                        {
                            changeComponents (frame);
                            
                            frontend.setPanel (null);
                            frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                            frontend.setPanel (background());
                            frontend.getPanel().setLayout (layout);
                            frontend.getPanel().add (quizTypes.showIdentification (frame, layout, highlightedTerms), "showIdentification");
                            
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
            
        });
        
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.add (question);
        components.add (shownAnswer);
        components.add (hitbox);
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        return layeredPane;
    }
    
    JLayeredPane correctAnswerTrueFalse (JFrame frame, CardLayout layout, List<String> highlightedTerms)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/105.png", "/quiz_generator/Design/106.png", "/quiz_generator/Design/110.png", "/quiz_generator/Design/114.png", "/quiz_generator/Design/82.png", "/quiz_generator/Design/84.png", "/quiz_generator/Design/118.png"};
        int[] x = {1129, 649};
        
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
                    decorators[i].setBounds (Worker.getBounds (1298, 434, x[0], x[1]));
                    
                    x[0] = 519;
                    x[1] = 153;
                }
                case 2 ->
                {
                    decorators[i].setBounds (Worker.getBounds (1293, 645, x[0], x[1]));
                    
                    x[0] = 155;
                    x[1] = 74;
                }
                case 3 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (994, 360, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (994, 360, x[0], x[1]));
                    
                    x[0] = 100;
                    x[1] = 70;
                }
                case 4 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (1670, 122, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1670, 122, x[0], x[1]));
                    
                    x[0] = 188;
                    x[1] = 293;
                }
                case 5 ->
                {
                    decorators[i].setBounds (Worker.getBounds (554, 24, x[0], x[1]));
                    
                    x[0] = 423;
                    x[1] = 67;
                }
                case 6 ->
                {
                    decorators[i].setBounds (Worker.getBounds (1149, 297, x[0], x[1]));
                    
                    decorators[i].setVisible (false);
                }
            }
        }
        
        int temp = backend.getIndex()[2] - 1;
        
        String[] correctArray = backend.getSavedChoices().get ("SLOT - 1");
        String[] randomizedArray = backend.getSavedChoices().get ("SLOT - 2");
        
        String wrappedText = "<html><div style='text-align: center;'>" + QuickProcessor.addLineBreaks (backend, randomizedArray[temp]) + "</div></html>";
        
        JLabel statement = new JLabel (wrappedText, SwingConstants.CENTER);
        statement.setFont (Worker.getFont ("/quiz_generator/Font/Alice-Regular.ttf", 1, 48.0f));
        statement.setForeground (Color.WHITE);
        statement.setBounds (Worker.getBounds (165, 463, 988, 388));
        
        boolean setCorrect = false;
        
        for (String correct : correctArray)
        {
            if (randomizedArray[temp].equals (correct))
            {
                setCorrect = true;
                
                break;
            }
        }
        
        boolean[] toggle = {false};
        
        for (int i = 0; i < decoratorImageName.length; i++)
        {
            final int j = i;
            
            switch(i)
            {
                case 1 -> decorators[i].setIcon ((setCorrect) ? Worker.getDecoratorImage ("/quiz_generator/Design/108.png", 502, 141) : Worker.getDecoratorImage ("/quiz_generator/Design/106.png", 502, 141));
                case 2 -> decorators[i].setIcon ((!setCorrect) ? Worker.getDecoratorImage ("/quiz_generator/Design/112.png", 519, 153) : Worker.getDecoratorImage ("/quiz_generator/Design/110.png", 519, 153));
                case 3 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon toggleOff = Worker.getDecoratorImage ("/quiz_generator/Design/115.png", 155, 74);
                            ImageIcon toggleOn = Worker.getDecoratorImage ("/quiz_generator/Design/117.png", 155, 74);
                            
                            decorators[j].setIcon ((toggle[0]) ? toggleOn : toggleOff);
                            
                            decorators[6].setVisible (true);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon toggleOff = Worker.getDecoratorImage ("/quiz_generator/Design/114.png", 155, 74);
                            ImageIcon toggleOn = Worker.getDecoratorImage ("/quiz_generator/Design/116.png", 155, 74);
                            
                            decorators[j].setIcon ((toggle[0]) ? toggleOn : toggleOff);
                            
                            decorators[6].setVisible (false);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            toggle[0] = !toggle[0];
                                                    
                            if (toggle[0])
                            {
                                ImageIcon toggleOn = Worker.getDecoratorImage ("/quiz_generator/Design/117.png", 155, 74);
                                decorators[j].setIcon (toggleOn);
                                
                                String text = "", target = "", detect = randomizedArray[temp];
                                
                                for (int i = 0; i < correctArray.length; i++)
                                {
                                    if (detect.contains(" ") && correctArray[i].contains(" "))
                                    {
                                        String[] detectLast = {detect.substring (detect.indexOf(" ") + 1), correctArray[i].substring (correctArray[i].indexOf(" ") + 1)};
                                        String[] detectFirst = {detect.substring (0, detect.lastIndexOf(" ")), correctArray[i].substring (0, correctArray[i].lastIndexOf(" "))};
                                        
                                        if (detectLast[0].equals (detectLast[1]) || detectFirst[0].equals (detectFirst[1]))
                                        {
                                            text = correctArray[i];
                                            target = backend.getAnswers().get(i);
                                            
                                            break;
                                        }
                                    }
                                }
                                
                                String trimText = target.trim(), textBreaks = QuickProcessor.addLineBreaks (backend, text), formattedText = textBreaks.replaceAll ("(?i)" + trimText, "<font color='#32CD32'>$0</font>");
                                String wrappedText = "<html><div style='text-align: center;'>" + formattedText + "</div></html>";        
                                
                                statement.setText (wrappedText);
                                statement.setHorizontalAlignment (SwingConstants.CENTER);
                            }
                            else
                            {
                                ImageIcon toggleOff = Worker.getDecoratorImage ("/quiz_generator/Design/115.png", 155, 74);
                                decorators[j].setIcon (toggleOff);
                                
                                String wrappedText = "<html><div style='text-align: center;'>" + QuickProcessor.addLineBreaks (backend, randomizedArray[temp]) + "</div></html>";
                                
                                statement.setText (wrappedText);
                                statement.setHorizontalAlignment (SwingConstants.CENTER);
                            }
                        }
                        
                    });
                }
                case 4 ->
                {
                    hitboxes[j].addMouseListener (new MouseAdapter()
                    {
                        @Override
                        public void mouseEntered (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/83.png", 100, 70);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/82.png", 100, 70);
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
                                    try
                                    {
                                        changeComponents (frame);
                                        
                                        frontend.setPanel (null);
                                        frontend.setNextPanel (FrontendSharedData.Page.TEST_PAGE);
                                        frontend.setPanel (background());
                                        frontend.getPanel().setLayout (layout);
                                        frontend.getPanel().add (quizTypes.showTrueFalse (frame, layout, highlightedTerms), "showTrueFalse");
                                        
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
                        
                    });
                }
            }
        }
        
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.addAll (Arrays.asList (hitboxes));
        components.add (statement);
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        return layeredPane;
    }
}