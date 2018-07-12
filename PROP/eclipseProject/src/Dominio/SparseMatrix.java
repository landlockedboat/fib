package Dominio;

import java.util.ArrayList;

/**
 * 
 * @author Gonzalo Diez
 * 
 */

public class SparseMatrix {
	ArrayList<SparseVector> rows = new ArrayList<SparseVector>();
	ArrayList<SparseVector> cols = new ArrayList<SparseVector>();
	
	/**
	 * Copy the Matrix to a SparseMatrix
	 * @param matrix to be copied
	 */
	public SparseMatrix(Matrix matrix) { 
		int nCols = matrix.getNCols();
		int nRows = matrix.getNRows();
		for (int i = 0; i < nRows; ++i) {
			rows.add(new SparseVector());
		}
		for (int i = 0; i < nCols; ++i) {
			cols.add(new SparseVector());
		}
		
		for (int i = 0; i < matrix.getNRows(); ++i) {
			for (Integer j : matrix.getRow(i).keySet()) {
				set(i, j, matrix.getValue(i,j));
			}
		}
		
	}
	
	/**
	 * A SparseMatrix empty with number of rows equals nRows and numebr of cols equals nCols
	 * @param nRows number of rows
	 * @param nCols number of cols
	 */
	SparseMatrix(int nRows, int nCols) {
		for (int i = 0; i < nRows; ++i) {
			rows.add(new SparseVector());
		}
		for (int i = 0; i < nCols; ++i) {
			cols.add(new SparseVector());
		}
	}
	/**
	 * A SparseMatrix empty only with rows
	 * @param nRows number of rows
	 */
	SparseMatrix(int nRows) {
		for (int i = 0; i < nRows; ++i) {
			rows.add(new SparseVector());
		}
	}
	
	/**
	 * Copy Constructor
	 * @param sm
	 */
	SparseMatrix(SparseMatrix sm) {
		ArrayList<SparseVector> rows = sm.getRows();
		for (SparseVector sv : rows) {
			SparseVector row = new SparseVector();
			for (Integer k : sv.keySet()){
				row.put(k, sv.get(k));
			}
			this.rows.add(row);
		}
		
		ArrayList<SparseVector> cols = sm.getCols();
		for (SparseVector sv : cols) {
			SparseVector col = new SparseVector();
			for (Integer k : sv.keySet()){
				col.put(k, sv.get(k));
			}
			this.cols.add(col);
		}
	}
	/**
	 * Getter of cols
	 * @return cols
	 */
	private ArrayList<SparseVector> getCols() {
		return cols;
	}
	/**
	 * Getter of rows
	 * @return rows
	 */
	private ArrayList<SparseVector> getRows() {
		return rows;
	}
	
	/**
	 * Setting value at the position (row,col)
	 * @param row number of row
	 * @param col number of col
	 * @param value to be setted
	 */
	void set(int row, int col, Float value) {
		if (value == 0.f) {
			try {
				if (rows.get(row).containsKey(col)) {
					rows.get(row).remove(col);
					cols.get(col).remove(row);
				}
			}
			catch (IndexOutOfBoundsException e) {
				//System.out.println("Trying to set a position of the matrix that is outside the matrix");
				throw e;
			}
			return;
		}
		try {
			rows.get(row).put(col, value);
			cols.get(col).put(row, value);
		}
		catch (IndexOutOfBoundsException e) {
			while (row >= rows.size()) rows.add(new SparseVector());
			while (col >= cols.size()) cols.add(new SparseVector());
			
			rows.get(row).put(col, value);
			cols.get(col).put(row, value);
//			System.out.println("Trying to set a position of the matrix that is outside the matrix");
//			throw e;
		}
	}
	
	/**
	 * Setting value at the position (row,col) only in the rows
	 * @param row number of row
	 * @param col number of col
	 * @param value to be setted
	 */
	void setOnRow(int row, int col, Float value) {
		if (value == 0.f) {
			return;
		}
		try {
			rows.get(row).put(col, value);
		}
		catch (IndexOutOfBoundsException e) {
			while (row >= rows.size()) rows.add(new SparseVector());
			
			rows.get(row).put(col, value);
		}
	}
	
