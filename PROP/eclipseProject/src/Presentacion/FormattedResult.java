package Presentacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import Dominio.CtrlDominio;
import Dominio.CtrlResults;
import Dominio.Pair;

/**
 * @author Albert Lopez Alcacer
**/

/*
 * Clase que permite extraer información de un resultado formateado (ArrayList)
 * y permite guardar las modificaciones de este resultado de manera que, comunicándose con
 * CtrlResults, puedan conservarse si así se desea.
 * 
 * Mediante los HashMaps: oldValues y newValues se mapean los posibles cambios al resultado.
 */

public class FormattedResult extends ArrayList<ArrayList<String>> {
	
	private static final long serialVersionUID = 1L;
	public static final Pair<Integer,Integer> resultIdPosition = new Pair<Integer,Integer>(0,0);
	public static final Pair<Integer,Integer> resultTypePosition = new Pair<Integer,Integer>(0,1);
	public static final Pair<Integer,Integer> searchPathPosition = new Pair<Integer,Integer>(0,2);
	public static final Pair<Integer,Integer> searchGraphIdPosition = new Pair<Integer,Integer>(0,3);
	public static final Pair<Integer,Integer> searchOriginNodePosition = new Pair<Integer, Integer>(0, 4);
	public static final Pair<Integer,Integer> searchDestinationNodePosition = new Pair<Integer, Integer>(0, 5);
	public static final Pair<Integer,Integer> searchTrhresholdPosition = new Pair<Integer, Integer>(0, 6);
	
	private String listedResult;
	private String idResult;
	private String resultType;
	private String searchPath;
	private String searchGraphId;
	private String resultThreshold;
	private String originNode;
	private String destinationNode;
	private Integer numberOfValues;
	private Integer numberOfColumns;
	private Object[][] resultData;
	private HashMap<Integer,Float> oldValues;
	private HashMap<Integer,Float> newValues;
	private CtrlResults cr;
	
	private ArrayList<Boolean> modifiedResultValues;
	
	/**
	 * Creadora por defecto que permite extraer la información relevante del resultado 
	 * formateado pasado por parámetro (ArrayList).
	 * @param res :  resultado
	 * @param cr : CtrlResults
	 */
	
	public FormattedResult(ArrayList<ArrayList<String>> res, CtrlResults cr) {
		
		this.addAll(res);
		this.cr = cr;
		resultType = get(resultTypePosition.first).get(resultTypePosition.second);
		searchPath = get(searchPathPosition.first).get(searchPathPosition.second);
		searchGraphId = get(searchGraphIdPosition.first).get(searchGraphIdPosition.second);
		idResult = get(resultIdPosition.first).get(resultIdPosition.second);
		resultThreshold = get(searchTrhresholdPosition.first).get(searchTrhresholdPosition.second);
		originNode = get(searchOriginNodePosition.first).get(searchOriginNodePosition.second);
		destinationNode = get(searchDestinationNodePosition.first).get(searchDestinationNodePosition.second);
		numberOfValues = size()-1;
		numberOfColumns = get(1).size();
		oldValues = new HashMap<Integer,Float>();
		newValues = new HashMap<Integer,Float>();
		
		modifiedResultValues = new ArrayList<Boolean>(numberOfValues);
		Collections.fill(modifiedResultValues, false);
		
		generateData();			
	}
	
	/**
	 * Función que permite extraer los valores correspondientes al resultado.
	 */
	private void generateData() {
		
		resultData = new Object[numberOfValues][numberOfColumns];
		
		for (int i = 1; i <= numberOfValues; ++i) {
			for (int j = 0; j < numberOfColumns; ++j) {
				if (j == numberOfColumns-1) resultData[i-1][j] = new Float(Float.parseFloat(get(i).get(j)));
				else resultData[i-1][j] = get(i).get(j);
			}
		}
	}
	
	/**
	 * Método que permite añadir a los oldValues (o valores actuales) el valor correspondiente
	 * al número de valor nValue
	 * 
	 * @param nValue: Integer que indica el número de valor del resultado.
	 * @param value: Valor actual
	 */
	public void setOldValue(int nValue, Float value) {
		if (nValue >= 0 && nValue <= numberOfValues) oldValues.put(nValue,value);
		else {
			//System.out.println("Intentas acceder a un valor inexistente");
		}
	}
	
