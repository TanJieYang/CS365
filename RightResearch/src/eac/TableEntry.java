package eac;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
/**
 *
 * @author w.wong
 */
public class TableEntry {
    //private members
    private ArrayList<TableEntryPair> pairList;
    
    //public methods
    TableEntry(){
        pairList = new ArrayList<TableEntryPair>();
    }
    
    //insert element
    public void AddPair(String category, Element elem){
        TableEntryPair tmp = new TableEntryPair(category,elem);
        pairList.add(tmp);
    }
    //insert int element
    public void AddPair(String category, int i){
        Element e = new Element(i);
        AddPair(category, e);
    }
    //insert String element
    public void AddPair(String category, String s){
        Element e = new Element(s);
        AddPair(category, e);
    }
    //insert float element
    public void AddPair(String category, float f){
        Element e = new Element(f);
        AddPair(category, e);
    }
    
    //get list of elements
    //(need to return as final??)
    public final ArrayList<TableEntryPair> GetElementList(){
        return pairList;
    }
    
    //get list of categories in this entry
    public final ArrayList<String> GetCategoryList(){
        ArrayList<String> catList = new ArrayList<String>();
        for(TableEntryPair pair : pairList){
            catList.add(pair.category);
        }
        return catList;
    }
}
