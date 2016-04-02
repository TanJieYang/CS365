package eac;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author w.wong
 */
public class TableEntryPair {
    //public members
    public String category;
    public Element elem;
    
    //public methods
    //default ctor
    public TableEntryPair(){
        
    }
    //param ctor
    public TableEntryPair(String cat, Element e){
        category = cat;
        elem = e;
    }
}
