package eac;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author w.wong
 */
public class Element {
    //private members
    int iVal;
    String sVal;
    float fVal;
    
    /*
     * element data type
     * 1 - int
     * 2 - String
     * 3 - float
     */
    char type;
    
    //public methods
    
    //ctor - int
    public Element(int i){
        iVal = i;
        type = 1;
    }
    //ctor - String
    public Element(String s){
        sVal = s;
        type = 2;
    }
    //ctor - float
    public Element(float f){
        fVal = f;
        type = 3;
    }
    
    //type id functions
    public boolean isInt(){
        if(type == 1) {
            return true;
        }
        return false;
    }
    public boolean isString(){
        if(type == 2) {
            return true;
        }
        return false;
    }
    public boolean isFloat(){
        if(type == 3) {
            return true;
        }
        return false;
    }
    
    //get functions
    public int getInt(){
        return iVal;
    }
    public String getString(){
        return sVal;
    }
    public float getFloat(){
        return fVal;
    }
}
