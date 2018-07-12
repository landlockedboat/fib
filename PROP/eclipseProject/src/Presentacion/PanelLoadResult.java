package Presentacion;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import Dominio.Result;
import Dominio.CtrlResults;
import Dominio.Graph;
import Dominio.Node;
import Dominio.Pair;
import Dominio.Path;
import Presentacion.FormattedResult;
import Presentacion.VistaPrincipal.Panels;


/**
 * @author Albert Lopez Alcacer
**/

//origin and destination nodes (if exists)
public class PanelLoadResult extends AbstractPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VistaPrincipal vp;
	private CtrlResults cr;
	private JSplitPane splitpane;
	private JPanel searchAndActionsPanel;
	private JPanel resultsPanel;
	private JPanel searchPanel;
	private JPanel actionsPanel;
	private MyResultsAndPathsList loadedResults;
	private JEditorPane resultResume;
	private JScrollPane scrollPane;
	private JButton show;
	private JButton delete;
	private JLabel title;
	ArrayList<FormattedResult> formattedResults;
	
	/**
	 * Creadora por defecto del PanelLoadResults
	 * @param vp
	 */
	public PanelLoadResult (VistaPrincipal vp)   {
		super(vp);
		this.vp = vp;
	}

	
	private void generateResultsPanel(){
		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.PAGE_AXIS));
		scrollPane = new JScrollPane(loadedResults);
		resultsPanel.add(scrollPane);		
	}
	
	

	private void generateSearchPanel() {
		
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));
		searchPanel.setAlignmentX(RIGHT_ALIGNMENT);
		searchPanel.add(Box.createHorizontalGlue());
		
	}
	
	private void generateActionPanel() {
		
		actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.PAGE_AXIS));
		actionsPanel.add(Box.createHorizontalGlue());
		actionsPanel.add(title);
		title.setAlignmentX(CENTER_ALIGNMENT);
		actionsPanel.add(resultResume);
		actionsPanel.add(Box.createHorizontalGlue());
		actionsPanel.add(show);
		show.setAlignmentX(CENTER_ALIGNMENT);
		delete.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	private void generateSearchAndActionsPanel() {
		
		searchAndActionsPanel.setLayout(new BoxLayout(searchAndActionsPanel, BoxLayout.PAGE_AXIS));
		generateSearchPanel();
		searchAndActionsPanel.add(searchPanel);
		searchAndActionsPanel.add(Box.createVerticalGlue());
		generateActionPanel();
		searchAndActionsPanel.add(actionsPanel);

	}
	
	private void initSubPanels() {
		
		generateResultsPanel();
		generateSearchAndActionsPanel();
		splitpane.setAlignmentX(LEFT_ALIGNMENT);
		splitpane.setLeftComponent(resultsPanel);
		splitpane.setRightComponent(searchAndActionsPanel);
		splitpane.resetToPreferredSizes();
		add(splitpane);
		splitpane.setDividerLocation(450);

	}
	
	private void assignListeners() {
		
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (loadedResults.indexSelected()) {
					vp.panelMostrarResultado.setShowedResult(loadedResults.getFormattedResult());
					vp.changePanel(Panels.PanelMostrarResultado);
				}
				else { 
					//System.out.println("Selecciona un resultado");
				}
			}
		});
		/*
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (loadedResults.indexSelected()) {
					String[] buttons = {"Si", "Cancelar"};
					int result = VistaDialog.setDialog("Titulo", "¿Estas seguro que quieres borrar? \n", buttons, VistaDialog.DialogType.QUESTION_MESSAGE);
					if (result == 0) loadedResults.deleteResult();
					if (loadedResults.getListSize() == 0)  {
						delete.setEnabled(false);
						show.setEnabled(false);
					}
				}
				else System.out.println("Selecciona un resultado");
			}
		});*/
	}
	public void init() {
		removeAll();
		initComponents();
	}
	private void initComponents(){
		
		splitpane = new JSplitPane();
		resultsPanel = new JPanel();
		actionsPanel = new JPanel();
		resultResume = new JEditorPane();
		searchPanel = new JPanel();
		searchAndActionsPanel = new JPanel();
		title = new JLabel("Result Informartion:");
		title.setFont(new Font("Serif",Font.BOLD,12));
		show = new JButton("Show");
		delete = new JButton("Delete");
		
		cr = cd.getCtrlResults();
		//generateResults();
		loadedResults = new MyResultsAndPathsList(resultResume,cr);
		
		BoxLayout bl = new BoxLayout(this,BoxLayout.LINE_AXIS);
		setLayout(bl);
		initSubPanels();
		assignListeners();
		
	}
	
	@Override
	public int closeIt() {
		return 0;
	}
	@Override
	public void setEnabledEverything(Boolean b) {
		// TODO Auto-generated method stub
		
	}

}
