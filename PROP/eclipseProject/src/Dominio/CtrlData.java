

package Dominio;

import java.util.ArrayList;

import Persistencia.CtrlDataGraph;
import Persistencia.LoadStorePath;
import Persistencia.LoadStoreResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
*
* @author Albert Lopez Alcacer
* 
* Esta clase es un controlador que ofrece las funciones de tres gestores de datos: CtrlDataGraph, LoadStoreResult y LoadStorePath. Además de eso
* implementa una función de copia en profundidad, para pasar copias de objetos independientes a los gestores.
* Por otra parte se encarga de organizar los paths ,grafos y resultados: Crea la carpeta Paths (carpeta donde se guardaran los paths que el dominio genere, y de la
* que se cargarán cuando sea necesario) y la carpeta GrafsAndResults (carpeta que contendrá una carpeta diferente para cada grafo junto con sus 
* resultados asociados)
*/

public class CtrlData {
	private static final long serialVersionUID = 1L;
	
	private CtrlDataGraph cg;
	private LoadStoreResult lsr;
	private LoadStorePath lsp;
	
	private Path pathToGrafsAndResults; //Path del directorio donde siempre se guardan las carpetas: (idGrafo ,que tiene dentro: grafo y sus resultados)
	private Path pathToPaths;		  //Path del directorio donde siempre guardamos los Paths.
	private Pair<Graph,ArrayList<Result>> graphAndResults;
	private ArrayList<Dominio.Path> allPaths;

