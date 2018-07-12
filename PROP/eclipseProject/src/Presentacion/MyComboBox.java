package Presentacion;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import Dominio.CtrlDominio;
import Dominio.Node;

/**
 * Custom implementaton of <code>JComboBox</code> to filter the dropdown list relative to the typed text.<p>
 * Supports a parent from which to read events and select the corresponding list.
 * 
 * @author Xavier Peñalosa
 *
 */
public class MyComboBox extends JComboBox<String> implements ActionListener, KeyListener{


	private static final long serialVersionUID = 1L;
	private JComboBox<String> parentComboBox;
	
	private CtrlDominio cd = new CtrlDominio();
	private Boolean autocomplete;
	
	private ComboBoxModel<String>[][] rawStrings;
	private ComboBoxModel<String>[] filteredStrings;
	private Integer inCase = new Integer(-1);
	private Integer matchStart = new Integer(0);
	
	public MyComboBox(){
		super();
		
		initParams();
	}
	
	/**
	 * <b>Stub:</b> Gives the <code>JComboBox</code> a <code>CtrlDominio</code>
	 * instance in order to get the needed information for the different selections.
	 * 
	 * @param cd - CtrlDominio, used to request node information
	 */
	public void loadNodesToLists(CtrlDominio cd){
		this.cd = cd;
		initStrings(this.cd);
	}
	
	/**
	 * Adds a JComboString from which to check events. The handled events can be seen
	 * in <code>actionPerformed(ActionEvent)</code>
	 * 
	 * @param parent
	 * @see #actionPerformed(ActionEvent)
	 */
	public void linkToParentComboBox(JComboBox<String> parent){
		if (parent != null){
			ComboBoxModel parentCB = new DefaultComboBoxModel(new String[]{"Paper","Autor","Conferencia","Term"});
			parentComboBox = parent;
			parentComboBox.addActionListener(this);
			parentComboBox.setModel(parentCB);
			parentComboBox.setSelectedItem("- Node Type -");
		}
	}
	
	private void initParams(){
		autocomplete = false;
		
		setPreferredSize(new Dimension(160,20));
		setEditable(true);
		setEnabled(false);
		setFocusable(true);
		setSelectedIndex(-1);
		
		addActionListener(this);
		getEditor().getEditorComponent().addKeyListener(this);
	}
	
	/**
	 * Calling this function with a <b>true</b> value will autocomplete the
	 * items in the <code>JComboBox</code> once a match is found with more
	 * than 4 characters.<p> Calling this function with a <b>false</b> value
	 * will disable the automatic completion of the field.
	 * 
	 * @param b - New value
	 */
	public void setAutocomplete(Boolean b){
		autocomplete = b;
	}
	
	/**
	 * Gets the node names for the dropdown <code>JComboBox</code> and stores them in a
	 * <code>Model</code><p><b>Warning:</b><p>The strings must be in ascending order for
	 * the autocompletion to work 
	 * 
	 * @param cd - CtrlDominio, to which the node information is requested
	 * @see #setAutocomplete(Boolean)
	 */
	private void initStrings(CtrlDominio cd){
		
		filteredStrings = new DefaultComboBoxModel[2];
		rawStrings = new ComboBoxModel[2][4];

		
		ArrayList<ArrayList<String[]>> nodeNames = getNodeNamesFromComplexArrayList();
		rawStrings[0][0] = new DefaultComboBoxModel<String>(nodeNames.get(0).get(0));
		rawStrings[0][1] = new DefaultComboBoxModel<String>(nodeNames.get(0).get(1));
		rawStrings[0][2] = new DefaultComboBoxModel<String>(nodeNames.get(0).get(2));
		rawStrings[0][3] = new DefaultComboBoxModel<String>(nodeNames.get(0).get(3));
		
		
		rawStrings[1][0] = new DefaultComboBoxModel<String>(nodeNames.get(1).get(0));
		rawStrings[1][1] = new DefaultComboBoxModel<String>(nodeNames.get(1).get(1));
		rawStrings[1][2] = new DefaultComboBoxModel<String>(nodeNames.get(1).get(2));
		rawStrings[1][3] = new DefaultComboBoxModel<String>(nodeNames.get(1).get(3));
		
	}
	
