package Presentacion;

import Dominio.CtrlDominio;

/**
 * 
 * @author Gonzalo Diez
 * 
 */

public class CtrlPresentacion {
	
	CtrlDominio ctrlDominio;
	VistaPrincipal vp;
	
	public CtrlPresentacion() {
		ctrlDominio = new CtrlDominio();
		vp = new VistaPrincipal(ctrlDominio);
	}
}
