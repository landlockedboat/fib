package Presentacion;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JPanel;

import Dominio.CtrlDominio;


/**
 * 
 * @author Gonzalo Diez
 * 
 */

public class VistaPrincipal extends VistaAbstracta{
	
	private String APPLICATION_NAME = "ola k ase lol";
	
	// Componentes UI
//	private JFrame this = new JFrame(APPLICATION_NAME);
	
	// Panels;
	enum Panels {ModificaGraph, LoadResult, PanelMostrarResultado, NuevaBusqueda, LoadPaths, NewPath, Test, Exit};
	private Panels nextPanel, currentPanel;
	
	private JPanel content = new JPanel();
	PanelModificaGraph modificaGraph = new PanelModificaGraph(this);
	PanelLoadResult loadResult = new PanelLoadResult(this);
	PanelNuevaBusqueda nuevaBusqueda = new PanelNuevaBusqueda(this);
	PanelMostrarResultado panelMostrarResultado = new PanelMostrarResultado(this);
	PanelNewPath panelNewPath = new PanelNewPath(this);
	PanelLoadPaths panelLoadPaths = new PanelLoadPaths(this);

	// Menus
	private JMenuBar menuBar = new JMenuBar();
	
	  	// File
	
	// Graph
	private JMenu menuGraph = new JMenu("Graph");	
	private JMenuItem menuitemGraphNew = new JMenuItem("New");
	private JMenuItem menuitemGraphImport = new JMenuItem("Import Default");
	private JMenuItem menuitemGraphImportPath = new JMenuItem("Import...");
	private JMenuItem menuitemGraphLoad = new JMenuItem("Load");
	private JMenuItem menuitemGraphModify = new JMenuItem("Modify Current");
	
	// Paths
	private JMenu menuPath = new JMenu("Path");
	private JMenuItem menuitemPathNew = new JMenuItem("New");
	private JMenuItem menuitemPathShow = new JMenuItem("Show and Modify"); // Hacer vista (listar, modificar, eliminar)
	
	// Search and Results
	private JMenu menuSearch = new JMenu("Search and Results");
	private JMenuItem menuitemSearchNew = new JMenuItem("New Search");
	private JMenuItem menuitemResultShow = new JMenuItem("Show Results");
	
	private JMenu menuFile = new JMenu("File");
	
	private JMenuItem menuitemFileExit = new JMenuItem("Exit");
	
	
	// Constructor and public stuff
	
