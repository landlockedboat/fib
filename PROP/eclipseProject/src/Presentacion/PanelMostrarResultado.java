package Presentacion;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import Dominio.CtrlResults;
import Presentacion.VistaPrincipal.Panels;

/**
 * @author Albert Lopez Alcacer
**/

public class PanelMostrarResultado extends AbstractPanel{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private MyResultTable rst;
	private CtrlResults cr;
	private JPanel infoAndActions;
	private JPanel actions;
	private JPanel info;
	private JSplitPane splitpane;
	private JButton editar;
	private JButton guardar;
	private JButton cancelar;
	private boolean changesnc;
	private DefaultListModel<String> dlm;
	private JList<String> changes;
	private JScrollPane scrollChange;
	private ArrayList<ArrayList<String>> showedResult;
	private VistaPrincipal vp;
	private String idResult;
	
	/**
	 * Creadora por defecto de PanelMostrarResultado
	 * @param v : VistaPrincipal actual
	 */
	public PanelMostrarResultado (VistaPrincipal v)  {
		super(v);
		this.vp = v;
		this.cr = cd.getCtrlResults();
	}
	
	/**
	 * Función privada que permite asignar los listeners a los distintos
	 * botones.
	 */
	private void asignListeners() {
		editar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//rst.repaint();
				rst.setEnabled(true);
				guardar.setEnabled(true);
				cancelar.setEnabled(true);
				rst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				editar.setEnabled(false);
				changesnc = true;
			}
		});
		guardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				rst.setEnabled(false);
				editar.setEnabled(true);
				saveChanges();
				editar.setEnabled(false);
				guardar.setEnabled(false);
				changesnc = false;
			}
		});
		cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				rst.setEnabled(false);
				editar.setEnabled(true);
				rst.clearChanges();
				vp.changePanel(Panels.Test);
			}
		});
	}

	/**
	 * Función usada para recargar una serie de elementos del panel,
	 * de manera que puedan cambiarse posteriormente a ser instanciado.
	 */
	public void init() {
		removeAll();
		initComponents();
	}
	
	/**
	 * Función que permite definir el resultado que se mostrará en el panel.
	 * @param res
	 */
	public void setShowedResult(ArrayList<ArrayList<String>> res) {
		this.showedResult = res;
		this.idResult = res.get(0).get(0);
	}
	

	private void generateTable()  {
		
		rst = new MyResultTable(showedResult,cr,changes);
		rst.setFillsViewportHeight(true);
		rst.setEnabled(false);
		splitpane.setLeftComponent(new JScrollPane(rst));
		
	}
	
	private void generateInfoPanel() {
		
		info.add(scrollChange);
		info.add(Box.createHorizontalGlue());
		info.setLayout(new BoxLayout(info,BoxLayout.PAGE_AXIS));
	}
	private void generateActionPanel() {
		actions.setLayout(new BoxLayout(actions,BoxLayout.LINE_AXIS));
		actions.setAlignmentX(LEFT_ALIGNMENT);
		actions.add(Box.createHorizontalGlue());
		actions.add(editar);
		actions.add(guardar);
		actions.add(cancelar);
	}
	private void generateInfoAndActionPanel() {
		
		infoAndActions.setLayout(new BoxLayout(infoAndActions,BoxLayout.PAGE_AXIS));
		
		generateInfoPanel();
		infoAndActions.add(info);
		
		JSeparator seperator = new JSeparator(SwingConstants.HORIZONTAL);
		seperator.setMaximumSize( new Dimension(Integer.MAX_VALUE, 1) );
		infoAndActions.add(seperator);
		
		infoAndActions.add(Box.createVerticalGlue());
		generateActionPanel();
		infoAndActions.add(actions);
		
		splitpane.setRightComponent(infoAndActions);
		splitpane.resetToPreferredSizes();
		add(splitpane);
		splitpane.setDividerLocation(540);
	}
	private void initSubPanels() {
		generateTable();
		generateInfoAndActionPanel();
		
	}

	private void initComponents() {
		
		infoAndActions = new JPanel();
		actions = new JPanel();
		info = new JPanel();
		splitpane = new JSplitPane();
		editar = new JButton("Edit");
		guardar = new JButton("Save");
		guardar.setEnabled(false);
		cancelar = new JButton("Cancel");
		cancelar.setEnabled(false);
		rst = new MyResultTable(cr);
		dlm = new DefaultListModel<String>();
		changes = new JList<String>(dlm);
		scrollChange = new JScrollPane(changes);
		
		BoxLayout bl = new BoxLayout(this,BoxLayout.LINE_AXIS);
		setLayout(bl);
		initSubPanels();
		asignListeners();
		
	}
	
	private void saveChanges() {
		String[] buttons = {"Exit", "Cancel"};
		int result = VistaDialog.setDialog("Overwrite changes", "All modifications will be saved?\n"
				+ "Are you sure do you want to exit?", buttons, VistaDialog.DialogType.QUESTION_MESSAGE);
		if (result == 0) {
			//System.out.println("Si");
			rst.saveChanges();
			cd.saveResults();
			String[] ok = {"Ok"};
			VistaDialog.setDialog("Done", "Modifications stored\n ", ok, VistaDialog.DialogType.QUESTION_MESSAGE);
		}
	}
	
	@Override
	public int closeIt() {
		
		if (changesnc) {
			String[] buttons = {"Exit", "Cancel"};
			int result = VistaDialog.setDialog("Unsaved changes", "Are you sure you want to exit?\n"
					+ "All unsaved changes will be lost",
					buttons, VistaDialog.DialogType.QUESTION_MESSAGE);
			return result;
		}
		return 0;
		
	}
	@Override
	public void setEnabledEverything(Boolean b) {
		// TODO Auto-generated method stub
		
	}

}
