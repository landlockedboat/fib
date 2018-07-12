package Presentacion;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import Presentacion.VistaPrincipal.Panels;

/**
 * 
 * @author Gonzalo Diez
 * 
 */

public class PanelNewPath extends AbstractPanel { //Abstract
//	public PanelNewPath() {
//	}

	JTextField textFieldName;
	JTextField textFieldDescription;
	JTextField textField;
	
	JButton buttonConf;
	JButton buttonAuthor;
	JButton buttonPaper;
	JButton buttonTerm;
	JButton btnSave;
	JButton btnCancel;
	
	JLabel tittleLabel;
	
	boolean finished;
	boolean editing = false;
	
	public PanelNewPath(VistaAbstracta vp) {
		super(vp);
	}
	
	public void init() {
//	PanelNewPath() {
		this.removeAll();
		finished = false;
		this.setSize(vp.getSize());
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut);
		
		tittleLabel = new JLabel("New Path");
		tittleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		tittleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(tittleLabel);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		add(verticalStrut_1);
		
		JPanel panelNameDescription = new JPanel();
		panelNameDescription.setAlignmentY(Component.TOP_ALIGNMENT);
		add(panelNameDescription);
		panelNameDescription.setLayout(new BoxLayout(panelNameDescription, BoxLayout.X_AXIS));
		

		Component horizontalGlue = Box.createHorizontalGlue();
		panelNameDescription.add(horizontalGlue);
		
		JLabel lblNewLabel = new JLabel("Name");
		panelNameDescription.add(lblNewLabel);
		
		textFieldName = new JTextField();
		textFieldName.setToolTipText("Name of the path");
		panelNameDescription.add(textFieldName);
		textFieldName.setColumns(10);
		
		panelNameDescription.add(horizontalGlue);
		
		JLabel lblNewLabel_1 = new JLabel("Description");
		panelNameDescription.add(lblNewLabel_1);
		
		textFieldDescription = new JTextField();
		textFieldDescription.setToolTipText("Info about the path");
		panelNameDescription.add(textFieldDescription);
		textFieldDescription.setColumns(10);
		
		panelNameDescription.add(horizontalGlue);
		
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		add(verticalStrut_2);
		
		JPanel panelContent = new JPanel();
		add(panelContent);
		panelContent.setLayout(new GridLayout(5, 3, 0, 5));
		
		buttonAuthor = new RoundButton("Author");
		
		buttonAuthor.setBackground(new Color(230, 230, 250));
//			buttonAuthor.setBackground(bg);
		panelContent.add(buttonAuthor);
		
		JLabel label = new JLabel("");
		panelContent.add(label);
		
		buttonConf = new RoundButton("Conference");
		
		buttonConf.setBackground(new Color(230, 230, 250));
		panelContent.add(buttonConf);
		
		
		JLabel label_1 = new JLabel("");
		panelContent.add(label_1);
		
		buttonPaper = new RoundButton("Paper");
		
		buttonPaper.setBackground(new Color(230, 230, 250));
		panelContent.add(buttonPaper);
		
		JLabel label_2 = new JLabel("");
		panelContent.add(label_2);
		
		
		JLabel label_3 = new JLabel("");
		panelContent.add(label_3);
		
		buttonTerm = new RoundButton("Term");
		
		buttonTerm.setBackground(new Color(230, 230, 250));
		panelContent.add(buttonTerm);
		
		JLabel label_4 = new JLabel("");
		panelContent.add(label_4);
		
		
		JLabel label_5 = new JLabel("");
		panelContent.add(label_5);
		
		JLabel label_6 = new JLabel("");
		panelContent.add(label_6);
		
		JLabel label_7 = new JLabel("");
		panelContent.add(label_7);
		
		
		
		JLabel label_8 = new JLabel("");
		panelContent.add(label_8);
		
		JPanel panel = new JPanel();
		panelContent.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel label_9 = new JLabel("Content");
		panel.add(label_9);
		
		textField = new JTextField();
		DocumentFilter df = new PathDocumentFilter();
		((AbstractDocument) textField.getDocument()).setDocumentFilter(df);
		
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel label_10 = new JLabel("");
		panelContent.add(label_10);
		
