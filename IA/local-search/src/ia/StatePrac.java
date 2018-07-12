package ia;

import IA.Red.Centro;
import IA.Red.CentrosDatos;
import IA.Red.Sensor;
import IA.Red.Sensores;

import java.util.Random;

public class StatePrac {
	// Haber estudiao
	public static boolean isInDebugMode = true;
	// Class that implements the state of the local search problem
	private int[][] connections;
	
	// This is for tweaking during experimentation
	private static double ponderacioCable = 1;
	private static double ponderacioInformacio = 1;
	
	public static Sensores sensors;
	public static CentrosDatos centers;
    // If all were connected correctly, this is the hypothetical amount of data
	// we could transmit
    public static int globalSensorCapturedData;
    // This exists because Java is not C++.
    private double efficientCableCost = 0;
    


	// Constructor
	// mode can be in three states: 0 = lousy, 1 = random, 2 = greedy
	public StatePrac(Sensores sensors, CentrosDatos centers, int mode) {
		// TODO posar totes les seed a random
		StatePrac.sensors = sensors;
		StatePrac.centers = centers;
        StatePrac.globalSensorCapturedData = 0;
		connections = new int[sensors.size() + centers.size()][];
		for (int i = 0; i < sensors.size(); i++) {
			connections[i] = new int[4];
			for (int j = 0; j < 4; j++) {
				connections[i][j] = -1;
			}
			globalSensorCapturedData += sensors.get(i).getCapacidad();
		}
		// [0] is the output, [1], [2], [3] are the inputs
		for (int i = sensors.size(); i < sensors.size() + centers.size(); i++) {
			connections[i] = new int[25];
			for (int j = 0; j < 25; j++) {
				connections[i][j] = -1;
			}
		}
		//al mode 1 fare anar aquesta conexió i fare molts canvis entre ells
		if (mode == 0 || mode == 1) {
			connections[0][0] = sensors.size();
            connections[sensors.size()][0] = 0;
			for (int i = 1; i < sensors.size(); i++) {
				connections[i][0] = i - 1;
				connections[i - 1][1] = i;
			}
		}

		if (mode == 1) {
            Random random = new Random();
            for(int i = 0; i < 100 * (sensors.size()+centers.size()); i++){
                connect(random.nextInt(sensors.size()), random.nextInt((sensors.size()+centers.size())));
            }
			// no et permet anar a tots els estats, ja que sempre
			// mante una estructura d'arbres amb els
			// nodes centrals com a arrels i els introdueix de forma
			// sequencial. per consequent mai passara que
			// (i < j) && j estigui conectat a i.
		}
	}

	// Copy constructor
	public StatePrac(StatePrac pStatePrac) {
        connections = new int[sensors.size() + centers.size()][];
        for(int i = 0; i < pStatePrac.connections.length; i++){
            int[] aux = pStatePrac.connections[i];
            connections[i] = new int[aux.length];
            for(int j = 0; j < aux.length; j++){
                connections[i][j]=aux[j];
            }
        }
	}

	public boolean connect(int origin, int destination) {
        int conectatOriginalment = connections[origin][0];
		// Base case, no need to do anything
		if (origin == destination) {
			return false;
		}
		
		// We try and grab the first empty input of the destination node
		int free_input = -1;
		for (int i = 0; i < connections[destination].length; i++) {
            //poso i = 0 perque o es una central o esta conectat a algo i te el primer numero positiu
			if ((connections[destination][i] < 0)) {
				free_input = i;
				break;
			}
		}
		
		// If there are no empty inputs
		if (free_input < 0) {
			return false;
		}
		
		// We are checking if this is a valid connection.
		// In this loop we iterate to find whether if our destination
		// sensor is connected to a Center or not by determining if the 
		// connection generates a cycle in the connections graph
		int pivot = destination;
		// While we have not "found" a Center yet;
		while (connections[pivot].length != 25 ) {
			// Check if the current sensor is connected to the original node
			if(connections[pivot][0] == origin){
				// We are in a loop! Connection is not valid, and we have to bail out
				return false;
			}
			// Go down a level and try and look for a Center again
			pivot = connections[pivot][0];
		}
		// If we've exited the previous loop, we can guarantee that this new
		// connection does not generate a loop.
		// So we can create the connection now:
		connections[origin][0] = destination;
		connections[destination][free_input] = origin;
        //desconectar on estava conectat
        for(int i = 0; i < connections[conectatOriginalment].length;i++){
            if(connections[conectatOriginalment][i] == origin){
            	connections[conectatOriginalment][i] = -1;
            	// sortim ara ja que si sortim a fora podem borrar el que acavem de conectar
                return true;
            }
        }
		// Everything went well!
		return true;
	}

	public double heuristic() {
		// This is done because the calculus for the cable
		// cost is done at the same time that the calculation
		// of center data.
		// E F F I C I E N C Y
		efficientCableCost = 0;
        double data = globalSensorCapturedData;
        // We iterate over all centers
        for (int i = 0; i < centers.size(); i++) {
            data -= getCentreData(i);
        }
        return efficientCableCost * ponderacioCable + 
        		Math.pow(data, 2) * ponderacioInformacio;

	}
	// This is bull shit but it is done in order to give more meaningful debug information
	// could've been more "elegantly" implemented but oh boy.
    public double costReal() {
        efficientCableCost = 0;
        // We iterate over all centers
        for (int i = 0; i < centers.size(); i++) {
        	getCentreData(i);
        }
        return efficientCableCost;
    }

	// Returns the connection cost between origin and destination
	// PRE:
	// origin MUST be a sensor
	// destination CAN BE a sensor OR a Center OR -1
	private double getConnectionCost(int origin, int destination) {
		// Base case, origin is not connected
		if(destination < 0){
			return 0;
		}
		
		int ox, oy, dx, dy;
		Sensor oSensor = sensors.get(origin);
		ox = oSensor.getCoordX();
		oy = oSensor.getCoordY();		
		
		if(destination >= sensors.size()){
			// Destination is a Center
			Centro dCenter = centers.get(destination-sensors.size());
			dx = dCenter.getCoordX();
			dy = dCenter.getCoordY();
		}
		else
		{
			// Destination is a sensor
			Sensor dSensor = sensors.get(destination);
			dx = dSensor.getCoordX();
			dy = dSensor.getCoordY();
		}
		// The connection cost is expressed by
		// the euclidean distance between two nodes times the connection volume
		double sqDist = Math.pow(ox - dx, 2) + Math.pow(oy - dy, 2);
		double vol = oSensor.getCapacidad();
		return sqDist * vol;
	}

	private double getCentreData(int index) {
		double data = 0;
		for(int i = 0; i < connections[index + sensors.size()].length; i++){
			if(connections[index + sensors.size()][i]>=0)
				data += getSensorData(connections[index + sensors.size()][i]);
		}
		return Math.min(data,150);
	}

	private double getSensorData(int index) {
		double data = sensors.get(index).getCapacidad();
		for(int i = 1; i < connections[index].length; i++){
			if(connections[index][i]>=0) {
                data += getSensorData(connections[index][i]);
            }
		}
		efficientCableCost += (getConnectionCost(index,connections[index][0])*Math.min(data,sensors.get(index).getCapacidad()*3));
		return Math.min(data,sensors.get(index).getCapacidad()*3);
	}

	public int emptyCenters() {
		int ec = 0;
		for (int i = sensors.size(); i < connections.length; ++i){
			boolean trobat = false;
			int j = 0;
			while (!trobat && j < connections[i].length){
				if(connections[i][j] > 0) trobat = true;
				j++;
			}
			if (!trobat) ++ec;
		}
		return ec;
	}
}
