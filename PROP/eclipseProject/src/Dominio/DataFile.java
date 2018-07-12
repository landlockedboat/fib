/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dominio;

/**
 *
 * @author jferrer91
 */
public class DataFile {
    
    private final String fileName;
    private final Node.Type type;
    
    /**
     * 
     * @param fileName
     * @param type 
     */
    public DataFile(String fileName, Node.Type type) {
        this.fileName = fileName;
        this.type = type;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the type
     */
    public Node.Type getType() {
        return type;
    }
    
    
}