	private ArrayList<ArrayList<String[]>> getNodeNamesFromComplexArrayList(){
		ArrayList<ArrayList<String[]>> ret = new ArrayList<ArrayList<String[]>>();

		ArrayList<String[]> p = auxGetNodeNames(cd.getCtrlGraph().getformattedNodesOfType("Paper"));
		ArrayList<String[]> a = auxGetNodeNames(cd.getCtrlGraph().getformattedNodesOfType("Autor"));
		ArrayList<String[]> c = auxGetNodeNames(cd.getCtrlGraph().getformattedNodesOfType("Conferencia"));
		ArrayList<String[]> t = auxGetNodeNames(cd.getCtrlGraph().getformattedNodesOfType("Terme"));
		
		
		ArrayList<String[]> mainIndex = new ArrayList<String[]>();
		ArrayList<String[]> mainString = new ArrayList<String[]>();
		
		if (p != null) {
			mainIndex.add(p.get(0));
			mainString.add(p.get(1));
		}
		else{
			String[] strings = new String[]{"None found!"};
			String[] indexs = new String[]{"-2"};
			mainIndex.add(indexs);
			mainString.add(strings);
		}
		if (a != null){
			mainIndex.add(a.get(0));
			mainString.add(a.get(1));
		}
		else{
			String[] strings = new String[]{"None found!"};
			String[] indexs = new String[]{"-2"};
			mainIndex.add(indexs);
			mainString.add(strings);
		}
		if (c != null){
			mainIndex.add(c.get(0));
			mainString.add(c.get(1));
		}
		else{
			String[] strings = new String[]{"None found!"};
			String[] indexs = new String[]{"-2"};
			mainIndex.add(indexs);
			mainString.add(strings);
		}
		if (t != null){
			mainIndex.add(t.get(0));
			mainString.add(t.get(1));
		}
		else{
			String[] strings = new String[]{"None found!"};
			String[] indexs = new String[]{"-2"};
			mainIndex.add(indexs);
			mainString.add(strings);
		}
		
		ret.add(mainIndex);
		ret.add(mainString);
		return ret;
		
	}
	private ArrayList<String[]> auxGetNodeNames(ArrayList<ArrayList<String>> alist){
		if (alist.isEmpty() || alist == null){
			return null;
		}
		else {
			ArrayList<String[]> ret = new ArrayList<String[]>();
			String[] strings = new String[alist.size()+1];
			String[] indexs = new String[alist.size()+1];
			strings[0] = " - Select all -";
			indexs[0] = "-1";
			for (int i = 0; i < alist.size(); ++i){
				strings[i+1] = alist.get(i).get(1);
				indexs[i+1] = alist.get(i).get(0);
			}

			ret.add(indexs);
			ret.add(strings);
			
			return ret;
		}
	}
	
	/**
	 * Add a new array of choices to the JComboBox
	 * 
	 * @param newItem - Array of strings to add
	 */
	public void addList(String[][] newItem){
		if (newItem != null){
			ComboBoxModel<String>[][] newRawStrings = new ComboBoxModel[2][rawStrings[1].length+1];
			for (int i = 0; i < rawStrings[1].length; ++i){
				String[] tempIndex = new String[rawStrings[0][i].getSize()];
				String[] tempString = new String[rawStrings[1][i].getSize()];
				for(int j = 0; j < rawStrings[1][i].getSize(); ++j){
					tempIndex[j] = rawStrings[0][i].getElementAt(j);
					tempString[j] = rawStrings[1][i].getElementAt(j);
				}
				newRawStrings[0][i] = new DefaultComboBoxModel<String>(tempString);
				newRawStrings[1][i] = new DefaultComboBoxModel<String>(tempString);
			}
			
			newRawStrings[0][newRawStrings[1].length-1] = new DefaultComboBoxModel<String>(newItem[0]);
			newRawStrings[1][newRawStrings[1].length-1] = new DefaultComboBoxModel<String>(newItem[1]);
			rawStrings = newRawStrings;
		}
	}
	/**
	 * Add a new array of choices to the JComboBox.
	 * 
	 * @param newItem - ArrayList of strings to add
	 */
	public void addList(ArrayList<String[]> newItem){
		String[][] thing = new String[2][newItem.size()];
		thing[0] = newItem.get(0);
		thing[1] = newItem.get(1);
		addList(thing);
	}
	
	/**
	 * Sets the ComboBoxModel stored in position <b>index</b>. This changes the dropdown list
	 * and therefore the options that can be selected.
	 * 
	 * @param index - Index of the ComboBoxModel to set
	 */
	public void setList(Integer index){
		if (index < rawStrings[1].length && index >= 0){
			if (inCase != index){
				inCase = index;
				setModel(rawStrings[1][index]);
				setSelectedIndex(0);
				getEditor().setItem(rawStrings[1][index].getElementAt(0));
			}
		}
		else throw new RuntimeException("Index out of bounds");
	}
	
	/**
	 * Returns the number of available lists in the JComboBox
	 * 
	 * @return The amount of lists currently stored
	 */
	public Integer getListAmount(){
		return rawStrings[1].length;
	}
	
	/**
	 * Returns the node index for the selected Node
	 * 
	 * @return Integer value for the node Index
	 */
	public Integer getSelectedNodeIndex(){
		if (getModel() == filteredStrings[1]){
			return Integer.valueOf(filteredStrings[0].getElementAt(getSelectedIndex()));
		}
		else {
			return Integer.valueOf(rawStrings[0][inCase].getElementAt(getSelectedIndex()));
		}
	}
	
	/**
	 * <b>Stub:</b> Gets an <code>ArrayList</code> of <code>String</code>s which corresponds to
	 * the names of the nodes in <u>alnode</u>.
	 * 
	 * @param alnode <code>Node</code> list from which we will get the names
	 * @return Array of node names as string
	 */
	private String[] getNodeNames(ArrayList<Node> alnode) {
		ArrayList<String> nodeNames = new ArrayList<String>();
		for (int i = 0; i < alnode.size(); ++i){
			nodeNames.add(alnode.get(i).getNom());
		}
		Collections.sort(nodeNames);
		return (String[]) nodeNames.toArray().clone();
	}
	
