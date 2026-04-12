package quiz_generator;

import quiz_generator.Frontend.Start;

public class QuizGenerator 
{ 
    public static void main (String[] args) 
    {
        System.setProperty ("sun.java2d.uiScale", "1.0");
        
        Start start = new Start();
        start.runSystem();
    }    
}