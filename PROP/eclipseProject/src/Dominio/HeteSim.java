//package Dominio;
//
//
////DEPRECATED
////DEPRECATED
////DEPRECATED
////DEPRECATED
////DEPRECATED
////DEPRECATED
////DEPRECATED
////DEPRECATED
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//
///**
// * 
// * @author Gonzalo Diez
// * 
// */
//
//class HeteSim {	
//	class Partite {
//		Matrix leftToMid;
//		Matrix midToRight;
//		Partite(Matrix f,Matrix s){
//			leftToMid = f;
//			midToRight = s;
//		}
//	}
//	
//	class WhatMatrix {
//		boolean transposeMatrix;
//		HeteSim.PathTypes pathType;
//		WhatMatrix(boolean trans, HeteSim.PathTypes t) {
//			this.transposeMatrix = trans;
//			this.pathType = t;
//		}
//	}
//	
//	
//	
//	private boolean paperAuthor;
//	private Matrix paper2author;
//	private Matrix author2paper;
//	private boolean paperConf;
//	private Matrix paper2conf;
//	private Matrix conf2paper;
//	private boolean paperTerm;
//	private Matrix paper2term;
//	private Matrix term2paper;
//	private boolean authorMid;
//	private Matrix author2mid;
//	private Matrix paper2authorMid;
//	private boolean confMid;
//	private Matrix conf2mid;
//	private Matrix paper2confMid;
//	private boolean termMid;
//	private Matrix term2mid;
//	private Matrix paper2termMid;
//	
//	private Graph graph;
//	
//	private enum PathTypes {
//		Author2Paper, Conf2Paper, Term2Paper, 
//		Author2Mid, Paper2MidAut,
//		Conf2Mid, Paper2MidConf,
//		Term2Mid, Paper2MidTerm
//	}
//	
//	public HeteSim() {
//		graph = null;
//	}
//	
//	private Matrix multiply(Matrix leftSide, Matrix rightSide) {
//		Matrix result = new Matrix();
//		result.setTamany(leftSide.getNRows(), rightSide.getNCols());
//		for (int i = 0; i < leftSide.getNRows(); ++i) {
//			for (int j = 0; j < rightSide.getNCols(); ++j) {
//				result.getRow(i).set(j,multiplyVectors(leftSide.getRow(i),rightSide.getCol(j)));
//			}
//		}
//		return result;
//	}
//	
//	private Float multiplyVectors(ArrayList<Float> v1, ArrayList<Float> v2) {
//		if (v1.size() != v2.size()) { throw new RuntimeException("The vectors size are mismatched");}
//		Float total = 0.f;
//		for (int i = 0; i < v1.size();++i) {
//			total += v1.get(i) * v2.get(i);
//		}
//		return total;
//	}
//	
//	private Matrix normaliceRows(Matrix m) {
//		Matrix result = new Matrix();
//		result.copiaTamany(m);
//		
//		for (int i = 0; i < m.getNRows(); ++i) {
//			Double total = 0.0;
//			for (int j = 0; j < m.getNCols(); ++j) {
//				total += Math.pow(m.getRow(i).get(j),2);
//			}
//			total = Math.sqrt(total);
//			for (int j = 0; j < m.getNCols(); ++j) {
//				result.getRow(i).set(j,(float) (m.getRow(i).get(j)/total));
//			}
//		}
//		return result;
//	}
//	
//	private float norm(ArrayList<Float> v) {
//		Float total = 0.f;
//		for (int i = 0; i < v.size();++i) {
//			total += (float) Math.pow(v.get(i), 2);
//		}
//		
//		return (float) Math.sqrt(total);
//	}
//	
//	/**
//	 *  
//	 * @param left is the multiplications of the U matrix from S to M (PMpl)
//	 * @param right is the trasposarMatriu of the multiplications of the U matrix from T to M (PMpr-1') (actually, is not trasposed, because row*row instead row*col)
//	 * @return Matrix of Hetesims
//	 */
//	private Matrix normaliceHeteSim(Matrix left, Matrix right) {
//		Matrix result = new Matrix();
//		result.setTamany(left.getNRows(),right.getNRows());
//		
//		for (int i = 0; i < result.getNRows(); ++i) {
//			for (int j = 0; j < result.getNCols(); ++j) {
//				double top = multiplyVectors(left.getRow(i),right.getRow(j));
//				double bot = norm(left.getRow(i))*norm(right.getRow(j));
//				Float res;
//				if (bot == 0) res = 0.f;
//				else res = (float) (top/bot);
//				result.getRow(i).set(j, (float) (Math.floor(res*1e5)/1e5));
//				
//			}
//		}
//		
//		return result;
//	}
//	
//	// POST ESPECIALIZATION
//	
//	public void setGraph(Graph g) {
//		graph = g;
//		paperAuthor = paperConf = paperTerm = authorMid = confMid = termMid = false;
//	}
//	
//	public Matrix getHeteSim(Path p) throws PathException {
//		ArrayList<Node.Type> left = null;
//		ArrayList<Node.Type> right = null;
//		Pair<ArrayList<Node.Type>, ArrayList<Node.Type>> aux = p.getPath();
//		left = aux.first;
//		right = aux.second;
//		if (left.size() < 2 || right.size() < 2) throw new PathException("The path is too short");
//		Collections.reverse(right);
//		return normaliceHeteSim(mutiplyMatrixes(getMatrixesToMultiply(left,right)),mutiplyMatrixes(getMatrixesToMultiply(right,left)));
//	}
//	
//	public ArrayList<Pair<Integer,Float>> getHeteSim(Path p, Node n) throws PathException {
//		ArrayList<Node.Type> left = null;
//		ArrayList<Node.Type> right = null;
//		Pair<ArrayList<Node.Type>, ArrayList<Node.Type>> aux = p.getPath();
//		left = aux.first;
//		right = aux.second;
//		if (left.size() < 2 || right.size() < 2) throw new PathException("The path is too short");
//		if (n.getTipus() != p.getContingut().get(0)) throw new RuntimeException("The first node is not of the same type that the path");
//		Collections.reverse(right);
//		Matrix hete = normaliceHeteSim(multiplyVectorMatrix(n,getMatrixesToMultiply(left,right)),mutiplyMatrixes(getMatrixesToMultiply(right,left)));
//		ArrayList<Pair<Integer,Float>> ret = new ArrayList<Pair<Integer,Float>>();
//		if (hete.getNRows() != 1) {
//			throw new RuntimeException("getHeteSim(Path p, Node n), el resultado no tiene un solo arraylist. Baia");
//		}
//		for (int i = 0; i < hete.getNCols(); ++i) {
//			if (!hete.getRow(0).get(i).equals(0.0f)) {
//				ret.add(new Pair<Integer, Float>(i, hete.getRow(0).get(i)));
//			}
//		}
//		return ret;
//	}
//	
//	public Float getHeteSim(Path p, Node n1, Node n2) throws PathException {
//		ArrayList<Node.Type> left, right;
//		Pair<ArrayList<Node.Type>, ArrayList<Node.Type>> aux = p.getPath();
//		left = aux.first;
//		right = aux.second;
//		if (n1.getTipus() != p.getContingut().get(0)) throw new RuntimeException("The first node is not of the same type that the path");
//		if (n2.getTipus() != p.getContingut().get(p.getContingut().size()-1)) throw new RuntimeException("The second node is not of the same type that the path");
//		if (left.size() < 2 || right.size() < 2) throw new PathException("The path is too short");
//		Collections.reverse(right);
//		return normaliceHeteSim(multiplyVectorMatrix(n1, getMatrixesToMultiply(left,right)),multiplyVectorMatrix(n2, getMatrixesToMultiply(right,left))).getValue(0,0);
//	}
//	
//	// Private Metods
//	
//	private Matrix arrayListToMatrix(ArrayList<Float> a) {
//		Matrix ret = new Matrix();
//		ret.setTamany(1, a.size());
//		for (int i = 0; i < a.size(); ++i) {
//			ret.getRow(0).set(i, a.get(i));
//		}
//		return ret;
//	}
//	
//	private Matrix multiplyVectorMatrix(Node n, ArrayList<Matrix> matrixesToMultiply) {
//		Matrix ret = arrayListToMatrix(matrixesToMultiply.get(0).getRow(n.id));
//		for (int i = 1; i < matrixesToMultiply.size(); ++i) {
//			ret = multiply(ret,matrixesToMultiply.get(i));
//		}
//		return ret;
//	}
//	
//	private Matrix mutiplyMatrixes(ArrayList<Matrix> matrixesToMultiply) {
//		Matrix ret = matrixesToMultiply.get(0);
//		for (int i = 1; i < matrixesToMultiply.size(); ++i) {
//			ret = multiply(ret,matrixesToMultiply.get(i));
//		}
//		return ret;
//	}
//	
//
//	
//	/**
//	 * 
//	 * @param path
//	 * @param aux If path finish with E and the Node.Type of before is Paper, we dont know what matrix chose. We need to know the full path
//	 * @return
//	 * @throws PathException 
//	 */
//	
//	private ArrayList<Matrix> getMatrixesToMultiply(ArrayList<Node.Type> path,ArrayList<Node.Type> aux) throws PathException {
//		ArrayList<Matrix> matrixesToMultiply = new ArrayList<Matrix>();
//		ArrayList<WhatMatrix> whatMatrixes = getPairs(path, aux);
//		for (int i = 0; i < whatMatrixes.size(); ++i) {
//			WhatMatrix w = whatMatrixes.get(i);
//			switch (w.pathType) {
//			case Author2Paper:
//				if (!paperAuthor) { // init paper2Author
//					this.author2paper = normaliceRows(graph.getMatrixAuthor());
//					this.paper2author = normaliceRows(graph.getMatrixAuthor().trasposarMatriu());
//					this.paperAuthor = true;
//				}
//				if (w.transposeMatrix) matrixesToMultiply.add(paper2author);
//				else matrixesToMultiply.add(author2paper);
//				break;
//			case Conf2Paper:
//				if (!paperConf) { // init paper2Author
//					this.conf2paper = normaliceRows(graph.getMatrixConf());
//					this.paper2conf = normaliceRows(graph.getMatrixConf().trasposarMatriu());
//					this.paperConf = true;
//				}
//				if (w.transposeMatrix) matrixesToMultiply.add(paper2conf);
//				else matrixesToMultiply.add(conf2paper);
//				break;
//			case Term2Paper:
//				if (!paperTerm) { // init paper2Author
//					this.term2paper = normaliceRows(graph.getMatrixTerm());
//					this.paper2term = normaliceRows(graph.getMatrixTerm().trasposarMatriu());
//					this.paperTerm = true;
//				}
//				if (w.transposeMatrix) matrixesToMultiply.add(paper2term);
//				else matrixesToMultiply.add(term2paper);
//				break;
//			case Author2Mid:
//			case Paper2MidAut:
//				if (!authorMid) {
//					Partite p = partiteMatrix(graph.getMatrixAuthor());
//					this.author2mid = normaliceRows(p.leftToMid);
//					this.paper2authorMid = normaliceRows(p.midToRight.trasposarMatriu());
//					authorMid = true;
//				}
//				if (w.pathType == PathTypes.Author2Mid) matrixesToMultiply.add(author2mid);
//				else matrixesToMultiply.add(paper2authorMid);
//				break;
//			case Conf2Mid:
//			case Paper2MidConf:
//				if (!confMid) {
//					Partite p = partiteMatrix(graph.getMatrixConf());
//					this.conf2mid = normaliceRows(p.leftToMid);
//					this.paper2confMid = normaliceRows(p.midToRight.trasposarMatriu());
//					confMid = true;
//				}
//				if (w.pathType == PathTypes.Conf2Mid) matrixesToMultiply.add(conf2mid);
//				else matrixesToMultiply.add(paper2confMid);
//				break;
//			case Term2Mid:
//			case Paper2MidTerm:
//				if (!termMid) {
//					Partite p = partiteMatrix(graph.getMatrixTerm());
//					this.term2mid = normaliceRows(p.leftToMid);
//					this.paper2termMid = normaliceRows(p.midToRight.trasposarMatriu());
//					termMid = true;
//				}
//				if (w.pathType == PathTypes.Term2Mid) matrixesToMultiply.add(term2mid);
//				else matrixesToMultiply.add(paper2termMid);
//				break;
//			}
//		}
//		return matrixesToMultiply;
//	}
//
//	private Partite partiteMatrix(Matrix matrix) {
//		Matrix thingA2Mid = new Matrix();
//		Matrix mid2ThingB = new Matrix();
//		int total = 0;
//		for (int i = 0; i < matrix.getNRows(); ++i) {
//			for (int j = 0; j < matrix.getNCols(); ++j) {
//				total += Math.round((Float) matrix.getValue(i, j));	// Useless cast			
//			}
//		}
//		thingA2Mid.setTamany(matrix.getNRows(), total);
//		mid2ThingB.setTamany(total, matrix.getNCols());
//		int index = 0;
//		for (int i = 0; i < matrix.getNRows(); ++i) {
//			for (int j = 0; j < matrix.getNCols(); ++j) {
//				if ((float) matrix.getValue(i, j) == 1.f) { // Useless cast
//					thingA2Mid.getRow(i).set(index,1.f);
//					mid2ThingB.getRow(index).set(j, 1.f);
//					index += 1;
//				}
//			}
//		}
//		return new Partite(thingA2Mid,mid2ThingB);
//	}
//
//	private ArrayList<WhatMatrix> getPairs(ArrayList<Node.Type> path, ArrayList<Node.Type> aux) throws PathException {
//		ArrayList<WhatMatrix> ret = new ArrayList<WhatMatrix>();
//		for (int i = 1; i < path.size(); ++i) {
//			Node.Type last = path.get(i-1);
//			Node.Type current = path.get(i);
//			if (current == Node.Type.MidElement) {
//				if (last == Node.Type.Paper) {
//					Node.Type nextToMid = aux.get(aux.size()-2);
//					switch (nextToMid) {
//						case Autor:
//							ret.add(new WhatMatrix(false,PathTypes.Paper2MidAut));
//							break;
//						case Conferencia:
//							ret.add(new WhatMatrix(false,PathTypes.Paper2MidConf));
//							break;
//						case Terme:
//							ret.add(new WhatMatrix(false,PathTypes.Paper2MidTerm));
//							break;
//						default:
//							System.out.println("The path is broken. The Mid element is linking Paper with Paper or another MidElement :(");
//							System.out.println(path);
//							System.out.println(aux);
//							throw new PathException("The path have a midElement linked to two papers or to another midElement");
////							System.exit(-1);
//					}
//				}
//				else {
//					switch (last) {
//						case Autor:
//							ret.add(new WhatMatrix(false,PathTypes.Author2Mid));
//							break;
//						case Conferencia:
//							ret.add(new WhatMatrix(false,PathTypes.Conf2Mid));
//							break;
//						case Terme:
//							ret.add(new WhatMatrix(false,PathTypes.Term2Mid));
//							break;
//						default:
//							/* Throw exception: The function is broken or the path is broken */
//							System.out.println("Maybe you dont know how to code, or maybe the path is broken. IoKze, no soi 100tifico :(");
//							System.out.println(path);
//							System.out.println(last);
//							throw new PathException("The path have a midElement linked to another midElement");
////							System.exit(-1);
//					}
//				}
//			}
//			else {
//				boolean trans = false;
//				if (last == Node.Type.Paper) { //Swap
//					Node.Type ntAux = last;
//					last = current;
//					current = ntAux;
//					trans = true;
//				}
//				if (current != Node.Type.Paper) throw new PathException("The path cant navigate throgh the graph (need to go throgh paper)");
//				switch (last) {
//					case Autor:
//						ret.add(new WhatMatrix(trans,PathTypes.Author2Paper));
//						break;
//					case Conferencia:
//						ret.add(new WhatMatrix(trans,PathTypes.Conf2Paper));
//						break;
//					case Terme:
//						ret.add(new WhatMatrix(trans,PathTypes.Term2Paper));
//						break;
//					default:
//						/* Throw exception: The function is broken or the path is broken */
//						System.out.println("Two papers together :(");
//						System.out.println(path);
//						System.out.println(current);
//						System.out.println(last);
//						throw new PathException("The path have two papers together");
////						System.exit(-1);
//				}
//			}
//		}
//
//		return ret;
//	}
//	
//}
