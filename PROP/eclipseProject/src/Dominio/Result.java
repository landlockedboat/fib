package Dominio;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;


/**
 * @author Xavier Peñalosa
 * 
 * This class stores the relation values from the HeteSim in an ArrayList made up of NodePairs.
 * given two Nodes and a Float. It allows the modification of the HeteSim values for a NodePair,
 * or its complete deletion.
 * Given more than one NodePair, this class will sort them in a descending order based on their value.
 *
 * 
 */

public class Result implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 1L;
	private Node firstN; //Search origin node
	private Node lastN; //Search destination node
	private Path usedP; //Search Path

	public enum ResultType{
		Path, PathThreshold, PathNode, PathNodeThreshold, PathNodeNode, PathNodeNodeThreshold
	}
	private ResultType resultType;
	
	private Float threshold; //Lowest heteSim value which will be displayed on screen 
	
	private String idResult; //Unique result id
	private String idGraph;	//Id of the graph associated to this result
	private Boolean modified; //When true: The result or the graph have been manually edited and they will probably not be coherent
	
	
	private ArrayList<NodePair> resultList; //Hetesim results, structured in NodePairs and (hopefully) sorted by Hetesim values
	
	
	/**
	 * Constructor for the case in which the Hetesim works with only one Path.
	 * 
	 * 
	 * @param g > Graph used to get the Node objects. 
	 * @param threshold > Float in range [0..1]. Will be used to display the result from this value.
	 * @param resultHete > Matrix with the hetesim values corresponding to the nodes i, j.
	 * @param p > Path used to calculate the hetesim values.
	 */
	public Result(final Graph g, final Float threshold, final Matrix resultHete, final Path p){
		if (resultHete == null) throw new RuntimeException("Null Matrix. Can't create result.");
		
		usedP = p;
		idResult = new String(String.valueOf(System.currentTimeMillis()));
		idGraph = String.valueOf(g.id);
		modified = false;
		this.threshold = threshold;
		
		if (threshold == 0){
			resultType = ResultType.Path;
		}
		else {
			resultType = ResultType.PathThreshold;
		}
		
		resultList = new ArrayList<NodePair>();
		for (Integer i = 0; i < resultHete.getNRows(); ++i){
			for (Integer j : resultHete.getRow(i).keySet()) {
				if (resultHete.getValue(i,j) != 0.f){
					Node n1 = g.getNode(i, p.getContingut().get(0)); //Get first node
					Node n2 = g.getNode(j, p.getContingut().get(p.getLength()-1)); //Get second node
					Float hetesimVal = resultHete.getValue(i,j); //Get hetesim value
					
					resultList.add(new NodePair(n1,n2,hetesimVal)); //Add NodePair to result list
				}
			}
		}
		
		sortResult(); //Sort result list
	}
	
	
	/**
	 * Constructor for the case in which the Hetesim works with one Node and one Path.
	 * 
	 * 
	 * @param g > Graph used to get the Node objects. 
	 * @param threshold  > Float in range [0..1]. Will be used to display the result from this value.
	 * @param resultHete > ArrayList with the HeteSim values corresponding to the node n1. Organized in pairs with the destination node and their hetesim value.
	 * @param p > Path used to calculate the HeteSim values.
	 * @param n1 > Starting node from which HeteSim was calculated.
	 */
	//One node, one path
	public Result(final Graph g, final Float threshold, final ArrayList<Pair<Integer,Float>> resultHete, final Path p, final Node n1) {
		if (p.getContingut().get(0) != n1.getTipus()) throw new RuntimeException("Result Path/Node: Node type doesn't match path");
		if (resultHete == null) throw new RuntimeException("Null ArrayList. Can't create result.");
		
		firstN = n1;
		usedP = p;
		modified = false;
		this.threshold = threshold;

		idResult = new String(String.valueOf(System.currentTimeMillis()));
		idGraph = String.valueOf(g.id);

		if (threshold == 0){
			resultType = ResultType.PathNode;
		}
		else {
			resultType = ResultType.PathNodeThreshold;
		}
		
		resultList = new ArrayList<NodePair>();
		for(Integer i = 0; i < resultHete.size(); ++i){
			Node n2 = g.getNode(resultHete.get(i).first, p.getContingut().get(p.getLength()-1)); //Get second node (we already have the first)
			float hetesimVal = resultHete.get(i).second; //Get hetesim value
			resultList.add(new NodePair(n1,n2,hetesimVal)); //Add NodePair to result list
		}
		
		sortResult(); //Sort result list
	}
	
	
	/**
	 * Constructor for the case in which the HeteSim works with two Nodes and one Path.
	 * 
	 * 
	 * @param g > Graph used to get the Node objects. 
	 * @param threshold  > Float in range [0..1]. Will be used to display the result from this value.
	 * @param resultHete > Float with the HeteSim value corresponding to the node n1 and n2 with path p.
	 * @param p > Path used to calculate the HeteSim values.
	 * @param n1 > Starting node from which HeteSim was calculated.
	 * @param n2 > Destination node from which HeteSim was calculated.
	 */
	public Result(final Graph g, final float threshold, final Float resultHete, final Path p, final Node n1, final Node n2){
		//Assert that the path starts with the node type N1 and ends with the node type N2
		if (p.getContingut().get(0) != n1.getTipus()) throw new RuntimeException("Result Path/Node/Node: Node 1 type doesn't match path");
		if (p.getContingut().get(p.getLength()-1) != n2.getTipus()) throw new RuntimeException("Result Path/Node/Node: Node 2 type doesn't match path");
		if (resultHete == null) throw new RuntimeException("Null Float. Can't create result.");
		
		firstN = n1;
		lastN = n2;
		usedP = p;
		modified = false;
		this.threshold = threshold;
		
		if (threshold == 0){
			resultType = ResultType.PathNodeNode;
		}
		else {
			resultType = ResultType.PathNodeNodeThreshold;
		}

		idResult = new String(String.valueOf(System.currentTimeMillis()));
		idGraph = String.valueOf(g.id);
		
		resultList = new ArrayList<NodePair>();
		resultList.add(new NodePair(n1,n2,resultHete)); //Create NodePair and add to list. We only need to get the float value from Hetesim
	}
	
	
	
	public String toString(){
		String retStr = new String();
		
		retStr = ("Resultado: " + idResult + "\n");                                            //Result: idresult
		retStr = retStr + ("    Path: " + usedP.getNom() + "\n");                            //Path: path
		if (firstN != null) retStr = retStr + ("    N1: " + firstN.getNom() + "\n");           //N1: <Node to string>    <<<<Igual solo con el nombre basta?
		if (lastN != null) retStr = retStr + ("    N2: " + lastN.getNom() + "\n");             //N2: <Node to string>    <<<<Igual solo con el nombre basta?
		retStr = retStr + ("    Threshold: " + threshold + "\n");                              //Threshold: threshold
		retStr = retStr + ("\n");                                                              //
		int i = 0;
		//System.out.println(resultList.size());
		while (i < resultList.size() && resultList.get(i).getHetesim() >= threshold){
			retStr = retStr + "    " + resultList.get(i).toString() + "\n";                    //First node: <Node to string> Last node: <Node to string> Hetesim: valorHetesim
			++i;
		}
		
		return retStr; 
	}


	/**
	 * Checks consistency with the loaded graph. If the ids don't match, the
	 * return value of toString will have an extra line with "Not consistent"
	 * at the beggining.
	 * 
	 * 
	 * @param g > Graph used to check consistency with the stored nodes.
	 * @return String with a value of result.toString(). It might have an extra line at the start. 
	 */
	public String toString(Graph g){
		String retStr = new String();
		if (String.valueOf(g.id) != idGraph || modified) retStr = retStr + "Not consistent!\n";
		retStr = retStr + toString();
		return retStr;
	}
	
	/**
	 * Set new threshold. When calling toString(), only the NodePairs with Hetesim value over this threshold will be displayed.
	 * 
	 * 
	 * @param threshold > Used to define a new value from which Result will be displayed.
	 */
	public void setThreshold(float threshold){
		this.threshold = threshold;
	}
	
	//Get the result list
	public ArrayList<NodePair> getResult(){ 
		ArrayList<NodePair> retResult = new ArrayList<NodePair>();
		int i = 0;
		//System.out.println(resultList.get(0).getHetesim());
		while (i < resultList.size() && resultList.get(i).getHetesim() >= threshold && retResult.size() < 50){
		    retResult.add(resultList.get(i));
		    ++i;
		}
		return retResult;
	}
	
	
	public String getIdResult(){
		String retStr = new String(idResult);
		return retStr;
	}
	
	public void setIdResult(String idResult){
		this.idResult = idResult;
	}

	public String getIdGraf(){
		return idGraph;
	}
	
	public void setIdGraf(String idGraph){
		this.idGraph = idGraph;
	}
	
	public String getUsedPath(){
		return usedP.getNom();
	}
	
	public ResultType getResultType(){
		return resultType;
	}
	
	public Float getThreshold(){
		return threshold;
	}
	public void setThreshold(Float threshold){
		this.threshold = threshold;
	}
	
	/**
	 * Modify the value of the HeteSim for the NodePair stored in line i
	 * 
	 * 
	 * @param i > Indicates which result line should have its HeteSim value modified.
	 * @param hetesimVal > The new modified value for the NodePair.
	 */
	public void modifLine(Integer i, Float hetesimVal){
		if (i < 0 || i > resultList.size()) throw new ArrayIndexOutOfBoundsException("Can't access the element " + i + " in resultList of size " + resultList.size() + ".");
		
		resultList.get(i).setHetesim(hetesimVal);
		modified = true;
		sortResult();
	}
	
	/**
	 * Delete the NodePair stored in the line i
	 * 
	 * 
	 * @param i > Indicates which result line should be removed.
	 */
	public void deleteLine(Integer i){
		if (i < 0 || i > resultList.size()) throw new ArrayIndexOutOfBoundsException("Can't access the element " + i + " in resultList of size " + resultList.size() + ".");
		
		resultList.remove(i);
		modified = true;
	}
	
	/**
	 * If either the result or the graph have been modified, the information is not consistent and toString() will issue a warning
	 * 
	 */
	public void setModified(){ 
		modified = true;
	}
	public boolean isModified(){
		return modified;
	}
	
	
	private void sortResult(){ //Sort the result list by hetesim value
		//System.out.println(resultList.size());
		try {
			Collections.sort(resultList,new NodePairComparator());
		}
		catch (IllegalArgumentException e){
			//System.out.println("Something went wrong when sorting the result");
		}
	}
}
