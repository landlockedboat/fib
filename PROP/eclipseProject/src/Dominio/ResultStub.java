package Dominio;

import java.io.Serializable;
import java.util.ArrayList;

//Stub class used to check if CtrlData successfully Deep copies Results
//So it only contains methods which allow you to construct a full Result 
public class ResultStub implements Serializable {
	
	private Node firstN; //Search origin node
	private Node lastN; //Search destination node
	private Path usedP; //Search Path

	private Float threshold; //Lowest heteSim value which will be displayed on screen 
	
	private String idResult; //Unique result id
	private String idGraph;	//Id of the graph associated to this result
	private Boolean modified; //When true: The result or the graph have been manually edited and they will probably not be coherent
	
	private ArrayList<NodePair> resultList; //Hetesim results, structured in NodePairs and (hopefully) sorted by Hetesim values
	
	public ResultStub (String idResult) {
		this.idResult = idResult;
	}
	
	public void setresultList (ArrayList<NodePair> resultList) {
		this.resultList = resultList;
	}
	
	public ArrayList<NodePair> getresultList() {
		return resultList;
	}
	public void setOriginDest (Node orig, Node dest) {
		this.firstN = orig;
		this.lastN = dest;
	}
	public Node getFirstN() {
		return firstN;
	}
	public Node getLastN() {
		return lastN;
	}
	
	public void setPathUsed (Path p) {
		this.usedP = p;
	}
	
	public void setThreshold (Float f) {
		this.threshold = f;
	}
	
	public void setidGraph (String idGraph) {
		this.idGraph = idGraph;
	}
	public void setModified (Boolean modified) {
		this.modified = modified;
	}
	
	public String toString(){
		String retStr = new String();
		retStr = ("Resultado: " + idResult + "\n"); 
		retStr = retStr + ("    Path: " + usedP.toString() + "\n");
		if (firstN != null) {
			retStr = retStr + ("    N1) nom: " + firstN.getNom() +" Id: "+firstN.getId()+ "  Type: "+firstN.getTipus().toString() + "  Label: "+ firstN.getLabel().toString()+"\n");
		}
		else retStr = retStr + ("    N1) nom: " + "NULL" + "  Type: "+"NULL" + "  Label: "+"NULL"+"\n");
		if (lastN != null) {
			retStr = retStr + ("    N2) nom: " + lastN.getNom() +" Id: "+lastN.getId()+ "  Type: "+lastN.getTipus().toString() + "  Label: "+ lastN.getLabel().toString()+"\n");
		}
		else retStr = retStr + ("    N2) nom: " + "NULL" + "  Type: "+"NULL" + "  Label: "+"NULL"+"\n");
		
		retStr = retStr + ("    Threshold: " + threshold + "\n");
		retStr = retStr + ("\n");
		int i = 0;
		//System.out.println("Tamany resultat: "+resultList.size());
		while (i < resultList.size()){
			retStr = retStr + "    " + resultList.get(i).toString() + "\n";
			++i;
		}
		
		return retStr; 
	}
}
