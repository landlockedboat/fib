package Presentacion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Dominio.CtrlDominio;

/**
 * 
 * @author Gonzalo Diez
 * 
 */
abstract public class AbstractPanel extends JPanel {
	
	VistaAbstracta vp;
	ArrayList<VistaAbstracta> childs;
	CtrlDominio cd;
	
	public AbstractPanel(VistaAbstracta vp) {
		this.vp = vp;
		childs = new ArrayList<VistaAbstracta>();
		cd = vp.getCtrlDominio();
	}
	
	/** 
	 * Esta funcion se llama desde la vista principal 
	 * por si se quiere inicializar algo nuevo cuando se va a llamar al panel
	 */
	public void init() {}
	
	public int close() {
		int ret = 0;
		int currentChild = 0;
		//System.out.println("Childs :" + childs.size());
		
		while (0 == ret && currentChild < childs.size()) {
			childs.get(currentChild).toFront();
			// En windows el toFront no te pone la ventana deltante.
			childs.get(currentChild).setAlwaysOnTop(true);
			childs.get(currentChild).setAlwaysOnTop(false);
			AbstractPanel panel = (AbstractPanel) childs.get(0).getContentPane().getComponent(0);
			ret = panel.close();
		}
		if (0 == ret) {
			ret = closeIt();
			if (0 == ret) vp.continueAction();
		}
		return ret;
		
	}
	
	/**
	 * If you can close, return 0. If not, return watever;
	 */
	abstract int closeIt(); 
	
	abstract public void setEnabledEverything(Boolean b);
	
	void addVista(Class<?> clas, boolean bloqueante) {
		VistaSecundaria newView = new VistaSecundaria(cd,this, bloqueante);
		newView.setSize(800, 600);
		newView.setMinimumSize(newView.getSize());
//		newView.setResizable(false);
		
		newView.setLocationRelativeTo(null);
		newView.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//set the first contentPanel
		JPanel contentPane = (JPanel) newView.getContentPane();
		
		//System.out.println(clas);
		try {
//			Class<?> clas = Class.forName(className);
			Constructor<?> construct = clas.getConstructor(VistaAbstracta.class);
			AbstractPanel p = (AbstractPanel) construct.newInstance(new Object[] {newView} );
			contentPane.add(p);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		
		newView.setEnabled(true);
//		newView.pack();
		newView.show();
		newView.setVisible(true);
		
		childs.add(newView);
	}
	
}
