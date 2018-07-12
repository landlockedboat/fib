/**
 * @author Victor Alcazar Lopez
**/

package Dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CtrlGraph {
	private Graph graph;
	Boolean isModified = false;

	public CtrlGraph() {
		graph = new Graph();
		graph.id = (int) System.currentTimeMillis();
	}

	public CtrlGraph(Graph graph) {
		this.graph = graph;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		isModified = true;
		graph.id = (int) System.currentTimeMillis();
		this.graph = graph;
	}

	public int addNode(String nodeType, String nodeName) {
		isModified = true;
		graph.id = (int) System.currentTimeMillis();
		return graph.addNode(Node.Type.valueOf(nodeType), nodeName);
	}

	public void modifyNode(Integer nodeIndex, String nodeType, String newName) {
		isModified = true;
		graph.id = (int) System.currentTimeMillis();
		Node n = graph.getNode(nodeIndex, Node.Type.valueOf(nodeType));
		Node.Label label = Utils.getNodeLabel(0);
		n.setLabel(label);
		n.setNom(newName);
	}

	public void eraseNode(Integer nodeIndex, String nodeType) {
		isModified = true;
		graph.id = (int) System.currentTimeMillis();
		Node n = graph.getNode(nodeIndex, Node.Type.valueOf(nodeType));
		graph.deleteNode(n);
	}

	// PRE: node1 MUST be a paper
	public void addNodeRelation(Integer node1IndexPaper, Integer node2Index, String node2Type) {
		isModified = true;
		graph.id = (int) System.currentTimeMillis();
		Node n1 = null;
		Node n2 = null;
		try {
			n1 = graph.getNode(node1IndexPaper, Node.Type.Paper);
		} catch (Exception e) {
			//system.out.println("Node 1 does not exist");
			return;
		}
		try {
			n2 = graph.getNode(node2Index, Node.Type.valueOf(node2Type));
		} catch (Exception e) {
			//system.out.println("Node 2 does not exist");
			return;
		}
		try {
			if (!graph.existsArc(n2, n1)) {
				graph.setArc(node1IndexPaper, node2Index, Node.Type.valueOf(node2Type));
			} else{
				//system.out.println("Relation already exists");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printNodesOfType(String t){		
		Utils.printNodesOfType(graph, Node.Type.valueOf(t));
	}
	
	public void printGraf(){
		Utils.printGraf(graph);
	}

	// PRE: node1 MUST be a paper
	public void eraseNodeRelation(Integer node1IndexPaper, Integer node2Index, String node2Type) {
		isModified = true;
		graph.id = (int) System.currentTimeMillis();
		Node n1 = graph.getNode(node2Index, Node.Type.valueOf(node2Type));
		Node n2 = graph.getNode(node1IndexPaper, Node.Type.Paper);
		try {
			if (graph.existsArc(n1, n2)) {
				graph.deleteArc(n1, n2);
			} else {
				//system.out.println("Relation does not exist");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Node.Label getNodeLabel(Integer i) {
		return Node.Label.values()[i];
	}

	//FORMATTING STUFF ----------------------------------------------------------------
	
	//Returns an ArrayList with:
	//0: String with the node's name
	//1: String with the node's type
	
	private ArrayList<String> formatNode(Integer index, Node n) {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add(String.valueOf(index));
		ret.add(n.getNom());
		ret.add(n.getTipus().toString());
		try{
			ret.add(n.getLabel().toString());
		}
		catch(Exception e){
			//Default Label
			ret.add(Node.Label.AI.toString());
		}
		return ret;
	}
	
	//Returns an arrayList of formatted nodes 
	//COLUMNS:
	//0 - Node's index
	//1 - Node's name
	//2 - Node's type
	
	private ArrayList<ArrayList<String>> formatMatrixNodes(Matrix m, Node.Type t){
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < m.getNRows(); ++i) {
			Node n = graph.getNode(i, t);
			if (n != null) ret.add(formatNode(i, n)); 
		}
		return ret;
	}
	
	private ArrayList<ArrayList<String>> formatPaperNodes(Matrix m){
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		try{
			for (int i = 0; i < m.getNCols(); ++i) {
				Node n = graph.getNode(i, Node.Type.Paper);
				if (n != null) ret.add(formatNode(i, n));
			}
		}catch(Exception e){
			//If there isn't any papers in a given matrix, the program crashes
		}
		return ret;

	}
	/**
	 * @param nodeType the type of nodes to be formatted
	 * @return
	 * Returns a matrix of all the graph's nodes of type <b>nodeType</b> formatted by the following criteria: <br><br>
	 * <i>Multiple rows</i> containing all formatted nodes of type <b>nodeType</b> formatted like so:
	 * <ol>
	 * 	 <li>Node's index</li>
	 * 	 <li>Node's name</li>
	 * 	 <li>Node's type</li>
	 * </ol>
	 */
	public ArrayList<ArrayList<String>> getformattedNodesOfType(String nodeType){
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		
		Matrix mauthor = graph.getMatrixAuthor();
		Matrix mterme = graph.getMatrixTerm();
		Matrix mconf = graph.getMatrixConf();
		Node.Type t = Node.Type.valueOf(nodeType);
		if(t == Node.Type.Autor){
			ret = formatMatrixNodes(mauthor, t);			
		}else if(t == Node.Type.Conferencia){
			ret = formatMatrixNodes(mconf, t);
		}else if(t == Node.Type.Terme){
			ret = formatMatrixNodes(mterme, t);
		}else if(t == Node.Type.Paper){
			ret = formatPaperNodes(mauthor);
		}else{
			//system.out.println("Node type not found " + t);
			return null;
		}
		
		Collections.sort(ret, new formatedNodeComparator());
		return ret;
	}
	
	/**
	 * Get the graph formatted like a matrix of strings of variable size.
	 * 
	 * @return
	 *<p>Returns an arrayList of arrayLists of strings formatted by the following criteria:</p>
	 *<ol>
	 *	<li><i>A single row</i> containing:
	 *	<ol>
	 *		<li>Name of the graph</li>
	 *		<li>Number of author nodes</li>
	 *		<li>Number of term nodes</li>
	 *		<li>Number of conference nodes</li>
	 *	</ol>
	 *	</li>
	 *	<li><i>Multiple rows</i> containing formatted <b>author nodes</b> formatted like so:
	 *	<ol>
	 *		<li>Node's index</li>
	 *		<li>Node's name</li>
	 *		<li>Node's type</li>
	 *	</ol>
	 *	</li>
	 *	<li><i>Multiple rows</i> containing formatted <b>term nodes</b>, formatted like the previous ones.</li>
	 *	<li><i>Multiple rows</i> containing formatted <b>conference nodes</b>, formatted like the previous ones.</li>
	 *	<li><i>Multiple rows</i> containing formatted <b>paper nodes</b>, formatted like the previous ones.</li>
	 *	<li><i>Multiple rows</i> containing formatted <b>Author - Paper relations</b>, formatted like so:
	 *	<ol>
	 *		<li>First node's index</li>
	 *		<li>First node's name</li>
	 *		<li>First node's type</li>
	 *		<li>Second node's index</li>
	 *		<li>Second node's name</li>
	 *		<li>Second node's type</li>
	 *	</ol>
	 *	</li>
	 *	<li><i>Multiple rows</i> containing formatted <b>Term - Paper relations</b>, formatted like the previous ones.</li>
	 *	<li><i>Multiple rows</i> containing formatted <b>Conference - Paper relations</b>, formatted like the previous ones.</li>
	 *</ol>
	 */
	public ArrayList<ArrayList<String>> getFormatted() {
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		ArrayList<String> firstCol = new ArrayList<String>();
		firstCol.add(graph.getNom());
		//ret.add(graph.getNom());
		Matrix mauthor = graph.getMatrixAuthor();
		Matrix mterme = graph.getMatrixTerm();
		Matrix mconf = graph.getMatrixConf();
		firstCol.add(String.valueOf(mauthor.getNRows()));
		firstCol.add(String.valueOf(mterme.getNRows()));
		firstCol.add(String.valueOf(mconf.getNRows()));
		try{
			firstCol.add(String.valueOf(mauthor.getNCols()));
		}
		catch (Exception e){
			//Program blows up if there are no papers
			firstCol.add("0");
		}
		ret.add(firstCol);
		//Adding nodes
		ret.addAll(formatMatrixNodes(mauthor, Node.Type.Autor));
		ret.addAll(formatMatrixNodes(mterme, Node.Type.Terme));
		ret.addAll(formatMatrixNodes(mconf, Node.Type.Conferencia));
		ret.addAll(formatPaperNodes(mauthor));
		//Adding relations
		ret.addAll(formatRelations(mauthor, Node.Type.Autor));
		ret.addAll(formatRelations(mterme, Node.Type.Terme));
		ret.addAll(formatRelations(mconf, Node.Type.Conferencia));

		return ret;

	}
	/**
	* Returns a string matrix with the nodes related to the <b><i>node</i></b> associated with <b>index</b> and <b>nodeType</b>.
	* 
	* @param index the node's index the relations with will be calculated.
	* @param nodeType the node's type the relations with will be calculated.
	* @return
	* Returns an arrayList of arrayLists of strings formatted by the following criteria:<br><br>
	* <i>Multiple rows</i> containing formatted nodes related with the <b><i>node</i></b>, formatted like so:
	*  	<ol>
	*  	    <li>Related node's index</li>
	* 		<li>Related node's name</li>
	* 		<li>Related node's type</li>
	*   </ol>	      
	*/
	public ArrayList<ArrayList<String>> getNodeRelationsFormatted(Integer index, String nodeType){
		Node.Type nType = Node.Type.valueOf(nodeType);
		switch (nType) {
		case Autor:
			return formatNodeRelations(graph.getMatrixAuthor(), index);
		case Conferencia:
			return formatNodeRelations(graph.getMatrixConf(), index);
		case Terme:
			return formatNodeRelations(graph.getMatrixTerm(), index);
		case Paper:
			return formatNodeRelations(graph.getMatrixAuthor(), graph.getMatrixTerm(), graph.getMatrixConf(), index);
		default:
			return null;
		}
	}
	
	
	private ArrayList<ArrayList<String>> formatRelations(Matrix matrix, Node.Type type) {
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();		
		for (int i = 0; i < matrix.getNRows(); ++i) {
			HashMap<Integer, Float> aRow = new HashMap<Integer, Float>();
			aRow = matrix.getRow(i);
			for(Map.Entry<Integer, Float> entry: aRow.entrySet()){
				if(entry.getValue() != 0.0f){
					ArrayList<String> col = new ArrayList<String>();
					col.addAll(formatNode(i, graph.getNode(i, type)));
					int j = entry.getKey();
					col.addAll(formatNode(j, graph.getNode(j, Node.Type.Paper)));
					ret.add(col);
				}
			}
		}
		
		return ret;
	}
	
	private ArrayList<ArrayList<String>> formatNodeRelations(Matrix matrix, Integer index){
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		HashMap<Integer, Float> aRow = new HashMap<Integer, Float>();
		aRow = matrix.getRow(index);
		for(Map.Entry<Integer, Float> entry: aRow.entrySet()){
			if(entry.getValue() != 0.0f){
				ArrayList<String> col = new ArrayList<String>();
				int j = entry.getKey();
				col.addAll(formatNode(j, graph.getNode(j, Node.Type.Paper)));
				ret.add(col);
			}
		}
		return ret;
	}
	//USED FOR PAPERS
	private ArrayList<ArrayList<String>> formatNodeRelations(Matrix matrixA, Matrix matrixT, Matrix matrixC, Integer index){
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		Matrix[] matrixArray = new Matrix[]{ matrixA, matrixT, matrixC};
		Node.Type[] typeArray = new Node.Type[]{Node.Type.Autor, Node.Type.Terme, Node.Type.Conferencia};
		//We are iterating over the three matrix to get all the paper's relations with other node types
		for(int t = 0; t < 3; ++t){
			Matrix matrix = matrixArray[t];
			Node.Type nodeType = typeArray[t];
			for (int i = 0; i < matrix.getNRows(); ++i) {
				if(matrix.getValue(i, index) != 0.0f){
					ArrayList<String> col = new ArrayList<String>();
					col.addAll(formatNode(i, graph.getNode(i, nodeType)));
					ret.add(col);
				}
			}
		}
		return ret;
	}

	
	


	
	/**
	* Returns an ArrayList of strings with all the information regarding the <b><i>node</i></b> associated with <b>index</b> and <b>nodeType</b>.
	* 
	* @param index the node's index the info of will be retrieved.
	* @param nodeType the node's type the info of will be retrieved.
	* @return
	* Returns an arrayList of strings containing:<br><br>
	* 	<ol>
	*		<li>The <b><i>node</i></b>'s <b>Index</b></li>
	*		<li>The <b><i>node</i></b>'s <b>Name</b></li>
	*		<li>The <b><i>node</i></b>'s <b>Type</b></li>
	*		<li>The <b><i>node</i></b>'s <b>Label</b></li>
	*	</ol>	      
	*/
	public ArrayList<String> getNodeFormatted(Integer index, String nodeType){
		Node node = graph.getNode(index, Node.Type.valueOf(nodeType));
		return formatNode(index, node);
	}
	public String toString() {
		return graph.toString();
	}

}
