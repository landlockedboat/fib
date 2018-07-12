/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dominio;

import java.io.Serializable;

/**
 *
 * @author jferrer91
 */
public class Pair <F,S> implements Serializable{
    public F first;
    public S second;
    
    //Pre: 
    //Post: first = f, second = s
    public Pair(F first, S second){
        this.first = first;
        this.second = second;
    }
}