	/**
	 * Getting the number of rows
	 * @return the number of rows
	 */
	int getNRows() {
		return rows.size();
	}
	
	/**
	 * Getting the number of cols
	 * @return the number of cols
	 */
	int getNCols() {
		return cols.size();
	}
	
	/**
	 * Getting the row i-th
	 * @param i number of row
	 * @return the row number i
	 */
	public SparseVector getRow(int i) {
		return rows.get(i);
	}
	
	/**
	 * Getting the col j-th
	 * @param j number of col
	 * @return the col number j
	 */
	public SparseVector getCol(int j) {
		return cols.get(j);
	}
	/**
	 * Getting the value at the position (i,j)
	 * @param i number of row
	 * @param j number of col
	 * @return the value at the position (i,j)
	 */
	public Float getValue(int i, int j) {
		if (i < rows.size() && rows.get(i).containsKey(j)) return rows.get(i).get(j);
		else return 0.f;
	}
	
	/**
	 * Transposing the matrix
	 */
	public void transpose() {
		ArrayList<SparseVector> aux = rows;
		rows = cols;
		cols = aux;
	}
	
	/**
	 * Multiplaying two matrixs
	 * @param m1 left-side SparseMatrix
	 * @param m2 right-side SparseMatrix
	 * @return the result of the multiplication of two SparseMatrix
	 */
	static SparseMatrix multiply(SparseMatrix m1, SparseMatrix m2) {
		SparseMatrix ret = new SparseMatrix(m1.getNRows(), m2.getNCols());
		for (int i = 0; i < ret.getNRows(); ++i) {
			SparseVector v1 = m1.getRow(i);
			for (int j = 0; j < ret.getNCols(); ++j) {
				ret.set(i, j, SparseVector.multiply(v1, m2.getCol(j)));
//				System.out.println("m1.row: " + v1);
//				System.out.println("m2.col: " + m2.getRow(j));
//				System.out.println("M1\n" + m1);
//				System.out.println("M2\n" + m2);
//				System.out.println("ret\n" + ret);
			}
		}
		return ret;
	}
	
	/** 
	 * This shit returns a SparseMatrix that do not have Cols.
	 * <b>THIS CANT BE MULTIPLIED ON THE RIGHT SIDE</b>
	 * 
	 * @param m1 left-side SparseMatrix
	 * @param m2 right-side SparseMatrix
	 * @return the result of the multiplication of two SparseMatrix
	 */
	static SparseMatrix multiplyHalf(SparseMatrix m1, SparseMatrix m2) {
		SparseMatrix ret = new SparseMatrix(m1.getNRows());
		for (int i = 0; i < ret.getNRows(); ++i) {
			SparseVector v1 = m1.getRow(i);
			for (int j = 0; j < m2.getNCols(); ++j) {
				ret.setOnRow(i, j, SparseVector.multiply(v1, m2.getCol(j)));
			}
		}
		return ret;
	}
	
	/**
	 * Normalicing by rows
	 */
	void normaliceRows() {
		for (int i = 0; i < getNRows(); ++i) {
			Double total = 0.0;
			for (Integer j : rows.get(i).keySet()) {
				total += Math.pow(getValue(i,j),2);
			}
			total = Math.sqrt(total);
			for (Integer j : rows.get(i).keySet()) {
				set(i,j,(float) (getValue(i,j)/total));
			}
		}
	}

	/**
	 * Getting number of not zeros
	 * @return number of not zeros
	 */
	public int numberOfNotZeros() {
		int total = 0;
		
		for (SparseVector sv : rows) {
			total += sv.size();
		}
		
		return total;
	}

	/**
	 * Translates SparseMatrix to Matrix
	 * @return a Matrix with the same values than this
	 */
	public Matrix toMatrix() {
		Matrix ret = new Matrix();
		ret.setNFiles(rows.size());
		for (int i = 0; i < getNRows(); ++i) {
			for (Integer j : rows.get(i).keySet()) {
				ret.setRelevance(i, j, getValue(i,j));
			}
		}
		return ret;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = new String();
		
		for (int i = 0; i < getNRows(); ++i) {
			//System.out.println(i + " " + rows.get(i));
		}
		//System.out.print("Cols " + cols.size());
		return s;
	}
}