package Presentacion;

import java.awt.AWTKeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * @author Gonzalo Diez
 * 
 */

public class VistaDialog {
	
	public enum DialogType {
		ERROR_MESSAGE(JOptionPane.ERROR_MESSAGE),
	    INFORMATION_MESSAGE(JOptionPane.INFORMATION_MESSAGE),
	    WARNING_MESSAGE(JOptionPane.WARNING_MESSAGE),
	    QUESTION_MESSAGE(JOptionPane.QUESTION_MESSAGE), 
	    PLAIN_MESSAGE(JOptionPane.PLAIN_MESSAGE);
		
		private int value;
		private DialogType(int value) {
			this.value = value;
		}
	}

	public static int setDialog(String strTitulo, String strTexto, String[] strBotones, DialogType iTipo) {

		// Crea y viisualiza el dialogo
		JOptionPane optionPane = new JOptionPane(strTexto,iTipo.value);
		optionPane.setOptions(strBotones);
		JFrame frame = new JFrame();
		JDialog dialogOptionPane = optionPane.createDialog(frame,strTitulo);
		dialogOptionPane.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialogOptionPane.pack();
		
		Set<AWTKeyStroke> focusTraversalKeys = new HashSet<AWTKeyStroke>(dialogOptionPane.getFocusTraversalKeys(0));
	    focusTraversalKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.VK_UNDEFINED));
	    dialogOptionPane.setFocusTraversalKeys(0, focusTraversalKeys);
	    

		dialogOptionPane.setVisible(true);

		// Captura la opcion elegida
		try {
			String vsel = (String) optionPane.getValue();
			int isel;
			for (isel = 0;
					isel < strBotones.length && !strBotones[isel].equals(vsel); isel++);
			return isel;
		}
		catch (Exception e) {
			return 0;
		}
	}
}
