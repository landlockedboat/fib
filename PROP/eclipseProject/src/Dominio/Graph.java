package Dominio;


import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author chus
 */
public class Graph implements Serializable {
    
    //Atributos
    
    String nom;   
    int id;
    private Matrix autorPaper;
    private Matrix temaPaper;
    private Matrix confPaper;
    private ArrayList<Node> autors;
    private ArrayList<Node> conferencies;
    private ArrayList<Node> papers;
    private ArrayList<Node> termes;
    private static final long serialVersionUID = 1L;
    //Metodos
    
    public Graph(String nom, int id) {
        this.nom = nom;
        this.id = id;
        autorPaper = new Matrix();
        temaPaper = new Matrix();
        confPaper = new Matrix();
        autors = new ArrayList<Node>(); 
        conferencies = new ArrayList<Node>();
        papers = new ArrayList<Node>();
        termes = new ArrayList<Node>();
    }   
    
    public Graph() {
        autorPaper = new Matrix();
        temaPaper = new Matrix();
        confPaper = new Matrix();
        autors = new ArrayList<Node>(); 
        conferencies = new ArrayList<Node>();
        papers = new ArrayList<Node>();
        termes = new ArrayList<Node>();
    }
    
    public String getNom() {
        return nom;
    }
     
    public void setNom(String s) {
        this.nom = s;
    }
    
    public int addNode(Node.Type tipus, String nom) {
        Node aux = new Node();
        int res;
        switch(tipus) {
            case Autor: 
                res = autors.size();
                aux.initialize(tipus,res,nom);
                autors.add(aux);
                autorPaper.addNode();
                break;
            case Conferencia:
                res = conferencies.size();
                aux.initialize(tipus,res,nom);
                conferencies.add(aux);
                confPaper.addNode();
                break;
            case Paper:
                res = papers.size();
                aux.initialize(tipus,res,nom);
                papers.add(aux);
                autorPaper.addPapers();
                confPaper.addPapers();
                temaPaper.addPapers();
                break;
            case Terme:
                res = termes.size();
                aux.initialize(tipus,res,nom);
                termes.add(aux);
                temaPaper.addNode();
                break;
            default: 
                //System.out.println ("Tipo incorrecto");
                res = -1;
                break;
        }
        return res;
    }
    
    public void addLabel(int index, Node.Label label, Node.Type tipus) {
        if (label == Node.Label.AI || label == Node.Label.DataMining || label == Node.Label.Database || label == Node.Label.InformationRetrieval) {
            Node aux;
            switch(tipus) {
                case Autor: aux = autors.get(index);
                    if (aux != null) autors.get(index).setLabel(label);
                    else {
                    	//System.out.println ("Nodo borrado");                    	
                    }
                    break; 
                case Conferencia: aux = conferencies.get(index);
                    if (aux != null) conferencies.get(index).setLabel(label);
                    else {
                    	//System.out.println ("Nodo borrado");
                    }
                    break;
                case Paper: aux = papers.get(index);
                    if (aux != null) papers.get(index).setLabel(label);
                    else {
                    	//System.out.println ("Nodo borrado");
                    }
                    break;
                default: 
                    //System.out.println ("Tipo incorrecto");
                    break;
            }
        }
        else {
        	//System.out.println ("Label incorrecta");
        }
    }
    
    public Integer existsNode(Node n) {
    	Integer i = 0;
        boolean trob = false;
        switch(n.getTipus()) {
            case Autor:         
                while (i < autors.size() && !trob) {
                    if (autors.get(i) != null) trob = (autors.get(i).equals(n));
                    if (!trob) ++i;
                }
                if (!trob) i = -1;
                return i;
            case Conferencia:
                while (i < conferencies.size() && !trob) { 
                    if (conferencies.get(i) != null) trob = (conferencies.get(i).equals(n));
                    if (!trob) ++i;
                }
                if (!trob) i = -1;
                return i;
            case Paper:
                while (i < papers.size() && !trob) {
                    if (papers.get(i) != null) trob = (papers.get(i).equals(n));
                    if (!trob) ++i;
                }
                if (!trob) i = -1;
                return i;
            case Terme: 
                while (i < termes.size() && !trob) {
                    if (termes.get(i) != null) trob = (termes.get(i).equals(n));
                    if (!trob) ++i;
                }
                if (!trob) i = -1;
                return i;
            default:
                //System.out.println ("Tipo incorrecto");
                return -1;            
        }
    }
    
    public Node getNode(int index, Node.Type tipus) {
        switch(tipus) {
            case Autor: if (autors.get(index) != null) return autors.get(index);
                else return null;
            case Conferencia: if (conferencies.get(index) != null) return conferencies.get(index);
                else return null;
            case Paper: if (papers.get(index) != null) return papers.get(index);
                else return null; 
            case Terme: if (termes.get(index) != null) return termes.get(index);
                else return null;
            default: 
                //System.out.println ("Tipo incorrecto");
                return null;
        }
    }
    
