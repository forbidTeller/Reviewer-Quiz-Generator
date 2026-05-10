package quiz_generator.Frontend;

import java.util.*;

interface Frontend 
{
    // ENUM
    
    enum Page
    {
        MAIN_PAGE, LOADING_SCREEN, PAGE_2, 
        ERROR_SCREEN, PAGE_3, MAIN_LOADING_SCREEN, 
        SET_PAGE_1, SET_PAGE_2, SET_PAGE_3, 
        TEST_PAGE, FINAL_LOADING_SCREEN, PAGE_4, 
        IN_LEADERBOARD, SCORE_HISTORY_PAGE_1, SCORE_HISTORY_PAGE_2,
        MAINTENANCE
    }
    
    enum Condition
    {
        IS_REUSED, TIMEOUT, CORRECT, 
        QUIT_IS_CLICKED, RETRY_IS_CLICKED, EXIT_IS_CLICKED,  
        IS_OVERWRITE, TOO_LARGE, TOO_SMALL, 
        FILE_EXISTED, IS_TEXT, DELETE_IS_CLICKED
    }
    
    Background getPanel();
    javax.swing.Timer getClock();
    String getName();
    Page getNextPanel();
    EnumSet<Condition> getState();
    
    void setPanel (Background panel);
    void setClock (javax.swing.Timer clock);
    void setName (String name);
    void setNextPanel (Page nextPanel);
}