		Component verticalGlue = Box.createVerticalGlue();
		add(verticalGlue);
		
		Component verticalStrut_3 = Box.createVerticalStrut(20);
		add(verticalStrut_3);
		
		JPanel panelSaveCancel = new JPanel();
		add(panelSaveCancel);
		panelSaveCancel.setLayout(new BoxLayout(panelSaveCancel, BoxLayout.X_AXIS));
		
		btnSave = new JButton("Save and Return");
		panelSaveCancel.add(btnSave);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panelSaveCancel.add(horizontalStrut);
		
		btnCancel = new JButton("Cancel");
		panelSaveCancel.add(btnCancel);		
		
		addListeners();
	}

//	@Override
	int closeIt() {
		if (!finished) {
			String[] buttons = {"Exit", "Cancel"};
			int result = VistaDialog.setDialog("Exit without saving", "Are you sure do you want to exit without saving?", buttons, VistaDialog.DialogType.QUESTION_MESSAGE);
			return result;
		}
		return 0;
	}

//	@Override
	public void setEnabledEverything(Boolean b) {
		// TODO Auto-generated method stub

	}
	
	private void addListeners() {
		buttonAuthor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!buttonAuthor.isEnabled()) return;
				DocumentFilter aux = ((AbstractDocument) textField.getDocument()).getDocumentFilter();
				((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter());
				textField.setText(textField.getText()+'A');
				((AbstractDocument) textField.getDocument()).setDocumentFilter(aux);
				
				buttonConf.setEnabled(false);
				buttonAuthor.setEnabled(false);
				buttonTerm.setEnabled(false);
				buttonPaper.setEnabled(true);
			}
		});
		buttonConf.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!buttonConf.isEnabled()) return;
				DocumentFilter aux = ((AbstractDocument) textField.getDocument()).getDocumentFilter();
				((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter());
				textField.setText(textField.getText()+'C');
				((AbstractDocument) textField.getDocument()).setDocumentFilter(aux);
				
				buttonConf.setEnabled(false);
				buttonAuthor.setEnabled(false);
				buttonTerm.setEnabled(false);
				buttonPaper.setEnabled(true);
			}
		});
		buttonPaper.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!buttonPaper.isEnabled()) return;
				DocumentFilter aux = ((AbstractDocument) textField.getDocument()).getDocumentFilter();
				((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter());
				textField.setText(textField.getText()+'P');
				((AbstractDocument) textField.getDocument()).setDocumentFilter(aux);
				
				buttonConf.setEnabled(true);
				buttonAuthor.setEnabled(true);
				buttonTerm.setEnabled(true);
				buttonPaper.setEnabled(false);
			}
		});
		
		buttonTerm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!buttonTerm.isEnabled()) return;
				DocumentFilter aux = ((AbstractDocument) textField.getDocument()).getDocumentFilter();
				((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter());
				textField.setText(textField.getText()+'T');
				((AbstractDocument) textField.getDocument()).setDocumentFilter(aux);
				
				buttonConf.setEnabled(false);
				buttonAuthor.setEnabled(false);
				buttonTerm.setEnabled(false);
				buttonPaper.setEnabled(true);
				
			}
		});
		textField.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				boolean good = isGoodPath();
				if (!good) {
					textField.setToolTipText("The path is badly formatted");
					textField.setBackground(new Color(246,100,100));
				}
				else {
					textField.setToolTipText("");
					textField.setBackground(Color.white);
				}
				enablePathButtons();
				
			}
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
			
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!editing) {
					boolean hasName = (textFieldName.getText().length()>0);
					boolean existName = cd.getCtrlPaths().existsPath(textFieldName.getText());
					boolean hasDes = (textFieldDescription.getText().length()>0);
					boolean goodPath = isGoodPath() && textField.getText().length() >= 2;
					String name = "The path needs a Name.\n";
					String nameRepeated = "The Name of the panel can't be repeated.\n";
					String des = "The path needs a Description.\n";
					String cont = "The path needs to be at least of leght 2 and nicely formated.\n";
					if (hasName && hasDes && goodPath && !existName){
						System.out.println(textField.getText() + " " + textFieldName.getText() + " " + textFieldDescription.getText());
						cd.getCtrlPaths().addPath(textField.getText(), textFieldName.getText(), textFieldDescription.getText());
						VistaDialog.setDialog("New Path", "The path was created correctly", null, VistaDialog.DialogType.INFORMATION_MESSAGE);
						finished = true;
						try {
							((VistaPrincipal)vp).changePanel(Panels.LoadPaths);
						}
						catch(Exception ex) {
							vp.dispose();
						}
					}
					else {
						String response = "Path can't be saved because of the following:\n";
						if (!hasName) response += name;
						if (existName) response += nameRepeated;
						if (!hasDes) response += des;
						if (!goodPath) response += cont;
						VistaDialog.setDialog("New Path", response, null, VistaDialog.DialogType.ERROR_MESSAGE);
					}
				}
				else {
					boolean goodPath = isGoodPath() && textField.getText().length() >= 2;
					String cont = "The path needs to be at least of leght 2 and good formated\n";
					if (goodPath){
						cd.getCtrlPaths().modifyPath(textFieldName.getText(),textField.getText());
						VistaDialog.setDialog("Edit Path", "The path was modified correctly", null, VistaDialog.DialogType.INFORMATION_MESSAGE);
						finished = true;
						try {
							((VistaPrincipal)vp).changePanel(Panels.LoadPaths);
						}
						catch(Exception ex) {
							((VistaPrincipal)((VistaSecundaria)vp).parent.vp).changePanel(Panels.LoadPaths);
							vp.dispose();
						}
					}
					else {
						String response = "Path can't be saved because of the following:\n";
						if (!goodPath) response += cont;
						VistaDialog.setDialog("Edit Path", response, null, VistaDialog.DialogType.ERROR_MESSAGE);
					}
				}
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					((VistaPrincipal)vp).changePanel(Panels.LoadPaths);
				}
				catch(Exception ex) {
					vp.dispose();
				}
			}
		});
	}
	
	boolean isGoodPath() {
		String s = textField.getText();
		boolean good = true;
		for (int i = 1; i < s.length(); ++i) {
			switch (s.charAt(i-1)){
				case 'T':
				case 'C':
				case 'A':
					if (s.charAt(i) != 'P') good = false;
					break;
				case 'P':
					if (s.charAt(i) == 'P') good = false;
				default:
					break;
				
			}
			if (!good) break;
		}
		return good;
	}
	
	public void enablePathButtons() {
		if (textField.getText().length() > 0) {
			char s = textField.getText().charAt(textField.getText().length()-1);
			//System.out.println(s);
			switch (s) {
				case 'A':
				case 'T':
				case 'C':
					buttonConf.setEnabled(false);
					buttonAuthor.setEnabled(false);
					buttonTerm.setEnabled(false);
					buttonPaper.setEnabled(true);
					break;
				case 'P':
					buttonConf.setEnabled(true);
					buttonAuthor.setEnabled(true);
					buttonTerm.setEnabled(true);
					buttonPaper.setEnabled(false);
					break;
				default:
					buttonConf.setEnabled(false);
					buttonAuthor.setEnabled(false);
					buttonTerm.setEnabled(false);
					buttonPaper.setEnabled(false);
					break;
			}
		}
		else {
			buttonConf.setEnabled(true);
			buttonAuthor.setEnabled(true);
			buttonTerm.setEnabled(true);
			buttonPaper.setEnabled(true);
		}
	}

}



class PathDocumentFilter extends DocumentFilter {
	@Override
	public void replace(FilterBypass fb, int i, int i1, String string, AttributeSet as) throws BadLocationException {
		 for (int n = string.length(); n > 0; n--) {
            char c = Character.toUpperCase(string.charAt(n - 1));
            if (c == 'A' || c == 'T' || c == 'P' || c == 'C')
                super.replace(fb, i, i1, String.valueOf(c), as);
        }
	}
}
