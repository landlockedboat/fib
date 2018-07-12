package Dominio;

import java.util.Comparator;

/**
 * @author Xavier Peñalosa
 *
 * Defines the way to compare two NodePairs based on their HeteSim value:
 * The greater it is, the higher it will appear in the Result (Used in a mergesort by Result.java)
 * 
 */

public class NodePairComparator implements Comparator<NodePair>{
	public int compare(NodePair n1, NodePair n2){
		return (n1.getHetesim() < n2.getHetesim() ? 1 : -1);
	}
}

