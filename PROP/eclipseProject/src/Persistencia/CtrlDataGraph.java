/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import Dominio.Graph;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author albert
 */
public class CtrlDataGraph {

    /**
     *
     */
    public CtrlDataGraph() {
    }

 

	/**
     *
     * @param filePath
     * @return
     */
    public boolean checkGraphFile(String filePath) {

        filePath += ".ser";
        File file = new File(filePath);
        return file.exists() && file.canRead();
    }

    /**
     *
     * @param g
     * @param filePath
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveGraph(Graph g, String filePath) throws FileNotFoundException, IOException {

        File file = new File(filePath);
        String name = file.getName();
        FileOutputStream fileOut = new FileOutputStream(filePath + ".ser");
        ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
        outStream.writeObject(g);
        outStream.close();
        fileOut.close();
    }

    /**
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Graph loadGraph(String filePath) throws FileNotFoundException, IOException {

        Graph g = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath+".ser");
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            g = (Graph) in.readObject();
            in.close();
            fileInputStream.close();
        } catch (ClassNotFoundException c) {
            //System.out.println("Graph class not found");
        }
        return g;
    }
}