	/**
	 * Add all the <code>String</code>s that match the prefix typed in the <code>JComboBox</code>
	 * to the filtered <code>ArrayList</code>.<p> Calls a binary search to find the first element
	 * that matches (<u>start</u>) and scans lineally until the string doesn't match.
	 * 
	 * 
	 * @see #findStart(ComboBoxModel, String, int, int)
	 */
	private void updateSubstrings(){
		matchStart = findStart(rawStrings[1][inCase], getEditor().getItem().toString().toLowerCase(), 0, rawStrings[1][inCase].getSize()-1);
		if (matchStart >= 0 && matchStart < rawStrings[1][inCase].getSize() && filteredStrings != null){
			ArrayList<String> alist = new ArrayList<String>();
			ArrayList<String> indexs = new ArrayList<String>();
			
			while (matchStart < rawStrings[1][inCase].getSize() && rawStrings[1][inCase].getElementAt(matchStart).toLowerCase().startsWith(getEditor().getItem().toString().toLowerCase())){
				alist.add(rawStrings[1][inCase].getElementAt(matchStart));
				indexs.add(rawStrings[0][inCase].getElementAt(matchStart));
				++matchStart;
			}
			filteredStrings[0] = new DefaultComboBoxModel(indexs.toArray());
			filteredStrings[1] = new DefaultComboBoxModel(alist.toArray());
			
		}
		else if (matchStart == -1){
			auxSetModel(rawStrings[1][inCase]);
		}
		else {
			if (filteredStrings == null) throw new RuntimeException("\n\n ~MyComboBox exploded, filteredStrings is null~ \n");
			else throw new RuntimeException("\n\n ~MyComboBox exploded, matchStart out of bounds?~ \n");
		}
		showPopup();
	}
	
	/**
	 * Keeps the current text in display and updates the dropdown options with the ComboBoxModel <u>model</u>
	 * 
	 * @param model - Model to be set
	 */
	private void auxSetModel(ComboBoxModel<String> model){
		String temp = getEditor().getItem().toString();
		try{
			setModel(model);
		}
		catch (NullPointerException e){
			setModel(rawStrings[1][inCase]);
			//System.out.println(e.getMessage() + " exception happened");
		}
		getEditor().setItem(temp);
	}
	
	/**
	 * Recursive call to find the first element that starts with the <code>String</code> <u>text</u>
	 * 
	 * @param nodeNames - List of all the available strings
	 * @param text - The prefix we are looking for in the strings
	 * @param begin - Lower bound for the search
	 * @param end - Upper bound for the search
	 * @return The first element which starts with the substring <u>text</u>
	 */
	private Integer findStart(ComboBoxModel<String> nodeNames, String text, int begin, int end){
		if (begin == end || begin +1 == end){
			if (nodeNames.getElementAt(begin).toLowerCase().startsWith(text)) return begin;
			else if (nodeNames.getElementAt(end).toLowerCase().startsWith(text)) return end;
			else return -1;
		}
		else {
			int middle = (begin + end)/2;
			if (nodeNames.getElementAt(middle).compareToIgnoreCase(text) < 0){
				return findStart(nodeNames,text,middle,end);
			}
			else{
				return findStart(nodeNames,text,begin,middle);
			}
		}
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (parentComboBox != null && e.getSource().equals(parentComboBox)){
			int index = parentComboBox.getSelectedIndex() - 1;
			
			try{
				setList(index);
				setEnabled(true);
				setSelectedItem(rawStrings[1][index].getElementAt(-1));
			}
			catch (RuntimeException exc){
				String errorMsg = exc.getMessage();
				if (errorMsg== "Index out of bounds"){
					if (index == -1) { //Pick a type
						setSelectedIndex(-1);
						setEnabled(false);
					}
					else throw new RuntimeException(errorMsg);
				}
				else throw new RuntimeException(errorMsg);
			}
		}
	}
	
	

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * Used to control the dropdown menu and filter the options displayed.
	 * Ignores the keys with a default action (<code>Enter, Up, Down</code>)
	 * 
	 * @param e Key Event
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code != KeyEvent.VK_ENTER && code != KeyEvent.VK_UP && code != KeyEvent.VK_DOWN && code != KeyEvent.VK_LEFT && code != KeyEvent.VK_RIGHT){
			if (getEditor().getItem().toString().length() > 0){
				updateSubstrings();
				auxSetModel(filteredStrings[1]);
				if (autocomplete && filteredStrings[1].getSize()==1){
					getEditor().setItem(getSelectedItem());
				}
			}
			else {
				auxSetModel(rawStrings[1][inCase]);
			}
			
			showPopup();
		}
		else if (code == KeyEvent.VK_ENTER){
			if (getSelectedIndex() >= 0 && getModel() == filteredStrings[1]){
				setSelectedItem(filteredStrings[1].getElementAt(getSelectedIndex()));
				getEditor().setItem(getSelectedItem());
			}
		}
	}

}