    public boolean existsArc(Node a, Node b) throws Exception {
        int i, j;
        j = existsNode(b);
        switch(a.getTipus()) {
            case Autor: 
                i = existsNode(a);
                if (i != -1 && j != -1) return autorPaper.existeixArc(i,j);
                else throw new RuntimeException("Graph::existsArc() : No existe alguno de los nodos");
            case Conferencia: 
                i = existsNode(a);
                if (i != -1 && j != -1) return confPaper.existeixArc(i,j);
                else throw new RuntimeException("Graph::existsArc() : No existe alguno de los nodos");
            case Terme: 
                i = existsNode(a);
                if (i != -1 && j != -1) return temaPaper.existeixArc(i,j);
                else throw new RuntimeException("Graph::existsArc() : No existe alguno de los nodos");
            default: 
                //System.out.println ("Tipo incorrecto");
                return false;
        }
    } 
    
    public void deleteNode(Node a) {
        int i = existsNode(a);
        if (i != -1) {
            switch(a.getTipus()) {
                case Autor:
                    autorPaper.borraFila(i);
                    autors.set(i,null);
                    break;
                case Conferencia:
                    confPaper.borraFila(i);
                    conferencies.set(i,null); 
                    break;
                case Paper: 
                    autorPaper.borraCol(i);
                    confPaper.borraCol(i);
                    temaPaper.borraCol(i);
                    papers.set(i,null);
                    break;
                case Terme: 
                    temaPaper.borraFila(i);
                    termes.set(i,null);
                    break;
                default: 
                    //System.out.println ("Tipo incorrecto");
                    break;
            }
        }
    }
    
    public void deleteArc(Node a, Node b) {
        int i, j;
        j = existsNode(b);
        switch(a.getTipus()) {
            case Autor: 
                i = existsNode(a);
                if (i != -1 && j != -1) autorPaper.esborrarArc(i,j);
                else {
                	//System.out.println ("No existeix el Node/s");
                }
                break;
            case Conferencia: 
                i = existsNode(a);
                if (i != -1 && j != -1) confPaper.esborrarArc(i,j);
                else {
                	//System.out.println ("No existeix el Node/s");
                }
                break;
            case Terme:
                i = existsNode(a);
                if (i != -1 && j != -1) temaPaper.esborrarArc(i,j);
                else {
                	//System.out.println ("No existeix el Node/s");
                }
                break;
            default:
                //System.out.println ("Tipo incorrecto");
                break;
        }
    }
    
    public void setNode(Node a, String s) {
        Integer aux;
        switch(a.getTipus()) {
            case Autor:
                aux = existsNode(a);
                //System.out.println("ojo cuiao");
                if (aux != -1) autors.get(aux).setNom(s);
                else {
                	//System.out.println("funca mal");
                }
                break;
            case Conferencia:
                aux = existsNode(a);
                if (aux != -1) conferencies.get(aux).setNom(s);
                break;
             case Paper:
                aux = existsNode(a);
                if (aux != -1) papers.get(aux).setNom(s);
                break;
            case Terme:
                aux = existsNode(a);
                if (aux != -1) termes.get(aux).setNom(s);
                break;
            default: 
                //System.out.println ("Tipo incorrecto");
                break;
        }
    }
    
    public void setArc(int indexpaper, int index2, Node.Type t) {
        switch(t) {
            case Autor: autorPaper.afegirArc(index2, indexpaper);
                break;
            case Conferencia: confPaper.afegirArc(index2, indexpaper); 
                break;
            case Terme: temaPaper.afegirArc(index2, indexpaper);
                break;
            default: 
                //System.out.println ("Tipo incorrecto");
                break;
        }
    }
    
    public Matrix getMatrixAuthor() {
        return autorPaper;
    }
    
    public Matrix getMatrixTerm() {
        return temaPaper;
    }
    
    public Matrix getMatrixConf() {
        return confPaper;
    }
    
    public ArrayList<Node> getAutors() {
        return autors;
    }
    
    public ArrayList<Node> getConferencies() {
        return conferencies;
    }
    
    public ArrayList<Node> getPapers() {
        return papers;
    }
    
    public ArrayList<Node> getTermes() {
        return termes;
    }
    
    public void escriuArray(Node.Type tipus) {
        switch(tipus) {
            case Autor: 
                for (int i = 0; i < autors.size(); ++i) {
                    if (autors.get(i) != null) {
                    	//System.out.println ("Autor " + i + ": " + autors.get(i).getNom() + "");
                    }
                    else {
                    	//System.out.println ("Autor " + i + ": esborrat");
                    }
                }
                break;
            case Conferencia: 
                for (int i = 0; i < conferencies.size(); ++i) { 
                    if (conferencies.get(i) != null) {
                    	//System.out.println ("Conferencia " + i + ": " + conferencies.get(i).getNom() + ""); 
                    }
                    else {
                    	//System.out.println ("Conferencia " + i + ": esborrada");
                    }
                }
                break;
            case Paper: 
                for (int i = 0; i < papers.size(); ++i) {
                    if (papers.get(i) != null) {
                    	//System.out.println ("Paper " + i + ": " + papers.get(i).getNom() + "");
                    }
                    else {
                    	//System.out.println ("Paper " + i + ": esborrat");
                    }
                }
                break;
            case Terme: 
                for (int i = 0; i < termes.size(); ++i) {
                    if (termes.get(i) != null) {
                    	//System.out.println ("Terme " + i + ": " + termes.get(i).getNom() + "");
                    }
                    else {
                    	//System.out.println ("Terme " + i + ": esborrat");
                    }
                }
                break;
            default: 
                //System.out.println ("Tipo incorrecto");
                break;
        }
    }
        
    public int numPapers() {
    	return papers.size();
    }
}
  