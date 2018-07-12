package Presentacion;

import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Dominio.CtrlDominio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * @author Victor Alcazar Lopez
 *
 */
public class PanelSelectNode extends AbstractPanel{

	DefaultListModel<String> nodeSelectionModel = new DefaultListModel<String>();
	JComboBox<String> typeComboBox = new JComboBox<String>();
	SpringLayout springLayout = new SpringLayout();

	JLabel lblSelectANode = new JLabel("Select a Node: ");
	JLabel lblNodeType = new JLabel("Node Type: ");
	JLabel lblNodePaper = new JLabel("Paper");
	
	ArrayList<ArrayList<String>> nodesInfo;
	INodeNeeder nodeNeeder; 
	JTextField searchTextField;
	ArrayList<Integer> indexOfNodes;

	public PanelSelectNode(VistaAbstracta vp) {
		super(vp);
		this.setLayout(springLayout);

		JButton btnCancel = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.SOUTH, btnCancel, -10, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, btnCancel, -10, SpringLayout.EAST, this);

		btnCancel.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						close();
					}
				});

		this.add(btnCancel);

		JButton btnGetNode = new JButton("Get node");
		springLayout.putConstraint(SpringLayout.NORTH, btnGetNode, 0, SpringLayout.NORTH, btnCancel);
		springLayout.putConstraint(SpringLayout.EAST, btnGetNode, -10, SpringLayout.WEST, btnCancel);
		

		this.add(btnGetNode);
		btnGetNode.setEnabled(false);

		springLayout.putConstraint(SpringLayout.NORTH, lblSelectANode, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblSelectANode, 10, SpringLayout.WEST, this);
		this.add(lblSelectANode);

		springLayout.putConstraint(SpringLayout.NORTH, lblNodeType, 10, SpringLayout.SOUTH, lblSelectANode);
		springLayout.putConstraint(SpringLayout.WEST, lblNodeType, 10, SpringLayout.WEST, lblSelectANode);
		this.add(lblNodeType);

		JLabel lblNodeList = new JLabel("All nodes of the selected type: ");
		springLayout.putConstraint(SpringLayout.NORTH, lblNodeList, 10, SpringLayout.SOUTH, lblNodeType);
		springLayout.putConstraint(SpringLayout.WEST, lblNodeList, 0, SpringLayout.WEST, lblNodeType);
		this.add(lblNodeList);
		
		JList<String> nodeSelectionList = new JList<String>(nodeSelectionModel);
		JScrollPane listScrollPane = new JScrollPane();
		listScrollPane.setViewportView(nodeSelectionList);
		springLayout.putConstraint(SpringLayout.NORTH, listScrollPane, 10, SpringLayout.SOUTH, lblNodeList);
		springLayout.putConstraint(SpringLayout.WEST, listScrollPane, 0, SpringLayout.WEST, lblNodeList);
		springLayout.putConstraint(SpringLayout.SOUTH, listScrollPane, -10, SpringLayout.NORTH, btnCancel);
		springLayout.putConstraint(SpringLayout.EAST, listScrollPane, -10, SpringLayout.EAST, this);
		this.add(listScrollPane);
		
		nodeSelectionList.addListSelectionListener(
				new ListSelectionListener(){
					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						btnGetNode.setEnabled(!nodeSelectionList.isSelectionEmpty());
					}

				}
				);

		searchTextField = new JTextField();
		
		springLayout.putConstraint(SpringLayout.EAST, searchTextField, -10, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, searchTextField, -10, SpringLayout.NORTH, listScrollPane);
		searchTextField.setColumns(16);
		
		this.add(searchTextField);
		
		JLabel lblFilter = new JLabel("Filter: ");
		
		springLayout.putConstraint(SpringLayout.EAST, lblFilter, -10, SpringLayout.WEST, searchTextField);
		springLayout.putConstraint(SpringLayout.SOUTH, lblFilter, -10, SpringLayout.NORTH, listScrollPane);
		
		this.add(lblFilter);
		
		
		
		btnGetNode.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						ArrayList<String> node = nodesInfo.get(indexOfNodes.get(nodeSelectionList.getSelectedIndex()));
						//System.out.println(indexOfNodes.get(nodeSelectionList.getSelectedIndex()) + " " + nodeSelectionList.getSelectedIndex());
						//System.out.println(node);
						//System.out.println(cd.getCtrlGraph().getNodeFormatted(indexOfNodes.get(nodeSelectionList.getSelectedIndex()), "Paper"));
						//System.out.println(node);
						nodeNeeder.setNode(node);
						close();
					}
				}
				);		
	}
	
	private void setNodesList(int comboBoxIndex){
		int realIndex = comboBoxIndex >= CtrlDominio.getNodeTypeIndex("Paper") ?
				comboBoxIndex + 1 :  comboBoxIndex;
		String nodeType = CtrlDominio.getNodeTypeOfIndex(realIndex);
		drawList(nodeType);
	}
	
	private void drawList(String nodeType){
		nodeSelectionModel.clear();
		nodesInfo = cd.getCtrlGraph().getformattedNodesOfType(nodeType);		
		indexOfNodes = new ArrayList<Integer>();
		for (int i = 0; i < nodesInfo.size(); ++i) {
			if (nodesInfo.get(i).get(1).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
				nodeSelectionModel.addElement(nodesInfo.get(i).get(1));
				indexOfNodes.add(i);
			}
		}
	}
	
	public void setNeeder(INodeNeeder daddy, boolean isAPaper){
		if(isAPaper){
			initTypeComboBox();			
			setNodesList(typeComboBox.getSelectedIndex());
			searchTextField.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent e) {}
				public void keyPressed(KeyEvent e) {}
				public void keyReleased(KeyEvent e) {
					setNodesList(typeComboBox.getSelectedIndex());
				}});
		}
		else{
			initTypeLabel();
			drawList("Paper");		
			searchTextField.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent e) {}
				public void keyPressed(KeyEvent e) {}
				public void keyReleased(KeyEvent e) {
					drawList("Paper");		
				}});
		}
		nodeNeeder = daddy;
	}
	
	private void initTypeLabel(){
		springLayout.putConstraint(SpringLayout.NORTH, lblNodePaper, 10, SpringLayout.SOUTH, lblSelectANode);
		springLayout.putConstraint(SpringLayout.WEST, lblNodePaper, 10, SpringLayout.EAST, lblNodeType);
		this.add(lblNodePaper);
	}
	
	private void initTypeComboBox(){
		//Initializing the type combo box
		ArrayList<String> typeData = new ArrayList<String>();
		typeData.addAll(CtrlDominio.getTypes());
		//Remove Paper type nodes, because we'll only use the combo box w/ papers
		typeData.remove(CtrlDominio.getNodeTypeIndex("Paper"));
		String[] typeDataArray = typeData.toArray(new String[typeData.size()]); 
		typeComboBox = new JComboBox<String>(typeDataArray);
		typeComboBox.setEnabled(true);
		springLayout.putConstraint(SpringLayout.NORTH, typeComboBox, 10, SpringLayout.SOUTH, lblSelectANode);
		springLayout.putConstraint(SpringLayout.WEST, typeComboBox, 10, SpringLayout.EAST, lblNodeType);
		this.add(typeComboBox);
		typeComboBox.setSelectedIndex(0);
		typeComboBox.addActionListener(				
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						setNodesList(typeComboBox.getSelectedIndex());
					}
				});

	}	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
