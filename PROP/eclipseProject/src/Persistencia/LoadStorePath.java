package Persistencia;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.util.ArrayList;

import Dominio.Path;

/**
*
* @author Albert Lopez Alcacer
*/


public class LoadStorePath implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private java.nio.file.Path pathsDirectory; 

	public LoadStorePath(String pathsDirectory) throws NotDirectoryException {
		this.setpathsDirectory(pathsDirectory);
	}
	public String getpathsDirectory() {
		return pathsDirectory.toString();
	}
	public void setpathsDirectory(String pathsDirectory) throws NotDirectoryException {
		java.nio.file.Path p = Paths.get(pathsDirectory);
		if (Files.exists(p))
				this.pathsDirectory = Paths.get(pathsDirectory);
		else throw new NotDirectoryException("No existe el directorio");
	}
	
	public void storePath(Path p) throws IOException {
		try {
			FileOutputStream FileOutput = new FileOutputStream(pathsDirectory.resolve(p.getNom()+".ser").toString());
			ObjectOutputStream ObjectOutput = new ObjectOutputStream(FileOutput);
			ObjectOutput.writeObject(p);
			ObjectOutput.close();
		}
		catch(FileNotFoundException fnfe){
			//System.out.println("Path absoluto del path:"+pathsDirectory.resolve(p.getNom()+".ser").toString());
			//System.out.println("No se puede guardar el Path");
		}

	}
	
	public void deletePath(String nomPath) throws ClassNotFoundException, IOException {
		File file = new File(pathsDirectory.resolve(nomPath+".ser").toString());
		if(!file.exists()) throw new FileNotFoundException("No existe el Path con ese nombre");
		if(!file.delete()){
			//System.out.println("No se ha podido borrar el Path");
			}
	}

	public Dominio.Path loadPath(String nomPath) throws IOException, ClassNotFoundException {
		try {
			FileInputStream FileInput = new FileInputStream(pathsDirectory.resolve(nomPath+".ser").toString());
			ObjectInputStream ObjectInput = new ObjectInputStream(FileInput);
			Object aux = ObjectInput.readObject();
			ObjectInput.close();
			return (Dominio.Path)aux;
		}
		catch(FileNotFoundException fnfe){
			if (Files.notExists(pathsDirectory.resolve(nomPath+".ser"))) {
				//System.out.println("Path absoluto del Path: "+pathsDirectory.resolve(nomPath+".ser").toString());
				//System.out.println("No se ha podido cargar el Path");
			}
			return null;
		}

	}
	
	public ArrayList<Dominio.Path> loadAllPaths () throws ClassNotFoundException {
		ArrayList<Dominio.Path> Paths = new ArrayList<Dominio.Path>();   
		try (DirectoryStream<java.nio.file.Path> directoryStream = Files.newDirectoryStream(pathsDirectory)) {
	            for (java.nio.file.Path path : directoryStream) {
	            	FileInputStream FileInput = new FileInputStream(path.toString());
	    			ObjectInputStream ObjectInput = new ObjectInputStream(FileInput);
	    			Object aux = ObjectInput.readObject();
	    			ObjectInput.close();
	    			Paths.add((Dominio.Path)aux);
	            	}
	            return Paths;
	    } 
		catch (IOException ex) {
			//System.out.println(ex.getMessage());
			//System.out.println("No se puede iterar por los Paths");
			return null;
		}
	}

}
