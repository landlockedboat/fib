package Dominio;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * 
 * @author Xavier Peñalosa
 *
 */
public class formatedNodeComparator implements Comparator<ArrayList<String>>{
	public int compare(ArrayList<String> a1, ArrayList<String> a2){
		return a1.get(1).compareTo(a2.get(1));
	}
}
