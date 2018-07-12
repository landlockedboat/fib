package ia;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SuccessorFunctionPracSA implements SuccessorFunction{

    public SuccessorFunctionPracSA() {
    }

    public List<Object> getSuccessors(Object state){
        List<Object> states = new ArrayList<>();
        StatePrac board = new StatePrac((StatePrac) state);
        Random random = new Random();
        random.nextInt();
        while (board.connect(
        		random.nextInt(StatePrac.sensors.size()),
        		random.nextInt(StatePrac.sensors.size() + StatePrac.centers.size())));
        
        String stateDescription = "";
        
        if(StatePrac.isInDebugMode){
        	stateDescription = "te heuristic :" + board.heuristic();
        }
        
        states.add(new Successor(stateDescription, board));
        return states;
    }
}
