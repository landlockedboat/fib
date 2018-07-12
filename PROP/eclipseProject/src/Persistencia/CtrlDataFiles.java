/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Dominio.Pair;
import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Ferrer Rodriguez, Jorge
 */
public class CtrlDataFiles {

    /**
     * Initializes the lists and filesDirectory contains filesPath.
     *
     */
    public CtrlDataFiles() {}

    /**
     * Load the information of each node type and adds it to the list.
     *
     * @param file
     * @return
     */
    public List<Pair<Integer, String>> getNodes(File file) throws FileNotFoundException {
        List<Pair<Integer, String>> node = new ArrayList<>();
        Scanner sc = new Scanner(file, "ISO-8859-1");
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] s = line.split("\t");
            Pair<Integer, String> p;
            p = new Pair(Integer.parseInt(s[0].trim()), s[1].trim());
            node.add(p);
        }
        return node;
    }

    /**
     * Load the relations and adds them to the list.
     *
     * @param file
     * @return
     */
    public List<Pair<Integer, Integer>> getRelations(File file) throws FileNotFoundException {
        return readIntIntFile(file);
    }

    /**
     * Load labels nodes and stores them in the lists.
     *
     * @param file
     * @return
     */
    public List<Pair<Integer, Integer>> getLabels(File file) throws FileNotFoundException {
        return readIntIntFile(file);
    }

    /**
     * Reads the first two Integer values of each line.
     * 
     * @param fileName
     * @return
     */
    private List<Pair<Integer, Integer>> readIntIntFile(File file) throws FileNotFoundException {
        List<Pair<Integer, Integer>> label = new ArrayList<>();
        Scanner sc = new Scanner(file, "ISO-8859-1");
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] s = line.split("\t");
            Pair<Integer, Integer> p;
            p = new Pair(Integer.parseInt(s[0].trim()),
                    Integer.parseInt(s[1].trim()));
            label.add(p);
        }
        sc.close();
        return label;
    }
}
