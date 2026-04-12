package quiz_generator.Frontend;

import java.util.*;

class FrontendSharedData 
{
    private Background panel;
    private javax.swing.Timer clock;
    private String name;
     
    // ENUM
    
    enum Page
    {
        MAIN_PAGE, LOADING_SCREEN, PAGE_2, 
        ERROR_SCREEN, PAGE_3, MAIN_LOADING_SCREEN, 
        SET_PAGE_1, SET_PAGE_2, SET_PAGE_3, 
        TEST_PAGE, FINAL_LOADING_SCREEN, PAGE_4, 
        IN_LEADERBOARD, MAINTENANCE
    }
    
    private Page nextPanel = Page.MAIN_PAGE;
    
    enum Condition
    {
        IS_REUSED, TIMEOUT, CORRECT, 
        QUIT_IS_CLICKED, RETRY_IS_CLICKED, EXIT_IS_CLICKED,  
        IS_OVERWRITE, TOO_LARGE, TOO_SMALL, 
        FILE_EXISTED, IS_TEXT
    }
     
    final EnumSet<Condition> state = EnumSet.noneOf (Condition.class);
    
    // GETTERS
    
    Background getPanel() { return panel; }
    javax.swing.Timer getClock() { return clock; }
    String getName() { return name; }
    Page getNextPanel() { return nextPanel; }
    
    // SETTERS
    
    void setPanel (Background panel) { this.panel = panel; }
    void setClock (javax.swing.Timer clock) { this.clock = clock; }
    void setName (String name) { this.name = name; }
    void setNextPanel (Page nextPanel) { this.nextPanel = nextPanel; }
}