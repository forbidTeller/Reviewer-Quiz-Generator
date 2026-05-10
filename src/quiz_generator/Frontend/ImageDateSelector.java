package quiz_generator.Frontend;

import javax.swing.text.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import java.time.*;
import java.time.format.*;

class ImageDateSelector 
{
    static String getDateSelector (JFrame frame)
    {
        JDialog dialog = new JDialog (frame, true);
        dialog.setUndecorated (true);
        dialog.setBackground (new Color (0, 0, 0, 1));
        dialog.setSize (1920, 1080);
        dialog.setLocationRelativeTo (frame);
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds (Worker.getBounds (0, 0, 1920, 1080));
        
        String[] decoratorImageName = {"/quiz_generator/Design/175.png", "/quiz_generator/Design/176.png", "/quiz_generator/Design/177.png", "/quiz_generator/Design/179.png", "/quiz_generator/Design/181.png", "/quiz_generator/Design/183.png"};
        int[] x = {899, 468};
        
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
                    decorators[i].setBounds (Worker.getBounds (441, 305, x[0], x[1]));
                    
                    x[0] = 716;
                    x[1] = 92;
                }
                case 1 ->
                {
                    decorators[i].setBounds (Worker.getBounds (532, 447, x[0], x[1]));
                    
                    x[0] = 40;
                    x[1] = 40;
                }
                case 2 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (1186, 453, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1186, 453, x[0], x[1]));
                }
                case 3 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (1165, 493, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (1165, 493, x[0], x[1]));
                    
                    x[0] = 266;
                    x[1] = 73;
                }
                case 4 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (558, 610, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (558, 610, x[0], x[1]));
                }
                case 5 ->
                {
                    hitboxes[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
                    
                    decorators[i].setBounds (Worker.getBounds (960, 610, x[0], x[1]));
                    hitboxes[i].setBounds (Worker.getBounds (960, 610, x[0], x[1]));
                }
            }
        }
        
        String[] date = {null};
        
        LocalDate[] currentDate = {LocalDate.now()};
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern ("MM-dd-yyyy");
        
        JTextField input = new JTextField (currentDate[0].format (formatter));
        input.setFont (Worker.getFont ("/quiz_generator/Font/Arimo-Regular.ttf", 1, 39.4f));
        input.setForeground (Color.BLACK);
        input.setHorizontalAlignment (SwingConstants.CENTER);
        input.setBorder (null);
        input.setOpaque (false);
        input.setBounds (Worker.getBounds (688, 462, 404, 62));
        
        AbstractDocument ad = (AbstractDocument) input.getDocument();
        ad.setDocumentFilter (new LimitFilter (10, false));
        
        Runnable syncDate = () ->
        {
            try
            {
                currentDate[0] = LocalDate.parse (input.getText().trim(), formatter);
            }
            catch (DateTimeParseException e)
            {
                input.setText (currentDate[0].format (formatter));
            }
        };
        
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/178.png", 40, 40);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/177.png", 40, 40);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            syncDate.run();
                            
                            currentDate[0] = currentDate[0].plusDays(1);
                            
                            input.setText (currentDate[0].format (formatter));
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/180.png", 40, 40);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/179.png", 40, 40);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            syncDate.run();
                            
                            currentDate[0] = currentDate[0].minusDays(1);
                            
                            input.setText (currentDate[0].format (formatter));
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/182.png", 266, 73);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/181.png", 266, 73);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            syncDate.run();
                            
                            date[0] = currentDate[0].format (formatter);
                            
                            dialog.dispose();
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
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/184.png", 266, 73);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mouseExited (MouseEvent e)
                        {
                            ImageIcon decoratorImage = Worker.getDecoratorImage ("/quiz_generator/Design/183.png", 266, 73);
                            decorators[j].setIcon (decoratorImage);
                        }
                        
                        @Override
                        public void mousePressed (MouseEvent e)
                        {
                            dialog.dispose();
                        }
                        
                    });
                }
            }
        }
        
        List<JComponent> components = new ArrayList<>();
        
        components.addAll (Arrays.asList (decorators));
        components.addAll (Arrays.asList (hitboxes));
        components.add (input);
        
        for (int i = 0; i < components.size(); i++)
        {
            layeredPane.add (components.get(i), Integer.valueOf(i));
        }
        
        dialog.add (layeredPane);
        
        decorators[0].setFocusable (true);
        
        layeredPane.setFocusable (true);
        
        MouseAdapter focusLoss = new MouseAdapter()
        {
            @Override
            public void mousePressed (MouseEvent e)
            {
                layeredPane.requestFocusInWindow();
            }
            
        };
        
        decorators[0].addMouseListener (focusLoss);
        
        layeredPane.addMouseListener (focusLoss);
        
        SwingUtilities.invokeLater (() ->
        {
            layeredPane.requestFocusInWindow();
            
        });
        
        dialog.setVisible (true);
        
        return date[0];
    }
}