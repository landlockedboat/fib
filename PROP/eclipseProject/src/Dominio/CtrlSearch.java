package Dominio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import Persistencia.MatrixManager;

/**
 * 
 * @author Gonzalo Diez
 * 
 */

public class CtrlSearch {

	Graph g = null;
	HeteSanic het = new HeteSanic();
	MatrixManager mm;
	Matrix lastMatrix;
	String lastMatrixWanted;
	
	public void setGraph(Graph g) {
		this.g = g;
		het.setGraph(g);
		mm = new MatrixManager("matricesPrecalculadas/"+g.id);
		lastMatrixWanted = "";
	}

	public Result searchPathThreshhold(final Float threshold, final Path p) throws PathException {
		Matrix matrix = null;
		String matrixWanted = p.getStringPath()+g.id;
		try {
			if (lastMatrixWanted.equals(matrixWanted)) matrix = lastMatrix;
			else {
				matrix = mm.loadMatrix(matrixWanted);
				lastMatrix = matrix;
				lastMatrixWanted = matrixWanted;
			}
		}catch (ClassNotFoundException | IOException e) {
//			e.printStackTrace(); // Debug hasta saber que funciona
			//System.out.println("DEBUG: no ha podido cargar la matrix y la va a generar");
			matrix = het.getHeteSim(p);
		}
		return new Result(this.g, threshold, matrix, p);
	}

	public Result searchPath(final Path p) throws PathException {
		return searchPathThreshhold(0.f, p);
	}

	
	public Result searchPathNodeThreshhold(final Float threshold, final Path p, final Node n) throws PathException {
		ArrayList<Pair<Integer,Float>> result = new ArrayList<Pair<Integer,Float>>();
		String matrixWanted = p.getStringPath()+g.id;
		try {
			Matrix matrix;
			if (lastMatrixWanted.equals(matrixWanted)) matrix = lastMatrix;
			else {
				matrix = mm.loadMatrix(matrixWanted);
				lastMatrix = matrix;
				lastMatrixWanted = matrixWanted;
			}
			for (int i : matrix.getRow(0).keySet()) {
				result.add(new Pair<Integer, Float>(i, matrix.getRow(n.getId()).get(i)));
			}
		}catch (ClassNotFoundException | IOException e) {
//			e.printStackTrace(); // Debug hasta saber que funciona
			//System.out.println("DEBUG: no ha podido cargar la matrix y la va a generar");
			result = het.getHeteSim(p, n);
		}

		return new Result(this.g, threshold, result, p, n);
	}


	public Result searchPathNode(final Path p, final Node n) throws PathException {
		return searchPathNodeThreshhold(0.f, p, n);
	}

	// No hago load de disco porque seguramente vaya mas lento haciendolo que calculandolo del tiron
	public Result searchPathNodeNodeThreshhold(final Float threshold, final Path p, final Node n1,final Node n2) throws PathException {
		return new Result(this.g, threshold, het.getHeteSim(p, n1, n2), p, n1, n2);
	}


	public Result searchPathNodeNode(final Path p, final Node n1, final Node n2) throws PathException {
		return searchPathNodeNodeThreshhold(0.f, p, n1, n2);
	}
	
	// Para llamarlo desde presentacion
	public int precalculePath(String path)  {
		try {
			Matrix m = het.getHeteSim(new Path("","",path));
			mm.storeMatrix(m, path+g.id);
		} catch (PathException e) {
			e.printStackTrace();
			return -1; // Error: la vista tiene que avisar al usuario de que el path es incorrecto (o no dejarle hacerlo directamente)
		} catch (IOException e) {
			e.printStackTrace();
			return -2; // Error: no puede guardarse.
		}
		return 0;
	}

}
