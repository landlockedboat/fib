package Dominio;

import java.io.Serializable;

/**
 * @author Xavier Peñalosa
 *
 * Each row of the Result is a NodePair, which has a pair of nodes (first/last node) and its Hetesim value.
 * 
 */


public class NodePair implements Serializable{

	
	private static final long serialVersionUID = 1L;
	public Pair<Node,Node> pairN;
	private float hetesimVal;
	
	NodePair(Node n1, Node n2, float hetesimVal) {
		pairN = new Pair<Node, Node>(n1,n2);
		setHetesim(hetesimVal);
	}
	
	/**
	 * Modify the HeteSim value.
	 * 
	 * @param hetesimVal > The new modified value for the NodePair.
	 */
	public void setHetesim(float hetesimVal){
		if (hetesimVal<0 || hetesimVal > 1) throw new RuntimeException("Hetesim has an invalid value");
		this.hetesimVal = hetesimVal;
	}

	public Node getFirstNode(){
		return pairN.first;
	}
	public Node getSecondNode(){
		return pairN.second;
	}
	
	public float getHetesim(){
		return hetesimVal;
	}
	
	public String toString(){
		return ("First Node: " + pairN.first.getNom() + " - Second Node: " + pairN.second.getNom() + " - Hetesim: " + hetesimVal);
	}
	
}
