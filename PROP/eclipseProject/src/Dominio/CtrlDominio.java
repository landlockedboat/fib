/**
 * @author Victor Alcazar Lopez
 **/

package Dominio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class CtrlDominio {

	// I/O Controllers
	CtrlData ctrlData;
	CtrlImport ctrlImport;
	// Data structures Controllers
	CtrlGraph ctrlGraph;
	CtrlPaths ctrlPaths;
	CtrlResults ctrlResults;
	// Method controllers
	CtrlSearch ctrlSearch;
	//path to where the graph is stored
	String graphPath;

	/**
	 * Default constructor, creates an empty graph and initializes CtrlSearch with that graph.
	 */
	public CtrlDominio() {
		ctrlData = new CtrlData();
		ctrlSearch = new CtrlSearch();
		ctrlGraph = new CtrlGraph();
		ctrlPaths = new CtrlPaths();
		ctrlResults = new CtrlResults();
		ctrlSearch.setGraph(ctrlGraph.getGraph());
	}

	/**
	 * @deprecated Use the default constructor instead.<br>
	 * Creates an empty graph and initializes CtrlSearch with that graph.
	 * @see #CtrlDominio()
	 */
	public void createGraph() {
		ctrlGraph.setGraph(new Graph());
		ctrlResults = new CtrlResults();
		ctrlSearch.setGraph(ctrlGraph.getGraph());
	}

	/**
	 * Overwrites all the paths loaded in Domain with the paths stored in the file path <b>filePath</b>.
	 * Can only be used after giving CtrlDominio a valid file path via <b>importGraph</b>.
	 * @return Returns an ArrayList of strings consisting of all the loaded paths' names.
	 * @see #importGraph(String)
	 */
	public ArrayList<String> loadStoredPaths() {
		ArrayList<Path> pathArray = new ArrayList<Path>();
		ArrayList<String> pathNames = new ArrayList<String>();
		try {
			pathArray = ctrlData.loadallPaths();
			ctrlPaths = new CtrlPaths(pathArray);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		for (Path p : pathArray) {
			pathNames.add(p.getNom());
		}
		return pathNames;
	}

	/**
	 * Overwrites the graph loaded in Domain with the graph stored in the file path <b>filePath</b>.
	 * Can only be used after giving CtrlDominio a valid file path via <b>importGraph</b>.
	 * @param idGraph The id of the graph to be loaded.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 * @see #importGraph(String)
	 */
	public void loadGraph(String idGraph) throws ClassNotFoundException, FileNotFoundException, IOException {
		Pair<Graph, ArrayList<Result>> auxPair;
		
			auxPair = ctrlData.loadgraphAndResults(idGraph);
			ctrlGraph = new CtrlGraph(auxPair.first);
			ctrlSearch.setGraph(ctrlGraph.getGraph());
			ctrlResults = new CtrlResults(auxPair.second);
		

	}

	/**
	 * Stores the current graph in memory.
	 */

	public void storeGraph() {
		try {
			ctrlData.storeGraf(ctrlGraph.getGraph());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * SEARCH FUNCTIONS Some general recommendations when sending search queries
	 * to CtrlDominio: All paths must be sent to the controller as a string
	 * that's its name attribute All nodes must be sent to the controller as two
	 * integers: First, one that holds the correspondent nodeIndex Second, one
	 * that holds its type. WARNING: the Integer that's being passed has to be
	 * the same as the index of the type of node that is wanted. Threshold is
	 * passed as a float.
	 */

	/**
	 * Execute a search function on the loaded graph with a threshold and a path as parameters.
	 * The search result may be lost if it is not stored immediately afterwards. Use <b>saveLastSearchResult</b> to do so.
	 * @param threshold the results with lower relatedness than the <b>threshold</b> will be ignored.
	 * @param pathName the name of the path the search will be executed on.
	 * @see #saveLastSearchResult()
	 */

	public void searchPathThreshhold(Float threshold, String pathName) {
		if (ctrlGraph.isModified)
			ctrlSearch.setGraph(ctrlGraph.getGraph());
		try {
			Result r = ctrlSearch.searchPathThreshhold(threshold, ctrlPaths.getPath(pathName));
			ctrlResults.setLastResult(r);
		} catch (PathException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Execute a search function on the loaded graph with a path as parameter.
	 * The search result may be lost if it is not stored immediately afterwards. Use <b>saveLastSearchResult</b> to do so.
	 * @param pathName the name of the path the search will be executed on. 
	 * @see #saveLastSearchResult()
	 */
	public void searchPath(String pathName) {
		//system.out.println(ctrlPaths.getPath(pathName).getContingut());
		if (ctrlGraph.isModified)
			ctrlSearch.setGraph(ctrlGraph.getGraph());
		try {
			Result r = ctrlSearch.searchPath(ctrlPaths.getPath(pathName));
			ctrlResults.setLastResult(r);
		} catch (PathException e) {
			//system.out.println("Path exception generated");
			e.printStackTrace();
		}
	}

	/**
	 * Execute a search function on the loaded graph with a threshold, a node and a path as parameters.
	 * The search result may be lost if it is not stored immediately afterwards. Use <b>saveLastSearchResult</b> to do so.
	 * @param threshold the results with lower relatedness than the <b>threshold</b> will be ignored.
	 * @param pathName the name of the path the search will be executed on.
	 * @param nodeIndex the index on the graph of the node the search will be executed on.
	 * @see #saveLastSearchResult()
	 */
	public void searchPathNodeThreshhold(Float threshold, String pathName, Integer nodeIndex) {
		Graph graf = ctrlGraph.getGraph();
		if (ctrlGraph.isModified)
			ctrlSearch.setGraph(ctrlGraph.getGraph());
		try {
			Path path = ctrlPaths.getPath(pathName);
			ArrayList<Node.Type> pathTypes= path.getContingut();
			Result r = ctrlSearch.searchPathNodeThreshhold(threshold, ctrlPaths.getPath(pathName),
					graf.getNode(nodeIndex, pathTypes.get(0)));
			ctrlResults.setLastResult(r);
		} catch (PathException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Execute a search function on the loaded graph with a node and a path as parameters.
	 * The search result may be lost if it is not stored immediately afterwards. Use <b>saveLastSearchResult</b> to do so.
	 * @param pathName the name of the path the search will be executed on.
	 * @param nodeIndex the index on the graph of the node the search will be executed on.
	 * @see #saveLastSearchResult()
	 */
	public void searchPathNode(String pathName, Integer nodeIndex) {
		Graph graf = ctrlGraph.getGraph();
		if (ctrlGraph.isModified)
			ctrlSearch.setGraph(ctrlGraph.getGraph());
		try {
			Path path = ctrlPaths.getPath(pathName);
			ArrayList<Node.Type> pathTypes= path.getContingut();
			Result r = ctrlSearch.searchPathNode(ctrlPaths.getPath(pathName),
					graf.getNode(nodeIndex, pathTypes.get(0)));
			//return ctrlResults.setLastResult(r);
			ctrlResults.setLastResult(r);
		} catch (PathException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Execute a search function on the loaded graph with two nodes, a threshold and a path as parameters.
	 * The search result may be lost if it is not stored immediately afterwards. Use <b>saveLastSearchResult</b> to do so.
	 * @param threshold the results with lower relatedness than the <b>threshold</b> will be ignored.
	 * @param pathName the name of the path the search will be executed on.
	 * @param node1Index the index on the graph of the one of the nodes the search will be executed on.
	 * @param node2Index the index on the graph of the one of the nodes the search will be executed on.
	 * @see #saveLastSearchResult()
	 */
	public void searchPathNodeNodeThreshhold(Float threshold, String pathName, Integer node1Index,
			Integer node2Index) {
		Graph graf = ctrlGraph.getGraph();
		if (ctrlGraph.isModified)
			ctrlSearch.setGraph(ctrlGraph.getGraph());
		try {
			Path path = ctrlPaths.getPath(pathName);
			ArrayList<Node.Type> pathTypes= path.getContingut();
			Result r = ctrlSearch.searchPathNodeNodeThreshhold(threshold, ctrlPaths.getPath(pathName),
					graf.getNode(node1Index, pathTypes.get(0)),
					graf.getNode(node2Index, pathTypes.get(pathTypes.size() - 1)));
			//return ctrlResults.setLastResult(r);
			ctrlResults.setLastResult(r);
		} catch (PathException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Execute a search function on the loaded graph with two nodes and a path as parameters.<br>
	 * The search result may be lost if it is not stored immediately afterwards. Use <b>saveLastSearchResult</b> to do so.
	 * @param pathName the name of the path the search will be executed on.
	 * @param node1Index the index on the graph of the one of the nodes the search will be executed on.
	 * @param node2Index the index on the graph of the one of the nodes the search will be executed on.
	 * @see #saveLastSearchResult()
	 */
	public void searchPathNodeNode(String pathName, Integer node1Index, Integer node2Index) {
		Graph graf = ctrlGraph.getGraph();
		if (ctrlGraph.isModified)
			ctrlSearch.setGraph(ctrlGraph.getGraph());
		try {
			Path path = ctrlPaths.getPath(pathName);
			ArrayList<Node.Type> pathTypes= path.getContingut();
			Result r = ctrlSearch.searchPathNodeNode(ctrlPaths.getPath(pathName),
					graf.getNode(node1Index, pathTypes.get(0)),
					graf.getNode(node2Index, pathTypes.get(pathTypes.size() - 1)));
			//return ctrlResults.setLastResult(r);
			ctrlResults.setLastResult(r);
		} catch (PathException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Saves the result of the last search.
	 */
	public void saveLastSearchResult() {
		ctrlResults.addLastResult();
	}

	// OTHER FUNCTIONS
	/**
	 * 
	 * @return Returns the instance of CtrlGraph associated with this instance of CtrlDomain.
	 */
	public CtrlGraph getCtrlGraph() {
		return ctrlGraph;
	}

	/**
	 * 
	 * @return Returns the instance of CtrlPaths associated with this instance of CtrlDomain.
	 */
	public CtrlPaths getCtrlPaths() {
		return ctrlPaths;
	}

	/**
	 * 
	 * @return Returns the instance of CtrlSearch associated with this instance of CtrlDomain.
	 */
	public CtrlSearch getCtrlSearch() {
		return ctrlSearch;
	}

	/**
	 * 
	 * @return Returns the instance of CtrlResults associated with this instance of CtrlDomain.
	 */
	public CtrlResults getCtrlResults() {
		return ctrlResults;
	}

	/**
	 * Saves the current graph to the file path <b>filePath</b>. 
	 * Can only be used after giving CtrlDominio a valid file path via <b>importGraph</b>.
	 * @see CtrlDominio#importGraph(String)
	 */
	public void saveGraph() {
		if (ctrlGraph.isModified) {
			try {
				ctrlData.storeGraf(ctrlGraph.getGraph());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Saves the current paths to the file path <b>filePath</b>. 
	 * Can only be used after giving CtrlDominio a valid file path via <b>importGraph</b>.
	 * @see CtrlDominio#importGraph(String)
	 */
	public void savePaths() {
		ArrayList<Path> modifiedPaths = ctrlPaths.getModifiedPaths();
		for (Path p : modifiedPaths) {
			try {
				ctrlData.storePath(p);
			} catch (CloneNotSupportedException | IOException e) {
				//system.out.println("Error saving path");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Saves the current results to the file path <b>filePath</b>. 
	 * Can only be used after giving CtrlDominio a valid file path via <b>importGraph</b>.
	 * @see CtrlDominio#importGraph(String)
	 */
	public void saveResults() {
		ArrayList<Result> modifiedResults = ctrlResults.getModifiedResults();
		for (Result r : modifiedResults) {
			try {
				ctrlData.storeResult(r);
			} catch (CloneNotSupportedException | IOException e) {
				//system.out.println("Error saving result");
				e.printStackTrace();
			}
		}
		ctrlResults.resultsStored();
	}

	/**
	 * Saves all the modified entities to the file path <b>filePath</b>. 
	 * Can only be used after giving CtrlDominio a valid file path via <b>importGraph</b>.
	 * @see CtrlDominio#importGraph(String)
	 */
	public void saveAllModifiedEntities() {
		saveGraph();
		savePaths();
		saveResults();
	}
	/**
	 * Loads a graph formatted for importing stored in the filePath.<br>
	 * It is necessary to call this function before doing any load/store operation 
	 * @param filePath
	 * @throws IOException
	 */
	public void importGraph(String filePath) throws IOException {
		ctrlImport = new CtrlImport(filePath);		
		ctrlImport.loadGraphInfo();
		ctrlGraph.setGraph(ctrlImport.getGraph());
		ctrlSearch.setGraph(ctrlGraph.getGraph());

	}

	/**
	 * 	
	 * @return Returns an ArrayList of strings containing the names of all the available labels.
	 */
	static public ArrayList<String> getLabels(){
		ArrayList<String> ret = new ArrayList<String>();
		for (Node.Label label : Node.Label.values()) {
			ret.add(label.toString());
		}
		return ret;
	}

	/**
	 * 
	 * @return Returns an ArrayList of strings containing the names of all the available node Types.
	 */
	static public ArrayList<String> getTypes(){
		ArrayList<String> ret = new ArrayList<String>();
		for (Node.Type type: Node.Type.values()){
			if(!type.equals(Node.Type.MidElement))
				ret.add(type.toString());
		}
		return ret;
	}

	/**
	 * 
	 * @param nodeType the node Type to get the index of.
	 * @return Returns an int with the index associated to the node Type <b>nodeType</b>
	 */
	static public int getNodeTypeIndex(String nodeType){
		int n = Node.Type.valueOf(nodeType).ordinal();
		if(n >= Node.Type.MidElement.ordinal())
			++n;
		return n;
	}

	/**
	 * 
	 * @param nodeLabel the node label to get the index of.
	 * @return Returns an int with the index associated to the node label <b>nodeLabel</b>
	 */
	static public int getIndexOfNodeLabel(String nodeLabel){
		int n = Node.Label.valueOf(nodeLabel).ordinal();
		return n;
	}
	
	/**
	 * 
	 * @param index the index to get the node label of.
	 * @return Returns the node Label associated with the index <b>index</b>
	 */
	static public String getNodeLabelOfIndex(int index){
		return Node.Label.values()[index].toString();
	}
	/**
	 * 
	 * @param index the index to get the node type of.
	 * @return Returns the node Type associated with the index <b>index</b>
	 */
	static public String getNodeTypeOfIndex(int index){
		if(index >= Node.Type.MidElement.ordinal())
			++index;
		return Node.Type.values()[index].toString();
	}
}
