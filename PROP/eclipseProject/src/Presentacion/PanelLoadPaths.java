package Presentacion;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import Dominio.CtrlPaths;
import Dominio.CtrlSearch;
import Presentacion.VistaPrincipal.Panels;

public class PanelLoadPaths extends AbstractPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VistaPrincipal vp;
	private CtrlPaths cp;
	private JSplitPane splitpane;
	private JPanel pathsAndActionsPanel;
	private JPanel pathsPanel;
	private JPanel actionsPanel;
	private MyResultsAndPathsList pathsList;
	private ArrayList<ArrayList<String>> paths;
	private JScrollPane scrollPane;
	private JButton editar;
	private JButton anadir;
	private JButton precalculate;

	public PanelLoadPaths(VistaPrincipal vp) {
		super(vp);
		initComponents();
		this.vp = vp;
	}
	
	private void initComponents() {
		cp = cd.getCtrlPaths();
		splitpane = new JSplitPane();
		pathsAndActionsPanel = new JPanel();
		pathsPanel = new JPanel();
		actionsPanel = new JPanel();
		pathsList = new MyResultsAndPathsList(cp);
		editar = new JButton("Edit");
		anadir = new JButton("Add");
		precalculate = new JButton("Precalculate");
		BoxLayout bl = new BoxLayout(this,BoxLayout.LINE_AXIS);
		setLayout(bl);
		
		initSubpanels();
		assignListeners();
	}
	
	private void initSubpanels() {
		generatePathsAndActionsPanel();
		splitpane.setAlignmentX(LEFT_ALIGNMENT);
		splitpane.setLeftComponent(pathsPanel);
		splitpane.setRightComponent(actionsPanel);
		splitpane.resetToPreferredSizes();
		add(splitpane);
		splitpane.setDividerLocation(520);
	}
	
	
	private void generatePathsPanel() {
		pathsPanel.setLayout(new BoxLayout(pathsPanel, BoxLayout.PAGE_AXIS));
		scrollPane = new JScrollPane(pathsList);
		pathsPanel.add(scrollPane);
		
	}
	
	private void generateActionsPanel() {
		
		actionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		actionsPanel.add(editar);
		actionsPanel.add(anadir);
		actionsPanel.add(precalculate);
		editar.setAlignmentY(BOTTOM_ALIGNMENT);
		anadir.setAlignmentY(BOTTOM_ALIGNMENT);
		

	}
	
	private void generatePathsAndActionsPanel() {
		pathsAndActionsPanel.setLayout(new BoxLayout(pathsAndActionsPanel, BoxLayout.PAGE_AXIS));
		generatePathsPanel();
		pathsAndActionsPanel.add(pathsPanel);
		pathsAndActionsPanel.add(Box.createVerticalGlue());
		generateActionsPanel();
		pathsAndActionsPanel.add(actionsPanel);
	}
	
	private void assignListeners() {
		
		precalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (pathsList.indexSelected()) {
					int index = pathsList.getSelectedIndex();
					CtrlSearch cs = cd.getCtrlSearch();
					ArrayList<String> fp = pathsList.getFormattedPath();
					cs.precalculePath(fp.get(2));
					//System.out.println("hola");
				}
				else {
					//System.outtem.out.println("Selecciona un resultado");					
				}
			}
		});
		PanelLoadPaths aux = this;
		editar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (pathsList.indexSelected()) {
					ArrayList<String> formattedPath = pathsList.getFormattedPath();
					String name, desc, content;
					
					name = formattedPath.get(0);
					desc = formattedPath.get(1);
					content = formattedPath.get(2);
					
					aux.addVista(PanelNewPath.class, true);
					
					PanelNewPath pnp = (PanelNewPath) (aux.childs.get(0).getContentPane().getComponent(0));
					pnp.init();
					pnp.textFieldName.setText(name);
					pnp.textFieldName.setEnabled(false);
					pnp.textFieldDescription.setText(desc);
					pnp.textFieldDescription.setEnabled(false);
					pnp.textField.setText(content);
					pnp.tittleLabel.setText("Edit Path");
					pnp.editing = true;
					pnp.enablePathButtons();
					
					
					//System.out.println("hola");
				}
				else {
					//System.outtem.out.println("Selecciona un resultado");
				}
			}
		});
		
		anadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				vp.changePanel(Panels.NewPath);
			}
		});
	}
	
	public void init() {
		removeAll();
		initComponents();
	}
	
	@Override
	int closeIt() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setEnabledEverything(Boolean b) {
		// TODO Auto-generated method stub
		
	}

}