	/**
	 * Realiza una copia en profundidad del objeto pasado como parámetro.
	 * @param o > Objeto a copiar
	 * @return Objeto > Objeto que será una copia independiente del objeto o 
	 */
	private static Object deepCopy(Object o) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}catch (NotSerializableException theProblem) {
			//System.out.println("El objeto no es serializable");
			return null;
		} catch (IOException e) {
			//System.out.println("Se ha producido un problema con la operación de E/S");
			return null;
		} catch (ClassNotFoundException e) {
			//System.out.println("No hay definición para la clase especificada");
			return null;
		} catch (Exception e) {
			//System.out.println("Ha habido algun problema con la funcion de deepCopy");
			return null;
		}
	}
	
	/**
	 * Método privado que crea un directorio en caso de que no exista
	 * @param p > Ruta donde se quiere crear el directorio
	 */
	private static void checkSubdirectory(Path p) {
		File f = new File(p.toString());
		f.setExecutable(true);
		f.setReadable(true);
		f.setWritable(true);
		if (!f.exists()) {
			try {
				f.mkdirs();
			}
			catch(SecurityException se) {
				//System.out.println("No se puede crear el directorio");
			}
		}
	}
	/**
	 * Creadora por defecto. Genera los dos directorios (Paths) (GrafsAndResults) en caso de no existir. y inicializa
	 * las rutas de dichos directorios.
	 */
	public CtrlData() {
		Path cwd = Paths.get(System.getProperty("user.dir"));
		Path Paths = cwd.resolve("Paths");
		File Pathsf = new File(Paths.toString());
		Pathsf.setReadable(true);
		Pathsf.setWritable(true);
		Path GrafsAndResults = cwd.resolve("GrafsAndResults");
		File GrafsAndResultsf = new File(GrafsAndResults.toString());
		if (!Pathsf.exists()) {
			try {
				Pathsf.mkdirs();
			}
			catch(SecurityException se) {
				//System.out.println("No se puede crear el directorio con los Paths");
			}
		}
		if (!GrafsAndResultsf.exists()) {
			try {
				GrafsAndResultsf.mkdirs();
			}
			catch(SecurityException se) {
				//System.out.println("No se puede crear el directorio GrafAndResults");
			}
		}
		
		this.pathToGrafsAndResults = GrafsAndResults;
		this.pathToPaths = Paths;	

	}
	
	/**
	 * Getter de la ruta a la carpeta Paths
	 * @return String > String que representa la ruta a la carpeta Paths.
	 */
	public String getPathtoPaths() {
		return pathToPaths.toString();
	}
	
	/**
	 * Getter de la ruta a la carpeta GrafsAndResults
	 * @return String > String que representa la ruta a la carpeta GrafsAndResults
	 */
	public String getPathtoGraphsAndResult() {
		return pathToGrafsAndResults.toString();
	}
	
	/**
	 * Método que permite cargar un grafo y sus resultados asociados.
	 * @param idGraf > String que identifica el grado guardado.
	 * @return pair<Graf,ArrayList<Result>> > Pair que contiene el grafo con id idGraf y un ArrayList con sus resultados asociados.	
	 */
	public  Pair<Graph,ArrayList<Result>> loadgraphAndResults(String idGraf) throws ClassNotFoundException, FileNotFoundException, IOException {
		Path p = Paths.get(pathToGrafsAndResults.toString());
		p = p.resolve(idGraf);
		checkSubdirectory(p);
		cg = new CtrlDataGraph();
		lsr = new LoadStoreResult(p.toString());
		
		Graph g = new Graph();
		ArrayList<Result> results = new ArrayList<Result>();
		graphAndResults = new Pair<Graph,ArrayList<Result>>(g, results);
		
		graphAndResults.second = lsr.LoadAllResults();
		graphAndResults.first = cg.loadGraph(pathToGrafsAndResults.resolve(idGraf).resolve(idGraf).toString());
		return graphAndResults;
	}
	/**
	 * Método que permite cargar todos los paths previamente guardados.
	 * @return ArrayList<Dominio.Path> > ArrayList de Paths del dominio que contiene todos los Paths almacenados anteriormente.	
	 */
	public ArrayList<Dominio.Path> loadallPaths() throws ClassNotFoundException, IOException {
		allPaths = new ArrayList<Dominio.Path>();
		lsp = new LoadStorePath(pathToPaths.toString());
		allPaths = lsp.loadAllPaths();
		return allPaths;
	}
	/**
	 * Método que permite almacenar un resultado.
	 * @param r > Resultado que se quiere almacenar
	 */
	public void storeResult(Result r) throws FileNotFoundException, CloneNotSupportedException, IOException{
		Path p = Paths.get(pathToGrafsAndResults.toString());
		p = p.resolve(r.getIdGraf());
		checkSubdirectory(p);
		Result res; 
		res = (Result)CtrlData.deepCopy(r);
		lsr = new LoadStoreResult(p.toString());
		lsr.storeResult(res);
	}
	/**
	 * Método que permite almacenar un Path.
	 * @param p > Path que se desea almacenar
	 */
	public void storePath(Dominio.Path p) throws FileNotFoundException, CloneNotSupportedException, IOException {
		Dominio.Path pa;
		pa = (Dominio.Path) CtrlData.deepCopy(p);
		lsp = new LoadStorePath(pathToPaths.toString());
		lsp.storePath(pa);
	}
	/**
	 * Método que permite almacenar un Grafo
	 * @param g > Graf que se desea almacenar.
	 */
	public void storeGraf(Graph g) throws FileNotFoundException, IOException {
		Path p = Paths.get(pathToGrafsAndResults.toString());
		Graph gc = (Graph) CtrlData.deepCopy(g);
		p = p.resolve(String.valueOf(g.id));
		checkSubdirectory(p);
		cg = new CtrlDataGraph();
		cg.saveGraph(gc, p.resolve(String.valueOf(gc.id)).toString());
	}
	/**
	 * Método que permite cargar un único resultado.
	 * @param idResult > String que identifica el resultado
	 * @param idGraf > String que identifica el grafo al que esta asociado el resultado.
	 * @return Result > Resultado que se desea cargar.
	 */
	public Result loadResult(String idResult, Integer idGraf) throws FileNotFoundException, ClassNotFoundException, IOException {
		Path p = Paths.get(pathToGrafsAndResults.toString());
		p = p.resolve(String.valueOf(idGraf));
		checkSubdirectory(p);
		lsr = new LoadStoreResult(p.toString());
		Result r = lsr.loadResult(idResult);
		return r;
	}
	/**
	 * Método que permite cargar un único Path.
	 * @param nomPath > String que identifica el path guardado
	 * @return Path > Path del dominio que se desea cargar
	 */
	public Dominio.Path loadPath(String nomPath) throws FileNotFoundException, ClassNotFoundException, IOException {
		Dominio.Path p = new Dominio.Path();
		lsp = new LoadStorePath(pathToPaths.toString());
		p = lsp.loadPath(nomPath);
		return p;
	}
	/**
	 * Método que permite borrar un único Path
	 * @param nomPath > String que identifica el path que se desea eliminar
	 */
	
	public void deletePath(String nomPath) throws ClassNotFoundException, IOException {
		lsp = new LoadStorePath(pathToPaths.toString());
		lsp.deletePath(nomPath);
	}
	/**
	 * Método que permite eliminar un único Resultado
	 * @param idResult > String que identifica el resultado que se desea eliminar
	 * @param idGraf > String que identifica el grafo al que esta asociado el resultado.
	 */
	public void deleteResult(String idResult, Integer idGraf) throws Exception {
		Path p = Paths.get(pathToGrafsAndResults.toString());
		p = p.resolve(String.valueOf(idGraf));
		checkSubdirectory(p);
		lsr = new LoadStoreResult(p.toString());
		lsr.deleteResult(idResult);
	}
	/**
	 * Método que permite comprobar si exsite un grafo
	 * @param filePath > String que indica la ruta a comprobar.
	 * @return boolean que indica que existe si cierto. No existe en caso contrario.
	 */

	public boolean checkGraphFile(String filePath) {
		cg = new CtrlDataGraph();
		return cg.checkGraphFile(filePath);
	}
}
