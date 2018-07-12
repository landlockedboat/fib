package Dominio;

import java.util.HashMap;


/**
 * 
 * @author Gonzalo Diez
 * 
 */

	
public class SparseVector extends HashMap<Integer,Float> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The multiplication of two vectors
	 * @param sv1 SparseVector that is going to be multiplied for the other SparseVector
	 * @param sv2 SparseVector that is going to be multiplied for the other SparseVector
	 * @return The result of the multiplication of sv1 and sv2
	 */
	static Float multiply(SparseVector sv1, SparseVector sv2) {
		Float ret = 0.f;
		
		if (sv1.keySet().size() < sv2.keySet().size()) {		
			for (Integer k : sv1.keySet()) if (sv2.containsKey(k)) ret += sv1.get(k) * sv2.get(k);
		}
		else {
			for (Integer k : sv2.keySet()) if (sv1.containsKey(k)) ret += sv1.get(k) * sv2.get(k);
		}
		
		return ret;
	}	
	
	/**
	 * The norm of a vector
	 * @return the norm of a vector
	 */
	float norm() {
		double total = 0.0;
		for (Integer i : keySet()) {
			total += Math.pow(get(i), 2);
		}
		return (float) Math.sqrt(total);
	}

}