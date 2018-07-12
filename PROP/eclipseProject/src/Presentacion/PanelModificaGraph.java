package Presentacion;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Dominio.CtrlDominio;

/**
 * 
 * @author Gonzalo Diez
 * 
 */

public class PanelModificaGraph extends AbstractPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton buttonAddNode = new JButton("Add New Node");
	private JButton buttonEditNode = new JButton("Edit Node");
	private JButton buttonSaveGraph = new JButton("Save Graph To File");
	private JButton buttonEraseNode = new JButton("Erase Node");
	
	private JPanel displayNode;
	private JComboBox<String> comboBoxTypeOfNode;
	Boolean changed = false;
	
	private JScrollPane scrollPane;
	private JList<String> list;
	private JTextField textField;
	private ArrayList<Integer> indexOfNodes;
	DefaultListModel<String> model;
	
	public PanelModificaGraph(VistaAbstracta vp) {
		super(vp);
	}
	
	public void init() {		
		initComponents();
		asignListeners();
	}
	
	private void initComponents() {
		this.removeAll();
		// DisplayNodes
		comboBoxTypeOfNode = new JComboBox<String>(CtrlDominio.getTypes().toArray(new String[CtrlDominio.getTypes().size()]));
		comboBoxTypeOfNode.setSelectedIndex(-1);
		comboBoxTypeOfNode.setToolTipText("Select Type of node");
		comboBoxTypeOfNode.setMaximumSize(buttonAddNode.getPreferredSize());
		comboBoxTypeOfNode.setSelectedIndex(0);
		
		createList();	
		
		displayNode = new JPanel();
		displayNode.setLayout(new BoxLayout(displayNode, BoxLayout.X_AXIS));
		displayNode.add(Box.createHorizontalStrut(5));
		displayNode.add(comboBoxTypeOfNode);
		displayNode.add(Box.createHorizontalStrut(5));
		JLabel filterLabel = new JLabel("Type text to filter: ");
		displayNode.add(filterLabel);
		displayNode.add(Box.createHorizontalStrut(5));
		displayNode.add(textField);
		displayNode.add(Box.createHorizontalStrut(5));
		displayNode.setMaximumSize(new Dimension(1000, comboBoxTypeOfNode.getHeight()));
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
		btnPanel.add(Box.createHorizontalGlue());
		btnPanel.add(buttonEditNode);
		btnPanel.add(Box.createHorizontalStrut(5));
		btnPanel.add(buttonAddNode);
		btnPanel.add(Box.createHorizontalStrut(5));
		btnPanel.add(buttonEraseNode);
		btnPanel.add(Box.createHorizontalStrut(5));
		btnPanel.add(buttonSaveGraph);
		btnPanel.add(Box.createHorizontalStrut(5));
		
		JPanel nodesInfoPanel = new JPanel();
		nodesInfoPanel.setLayout(new BoxLayout(nodesInfoPanel, BoxLayout.X_AXIS));
		nodesInfoPanel.add(Box.createHorizontalStrut(5));
		nodesInfoPanel.add(scrollPane);
		nodesInfoPanel.add(Box.createHorizontalStrut(5));
		nodesInfoPanel.setPreferredSize(new Dimension(9000,9000));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(5));
		add(displayNode);
		add(Box.createVerticalStrut(5));
		add(nodesInfoPanel);
		add(Box.createVerticalStrut(5));
		add(btnPanel);
		add(Box.createVerticalStrut(5));
		
		buttonAddNode.setEnabled(false);
		buttonEraseNode.setEnabled(false);
		buttonEditNode.setEnabled(false);
	}
	
	private void createList() {
		scrollPane = new JScrollPane();
		model = new DefaultListModel<String>();

		list = new JList<String>(model);
		scrollPane.setViewportView(list);
		
		textField = new JTextField();
		
		textField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {
				model.clear();
				ArrayList<ArrayList<String>> nodes = cd.getCtrlGraph().getformattedNodesOfType((String) comboBoxTypeOfNode.getSelectedItem());
				indexOfNodes = new ArrayList<Integer>();
				System.gc();
				if (textField.getText().length() > 0) {
					for (int i = 0; i < nodes.size(); ++i) {
						if (nodes.get(i).get(1).toLowerCase().contains(textField.getText().toLowerCase())) {
							model.addElement(nodes.get(i).get(1));
							indexOfNodes.add(Integer.parseInt(nodes.get(i).get(0)));
						}
					}
				}
				else {
					for (int i = 0; i < nodes.size(); ++i) {
						model.addElement(nodes.get(i).get(1));
						indexOfNodes.add(Integer.parseInt(nodes.get(i).get(0)));
					}
				}
			}
			
		});
		
		comboBoxTypeOfNode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
				model.clear();
				ArrayList<ArrayList<String>> nodes = cd.getCtrlGraph().getformattedNodesOfType((String) comboBoxTypeOfNode.getSelectedItem());
				indexOfNodes = new ArrayList<Integer>();
				for (int i = 0; i < nodes.size(); ++i) {
					model.addElement(nodes.get(i).get(1));
					indexOfNodes.add(Integer.parseInt(nodes.get(i).get(0)));
				}
				buttonAddNode.setEnabled(true);
				buttonEraseNode.setEnabled(false);
				buttonEditNode.setEnabled(false);
			}
		});
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				buttonAddNode.setEnabled(true);
				buttonEraseNode.setEnabled(true);
				buttonEditNode.setEnabled(true);
			}
		});
		updateGraph();
	}
	
	private void asignListeners() {
		PanelModificaGraph aux = this;
		buttonAddNode.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (!buttonAddNode.isEnabled()) return;
				aux.addVista(PanelEditNode.class, true); // Cambiar al panel que agrega nodos
				String nodeType = (String) comboBoxTypeOfNode.getSelectedItem();
				int i = cd.getCtrlGraph().addNode(nodeType, "New Node");
				((PanelEditNode) aux.childs.get(0).getContentPane().getComponent(0))
				.setNodeToEdit(i, nodeType);
			}
		});
		
		
		buttonEditNode.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {
				if (!buttonEditNode.isEnabled()) return;
				aux.addVista(PanelEditNode.class, true);
				((PanelEditNode) aux.childs.get(0).getContentPane().getComponent(0))
				.setNodeToEdit(indexOfNodes.get(list.getSelectedIndex()), 
						(String) comboBoxTypeOfNode.getSelectedItem());

			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			
		});
		buttonSaveGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!buttonSaveGraph.isEnabled()) return;
				
				cd.saveGraph();
				VistaDialog.setDialog("Graph Saved", "Graph save correctly",
						new String[] {"Continue"}, VistaDialog.DialogType.QUESTION_MESSAGE);
			}
		});
		buttonEraseNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!buttonSaveGraph.isEnabled()) return;
				cd.getCtrlGraph().eraseNode(indexOfNodes.get(list.getSelectedIndex()), 
						(String) comboBoxTypeOfNode.getSelectedItem());
				int pos = list.getSelectedIndex();
				model.remove(pos);
				indexOfNodes.remove(pos);
			}
		});
	}

	@Override
	public int closeIt() {
		if (changed) {
			// Preguntar si estas seguro de que te pueden cerrar desde un sitio externo
			String[] buttons = {"Exit", "Cancel"};
			int result = VistaDialog.setDialog("Unsaved changes", "Are you sure you want to exit?\n"
					+ "All unsaved changes will be lost",
					buttons, VistaDialog.DialogType.QUESTION_MESSAGE);
			return result;
		}
		else return 0;
	}

	@Override
	public void setEnabledEverything(Boolean b) {
		buttonAddNode.setEnabled(b);
		
		buttonEditNode.setEnabled(b);
		buttonSaveGraph.setEnabled(b);
		
		displayNode.setEnabled(b);
		comboBoxTypeOfNode.setEnabled(b);
		
		scrollPane.setEnabled(b);
		list.setEnabled(b);
		textField.setEnabled(b);
		
		
		//System.out.println("testestes");
		try { // Solo para el test. Luego lo tengo que quitar cuando ya no sea una vista abstracta y sea una vista principal
			VistaPrincipal v = (VistaPrincipal) vp;
			v.setEnabledPrincipal(b);
		}
		catch (ClassCastException e) {
			
		}
	}
	
	void updateGraph() {
		model.clear();
		ArrayList<ArrayList<String>> nodes = cd.getCtrlGraph().getformattedNodesOfType((String) comboBoxTypeOfNode.getSelectedItem());
		indexOfNodes = new ArrayList<Integer>();
		System.gc();
		if (textField.getText().length() > 0) {
			for (int i = 0; i < nodes.size(); ++i) {
				if (nodes.get(i).get(1).toLowerCase().contains(textField.getText().toLowerCase())) {
					model.addElement(nodes.get(i).get(1));
					indexOfNodes.add(Integer.parseInt(nodes.get(i).get(0)));
				}
			}
		}
		else {
			for (int i = 0; i < nodes.size(); ++i) {
				model.addElement(nodes.get(i).get(1));
				indexOfNodes.add(Integer.parseInt(nodes.get(i).get(0)));
			}
		}
		
	}
	

}