	/**
	 * Método que permite obtener de los oldValues (o valores actuales) el valor correspondiente
	 * al número de valor nValue
	 * 
	 * @param nValue: Integer que indica el número de valor del resultado.
	 * @param value: Valor actual
	 */
	public Float getOldValue(int nValue) {
		if (oldValues.containsKey(nValue))
			return oldValues.get(nValue);
		else {
			//System.out.println("No existe el valor antiguo");
			return null;
		}
	}
	
	/**
	 * Método que permite establecer el nuevo valor de ese número de valor del resultado
	 * (un cambio indicado por el usuario).
	 * @param nValue: Número que hace referencia al numero de valor
	 * @param value Nuevo valor
	 */
	public void setNewValue(int nValue, Float value) {
		if (nValue >= 0 && nValue <= numberOfValues) newValues.put(nValue,value);
		else {
			//System.out.println("Intentas acceder a un indice de valor inexistente");
		}
	}
	
	/**
	 * Método que permite obtener de los newValues (o valores cambiados) el valor correspondiente
	 * al número de valor nValue
	 * 
	 * @param nValue: Integer que indica el número de valor del resultado a modificar.
	 * @param value: Valor nuevo
	 */
	public Float getNewValue(int nValue) {
		if (newValues.containsKey(nValue))
			return newValues.get(nValue);
		else {
			//System.out.println("No existe el valor nuevo");
			return null;
		}
	}
	
	/**
	 * Método que permite borrar los cambios
	 */
	public void clearChanges() {
		newValues.clear();
		oldValues.clear();
	}
	
	/**
	 * Función que permite obtener el id asociado al Resultado
	 * @return String: Id del resultado
	 */
	public String getIdResult() {
		return idResult;
	}
	
	/**
	 * Método que permite obtener el tipo de resultado
	 * @return String: Tipo de resultado
	 */
	public String getResultType() {
		return resultType;
	}
	
	/**
	 * Función que permite obtener el path asociado al resultado
	 * @return String: Path asociado al resultado
	 */
	public String getSearchPath() {
		return searchPath;
	}
	
	/**
	 * Método que permite obtener el Id del grafo asociado al resultado
	 * @return String: Id del grafo
	 */
	public String getSearchGraphId() {
		return searchGraphId;
	}
	
	/**
	 * Método que permite obtener el threshold asociado al resultado
	 * @return String: Valor Threshold
	 */
	public String getResultThreshold() {
		return resultThreshold;
	}
	
	/**
	 * Método que permite obtener nodo origen asociado al resultado
	 * @return String: Nombre nodo
	 */
	public String getResultOriginNode() {
		return originNode;
	}
	
	/**
	 * Método que permite obtener nodo destino asociado al resultado
	 * @return String: Nombre nodo
	 */
	public String getResultDestinationNode() {
		return destinationNode;
	}
	
	/**
	 * Método que te permite obtener el numero de valores del resultado
	 * @return Integer: Numero de valores.
	 */
	public int getNumberOfValues() {
		return numberOfValues;
	}
	
	/**
	 * Método que te permite obtener el número de columnas
	 * @return Integer: Número de columnas
	 */
	public int getNumberOfColumns() {
		return numberOfColumns;
	}
	
	/**
	 * Método que te permite obtener todos los valores del resultado
	 * @return Object array: valores del resultado
	 */
	public Object[][] getResultData() {
		return resultData;
	}
	
	/**
	 * Método que te permite guardar los cambios realizados en el resultado
	 */
	public void commitChanges() {
		for (Integer index: newValues.keySet()) {
			Float aux = newValues.get(index);
			cr.modifyResult(idResult, index, aux);
		}
		
	}
	
	@Deprecated
	public String getAllInfo() {
		listedResult = "Search Type: " + resultType + ", Search Path: ";
	    listedResult += searchPath + ", Associated Graph: " + searchGraphId;
	    return listedResult;
	}
	
}
