package quiz_generator.Frontend;

import quiz_generator.Backend.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MainComponent 
{
    // FOR TIMER REMOVER
    
    private javax.swing.Timer timer;
    
    // PROGRAM DATA
    
    protected BackendSharedData backend;
    protected FrontendSharedData frontend;
    
    // CONSTRUCTOR
    
    MainComponent (BackendSharedData backend, FrontendSharedData frontend)
    {
        this.backend = backend;
        this.frontend = frontend;
    }
    
    // (FRONTEND) MAIN FRAME
    
    JFrame formatFrame()
    {
        JFrame frame = new JFrame ("Reviewer Quiz Generator");
        frame.setSize (1920, 1080);
        frame.setResizable (false);
        frame.setExtendedState (JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo (null);
        frame.setLayout (null);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        
        return frame;
    }
    
    // (FRONTEND) CHANGE COMPONENTS
    
    void changeComponents (JFrame frame)
    {
        if (timer != null)
        {
            timer.stop();
            
            for (ActionListener al : timer.getActionListeners())
            {
                timer.removeActionListener (al);
            }
            
            timer = null;
        }
        
        if (frontend.getPanel() != null)
        {
            recursivelyRemoved (frontend.getPanel());
            
            frontend.getPanel().removeAll();
            
            frame.remove (frontend.getPanel());
            frame.revalidate();
            frame.repaint();
        }
    }
    
    void recursivelyRemoved (Component component)
    {
        for (MouseListener ml : component.getMouseListeners())
        {
            component.removeMouseListener (ml);
        }
        
        for (MouseMotionListener mml : component.getMouseMotionListeners())
        {
            component.removeMouseMotionListener (mml);
        }
        
        switch (component)
        {
            case JButton buttons ->
            {
                for (ActionListener al : buttons.getActionListeners())
                {
                    buttons.removeActionListener (al);
                }
            }
            case JTextField textField ->
            {
                for (ActionListener al : textField.getActionListeners())
                {
                    textField.removeActionListener (al);
                }
            }
            default -> {} // Ignore
        }
        
        if (component instanceof Container container)
        {
            for (Component child : container.getComponents())
            {
                recursivelyRemoved (child);
            }
        } 
    }
    
    // (FRONTEND) BACKGROUND
    
    Background background()
    {
        String imagePath = "/quiz_generator/Design/1.gif";
        
        switch (frontend.getNextPanel())
        {
            case MAIN_PAGE -> imagePath = "/quiz_generator/Design/1.gif";
            case LOADING_SCREEN -> imagePath = "/quiz_generator/Design/13.png";
            case PAGE_2 -> imagePath = "/quiz_generator/Design/23.png";
            case ERROR_SCREEN -> imagePath = "/quiz_generator/Design/16.png";
            case PAGE_3 -> imagePath = "/quiz_generator/Design/33.png";
            case MAIN_LOADING_SCREEN -> imagePath = "/quiz_generator/Design/44.png";
            case TEST_PAGE -> imagePath = "/quiz_generator/Design/60.png";
            case SET_PAGE_1 -> imagePath = "/quiz_generator/Design/76.png";
            case SET_PAGE_2 -> imagePath = "/quiz_generator/Design/97.png";
            case SET_PAGE_3 -> imagePath = "/quiz_generator/Design/104.png";
            case FINAL_LOADING_SCREEN -> imagePath = "/quiz_generator/Design/120.png";
            case PAGE_4 -> imagePath = "/quiz_generator/Design/122.gif";
            case IN_LEADERBOARD -> imagePath = "/quiz_generator/Design/126.png";
            case MAINTENANCE -> imagePath = "/quiz_generator/Design/maintenance.png";
        }
        
        frontend.setPanel (new Background (imagePath));
        frontend.getPanel().setLayout (null);
        frontend.getPanel().setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        return frontend.getPanel();
    } 
}