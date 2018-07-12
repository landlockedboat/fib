/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Alejandro Ibanyez
 */
public class Matrix implements Serializable {
    private ArrayList< HashMap<Integer,Float> > m = new ArrayList< HashMap<Integer,Float> >();
    private final float valArc = 1.0f;
    private int nPapers = 0;
    private static final long serialVersionUID = 1L;
    
    
    public void afegirArc(int id1,int id2) {
        m.get(id1).put(id2, valArc);
    }
    
    public void esborrarArc(int id1,int id2) {
        m.get(id1).remove(id2);
    }
    
    public boolean existeixArc(int id1,int id2) {
        return (m.get(id1).containsKey(id2));
    }
    
    public void addNode() {
        HashMap<Integer,Float> n = new HashMap<Integer,Float>();
        m.add(n);
    }

    public Matrix trasposarMatriu(){
        Matrix asd = new Matrix();
        return asd;
    }

    public Integer getNCols(int index){
        return m.get(index).size();
    }

    public Integer getNRows(){
        return m.size();
    }

    public Float getValue(int id1, int id2){
        if (m.get(id1).containsKey(id2)) return m.get(id1).get(id2);
        else return 0.0f;
    }

    public HashMap<Integer,Float> getRow(int i){
        return m.get(i);
    }
    
    public void addNFiles(int num) {
    	while (m.size() < num) m.add(new HashMap<Integer,Float>());
    }

    public void setNFiles(int f){
        m = new ArrayList <HashMap<Integer,Float>>();
        addNFiles(f);
    }
   
    public void setRelevance(int id1,int id2,float r) {
        m.get(id1).put(id2, r);
    }

    public void borraFila(int index) {
       Iterator<Map.Entry<Integer,Float>> iter = m.get(index).entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,Float> entry = iter.next();
            iter.remove();
        }
    }

    public void borraCol(int index) {
        for (int i = 0; i < m.size(); i++) {
            if (m.get(i).containsKey(index)) m.get(i).remove(index);
        }
    }
    
    public void addPapers() {
    	++nPapers;
    }
    
    public int getNCols() {
    	return nPapers;
    }
    
    public void setNCols(int c) {
    	nPapers = c;
    }
    
    public void setTamany(int fils,int cols) {
    	setNFiles(fils);
    	setNCols(cols);
    }
    
    public ArrayList<HashMap<Integer,Float> > retornarM() {
    	return m;
    }
}
