import aima.search.informed.HillClimbingSearch;
import ia.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.SimulatedAnnealingSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import IA.Red.CentrosDatos;
import IA.Red.Sensores;

public class Main {
	
	private static boolean doIWantTime = true;
	
	private static void cout(String aMessage){
		System.out.println(aMessage);
	}
	
	private static String cin(){
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		try {
			line =  bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return line;
	}

	public static void main(String[] args) throws Exception {
		int numSens, numCenters;
		Random randomGenerator = new Random();
		// TODO make it random, maybe?
		cout("Enter the number of sensors you want:");
		numSens = Integer.parseInt(cin());
		cout("Enter the number of centers you want:");
		numCenters = Integer.parseInt(cin());;
		

		
		// Then we use them to create the state
		cout("1. Hill\n2. SimAnn\n3. Both");
		String opt = cin();
		cout("How many iterations do you want¿?¿?¿?");
		int n = Integer.parseInt(cin());
		cout("Initial state: \n0. Complete \n1. Random");
		int mode = Integer.parseInt(cin());
		if(!opt.equals("2")){
			for(int i = 0; i < n; ++i){
				// Create the sensors and the data centers
				int sensorSeed = randomGenerator.nextInt();
				cout("\nSensor seed is " + Integer.toString(sensorSeed));
				int centerSeed = randomGenerator.nextInt();
				cout("Center seed is " + Integer.toString(centerSeed));
				Sensores sensors = new Sensores(numSens, sensorSeed);
				CentrosDatos centers = new CentrosDatos(numCenters, centerSeed);
				StatePrac state = new StatePrac(sensors, centers, mode);
				long prevTime = System.nanoTime();
				HillClimbingSearch(state);
				long elapsedTime = System.nanoTime() - prevTime;
				if(doIWantTime){
					cout(elapsedTime + "");
				}
					
			}
		}
		if(!opt.equals("1")){
			for(int i = 0; i < n; ++i){
				// Create the sensors and the data centers
				int sensorSeed = randomGenerator.nextInt();
				cout("\nSensor seed is " + Integer.toString(sensorSeed));
				int centerSeed = randomGenerator.nextInt();
				cout("Center seed is " + Integer.toString(centerSeed));
				Sensores sensors = new Sensores(numSens, sensorSeed);
				CentrosDatos centers = new CentrosDatos(numCenters, centerSeed);
				StatePrac state = new StatePrac(sensors, centers,mode);
				long prevTime = System.nanoTime();
				SimulatedAnnealingSearch(state);
				long elapsedTime = System.nanoTime() - prevTime;
				if(doIWantTime){
					cout(elapsedTime + "");
				}
			}
		}
		cout("End\n\n");
	}

	public static void HillClimbingSearch(StatePrac state) throws Exception {

		System.out.println("HillClimbing  -->");

		// And then we use the state to create our problem
		Problem p = new Problem(state, new SuccessorFunctionPrac(),
				new GoalTestPrac(), new HeuristicFunctionPrac());
		// Instantiate the search algorithm
		Search alg = new HillClimbingSearch();
		// Instantiate the SearchAgent object
		SearchAgent agent = new SearchAgent(p, alg);
		// We print the results of the search
		System.out.println();
		printActions(agent.getActions());
		printInstrumentation(agent.getInstrumentation());
		// You can access also to the goal state using the
		// method getGoalState of class Search
	}

	public static void SimulatedAnnealingSearch(StatePrac state) throws Exception {

		System.out.println("TSP SimulatedAnnealing  -->");


		// And then we use the state to create our problem
		Problem p = new Problem(state, new SuccessorFunctionPracSA(),
				new GoalTestPrac(), new HeuristicFunctionPrac());
		// Instantiate the search algorithm
		// AStarSearch(new GraphSearch()) or IterativeDeepeningAStarSearch()
		// FIXME: Do not hard code parameters please
		SimulatedAnnealingSearch alg = new SimulatedAnnealingSearch(200000,100,5,0.001);
		// Instantiate the SearchAgent object
		alg.traceOn();
		SearchAgent agent = new SearchAgent(p, alg);
		// We print the results of the search
		System.out.println();
		printActions(agent.getActions());
		printInstrumentation(agent.getInstrumentation());
		// You can access also to the goal state using the
		// method getGoalState of class Search
	}

	private static void printInstrumentation(Properties properties) {
		Iterator keys = properties.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String property = properties.getProperty(key);
			System.out.println(key + " : " + property);
		}
	}

	// This has two possible implementations based on SimmAnn or HillClimb
	private static void printActions(List actions) {
		for (int i = 0; i < actions.size(); i++) {
			if(i == actions.size()-1){
				if((actions.get(i) instanceof String)){
					String action = (String) actions.get(i);
					System.out.println(action);
				}
				else
				{
					System.out.println("h:" + ((StatePrac)actions.get(i)).heuristic());
					System.out.println("c:" + ((StatePrac)actions.get(i)).costReal());
					cout("--ec:"+((StatePrac)actions.get(i)).emptyCenters());
				}
			}

		}
	}

}
