/**
 * @author Victor Alcazar Lopez
**/

package Dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CtrlPaths {
	private Map<String, Path> paths;
	private Map<String, Boolean> modifiedPaths;

	public CtrlPaths() {
		paths = new HashMap<String, Path>();
		modifiedPaths = new HashMap<String, Boolean>();
		initDefaultPaths();
	}

	public CtrlPaths(ArrayList<Path> pathArray) {
		paths = new HashMap<String, Path>();
		modifiedPaths = new HashMap<String, Boolean>();
		initDefaultPaths();
		for (Path p : pathArray) {
			paths.put(p.getNom(), p);
			modifiedPaths.put(p.getNom(), false);
		}
	}

	public Path getPath(String pathName) {
		if (paths.containsKey(pathName))
			return paths.get(pathName);
		else
			//System.out.println("Path not found");
		return new Path();
	}
	
	public boolean existsPath(String pathName){
		return paths.containsKey(pathName);
	}

	public void setPath(String pathName, Path path) {
		if (paths.containsKey(pathName)) {
			paths.replace(pathName, path);
			modifiedPaths.replace(pathName, true);
		}

		else {
			//System.out.println("Path not found");
		}
	}

	public void modifyPath(String pathName, String newPathContent) {
		if (paths.containsKey(pathName)) {
			Path oldPath = paths.get(pathName);
			Path modifiedPath = new Path();
			modifiedPath.setContingut(newPathContent);
			modifiedPath.setDescripcio(oldPath.getDescripcio());
			modifiedPath.setNom(oldPath.getNom());
			paths.replace(pathName, modifiedPath);
			modifiedPaths.replace(pathName, true);
		} else {
			//System.out.println("Path not found");
		}
	}

	public void modifyPath(String pathName, String newPathContent, String newPathDescription) {
		if (paths.containsKey(pathName)) {
			Path oldPath = paths.get(pathName);
			Path modifiedPath = new Path();
			modifiedPath.setContingut(newPathContent);
			modifiedPath.setDescripcio(newPathDescription);
			modifiedPath.setNom(oldPath.getNom());
			paths.replace(pathName, modifiedPath);
			modifiedPaths.replace(pathName, true);
		} else {
			//System.out.println("Path not found");
		}
	}

	public void addPath(String pathContent, String pathName, String description) {
		if (!paths.containsKey(pathName)) {
			Path p = new Path();
			System.out.println(pathContent);
			p.setContingut(pathContent);
			p.setNom(pathName);
			p.setDescripcio(description);

			System.out.println(p.getContingut() + " " + p.getStringPath());
			paths.put(pathName, p);
			modifiedPaths.put(pathName, true);
		} else {
			//System.out.println("Path already exists");
		}
	}

	public void erasePath(String pathName) {
		if (paths.containsKey(pathName)) {
			// TODO Deeply problematic
			paths.remove(pathName);
			modifiedPaths.remove(pathName);
		} else {
			//System.out.println("Path not found");
		}
	}

	public ArrayList<String> getPathNames() {
		ArrayList<String> pathNames = new ArrayList<String>();
		for (Map.Entry<String, Path> entry : paths.entrySet()) {
			pathNames.add(entry.getKey());
		}
		return pathNames;
	}

	public String toString() {
		String ret = new String();
		for (Map.Entry<String, Path> entry : paths.entrySet()) {
			ret += entry.getValue().toString();
		}
		return ret;
	}

	public ArrayList<Path> getModifiedPaths() {
		ArrayList<Path> ret = new ArrayList<Path>();
		for (Map.Entry<String, Boolean> entry : modifiedPaths.entrySet()) {
			if (entry.getValue()) {
				ret.add(paths.get(entry.getKey()));
			}
		}
		return ret;
	}

	public void printPath(String pathName) {
		if (paths.containsKey(pathName))
			Utils.printPath(paths.get(pathName));
		else{
			//System.out.println("Path not found");
			}
	}

	public String getPathsFirstType(String pathName) {
		if (paths.containsKey(pathName)) {
			return paths.get(pathName).getContingut().get(0).toString();
		} else {
			//System.out.println("Path not found");
			return null;
		}
	}

	public String getPathsLastType(String pathName) {
		if (paths.containsKey(pathName)) {
			ArrayList<Node.Type> pathContent = paths.get(pathName).getContingut();
			return pathContent.get(pathContent.size() - 1).toString();
		} else {
			//System.out.println("Path not found");
			return null;
		}
	}
	
	/**
	 * Returns an array containing all the paths' info
	 * @return
	 * <p>Returns an ArrayList of ArrayLists of Strings containing the all paths' info in formatted as such</p>
	 * <i>Multiple rows</i> containing all the info associated with a path formatted like so:
	 * <ol>
	 * 	 <li>Path's name</li>
	 * 	 <li>Path's description</li>
	 * 	 <li>Path's Content</li>
	 * </ol>
	 */
	public ArrayList<ArrayList<String>> getFormattedPaths(){
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		for (Map.Entry<String, Path> entry : paths.entrySet()) {
			ArrayList<String> col = new ArrayList<String>();
			col.add(entry.getKey());
			col.add(entry.getValue().getDescripcio());
			col.add(entry.getValue().getStringPath());
			ret.add(col);
		}
		return ret;
	}

	private void initDefaultPaths() {
		Path p = new Path();
		p.setNom("APA");
		p.setContingut("APA");
		p.setDescripcio("Lorem ipsum dolor sit amet, consectetur adipiscing "
				+ "elit, sed do eiusmod tempor incididunt ut labore et"
				+ " dolore magna aliqua. Ut enim ad minim veniam, quis"
				+ " nostrud");
		paths.put("APA", p);
		modifiedPaths.put("APA", false);
		p = new Path();
		p.setNom("APC");
		p.setContingut("APC");
		p.setDescripcio("Lorem ipsum dolor sit amet, consectetur adipiscing "
				+ "elit, sed do eiusmod tempor incididunt ut labore et"
				+ " dolore magna aliqua. Ut enim ad minim veniam, quis"
				+ " nostrud");
		paths.put("APC", p);
		modifiedPaths.put("APC", false);
		p = new Path();
		p.setNom("APT");
		p.setContingut("APT");
		p.setDescripcio("Lorem ipsum dolor sit amet, consectetur adipiscing "
				+ "elit, sed do eiusmod tempor incididunt ut labore et"
				+ " dolore magna aliqua. Ut enim ad minim veniam, quis"
				+ " nostrud");
		paths.put("APT", p);
		modifiedPaths.put("APT", false);
	}

}
