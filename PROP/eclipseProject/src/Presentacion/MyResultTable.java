package Presentacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;

import Dominio.CtrlResults;
import Presentacion.FormattedResult;
/**
 * @author Albert Lopez Alcacer
**/

public class MyResultTable extends JTable implements ListSelectionListener  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FormattedResult result;
	private MyTableModel mtm;
	private ListSelectionModel cellSelectionModel;
	private CtrlResults cr;
	private JList changes;
	
	/**
	 * Creadora por defecto de la tabla de resultados
	 * @param cr: CtrlResults del dominio
	 * @see CtrlDominio
	 */
	public MyResultTable(CtrlResults cr) {
		this.cr = cr;
	}
	
	/**
	 * Constructora que permite especificar el resultado a mostrar en la tabla.
	 * Así como el control de los resultados.
	 * @param result: ArrayList  resultado a mostrar.
	 * @param cr: CtrlResults
	 * @see CtrlResults, CtrlDominio
	 */
	public MyResultTable (ArrayList<ArrayList<String>> result, CtrlResults cr) {
		this.cr = cr;
		this.result = new FormattedResult(result,cr);
		cellSelectionModel = getSelectionModel();
		cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mtm = new MyTableModel(this.result);
		setModel(mtm);
		setFillsViewportHeight(true);
		setEnabled(false);
		setCellSelectionEnabled(false);
		setColumnSelectionAllowed(false);
	}
		
	/**
	 * Constructora que permite especificar el resultado a mostrar y ademas la lista
	 * que informará de los cambios realizados.
	 * @param result: ArrayList resultado a mostrar.
	 * @param cr : CtrlResults de dominio.
	 * @param changes : JList que mostrará el registro de cambios realizados
	 */
	public MyResultTable (ArrayList<ArrayList<String>> result, CtrlResults cr, JList<String> changes) {
		this.cr = cr;
		this.result = new FormattedResult(result,cr);
		this.changes = changes;
		cellSelectionModel = getSelectionModel();
		cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mtm = new MyTableModel(this.result,changes);
		setModel(mtm);
		setFillsViewportHeight(true);
		setEnabled(false);
		setCellSelectionEnabled(true);
	}
	
	/**
	 * Función que permite definir el resultado a mostrar en la tabla.
	 * @param result: ArrayList:resultado a mostrar.
	 */
	
	public void generateTableContent(ArrayList<ArrayList<String>> result) {
		this.result = new FormattedResult(result,cr);
		mtm = new MyTableModel(this.result);
		setModel(mtm);
		mtm.fireTableChanged(null);
	}
	
	/**
	 * Función que es lanzada cuando se realiza un cambio. Y
	 * transmite el cambio al resultado (FormattedResult)
	 */
	public void valueChanged(ListSelectionEvent e) {
        Float selectedData = null;

        int[] selectedRow = getSelectedRows();
        int[] selectedColumns = getSelectedColumns();
        if (selectedRow.length > 0 && selectedColumns.length >0) {
	        if (selectedColumns[0] == 4) {
		        for (int i = 0; i < selectedRow.length; i++) {
		          for (int j = 0; j < selectedColumns.length; j++) {
		            selectedData =  (Float) getValueAt(selectedRow[i], selectedColumns[j]);
		          }
		        }
		        
		        result.setOldValue(selectedRow[0], selectedData);
		        this.repaint();
		    
	        }
        }
	}
	
	/**
	 * Método que le dice al resultado de la tabla que conserve 
	 * sus cambios
	 */
	public void saveChanges() {
		result.commitChanges();
	}
	
	/**
	 * Método que permite borrar los cambios, que no se guardaran
	 * definitivamente
	 */
	public void clearChanges() {
		result.clearChanges();
		mtm.clearListChanges();
		
		
	}
}
