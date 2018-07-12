package Presentacion;

import javax.swing.JFrame;

import Dominio.CtrlDominio;

/**
 * 
 * @author Gonzalo Diez
 * 
 */

@SuppressWarnings("serial")
abstract public class VistaAbstracta extends JFrame {
	protected CtrlDominio cd;
	
	VistaAbstracta(CtrlDominio cd) {
		this.cd = cd;
	}
	
	/**
	 * No llameis a esto pl0x lo llama el AbstractPanel.
	 * @see Presentacion.VistaAbstracta#continueAction()
	 */
	
	@Deprecated
	abstract void continueAction();
//	abstract public void setEnabledEverything(Boolean b);
	public CtrlDominio getCtrlDominio() {
		return cd;
	}
}