	public VistaPrincipal(CtrlDominio cdd) {
		super(cdd);
		initComponents();
		this.setSize(800, 600);
		this.setMinimumSize(new Dimension(800,600));
		this.setMaximumSize(new Dimension(800,600));
		this.setResizable(true);
		this.setEnabled(true);
		this.pack();
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//System.out.println("windowClosing");
				changePanel(Panels.Exit);
			}
		});
	}
	
	// listeners de los menus
	
	private void asign_listeners() {
		
		VistaPrincipal p = this;
	
		menuitemFileExit.addActionListener
		( new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				changePanel(Panels.Exit);
				
			}
		});
		
		menuitemPathNew.addActionListener
		(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel(Panels.NewPath);
			}
		});
		
		menuitemPathShow.addActionListener
		(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel(Panels.LoadPaths);
			}
		});
		
		menuitemGraphModify.addActionListener
	    (new ActionListener() {
	      public void actionPerformed (ActionEvent event) {
	        changePanel(Panels.ModificaGraph);
	      }
	    });
		
		menuitemResultShow.addActionListener
		(new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				changePanel(Panels.LoadResult);
			}
		});
		
		menuitemSearchNew.addActionListener
		(new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				changePanel(Panels.NuevaBusqueda);
			}
		});
		
		menuitemGraphImport.addActionListener
		(new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				int response = VistaDialog.setDialog("Import a new Graph", "Are you sure that you want to import a new graph?\n"
						+ "You will lose all unsaved changes made to the old graph", new String[]{"Continue", "Cancel"}, VistaDialog.DialogType.QUESTION_MESSAGE);
				if (0 == response) {
					try {
						//System.out.println(System.getProperty("user.dir"));
						String slash = (System.getProperty("os.name").toLowerCase().contains("windows") ? "\\" : "/");
						cd.importGraph(System.getProperty("user.dir")+slash+"DBLP_four_area"+slash);
						VistaDialog.setDialog("Success",  "Graph imported correctly\n"
								+ "The current panel may need to be reloaded for the changes to be committed\n"
								+ "It is recommended to press the continue button, because not doing so may lead to "
								+ "inconsistencies within the program.", new String[] {"Continue"}, VistaDialog.DialogType.INFORMATION_MESSAGE);
						if (currentPanel != null) p.changePanel(currentPanel);
					}
					catch (Exception e) {
						VistaDialog.setDialog("Error importing",
								"Failed to load the Graph.\n"
								+ "(Bad database directory file path) ", new String[]{"Continue"}, VistaDialog.DialogType.ERROR_MESSAGE);
					}
				}
//				VistaDialog.setDialog("Titulo", "¿Estas seguro que quieres salir?\n (Se perderan todo los cambios no guardados)", buttons, VistaDialog.DialogType.QUESTION_MESSAGE);
			}
		});
		
		menuitemGraphImportPath.addActionListener
		(new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				int response = VistaDialog.setDialog("Import a new Graph", "Are you sure that you want to import a new graph?\n"
						+ "You will lose all unsaved changes made to the old graph", new String[]{"Continue", "Cancel"}, VistaDialog.DialogType.QUESTION_MESSAGE);
				if (0 == response) {
					try {
						JFileChooser f  = new JFileChooser();
						f.setCurrentDirectory(new File(System.getProperty("user.dir")));
						f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						f.showOpenDialog(f);
						try {
							String s = f.getSelectedFile().getAbsolutePath();
							cd.importGraph(s);
							VistaDialog.setDialog("Success",  "Graph imported correctly\n"
									+ "The current panel may need to be reloaded for the changes to be committed\n"
									+ "It is recommended to press the continue button, because not doing so may lead to "
									+ "inconsistencies within the program.", new String[] {"Continue"}, VistaDialog.DialogType.INFORMATION_MESSAGE);
							if (currentPanel != null) p.changePanel(currentPanel);
						}
						catch (NullPointerException exception) {}
					}
					catch (Exception e) {
						VistaDialog.setDialog("Error", "No graph detected", new String[]{"Continue"}, VistaDialog.DialogType.ERROR_MESSAGE);
					}
				}
	//				VistaDialog.setDialog("Titulo", "¿Estas seguro que quieres salir?\n (Se perderan todo los cambios no guardados)", buttons, VistaDialog.DialogType.QUESTION_MESSAGE);
			}
		});
		
		menuitemGraphLoad.addActionListener
		(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response = VistaDialog.setDialog("Load a new Graph", "Are you sure that you want to load a new graph?\n"
						+ "You will lose all unsaved changes made to the old graph", new String[]{"Continue", "Cancel"}, VistaDialog.DialogType.QUESTION_MESSAGE);
				if (0 == response) {
					JFileChooser f  = new JFileChooser();
					f.setCurrentDirectory(new File(System.getProperty("user.dir")));
					f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					f.showOpenDialog(f);
					try {
						String s = f.getSelectedFile().getAbsolutePath();
						String[] parts = s.split("/");
						cd.loadGraph(parts[parts.length-1]);
						VistaDialog.setDialog("Success",  "Graph loaded correctly\n"
								+ "The current panel may need to be reloaded for the changes to be committed\n"
								+ "It is recommended to press the continue button, because not doing so may lead to "
								+ "inconsistencies within the program.", new String[] {"Continue"}, VistaDialog.DialogType.INFORMATION_MESSAGE);
						if (currentPanel != null) p.changePanel(currentPanel);
					}
					catch (Exception exception) {
						VistaDialog.setDialog("Loading Graph", "There is no need to be upset. There is no graph at that directory", new String[]{"Continue"}, VistaDialog.DialogType.ERROR_MESSAGE);
					}
				}
			}
		});
		
		menuitemGraphNew.addActionListener
		(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int response = VistaDialog.setDialog("Create a new Graph", "Are you sure that you want to create a new graph?\n"
						+ "You will lose all unsaved changes made to the old graph", new String[]{"Continue", "Cancel"}, VistaDialog.DialogType.QUESTION_MESSAGE);
				if (0 == response) {
					cd.createGraph();
					VistaDialog.setDialog("Success", "Graph correctly created", new String[]{"Continue"}, VistaDialog.DialogType.INFORMATION_MESSAGE);
				}
			}
		});
		
	}
	
	// private metods
	
	private void initComponents() {
		initFrame();
		initMenuBar();
		asign_listeners();
	}
	
	private void initFrame() {
		this.setSize(600, 400);
		this.setMinimumSize(this.getSize());
		this.setResizable(false);
		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		try {
			String path = System.getProperty("user.dir");
			String slash = (System.getProperty("os.name").toLowerCase().contains("windows") ? "\\" : "/");
			BufferedImage myPicture;
			myPicture = ImageIO.read(new File(path+slash + "resources" + slash + "Hetesim.png"));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			content.add(picLabel);
		} catch (IOException e) {
			//System.out.println("Couldn't load Hetesim image");
		}
		
		//set the first contentPanel
		JPanel contentPane = (JPanel) this.getContentPane();
		contentPane.add(content);
		contentPane.setBackground(Color.blue);
	}
	
	private void initMenuBar() {
		
		menuFile.add(this.menuitemFileExit);

		menuGraph.add(this.menuitemGraphNew);
		menuGraph.add(this.menuitemGraphImport);
		menuGraph.add(this.menuitemGraphImportPath);
		menuGraph.add(this.menuitemGraphLoad);
		menuGraph.add(this.menuitemGraphModify);
		
		menuPath.add(this.menuitemPathNew);
		menuPath.add(this.menuitemPathShow);
		
		menuSearch.add(this.menuitemSearchNew);
		menuSearch.add(this.menuitemResultShow);
		
		menuBar.add(this.menuFile);
		menuBar.add(this.menuGraph);
		menuBar.add(this.menuPath);
		menuBar.add(this.menuSearch);

		this.setJMenuBar(menuBar);
	}
	
	// Stuff
	
	void changePanel(Panels p) {
		//System.out.println("Cambiando a panel " + p.toString());
		nextPanel = p;
		try {
			AbstractPanel currentPanel = (AbstractPanel) this.getContentPane().getComponent(0);
			if (0 == currentPanel.close()) {
//				continueAction();
			}
		}
		catch (Exception e) {
			continueAction();
		}
	}
	
	/**
	 * No llameis a esto pl0x lo llama el AbstractPanel.
	 * @see Presentacion.VistaAbstracta#continueAction()
	 */
	
	@Deprecated
	void continueAction() {
		this.getContentPane().removeAll();
		currentPanel = nextPanel;
		switch (nextPanel) {
			case ModificaGraph:
				modificaGraph.init();
				this.getContentPane().add(modificaGraph);
				break;
			case Test:
				this.getContentPane().add(content);
				break;
			case LoadResult:
				loadResult.init();
				this.getContentPane().add(loadResult);
				break;
			case NuevaBusqueda:
				nuevaBusqueda.init();
				this.getContentPane().add(nuevaBusqueda);
				break;
			case PanelMostrarResultado:
				panelMostrarResultado.init();
				this.getContentPane().add(panelMostrarResultado);
				break;
			case NewPath:
				panelNewPath.init();
				this.getContentPane().add(panelNewPath);
				break;
			case LoadPaths:
				panelLoadPaths.init();
				this.getContentPane().add(panelLoadPaths);
				break;
			case Exit:
				dispose();
				System.exit(0);
			default:
				throw new RuntimeException("Ese panel no existe");
		}
//		this.pack();
		this.show();
		this.repaint();
	}
	
	void setEnabledPrincipal(Boolean b) {
		//System.out.println("lalala");
//		menuFile.setEnabled(b);
//		menuEdit.setEnabled(b);
	}

}
