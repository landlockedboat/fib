package Presentacion;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import Dominio.CtrlDominio;

/**
 * 
 * @author Gonzalo Diez
 * 
 */

public class VistaSecundaria extends VistaAbstracta {

	AbstractPanel parent;
	
	VistaSecundaria(CtrlDominio cd, AbstractPanel p, boolean bloqueante) {
		super(cd);
		parent = p;
		parent.setEnabledEverything(!bloqueante);
		VistaSecundaria vs = this;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//System.out.println("windowClosing");
				AbstractPanel currentPanel = (AbstractPanel) vs.getContentPane().getComponent(0);
				if (0 == currentPanel.close()) continueAction();
			}
		});
	}
	
	@Override
	void continueAction() {
		//System.out.println("continueAction");
		parent.setEnabledEverything(true);
		parent.childs.remove(this);
		this.removeAll();
		dispose();
	}	

}
