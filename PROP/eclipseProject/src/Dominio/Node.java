/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alejandro Ibáñez
 */

package Dominio;

import java.io.Serializable;

public class Node implements Serializable {
Type tipus;

Integer id;

String nom;

Label label;

private static final long serialVersionUID = 1L;

public enum Type { 
    Autor, Conferencia, Paper, Terme, MidElement
}

public enum Label {
Database, DataMining, AI, InformationRetrieval
}

public Node() {
}

public void initialize(Type tipus, Integer id, String nom) {
    this.tipus = tipus;
    this.id = id;
    this.nom = nom;
}

public Type getTipus() {
    return this.tipus;
}

public Integer getId() {
    return this.id;
}

public String getNom() {
    return this.nom;
}

public void setId(Integer id) {
    this.id = id;
} 

public void setNom(String nom) {
    this.nom = nom;
}

public Label getLabel() {
    return this.label;
}

public void setLabel(Label label) {
    this.label = label;
}

}
