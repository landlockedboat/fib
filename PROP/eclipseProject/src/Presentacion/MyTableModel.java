package Presentacion;


import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import Dominio.CtrlResults;
import Presentacion.FormattedResult;

/**
 * @author Albert Lopez Alcacer
 */

class MyTableModel extends AbstractTableModel implements TableModelListener{
    
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Entity A",
            "Type",
            "Entity B",
            "Type",
            "Value",
    };
	private Class[] columns = new Class[]{String.class, String.class, String.class, String.class, Float.class};
	private FormattedResult result;
    private Object[][] data;
    private DefaultListModel<String> dlm;
    private JList<String> changes;
    private CtrlResults cr;
    Integer nChange = 0;
  
    /**
     * Creadora por defecto del modelo. Permite usar una lista en la que irá registrando (información)
     * los cambios que se realicen sobre el resultado result.
     * @param result: FormattedResult sobre el que se realizan los cambios.
     * @param changes: JList sobre la que se registran los cambios.
     */
    public MyTableModel(FormattedResult result, JList<String> changes) {
    	this.result = result;
    	this.changes = changes;
    	data = result.getResultData();
    	dlm = (DefaultListModel<String>) changes.getModel();
    	
    	for (int i = 0; i < result.getNumberOfValues(); ++i) {
    		dlm.addElement(i+")");
    	}
    	addTableModelListener(this);
    	
    }
    
    /**
     * Creadora que permite solamente mostrar el resultado.
     * @param result: FormattedResult mostrado
     */
    public MyTableModel(FormattedResult result) {
    	this.result = result;
    	data = result.getResultData();
    	
    }
    
    /**
     * Método que permite el número de columnas de la tabla asociada
     * @return int: Numero de columnas
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Método que permite obtener el número de filas de la tabla asociada
     * @return int: Numero de filas
     */
    public int getRowCount() {
        return data.length;
    }
    
    /**
     * Método que permite devolver el nombre de una cierta columna
     * @return String: Nombre de la columna.
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    /**
     * Métdodo que permite obtener el Object de una cierta posición
     * de la tabla.
     * @return Object:Objeto de la posición.
     */
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    
    /**
     * Método que permite saber si una celda es editable.
     * @return boolean: Booleano que indica cierto si la celda es editable
     * ,falso en caso contrario. Solo la columna que contiene el valor de 
     * HeteSim será editable.
     */
    public boolean isCellEditable(int row, int col) {
    	
    	return col == 4; 
    	
    }
    
    /**
     * Método que devuelve el tipo de objeto de una columna determinada
     */
    @Override
    public Class getColumnClass(int column) {
		return columns[column];
    	
    }
    
    /**
     * Función que permite establecer el valor de una posicion determinada
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    
	/**
	 * Función que permite borrar los cambios de la tabla
	 */
	public void clearListChanges() {
		changes.removeAll();
	}
	
	/**
	 * Función que salta cuando detecta un cambio en la tabla, para 
	 * añadir a la lista de cambios el registro correspondiente.	
	 */
	@Override
	public void tableChanged(TableModelEvent e) {
		Integer row = e.getFirstRow();
        Integer column = e.getColumn();
        //System.out.println(Integer.toString(row)+" "+Integer.toString(column));
        TableModel model = (TableModel)e.getSource();
        Float data = (Float) model.getValueAt(row, column);
        
        if (data >= 0 && data <= 1) {
	        nChange = row;
	        
	        result.setNewValue(row, data);
	    	Float oldValue = result.getOldValue(row);
	    	Float newValue = result.getNewValue(row);
	    	String ch = row+")"+" [Old value: "+ oldValue +", New value: "+ newValue +"]";
	    	
	    	dlm.set(row, ch);
	    	
	        //System.out.println("Change");
        }
        else {
        	String[] ok = {"Ok"};
			VistaDialog.setDialog("Change", "Only values between 0 and 1 are accepted\n ", ok, VistaDialog.DialogType.ERROR_MESSAGE);
        }
		
	}

}