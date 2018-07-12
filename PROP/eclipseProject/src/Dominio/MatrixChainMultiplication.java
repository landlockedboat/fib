package Dominio;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Gonzalo Diez
 * 
 */

public class MatrixChainMultiplication {
	static private List<List<Long>> m;
	static private List<List<Integer>> s;
	
	static SparseMatrix compute(ArrayList<SparseMatrix> ms) {
		m = new ArrayList<List<Long> >(ms.size());
		for (int i = 0; i < ms.size(); ++i) {
			m.add(new ArrayList<Long>(ms.size()));
			for (int j = 0; j < ms.size();++j) {
				m.get(i).add(null);
			}
		}
		s = new ArrayList<List<Integer>>(ms.size());
		for (int i = 0; i < ms.size(); ++i) {
			s.add(new ArrayList<Integer>(ms.size()));
			for (int j = 0; j < ms.size();++j) {
				s.get(i).add(null);
			}
		}
		for (int j = 0; j < ms.size();++j) {
			m.get(j).set(j,0L);
		}
		
		for (int l = 2; l <= ms.size(); ++l) {
			for (int i = 1; i <= ms.size() - l + 1; ++i) {
				int j = i + l - 1;
				m.get(i-1).set(j-1, Long.MAX_VALUE);
				for (int k = i; k <= j-1; ++k) {
					long q = m.get(i-1).get(k-1) + m.get(k+1-1).get(j-1) + ms.get(i-1).getNRows() * ms.get(k-1).getNCols() * ms.get(j-1).getNCols();
					if (q < m.get(i-1).get(j-1)) {
						m.get(i-1).set(j-1, q);
						s.get(i-1).set(j-1, k-1);
					}
				}
			}
		}		
		
		return mult(ms, 0, ms.size()-1);
	}

	private static SparseMatrix mult(ArrayList<SparseMatrix> ms, int i, int j) {
		if (i == j) return ms.get(i);
		int k = s.get(i).get(j);
		SparseMatrix left = mult(ms,i,k);
		SparseMatrix right = mult(ms,k+1, j);
		return SparseMatrix.multiply(left, right);
	}
	
}
