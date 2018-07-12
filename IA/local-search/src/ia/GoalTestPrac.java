package ia;

import aima.search.framework.GoalTest;

public class GoalTestPrac implements GoalTest {
	
	//private StatePrac state;
	
    public boolean isGoalState(Object state){
    	// This always returns false because our algorithm never actually reaches a solution
    	return false;
    }
}
