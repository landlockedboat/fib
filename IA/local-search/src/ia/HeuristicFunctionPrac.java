package ia;

import aima.search.framework.HeuristicFunction;

public class HeuristicFunctionPrac implements HeuristicFunction {
	
    public double getHeuristicValue(Object state){
    	return ((StatePrac) state).heuristic();
    }
}
