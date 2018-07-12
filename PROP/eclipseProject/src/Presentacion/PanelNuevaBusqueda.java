package Presentacion;


import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;

import Presentacion.VistaDialog.DialogType;
import Presentacion.VistaPrincipal.Panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
//import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 * Panel para el caso de uso de nueva Búsqueda
 * 
 * @author Xavier Peñalosa
 *
 */
public class PanelNuevaBusqueda extends AbstractPanel implements ActionListener{

	private JComboBox<String> pathSelect;
	private MyComboBox node1Select, node2Select;
	private JLabel pathLabel, node1Label, node2Label, thresholdLabel;
	private JCheckBox checkbox;
	
	//private JList resultList;
	private MyResultTable resultTable;
	private JSpinner threshold;
	
	private JButton calcHete, reuseSearch, saveResult, editResult;
	
	private SpringLayout sl;
	
	private VistaPrincipal vp;
	private ArrayList<String> pathContents;
	private boolean hasResult = false;
	//private String idResult = "";
	private static final long serialVersionUID = 1L;
	

	PanelNuevaBusqueda(VistaPrincipal vp) {
		super(vp);
		
		this.vp = vp;
	}
	
	public void init(){
		this.removeAll();
		hasResult = false;
		
		initComponents();
		assignListeners();
	}
	
	/**
	 * Initialize the components for the panel
	 * <p>
	 * <b>node1SelectType</b>: Dropdown list to pick the NodeType for the first node <p>
	 * <b>node2SelectType</b>: Dropdown list to pick the NodeType for the second node <p>
	 * <b>node1Select</b>: Dropdown list to pick the first node. Only the nodes that belong to the selected NodeType in node1SelectType will be displayed <p>
	 * <b>node2Select</b>: Dropdown list to pick the last node. Only the nodes that belong to the selected NodeType in node2SelectType will be displayed
	 */
	private void initComponents() {
		
		sl = new SpringLayout();
		setLayout(sl);
		pathContents = new ArrayList<String>();
		String slash = (System.getProperty("os.name").toLowerCase().contains("windows") ? "\\" : "/");
		
		//Path
		ArrayList<ArrayList<String>> pathsTemp = cd.getCtrlPaths().getFormattedPaths();
		ArrayList<String> paths = new ArrayList<String>();
		for (int i = 0;i < pathsTemp.size(); ++i){
			paths.add(pathsTemp.get(i).get(0));
			pathContents.add(pathsTemp.get(i).get(2));
		}
		pathSelect = new JComboBox<String>(arrayListToArray(paths));
		pathSelect.setPreferredSize(new Dimension(100,20));
		pathSelect.setSelectedIndex(-1);
		add(pathSelect);
		
		//Node 1
		node1Label = new JLabel();
		node1Label.setPreferredSize(new Dimension(168,20));
		node1Label.setText("Node type:");
		node1Label.setForeground(Color.gray);
		add(node1Label);
			node1Select = new MyComboBox();
			node1Select.loadNodesToLists(this.cd);
			add(node1Select);
		
		//Node 2
		node2Label = new JLabel();
		node2Label.setPreferredSize(new Dimension(168,20));
		node2Label.setText("Node type:");
		node2Label.setForeground(Color.gray);
		add(node2Label);
			node2Select = new MyComboBox();
			node2Select.loadNodesToLists(this.cd);
			node2Select.setEnabled(false);
			add(node2Select);
		
		//Path label
		pathLabel = new JLabel();
		pathLabel.setPreferredSize(new Dimension(168,20));
		pathLabel.setText("Path");
		add(pathLabel);
		
		//ResultList
		//ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
		resultTable = new MyResultTable(cd.getCtrlResults());
		initResultTable();
		resultTable.setBorder(BorderFactory.createLineBorder(Color.gray));
		resultTable.setPreferredSize(new Dimension(350,200));
		add(resultTable);
		
		//resultList = new JList<String>();
		//resultList.setPreferredSize(new Dimension(350,200));
		//resultList.setBorder(BorderFactory.createLineBorder(Color.gray));
		//add(resultList);
		
		
		
		//Threshold value
		checkbox = new JCheckBox();
		checkbox.setSelected(false);
		checkbox.setEnabled(false);
		add(checkbox);
		
		thresholdLabel = new JLabel();
		thresholdLabel.setPreferredSize(new Dimension(75,20));
		thresholdLabel.setText("Threshold");
		thresholdLabel.setForeground(Color.gray);
		add(thresholdLabel);
			threshold = new JSpinner();
			SpinnerModel sm = new SpinnerNumberModel(0.5,0,1,0.05);
			threshold.setModel(sm);
			JComponent editor = threshold.getEditor();
			((JSpinner.DefaultEditor) editor).getTextField().setColumns(4);
			threshold.setEnabled(false);
			add(threshold);
		
		//Hetesim button
		Icon icon = null;
		try {
			BufferedImage bi = ImageIO.read(new File(System.getProperty("user.dir")+slash+"resources"+slash+"calcIcon.png"));
			icon = new ImageIcon(bi);
		} catch (IOException e) {
		}
		calcHete = new JButton("Calculate Hetesim", icon);
		calcHete.setPreferredSize(new Dimension(200,50));
		calcHete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		calcHete.setFocusable(false);
		calcHete.setEnabled(false);
		add(calcHete);
		
		Icon icon4 = null;
		try {
			BufferedImage bi = ImageIO.read(new File(System.getProperty("user.dir")+slash+"resources"+slash+"calcIcon.png"));
			icon4 = new ImageIcon(bi);
		} catch (IOException e) {
			
		}
		reuseSearch = new JButton("Use fields", icon4);
		reuseSearch.setPreferredSize(new Dimension(200,50));
		reuseSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		reuseSearch.setFocusable(false);
		reuseSearch.setEnabled(false);
		add(reuseSearch);
		
		//Save result button
		Icon icon2 = null;
		try {
			BufferedImage bi = ImageIO.read(new File(System.getProperty("user.dir")+slash+"resources"+slash+"saveIcon.png"));
			icon2 = new ImageIcon(bi);
		} catch (IOException e) {
		}
		saveResult = new JButton("Save", icon2);
		saveResult.setPreferredSize(new Dimension(92,30));
		saveResult.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveResult.setFocusable(false);
		saveResult.setEnabled(false);
		add(saveResult);
		
		//Edit result button
		Icon icon3 = null;
		try {
			BufferedImage bi = ImageIO.read(new File(System.getProperty("user.dir")+slash+"resources"+slash+"editIcon.jpg"));
			icon3 = new ImageIcon(bi);
		} catch (IOException e) {
		}
		editResult = new JButton("Edit", icon3);
		editResult.setPreferredSize(new Dimension(92,30));
		editResult.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		editResult.setToolTipText("You must save the result before editing");
		editResult.setFocusable(false);
		editResult.setEnabled(false);
		add(editResult);
		
		
		putConstraints();
	}
	
