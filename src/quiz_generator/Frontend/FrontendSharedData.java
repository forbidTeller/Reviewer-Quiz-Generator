package quiz_generator.Frontend;

import java.util.*;

class FrontendSharedData implements Frontend
{
    private Background panel;
    private javax.swing.Timer clock;
    private String name;
    private Page nextPanel = Page.MAIN_PAGE;
    
    private final EnumSet<Condition> state = EnumSet.noneOf (Condition.class);
    
    @Override public Background getPanel() { return panel; }
    @Override public javax.swing.Timer getClock() { return clock; }
    @Override public String getName() { return name; }
    @Override public Page getNextPanel() { return nextPanel; }
    @Override public EnumSet<Condition> getState() { return state; };
    
    @Override public void setPanel (Background panel) { this.panel = panel; }
    @Override public void setClock (javax.swing.Timer clock) { this.clock = clock; }
    @Override public void setName (String name) { this.name = name; }
    @Override public void setNextPanel (Page nextPanel) { this.nextPanel = nextPanel; }
}