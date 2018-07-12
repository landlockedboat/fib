/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dominio;

import Persistencia.CtrlDataFiles;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import static java.util.Arrays.asList;

/**
 * @author Ferrer Rodríguez, Jorge
 * @version 1.0
 */
public class CtrlImport {

    private final HashMap<Node.Type, HashMap<Integer, Integer>> eqHashMap = new HashMap<>();
    private final Graph graph = new Graph();
    private final CtrlDataFiles data = new CtrlDataFiles();

    private final List<DataFile> nodesFiles = asList(new DataFile("author.txt", Node.Type.Autor),
            new DataFile("conf.txt", Node.Type.Conferencia),
            new DataFile("paper.txt", Node.Type.Paper),
            new DataFile("term.txt", Node.Type.Terme));

    private final List<DataFile> relationsFiles = asList(new DataFile("paper_author.txt", Node.Type.Autor),
            new DataFile("paper_conf.txt", Node.Type.Conferencia),
            new DataFile("paper_term.txt", Node.Type.Terme));

    private final List<DataFile> labelsFiles = asList(new DataFile("author_label.txt", Node.Type.Autor),
            new DataFile("conf_label.txt", Node.Type.Conferencia),
            new DataFile("paper_label.txt", Node.Type.Paper));

    //Meaning of the labels: 0: Database; 1: Data Mining; 2: AI; 3: Information Retrieval;
    private final Node.Label[] labelType = new Node.Label[]{Node.Label.Database,
        Node.Label.DataMining,
        Node.Label.AI,
        Node.Label.InformationRetrieval};

    private final String filesDirectory;

    /**
     * Creates a data controller with the path filesPath
     *
     * @param filesPath     
     * @throws IllegalArgumentException: filesPath isn't a directory.
     */
    public CtrlImport(String filesPath) throws IllegalArgumentException {
        File directory = new File(filesPath);
        if (!directory.isDirectory())
            throw new IllegalArgumentException(filesPath + " is not a directory");
        else filesDirectory = filesPath + File.separator;
    }

    /**
     * Load all the information on the graph
     *
     */
    public void loadGraphInfo() throws IOException {
        addNodes();
        addRelations();
        addLabelNodes();
    }

    /**
     * Add all the information of each node type
     *
     */
    private void addNodes() throws IOException {
        for (DataFile f : nodesFiles) {
            File file = new File(filesDirectory + f.getFileName());
            List<Pair<Integer, String>> node = data.getNodes(file);
            HashMap<Integer, Integer> eqNode = new HashMap<>();
            
            node.stream().forEach((p) -> {
                int eqID = graph.addNode(f.getType(), p.second);
                eqNode.put(p.first, eqID);
            });
            eqHashMap.put(f.getType(), eqNode);
        }
    }

    /**
     * If the nodes involved in the relation exist in the graph, then the the
     * information of the relation is added.
     * 
     */
    private void addRelations() throws IOException {
        for (DataFile f : relationsFiles) {
            File file = new File(filesDirectory + f.getFileName());
            List<Pair<Integer, Integer>> rel = data.getRelations(file);

            rel.stream().forEach((p) -> {
                int eqNode01 = eqHashMap.get(Node.Type.Paper).get(p.first);
                int eqNode02 = eqHashMap.get(f.getType()).get(p.second);
                graph.setArc(eqNode01, eqNode02, f.getType());
            });
        }
    }

    /**
     * Add the node with its label, if previously exists in the graph
     * 
     */
    private void addLabelNodes() throws IOException {
        for (DataFile f : labelsFiles) {
            File file = new File(filesDirectory + f.getFileName());
            List<Pair<Integer, Integer>> label = data.getLabels(file);

            label.stream().forEach((p) -> {
                int eqID = eqHashMap.get(f.getType()).get(p.first);
                graph.addLabel(eqID, labelType[p.second], f.getType());
            });
        }
    }

    /**
     * 
     * @return
     */
    public Graph getGraph() {
        return graph;
    }
}