	private void putConstraints(){
		//Path selection
		sl.putConstraint(SpringLayout.NORTH, pathSelect, 20, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, pathSelect, 20, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.NORTH, pathLabel, 20, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, pathLabel, 15, SpringLayout.EAST, pathSelect);
		
		
		//Node 1
		sl.putConstraint(SpringLayout.NORTH, node1Select, 15, SpringLayout.SOUTH, pathSelect);
		sl.putConstraint(SpringLayout.WEST, node1Select, 20, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.NORTH, node1Label, 15, SpringLayout.SOUTH, pathSelect);
		sl.putConstraint(SpringLayout.WEST, node1Label, 15, SpringLayout.EAST, node1Select);
		
		//Node 2
		sl.putConstraint(SpringLayout.NORTH, node2Select, 15, SpringLayout.SOUTH, node1Select);
		sl.putConstraint(SpringLayout.WEST, node2Select, 20, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.NORTH, node2Label, 15, SpringLayout.SOUTH, node1Label);
		sl.putConstraint(SpringLayout.WEST, node2Label, 15, SpringLayout.EAST, node2Select);

		//List
		sl.putConstraint(SpringLayout.NORTH, resultTable, 15, SpringLayout.SOUTH, node2Label);
		sl.putConstraint(SpringLayout.SOUTH, resultTable, -35, SpringLayout.SOUTH, this);
		sl.putConstraint(SpringLayout.WEST, resultTable, 20, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.EAST, resultTable, -15, SpringLayout.WEST, calcHete);
		//sl.putConstraint(SpringLayout.NORTH, resultList, 15, SpringLayout.SOUTH, node2Label);
		//sl.putConstraint(SpringLayout.SOUTH, resultList, -35, SpringLayout.SOUTH, this);
		//sl.putConstraint(SpringLayout.WEST, resultList, 20, SpringLayout.WEST, this);
		
		//Threshold
		sl.putConstraint(SpringLayout.NORTH, checkbox, 5, SpringLayout.SOUTH, resultTable);
		sl.putConstraint(SpringLayout.EAST, checkbox, 0, SpringLayout.WEST, thresholdLabel);
		
		sl.putConstraint(SpringLayout.NORTH, threshold, 5, SpringLayout.SOUTH, resultTable);
		sl.putConstraint(SpringLayout.EAST, threshold, 0, SpringLayout.EAST, resultTable);
		
		sl.putConstraint(SpringLayout.NORTH, thresholdLabel, 5, SpringLayout.SOUTH, resultTable);
		sl.putConstraint(SpringLayout.EAST, thresholdLabel, 0, SpringLayout.WEST, threshold);
		//sl.putConstraint(SpringLayout.NORTH, checkbox, 5, SpringLayout.SOUTH, resultList);
		//sl.putConstraint(SpringLayout.EAST, checkbox, 0, SpringLayout.WEST, thresholdLabel);
		
		//sl.putConstraint(SpringLayout.NORTH, threshold, 5, SpringLayout.SOUTH, resultList);
		//sl.putConstraint(SpringLayout.EAST, threshold, 0, SpringLayout.EAST, resultList);
		
		//sl.putConstraint(SpringLayout.NORTH, thresholdLabel, 5, SpringLayout.SOUTH, resultList);
		//sl.putConstraint(SpringLayout.EAST, thresholdLabel, 0, SpringLayout.WEST, threshold);
		
		//Calc hetesim
		sl.putConstraint(SpringLayout.NORTH, calcHete, 0, SpringLayout.NORTH, resultTable);
		sl.putConstraint(SpringLayout.EAST, calcHete, -15, SpringLayout.EAST, this);
		//sl.putConstraint(SpringLayout.NORTH, calcHete, 0, SpringLayout.NORTH, resultList);
		//sl.putConstraint(SpringLayout.WEST, calcHete, 15, SpringLayout.EAST, resultList);
		
		//Reuse search
		sl.putConstraint(SpringLayout.NORTH, reuseSearch, 15, SpringLayout.SOUTH, calcHete);
		sl.putConstraint(SpringLayout.WEST, reuseSearch, 15, SpringLayout.EAST, resultTable);
		sl.putConstraint(SpringLayout.EAST, reuseSearch, -15, SpringLayout.EAST, this);
		
		
		//Edit result
		//sl.putConstraint(SpringLayout.NORTH, editResult, 1000, SpringLayout.SOUTH, calcHete);
		sl.putConstraint(SpringLayout.SOUTH, editResult, -35, SpringLayout.SOUTH, this);
		sl.putConstraint(SpringLayout.WEST, editResult, 15, SpringLayout.EAST, resultTable);
		//sl.putConstraint(SpringLayout.SOUTH, editResult, -35, SpringLayout.SOUTH, this);
		//sl.putConstraint(SpringLayout.WEST, editResult, 15, SpringLayout.EAST, resultList);
		
		//Save result
		//sl.putConstraint(SpringLayout.NORTH, saveResult, 50, SpringLayout.SOUTH, calcHete);
		sl.putConstraint(SpringLayout.SOUTH, saveResult, -35, SpringLayout.SOUTH, this);
		sl.putConstraint(SpringLayout.WEST, saveResult, 15, SpringLayout.EAST, editResult);
		
		
		
	}

