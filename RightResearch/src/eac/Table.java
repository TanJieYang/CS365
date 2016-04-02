
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
public class Table {
    //private members
    private ArrayList<TableEntry> entryList;
    
    //public methods
    //ctor
    Table(){
        entryList = new ArrayList<TableEntry>();
    }
    
    //add table entry
    public void AddEntry(TableEntry entry){
        entryList.add(entry);
    }
    
    //get table entries
    public final ArrayList<TableEntry> GetEntryList(){
        return entryList;
    }
}
