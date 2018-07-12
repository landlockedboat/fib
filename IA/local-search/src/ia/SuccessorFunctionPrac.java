package ia;

import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;
import java.util.ArrayList;
import java.util.List;

public class SuccessorFunctionPrac implements SuccessorFunction{

    public SuccessorFunctionPrac() {
    }
    public List<Object> getSuccessors(Object state){
        StatePrac myState = ((StatePrac) state);
        List<Object> states = new ArrayList<Object>();
        // We create a new state
        StatePrac newState = new StatePrac(myState);
        for(int i = 0; i < StatePrac.sensors.size(); i++){
            for(int j = 0; j < StatePrac.sensors.size() + StatePrac.centers.size();j++ ) {
            	String stateDescription = "";
            	if(StatePrac.isInDebugMode){
            		stateDescription =
                			"" + i + " -> " + j +
                			" h:" + newState.heuristic() +
                			" c:" +
                			myState.costReal();
            		int ec = myState.emptyCenters();
            		stateDescription += "---empty centers:" + ec;
            	}
            	if (newState.connect(i, j)) {
                    states.add(new Successor(stateDescription, newState));
                    newState = new StatePrac(myState);
                }
            }
        }
        return states;
    }

}