	private void initResultTable(){
		resultTable.setPreferredSize(new Dimension(350,200));
		resultTable.setBorder(BorderFactory.createLineBorder(Color.black));
		resultTable.setEnabled(true);
		//resultTable.setSelectionModel(new DefaultListSelectionModel());
		//resultTable.setColumnModel(new DefaultTableColumnModel());
		ListSelectionListener lsl = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(resultTable.getSelectedColumns().length < 5 && resultTable.getSelectedRow() > -1){
					resultTable.setColumnSelectionInterval(0, 4);
				}
				
			}
		};
		resultTable.getSelectionModel().addListSelectionListener(lsl);
		resultTable.getColumnModel().getSelectionModel().addListSelectionListener(lsl);
		
		resultTable.setCellSelectionEnabled(true);
	}
	
	private void assignListeners(){
		
		pathSelect.addActionListener(this);
		node1Select.addActionListener(this);
		node2Select.addActionListener(this);
		
		calcHete.addActionListener(this);
		saveResult.addActionListener(this);
		editResult.addActionListener(this);
		reuseSearch.addActionListener(this);
		checkbox.addActionListener(this);
		
	}

	private String[] arrayListToArray(ArrayList<String> alist){
		String[] temp = new String[alist.size()];
		return alist.toArray(temp);
	}
	
	
	@Override
	public int closeIt() {
		if (hasResult){
			String[] buttons = {"Exit", "Cancel"};
			int result = VistaDialog.setDialog("Unsaved changes", "Are you sure you want to exit?\n"
					+ "All unsaved changes will be lost",
					buttons, VistaDialog.DialogType.QUESTION_MESSAGE);
			return result;
		}
		else {
			return 0;
		}
		
	}

	@Override
	public void setEnabledEverything(Boolean b) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		
		calcHete.setEnabled(true);
		
		if (e.getSource().equals(calcHete)){
			
			String path = pathSelect.getSelectedItem().toString();
			System.out.println("cosa " + path);
			int n1 = -1, n2 = -1;
			if (!node1Select.getEditor().getItem().toString().equals(" - Select all -")) n1 = node1Select.getSelectedNodeIndex();
			if (!node2Select.getEditor().getItem().toString().equals(" - Select all -") && node2Select.isEnabled()) n2 = node2Select.getSelectedNodeIndex();
			if (n1 == -1 && n2 == -1){
				//No nodes
				//System.out.println("Searching");
				if (checkbox.isSelected()){
					//System.out.println("P threshold");
					//idResult = cd.searchPathThreshhold((float)((double)threshold.getValue()), path);
					cd.searchPathThreshhold(Float.valueOf(threshold.getValue().toString()), path);
					//resultTable = new MyResultTable(cd.getCtrlResults().getLastResultFormatted(),cd.getCtrlResults());
				}
				else {
					//System.out.println("P");
					//idResult = cd.searchPath(path);
					cd.searchPath(path);
					//resultTable = new MyResultTable(cd.getCtrlResults().getLastResultFormatted(),cd.getCtrlResults());
				}
				//System.out.println("Done");
			}
			else if (n1 >= 0 && n2 == -1){
				//One node
				if (checkbox.isSelected()){
					//System.out.println("PN1 threshold");
					//idResult = cd.searchPathNodeThreshhold((float)((double)threshold.getValue()), path, n1);
					cd.searchPathNodeThreshhold(Float.valueOf(threshold.getValue().toString()), path, n1);
					//resultTable = new MyResultTable(cd.getCtrlResults().getLastResultFormatted(),cd.getCtrlResults());
				}
				else {
					//System.out.println("PN1");
					//idResult = cd.searchPathNode(path,n1);
					cd.searchPathNode(path,n1);
				}
				//System.out.println("Done");
				
			}
			/*
			 * We can't reverse the paths
			 * 
			else if (n1 == -1 && n2 >= 0){
				//One node, reverse
				if (checkbox.isSelected()){
					System.out.println("PN2 threshold");
					path = new StringBuilder(path).reverse().toString();
					idResult = cd.searchPathNodeThreshhold((float)((double)threshold.getValue()), path, n2);
				}
				else {
					System.out.println("PN2");
					System.out.println(path);
					path = new StringBuilder(path).reverse().toString();
					System.out.println(path);
					idResult = cd.searchPathNode(path,n2);
				}
				System.out.println("Done");
			}
			*/
			else if (n1 >= 0 && n2 >= 0){
				//Two
				if (checkbox.isSelected()){
					//System.out.println("PNN threshold");
					//idResult = cd.searchPathNodeNodeThreshhold((float)((double)threshold.getValue()), path, n1, n2);
					cd.searchPathNodeNodeThreshhold((Float.valueOf(threshold.getValue().toString())), path, n1, n2);
				}
				else {
					//System.out.println("PNN");
					//idResult = cd.searchPathNodeNode(path,n1,n2);
					cd.searchPathNodeNode(path,n1,n2);
				}
				//System.out.println("Done");
			}
			else {
				//System.out.println("Either node is invalid");
			}
			
			
			
			try {
				remove(resultTable);

				
				resultTable.generateTableContent(cd.getCtrlResults().getLastResultFormatted());
				initResultTable();
				//System.out.println(resultTable.getRowCount() + " " + resultTable.getColumnCount());
				//resultTable.setModel(cd.getCtrlResults().getLastResultFormatted());

				//resultTable.setFillsViewportHeight(false);
				add(resultTable);
				putConstraints();

				resultTable.setVisible(true);
					
				threshold.setEnabled(true);
				thresholdLabel.setForeground(Color.black);
				saveResult.setEnabled(true);
				reuseSearch.setEnabled(true);
				hasResult = true;

				calcHete.setEnabled(false);

			}
			catch (NullPointerException p){
				VistaDialog.setDialog("Error", "It hasn't been possible to access the result\n", new String[]{"Continue"}, VistaDialog.DialogType.ERROR_MESSAGE);
			}
			
			updateUI();
		}
		else if (e.getSource().equals(reuseSearch)){
			//node1Select.setSelectedItem(resultTable.getModel().getValueAt(resultTable.getSelectedRow(),0));
			//node2Select.setSelectedItem(resultTable.getModel().getValueAt(resultTable.getSelectedRow(),2));
			node1Select.setSelectedItem(resultTable.getValueAt(resultTable.getSelectedRow(), 0));
			node2Select.setSelectedItem(resultTable.getValueAt(resultTable.getSelectedRow(), 2));
			threshold.setValue(resultTable.getValueAt(resultTable.getSelectedRow(), 4));
			checkbox.setSelected(true);
			calcHete.setEnabled(true);
		}
		else if (e.getSource().equals(saveResult)){
			//idResult = cd.getCtrlResults().addLastResult();
			cd.getCtrlResults().addLastResult();
			
			cd.saveResults();
			
			editResult.setEnabled(true);
			saveResult.setEnabled(false);
			VistaDialog.setDialog("", "Result stored", new String[]{"OK"}, DialogType.INFORMATION_MESSAGE);
		}
		else if (e.getSource().equals(editResult)){
			this.vp.panelMostrarResultado.setShowedResult(cd.getCtrlResults().getLastResultFormatted());
			this.vp.changePanel(Panels.PanelMostrarResultado);
		}
		else if (e.getSource().equals(checkbox)){
			if (checkbox.isSelected()){
				threshold.setEnabled(true);
				thresholdLabel.setForeground(Color.black);
			}
			else {
				threshold.setValue(0.5);
				threshold.setEnabled(false);
				thresholdLabel.setForeground(Color.gray);
			}
		}
		else if (e.getSource().equals(node1Select)){
			//System.out.println(node1Select.getSelectedItem().toString());
			if (node1Select.getSelectedNodeIndex() != -1){
				node2Select.setEnabled(true);
			}
			else {
				node2Select.setEnabled(false);
			}
		}
		else if (e.getSource().equals(pathSelect)){
			calcHete.setEnabled(true);
			checkbox.setEnabled(true);
			String text = pathContents.get(pathSelect.getSelectedIndex()).toString();
			boolean n1 = false, n2 = false;
			switch (text.charAt(0)){
				case 'P':
					node1Label.setText("Node type: Paper");
					node1Label.setForeground(Color.black);
					node1Select.setList(0);
					node1Select.setEnabled(true);
					n1 = true;
					break;
				case 'A':
					node1Label.setText("Node type: Author");
					node1Label.setForeground(Color.black);
					node1Select.setList(1);
					node1Select.setEnabled(true);
					n1 = true;
					break;
				case 'C':
					node1Label.setText("Node type: Conference");
					node1Label.setForeground(Color.black);
					node1Select.setList(2);
					node1Select.setEnabled(true);
					n1 = true;
					break;
				case 'T':
					node1Label.setText("Node type: Term");
					node1Label.setForeground(Color.black);
					node1Select.setList(3);
					node1Select.setEnabled(true);
					n1 = true;
					break;
				default:
					node1Label.setText("INVALID PATH");
					node1Label.setForeground(Color.gray);
					node1Select.setEnabled(false);
					break;
			}
			switch (text.charAt(text.length()-1)){
				case 'P':
					node2Label.setText("Node type: Paper");
					node2Label.setForeground(Color.black);
					node2Select.setList(0);
					//node2Select.setEnabled(true);
					n2 = true;
					break;
				case 'A':
					node2Label.setText("Node type: Author");
					node2Label.setForeground(Color.black);
					node2Select.setList(1);
					//node2Select.setEnabled(true);
					n2 = true;
					break;
				case 'C':
					node2Label.setText("Node type: Conference");
					node2Label.setForeground(Color.black);
					node2Select.setList(2);
					//node2Select.setEnabled(true);
					n2 = true;
					break;
				case 'T':
					node2Label.setText("Node type: Term");
					node2Label.setForeground(Color.black);
					node2Select.setList(3);
					//node2Select.setEnabled(true);
					n2 = true;
					break;
				default:
					node2Label.setText("INVALID PATH");
					node2Label.setForeground(Color.gray);
					node2Select.setEnabled(false);
					break;
			}
			if (node1Select.getEditor().getItem().toString().equals("None found!") ||
				node2Select.getEditor().getItem().toString().equals("None found!") ||
				!(n1 && n2)){
					
					node1Select.setEnabled(false);
					node2Select.setEnabled(false);
					node1Label.setForeground(Color.gray);
					node2Label.setForeground(Color.gray);
					
					calcHete.setEnabled(false);
					checkbox.setEnabled(false);
					threshold.setEnabled(false);
					thresholdLabel.setForeground(Color.gray);
				
			}
		}
		
	}
}
