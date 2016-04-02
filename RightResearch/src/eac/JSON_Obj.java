/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * java io/utils imports
 */
 
 package eac;
 
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * JSON imports
 */
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;



import java.io.FileWriter;

/**
 *
 * @author alex.chiam
 * @Notes: Not completed yet...
 */
public class JSON_Obj {
    
    public JSONObject   m_obj;
    public String       m_key;
    
    
    private JSON_Obj(JSONObject parent, String targetKey) {
                m_key = targetKey;
		m_obj = (JSONObject)parent.get(m_key);
    }
    //this function is constructor, i.e. JSONObject    
    public JSON_Obj(String _str) {
        //key names : "key", "Company Name", "Market", "Listed Date", 
        //            "Company", "Siblings"
		//http://stackoverflow.com/questions/15918861/how-to-get-data-from-json-object
		//http://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        
        try
        {
            JSONParser json_parser = new JSONParser();
            JSONObject m_obj = (JSONObject)json_parser.parse(_str);

            JSONArray keyArray = (JSONArray)m_obj.get("items");
	if(keyArray != null)
        {
            
            
            
            List<String> keyList = new ArrayList<String>();
            List<String> companyNameList = new ArrayList<String>();
            List<String> anncTypeList = new ArrayList<String>();
            List<String> exDateList = new ArrayList<String>();
            List<String> recordDataList = new ArrayList<String>();
            List<String> dataPayableList = new ArrayList<String>();
            List<String> particularList = new ArrayList<String>();
			
            for(int i = 0 ; i < keyArray.size()-1 ; i++) 
            {
                                
               JSONObject tmp = (JSONObject)keyArray.get(i);
                      
               
               keyList.add(((String)tmp.get("key")));
                     
               String companyName = (String)tmp.get("CompanyName");
               
               companyNameList.add(companyName);
               System.out.println(companyName);
                     
               anncTypeList.add(((String)tmp.get("Annc_Type")));
               exDateList.add(((String)tmp.get("Ex_Date")));
               recordDataList.add(((String)tmp.get("Record_Date")));
               dataPayableList.add(((String)tmp.get("DatePaid_Payable")));
               particularList.add(((String)tmp.get("Particulars")));
               
            }
            
            
             //writing to file
            FileWriter writer = new FileWriter("SGX_Corporate_Action.csv");
            //write table headers
            writer.append("Company Name");
            writer.append(',');
            writer.append("Type");
            writer.append(',');
            writer.append("Ex Date");
            writer.append(',');
            writer.append("Record Date");
            writer.append(',');
            writer.append("Date Paid/Payable");
            writer.append(',');
            writer.append("Particulars");
            writer.append('\n');
            
            for(int i =0; i < keyList.size(); ++i)
            {
                writer.append(companyNameList.get(i));
                writer.append(',');
                writer.append(anncTypeList.get(i));
                writer.append(',');
                writer.append(exDateList.get(i));
                writer.append(',');
                writer.append(recordDataList.get(i));
                writer.append(',');
                writer.append(dataPayableList.get(i));
                writer.append(',');
                writer.append(particularList.get(i));
                writer.append('\n');
                
            }
            
            writer.flush();
            writer.close();
	}
        
        //System.out.println();

        
        }
        catch (Exception e){

        }
        

       
        
        
    }
    
    /*@info
     * 
     */
    public boolean IsValid(){
     return false;
    }

    /*@info
     * 
     */
    public boolean IsTable() {
     return false;
    }

    /*@info
     * 
     */
    public boolean IsArray(){
     return false;
    }

    /*@info
     * 
     */
    public boolean IsInt(){
     return false;
    }

    /*@info
     * 
     */
    public boolean IsFloat() {
     return false;
    }

    /*@info
     * 
     */
    public boolean IsString(){
        
     return false;
    }

    /*@info
     * 
     */
    public boolean IsNull(){
     return false;
    }

    /*@info
     * 
     */
    public int GetInt() {
     return 1;
    }

    /*@info
     * 
     */
    public float GetFloat(){
     return 1.0f;
    }

    /*@info
     * 
     */
    public String GetString() {
        return (String)m_obj.get(m_key);
    }

    /*@info
     * 
     */
    public JSONObject GetJSONObject(String _str){
     JSONObject obj = new JSONObject();
     return obj;
    }

    /*@info
     * 
     */
    public JSONObject GetElementIndex(int _val){
     JSONObject obj = new JSONObject();
     return obj;
    }
    
}